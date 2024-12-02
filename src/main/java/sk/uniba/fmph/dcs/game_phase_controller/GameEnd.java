package sk.uniba.fmph.dcs.game_phase_controller;

import sk.uniba.fmph.dcs.stone_age.*;

import java.util.Collection;

public class GameEnd implements InterfaceGamePhaseState{
    @Override
    public boolean placeFigures(PlayerOrder player, Location location, int figuresCount) {
        return boolean.FAILURE;
    }

    @Override
    public boolean makeAction(PlayerOrder player, Location location, Collection<Effect> inputResources, Collection<Effect> outputResources) {
        return boolean.FAILURE;
    }

    @Override
    public boolean skipAction(PlayerOrder player, Location location) {
        return boolean.FAILURE;
    }

    @Override
    public boolean useTools(PlayerOrder player, int toolIndex) {
        return boolean.FAILURE;
    }

    @Override
    public boolean noMoreToolsThisThrow(PlayerOrder player) {
        return boolean.FAILURE;
    }

    @Override
    public boolean feedTribe(PlayerOrder player, Collection<Effect> resources) {
        return boolean.FAILURE;
    }

    @Override
    public boolean doNotFeedThisTurn(PlayerOrder player) {
        return boolean.FAILURE;
    }

    @Override
    public boolean makeAllPlayersTakeARewardChoice(PlayerOrder player, Effect reward) {
        return boolean.FAILURE;
    }

    @Override
    public HasAction tryToMakeAutomaticAction(PlayerOrder player) {
        return HasAction.WAITING_FOR_PLAYER_ACTION;
    }
}
