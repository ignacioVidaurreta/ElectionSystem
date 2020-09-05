package ar.edu.itba.pod.g3.server.interfaces;

import ar.edu.itba.pod.g3.models.QueryDescriptor;

public interface QueryService {
    String executeQuery(QueryDescriptor descriptor);
}
