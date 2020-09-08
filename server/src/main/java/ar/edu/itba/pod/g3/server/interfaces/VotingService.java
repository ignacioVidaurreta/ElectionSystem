package ar.edu.itba.pod.g3.server.interfaces;

import ar.edu.itba.pod.g3.models.Vote;

public interface VotingService {
    boolean emitVote(Vote vote) throws Exception;
}
