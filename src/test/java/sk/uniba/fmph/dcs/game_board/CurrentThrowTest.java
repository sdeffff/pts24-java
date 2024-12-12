package sk.uniba.fmph.dcs.game_board;

import junit.framework.TestCase;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.Collection;
import java.util.Optional;

public class CurrentThrowTest extends TestCase {
    private CurrentThrow currentThrow;
    private MockPlayer mockPlayer;
    private MockPlayerBoard mockBoard;
    private Effect mockEffect;

    // Mock implementation for PlayerBoard
    private class MockPlayerBoard implements InterfacePlayerBoardGameBoard {
        private boolean hasSufficientToolsResponse = true;
        private boolean takeResourcesCalled = false;

        @Override
        public void giveEffect(Collection<Effect> stuff) {
        }

        @Override
        public void giveFigure() {
        }

        @Override
        public void giveEndOfGameEffect(Collection<EndOfGameEffect> stuff) {
        }

        @Override
        public void giveCard(CivilizationCard card) {
        }

        @Override
        public boolean takeResources(Collection<Effect> stuff) {
            takeResourcesCalled = true;
            return true;
        }

        @Override
        public boolean takeFigures(int count) {
            return true;
        }

        @Override
        public boolean hasFigures(int count) {
            return true;
        }

        @Override
        public boolean hasSufficientTools(int goal) {
            return hasSufficientToolsResponse;
        }

        @Override
        public Optional<Integer> useTool(int idx) {
            return Optional.of(1); // Always add +1 for simplicity
        }
    }

    // Mock implementation for Player
    private class MockPlayer implements Player {
        private final PlayerOrder order;
        private final InterfacePlayerBoardGameBoard board;

        public MockPlayer(int orderNum, InterfacePlayerBoardGameBoard board) {
            this.order = new PlayerOrder(orderNum, 2);
            this.board = board;
        }

        @Override
        public PlayerOrder playerOrder() {
            return order;
        }

        @Override
        public InterfacePlayerBoardGameBoard playerBoard() {
            return board;
        }
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mockBoard = new MockPlayerBoard();
        mockPlayer = new MockPlayer(1, mockBoard);
        mockEffect = Effect.WOOD;
        currentThrow = new CurrentThrow();
    }

    public void testInitiateWithoutUsingTools() {
        mockBoard.hasSufficientToolsResponse = false; // Player can't use tools
        currentThrow.initiate(mockPlayer, mockEffect, 3); // 3 dice rolls
        assertTrue(mockBoard.takeResourcesCalled);
    }

    public void testInitiateWithUsingTools() {
        mockBoard.hasSufficientToolsResponse = true; // Player can use tools
        currentThrow.initiate(mockPlayer, mockEffect, 3); // 3 dice rolls
        assertTrue(mockBoard.takeResourcesCalled);
    }

    public void testCanUseTools() {
        mockBoard.hasSufficientToolsResponse = true;
        currentThrow.initiate(mockPlayer, mockEffect, 3); // Initialize with some dice rolls
        assertTrue(currentThrow.canUseTools());
    }

    public void testCannotUseTools() {
        mockBoard.hasSufficientToolsResponse = false;
        currentThrow.initiate(mockPlayer, mockEffect, 3); // Initialize with some dice rolls
        assertFalse(currentThrow.canUseTools());
    }

    public void testStateOutput() {
        currentThrow.initiate(mockPlayer, mockEffect, 3);
        String state = currentThrow.state();
        assertNotNull(state);
        assertTrue(state.contains("Throw result"));
    }
}