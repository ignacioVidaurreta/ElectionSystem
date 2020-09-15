package ar.edu.itba.pod.g3.api.models;

import ar.edu.itba.pod.g3.api.enums.PoliticalParty;
import ar.edu.itba.pod.g3.api.interfaces.NotificationConsumer;

import java.io.Serializable;

public class Fiscal implements NotificationConsumer, Serializable {
    private final int booth;
    private final PoliticalParty party;
    public Fiscal(int booth, PoliticalParty party){
        this.booth = booth;
        this.party = party;
    }

    @Override
    public void notify(Vote vote) {
        System.out.println(String.format("New vote for %s in booth %d", vote.getFptpWinner(), vote.getBooth()));

    }

    public PoliticalParty getParty() {
        return party;
    }

    public int getBooth() {
        return booth;
    }
}
