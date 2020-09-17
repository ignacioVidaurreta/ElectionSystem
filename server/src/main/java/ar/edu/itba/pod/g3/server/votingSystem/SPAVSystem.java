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

        // Parsing votes to doubles for next calculation
        List<Map<PoliticalParty, Double>> list = this.votes.stream().map(v -> v.getRanking().entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().doubleValue()
                ))).collect(Collectors.toList());

        // Three winners for each province, hence three rounds for each province
        for(int round = 0; round < 3; round++) {

            // Calculation and accumulation of the value of each ballot:
            // 1/(1+m) where m is the number of candidates
            // approved on that ballot who were already elected
            Map<PoliticalParty, Double> roundRanking = list.stream().flatMap(m ->  {m.entrySet()
                    .forEach(e -> e.setValue(1.0 / (1 + amountOfElementsInCommon(winners, m.keySet()))));return m.entrySet().stream();})
                    .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue,
                    Double::sum));

            // The unelected candidate who has the highest approval score is elected for the round
            winners.forEach(roundRanking::remove);
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
