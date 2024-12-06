package sk.uniba.fmph.dcs.player_board;

import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.EndOfGameEffect;
import sk.uniba.fmph.dcs.stone_age.TribeFedStatus;

import java.util.List;

import static org.junit.Assert.*;

public class PlayerBoardTest {
    @Test
    public void testNewTurnBehavior() {
        // Setup: Initialize components
        PlayerResourcesAndFood resourcesAndFood = new PlayerResourcesAndFood();
        PlayerFigures figures = new PlayerFigures();
        TribeFedStatus tribeStatus = new TribeFedStatus(figures);
        PlayerCivilisationCards civilisationCards = new PlayerCivilisationCards();
        PlayerBoard playerBoard = new PlayerBoard(civilisationCards, figures, resourcesAndFood, new PlayerTools(), tribeStatus);

        // Add initial resources and upgrades
        playerBoard.getPlayerFigures().addNewFigure(); // Total figures = 6
        playerBoard.getTribeFedStatus().addField(); // Add 1 field
        playerBoard.getPlayerTools().addTool(); // Add 1 tool
        assertFalse(playerBoard.getTribeFedStatus().isTribeFed());

        playerBoard.getPlayerResourcesAndFood()
                .giveResources(List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD));

        boolean fed = playerBoard.getTribeFedStatus().feedTribeIfEnoughFood();
        assertTrue(fed); // Tribe fed
        assertEquals(6, playerBoard.getPlayerFigures().getTotalFigures());
        assertTrue(playerBoard.getPlayerFigures().hasFigures(5));
        assertFalse(playerBoard.getPlayerFigures().hasFigures(6));
        assertTrue(playerBoard.getPlayerTools().hasSufficientTools(1));
        assertFalse(playerBoard.getPlayerTools().hasSufficientTools(2));

        playerBoard.getPlayerTools().useTool(0); // Use 1 tool
        assertFalse(playerBoard.getPlayerTools().hasSufficientTools(1));

        // Simulate new turn
        playerBoard.newTurn();
        assertFalse(playerBoard.getTribeFedStatus().isTribeFed());
        assertFalse(playerBoard.getPlayerResourcesAndFood().hasResources(List.of(Effect.FOOD)));
        assertTrue(playerBoard.getPlayerTools().hasSufficientTools(1));
        assertTrue(playerBoard.getPlayerFigures().hasFigures(6));
        assertFalse(playerBoard.getPlayerFigures().hasFigures(7));
    }

    @Test
    public void testPointAddition() {
        PlayerBoard playerBoard = new PlayerBoard();

        assertEquals(10, playerBoard.addPoints(10));
        assertEquals(0, playerBoard.addPoints(-10));
        assertEquals(7, playerBoard.addPoints(7));
        assertEquals(17, playerBoard.addPoints(10));
    }

    @Test
    public void testHouseAddition() {
        PlayerBoard playerBoard = new PlayerBoard();

        playerBoard.addHouse();
        playerBoard.addHouse();

        System.out.println(playerBoard.state());
    }

    @Test
    public void testEndOfGamePointsCalculation() {
        PlayerBoard playerBoard = new PlayerBoard();

        playerBoard.getPlayerTools().addTool();
        playerBoard.getPlayerTools().addTool();
        playerBoard.getPlayerTools().addTool(); // 3 tools

        int expectedPoints = 0;

        for (int i = 0; i < 4; i++) {
            playerBoard.getTribeFedStatus().addField();
            playerBoard.newTurn();
        } // 4 fields

        playerBoard.addHouse();
        playerBoard.addHouse(); // 2 houses
        playerBoard.getPlayerResourcesAndFood()
                .giveResources(List.of(new Effect[]{Effect.FOOD, Effect.WOOD, Effect.CLAY, Effect.STONE, Effect.GOLD}));
        expectedPoints += 4; // Resources

        playerBoard.getPlayerCivilisationCards()
                .addEndOfGameEffects(new EndOfGameEffect[]{EndOfGameEffect.SHAMAN, EndOfGameEffect.SHAMAN});
        expectedPoints += 10; // 2 Shamans * 5 figures = 10

        playerBoard.getPlayerCivilisationCards()
                .addEndOfGameEffects(new EndOfGameEffect[]{EndOfGameEffect.FARMER});
        expectedPoints += 4; // 1 Farmer * 4 fields = 4

        playerBoard.getPlayerCivilisationCards()
                .addEndOfGameEffects(new EndOfGameEffect[]{
                        EndOfGameEffect.TOOL_MAKER, EndOfGameEffect.TOOL_MAKER, EndOfGameEffect.TOOL_MAKER,
                        EndOfGameEffect.TOOL_MAKER, EndOfGameEffect.TOOL_MAKER
                });
        expectedPoints += 15; // 5 Tool Makers * 3 tools = 15

        playerBoard.getPlayerCivilisationCards()
                .addEndOfGameEffects(new EndOfGameEffect[]{
                        EndOfGameEffect.BUILDER, EndOfGameEffect.BUILDER, EndOfGameEffect.BUILDER,
                        EndOfGameEffect.BUILDER, EndOfGameEffect.BUILDER, EndOfGameEffect.BUILDER
                });
        expectedPoints += 12; // 6 Builders * 2 houses = 12

        playerBoard.getPlayerCivilisationCards()
                .addEndOfGameEffects(new EndOfGameEffect[]{
                        EndOfGameEffect.WEAVING, EndOfGameEffect.ART, EndOfGameEffect.MEDICINE, EndOfGameEffect.ART
                });
        expectedPoints += 10; // 3^2 + 1 = 10

        playerBoard.addEndOfGamePoints();

        int finalPoints = playerBoard.addPoints(-10); // Deduct 10
        expectedPoints -= 10;
        finalPoints = playerBoard.addPoints(4); // Add 4
        expectedPoints += 4;

        assertEquals(expectedPoints, finalPoints);
    }

    @Test
    public void testResetPlayerBoard() {
        PlayerBoard playerBoard = new PlayerBoard();

        playerBoard.addHouse();
        playerBoard.addPoints(20);
        playerBoard.getPlayerTools().addTool();
    }
}
