package ar.edu.itba.pod.g3.api.interfaces;

import ar.edu.itba.pod.g3.api.enums.ElectionState;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ManagementService extends Remote {
    boolean openElection() throws RemoteException, IllegalStateException;

    boolean closeElection() throws RemoteException, IllegalStateException;

    ElectionState consultElectionState() throws RemoteException;

}
