package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.*;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class CivilizationCardPlaceTest {
    private CivilizationCardPlace cardPlace;
    private MockPlayer player;
    private MockCivilizationCardDeck deck;
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

        @Override
        public boolean performEffect(Player player, Effect choice) {
            return true;
        }
    }

    @Before
    public void setUp() {
        figures = new ArrayList<>();
        deck = new MockCivilizationCardDeck();
        MockEvaluator evaluator = new MockEvaluator();
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