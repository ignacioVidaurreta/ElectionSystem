package ar.edu.itba.pod.g3.client;

import ar.edu.itba.pod.g3.api.interfaces.ManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Properties;

import static ar.edu.itba.pod.g3.api.enums.ServiceName.MANAGEMENT;


/**
 * Client that runs the Management Client
 */
public class ElectionManagementClient extends Client{
    private static Logger logger = LoggerFactory.getLogger(ElectionManagementClient.class);
    public static void main(final String[] args){
        logger.info("Initializing ElectionManagementClient...");
        Properties properties = System.getProperties();
        try {
            if(properties.containsKey("action") && properties.containsKey("serverAddress")) {
                ManagementService remote = (ManagementService) getRemoteService(properties.getProperty("serverAddress"), MANAGEMENT);
                executeAction(remote, properties.getProperty("action"));
            } else {
                logger.error("Invalid arguments. -Daction and -DserverAddress arguments must be present");
            }
        } catch (RemoteException | NotBoundException | MalformedURLException ex){
            ex.printStackTrace();
        }
    }

    private static void executeAction(ManagementService remote, String action) throws RemoteException {
        switch (action) {
            case "open":
                logger.info("Opening elections");
                try {
                    boolean state = remote.openElection();
                    System.out.println(state);
                    System.out.println("ELECTION STARTED");
                } catch (IllegalStateException illegalStateException) {
                    System.out.println(illegalStateException.getMessage());
                }
                break;
            case "close":
                logger.info("Closing elections");
                try {
                    remote.closeElection();
                    System.out.println("ELECTION CLOSED");
                } catch (IllegalStateException illegalStateException) {
                    System.out.println(illegalStateException.getMessage());
                }
                break;
            case "state":
                logger.info("Querying for election state");
                System.out.println(remote.consultElectionState());
                break;
            default:
                logger.error("Unknown action");
                System.exit(1);
        }
    }

}
