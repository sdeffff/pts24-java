package sk.uniba.fmph.dcs.game_board;

import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.Collection;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class GetSomethingFixedTest {
    private class PlayerGameBoardMock implements InterfacePlayerBoardGameBoard{

        @Override
        public void giveFigure() {

        }

        @Override
        public void giveEffect(Collection<Effect> stuff) {

        }

        @Override
        public void giveEndOfGameEffect(Collection<EndOfGameEffect> stuff) {

        }

        @Override
        public void giveCard(CivilizationCard card) {

        }

        @Override
        public boolean takeResources(Collection<Effect> stuff) {
            return false;
        }

        @Override
        public boolean takeFigures(int count) {
            return false;
        }

        @Override
        public boolean hasFigures(int count) {
            return false;
        }

        @Override
        public boolean hasSufficientTools(int goal) {
            return false;
        }

        @Override
        public Optional<Optional<Integer>> useTool(int idx) {
            return Optional.empty();
        }
    }
    private class PlayerMock implements Player{
        public InterfacePlayerBoardGameBoard board;
        public PlayerMock(PlayerOrder playerOrder, InterfacePlayerBoardGameBoard board){
            this.board = board;
        }

        @Override
        public PlayerOrder playerOrder() {
            return null;
        }

        @Override
        public InterfacePlayerBoardGameBoard playerBoard() {
            return board;
        }
    }
    private InterfacePlayerBoardGameBoard gameBoardMock = new PlayerGameBoardMock();
    @Test
    public void defaultTest(){
        Player player = new PlayerMock(null,gameBoardMock);
        GetSomethingFixed gsf = new GetSomethingFixed(Effect.GOLD);
        assertTrue(gsf.performEffect(player,null));
    }

    @Test
    public void buildingTest(){
        Player player = new PlayerMock(null, gameBoardMock);
        GetSomethingFixed gsf = new GetSomethingFixed(Effect.BUILDING);
        assertFalse(gsf.performEffect(player,null));
    }
}
