package ar.edu.itba.pod.g3.server.model;

import ar.edu.itba.pod.g3.enums.ElectionState;
import ar.edu.itba.pod.g3.enums.Province;
import ar.edu.itba.pod.g3.enums.QueryType;
import ar.edu.itba.pod.g3.interfaces.NotificationConsumer;
import ar.edu.itba.pod.g3.models.QueryDescriptor;
import ar.edu.itba.pod.g3.models.Vote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Election {

    private static final Logger logger = LoggerFactory.getLogger(Election.class);
    private final Lock readlock;
    private final Lock writeLock;

    ElectionState electionState;

    private final List<Vote> votes = new ArrayList<>();
    private final List<NotificationConsumer> notificationConsumers = new ArrayList<>();


    public Election() {
        // Create a reentrant RWlock with fairness true
        ReadWriteLock rwLock = new ReentrantReadWriteLock(true);
        readlock = rwLock.readLock();
        writeLock = rwLock.writeLock();
    }

    public boolean setElectionState(ElectionState state) {
        this.electionState = state;
        return true;
    }

    public ElectionState getElectionState() {
        return electionState;
    }

    public boolean addVote(Vote vote) {
        writeLock.lock();
        switch (electionState) {
            case NOT_STARTED:
                return false;
            case OPEN:
                votes.add(vote);
            case CLOSED:
                return false;
        }
        writeLock.unlock();
        return false;
    }

    public boolean addNotificationConsumer(NotificationConsumer notificationConsumer) {
        synchronized (notificationConsumers) {
            return notificationConsumers.add(notificationConsumer);
        }
    }

    public String queryElection(QueryDescriptor descriptor) throws Exception {
        readlock.lock();
        QueryType queryType = descriptor.getType();
        String id = descriptor.getId();
        switch (queryType) {
            case BOOTH:
                int boothId = Integer.parseInt(id);
                return queryBooth(boothId);
            case PROVINCIAL:
                Province province = Province.valueOf(id);
                return queryProvince(province);
            case NATIONAL:
                return queryNational();
        }
        readlock.unlock();
        throw new Exception();
    }

    private String queryBooth(int boothId){
        switch (electionState) {
            case NOT_STARTED:
                break;
            case OPEN:
                break;
            case CLOSED:
                break;
        }
        return "sample queryBooth return";
    }

    private String queryProvince(Province province) {
        switch (electionState) {
            case NOT_STARTED:
                break;
            case OPEN:
                break;
            case CLOSED:
                break;
        }
        return "sample queryProvince return";
    }

    private String queryNational() {
        switch (electionState) {
            case NOT_STARTED:
                break;
            case OPEN:
                break;
            case CLOSED:
                break;
        }
        return "sample queryNational return";
    }
}
