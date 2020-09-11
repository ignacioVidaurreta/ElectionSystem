package ar.edu.itba.pod.g3.api.models;

import ar.edu.itba.pod.g3.api.enums.PoliticalParty;
import ar.edu.itba.pod.g3.api.enums.Province;

import java.io.Serializable;
import java.util.Map;

public class Vote implements Serializable {
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

    @Override
    public String toString() {
        return "Vote{" +
                "booth=" + booth +
                ", province=" + province +
                ", ranking=" + ranking +
                ", fptpWinner=" + fptpWinner +
                '}';
    }
}
