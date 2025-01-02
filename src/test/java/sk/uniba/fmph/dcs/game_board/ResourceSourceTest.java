package sk.uniba.fmph.dcs.game_board;

import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.OptionalInt;

import static org.junit.Assert.*;

public class ResourceSourceTest {

    private Player mockPlayer1;
    private Player mockPlayer2;
    private Player mockPlayer3;
    private Player mockPlayer4;
    private MockPlayerBoard playerBoard1;
    private MockPlayerBoard playerBoard2;
    private MockPlayerBoard playerBoard3;
    private MockPlayerBoard playerBoard4;
    private CurrentThrow currentThrow;

    @Before
    public void setUp() {
        playerBoard1 = new MockPlayerBoard();
        playerBoard2 = new MockPlayerBoard();
        playerBoard3 = new MockPlayerBoard();
        playerBoard4 = new MockPlayerBoard();

        mockPlayer1 = new MockPlayer(new PlayerOrder(0, 4), playerBoard1);
        mockPlayer2 = new MockPlayer(new PlayerOrder(1, 4), playerBoard2);
        mockPlayer3 = new MockPlayer(new PlayerOrder(2, 4), playerBoard3);
        mockPlayer4 = new MockPlayer(new PlayerOrder(3, 4), playerBoard4);

        currentThrow = new CurrentThrow();
    }

    @Test
    public void testPlaceFiguresWhenNoFiguresAvailable() {
        ResourceSource forestResource = new ResourceSource("Forest", Effect.WOOD, 7, 4);

        playerBoard1.hasFiguresFlag = false;
        playerBoard2.hasFiguresFlag = false;

        assertFalse(forestResource.placeFigures(mockPlayer1, 2));
        assertFalse(forestResource.placeFigures(mockPlayer2, 3));

        assertEquals(0, playerBoard1.figuresTaken);
        assertEquals(0, playerBoard2.figuresTaken);
    }

    @Test
    public void testPlaceFiguresSuccessfully() {
        ResourceSource forestResource = new ResourceSource("Forest", Effect.WOOD, 7, 4);

        playerBoard1.hasFiguresFlag = true;
        playerBoard2.hasFiguresFlag = true;

        assertTrue(forestResource.placeFigures(mockPlayer1, 2));
        assertEquals(2, playerBoard1.figuresTaken);

        assertTrue(forestResource.placeFigures(mockPlayer2, 3));
        assertEquals(3, playerBoard2.figuresTaken);
    }

    @Test
    public void testPlaceFiguresExceedsMaxFigures() {
        ResourceSource forestResource = new ResourceSource("Forest", Effect.WOOD, 7, 4);

        playerBoard1.hasFiguresFlag = true;

        // Place figures successfully within limit
        assertTrue(forestResource.placeFigures(mockPlayer1, 4));
        assertEquals(4, playerBoard1.figuresTaken);

        // Exceed max allowed figures
        assertFalse(forestResource.placeFigures(mockPlayer1, 4));
        assertEquals(4, playerBoard1.figuresTaken); // Figures taken should not increase
    }

    @Test
    public void testPlaceFiguresExceedsMaxColors() {
        ResourceSource forestResource = new ResourceSource("Forest", Effect.WOOD, 7, 2);

        playerBoard1.hasFiguresFlag = true;
        playerBoard2.hasFiguresFlag = true;
        playerBoard3.hasFiguresFlag = true;

        assertTrue(forestResource.placeFigures(mockPlayer1, 3));
        assertTrue(forestResource.placeFigures(mockPlayer2, 3));

        // Third player's figures should not be placed due to color limit
        assertFalse(forestResource.placeFigures(mockPlayer3, 1));
    }

    @Test
    public void testMakeActionSuccessfully() {
        ResourceSource forestResource = new ResourceSource("Forest", Effect.WOOD, 7, 4);

        playerBoard1.hasFiguresFlag = true;

        assertTrue(forestResource.placeFigures(mockPlayer1, 3));
        Collection<Effect> outputResources = new ArrayList<>();
        assertEquals(ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE, forestResource.makeAction(mockPlayer1, null, outputResources));
        assertEquals(-3, playerBoard1.figuresTaken);
    }

    @Test
    public void testMakeActionFailure() {
        ResourceSource forestResource = new ResourceSource("Forest", Effect.WOOD, 7, 4);

        playerBoard1.hasFiguresFlag = true;

        assertTrue(forestResource.placeFigures(mockPlayer1, 3));
        Collection<Effect> outputResources = new ArrayList<>();
        assertEquals(ActionResult.FAILURE, forestResource.makeAction(mockPlayer1, null, outputResources));
        assertEquals(-3, playerBoard1.figuresTaken);
    }

    @Test
    public void testSkipActionSuccessfully() {
        ResourceSource forestResource = new ResourceSource("Forest", Effect.WOOD, 7, 4);

        playerBoard1.hasFiguresFlag = true;

        assertTrue(forestResource.placeFigures(mockPlayer1, 3));
        assertTrue(forestResource.skipAction(mockPlayer1));

        assertEquals(0, playerBoard1.figuresTaken);
    }

    @Test
    public void testStateSerialization() {
        ResourceSource forestResource = new ResourceSource("Forest", Effect.WOOD, 7, 4);
        playerBoard1.hasFiguresFlag = true;

        forestResource.placeFigures(mockPlayer1, 3);

        String state = forestResource.state();
        assertTrue(state.contains("Forest"));
        assertTrue(state.contains("WOOD"));
        assertTrue(state.contains("3"));
    }

    private static class MockPlayerBoard implements InterfacePlayerBoardGameBoard {
        public boolean hasFiguresFlag;
        public int figuresTaken = 0;

        @Override
        public void giveEffect(Collection<Effect> stuff) {
        }

        @Override
        public void giveEndOfGameEffect(Collection<EndOfGameEffect> stuff) {
        }

        @Override
        public boolean takeResources(Collection<Effect> stuff) {
            return false;
        }

        @Override
        public void giveCard(CivilizationCard card) {

        }

        @Override
        public void giveFigure() {}

        @Override
        public boolean takeFigures(int count) {
            figuresTaken += count;
            return hasFiguresFlag;
        }

        @Override
        public boolean hasFigures(int count) {
            return hasFiguresFlag;
        }

        @Override
        public boolean hasSufficientTools(int goal) {
            return false;
        }

        @Override
        public Optional<Optional<Integer>> useTool(int idx) {
            OptionalInt optionalInt = OptionalInt.of(1);

            Optional<Integer> optionalInteger = optionalInt.isPresent() ?
                    Optional.of(optionalInt.getAsInt()) :
                    Optional.empty();

            return Optional.of(optionalInteger);
        }
    }

    private static class MockPlayer implements Player {
        private final PlayerOrder order;
        private final InterfacePlayerBoardGameBoard playerBoard;

        public MockPlayer(PlayerOrder order, InterfacePlayerBoardGameBoard playerBoard) {
            this.order = order;
            this.playerBoard = playerBoard;
        }

        @Override
        public PlayerOrder playerOrder() {
            return order;
        }

        @Override
        public InterfacePlayerBoardGameBoard playerBoard() {
            return playerBoard;
        }
    }
}
