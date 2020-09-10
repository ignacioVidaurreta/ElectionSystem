package ar.edu.itba.pod.g3.api.interfaces;

import ar.edu.itba.pod.g3.api.models.Vote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

public interface VotingService extends Remote {
  boolean emitVotes(Collection<Vote> votes) throws Exception;
}
