package sk.uniba.fmph.dcs.stone_age;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.game_board.Player;

import java.util.ArrayList;
import java.util.Collection;

public class ResourceSourceTest {
    private ResourceSource source;
    private Player mockPlayer1;
    private Player mockPlayer2;
    private PlayerBoard mockBoard1;
    private PlayerBoard mockBoard2;
    
    private class MockPlayerBoard implements PlayerBoard {
        private boolean hasFiguresResponse = true;
        
        @Override
        public boolean hasFigures(int count) {
            return hasFiguresResponse;
        }
        
        public void setHasFiguresResponse(boolean response) {
            this.hasFiguresResponse = response;
        }
    }
    
    private class MockPlayer implements Player {
        private final PlayerOrder order;
        private final PlayerBoard board;
        
        public MockPlayer(int orderNum, PlayerBoard board) {
            this.order = new PlayerOrder(orderNum, 2);
            this.board = board;
        }
        
        @Override
        public PlayerOrder playerOrder() {
            return order;
        }
        
        @Override
        public PlayerBoard playerBoard() {
            return board;
        }
    }

    @Before
    public void setUp() {
        mockBoard1 = new MockPlayerBoard();
        mockBoard2 = new MockPlayerBoard();
        mockPlayer1 = new MockPlayer(0, mockBoard1);
        mockPlayer2 = new MockPlayer(1, mockBoard2);
        source = new ResourceSource("Forest", Effect.WOOD, 7, 2);
    }

    @Test
    public void testPlaceFiguresSuccess() {
        assertTrue(source.placeFigures(mockPlayer1, 2));
    }

    @Test
    public void testPlaceFiguresNoFigures() {
        ((MockPlayerBoard)mockBoard1).setHasFiguresResponse(false);
        assertFalse(source.placeFigures(mockPlayer1, 2));
    }

    @Test
    public void testPlaceFiguresExceedMax() {
        assertTrue(source.placeFigures(mockPlayer1, 7));
        assertFalse(source.placeFigures(mockPlayer2, 1));
    }

    @Test
    public void testPlaceFiguresExceedColors() {
        assertTrue(source.placeFigures(mockPlayer1, 2));
        assertTrue(source.placeFigures(mockPlayer2, 2));
        MockPlayer mockPlayer3 = new MockPlayer(2, new MockPlayerBoard());
        assertFalse(source.placeFigures(mockPlayer3, 2));
    }

    @Test
    public void testMakeActionSuccess() {
        source.placeFigures(mockPlayer1, 2);
        Collection<Effect> input = new ArrayList<>();
        Collection<Effect> output = new ArrayList<>();
        output.add(Effect.WOOD);
        output.add(Effect.WOOD);
        assertEquals(ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE,
            source.makeAction(mockPlayer1, input, output));
    }

    @Test
    public void testMakeActionWrongResourceCount() {
        source.placeFigures(mockPlayer1, 2);
        Collection<Effect> input = new ArrayList<>();
        Collection<Effect> output = new ArrayList<>();
        output.add(Effect.WOOD);
        assertEquals(ActionResult.FAILURE,
            source.makeAction(mockPlayer1, input, output));
    }

    @Test
    public void testNewTurnClearsFigures() {
        assertTrue(source.placeFigures(mockPlayer1, 2));
        source.newTurn();
        assertTrue(source.placeFigures(mockPlayer1, 2));
    }
}
