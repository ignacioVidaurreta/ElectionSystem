package ar.edu.itba.pod.g3.client;

import ar.edu.itba.pod.g3.client.utils.VoteParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

import static ar.edu.itba.pod.g3.enums.ServiceName.VOTE;

public class VotingClient extends Client{
    private static Logger logger = LoggerFactory.getLogger(VotingClient.class);
    public static void main(String[] str){
        logger.info("Initializing VotingClient...");
        Properties properties = System.getProperties();
        try {
            if(containsValidArguments(properties)){
                VotingClient client = (VotingClient) getRemoteService(properties.getProperty("serverAddress"), VOTE);
                executeAction(client, properties.getProperty("votesPath"));
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

    private static void executeAction(VotingClient client, String votePath){
        VoteParser parser = new VoteParser(votePath);
        parser.parseVotes();
    }
}
