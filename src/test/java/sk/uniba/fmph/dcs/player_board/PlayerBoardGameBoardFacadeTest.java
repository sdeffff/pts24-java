package sk.uniba.fmph.dcs.player_board;

import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.TribeFedStatus;

import java.util.*;

import static org.junit.Assert.*;

public class PlayerBoardGameBoardFacadeTest {
    @Test
    public void testFeedTribeWithExactFood() {
        PlayerResourcesAndFood resourcesAndFood = new PlayerResourcesAndFood();
        PlayerFigures playerFigures = new PlayerFigures();
        TribeFedStatus tribeStatus = new TribeFedStatus(playerFigures);
        PlayerBoard playerBoard = new PlayerBoard(
                new PlayerCivilisationCards(),
                playerFigures,
                resourcesAndFood,
                new PlayerTools(),
                tribeStatus
        );
        PlayerBoardGameBoardFacade facade = new PlayerBoardGameBoardFacade(playerBoard);

        playerBoard.getPlayerResourcesAndFood()
                .giveResources(List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD, Effect.FOOD));

        boolean result = facade.feedTribeIfEnoughFood();

        assertTrue(result);
        assertFalse(facade.doNotFeedThisTurn());
        assertEquals(0, playerBoard.addPoints(0));
    }

    @Test
    public void testInsufficientResourcesForFeeding() {
        PlayerResourcesAndFood resourcesAndFood = new PlayerResourcesAndFood();
        PlayerFigures playerFigures = new PlayerFigures();
        TribeFedStatus tribeStatus = new TribeFedStatus(playerFigures);
        PlayerBoard playerBoard = new PlayerBoard(
                new PlayerCivilisationCards(),
                playerFigures,
                resourcesAndFood,
                new PlayerTools(),
                tribeStatus
        );
        PlayerBoardGameBoardFacade facade = new PlayerBoardGameBoardFacade(playerBoard);

        playerBoard.getPlayerResourcesAndFood()
                .giveResources(List.of(Effect.WOOD, Effect.CLAY, Effect.STONE));

        boolean result = facade.feedTribeIfEnoughFood();

        assertFalse(result);
        assertTrue(facade.doNotFeedThisTurn());
        assertEquals(-10, playerBoard.addPoints(0));
    }

    @Test
    public void testDoNotFeedTribeForCurrentTurn() {
        PlayerResourcesAndFood resourcesAndFood = new PlayerResourcesAndFood();
        PlayerFigures playerFigures = new PlayerFigures();
        TribeFedStatus tribeStatus = new TribeFedStatus(playerFigures);
        PlayerBoard playerBoard = new PlayerBoard(
                new PlayerCivilisationCards(),
                playerFigures,
                resourcesAndFood,
                new PlayerTools(),
                tribeStatus
        );
        PlayerBoardGameBoardFacade facade = new PlayerBoardGameBoardFacade(playerBoard);

        playerBoard.getPlayerResourcesAndFood()
                .giveResources(List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD, Effect.FOOD, Effect.FOOD));
        playerBoard.getPlayerResourcesAndFood()
                .giveResources(List.of(Effect.WOOD, Effect.CLAY, Effect.STONE, Effect.GOLD, Effect.WOOD));
        Collection<Effect> materials = Arrays.asList(Effect.WOOD, Effect.CLAY, Effect.STONE, Effect.GOLD);

        assertFalse(facade.doNotFeedThisTurn());

        facade.feedTribeIfEnoughFood();

        assertFalse(facade.doNotFeedThisTurn());
        assertEquals(0, playerBoard.addPoints(0));

        facade.newTurn();
        assertFalse(facade.feedTribeIfEnoughFood());
        assertTrue(facade.doNotFeedThisTurn());
        assertEquals(-10, playerBoard.addPoints(0));

        facade.newTurn();
        assertFalse(facade.feedTribeIfEnoughFood());
        assertFalse(facade.doNotFeedThisTurn());
        assertEquals(-10, playerBoard.addPoints(0));

        facade.newTurn();
        assertFalse(facade.feedTribeIfEnoughFood());
        assertFalse(facade.feedTribe(materials));
        assertTrue(facade.doNotFeedThisTurn());
        assertEquals(-20, playerBoard.addPoints(0));
    }
}
