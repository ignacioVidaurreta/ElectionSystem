package ar.edu.itba.pod.g3.client;

import ar.edu.itba.pod.g3.client.interfaces.ManagementClient;
import ar.edu.itba.pod.g3.interfaces.FiscalizationService;
import ar.edu.itba.pod.g3.models.Fiscal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Properties;

import static ar.edu.itba.pod.g3.enums.ServiceName.MANAGEMENT;

public class FiscalizationClient extends Client {
    private static Logger logger = LoggerFactory.getLogger(FiscalizationClient.class);
    public static void main(final String[] args){
        logger.info("Initializing FiscalizationClient...");
        Properties properties = System.getProperties();
        try {
            if(containsValidArguments(properties)){
                FiscalizationService client = (FiscalizationService) getRemoteService(properties.getProperty("serverAddress"), MANAGEMENT);
                createAndRegisterFiscal(client,properties.getProperty("id"), properties.getProperty("party"));
            }else {
                logger.error("Invalid arguments. -Daction and -DserverAddress arguments must be present");
            }
        }catch (RemoteException | NotBoundException ex){
            ex.printStackTrace();
        }
    }

    /* protected */ static boolean containsValidArguments(Properties properties){
        return Client.containsValidArguments(properties) && properties.containsKey("id")
                && properties.containsKey("party");
    }

    private static void createAndRegisterFiscal(FiscalizationService client, final String id, final String party) throws RemoteException{
        Fiscal fiscal = new Fiscal(id, party);
        //client.registerFiscal(fiscal);
        System.out.println(String.format("Fiscal of %s registered on polling place %s", fiscal.getParty(), fiscal.getId()));
    }
}
