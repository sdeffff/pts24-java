package sk.uniba.fmph.dcs.game_board;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.*;

public class GameBoardIntegrationTest {
    private ThrowInterface mockThrow;
    private List<Player> players;
    private List<CivilizationCard> testCards;
    private List<Building> testBuildings;
    private PlayerBoardMock playerBoardMock;
    
    @Before
    public void setUp() {
        // Setup mock throw with controlled results
        mockThrow = new ThrowInterface() {
            private int[] nextRolls = {3, 3, 3};
            
            @Override
            public int[] throw_(int dices) {
                return Arrays.copyOf(nextRolls, dices);
            }
        };
        
        // Setup test cards
        testCards = Arrays.asList(
            new CivilizationCard(
                Arrays.asList(ImmediateEffect.FOOD),
                Arrays.asList(EndOfGameEffect.FARMER)
            )
        );
        
        // Setup test buildings
        testBuildings = Arrays.asList(
            new SimpleBuilding(Arrays.asList(Effect.WOOD))
        );
        
        // Setup players with mock board
        playerBoardMock = new PlayerBoardMock();
        players = Arrays.asList(
            new Player(new PlayerOrder(0, 2), playerBoardMock),
            new Player(new PlayerOrder(1, 2), playerBoardMock)
        );
    }
    
    @Test
    public void testGameBoardIntegration() {
        // Configure mock throw for specific results
        ((ThrowInterface)mockThrow).setNextRolls(new int[]{4, 4}); // Ensure predictable resource gathering
        
        GameBoard gameBoard = GameBoardFactory.createGameBoard(
            players,
            mockThrow,
            testCards,
            testBuildings
        );
        
        // Test placing figures through interface
        assertTrue(gameBoard.placeFigures(
            players.get(0).playerOrder(),
            Location.HUNTING_GROUNDS,
            2
        ));
        
        // Verify figure placement through state
        String placementState = gameBoard.state();
        assertTrue(placementState.contains("HUNTING_GROUNDS"));
        assertTrue(placementState.contains("\"figures\":[2]"));
        
        // Test resource gathering with controlled dice rolls
        assertEquals(
            ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE,
            gameBoard.makeAction(
                players.get(0).playerOrder(),
                Location.HUNTING_GROUNDS,
                new ArrayList<>(),
                Arrays.asList(Effect.FOOD, Effect.FOOD)
            )
        );
        
        // Verify resources were given through PlayerBoard mock
        assertTrue(playerBoardMock.getEffects().containsAll(Arrays.asList(Effect.FOOD, Effect.FOOD)));
        
        // Verify final state
        String finalState = gameBoard.state();
        assertTrue(finalState.contains("HUNTING_GROUNDS"));
        assertTrue(finalState.contains("figures"));
    }
    
    @Test 
    public void testCivilizationCardFlow() {
        // Test with specific civilization card
        CivilizationCard testCard = new CivilizationCard(
            Arrays.asList(ImmediateEffect.FOOD),
            Arrays.asList(EndOfGameEffect.FARMER)
        );
        
        GameBoard gameBoard = GameBoardFactory.createGameBoard(
            players,
            mockThrow,
            Arrays.asList(testCard),
            testBuildings
        );

        // Place figure on civilization card spot
        assertTrue(gameBoard.placeFigures(
            players.get(0).playerOrder(),
            Location.CIVILIZATION_CARD,
            1
        ));

        // Make action to get card
        assertEquals(
            ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE,
            gameBoard.makeAction(
                players.get(0).playerOrder(),
                Location.CIVILIZATION_CARD,
                new ArrayList<>(),
                Arrays.asList(Effect.FOOD)
            )
        );

        // Verify card was given through PlayerBoard mock
        assertEquals(1, playerBoardMock.getCards().size());
        assertEquals(testCard, playerBoardMock.getCards().get(0));
    }

    @Test
    public void testCompleteRoundFlow() {
        GameBoard gameBoard = GameBoardFactory.createGameBoard(
            players,
            mockThrow,
            testCards,
            testBuildings
        );
        
        // Place figures phase
        assertTrue(gameBoard.placeFigures(
            players.get(0).playerOrder(),
            Location.HUNTING_GROUNDS,
            2
        ));
        assertTrue(gameBoard.placeFigures(
            players.get(1).playerOrder(),
            Location.FOREST,
            2
        ));
        
        // Make actions phase
        assertEquals(
            ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE,
            gameBoard.makeAction(
                players.get(0).playerOrder(),
                Location.HUNTING_GROUNDS,
                new ArrayList<>(),
                Arrays.asList(Effect.FOOD, Effect.FOOD)
            )
        );
        
        assertEquals(
            ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE,
            gameBoard.makeAction(
                players.get(1).playerOrder(),
                Location.FOREST,
                new ArrayList<>(),
                Arrays.asList(Effect.WOOD, Effect.WOOD)
            )
        );
        
        // Verify end of round state
        gameBoard.newTurn();
        String state = gameBoard.state();
        assertTrue(state.contains("\"figures\":[]"));
    }
}
