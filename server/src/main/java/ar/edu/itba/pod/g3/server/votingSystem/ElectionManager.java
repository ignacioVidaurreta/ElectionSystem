package ar.edu.itba.pod.g3.server.votingSystem;

import ar.edu.itba.pod.g3.enums.ElectionState;
import ar.edu.itba.pod.g3.enums.PoliticalParty;
import ar.edu.itba.pod.g3.enums.Province;
import ar.edu.itba.pod.g3.enums.QueryType;
import ar.edu.itba.pod.g3.models.ElectionException;
import ar.edu.itba.pod.g3.models.Fiscal;
import ar.edu.itba.pod.g3.models.QueryDescriptor;
import ar.edu.itba.pod.g3.models.Vote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

// This class will take care of handling the queries
public class ElectionManager {


    private static final Logger logger = LoggerFactory.getLogger(ElectionManager.class);
    private final Lock readlock;
    private final Lock writeLock;

    ElectionState electionState;

    private final List<Vote> votes = new ArrayList<>();
    private final Map<PoliticalParty, Map<Integer, List<Fiscal>>> fiscalMap = new EnumMap<>(PoliticalParty.class);
    private final Object[] fiscalMapLocks = new Object[7];

    private void populateFiscalMap(Map<PoliticalParty, Map<Integer, List<Fiscal>>> fiscalMap) {
        for(PoliticalParty politicalParty: PoliticalParty.values()) {
            fiscalMap.put(politicalParty, new HashMap<>());
        }
    }

    public ElectionManager() {
        // Create a reentrant RWlock with fairness true
        ReadWriteLock rwLock = new ReentrantReadWriteLock(true);
        readlock = rwLock.readLock();
        writeLock = rwLock.writeLock();
        populateFiscalMap(fiscalMap);
    }

    public boolean setElectionState(ElectionState state) {
        this.electionState = state;
        return true;
    }

    public ElectionState getElectionState() {
        return electionState;
    }

    private void emitNotifications(Collection<Vote> newVotes) {
        for(Vote vote: newVotes) {
            Map<Integer, List<Fiscal>> partyMap = fiscalMap.get(vote.getFptpWinner());
            List<Fiscal> fiscals = partyMap.get(vote.getBooth());
            if (fiscals == null) {
                return;
            }
            for (Fiscal fiscal : fiscals) {
                fiscal.notify(vote);
            }
        }
    }


    public boolean addVotes(Collection<Vote> newVotes) throws ElectionException {
        writeLock.lock();
        switch (electionState) {
            case NOT_STARTED:
                writeLock.unlock();
                throw new ElectionException("Cannot add votes to an election that has not started");
            case OPEN:
                votes.addAll(newVotes);
                emitNotifications(newVotes);
                writeLock.unlock();
            case CLOSED:
                writeLock.unlock();
                throw new ElectionException("Cannot add votes to an election that has been closed");
        }
        writeLock.unlock();
        return false;
    }

    public boolean addFiscal(Fiscal fiscal) {
        synchronized (fiscalMapLocks[fiscal.getParty().ordinal()]) {

            Map<Integer, List<Fiscal>> partyMap = fiscalMap.get(fiscal.getParty());
            if (partyMap.containsKey(fiscal.getBooth())) {
                List<Fiscal> fiscals = partyMap.get(fiscal.getBooth());
                fiscals.add(fiscal);
            } else {
                List<Fiscal> fiscals = Collections.singletonList(fiscal);
                partyMap.put(fiscal.getBooth(), fiscals);
            }
            return true;
        }
    }

    public String queryElection(QueryDescriptor descriptor) throws Exception {
        readlock.lock();
        QueryType queryType = descriptor.getType();
        String id = descriptor.getId();
        ElectionResults electionResults = null;
        switch (queryType) {
            case BOOTH:
                int boothId = Integer.parseInt(id);
                try{
                    electionResults = queryBooth(boothId);
                }
                catch(ElectionException e) {
                    readlock.unlock();
                    throw e;
                }
                break;
            case PROVINCIAL:
                Province province = Province.valueOf(id);
                try{
                    electionResults = queryProvince(province);
                }
                catch(ElectionException e) {
                    readlock.unlock();
                    throw e;
                }
                break;
            case NATIONAL:
                try{
                    electionResults = queryNational();
                }
                catch(ElectionException e) {
                    readlock.unlock();
                    throw e;
                }
        }
        readlock.unlock();
        return electionResults.toString();
    }

    private ElectionResults queryBooth(int boothId) throws Exception {
        if (electionState == ElectionState.NOT_STARTED) {
            throw new ElectionException("The election has not started");
        }

        List<Vote> votesForProvince = votes .stream()
                                            .filter(v -> v.getBooth() == boothId)
                                            .collect(Collectors.toList());

        if (electionState == ElectionState.OPEN || electionState == ElectionState.CLOSED) {
            return new FPTPSystem(votesForProvince).getResults();
        }
        else {
            throw new Exception("election is in an invalid state");
        }
    }

    private ElectionResults queryProvince(Province province) throws Exception {
        if (electionState == ElectionState.NOT_STARTED) {
            throw new ElectionException("The election has not started");
        }

        List<Vote> votesForProvince = votes .stream()
                                            .filter(v -> v.getProvince() == province)
                                            .collect(Collectors.toList());

        if (electionState == ElectionState.OPEN) {
            return new FPTPSystem(votesForProvince).getResults();
        }
        else if (electionState == ElectionState.CLOSED) {
            return new SPAVSystem(votes).getResults();
        }
        else {
            throw new Exception("election is in an invalid state");
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
        throw new Exception("election is in an invalid state");
    }

}
