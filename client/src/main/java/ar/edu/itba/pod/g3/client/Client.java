package ar.edu.itba.pod.g3.client;

import ar.edu.itba.pod.g3.client.interfaces.ManagementClient;
import ar.edu.itba.pod.g3.enums.ServiceName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;

public abstract class Client {
    private static Logger logger = LoggerFactory.getLogger(Client.class);

    static Remote getRemoteService(final String address, final ServiceName serviceName) throws NotBoundException, RemoteException{
        logger.info(String.format("Connecting to %s:%d/%s", address, 1099, serviceName.getServiceName()));
        final Registry registry = LocateRegistry.getRegistry(address, 1099);
        // return registry.lookup(serviceName.getServiceName());
        return null;
    }
}
