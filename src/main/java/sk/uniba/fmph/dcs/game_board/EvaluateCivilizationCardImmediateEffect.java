package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.game_board.Player;

import sk.uniba.fmph.dcs.stone_age.ActionResult;
import sk.uniba.fmph.dcs.stone_age.Effect;

public interface EvaluateCivilizationCardImmediateEffect {
    ActionResult performEffect(Player player, Effect choice);
    boolean tryToPerformEffect(Player player, Effect choice);
}