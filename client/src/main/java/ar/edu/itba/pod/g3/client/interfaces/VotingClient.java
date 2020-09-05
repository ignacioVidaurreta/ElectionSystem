package ar.edu.itba.pod.g3.client.interfaces;

import ar.edu.itba.pod.g3.models.Vote;

import java.util.Collection;

public interface VotingClient {
    int submitVotes(Collection<Vote> votes);
}
