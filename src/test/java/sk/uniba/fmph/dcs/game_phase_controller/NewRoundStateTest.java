package sk.uniba.fmph.dcs.game_phase_controller;

import junit.framework.TestCase;
import sk.uniba.fmph.dcs.stone_age.*;

public class NewRoundStateTest extends TestCase {

    private NewRoundState newRoundState;
    private MockNewTurn mockNewTurn;
    private PlayerOrder player;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // Initialize player
        player = new PlayerOrder(0, 3);

        // Initialize mock InterfaceNewTurn
        mockNewTurn = new MockNewTurn();

        // Initialize NewRoundState with mockNewTurn
        newRoundState = new NewRoundState(mockNewTurn);
    }

    public void testTryToMakeAutomaticAction() {
        // Test that the new round is initialized and returns AUTOMATIC_ACTION_DONE
        HasAction hasAction = newRoundState.tryToMakeAutomaticAction(player);
        assertEquals(HasAction.AUTOMATIC_ACTION_DONE, hasAction);
        assertTrue(mockNewTurn.isNewTurnCalled());

        // Test that calling again returns NO_ACTION_POSSIBLE
        hasAction = newRoundState.tryToMakeAutomaticAction(player);
        assertEquals(HasAction.NO_ACTION_POSSIBLE, hasAction);

        // Simulate game ended
        mockNewTurn.setGameEnded(true);
        // Re-initialize NewRoundState to reset roundInitialized
        newRoundState = new NewRoundState(mockNewTurn);
        hasAction = newRoundState.tryToMakeAutomaticAction(player);
        assertEquals(HasAction.NO_ACTION_POSSIBLE, hasAction);
    }

    // Mock implementation of InterfaceNewTurn for testing
    private static class MockNewTurn implements InterfaceNewTurn {
        private boolean newTurnCalled = false;
        private boolean gameEnded = false;

        @Override
        public boolean newTurn() {
            newTurnCalled = true;
            return gameEnded;
        }

        public boolean isNewTurnCalled() {
            return newTurnCalled;
        }

        public void setGameEnded(boolean gameEnded) {
            this.gameEnded = gameEnded;
        }
    }
}