package ar.edu.itba.pod.g3.server.votingSystem;

import ar.edu.itba.pod.g3.api.enums.PoliticalParty;
import ar.edu.itba.pod.g3.api.models.Vote;
import ar.edu.itba.pod.g3.server.interfaces.VotingSystem;
import ar.edu.itba.pod.g3.server.votingSystem.utils.DoubleRankingComparator;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FPTPSystem implements VotingSystem {

    private List<Vote> votes;

    public FPTPSystem(List<Vote> votes) {
        this.votes = votes;
    }

    @Override
    public ElectionResults getResults() {
        double size = this.votes.size();
        Map<PoliticalParty, Double> results = this.votes.stream().collect(Collectors.groupingBy(Vote::getFptpWinner, Collectors.collectingAndThen(Collectors.counting(), c -> c/size)));
        return new FPTPSystemResults(Collections.max(results.entrySet(), new DoubleRankingComparator()).getKey(), results);
    }

    public static class FPTPSystemResults extends ElectionResults{

        public FPTPSystemResults(PoliticalParty winner, Map<PoliticalParty, Double> results) {
            super(Collections.singletonList(winner), Collections.singletonList(results));
        }

    }
}
