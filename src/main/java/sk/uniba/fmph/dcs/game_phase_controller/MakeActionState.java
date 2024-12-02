package sk.uniba.fmph.dcs.game_phase_controller;

import sk.uniba.fmph.dcs.stone_age.*;

import java.util.Collection;
import java.util.Map;

/**
 * The {@code MakeActionState} class represents the "Make Action" phase in the Stone Age game.
 * During this phase, the current player performs actions with their placed figures on the board.
 * This class implements the {@link InterfaceGamePhaseState} interface and provides the logic
 * for handling player actions specific to this phase.
 */
public class MakeActionState implements InterfaceGamePhaseState {

    /**
     * A map of all locations on the game board and their corresponding figure locations.
     * This map is used to delegate actions to the appropriate location.
     */
    private final Map<Location, InterfaceFigureLocation> places;

    /**
     * The player who is currently taking their turn during the "Make Action" phase.
     */
    private final PlayerOrder currentPlayer;

    /**
     * Constructs a new {@code MakeActionState} with the specified places and current player.
     *
     * @param places         a map of locations to their corresponding figure locations
     * @param currentPlayer  the player who is currently taking their turn
     */
    public MakeActionState(Map<Location, InterfaceFigureLocation> places, PlayerOrder currentPlayer) {
        this.places = places;
        this.currentPlayer = currentPlayer;
    }

    /**
     * Players cannot place figures during the "Make Action" phase.
     * This method always returns {@link boolean#FAILURE}.
     *
     * @param playerOrder   the player attempting to place figures
     * @param location      the location where the player wants to place figures
     * @param figuresCount  the number of figures the player wants to place
     * @return {@code boolean.FAILURE} indicating the action is not allowed
     */
    @Override
    public boolean placeFigures(PlayerOrder playerOrder, Location location, int figuresCount) {
        // Players cannot place figures during the 'Make Action' phase
        return boolean.FAILURE;
    }

    /**
     * Allows the current player to perform an action at a specified location.
     * The action is delegated to the corresponding {@link InterfaceFigureLocation}.
     *
     * @param player           the player attempting to make an action
     * @param location         the location where the action is to be made
     * @param inputResources   resources provided as input for the action
     * @param outputResources  resources expected as output from the action
     * @return the result of the action as an {@link boolean}
     */
    @Override
    public boolean makeAction(PlayerOrder player, Location location, Collection<Effect> inputResources, Collection<Effect> outputResources) {
        // Check if the player is the current player
        if (!player.equals(currentPlayer)) {
            return boolean.FAILURE;
        }

        InterfaceFigureLocation figureLocation = places.get(location);
        if (figureLocation == null) {
            return boolean.FAILURE;
        }

        // Attempt to make the action at the specified location
        return figureLocation.makeAction(player, inputResources, outputResources);
    }

    /**
     * Allows the current player to skip an action at a specified location.
     * The skip action is delegated to the corresponding {@link InterfaceFigureLocation}.
     *
     * @param player    the player attempting to skip an action
     * @param location  the location where the action is to be skipped
     * @return {@code boolean.ACTION_DONE} if the action was successfully skipped,
     *         {@code boolean.FAILURE} otherwise
     */
    @Override
    public boolean skipAction(PlayerOrder player, Location location) {
        // Check if the player is the current player
        if (!player.equals(currentPlayer)) {
            return boolean.FAILURE;
        }

        InterfaceFigureLocation figureLocation = places.get(location);
        if (figureLocation == null) {
            return boolean.FAILURE;
        }

        boolean success = figureLocation.skipAction(player);
        return success ? boolean.ACTION_DONE : boolean.FAILURE;
    }

    /**
     * Tool usage is not directly handled in this class during the "Make Action" phase.
     * This method always returns {@link boolean#FAILURE}.
     *
     * @param playerOrder  the player attempting to use a tool
     * @param toolIndex    the index of the tool being used
     * @return {@code boolean.FAILURE} indicating the action is not allowed
     */
    @Override
    public boolean useTools(PlayerOrder playerOrder, int toolIndex) {
        return boolean.FAILURE;
    }

    /**
     * Indicating that the player will not use more tools for the current throw.
     * This method always returns {@link boolean#FAILURE} as tool usage is not handled here.
     *
     * @param playerOrder  the player indicating no more tool usage
     * @return {@code boolean.FAILURE} indicating the action is not allowed
     */
    @Override
    public boolean noMoreToolsThisThrow(PlayerOrder playerOrder) {
        return boolean.FAILURE;
    }

    /**
     * Feeding the tribe is not handled during the "Make Action" phase.
     * This method always returns {@link boolean#FAILURE}.
     *
     * @param playerOrder  the player attempting to feed their tribe
     * @param resources    the resources being used to feed the tribe
     * @return {@code boolean.FAILURE} indicating the action is not allowed
     */
    @Override
    public boolean feedTribe(PlayerOrder playerOrder, Collection<Effect> resources) {
        // Feeding the tribe happens during the FeedTribe phase
        return boolean.FAILURE;
    }

    /**
     * Indicating that the player chooses not to feed their tribe this turn.
     * This method always returns {@link boolean#FAILURE} as it's not applicable in this phase.
     *
     * @param playerOrder  the player choosing not to feed their tribe
     * @return {@code boolean.FAILURE} indicating the action is not allowed
     */
    @Override
    public boolean doNotFeedThisTurn(PlayerOrder playerOrder) {
        // Feeding the tribe happens during the FeedTribe phase
        return boolean.FAILURE;
    }

    /**
     * Making all players take a reward choice is not applicable during the "Make Action" phase.
     * This method always returns {@link boolean#FAILURE}.
     *
     * @param playerOrder  the player attempting to make a reward choice
     * @param reward       the reward being chosen
     * @return {@code boolean.FAILURE} indicating the action is not allowed
     */
    @Override
    public boolean makeAllPlayersTakeARewardChoice(PlayerOrder playerOrder, Effect reward) {
        // Not applicable during the 'Make Action' phase
        return boolean.FAILURE;
    }

    /**
     * Attempts to perform any automatic actions for the current player during the "Make Action" phase.
     * Checks if the player has any actions to perform and returns the appropriate {@link HasAction} status.
     *
     * @param player  the player attempting to perform automatic actions
     * @return {@code HasAction.WAITING_FOR_PLAYER_ACTION} if the player has actions to perform,
     *         {@code HasAction.NO_ACTION_POSSIBLE} if no actions are possible,
     *         {@code HasAction.AUTOMATIC_ACTION_DONE} if an automatic action was performed
     */
    @Override
    public HasAction tryToMakeAutomaticAction(PlayerOrder player) {
        if (!player.equals(currentPlayer)) {
            return HasAction.NO_ACTION_POSSIBLE;
        }

        boolean hasWaitingActions = false;

        for (InterfaceFigureLocation figureLocation : places.values()) {
            HasAction hasAction = figureLocation.tryToMakeAction(player);
            if (hasAction == HasAction.WAITING_FOR_PLAYER_ACTION) {
                hasWaitingActions = true;
                break;
            }
            if (hasAction == HasAction.AUTOMATIC_ACTION_DONE) {
                return HasAction.AUTOMATIC_ACTION_DONE;
            }
        }

        if (hasWaitingActions) {
            return HasAction.WAITING_FOR_PLAYER_ACTION;
        }

        return HasAction.NO_ACTION_POSSIBLE;
    }
}