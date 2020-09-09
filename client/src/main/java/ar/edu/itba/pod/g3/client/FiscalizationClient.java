package ar.edu.itba.pod.g3.client;

import ar.edu.itba.pod.g3.client.interfaces.ManagementClient;
import ar.edu.itba.pod.g3.enums.PoliticalParty;
import ar.edu.itba.pod.g3.interfaces.FiscalizationService;
import ar.edu.itba.pod.g3.models.Fiscal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Properties;

import static ar.edu.itba.pod.g3.enums.ServiceName.FISCALIZATION;
import static ar.edu.itba.pod.g3.enums.ServiceName.MANAGEMENT;

public class FiscalizationClient extends Client {
    private static Logger logger = LoggerFactory.getLogger(FiscalizationClient.class);
    public static void main(final String[] args){
        logger.info("Initializing FiscalizationClient...");
        Properties properties = System.getProperties();
        try {
            if(containsValidArguments(properties)){
                FiscalizationService remote = (FiscalizationService) getRemoteService(properties.getProperty("serverAddress"), FISCALIZATION);
                createAndRegisterFiscal(remote,properties.getProperty("id"), properties.getProperty("party"));
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

    private static void createAndRegisterFiscal(FiscalizationService remote, final String booth, final String party) throws RemoteException{
        Fiscal fiscal = new Fiscal(Integer.parseInt(booth), PoliticalParty.valueOf(party));
        //remote.registerFiscal(fiscal);
        System.out.println(String.format("Fiscal of %s registered on polling place %d", fiscal.getParty(), fiscal.getBooth()));
    }
}
