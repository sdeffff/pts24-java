package sk.uniba.fmph.dcs.player_board;

import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.EndOfGameEffect;
import sk.uniba.fmph.dcs.stone_age.TribeFedStatus;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Integration test for PlayerBoard and its dependencies.
 */
public class PlayerBoardTest {
    /**
     * Tests the full interaction of the PlayerBoard and its components in a game-like scenario.
     */
    @Test
    public void testPlayerBoardIntegration() {
        PlayerBoard playerBoard = new PlayerBoard();

        assertEquals(0, playerBoard.addPoints(0));
        assertEquals(0, playerBoard.getPlayerFigures().getTotalFigures());
        assertFalse(playerBoard.getTribeFedStatus().isTribeFed());

        playerBoard.getPlayerFigures().addNewFigure();
        playerBoard.getPlayerFigures().addNewFigure();
        assertEquals(2, playerBoard.getPlayerFigures().getTotalFigures());

        playerBoard.getPlayerResourcesAndFood()
                .giveResources(List.of(Effect.FOOD, Effect.FOOD));
        assertTrue(playerBoard.getPlayerResourcesAndFood().hasResources(List.of(Effect.FOOD, Effect.FOOD)));

        boolean fed = playerBoard.getTribeFedStatus().feedTribeIfEnoughFood();
        assertTrue(fed);
        assertTrue(playerBoard.getTribeFedStatus().isTribeFed());

        playerBoard.getPlayerTools().addTool();
        playerBoard.getPlayerTools().addTool();
        assertTrue(playerBoard.getPlayerTools().hasSufficientTools(2));

        playerBoard.getPlayerTools().useTool(0);
        assertFalse(playerBoard.getPlayerTools().hasSufficientTools(2));

        playerBoard.getPlayerCivilisationCards()
                .addEndOfGameEffects(new EndOfGameEffect[]{
                        EndOfGameEffect.SHAMAN, EndOfGameEffect.FARMER
                });
        assertEquals(0, playerBoard.addPoints(0));

        assertEquals(5, playerBoard.addPoints(5));
        assertEquals(0, playerBoard.addPoints(-5));

        playerBoard.addHouse();
        playerBoard.addHouse();
        assertEquals(2, playerBoard.getPlayerCivilisationCards()
                .calculateEndOfGameCivilisationCardsPoints(2, 0, 1, 2));

        playerBoard.getPlayerResourcesAndFood()
                .giveResources(List.of(Effect.WOOD, Effect.CLAY, Effect.STONE));
        assertTrue(playerBoard.getPlayerResourcesAndFood().hasResources(List.of(Effect.WOOD, Effect.CLAY, Effect.STONE)));

        playerBoard.addEndOfGamePoints();
        assertTrue(playerBoard.state().contains("\"points\""));

        playerBoard.newTurn();
        assertFalse(playerBoard.getTribeFedStatus().isTribeFed());
        assertTrue(playerBoard.getPlayerFigures().hasFigures(2));
        assertTrue(playerBoard.getPlayerTools().hasSufficientTools(1));
    }

    /**
     * Tests serialization of the PlayerBoard state to JSON.
     */
    @Test
    public void testStateSerialization() {
        PlayerBoard playerBoard = new PlayerBoard();

        playerBoard.getPlayerFigures().addNewFigure();
        playerBoard.getPlayerTools().addTool();
        playerBoard.addHouse();
        playerBoard.addPoints(15);
        playerBoard.getPlayerResourcesAndFood()
                .giveResources(List.of(Effect.FOOD, Effect.WOOD));

        String state = playerBoard.state();
        assertTrue(state.contains("\"points\":15"));
        assertTrue(state.contains("\"houses\":1"));
        assertTrue(state.contains("\"resourcesAndFood\""));
        assertTrue(state.contains("\"figures\""));
        assertTrue(state.contains("\"tools\""));
        assertTrue(state.contains("\"tribeFeedingStatus\""));
    }
}
