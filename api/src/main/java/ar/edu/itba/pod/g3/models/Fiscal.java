package ar.edu.itba.pod.g3.models;

import ar.edu.itba.pod.g3.interfaces.NotificationConsumer;

public class Fiscal implements NotificationConsumer {
    private final String id;
    private final String party;
    public Fiscal(String id, String party){
        this.id = id;
        this.party = party;
    }
    public void process(){
        //TODO: Implement
        //throw new NotImplementedException();
    }

    public String getParty() {
        return party;
    }

    public String getId() {
        return id;
    }
}
