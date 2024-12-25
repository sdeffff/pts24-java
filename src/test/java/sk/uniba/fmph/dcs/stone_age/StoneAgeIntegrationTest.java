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
        // Configure controlled dice rolls for the test
        ((ThrowInterface)mockThrow).setNextRolls(new int[]{3, 3, 3, 4, 4, 4, 5, 5, 5});
        
        StoneAgeGame game = StoneAgeGameFactory.createGame(
            playerIds,
            mockThrow,
            testCards,
            testBuildings,
            observer
        );
        
        // First player places figures
        assertTrue(game.placeFigures(new PlayerOrder(0, 3), Location.HUNTING_GROUNDS, 2));
        assertTrue(observer.getLastState().contains("PLACE_FIGURES"));
        
        // Second player places figures
        assertTrue(game.placeFigures(new PlayerOrder(1, 3), Location.FOREST, 2));
        assertTrue(observer.getLastState().contains("FOREST"));
        
        // Third player places figures
        assertTrue(game.placeFigures(new PlayerOrder(2, 3), Location.CLAY_MOUND, 2));
        assertTrue(observer.getLastState().contains("CLAY_MOUND"));
        
        // First player makes action with controlled dice roll
        assertEquals(
            ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE,
            game.makeAction(
                new PlayerOrder(0, 3),
                Location.HUNTING_GROUNDS,
                new ArrayList<>(),
                Arrays.asList(Effect.FOOD, Effect.FOOD)
            )
        );
        assertTrue(observer.getLastState().contains("MAKE_ACTION"));
        
        // Second player makes action
        assertEquals(
            ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE,
            game.makeAction(
                new PlayerOrder(1, 3),
                Location.FOREST,
                new ArrayList<>(),
                Arrays.asList(Effect.WOOD, Effect.WOOD)
            )
        );
        
        // Third player makes action
        assertEquals(
            ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE,
            game.makeAction(
                new PlayerOrder(2, 3),
                Location.CLAY_MOUND,
                new ArrayList<>(),
                Arrays.asList(Effect.CLAY, Effect.CLAY)
            )
        );
        
        // Verify complete round state
        String finalState = observer.getLastState();
        assertTrue(finalState.contains("MAKE_ACTION"));
        assertTrue(finalState.contains("figures"));
        assertTrue(finalState.contains("resources"));
    }
}
