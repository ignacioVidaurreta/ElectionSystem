package ar.edu.itba.pod.g3.interfaces;

import ar.edu.itba.pod.g3.models.Vote;

@FunctionalInterface
public interface NotificationConsumer {
    void process(Vote vote);
}
