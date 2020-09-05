package ar.edu.itba.pod.g3.client.interfaces;

import ar.edu.itba.pod.g3.enums.ElectionState;

public interface AdministrationClient {
    ElectionState open() throws Exception;

    ElectionState close() throws Exception;

    ElectionState getState();
}
