package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.HasAction;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.ActionResult;
import sk.uniba.fmph.dcs.stone_age.InterfaceFigureLocation;
import sk.uniba.fmph.dcs.stone_age.PlayerOrder;

import java.util.*;

public class FigureLocationAdaptor implements InterfaceFigureLocation {
    private final InterfaceFigureLocationInternal figureLocation;
    private final List<Player> players;

    /**
     * Constructs a FigureLocationAdaptor.
     *
     * @param figureLocation The internal figure location to adapt.
     * @param players        The list of players associated with the game.
     */
    public FigureLocationAdaptor(final InterfaceFigureLocationInternal figureLocation, final List<Player> players) {
        this.figureLocation = figureLocation;
        this.players = players;
    }

    /**
     * Retrieves the {@link Player} object corresponding to a given {@link PlayerOrder}.
     *
     * @param playerOrder The player order to find.
     * @return The corresponding Player object, or null if not found.
     */
    private Player getPlayerOrder(final PlayerOrder playerOrder) {
        for (Player pl : players) {
            if (pl.playerOrder().equals(playerOrder)) {
                return pl;
            }
        }
        return null;
    }

    /**
     * Places the specified number of figures for the given player.
     *
     * @param playerOrder The player's order.
     * @param figureCount The number of figures to place.
     * @return True if the placement was successful, otherwise false.
     */
    @Override
    public boolean placeFigures(final PlayerOrder playerOrder, final int figureCount) {
        Player player = getPlayerOrder(playerOrder);

        if (player != null) {
            return figureLocation.placeFigures(player, figureCount);
        }
        return false;
    }

    /**
     * Checks if figures can be placed for the given player.
     *
     * @param playerOrder The player's order.
     * @param count       The number of figures to place.
     * @return An instance of {@link HasAction} indicating if the action is possible.
     */
    @Override
    public HasAction tryToPlaceFigures(final PlayerOrder playerOrder, final int count) {
        Player player = getPlayerOrder(playerOrder);

        if (player != null) {
            return figureLocation.tryToPlaceFigures(player, count);
        }
        return HasAction.NO_ACTION_POSSIBLE;
    }

    /**
     * Performs an action for the player using input and output resources.
     *
     * @param playerOrder     The player's order.
     * @param inputResources  The input resources for the action.
     * @param outputResources The output resources resulting from the action.
     * @return An {@link ActionResult} representing the result of the action.
     */
    @Override
    public ActionResult makeAction(final PlayerOrder playerOrder, final Collection<Effect> inputResources, final Collection<Effect> outputResources) {
        Player player = getPlayerOrder(playerOrder);

        if (player != null) {
            return figureLocation.makeAction(player, inputResources, outputResources);
        }
        return ActionResult.FAILURE;
    }

    /**
     * Skips the action for the given player.
     *
     * @param playerOrder The player's order.
     * @return True if the action was successfully skipped, otherwise false.
     */
    @Override
    public boolean skipAction(final PlayerOrder playerOrder) {
        Player player = getPlayerOrder(playerOrder);

        if (player != null) {
            return figureLocation.skipAction(player);
        }
        return false;
    }

    /**
     * Attempts to perform an action for the given player.
     *
     * @param playerOrder The player's order.
     * @return An instance of {@link HasAction} indicating if the action is possible.
     */
    @Override
    public HasAction tryToMakeAction(final PlayerOrder playerOrder) {
        Player player = getPlayerOrder(playerOrder);

        if (player != null) {
            return figureLocation.tryToMakeAction(player);
        }
        return HasAction.NO_ACTION_POSSIBLE;
    }

    /**
     * Prepares the figure location for a new turn.
     *
     * @return True if the preparation was successful, otherwise false.
     */
    @Override
    public boolean newTurn() {
        return figureLocation.newTurn();
    }
}
