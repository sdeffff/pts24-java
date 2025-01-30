package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.HasAction;
import sk.uniba.fmph.dcs.stone_age.PlayerOrder;

import java.util.ArrayList;

public interface RewardMenuInterface {
    void initiate(ArrayList<Effect> items);
    boolean takeReward(PlayerOrder player, Effect reward);
    HasAction tryMakeAction(PlayerOrder player);
    String state();
}