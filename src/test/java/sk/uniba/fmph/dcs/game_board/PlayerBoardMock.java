package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.EndOfGameEffect;
import sk.uniba.fmph.dcs.stone_age.InterfacePlayerBoardGameBoard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.OptionalInt;

public class PlayerBoardMock implements InterfacePlayerBoardGameBoard {
    private int numberOfFigures = 0;
    private ArrayList<Effect> resources = new ArrayList<>();

    @Override
    public void giveEffect(Collection<Effect> stuff) {
        resources.addAll(stuff);
    }

    @Override
    public void giveEndOfGameEffect(Collection<EndOfGameEffect> stuff) {

    }

    @Override
    public boolean takeResources(Collection<Effect> stuff) {
        ArrayList<Effect> takenResources = new ArrayList<>();
        for (Effect e: stuff) {
            if (resources.contains(e)) {
                takenResources.add(e);
                resources.remove(e);
            }
            else {
                resources.addAll(takenResources);
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean takeFigures(int count) {
        if (numberOfFigures < count) {
            return false;
        }
        numberOfFigures -= count;
        return true;
    }

    @Override
    public void giveFigures(int count) {
        numberOfFigures += count;
    }

    @Override
    public boolean hasFigures(int count) {
        return (numberOfFigures >= count);
    }

    @Override
    public boolean hasSufficientTools(int goal) {
        return goal < 2;
    }

    @Override
    public OptionalInt useTool(int idx) {
        if (idx == 1) {
            return OptionalInt.of(5);
        }
        return OptionalInt.empty();
    }

    @Override
    public void takePoints(int points) {

    }

    @Override
    public void givePoints(int points) {

    }
}
