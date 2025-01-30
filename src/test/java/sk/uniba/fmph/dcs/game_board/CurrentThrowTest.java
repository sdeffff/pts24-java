package sk.uniba.fmph.dcs.game_board;

import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.Effect;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class CurrentThrowTest {

    @Test
    public void testCalculation() {
        ThrowInterface throwMock = new ThrowInterface() {
            @Override
            public ArrayList<Integer> throwDice(int dices) {
                ArrayList<Integer> result = new ArrayList<>();
                result.add(6);
                result.add(1);
                result.add(3);
                return result;
            }

            @Override
            public void setRolls(ArrayList<Integer> rolls) {

            }
        };

        CurrentThrow c = new CurrentThrow(throwMock);
        PlayerBoardMock board = new PlayerBoardMock();
        Player player = new Player(null, board);
        assertFalse(c.useTool(1));
        c.initiate(player, Effect.WOOD, 3);
        assertEquals(c.getThrowResult(), 10);
        assertTrue(c.canUseTools());
        assertFalse(c.useTool(2));
        assertTrue(c.useTool(1));
        assertEquals(c.getThrowResult(), 15);
        assertTrue(c.finishUsingTools());
        assertFalse(c.canUseTools());
    }
}