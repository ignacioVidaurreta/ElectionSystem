package ar.edu.itba.pod.g3.server;

import ar.edu.itba.pod.g3.enums.PoliticalParty;
import ar.edu.itba.pod.g3.models.Vote;
import ar.edu.itba.pod.g3.server.votingSystem.FPTPSystem;
import ar.edu.itba.pod.g3.server.votingSystem.SPAVSystem;
import ar.edu.itba.pod.g3.server.votingSystem.STARSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static ar.edu.itba.pod.g3.enums.PoliticalParty.*;
import static ar.edu.itba.pod.g3.enums.Province.JUNGLE;
import static ar.edu.itba.pod.g3.enums.Province.SAVANNAH;

public class Server {
    private static Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) {
        logger.info("election-system Server Starting ...");
    }
}
