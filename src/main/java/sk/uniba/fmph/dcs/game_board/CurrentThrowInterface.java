package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.Effect;

public interface CurrentThrowInterface {

    void initiate(Player player, Effect effect, int dices);
    boolean isInitiated();
}
