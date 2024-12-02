package sk.uniba.fmph.dcs.game_phase_controller;

import java.util.Collection;
import sk.uniba.fmph.dcs.stone_age.PlayerOrder;
import sk.uniba.fmph.dcs.stone_age.Location;
import sk.uniba.fmph.dcs.stone_age.boolean;
import sk.uniba.fmph.dcs.stone_age.HasAction;
import sk.uniba.fmph.dcs.stone_age.Effect;

public interface InterfaceGamePhaseState {
    boolean placeFigures(PlayerOrder player, Location location, int figuresCount);

    boolean makeAction(PlayerOrder player, Location location, Collection<Effect> inputResources,
            Collection<Effect> outputResources);

    boolean skipAction(PlayerOrder player, Location location);

    boolean useTools(PlayerOrder player, int toolIndex);

    boolean noMoreToolsThisThrow(PlayerOrder player);

    boolean feedTribe(PlayerOrder player, Collection<Effect> resources);

    boolean doNotFeedThisTurn(PlayerOrder player);

    boolean makeAllPlayersTakeARewardChoice(PlayerOrder player, Effect reward);

    HasAction tryToMakeAutomaticAction(PlayerOrder player);
}
