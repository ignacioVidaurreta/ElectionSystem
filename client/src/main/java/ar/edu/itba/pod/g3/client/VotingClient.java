package ar.edu.itba.pod.g3.client;

import ar.edu.itba.pod.g3.client.utils.VoteParser;
import ar.edu.itba.pod.g3.interfaces.VotingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.util.Properties;

import static ar.edu.itba.pod.g3.enums.ServiceName.VOTE;

/**
 * Client that runs the voting client.
 * Used to emit votes
 */
public class VotingClient extends Client{
    private static Logger logger = LoggerFactory.getLogger(VotingClient.class);
    public static void main(String[] str){
        logger.info("Initializing VotingClient...");
        Properties properties = System.getProperties();
        try {
            if(containsValidArguments(properties)){
                //VotingService client = (VotingService) getRemoteService(properties.getProperty("serverAddress"), VOTE);
                executeAction(null, properties.getProperty("votesPath"));
            }else {
                System.out.println("Not found");
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    /* protected */ static boolean containsValidArguments(Properties properties){
        return Client.containsValidArguments(properties) && properties.containsKey("votesPath");
    }

    private static void executeAction(VotingService client, String votePath){
        VoteParser parser = new VoteParser(votePath);
        parser.parseVotes();
        parser.getParsedVotes().forEach(vote -> {
            System.out.println(vote.getTable());
            System.out.println(vote.getProvince());
            System.out.println(vote.getRanking());
            System.out.println(vote.getFptpWinner());

            System.out.println("---------------------------");
        });
        /*
        try {
            client.submitVotes(parser.getParsedVotes());
        }catch (RemoteException ex){
            ex.printStackTrace();
        }
         */
    }
}
