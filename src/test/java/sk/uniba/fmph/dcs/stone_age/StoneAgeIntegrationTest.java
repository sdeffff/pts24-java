package sk.uniba.fmph.dcs.stone_age;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.game_board.*;

import java.util.*;

public class StoneAgeIntegrationTest {
    private ThrowInterface mockThrow;
    private List<Integer> playerIds;
    private List<CivilizationCard> testCards;
    private List<Building> testBuildings;
    private MockStoneAgeObserver observer;
    
    private class MockStoneAgeObserver implements InterfaceStoneAgeObserver {
        private String lastState;
        
        @Override
        public void update(String gameState) {
            this.lastState = gameState;
        }
        
        public String getLastState() {
            return lastState;
        }
    }
    
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
        
        // Setup player IDs
        playerIds = Arrays.asList(10, 15, 1);
        
        // Setup test cards and buildings
        testCards = Arrays.asList(
            new CivilizationCard(
                Arrays.asList(ImmediateEffect.FOOD),
                Arrays.asList(EndOfGameEffect.FARMER)
            )
        );
        
        testBuildings = Arrays.asList(
            new SimpleBuilding(Arrays.asList(Effect.WOOD))
        );
        
        // Setup observer
        observer = new MockStoneAgeObserver();
    }
    
    @Test
    public void testGameIntegration() {
        StoneAgeGame game = StoneAgeGameFactory.createGame(
            playerIds,
            mockThrow,
            testCards,
            testBuildings,
            observer
        );
        
        // Test game initialization
        assertNotNull(observer.getLastState());
        assertTrue(observer.getLastState().contains("PLACE_FIGURES"));
        
        // Test placing figures
        assertTrue(game.placeFigures(new PlayerOrder(0, 3), Location.HUNTING_GROUNDS, 2));
        assertTrue(observer.getLastState().contains("HUNTING_GROUNDS"));
        
        // Test making action
        assertEquals(
            ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE,
            game.makeAction(
                new PlayerOrder(0, 3),
                Location.HUNTING_GROUNDS,
                new ArrayList<>(),
                Arrays.asList(Effect.FOOD, Effect.FOOD)
            )
        );
    }
    
    @Test
    public void testCompleteGameFlow() {
        StoneAgeGame game = StoneAgeGameFactory.createGame(
            playerIds,
            mockThrow,
            testCards,
            testBuildings,
            observer
        );
        
        // First player places figures
        assertTrue(game.placeFigures(new PlayerOrder(0, 3), Location.HUNTING_GROUNDS, 2));
        
        // Second player places figures
        assertTrue(game.placeFigures(new PlayerOrder(1, 3), Location.FOREST, 2));
        
        // Third player places figures
        assertTrue(game.placeFigures(new PlayerOrder(2, 3), Location.CLAY_MOUND, 2));
        
        // Players make actions
        assertEquals(
            ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE,
            game.makeAction(
                new PlayerOrder(0, 3),
                Location.HUNTING_GROUNDS,
                new ArrayList<>(),
                Arrays.asList(Effect.FOOD, Effect.FOOD)
            )
        );
        
        // Verify game state updates
        assertTrue(observer.getLastState().contains("MAKE_ACTION"));
        assertTrue(observer.getLastState().contains("figures"));
    }
}
