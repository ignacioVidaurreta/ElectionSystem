package ar.edu.itba.pod.g3.server.votingSystem;

import ar.edu.itba.pod.g3.api.enums.PoliticalParty;
import ar.edu.itba.pod.g3.api.models.NoVotesException;
import ar.edu.itba.pod.g3.api.models.Vote;
import ar.edu.itba.pod.g3.server.interfaces.VotingSystem;
import ar.edu.itba.pod.g3.server.votingSystem.utils.DoubleRankingComparator;

import java.util.*;
import java.util.stream.Collectors;

public class SPAVSystem implements VotingSystem {

    private List<Vote> votes;

    public SPAVSystem(List<Vote> votes) {
        this.votes = votes;
    }

    @Override
    public ElectionResults getResults() throws NoVotesException {
        if (this.votes.isEmpty()) {
            throw new NoVotesException("SPAV");
        }

        // Each round result is concatenated in the corresponding list
        List<PoliticalParty> winners = new LinkedList<>();
        List<Map<PoliticalParty, Double>> roundsRankings = new LinkedList<>();

        // Three winners for each province, hence three rounds for each province
        for(int round = 0; round < 3; round++) {

            Map<PoliticalParty, Double> roundRanking = new HashMap<>();

            // For each political party we calculate all ballots
            // that contain such party and accumulate the index:
            // 1/(1+m) where m is the number of candidates
            // approved on that ballot who were already elected
            Arrays.stream(PoliticalParty.values()).filter(p -> !winners.contains(p))
                    .forEach(p -> roundRanking.put(p,
                                    this.votes.stream()
                                            .filter(v -> v.getRanking().containsKey(p))
                                            .mapToDouble(v -> 1.0/(1 + amountOfElementsInCommon(winners, v.getRanking().keySet())))
                                            .sum()));

            roundRanking.entrySet().removeIf(e -> e.getValue() == 0);
            winners.add(Collections.max(roundRanking.entrySet(), new DoubleRankingComparator()).getKey());
            roundsRankings.add(roundRanking);
        }

        return new SPAVSystemResults(winners, roundsRankings);
    }


    private Integer amountOfElementsInCommon(List<PoliticalParty> s1, Set<PoliticalParty> s2) {
        int i = 0;
        for(PoliticalParty p: s1) {
            if(s2.contains(p))
                i++;
        }
        return i;
    }

    public static class SPAVSystemResults extends ElectionResults{

        public SPAVSystemResults(List<PoliticalParty> winners, List<Map<PoliticalParty, Double>> roundsRankings) {
            super(winners, roundsRankings);
        }

    }
}
