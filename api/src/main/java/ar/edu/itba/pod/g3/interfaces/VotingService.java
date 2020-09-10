package ar.edu.itba.pod.g3.interfaces;

import ar.edu.itba.pod.g3.models.QueryDescriptor;
import ar.edu.itba.pod.g3.models.Vote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

public interface VotingService extends Remote {
  int submitVotes(Collection<Vote> votes) throws RemoteException;

}
