package ar.edu.itba.pod.g3.client.utils;

import ar.edu.itba.pod.g3.api.enums.PoliticalParty;
import ar.edu.itba.pod.g3.api.enums.Province;
import ar.edu.itba.pod.g3.api.models.Vote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
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
            logger.info(String.format("Parsed %d votes", this.parsedVotes.size()));
            br.close();
        }catch(IOException ex){
            logger.error("There was an error while parsing the votes");
            ex.printStackTrace();
        }
    }

    private Function<String, Vote> readVote = (line) -> {
        String[] values = line.split(";");
        logger.info(String.format("Saving vote %s", line));
        Integer booth = Integer.parseInt(values[0]);
        Province province = Province.valueOf(values[1]);
        Map<PoliticalParty,Integer> ranking = Arrays.stream(values[2].split(","))
                .map(rank -> rank.split("\\|"))
                .collect(Collectors.toMap(r -> PoliticalParty.valueOf(r[0]), r-> Integer.parseInt(r[1])));

        return new Vote(booth,province,ranking, PoliticalParty.valueOf(values[3]));

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
