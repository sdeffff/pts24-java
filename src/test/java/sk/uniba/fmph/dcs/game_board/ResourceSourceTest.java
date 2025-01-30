package sk.uniba.fmph.dcs.game_board;

import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.*;

import static org.junit.Assert.*;

public class ResourceSourceTest {

    private static class CurrentThrowMock implements CurrentThrowInterface{
        @Override
        public void initiate(Player player, Effect effect, int dices) {

        }

        @Override
        public boolean isInitiated() {
            return false;
        }
    }

    @Test
    public void resourceTest() {
        PlayerBoardMock playerBoard1 = new PlayerBoardMock();
        PlayerBoardMock playerBoard2 = new PlayerBoardMock();
        CurrentThrowMock currentThrow = new CurrentThrowMock();
        ResourceSource r = new ResourceSource("", Effect.WOOD, currentThrow, 2);
        PlayerOrder playerOrder1 = new PlayerOrder(0,2);
        PlayerOrder playerOrder2 = new PlayerOrder(1,2);
        Player player1 = new Player(playerOrder1, playerBoard1);
        Player player2 = new Player(playerOrder2, playerBoard2);
        player1.playerBoard().giveFigures(10);
        player2.playerBoard().giveFigures(4);

        assertEquals(r.tryToPlaceFigures(player2, 5), HasAction.NO_ACTION_POSSIBLE);
        assertEquals(r.tryToPlaceFigures(player1, 5), HasAction.WAITING_FOR_PLAYER_ACTION);

        player2.playerBoard().giveFigures(10);

        assertTrue(r.placeFigures(player1,4));
        assertEquals(r.tryToPlaceFigures(player2, 4), HasAction.NO_ACTION_POSSIBLE);
        assertFalse(r.placeFigures(player1,9));
        assertTrue(r.placeFigures(player2, 3));

        assertEquals(r.tryToMakeAction(player1), HasAction.WAITING_FOR_PLAYER_ACTION);
        assertEquals(r.makeAction(player1, null, null), ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE);
        assertEquals(r.tryToMakeAction(player1), HasAction.NO_ACTION_POSSIBLE);
        assertEquals(r.makeAction(player1, null, null), ActionResult.FAILURE);
        assertFalse(r.skipAction(player1));

        assertTrue(r.skipAction(player2));
        assertEquals(r.tryToMakeAction(player2), HasAction.NO_ACTION_POSSIBLE);
    }

    @Test
    public void foodTest() {
        PlayerBoardMock playerBoard1 = new PlayerBoardMock();
        PlayerBoardMock playerBoard2 = new PlayerBoardMock();
        CurrentThrowMock currentThrow = new CurrentThrowMock();
        ResourceSource r = new ResourceSource("", Effect.FOOD, currentThrow, 2);
        PlayerOrder playerOrder1 = new PlayerOrder(0,2);
        PlayerOrder playerOrder2 = new PlayerOrder(1,2);
        Player player1 = new Player(playerOrder1, playerBoard1);
        Player player2 = new Player(playerOrder2, playerBoard2);
        player1.playerBoard().giveFigures(100);
        player2.playerBoard().giveFigures(400);

        assertEquals(r.tryToPlaceFigures(player1, 100), HasAction.WAITING_FOR_PLAYER_ACTION);
        assertTrue(r.placeFigures(player2, 400));
    }
}