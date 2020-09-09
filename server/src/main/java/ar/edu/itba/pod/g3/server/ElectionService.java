package ar.edu.itba.pod.g3.server;

import ar.edu.itba.pod.g3.enums.ElectionState;
import ar.edu.itba.pod.g3.interfaces.NotificationConsumer;
import ar.edu.itba.pod.g3.models.QueryDescriptor;
import ar.edu.itba.pod.g3.models.Vote;
import ar.edu.itba.pod.g3.server.interfaces.AdministrationService;
import ar.edu.itba.pod.g3.server.interfaces.FiscalizationService;
import ar.edu.itba.pod.g3.server.interfaces.QueryService;
import ar.edu.itba.pod.g3.server.interfaces.VotingService;
import ar.edu.itba.pod.g3.server.votingSystem.ElectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.util.Collection;

/**
 * Service implementation for all service types
 */
public class ElectionService implements AdministrationService, VotingService, QueryService, FiscalizationService {
    private static final Logger logger = LoggerFactory.getLogger(ElectionService.class);

    private final ElectionManager electionManager;

    public ElectionService(ElectionManager electionManager) {
        this.electionManager = electionManager;
        logger.info("instantiated ElectionService");
    }

    /************************************************************************
     ************************ Administration Service ************************
     ************************************************************************/
    @Override
    public boolean openElection() throws RemoteException {
        return electionManager.setElectionState(ElectionState.OPEN);
    }

    @Override
    public boolean closeElection() throws RemoteException {
        return electionManager.setElectionState(ElectionState.CLOSED);
    }

    @Override
    public ElectionState consultElectionState() throws RemoteException {
        return electionManager.getElectionState();
    }

    /************************************************************************
     **************************** Voting Service ****************************
     ************************************************************************/
    @Override
    public boolean emitVote(Collection<Vote> vote) throws Exception {
        return electionManager.addVotes(vote);
    }

    /************************************************************************
     **************************** Query Service *****************************
     ************************************************************************/
    @Override
    public String executeQuery(QueryDescriptor descriptor) throws Exception {
        return electionManager.queryElection(descriptor);
    }

    /************************************************************************
     ************************* Fiscalization Service ************************
     ************************************************************************/
    @Override
    public boolean registerFiscal(NotificationConsumer notificationConsumer) throws Exception {
        return electionManager.addNotificationConsumer(notificationConsumer);
    }
}
