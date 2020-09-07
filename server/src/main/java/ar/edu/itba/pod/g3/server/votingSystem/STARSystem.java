package ar.edu.itba.pod.g3.server.votingSystem;

import ar.edu.itba.pod.g3.enums.PoliticalParty;
import ar.edu.itba.pod.g3.models.Vote;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class STARSystem implements VotingSystem {

    private List<Vote> votes;

    public STARSystem(List<Vote> votes) {
        this.votes = votes;
    }

    @Override
    public Map<PoliticalParty, Double> getResults() {
        Map<PoliticalParty, Integer> firstRound = this.votes.stream().flatMap(v->v.getRanking().entrySet().stream()).collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                Integer::sum));
        System.out.println(firstRound);


        return null;
    }
}
