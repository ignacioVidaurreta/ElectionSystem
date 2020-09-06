package ar.edu.itba.pod.g3.client.interfaces;

import ar.edu.itba.pod.g3.enums.ElectionState;

import java.rmi.RemoteException;

public interface ManagementClient {
    ElectionState open() throws RemoteException;

    ElectionState close() throws RemoteException;

    ElectionState getState();

    ElectionState executeAction(String action) throws RemoteException;
}
