package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.Player;

public class GetSomethingThrow implements EvaluateCivilizationCardImmediateEffect {
    private Effect resource;

    public GetSomethingThrow(Effect resource){
        this.resource = resource;
    }

    @Override
    public boolean performEffect(Player player, Effect choice) {
        if(resource.isResource() && resource == choice){
            CurrentThrow currentThrow = new CurrentThrow();
            currentThrow.initiate(player, choice, 2);
            return true;
        }
        return false;
    }

}
