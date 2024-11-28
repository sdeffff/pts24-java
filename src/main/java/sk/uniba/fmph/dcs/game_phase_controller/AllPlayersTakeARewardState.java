package sk.uniba.fmph.dcs.game_phase_controller;

import org.apache.commons.lang3.tuple.Pair;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.Collection;

public class AllPlayersTakeARewardState implements InterfaceGamePhaseState{

    InterfaceTakeReward reward;


    @Override
    public ActionResult placeFigures(PlayerOrder player, Location location, int figuresCount) {
        return ActionResult.FAILURE;
    }

    @Override
    public ActionResult makeAction(PlayerOrder player, Location location, Collection<Effect> inputResources, Collection<Effect> outputResources) {
        return ActionResult.FAILURE;
    }

    @Override
    public ActionResult skipAction(PlayerOrder player, Location location) {
        return ActionResult.FAILURE;
    }

    @Override
    public ActionResult useTools(PlayerOrder player, int toolIndex) {
        return ActionResult.FAILURE;
    }

    @Override
    public ActionResult noMoreToolsThisThrow(PlayerOrder player) {
        return ActionResult.FAILURE;
    }

    @Override
    public ActionResult feedTribe(PlayerOrder player, Collection<Effect> resources) {
        return ActionResult.FAILURE;
    }

    @Override
    public ActionResult doNotFeedThisTurn(PlayerOrder player) {
        return ActionResult.FAILURE;
    }

    @Override
    public ActionResult makeAllPlayersTakeARewardChoice(PlayerOrder player, Effect reward) {
        return this.reward.takeReward(player, reward)
        ? ActionResult.ACTION_DONE
        : ActionResult.FAILURE;
    }

    @Override
    public HasAction tryToMakeAutomaticAction(PlayerOrder player) {
        if(reward.playerHasAllRewards(player)) return HasAction.NO_ACTION_POSSIBLE;
        Pair<PlayerOrder, Effect> res = reward.playerLastReward();
        if (res != null) {
            PlayerOrder playerLast = res.getLeft();
            Effect lastEffect = res.getRight();
            reward.takeReward(playerLast, lastEffect);
            return HasAction.AUTOMATIC_ACTION_DONE;
        }
        return HasAction.WAITING_FOR_PLAYER_ACTION;
    }
}
