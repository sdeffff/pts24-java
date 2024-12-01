package sk.uniba.fmph.dcs.game_phase_controller;

import sk.uniba.fmph.dcs.stone_age.ActionResult;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.HasAction;
import sk.uniba.fmph.dcs.stone_age.Location;
import sk.uniba.fmph.dcs.stone_age.PlayerOrder;
import sk.uniba.fmph.dcs.stone_age.InterfaceFeedTribe;

import java.util.Collection;
import java.util.Map;

public final class FeedTribeState implements InterfaceGamePhaseState {

    private final Map<PlayerOrder, InterfaceFeedTribe> playerTribes;

    public FeedTribeState(Map<PlayerOrder, InterfaceFeedTribe> playerTribes) {
        this.playerTribes = playerTribes;
    }

    @Override
    public ActionResult placeFigures(final PlayerOrder player, final Location location, final int figuresCount) {
        return ActionResult.FAILURE;
    }
    @Override
    public ActionResult makeAction(final PlayerOrder player, final Location location,
                                   final Collection<Effect> inputResources, final Collection<Effect> outputResources) {
        return ActionResult.FAILURE;
    }

    @Override
    public ActionResult skipAction(final PlayerOrder player, final Location location) {
        return ActionResult.FAILURE;

    }

    @Override
    public ActionResult useTools(final PlayerOrder player, final int toolIndex) {
        return ActionResult.FAILURE;
    }

    @Override
    public ActionResult noMoreToolsThisThrow(final PlayerOrder player) {
        return ActionResult.FAILURE;
    }

    @Override
    public ActionResult feedTribe(final PlayerOrder player, final Collection<Effect> resources) {
        if (playerTribes.get(player).feedTribe(resources)) {
            return ActionResult.ACTION_DONE;
        }
        return ActionResult.FAILURE;
    }

    @Override
    public ActionResult doNotFeedThisTurn(final PlayerOrder player) {
        if (playerTribes.get(player).doNotFeedThisTurn()) {
            return ActionResult.ACTION_DONE;
        }
        return ActionResult.FAILURE;
    }

    @Override
    public ActionResult makeAllPlayersTakeARewardChoice(final PlayerOrder player, final Effect reward) {
        return ActionResult.FAILURE;
    }

    @Override
    public HasAction tryToMakeAutomaticAction(final PlayerOrder player) {
        if (playerTribes.get(player).isTribeFed()) {
            return HasAction.NO_ACTION_POSSIBLE;
        }
        if (playerTribes.get(player).feedTribeIfEnoughFood()) {
            return HasAction.AUTOMATIC_ACTION_DONE;
        }

        return HasAction.WAITING_FOR_PLAYER_ACTION;
    }
}