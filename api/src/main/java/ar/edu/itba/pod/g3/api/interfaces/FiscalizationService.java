package ar.edu.itba.pod.g3.api.interfaces;

import ar.edu.itba.pod.g3.api.models.Fiscal;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FiscalizationService extends Remote {
    void registerFiscal(Fiscal fiscal) throws RemoteException, Exception;
}
