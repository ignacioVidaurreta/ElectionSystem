package ar.edu.itba.pod.g3.server.votingSystem;

import ar.edu.itba.pod.g3.enums.PoliticalParty;
import ar.edu.itba.pod.g3.models.Vote;

import java.util.*;
import java.util.stream.Collectors;

public class SPAVSystem implements VotingSystem {

    private List<Vote> votes;

    public SPAVSystem(List<Vote> votes) {
        this.votes = votes;
    }

    @Override
    public Object getResults() {
        List<PoliticalParty> winners = new LinkedList<>();
        List<Map<PoliticalParty, Double>> roundsRankings = new LinkedList<>();

        for(int round = 0; round < 3; round++) {
            List<Map<PoliticalParty, Double>> list = this.votes.stream().map(v -> v.getRanking().entrySet()
                    .stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> e.getValue().doubleValue()
                    ))).collect(Collectors.toList());

            Map<PoliticalParty, Double> roundRanking = list.stream().flatMap(m ->  {m.entrySet()
                    .forEach(e -> e.setValue(1.0 / (1 + amountOfElementsInCommon(winners, m.keySet()))));return m.entrySet().stream();})
                    .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue,
                    Double::sum));

            winners.forEach(roundRanking::remove);

            winners.add(Collections.max(roundRanking.entrySet(), new SPAVSystem.RankingComparator()).getKey());
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

    public class SPAVSystemResults {
        List<PoliticalParty> winners;
        List<Map<PoliticalParty, Double>> roundsRankings;

        public SPAVSystemResults(List<PoliticalParty> winners, List<Map<PoliticalParty, Double>> roundsRankings) {
            this.winners = winners;
            this.roundsRankings = roundsRankings;
        }

        @Override
        public String toString() {
            return "SPAVSystemResults{" +
                    "winners=" + winners +
                    ", roundsRankings=" + roundsRankings +
                    '}';
        }
    }

    private class RankingComparator implements Comparator<Map.Entry<PoliticalParty, Double>> {
        @Override
        public int compare(Map.Entry<PoliticalParty, Double> t1, Map.Entry<PoliticalParty, Double> t2) {
            int diff = t1.getValue().compareTo(t2.getValue());
            if(diff != 0)
                return diff;
            return -t1.getKey().compareTo(t2.getKey()); // Alphabetically, we want the min value of the Enum
        }
    }
}
