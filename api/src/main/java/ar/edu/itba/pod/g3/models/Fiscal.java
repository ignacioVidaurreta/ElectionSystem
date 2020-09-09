package ar.edu.itba.pod.g3.models;

import ar.edu.itba.pod.g3.enums.PoliticalParty;
import ar.edu.itba.pod.g3.interfaces.NotificationConsumer;

public class Fiscal implements NotificationConsumer {
    private final int booth;
    private final PoliticalParty party;
    public Fiscal(int booth, PoliticalParty party){
        this.booth = booth;
        this.party = party;
    }
    public void process(){
        //TODO: Implement
        //throw new NotImplementedException();
    }

    public PoliticalParty getParty() {
        return party;
    }

    public int getBooth() {
        return booth;
    }
}
