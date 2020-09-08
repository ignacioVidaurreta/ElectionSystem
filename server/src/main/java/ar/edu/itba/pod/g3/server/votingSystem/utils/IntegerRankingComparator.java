package ar.edu.itba.pod.g3.server.votingSystem.utils;

import ar.edu.itba.pod.g3.enums.PoliticalParty;

import java.util.Comparator;
import java.util.Map;

public class IntegerRankingComparator implements Comparator<Map.Entry<PoliticalParty, Integer>> {
    @Override
    public int compare(Map.Entry<PoliticalParty, Integer> t1, Map.Entry<PoliticalParty, Integer> t2) {
        int diff = t1.getValue().compareTo(t2.getValue());
        if(diff != 0)
            return diff;
        return -t1.getKey().compareTo(t2.getKey()); // Alphabetically, we want the min value of the Enum
    }
}
