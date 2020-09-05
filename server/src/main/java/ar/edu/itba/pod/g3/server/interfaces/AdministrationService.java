package ar.edu.itba.pod.g3.server.interfaces;

import ar.edu.itba.pod.g3.enums.ElectionState;

public interface AdministrationService {
    boolean openElection() throws Exception;

    boolean closeElection() throws Exception;

    ElectionState consultElectionState();


}