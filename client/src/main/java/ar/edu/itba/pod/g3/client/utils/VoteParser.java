package ar.edu.itba.pod.g3.client.utils;

import ar.edu.itba.pod.g3.client.VotingClient;
import ar.edu.itba.pod.g3.models.Vote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

public class VoteParser {
    private static Logger logger = LoggerFactory.getLogger(VoteParser.class);
    private Collection<Vote> parsedVotes;
    final private String voteFilePath;

    public VoteParser(String voteFilePath){
        parsedVotes = new ArrayList<>();
        this.voteFilePath = voteFilePath;
    }

    public void parseVotes(){
        try {
            File inputFile = new File(this.voteFilePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));

            this.parsedVotes = br.lines().map(readVote).collect(Collectors.toList());
            br.close();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    private Function<String, Vote> readVote = (line) -> {
        String[] values = line.split(";");
        logger.info(String.format("Saving vote %s", line));
        return new Vote(values[0]);
    };

    public Collection<Vote> getParsedVotes() {
        return parsedVotes;
    }

    public String getVoteFilePath() {
        return voteFilePath;
    }

    public void setParsedVotes(Collection<Vote> parsedVotes) {
        this.parsedVotes = parsedVotes;
    }
}
