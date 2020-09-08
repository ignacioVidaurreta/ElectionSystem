package ar.edu.itba.pod.g3.server.votingSystem;

import ar.edu.itba.pod.g3.enums.PoliticalParty;
import ar.edu.itba.pod.g3.models.Vote;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FPTPSystem implements VotingSystem {

    private List<Vote> votes;

    public FPTPSystem(List<Vote> votes) {
        this.votes = votes;
    }

    @Override
    public Map<PoliticalParty, Double> getResults() {
        double size = this.votes.size();
        return this.votes.stream().collect(Collectors.groupingBy(Vote::getFptpWinner, Collectors.collectingAndThen(Collectors.counting(), c -> c/size)));
    }
}
