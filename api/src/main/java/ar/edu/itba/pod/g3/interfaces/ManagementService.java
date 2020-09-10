package ar.edu.itba.pod.g3.interfaces;

import ar.edu.itba.pod.g3.enums.ElectionState;

import java.rmi.RemoteException;

public interface ManagementService {
    boolean openElection() throws RemoteException;

    boolean closeElection() throws RemoteException;

    ElectionState consultElectionState() throws RemoteException;

    // ElectionState executeAction(String action) throws RemoteException;
}
