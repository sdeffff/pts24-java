package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.InterfacePlayerBoardGameBoard;
import sk.uniba.fmph.dcs.stone_age.Player;

public class GetSomethingThrow implements EvaluateCivilizationCardImmediateEffect {
    private Effect resource;
    private final CurrentThrowInterface currentThrow;

    public GetSomethingThrow(Effect resource, CurrentThrowInterface currentThrow){
        this.resource = resource;
        this.currentThrow = currentThrow;
    }

    @Override
    public boolean performEffect(Player player, Effect choice) {
        if(resource == choice){
            currentThrow.initiate(player, choice, 2);
            return true;
        }
        return false;
    }

}
