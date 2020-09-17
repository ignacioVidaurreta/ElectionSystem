package ar.edu.itba.pod.g3.server;

import ar.edu.itba.pod.g3.api.enums.PoliticalParty;
import ar.edu.itba.pod.g3.api.enums.QueryType;
import ar.edu.itba.pod.g3.api.models.NoVotesException;
import ar.edu.itba.pod.g3.api.models.Vote;
import ar.edu.itba.pod.g3.server.votingSystem.ElectionResults;
import ar.edu.itba.pod.g3.server.votingSystem.FPTPSystem;
import ar.edu.itba.pod.g3.server.votingSystem.SPAVSystem;
import ar.edu.itba.pod.g3.server.votingSystem.STARSystem;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

import static ar.edu.itba.pod.g3.api.enums.PoliticalParty.*;
import static ar.edu.itba.pod.g3.api.enums.Province.JUNGLE;
import static ar.edu.itba.pod.g3.api.enums.Province.SAVANNAH;

public class VotingSystemsTest {

    @Test
    public void FPTPSystemTest() throws NoVotesException {
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

        FPTPSystem fptpSystem = new FPTPSystem(Arrays.asList(v1,v2,v3));
        ElectionResults electionResults = fptpSystem.getResults();

        assertEquals(electionResults.getWinners().get(0), TIGER);
        assertEquals(electionResults.getRoundsRankings().get(0).get(LYNX), 1.0/3);
        assertEquals(electionResults.getRoundsRankings().get(0).get(TIGER), 2.0/3);

        System.out.println(electionResults.serialize(true, QueryType.BOOTH));
    }


    @Test
    public void STARSystemTest() throws NoVotesException {
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

        STARSystem starSystem = new STARSystem(Arrays.asList(v1,v2,v3));
        ElectionResults electionResults = starSystem.getResults();

        assertEquals(electionResults.getWinners().get(0), TIGER);
        assertEquals(electionResults.getRoundsRankings().get(0).get(OWL), 3.0);
        assertEquals(electionResults.getRoundsRankings().get(0).get(LEOPARD), 4.0);
        assertEquals(electionResults.getRoundsRankings().get(0).get(BUFFALO), 5.0);
        assertEquals(electionResults.getRoundsRankings().get(0).get(LYNX), 5.0);

        assertEquals(electionResults.getRoundsRankings().get(1).get(BUFFALO), 1.0/3);
        assertEquals(electionResults.getRoundsRankings().get(1).get(TIGER), 2.0/3);

        System.out.println(electionResults.serialize(true, QueryType.NATIONAL));
    }


    @Test
    public void SPAVSystemTest() throws NoVotesException {
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

        SPAVSystem spavSystem = new SPAVSystem(Arrays.asList(v1,v2,v3));
        ElectionResults electionResults = spavSystem.getResults();

        assertEquals(electionResults.getWinners().get(0), LYNX);
        assertEquals(electionResults.getRoundsRankings().get(0).get(BUFFALO), 1.0);
        assertEquals(electionResults.getRoundsRankings().get(0).get(LEOPARD), 2.0);
        assertEquals(electionResults.getRoundsRankings().get(0).get(TIGER), 3.0);
        assertEquals(electionResults.getRoundsRankings().get(0).get(LYNX), 3.0);
        assertEquals(electionResults.getRoundsRankings().get(0).get(OWL), 1.0);

        assertEquals(electionResults.getWinners().get(1), TIGER);
        assertEquals(electionResults.getRoundsRankings().get(1).get(BUFFALO), 0.5);
        assertEquals(electionResults.getRoundsRankings().get(1).get(TIGER), 1.5);
        assertEquals(electionResults.getRoundsRankings().get(1).get(OWL), 0.5);
        assertEquals(electionResults.getRoundsRankings().get(1).get(LEOPARD), 1.0);

        assertEquals(electionResults.getWinners().get(2), LEOPARD);
        assertEquals(electionResults.getRoundsRankings().get(2).get(BUFFALO), 1.0/3);
        assertEquals(electionResults.getRoundsRankings().get(2).get(OWL), 1.0/3);
        assertEquals(electionResults.getRoundsRankings().get(2).get(LEOPARD), 2.0/3);

        System.out.println(electionResults.serialize(true, QueryType.PROVINCIAL));
    }

    @Test(expected = NoVotesException.class)
    public void NoVotesYetInFPTP() throws NoVotesException {
        FPTPSystem fptpSystem = new FPTPSystem(new LinkedList<>());
        fptpSystem.getResults();
    }

    @Test(expected = NoVotesException.class)
    public void NoVotesYetInSPAV() throws NoVotesException {
        SPAVSystem spavSystem = new SPAVSystem(new LinkedList<>());
        spavSystem.getResults();
    }

    @Test(expected = NoVotesException.class)
    public void NoVotesYetInSTAR() throws NoVotesException {
        STARSystem starSystem = new STARSystem(new LinkedList<>());
        starSystem.getResults();
    }
}
