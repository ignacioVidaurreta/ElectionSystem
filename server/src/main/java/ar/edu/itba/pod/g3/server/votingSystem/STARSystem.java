package ar.edu.itba.pod.g3.server.votingSystem;

import ar.edu.itba.pod.g3.api.enums.PoliticalParty;
import ar.edu.itba.pod.g3.api.models.Vote;
import ar.edu.itba.pod.g3.server.interfaces.VotingSystem;
import ar.edu.itba.pod.g3.server.votingSystem.utils.DoubleRankingComparator;
import ar.edu.itba.pod.g3.server.votingSystem.utils.IntegerRankingComparator;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class STARSystem implements VotingSystem {

    private List<Vote> votes;

    public STARSystem(List<Vote> votes) {
        this.votes = votes;
    }

    @Override
    public ElectionResults getResults() {
        // Scoring round
        // Adding all scores for all candidates
        Map<PoliticalParty, Double> firstRound = this.votes.stream()
                .flatMap(v->v.getRanking().entrySet().stream())
                .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> e.getValue().doubleValue(),
                Double::sum));

        PoliticalParty fistPlaceFirstRound = Collections.max(firstRound.entrySet(), new DoubleRankingComparator()).getKey();
        Double firstPlaceFirstRoundValue = firstRound.get(fistPlaceFirstRound);
        firstRound.remove(fistPlaceFirstRound);

        PoliticalParty secondPlaceFirstRound = Collections.max(firstRound.entrySet(), new DoubleRankingComparator()).getKey();
        firstRound.put(fistPlaceFirstRound, firstPlaceFirstRoundValue);

        // Automatic run-off
        // Filtering second run winners ballots
        List<Map<PoliticalParty, Integer>> secondRoundFilteredList = this.votes.stream()
                .map(v->v.getRanking()
                        .entrySet()
                        .stream()
                        .filter(e -> e.getKey().equals(fistPlaceFirstRound) || e.getKey().equals(secondPlaceFirstRound))
                        .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingInt(Map.Entry::getValue))))
                .collect(Collectors.toList());

        // Considering one winner per ballot
        List<PoliticalParty> secondRoundVotesList = secondRoundFilteredList.stream()
                .map(m -> Collections.max(m.entrySet(), new IntegerRankingComparator()).getKey())
                .collect(Collectors.toList());

        double size = secondRoundFilteredList.size();

        // Calculating percentage for candidates
        Map<PoliticalParty, Double> finalRound = secondRoundVotesList.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.collectingAndThen(Collectors.counting(), c -> c/size)));

        PoliticalParty winner = Collections.max(finalRound.entrySet(), new DoubleRankingComparator()).getKey();

        return new STARSystemResults(firstRound, finalRound, winner);
    }

    public static class STARSystemResults extends ElectionResults{

        public STARSystemResults(Map<PoliticalParty, Double> firstRound, Map<PoliticalParty, Double> finalRound, PoliticalParty winner) {
            super(Collections.singletonList(winner), Arrays.asList(firstRound,finalRound));
        }

    }
}
