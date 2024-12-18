package sk.uniba.fmph.dcs.player_board;

import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.EndOfGameEffect;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Integration tests for PlayerBoard and PlayerBoardGameBoardFacade.
 */
public class PlayerBoardIntegrationTest {

    @Test
    public void testGameFlowWithCustomConfiguration() {
        // Create a PlayerBoard with custom configuration
        Map.Entry<PlayerBoard, PlayerBoardGameBoardFacade> boardSetup = PlayerBoardFactory.createPlayerBoard(5, 0);
        PlayerBoard playerBoard = boardSetup.getKey();
        PlayerBoardGameBoardFacade boardFacade = boardSetup.getValue();

        Effect[] startingResources = new Effect[]{Effect.FOOD, Effect.WOOD, Effect.CLAY, Effect.STONE, Effect.GOLD};
        boardFacade.giveEffect(List.of(startingResources));
        assertTrue(playerBoard.getPlayerResourcesAndFood().hasResources(List.of(startingResources)));

        assertFalse(boardFacade.isTribeFed());
        assertTrue(boardFacade.hasFigures(5));

        assertFalse(boardFacade.feedTribeIfEnoughFood());
        assertFalse(boardFacade.feedTribe(List.of()));
        assertTrue(boardFacade.doNotFeedThisTurn());
        assertTrue(boardFacade.feedTribe(List.of()));
        assertFalse(boardFacade.takeResources(List.of(new Effect[]{Effect.FOOD})));

        int currentPoints = playerBoard.addPoints(0);
        assertEquals(-10, currentPoints);

        boardFacade.newTurn();
        boardFacade.giveEffect(List.of(new Effect[]{Effect.FOOD, Effect.FOOD, Effect.FOOD, Effect.STONE, Effect.GOLD}));
        assertFalse(boardFacade.hasFigures(6));
        assertFalse(boardFacade.isTribeFed());
        assertFalse(boardFacade.feedTribe(Arrays.asList(Effect.FOOD, Effect.FOOD, Effect.CLAY, Effect.STONE, Effect.GOLD)));
        assertFalse(boardFacade.isTribeFed());

        assertTrue(boardFacade.feedTribe(Arrays.asList(Effect.FOOD, Effect.FOOD, Effect.FOOD, Effect.STONE, Effect.GOLD)));
        assertTrue(playerBoard.getPlayerResourcesAndFood().hasResources(List.of(new Effect[]{Effect.WOOD, Effect.CLAY, Effect.STONE, Effect.GOLD})));
        assertFalse(playerBoard.getPlayerResourcesAndFood().hasResources(List.of(new Effect[]{Effect.FOOD})));
        assertTrue(boardFacade.isTribeFed());

        boardFacade.giveFigure();
        assertTrue(boardFacade.hasFigures(5));

        boardFacade.newTurn();
        assertTrue(boardFacade.hasFigures(6));
        assertFalse(boardFacade.hasFigures(7));
        assertFalse(boardFacade.takeFigures(7));
        assertTrue(boardFacade.hasFigures(6));
        assertTrue(boardFacade.takeFigures(6));

        // Test feeding mechanics again
        boardFacade.giveEffect(List.of(new Effect[]{Effect.FOOD, Effect.FOOD, Effect.FOOD, Effect.FOOD, Effect.FOOD}));
        assertFalse(boardFacade.feedTribeIfEnoughFood());
        assertFalse(boardFacade.isTribeFed());
        boardFacade.giveEffect(List.of(new Effect[]{Effect.FOOD}));
        assertTrue(boardFacade.feedTribeIfEnoughFood());
        assertTrue(playerBoard.getPlayerResourcesAndFood().hasResources(List.of(new Effect[]{Effect.WOOD, Effect.CLAY, Effect.STONE, Effect.GOLD})));
        assertFalse(playerBoard.getPlayerResourcesAndFood().hasResources(List.of(new Effect[]{Effect.FOOD})));

        // Test points after end-of-game effects
        currentPoints = playerBoard.addPoints(0);
        assertEquals(-10, currentPoints);
        playerBoard.addEndOfGamePoints();
        currentPoints = playerBoard.addPoints(0);
        assertEquals(-6, currentPoints); // Adjusted points after adding end-of-game effects
    }

    @Test
    public void testGameFlowWithDefaultConfiguration() {
        // Create a PlayerBoard with default configuration
        Map.Entry<PlayerBoard, PlayerBoardGameBoardFacade> boardSetup = PlayerBoardFactory.createDefaultPlayerBoard();
        PlayerBoard playerBoard = boardSetup.getKey();
        PlayerBoardGameBoardFacade boardFacade = boardSetup.getValue();

        // Test initial state
        Effect[] initialFood = new Effect[12];
        Arrays.fill(initialFood, Effect.FOOD);
        assertTrue(playerBoard.getPlayerResourcesAndFood().hasResources(List.of(initialFood)));
        assertTrue(playerBoard.getPlayerFigures().hasFigures(5));
        assertFalse(playerBoard.getPlayerFigures().hasFigures(6));

        // Test figure management
        assertFalse(boardFacade.isTribeFed());
        assertTrue(boardFacade.takeFigures(5));
        assertFalse(boardFacade.hasFigures(1));

        // Test resource management and feeding
        Collection<Effect> testGiveEffectsArray = List.of(new Effect[]{Effect.WOOD});
        boardFacade.giveEffect(testGiveEffectsArray);
        List<Effect> insufficientFeed = Arrays.asList(Effect.FOOD, Effect.FOOD, Effect.FOOD, Effect.FOOD, Effect.WOOD);
        assertFalse(boardFacade.feedTribe(insufficientFeed));
        assertFalse(boardFacade.isTribeFed());
        assertTrue(boardFacade.feedTribeIfEnoughFood());
        assertTrue(boardFacade.isTribeFed());

        // Test tool management
        assertTrue(boardFacade.hasSufficientTools(0));
        playerBoard.getPlayerTools().addSingleUseTool(4); // Add a temporary tool
        int currentPoints = playerBoard.addPoints(0);
        assertEquals(0, currentPoints);

        assertFalse(boardFacade.hasSufficientTools(5));
        playerBoard.getPlayerTools().addTool(); // Add a reusable tool
        assertTrue(boardFacade.hasSufficientTools(5));

        Optional<Optional<Integer>> toolEffect = boardFacade.useTool(3);
        assertTrue(toolEffect.isPresent());
        assertEquals(4, toolEffect.get().get().intValue());

        toolEffect = boardFacade.useTool(0);
        assertTrue(toolEffect.isPresent());
        assertEquals(1, toolEffect.get().get().intValue());

        boardFacade.giveEndOfGameEffect(List.of(new EndOfGameEffect[]{EndOfGameEffect.WRITING, EndOfGameEffect.ART}));
        boardFacade.giveEndOfGameEffect(List.of(new EndOfGameEffect[]{EndOfGameEffect.TOOL_MAKER}));
        boardFacade.giveEndOfGameEffect(List.of(new EndOfGameEffect[]{EndOfGameEffect.SHAMAN, EndOfGameEffect.SHAMAN}));

        playerBoard.addEndOfGamePoints();
        currentPoints = playerBoard.addPoints(0);
        assertEquals(3 + 4 + 10, currentPoints);
    }
}
