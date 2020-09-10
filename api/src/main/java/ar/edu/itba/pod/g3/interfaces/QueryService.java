package ar.edu.itba.pod.g3.interfaces;

import ar.edu.itba.pod.g3.models.QueryDescriptor;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface QueryService extends Remote {
    String executeQuery(QueryDescriptor descriptor) throws RemoteException;

}