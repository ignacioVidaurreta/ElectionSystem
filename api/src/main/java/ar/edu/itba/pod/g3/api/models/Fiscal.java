package ar.edu.itba.pod.g3.api.models;

import ar.edu.itba.pod.g3.api.enums.PoliticalParty;
import ar.edu.itba.pod.g3.api.interfaces.NotificationConsumer;


public class Fiscal implements NotificationConsumer {
    private final int booth;
    private final PoliticalParty party;
    public Fiscal(int booth, PoliticalParty party){
        this.booth = booth;
        this.party = party;
    }

    @Override
    public void notifyFiscal(Vote vote) {
        System.out.println(String.format("New vote for %s on polling place %d", vote.getFptpWinner(), vote.getBooth()));
    }

    @Override
    public PoliticalParty getParty() {
        return party;
    }

    @Override
    public int getBooth() {
        return booth;
    }
}
