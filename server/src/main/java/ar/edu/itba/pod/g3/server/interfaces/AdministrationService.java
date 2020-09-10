package ar.edu.itba.pod.g3.server.interfaces;

import ar.edu.itba.pod.g3.enums.ElectionState;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AdministrationService extends Remote {
    boolean openElection() throws RemoteException;

    boolean closeElection() throws RemoteException;

    ElectionState consultElectionState() throws RemoteException ;


}