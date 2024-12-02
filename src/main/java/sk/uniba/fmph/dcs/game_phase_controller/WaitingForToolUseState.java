package sk.uniba.fmph.dcs.game_phase_controller;

import sk.uniba.fmph.dcs.stone_age.*;

import java.util.Collection;

public class WaitingForToolUseState implements InterfaceGamePhaseState {

    private InterfaceToolUse toolUse;
    private PlayerOrder currentPlayer;

    WaitingForToolUseState(PlayerOrder player) {
        currentPlayer = player;
    }

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
        if(player.equals(currentPlayer)) {
            if(toolUse.canUseTools()){
                toolUse.useTool(toolIndex);           //nothing stops player from using same tool several times
                return boolean.ACTION_DONE;
            }
        }
        return boolean.FAILURE;
    }

    @Override
    public boolean noMoreToolsThisThrow(PlayerOrder player) {
        toolUse.finishUsingTools();
        return boolean.ACTION_DONE;              //ACTION_DONE_ALL_PLAYERS_TAKE_A_REWARD?
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
        if (player.equals(currentPlayer)) {                     //???
            if(toolUse.canUseTools()) return HasAction.WAITING_FOR_PLAYER_ACTION;
            return HasAction.NO_ACTION_POSSIBLE;
        }
        return  HasAction.NO_ACTION_POSSIBLE;
    }
}
