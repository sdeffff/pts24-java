package sk.uniba.fmph.dcs.stone_age;

import junit.framework.TestCase;
import sk.uniba.fmph.dcs.game_board.*;
import sk.uniba.fmph.dcs.game_board.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class ResourceSourceTest extends TestCase {
    private ResourceSource source;
    private MockPlayer mockPlayer1;
    private MockPlayer mockPlayer2;
    private MockPlayerBoard mockBoard1;
    private MockPlayerBoard mockBoard2;

    // Mock implementation for PlayerBoard
    private class MockPlayerBoard implements InterfacePlayerBoardGameBoard {
        private boolean hasFiguresResponse = true;

        @Override
        public boolean hasFigures(int count) {
            return hasFiguresResponse;
        }

        @Override
        public void giveEffect(Collection<Effect> stuff) {
            // Mock implementation, does nothing
        }

        @Override
        public void giveFigure() {
            // Mock implementation, does nothing
        }

        @Override
        public void giveEndOfGameEffect(Collection<EndOfGameEffect> stuff) {
            // Mock implementation, does nothing
        }

        @Override
        public void giveCard(CivilizationCard card) {
            // Mock implementation, does nothing
        }

        @Override
        public boolean takeResources(Collection<Effect> stuff) {
            return true; // Default mock behavior
        }

        @Override
        public boolean takeFigures(int count) {
            return true; // Default mock behavior
        }

        @Override
        public boolean hasSufficientTools(int goal) {
            return true; // Default mock behavior
        }

        @Override
        public Optional<Integer> useTool(int idx) {
            return Optional.of(1); // Default mock behavior
        }

        public void setHasFiguresResponse(boolean response) {
            this.hasFiguresResponse = response;
        }
    }


    // Mock implementation for Player
    private class MockPlayer implements Player {
        private final PlayerOrder order;
        private final InterfacePlayerBoardGameBoard board;

        public MockPlayer(int orderNum, InterfacePlayerBoardGameBoard board) {
            this.order = new PlayerOrder(orderNum, 2); // Ensure PlayerOrder exists and is correctly implemented
            this.board = board;
        }

        @Override
        public PlayerOrder playerOrder() {
            return order;
        }

        @Override
        public InterfacePlayerBoardGameBoard playerBoard() {
            return (InterfacePlayerBoardGameBoard) board; // Adjust if InterfacePlayerBoardGameBoard is required
        }
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mockBoard1 = new MockPlayerBoard();
        mockBoard2 = new MockPlayerBoard();
        mockPlayer1 = new MockPlayer(0, mockBoard1);
        mockPlayer2 = new MockPlayer(1, mockBoard2);
        source = new ResourceSource("Forest", Effect.WOOD, 7, 2); // Ensure Effect.WOOD is defined
    }

    public void testPlaceFiguresSuccess() {
        assertTrue(source.placeFigures(mockPlayer1, 3));
    }

    public void testPlaceFiguresNoFigures() {
        mockBoard1.setHasFiguresResponse(false);
        assertFalse(source.placeFigures(mockPlayer1, 3));
    }

    public void testPlaceFiguresExceedsMaxFigures() {
        assertTrue(source.placeFigures(mockPlayer1, 4));
        assertFalse(source.placeFigures(mockPlayer2, 4)); // Exceeds max figures
    }

    public void testMakeActionSuccess() {
        source.placeFigures(mockPlayer1, 2);
        Collection<Effect> input = new ArrayList<>();
        Collection<Effect> output = new ArrayList<>();
        output.add(Effect.WOOD);
        output.add(Effect.WOOD);

        assertEquals(ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE,
                source.makeAction(mockPlayer1, input, output));
    }

    public void testMakeActionWrongResourceCount() {
        source.placeFigures(mockPlayer1, 2);
        Collection<Effect> input = new ArrayList<>();
        Collection<Effect> output = new ArrayList<>();
        output.add(Effect.WOOD);

        assertEquals(ActionResult.FAILURE,
                source.makeAction(mockPlayer1, input, output));
    }

    public void testMakeActionWrongResourceType() {
        source.placeFigures(mockPlayer1, 2);
        Collection<Effect> input = new ArrayList<>();
        Collection<Effect> output = new ArrayList<>();
        output.add(Effect.FOOD); // Wrong resource type
        output.add(Effect.FOOD);

        assertEquals(ActionResult.FAILURE,
                source.makeAction(mockPlayer1, input, output));
    }

    public void testNewTurnClearsFigures() {
        source.placeFigures(mockPlayer1, 3);
        source.newTurn();
        assertTrue(source.placeFigures(mockPlayer1, 3)); // Figures cleared, can place again
    }

    public void testTryToPlaceFigures() {
        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION,
                source.tryToPlaceFigures(mockPlayer1, 2));
    }

    public void testTryToMakeAction() {
        source.placeFigures(mockPlayer1, 2);
        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION,
                source.tryToMakeAction(mockPlayer1));
    }

    public void testSkipActionNotAllowed() {
        assertFalse(source.skipAction(mockPlayer1));
    }
}
