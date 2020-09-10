package ar.edu.itba.pod.g3.server.votingSystem;

import ar.edu.itba.pod.g3.api.enums.PoliticalParty;

import java.util.List;
import java.util.Map;

public class ElectionResults {
    List<PoliticalParty> winners;
    List<Map<PoliticalParty, Double>> roundsRankings;

    public ElectionResults(List<PoliticalParty> winners, List<Map<PoliticalParty, Double>> roundsRankings) {
        this.winners = winners;
        this.roundsRankings = roundsRankings;
    }

    public List<Map<PoliticalParty, Double>> getRoundsRankings() {
        return roundsRankings;
    }

    public List<PoliticalParty> getWinners() {
        return winners;
    }

    @Override
    public String toString() {
        return "ElectionResults{" +
                "winners=" + winners +
                ", roundsRankings=" + roundsRankings +
                '}';
    }
}
