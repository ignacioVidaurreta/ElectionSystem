package ar.edu.itba.pod.g3.interfaces;

import ar.edu.itba.pod.g3.models.Fiscal;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FiscalizationService extends Remote {
    boolean registerFiscal(Fiscal fiscal) throws RemoteException, Exception;
}
