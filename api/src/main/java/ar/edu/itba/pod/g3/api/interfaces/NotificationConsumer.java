package ar.edu.itba.pod.g3.api.interfaces;

import ar.edu.itba.pod.g3.api.models.Vote;

import java.rmi.Remote;

@FunctionalInterface
public interface NotificationConsumer extends Remote {
    void notify(Vote vote);
}
