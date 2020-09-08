package ar.edu.itba.pod.g3.server;

import ar.edu.itba.pod.g3.enums.ServiceName;
import ar.edu.itba.pod.g3.server.interfaces.AdministrationService;
import ar.edu.itba.pod.g3.server.interfaces.FiscalizationService;
import ar.edu.itba.pod.g3.server.model.Election;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) throws RemoteException {
        logger.info("election-system Server Starting ...");

        // Create registry
        final Registry registry = LocateRegistry.getRegistry();

        // Publish service
        final Election election = new Election();
        final ElectionService electionService = new ElectionService(election);
        final Remote remote = UnicastRemoteObject.exportObject(electionService, 0);
        registry.rebind(ServiceName.VOTE.getServiceName(), remote);
        registry.rebind(ServiceName.MANAGEMENT.getServiceName(), remote);
        registry.rebind(ServiceName.QUERY.getServiceName(), remote);
        registry.rebind(ServiceName.FISCALIZATION.getServiceName(), remote);


        logger.info("Published administration service");

    }
}
