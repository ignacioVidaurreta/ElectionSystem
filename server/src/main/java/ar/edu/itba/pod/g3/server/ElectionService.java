package ar.edu.itba.pod.g3.server;

import ar.edu.itba.pod.g3.enums.ElectionState;
import ar.edu.itba.pod.g3.interfaces.NotificationConsumer;
import ar.edu.itba.pod.g3.models.QueryDescriptor;
import ar.edu.itba.pod.g3.models.Vote;
import ar.edu.itba.pod.g3.server.interfaces.AdministrationService;
import ar.edu.itba.pod.g3.server.interfaces.FiscalizationService;
import ar.edu.itba.pod.g3.server.interfaces.QueryService;
import ar.edu.itba.pod.g3.server.interfaces.VotingService;
import ar.edu.itba.pod.g3.server.model.Election;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;

/**
 * Service implementation for all service types
 */
public class ElectionService implements AdministrationService, VotingService, QueryService, FiscalizationService {
    private static final Logger logger = LoggerFactory.getLogger(ElectionService.class);

    private final Election election;

    public ElectionService(Election election) {
        this.election = election;
        logger.info("instantiated ElectionService");
    }

    /************************************************************************
     ************************ Administration Service ************************
     ************************************************************************/
    @Override
    public boolean openElection() throws RemoteException {
        return election.setElectionState(ElectionState.OPEN);
    }

    @Override
    public boolean closeElection() throws RemoteException {
        return election.setElectionState(ElectionState.CLOSED);
    }

    @Override
    public ElectionState consultElectionState() throws RemoteException {
        return election.getElectionState();
    }

    /************************************************************************
     **************************** Voting Service ****************************
     ************************************************************************/
    @Override
    public boolean emitVote(Vote vote) throws Exception {
        return election.addVote(vote);
    }

    /************************************************************************
     **************************** Query Service *****************************
     ************************************************************************/
    @Override
    public String executeQuery(QueryDescriptor descriptor) throws Exception {
        return election.queryElection(descriptor);
    }

    /************************************************************************
     ************************* Fiscalization Service ************************
     ************************************************************************/
    @Override
    public boolean registerFiscal(NotificationConsumer notificationConsumer) throws Exception {
        return election.addNotificationConsumer(notificationConsumer);
    }
}
