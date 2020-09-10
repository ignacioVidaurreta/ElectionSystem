package ar.edu.itba.pod.g3.models;

import ar.edu.itba.pod.g3.enums.PoliticalParty;
import ar.edu.itba.pod.g3.enums.Province;

import java.util.Map;

public class Vote {
    private Integer booth;
    private Province province;
    private Map<PoliticalParty,Integer> ranking;
    private PoliticalParty fptpWinner;

    public Vote(Integer booth, Province province, Map<PoliticalParty,Integer> ranking, PoliticalParty fptpWinner) {
        this.booth = booth;
        this.province = province;
        this.ranking = ranking;
        this.fptpWinner = fptpWinner;
    }

    public Integer getBooth() {
        return booth;
    }

    public PoliticalParty getFptpWinner() {
        return fptpWinner;
    }

    public Province getProvince() {
        return province;
    }

    public Map<PoliticalParty, Integer> getRanking() {
        return ranking;
    }
}
