package ar.edu.itba.pod.g3.client;

import static ar.edu.itba.pod.g3.api.enums.ServiceName.QUERY;

import ar.edu.itba.pod.g3.api.enums.QueryType;
import ar.edu.itba.pod.g3.api.interfaces.QueryService;
import ar.edu.itba.pod.g3.api.models.ElectionException;
import ar.edu.itba.pod.g3.api.models.NoVotesException;
import ar.edu.itba.pod.g3.api.models.QueryDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.NoSuchElementException;
import java.util.Properties;

import static ar.edu.itba.pod.g3.client.Client.getRemoteService;

public class QueryClient {
    private static Logger logger = LoggerFactory.getLogger(QueryClient.class);
    public static void main(String[] str){
        logger.info("Initializing QueryClient...");
        Properties properties = System.getProperties();
        try {
            if (containsValidArguments(properties)) {
                QueryService client = (QueryService) getRemoteService(properties.getProperty("serverAddress"), QUERY);
                QueryDescriptor descriptor = generateQueryDescriptor(properties);
                String result = client.executeQuery(descriptor);

                writeToFile(result, properties.getProperty("outPath"));

            } else {
                System.out.println("Not found");
            }
        } catch (RemoteException | NotBoundException rex) {
            logger.error(String.format("(%s): Remote Exception Occurred", rex));
            rex.printStackTrace();
        } catch (IOException ex) {
            logger.error(String.format("(%s): IOException occurred", ex));
            ex.printStackTrace();
        } catch (NoVotesException noVotesException) {
            System.out.println("Displaying results for " + noVotesException.getMessage() + " system.\n");
            System.out.println("No votes");
        } catch (ElectionException electionException) {
            System.out.println("Election has not begun yet.");
        } catch (Exception ex) {
            logger.error(String.format("(%s): Exception occurred", ex));
            ex.printStackTrace();
        }
    }

    private static void writeToFile(String result, String outputPath) throws IOException {
        FileWriter writer = new FileWriter(outputPath);
        writer.write(result);
        writer.close();
    }

    /* protected */ static boolean containsValidArguments(Properties properties){
        return Client.containsValidArguments(properties) && properties.containsKey("outPath");
    }

    private static QueryDescriptor generateQueryDescriptor(Properties properties){
        QueryType type = getQueryType(properties.containsKey("state"), properties.containsKey("id"));
        String id;
        switch (type){
            case PROVINCIAL:
                id = properties.getProperty("state");
                break;
            case BOOTH:
                id = properties.getProperty("id");
                break;
            case NATIONAL:
            default:
                id = null;
        }

        return new QueryDescriptor(id, type);
    }

    private static QueryType getQueryType(boolean hasState, boolean hasPollingPlace){

        if(hasState) return QueryType.PROVINCIAL;

        if(hasPollingPlace) return QueryType.BOOTH;

        return QueryType.NATIONAL;
    }

}
