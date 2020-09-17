package ar.edu.itba.pod.g3.server.votingSystem;

import ar.edu.itba.pod.g3.api.enums.ElectionState;
import ar.edu.itba.pod.g3.api.enums.PoliticalParty;
import ar.edu.itba.pod.g3.api.enums.Province;
import ar.edu.itba.pod.g3.api.enums.QueryType;
import ar.edu.itba.pod.g3.api.interfaces.NotificationConsumer;
import ar.edu.itba.pod.g3.api.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

// This class will take care of handling the queries
public class ElectionManager {


    private static final Logger logger = LoggerFactory.getLogger(ElectionManager.class);
    private final Lock readLock;
    private final Lock writeLock;

    ElectionState electionState;

    private final List<Vote> votes = new ArrayList<>();
    private final Map<PoliticalParty, Map<Integer, List<NotificationConsumer>>> fiscalMap = new EnumMap<>(PoliticalParty.class);
    private final Object[] fiscalMapLocks = new Object[7];
    private ExecutorService executorService;

    private final Map<Integer, ElectionResults> finalBoothResults = new HashMap<>();
    private final Map<Province, ElectionResults> finalProvincialResults = new EnumMap<>(Province.class);

    private void populateFiscalMap(Map<PoliticalParty, Map<Integer, List<NotificationConsumer>>> fiscalMap) {
        for(PoliticalParty politicalParty: PoliticalParty.values()) {
            fiscalMap.put(politicalParty, new HashMap<>());
        }
    }

    public ElectionManager() {
        // Create a reentrant RWLock with fairness true
        ReadWriteLock rwLock = new ReentrantReadWriteLock(true);
        readLock = rwLock.readLock();
        writeLock = rwLock.writeLock();
        populateFiscalMap(fiscalMap);
        this.electionState = ElectionState.NOT_STARTED;
        for (int i = 0; i < fiscalMapLocks.length; i++) {
            fiscalMapLocks[i] = new Object();
        }
        executorService = Executors.newCachedThreadPool();
    }

    public Map<PoliticalParty, Map<Integer, List<NotificationConsumer>>> getFiscalMap() {
        return fiscalMap;
    }

    public boolean setElectionState(ElectionState state) {
        if (this.electionState == ElectionState.NOT_STARTED && state == ElectionState.OPEN ||
            this.electionState == ElectionState.OPEN        && state == ElectionState.CLOSED) {
            this.electionState = state;
            return true;
        }
        throw new IllegalStateException(String.format("Invalid transition from %s to %s", this.electionState, state));

    }

    public ElectionState getElectionState() {
        return electionState;
    }

    private void emitNotifications(Collection<Vote> newVotes) throws RemoteException {
        for(Vote vote: newVotes) {
            Map<Integer, List<NotificationConsumer>> partyMap = fiscalMap.get(vote.getFptpWinner());
            List<NotificationConsumer> fiscals = partyMap.get(vote.getBooth());
            if (fiscals == null) {
                return;
            }
            for (NotificationConsumer fiscal : fiscals) {
                fiscal.notifyFiscal(vote);
            }
        }
    }


    public boolean addVotes(Collection<Vote> newVotes) throws ElectionException, NoVotesException, IllegalArgumentException, RemoteException {
        if (newVotes == null) {
            throw new IllegalArgumentException();
        }
        if (newVotes.isEmpty()) {
            throw new NoVotesException();
        }
        writeLock.lock();
        switch (electionState) {
            case NOT_STARTED:
                writeLock.unlock();
                throw new ElectionException("Cannot add votes to an election that has not started");
            case OPEN:
                votes.addAll(newVotes);
                writeLock.unlock();
                newVotes.forEach((vote) -> {
                    Optional.ofNullable(this.fiscalMap.get(vote.getFptpWinner()).get(vote.getBooth())).ifPresent(fiscals -> {
                        fiscals.forEach((fiscal)-> {
                            try {
                                fiscal.notifyFiscal(vote);
                            }catch (RemoteException ex){
                                ex.printStackTrace();
                            }
                        });
                    });
                    logger.info("Fiscals notified");
                });

                emitNotifications(newVotes);
                break;
            case CLOSED:
                writeLock.unlock();
                throw new ElectionException("Cannot add votes to an election that has been closed");
        }
        return true;
    }

    public boolean addFiscal(NotificationConsumer fiscal) throws IllegalStateException, RemoteException {
        if (electionState != ElectionState.NOT_STARTED) {
            throw new IllegalStateException("Fiscals cannot be added after election has begun.");
        }
        synchronized (fiscalMapLocks[fiscal.getParty().ordinal()]) {

            Map<Integer, List<NotificationConsumer>> partyMap = fiscalMap.get(fiscal.getParty());
            if (partyMap.containsKey(fiscal.getBooth())) {
                List<NotificationConsumer> fiscals = partyMap.get(fiscal.getBooth());
                fiscals.add(fiscal);
            } else {
                List<NotificationConsumer> fiscals = Collections.singletonList(fiscal);
                partyMap.put(fiscal.getBooth(), fiscals);
            }
            return true;
        }
    }

    public String queryElection(QueryDescriptor descriptor) throws Exception {
        readLock.lock();
        QueryType queryType = descriptor.getType();
        String id = descriptor.getId();
        ElectionResults electionResults = null;
        switch (queryType) {
            case BOOTH:
                int boothId = Integer.parseInt(id);
                try{
                    electionResults = queryBooth(boothId);
                }
                catch(ElectionException | NoVotesException e) {
                    readLock.unlock();
                    throw e;
                }
                break;
            case PROVINCIAL:
                Province province = Province.valueOf(id);
                try{
                    electionResults = queryProvince(province);
                }
                catch(ElectionException | NoVotesException e) {
                    readLock.unlock();
                    throw e;
                }
                break;
            case NATIONAL:
                try{
                    electionResults = queryNational();
                }
                catch(ElectionException | NoVotesException e) {
                    readLock.unlock();
                    throw e;
                }
        }
        readLock.unlock();
        return electionResults.serialize(electionState == ElectionState.CLOSED, queryType);
    }

    private ElectionResults queryBooth(int boothId) throws Exception {
        if (electionState == ElectionState.NOT_STARTED) {
            throw new ElectionException("The election has not started");
        }

        if (electionState == ElectionState.CLOSED) {
            if (finalBoothResults.containsKey(boothId)) {
                return finalBoothResults.get(boothId);
            } else {
                List<Vote> votesForBooth = votes.stream()
                                                .filter(v -> v.getBooth() == boothId)
                                                .collect(Collectors.toList());
                ElectionResults results = new FPTPSystem(votesForBooth).getResults();
                finalBoothResults.put(boothId, results);
                return results;
            }
        }

        if (electionState == ElectionState.OPEN) {
            List<Vote> votesForBooth = votes.stream()
                                            .filter(v -> v.getBooth() == boothId)
                                            .collect(Collectors.toList());
            return new FPTPSystem(votesForBooth).getResults();
        }
        else {
            throw new IllegalStateException("election is in an invalid state");
        }
    }

    private ElectionResults queryProvince(Province province) throws Exception {
        if (electionState == ElectionState.NOT_STARTED) {
            throw new ElectionException("The election has not started");
        }

        if (electionState == ElectionState.CLOSED) {
            if (finalProvincialResults.containsKey(province)) {
                return finalProvincialResults.get(province);
            }
            else {
                List<Vote> votesForProvince = votes .stream()
                                                    .filter(v -> v.getProvince() == province)
                                                    .collect(Collectors.toList());
                ElectionResults results = new SPAVSystem(votesForProvince).getResults();
                finalProvincialResults.put(province, results);
                return results;
            }
        }

        else if (electionState == ElectionState.OPEN) {
            List<Vote> votesForProvince = votes .stream()
                                                .filter(v -> v.getProvince() == province)
                                                .collect(Collectors.toList());
            return new FPTPSystem(votesForProvince).getResults();
        }

        else {
            throw new IllegalStateException("election is in an invalid state");
        }
    }

    private ElectionResults queryNational() throws Exception {
        switch (electionState) {
            case NOT_STARTED:
                throw new ElectionException("The election has not started");
            case OPEN:
                return new FPTPSystem(votes).getResults();
            case CLOSED:
                return new STARSystem(votes).getResults();
        }
        throw new IllegalStateException("election is in an invalid state");
    }

}
