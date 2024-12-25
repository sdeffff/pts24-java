package sk.uniba.fmph.dcs.game_board;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

public class ResourceSourceTest {
    
    private static class MockCurrentThrow implements CurrentThrowInterface {
        private Player lastPlayer;
        private Effect lastEffect;
        private int lastDices;
        private boolean toolsUsed = false;
        
        @Override
        public void initiate(Player player, Effect effect, int dices) {
            this.lastPlayer = player;
            this.lastEffect = effect;
            this.lastDices = dices;
        }
        
        @Override
        public boolean useTool(int idx) {
            toolsUsed = true;
            return true;
        }
        
        @Override
        public boolean canUseTools() {
            return true;
        }
        
        @Override
        public boolean finishUsingTools() {
            return true;
        }
        
        @Override
        public String state() {
            return "mock_state";
        }
    }
    
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
        private boolean hasFigures = true;
        
        @Override
        public boolean hasFigures(int count) {
            return hasFigures;
        }
        
        public void setHasFigures(boolean value) {
            hasFigures = value;
        }
        
        // Implement other interface methods as needed for tests
        @Override
        public void giveEffect(Collection<Effect> effects) {}
        
        @Override
        public Optional<Integer> useTool(int idx) {
            return Optional.of(1);
        }
        
        @Override
        public boolean hasSufficientTools(int count) {
            return true;
        }
    }
    
    private ResourceSource resourceSource;
    private MockCurrentThrow mockCurrentThrow;
    private MockPlayer player;
    private MockPlayerBoard playerBoard;
    
    @Before
    public void setUp() {
        mockCurrentThrow = new MockCurrentThrow();
        playerBoard = new MockPlayerBoard();
        player = new MockPlayer(1, playerBoard);
        resourceSource = new ResourceSource("Wood", Effect.WOOD, 7, 2, mockCurrentThrow);
    }
    
    @Test
    public void testPlaceFiguresSuccess() {
        assertTrue(resourceSource.placeFigures(player, 2));
    }
    
    @Test
    public void testPlaceFiguresFailureNoFigures() {
        playerBoard.setHasFigures(false);
        assertFalse(resourceSource.placeFigures(player, 2));
    }
    
    @Test
    public void testMakeActionSuccess() {
        resourceSource.placeFigures(player, 2);
        Collection<Effect> output = Arrays.asList(Effect.WOOD, Effect.WOOD);
        assertEquals(
            ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE,
            resourceSource.makeAction(player, new ArrayList<>(), output)
        );
        assertEquals(Effect.WOOD, mockCurrentThrow.lastEffect);
        assertEquals(2, mockCurrentThrow.lastDices);
        assertEquals(player, mockCurrentThrow.lastPlayer);
    }
    
    @Test
    public void testMakeActionFailureWrongResource() {
        resourceSource.placeFigures(player, 1);
        Collection<Effect> output = Arrays.asList(Effect.CLAY);
        assertEquals(
            ActionResult.FAILURE,
            resourceSource.makeAction(player, new ArrayList<>(), output)
        );
    }
    @Test
    public void testCurrentThrowDependencyInjection() {
        MockCurrentThrow mockCurrentThrow = new MockCurrentThrow();
        ResourceSource source = new ResourceSource("Test", Effect.WOOD, 7, 2, mockCurrentThrow);
        
        // Place figures and make action
        source.placeFigures(mockPlayer1, 2);
        Collection<Effect> output = Arrays.asList(Effect.WOOD, Effect.WOOD);
        
        assertEquals(ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE,
            source.makeAction(mockPlayer1, new ArrayList<>(), output));
        
        // Verify CurrentThrow was called correctly
        assertEquals(mockPlayer1, mockCurrentThrow.lastPlayer);
        assertEquals(Effect.WOOD, mockCurrentThrow.lastEffect);
        assertEquals(2, mockCurrentThrow.lastDices);
    }

    @Test
    public void testSolitaryWithMockedDependencies() {
        MockCurrentThrow mockCurrentThrow = new MockCurrentThrow();
        MockPlayerBoard mockBoard = new MockPlayerBoard();
        Player mockPlayer = new MockPlayer(0, mockBoard);
        
        ResourceSource source = new ResourceSource("Test", Effect.WOOD, 7, 2, mockCurrentThrow);
        
        // Test with mocked dependencies
        assertTrue(source.placeFigures(mockPlayer, 2));
        Collection<Effect> output = Arrays.asList(Effect.WOOD, Effect.WOOD);
        assertEquals(ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE,
            source.makeAction(mockPlayer, new ArrayList<>(), output));
    }

    @Test
    public void testCompleteActionFlow() {
        MockCurrentThrow mockCurrentThrow = new MockCurrentThrow();
        ResourceSource source = new ResourceSource("Test", Effect.WOOD, 7, 2, mockCurrentThrow);
        
        // Test complete flow
        assertTrue(source.placeFigures(mockPlayer1, 2));
        Collection<Effect> output = Arrays.asList(Effect.WOOD, Effect.WOOD);
        assertEquals(ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE,
            source.makeAction(mockPlayer1, new ArrayList<>(), output));
        
        // Verify state after action
        assertTrue(source.state().contains("\"figures\":[0,0]"));
        
        // Test new turn resets
        source.newTurn();
        assertTrue(source.state().contains("\"figures\":[]"));
    }
}
