package ar.edu.itba.pod.g3.server.interfaces;

import ar.edu.itba.pod.g3.models.Vote;

import java.util.Collection;
public interface VotingService {
    boolean emitVote(Collection<Vote> vote) throws Exception;
}
