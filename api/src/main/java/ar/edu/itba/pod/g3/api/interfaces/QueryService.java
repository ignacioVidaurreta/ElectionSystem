package ar.edu.itba.pod.g3.api.interfaces;

import ar.edu.itba.pod.g3.api.models.QueryDescriptor;

import java.rmi.Remote;

public interface QueryService extends Remote {
    String executeQuery(QueryDescriptor descriptor) throws Exception;

}
