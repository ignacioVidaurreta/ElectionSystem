package ar.edu.itba.pod.g3.api.interfaces;

import ar.edu.itba.pod.g3.api.enums.PoliticalParty;
import ar.edu.itba.pod.g3.api.models.Vote;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NotificationConsumer extends Remote, Serializable {
    void notifyFiscal(Vote vote) throws RemoteException;

    PoliticalParty getParty() throws RemoteException;

    int getBooth() throws RemoteException;
}
