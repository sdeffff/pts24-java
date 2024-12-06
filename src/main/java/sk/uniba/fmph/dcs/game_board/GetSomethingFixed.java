package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.Player;

import java.util.Arrays;
import java.util.List;

public class GetSomethingFixed implements EvaluateCivilizationCardImmediateEffect{
    private Effect effects;

    public GetSomethingFixed(Effect effect) {
        effects = effect;
    }
    @Override
    public boolean performEffect(Player player, Effect choice) {
        if(effects == Effect.BUILDING){
            return false;
        }else{
            player.playerBoard().giveEffect(Arrays.asList(effects));
            return true;
        }
    }
}
