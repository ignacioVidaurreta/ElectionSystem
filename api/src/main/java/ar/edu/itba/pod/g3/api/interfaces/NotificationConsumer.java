package ar.edu.itba.pod.g3.api.interfaces;

import ar.edu.itba.pod.g3.api.models.Vote;

@FunctionalInterface
public interface NotificationConsumer{
    void notify(Vote vote);
}
