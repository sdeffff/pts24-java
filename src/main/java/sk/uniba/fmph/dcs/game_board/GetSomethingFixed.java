package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.Player;

import java.util.List;

public class GetSomethingFixed implements EvaluateCivilizationCardImmediateEffect{
    private List<Effect> effects;

    public GetSomethingFixed(List<Effect> effect) {
        effects = effect;
    }
    @Override
    public boolean performEffect(Player player, Effect choice) {
        player.playerBoard().giveEffect(effects);
        return true;
    }
}
