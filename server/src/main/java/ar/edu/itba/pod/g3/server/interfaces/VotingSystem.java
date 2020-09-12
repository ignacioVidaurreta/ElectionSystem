package ar.edu.itba.pod.g3.server.interfaces;

import ar.edu.itba.pod.g3.api.models.NoVotesException;
import ar.edu.itba.pod.g3.server.votingSystem.ElectionResults;

public interface VotingSystem {
    ElectionResults getResults() throws NoVotesException;
}
