package ar.edu.itba.pod.g3.server.votingSystem;

import ar.edu.itba.pod.g3.enums.PoliticalParty;
import ar.edu.itba.pod.g3.models.Vote;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class STARSystem implements VotingSystem {

    private List<Vote> votes;

    public STARSystem(List<Vote> votes) {
        this.votes = votes;
    }

    @Override
    public STARSystemResults getResults() {
        Map<PoliticalParty, Integer> firstRound = this.votes.stream()
                .flatMap(v->v.getRanking().entrySet().stream())
                .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                Integer::sum));

        PoliticalParty fistPlaceFirstRound = Collections.max(firstRound.entrySet(), new RankingComparator()).getKey();
        firstRound.remove(fistPlaceFirstRound);

        PoliticalParty secondPlaceFirstRound = Collections.max(firstRound.entrySet(), new RankingComparator()).getKey();

        List<Map<PoliticalParty, Integer>> secondRoundFilteredList = this.votes.stream()
                .map(v->v.getRanking()
                        .entrySet()
                        .stream()
                        .filter(e -> e.getKey().equals(fistPlaceFirstRound) || e.getKey().equals(secondPlaceFirstRound))
                        .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingInt(Map.Entry::getValue))))
                .collect(Collectors.toList());

        List<PoliticalParty> secondRoundVotesList = secondRoundFilteredList.stream()
                .map(m -> Collections.max(m.entrySet(), new RankingComparator()).getKey())
                .collect(Collectors.toList());

        double size = secondRoundFilteredList.size();
        Map<PoliticalParty, Double> finalRound = secondRoundVotesList.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.collectingAndThen(Collectors.counting(), c -> c/size)));

        PoliticalParty winner = Collections.max(finalRound.entrySet(), new FinalRankingComparator()).getKey();

        return new STARSystemResults(firstRound, finalRound, winner);
    }

    public class STARSystemResults{
        private Map<PoliticalParty, Integer> firstRound;
        private Map<PoliticalParty, Double> finalRound;
        private PoliticalParty winner;

        public STARSystemResults(Map<PoliticalParty, Integer> firstRound, Map<PoliticalParty, Double> finalRound, PoliticalParty winner) {
            this.firstRound = firstRound;
            this.finalRound = finalRound;
            this.winner = winner;
        }

        @Override
        public String toString() {
            return "STARSystemResults{" +
                    "firstRound=" + firstRound +
                    ", finalRound=" + finalRound +
                    ", winner=" + winner +
                    '}';
        }
    }

    private class RankingComparator implements Comparator<Map.Entry<PoliticalParty, Integer>>{
        @Override
        public int compare(Map.Entry<PoliticalParty, Integer> t1, Map.Entry<PoliticalParty, Integer> t2) {
            int diff = t1.getValue().compareTo(t2.getValue());
            if(diff != 0)
                return diff;
            return -t1.getKey().compareTo(t2.getKey()); // Alphabetically, we want the min value of the Enum
        }
    }

    private class FinalRankingComparator implements Comparator<Map.Entry<PoliticalParty, Double>>{
        @Override
        public int compare(Map.Entry<PoliticalParty, Double> t1, Map.Entry<PoliticalParty, Double> t2) {
            int diff = t1.getValue().compareTo(t2.getValue());
            if(diff != 0)
                return diff;
            return -t1.getKey().compareTo(t2.getKey()); // Alphabetically, we want the min value of the Enum
        }
    }
}
