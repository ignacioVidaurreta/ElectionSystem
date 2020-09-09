package ar.edu.itba.pod.g3.client;

import ar.edu.itba.pod.g3.client.interfaces.ManagementClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Properties;

import static ar.edu.itba.pod.g3.enums.ServiceName.MANAGEMENT;


/**
 * Client that runs the Management Client
 */
public class ElectionManagementClient extends Client{
    private static Logger logger = LoggerFactory.getLogger(ElectionManagementClient.class);
    public static void main(final String[] args){
        logger.info("Initializing ElectionManagementClient...");
        Properties properties = System.getProperties();
        try {
            if(properties.containsKey("action") && properties.containsKey("serverAddress")){
                ManagementClient remote = (ManagementClient) getRemoteService(properties.getProperty("serverAddress"), MANAGEMENT);
                executeAction(remote, properties.getProperty("action"));
            }else {
                logger.error("Invalid arguments. -Daction and -DserverAddress arguments must be present");
            }
        }catch (RemoteException | NotBoundException ex){
            ex.printStackTrace();
        }
    }

    private static void executeAction(ManagementClient client, String action) throws RemoteException{
        switch (action){
            case "open":
                logger.info("Opening elections");
                //remote.open();
                System.out.println("ELECTION STARTED");
                break;
            case "close":
                logger.info("Closing elections");
                //remote.close();
                System.out.println("ELECTION CLOSED");
                break;
            case "state":
                logger.info("Querying for election state");
                //remote.getState();
                break;
            default:
                logger.error("Unknown action");
                //System.exit(1);

        }
    }

}
