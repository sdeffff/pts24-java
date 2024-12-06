package sk.uniba.fmph.dcs.game_phase_controller;

import sk.uniba.fmph.dcs.stone_age.*;
import sk.uniba.fmph.dcs.stone_age.PlayerOrder;

public interface InterfaceTakeReward {
    boolean takeReward(PlayerOrder player, Effect effect);
    HasAction tryMakeAction(PlayerOrder player);
}
