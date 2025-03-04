package ar.edu.itba.pod.g3.client;

import ar.edu.itba.pod.g3.api.models.ElectionException;
import ar.edu.itba.pod.g3.client.utils.VoteParser;
import ar.edu.itba.pod.g3.api.interfaces.VotingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.util.Properties;

import static ar.edu.itba.pod.g3.api.enums.ServiceName.VOTE;

/**
 * Client that runs the voting client.
 * Used to emit votes
 */
public class VotingClient extends Client {
    private static final Logger logger = LoggerFactory.getLogger(VotingClient.class);

    public static void main(String[] str) {
        logger.info("Initializing VotingClient...");
        Properties properties = System.getProperties();
        try {
            if (containsValidArguments(properties)) {
                VotingService client = (VotingService) getRemoteService(properties.getProperty("serverAddress"), VOTE);
                executeAction(client, properties.getProperty("votesPath"));
            } else {
                System.out.println("Not found");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /* protected */ static boolean containsValidArguments(Properties properties) {
        return Client.containsValidArguments(properties) && properties.containsKey("votesPath");
    }

    private static void executeAction(VotingService client, String votePath) {
        VoteParser parser = new VoteParser(votePath);
        parser.parseVotes();
        try {
            client.emitVotes(parser.getParsedVotes());
        } catch (ElectionException electionException) {
            System.out.println("No votes cast! Votes can only be submitted to open elections.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
