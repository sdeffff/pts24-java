package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.ActionResult;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.HasAction;

public class GetSomethingThrow implements EvaluateCivilizationCardImmediateEffect {
    private final CurrentThrow currentThrow;
    private static final int NUMBER_OF_DICES = 2;
    private final Effect effect;

    public GetSomethingThrow(final CurrentThrow currentThrow, final Effect effect) {
        this.currentThrow = currentThrow;
        this.effect = effect;
    }

    @Override
    public boolean performEffect(Player player, Effect choice) {
        if (!effect.isResource()) {
            return false;
        }
        currentThrow.initiate(player, effect, NUMBER_OF_DICES);
        return true;
    }
}