package ar.edu.itba.pod.g3.server;

import ar.edu.itba.pod.g3.api.enums.ElectionState;
import ar.edu.itba.pod.g3.api.enums.PoliticalParty;
import ar.edu.itba.pod.g3.api.enums.QueryType;
import ar.edu.itba.pod.g3.api.models.*;
import ar.edu.itba.pod.g3.server.votingSystem.ElectionManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static ar.edu.itba.pod.g3.api.enums.PoliticalParty.*;
import static ar.edu.itba.pod.g3.api.enums.PoliticalParty.TIGER;
import static ar.edu.itba.pod.g3.api.enums.Province.JUNGLE;
import static junit.framework.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ElectionManagerTest {
    public static final int BOOTH_NUMBER = 1000;
    public static final String BOOTH_STRING = "1000";
    public static final PoliticalParty POLITICAL_PARTY = LYNX;

    @InjectMocks
    private ElectionManager electionManager;

    private Collection<Vote> shortVotesCollection;

    @Before
    public void init() {
        this.shortVotesCollection = new LinkedList<>();
        Map<PoliticalParty,Integer> ranking1 = new HashMap<>();
        ranking1.put(TIGER,3);
        ranking1.put(LEOPARD,2);
        ranking1.put(LYNX,1);
        Vote v1 = new Vote(1000, JUNGLE, ranking1, TIGER);
        shortVotesCollection.add(v1);

        // todo: fix mocking of object grab for lock
        // try entering concurrently
        // Mockito.when(fiscalMapLocks[fiscal.getParty().ordinal()]).thenReturn(new Object());
    }

    /* Administration */

    /**
     * Upon attempting to open a not started election, the service should be able to start it.
     * @throws IllegalStateException if the election is in {@link ElectionState}: CLOSED
     */
    @Test
    public void testOpenNotStartedElection() throws IllegalStateException {
        // GIVEN
        // System.out.println(electionManager.getElectionState()); // not-started

        // WHEN
        boolean started = electionManager.setElectionState(ElectionState.OPEN);

        // THEN
        assertTrue("Election state should change to OPEN", started);
    }

    /**
     * Upon attempting to open a closed election, the service should throw an error.
     * @throws IllegalStateException if the election is in {@link ElectionState}: CLOSED
     */
    @Test(expected = IllegalStateException.class)
    public void testOpenClosedElection() {
        // GIVEN
        electionManager.setElectionState(ElectionState.CLOSED);

        // WHEN
        electionManager.setElectionState(ElectionState.OPEN);

        // THEN
        // Exception should be thrown that state OPEN cannot be entered once more.
    }

    /**
     * Upon attempting to close an open election, the service should be able to end it.
     * @throws IllegalStateException if the election is in {@link ElectionState}: NOT_STARTED
     */
    @Test
    public void testCloseOpenedElection() throws IllegalStateException {
        // GIVEN
        electionManager.setElectionState(ElectionState.OPEN);

        // WHEN
        boolean closed = electionManager.setElectionState(ElectionState.CLOSED);

        // THEN
        assertTrue(closed);
    }

    /**
     * Upon attempting to close an election that hasn't started an {@link IllegalStateException} is thrown.
     * @throws IllegalStateException if the election is in {@link ElectionState}: NOT_STARTED
     */
    @Test(expected = IllegalStateException.class)
    public void testCloseNotStartedElection() throws IllegalStateException {
        // GIVEN
        electionManager.setElectionState(ElectionState.NOT_STARTED);

        // WHEN
        boolean closed = electionManager.setElectionState(ElectionState.CLOSED);

        // THEN
        assertTrue(closed);
    }

    /**
     * The initial state for any manager should be {@link ElectionState}: NOT_STARTED by default.
     */
    @Test
    public void testElectionStateBeforeOpening() {
        // GIVEN
        // electionManager.setElectionState(ElectionState.NOT_STARTED);  // default

        // WHEN
        ElectionState state = electionManager.getElectionState();

        // THEN
        assertEquals(ElectionState.NOT_STARTED, state);
    }

    /* Voting */

    /**
     * Attempting to cast votes with a null collection throws a {@link IllegalArgumentException}.
     * @throws ElectionException handles casting votes to elections that are not open
     * @throws NoVotesException handles emitting of empty votes
     * @throws IllegalArgumentException handles sending null values as collection of votes
     */
    @Test(expected = IllegalArgumentException.class)
    public void testVotesCastNull() throws Exception {
        electionManager.addVotes(null);
    }

    /**
     * Attempting to cast votes with an empty list throws {@link NoVotesException}.
     * @throws ElectionException handles casting votes to elections that are not open
     * @throws NoVotesException handles emitting of empty votes
     * @throws IllegalArgumentException handles sending null values as collection of votes
     */
    @Test(expected = NoVotesException.class)
    public void testVotesCastEmpty() throws ElectionException, NoVotesException {
        // GIVEN
        Collection<Vote> votesEmpty = new LinkedList<>();
        // add votes to list

        // WHEN
        electionManager.addVotes(votesEmpty);

        // THEN
        // Exception should be thrown that no votes were submitted.
    }

    /**
     * Casting votes is the main functionality of the voting service.
     * @throws ElectionException handles casting votes to elections that are not open
     * @throws NoVotesException handles emitting of empty votes
     */
    @Test
    public void testVotesCastValid() throws NoVotesException, ElectionException {
        // GIVEN
        electionManager.setElectionState(ElectionState.OPEN);

        // WHEN
        boolean successful = electionManager.addVotes(shortVotesCollection);

        // THEN
        assertTrue("Votes should have been cast successfully.\n", successful);
    }

    /**
     * Attempting to cast votes to an election that hasn't started throws {@link ElectionException}.
     * @throws NoVotesException handles emitting of empty votes
     * @throws ElectionException handles casting votes to elections that are not open
     */
    @Test(expected = ElectionException.class)
    public void testCannotCastVotesForNotStartedElection() throws NoVotesException, ElectionException {
        electionManager.addVotes(shortVotesCollection);
    }

    /**
     * Attempting to cast votes to a closed election throws {@link ElectionException}.
     * @throws NoVotesException handles emitting of empty votes
     * @throws ElectionException handles casting votes to elections that are not open
     */
    @Test(expected = ElectionException.class)
    public void testCannotCastVotesForClosedElection() throws NoVotesException, ElectionException {
        // GIVEN
        electionManager.setElectionState(ElectionState.OPEN);
        electionManager.setElectionState(ElectionState.CLOSED);
//        Mockito.when()

        // WHEN
        electionManager.addVotes(shortVotesCollection);

        // THEN
        // Exception should be thrown that election state was not open.
    }

    /* Fiscalization */

    /**
     * Attempting to register a fiscal on an already open election throws a {@link IllegalStateException}.
     */
    @Test(expected = IllegalStateException.class)
    public void testCannotRegisterFiscalOnOpenElection() {
        // GIVEN
        electionManager.setElectionState(ElectionState.OPEN);

        // WHEN fixme
        electionManager.addFiscal(new Fiscal(BOOTH_NUMBER, POLITICAL_PARTY));

        // THEN
        // Exception should be thrown that election state was already open.
    }

    /**
     * Attempting to register a fiscal on an already closed election throws a {@link IllegalStateException}.
     */
    @Test(expected = IllegalStateException.class)
    public void testCannotRegisterFiscalOnClosedElection() {
        // GIVEN
        electionManager.setElectionState(ElectionState.OPEN);
        electionManager.setElectionState(ElectionState.CLOSED);

        // WHEN
        electionManager.addFiscal(new Fiscal(BOOTH_NUMBER, POLITICAL_PARTY));

        // THEN
        // Exception should be thrown that election state was already open.
    }

    /**
     * Registering a fiscal involves registering its political party and its booth.
     * @throws IllegalStateException if the election is not in {@link ElectionState}: NOT_STARTED
     */
    @Test
    public void testRegisterFiscalOnNotStartedElection() throws IllegalStateException {
        // GIVEN
        electionManager.setElectionState(ElectionState.NOT_STARTED);

        // WHEN
        boolean successful = electionManager.addFiscal(new Fiscal(BOOTH_NUMBER, POLITICAL_PARTY));

        // THEN
        assertTrue(successful);
//        electionManager.getFiscalMap().get(POLITICAL_PARTY).forEach((integer, fiscals) -> fiscals.forEach(
//                fiscal -> System.out.println("Party: " + fiscal.getParty() + "\nBooth: " + fiscal.getBooth())));
        List<Fiscal> fiscals = electionManager.getFiscalMap().get(POLITICAL_PARTY).get(BOOTH_NUMBER);
        assertFalse(fiscals.isEmpty());
        assertEquals(fiscals.get(0).getParty(), POLITICAL_PARTY);
        assertEquals(fiscals.get(0).getBooth(), BOOTH_NUMBER);
    }

    /**
     * Attempting to query an election that hasn't started yet throws an {@link ElectionException}.
     * @throws Exception can be {@link IllegalStateException} or {@link ElectionException} depending on the election state.
     */
    @Test(expected = ElectionException.class)
    public void testCannotQueryOnNotStartedElection() throws Exception {
        // GIVEN
        electionManager.setElectionState(ElectionState.NOT_STARTED);

        // WHEN
        electionManager.queryElection(new QueryDescriptor(BOOTH_STRING, QueryType.BOOTH));

        // THEN
        // Exception should be thrown that election had not started yet.
    }

    /**
     * Quering an open election returns only the results of the FPTP voting system.
     * @throws Exception can be {@link IllegalStateException} or {@link ElectionException} depending on the election state.
     */
    @Test
    public void testQueryShouldReturnFPTPResultsDuringOpenElection() throws Exception {
        // GIVEN
        electionManager.setElectionState(ElectionState.OPEN);
        electionManager.addVotes(shortVotesCollection);

        // WHEN
        String results = electionManager.queryElection(new QueryDescriptor(BOOTH_STRING, QueryType.BOOTH));

        // THEN
        // todo: improve test parser for FPTP voting system
        assertEquals("ElectionResults{winners=[TIGER], roundsRankings=[{TIGER=1.0}]}", results);
    }

}
