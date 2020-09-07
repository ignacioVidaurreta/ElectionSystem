package ar.edu.itba.pod.g3.server;

import ar.edu.itba.pod.g3.enums.PoliticalParty;
import ar.edu.itba.pod.g3.models.Vote;
import ar.edu.itba.pod.g3.server.votingSystem.FPTPSystem;
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

        Map<PoliticalParty,Integer> ranking1 = new HashMap<>();
        ranking1.put(TIGER,3);
        ranking1.put(LEOPARD,2);
        ranking1.put(LYNX,1);
        Vote v1 = new Vote(1000, JUNGLE, ranking1, TIGER);
        Map<PoliticalParty,Integer> ranking2 = new HashMap<>();
        ranking2.put(TIGER,2);
        ranking2.put(LEOPARD,2);
        ranking2.put(LYNX,1);
        Vote v2 = new Vote(1001, JUNGLE, ranking2, LYNX);
        Map<PoliticalParty,Integer> ranking3 = new HashMap<>();
        ranking3.put(TIGER,3);
        ranking3.put(OWL,3);
        ranking3.put(LYNX,3);
        ranking3.put(BUFFALO,5);
        Vote v3 = new Vote(1002, SAVANNAH, ranking3, TIGER);

//        FPTPSystem fptpSystem = new FPTPSystem(Arrays.asList(v1,v2,v3));
        STARSystem starSystem = new STARSystem(Arrays.asList(v1,v2,v3));
        starSystem.getResults();
//        System.out.println(fptpSystem.getResults());
    }
}
