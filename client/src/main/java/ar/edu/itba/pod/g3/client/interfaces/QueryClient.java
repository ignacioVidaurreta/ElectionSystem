package ar.edu.itba.pod.g3.client.interfaces;

import ar.edu.itba.pod.g3.models.QueryDescriptor;

public interface QueryClient {
    String executeQuery(QueryDescriptor descriptor);

}
