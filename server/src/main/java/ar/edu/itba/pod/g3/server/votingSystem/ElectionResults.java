package ar.edu.itba.pod.g3.server.votingSystem;

import ar.edu.itba.pod.g3.api.enums.PoliticalParty;
import ar.edu.itba.pod.g3.api.enums.Province;
import ar.edu.itba.pod.g3.api.enums.QueryType;
import ar.edu.itba.pod.g3.api.models.ElectionException;
import ar.edu.itba.pod.g3.server.votingSystem.utils.DoubleRankingComparator;
import ar.edu.itba.pod.g3.api.enums.QueryType;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ar.edu.itba.pod.g3.api.enums.QueryType.*;

public class ElectionResults{
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

    public String serialize(boolean electionsClosed, QueryType queryType) {
        StringBuilder sb = new StringBuilder();
        if(!electionsClosed || queryType == BOOTH) {
            sb.append("Percentage;Party\n");
            roundsRankings.get(0).entrySet().stream()
                    .sorted(new DoubleRankingComparator().reversed())
                    .forEach(e -> sb.append(String.format("%.2f%%;%s\n",e.getValue()*100,e.getKey())));
            if(!electionsClosed)
                return String.valueOf(sb);
        }
        switch(queryType) {
            case BOOTH:
                sb.append(String.format("Winner\n%s\n",winners.get(0)));
                break;
            case PROVINCIAL:
                for(int i = 0; i < 3; i++) {
                    sb.append(String.format("Round %d\nApproval;Party\n",i+1));
                    roundsRankings.get(i).entrySet().stream()
                            .sorted(new DoubleRankingComparator().reversed())
                            .forEach(e -> sb.append(String.format("%.2f;%s\n",e.getValue(),e.getKey())));
                    sb.append("Winners\n");
                    sb.append(winners.subList(0,i+1).stream()
                                            .map(Object::toString)
                                            .collect(Collectors.joining(",")));
                    sb.append("\n");
                }
                break;
            case NATIONAL:
                sb.append("Score;Party\n");
                roundsRankings.get(0).entrySet().stream()
                        .sorted(new DoubleRankingComparator().reversed())
                        .forEach(e -> sb.append(String.format("%d;%s\n",e.getValue().intValue(),e.getKey())));
                sb.append("Percentage;Party\n");
                roundsRankings.get(1).entrySet().stream()
                        .sorted(new DoubleRankingComparator().reversed())
                        .forEach(e -> sb.append(String.format("%.2f%%;%s\n",e.getValue()*100,e.getKey())));
                sb.append(String.format("Winner\n%s\n",winners.get(0)));
                break;
        }
        return String.valueOf(sb);
    }
}
