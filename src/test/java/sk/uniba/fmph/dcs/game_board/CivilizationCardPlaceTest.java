package sk.uniba.fmph.dcs.game_board;

import junit.framework.TestCase;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.*;

package sk.uniba.fmph.dcs.game_board;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class CivilizationCardPlaceTest {
    private CivilizationCardPlace cardPlace;
    private MockPlayer player;
    private MockCivilizationCardDeck deck;
    private MockEvaluator evaluator;
    private List<PlayerOrder> figures;

    private static class MockPlayer implements Player {
        private final PlayerOrder order;
        private final MockPlayerBoard board;

        public MockPlayer(int orderNum, MockPlayerBoard board) {
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

    private static class MockPlayerBoard implements InterfacePlayerBoardGameBoard {
        private boolean hasFiguresResponse = true;
        private boolean takeResourcesResponse = true;

        @Override
        public boolean hasFigures(int count) {
            return hasFiguresResponse;
        }

        @Override
        public boolean takeFigures(int count) {
            return hasFiguresResponse;
        }

        @Override
        public boolean takeResources(Collection<Effect> stuff) {
            return takeResourcesResponse;
        }

        @Override
        public void giveEffect(Collection<Effect> stuff) {}

        @Override
        public void giveFigure() {}

        @Override
        public void giveEndOfGameEffect(Collection<EndOfGameEffect> stuff) {}

        @Override
        public void giveCard(CivilizationCard card) {}

        @Override
        public boolean hasSufficientTools(int goal) {
            return false;
        }

        @Override
        public Optional<Integer> useTool(int idx) {
            return Optional.empty();
        }

        public void setHasFiguresResponse(boolean response) {
            this.hasFiguresResponse = response;
        }

        public void setTakeResourcesResponse(boolean response) {
            this.takeResourcesResponse = response;
        }
    }

    private static class MockCivilizationCardDeck extends CivilizationCardDeck {
        private CivilizationCard topCard;

        @Override
        public Optional<CivilizationCard> getTop() {
            return Optional.ofNullable(topCard);
        }

        public void setTopCard(CivilizationCard card) {
            this.topCard = card;
        }
    }

    private static class MockEvaluator implements EvaluateCivilizationCardImmediateEffect {
        private boolean effectPerformed = false;

        @Override
        public boolean performEffect(Player player, Effect choice) {
            effectPerformed = true;
            return true;
        }

        public boolean wasEffectPerformed() {
            return effectPerformed;
        }
    }

    @Before
    public void setUp() {
        figures = new ArrayList<>();
        deck = new MockCivilizationCardDeck();
        evaluator = new MockEvaluator();
        MockPlayerBoard board = new MockPlayerBoard();
        player = new MockPlayer(0, board);
        cardPlace = new CivilizationCardPlace(2, figures, deck, evaluator);
    }

    @Test
    public void testPlaceFiguresSuccess() {
        assertTrue(cardPlace.placeFigures(player, 1));
        assertTrue(figures.contains(player.playerOrder()));
    }

    @Test
    public void testPlaceFiguresNoFigures() {
        ((MockPlayerBoard)player.playerBoard()).setHasFiguresResponse(false);
        assertFalse(cardPlace.placeFigures(player, 1));
        assertTrue(figures.isEmpty());
    }

    @Test
    public void testMakeActionSuccess() {
        figures.add(player.playerOrder());
        CivilizationCard card = new CivilizationCard(
                List.of(ImmediateEffect.FOOD),
                List.of(EndOfGameEffect.FARMER)
        );
        deck.setTopCard(card);

        Collection<Effect> input = new ArrayList<>();
        input.add(Effect.WOOD);
        input.add(Effect.WOOD);
        Collection<Effect> output = new ArrayList<>();

        assertEquals(ActionResult.ACTION_DONE,
                cardPlace.makeAction(player, input, output));
        assertTrue(evaluator.wasEffectPerformed());
    }

    @Test
    public void testMakeActionInsufficientResources() {
        figures.add(player.playerOrder());
        ((MockPlayerBoard)player.playerBoard()).setTakeResourcesResponse(false);

        Collection<Effect> input = new ArrayList<>();
        input.add(Effect.WOOD);
        Collection<Effect> output = new ArrayList<>();

        assertEquals(ActionResult.FAILURE,
                cardPlace.makeAction(player, input, output));
    }

    @Test
    public void testNewTurnResetsState() {
        figures.add(player.playerOrder());
        cardPlace.newTurn();
        assertTrue(figures.isEmpty());

        // Should be able to place figures again after reset
        assertTrue(cardPlace.placeFigures(player, 1));
    }
}

//public class CivilizationCardPlaceTest extends TestCase {
//    private CivilizationCardPlace civilizationCardPlace;
//    private List<PlayerOrder> figures;
//    private CivilizationCardDeck mockDeck;
//    private EvaluateCivilizationCardImmediateEffect mockEvaluator;
//    private Player mockPlayer;
//    private PlayerBoard mockPlayerBoard;
//    private static final int REQUIRED_RESOURCES = 2;
//
//    protected void setUp() {
//        figures = new ArrayList<>();
//        mockDeck = new MockCivilizationCardDeck();
//        mockPlayerBoard = new MockPlayerBoard();
//        mockPlayer = new MockPlayer(0, mockPlayerBoard);
//
//        civilizationCardPlace = new CivilizationCardPlace(
//                REQUIRED_RESOURCES,
//                figures,
//                mockDeck
//        );
//    }
//
//    // Figure Placement Tests
//    public void testPlaceFiguresSuccessful() {
//        assertTrue("Should successfully place figures",
//                civilizationCardPlace.placeFigures(mockPlayer, 1));
//        assertEquals("Should have one figure placed",
//                1, figures.size());
//        assertEquals("Should contain player's order",
//                mockPlayer.playerOrder(), figures.get(0));
//    }
//
//    public void testPlaceFiguresWhenLocationOccupied() {
//        civilizationCardPlace.placeFigures(mockPlayer, 1);
//        Player secondPlayer = new MockPlayer(1, new MockPlayerBoard());
//
//        assertFalse("Should fail to place figures in occupied location",
//                civilizationCardPlace.placeFigures(secondPlayer, 1));
//    }
//
//    public void testPlaceFiguresWithInsufficientFigures() {
//        ((MockPlayerBoard) mockPlayerBoard).setHasFigures(false);
//        assertFalse("Should fail when player has insufficient figures",
//                civilizationCardPlace.placeFigures(mockPlayer, 1));
//    }
//
//    // Try To Place Figures Tests
//    public void testTryToPlaceFiguresWithNullPlayer() {
//        assertEquals("Should return NO_ACTION_POSSIBLE for null player",
//                HasAction.NO_ACTION_POSSIBLE,
//                civilizationCardPlace.tryToPlaceFigures(null, 1));
//    }
//
//    public void testTryToPlaceFiguresWithInvalidCount() {
//        assertEquals("Should return NO_ACTION_POSSIBLE for count < 1",
//                HasAction.NO_ACTION_POSSIBLE,
//                civilizationCardPlace.tryToPlaceFigures(mockPlayer, 0));
//
//        assertEquals("Should return NO_ACTION_POSSIBLE for count > 10",
//                HasAction.NO_ACTION_POSSIBLE,
//                civilizationCardPlace.tryToPlaceFigures(mockPlayer, 11));
//    }
//
//    // Make Action Tests
//    public void testMakeActionSuccessful() {
//        civilizationCardPlace.placeFigures(mockPlayer, 1);
//        Collection<Effect> inputResources = Arrays.asList(
//                Effect.FOOD, Effect.FOOD
//        );
//
//        assertEquals("Should successfully make action",
//                ActionResult.ACTION_DONE,
//                civilizationCardPlace.makeAction(mockPlayer, inputResources, new ArrayList<>()));
//    }
//
//    public void testMakeActionWithoutPlacedFigure() {
//        Collection<Effect> inputResources = Arrays.asList(
//                Effect.FOOD,
//                Effect.FOOD
//        );
//
//        assertEquals("Should fail when no figure is placed",
//                ActionResult.FAILURE,
//                civilizationCardPlace.makeAction(mockPlayer, inputResources, new ArrayList<>()));
//    }
//
//    public void testMakeActionWithInsufficientResources() {
//        civilizationCardPlace.placeFigures(mockPlayer, 1);
//        Collection<Effect> inputResources = Arrays.asList(
//                Effect.FOOD
//        );
//
//        assertEquals("Should fail with insufficient resources",
//                ActionResult.FAILURE,
//                civilizationCardPlace.makeAction(mockPlayer, inputResources, new ArrayList<>()));
//    }
//
//    public void testMakeActionTwiceInSameTurn() {
//        civilizationCardPlace.placeFigures(mockPlayer, 1);
//        Collection<Effect> inputResources = Arrays.asList(
//                Effect.FOOD,
//                Effect.FOOD
//        );
//
//        civilizationCardPlace.makeAction(mockPlayer, inputResources, new ArrayList<>());
//        assertEquals("Should fail on second action attempt",
//                ActionResult.FAILURE,
//                civilizationCardPlace.makeAction(mockPlayer, inputResources, new ArrayList<>()));
//    }
//
//    // Skip Action Tests
//    public void testSkipAction() {
//        civilizationCardPlace.placeFigures(mockPlayer, 1);
//        assertTrue("Should successfully skip action",
//                civilizationCardPlace.skipAction(mockPlayer));
//        assertTrue("Figures should be empty after skip",
//                figures.isEmpty());
//    }
//
//    // New Turn Tests
//    public void testNewTurn() {
//        civilizationCardPlace.placeFigures(mockPlayer, 1);
//        civilizationCardPlace.makeAction(mockPlayer,
//                Arrays.asList(Effect.FOOD, Effect.FOOD),
//                new ArrayList<>());
//
//        assertTrue("Should successfully start new turn",
//                civilizationCardPlace.newTurn());
//        assertTrue("Figures should be cleared",
//                figures.isEmpty());
//        assertFalse("Action made should be reset",
//                ((MockCivilizationCardPlace) civilizationCardPlace).isActionMade());
//    }
//
//    // Mock Classes
//    private class MockCivilizationCardDeck extends CivilizationCardDeck {
//        @Override
//        public Optional<CivilizationCard> getTop() {
//            return Optional.of(new CivilizationCard(new ArrayList<>(), new ArrayList<>()));
//        }
//    }
//
//    private class MockPlayerBoard implements PlayerBoard {
//        private boolean hasFiguresResponse = true;
//        public void setHasFigures(boolean value) {
//            this.hasFiguresResponse = value;
//        }
//        @Override
//        public boolean hasFigures(int count) {
//            return hasFiguresResponse;
//        }
//
//        @Override
//        public void giveEffect(Collection<Effect> stuff) {
//            ;
//        }
//
//        public void setHasFiguresResponse(boolean response) {
//            this.hasFiguresResponse = response;
//        }
//    }
//
//    private class MockCivilizationCardPlace extends CivilizationCardPlace {
//        public MockCivilizationCardPlace(int requiredResources, List<PlayerOrder> figures,
//                                         CivilizationCardDeck deck) {
//            super(requiredResources, figures, deck);
//        }
//
//        public boolean isActionMade() {
//            return super.newTurn();
//        }
//    }
//
//    private class MockPlayer implements Player {
//        private final PlayerOrder order;
//        private final PlayerBoard board;
//
//        public MockPlayer(int orderNum, PlayerBoard board) {
//            this.order = new PlayerOrder(orderNum, 2);
//            this.board = board;
//        }
//
//        @Override
//        public PlayerOrder playerOrder() {
//            return order;
//        }
//
//        @Override
//        public PlayerBoard playerBoard() {
//            return board;
//        }
//    }
//}