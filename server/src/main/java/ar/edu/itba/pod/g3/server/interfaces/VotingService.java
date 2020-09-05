package ar.edu.itba.pod.g3.server.interfaces;

public interface VotingService {
    boolean emitVote(String vote) throws Exception;
}
