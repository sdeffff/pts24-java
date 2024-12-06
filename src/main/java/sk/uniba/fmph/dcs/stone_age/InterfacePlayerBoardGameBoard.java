package sk.uniba.fmph.dcs.stone_age;

import sk.uniba.fmph.dcs.game_board.CivilizationCard;

import java.util.Collection;
import java.util.Optional;

public interface InterfacePlayerBoardGameBoard {
    void giveFigure();
    void giveEffect(Collection<Effect> stuff);
    void giveEndOfGameEffect(Collection<EndOfGameEffect> stuff);
    void giveCard(CivilizationCard card);
    boolean takeResources(Collection<Effect> stuff);
    boolean takeFigures(int count);
    boolean hasFigures(int count);
    boolean hasSufficientTools(int goal);
    Optional<Integer> useTool(int idx);
}
