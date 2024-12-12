package sk.uniba.fmph.dcs.game_board;

import java.util.ArrayList;
import java.util.Collection;
import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.*;
import java.util.Map;

/**
 * The ResourceSource class represents a location on the game board where players
 * can place figures to collect resources or food in the game.
 */
public final class ResourceSource implements InterfaceFigureLocationInternal {
    private final String name; // Name of the resource source.
    private final Effect resource; // Type of resource provided by this source.
    private final int maxFigures; // Maximum number of figures allowed at this source.
    private final int maxFigureColors; // Maximum number of different player colors allowed.
    private final ArrayList<PlayerOrder> figures; // List of figures placed at this source.

    /**
     * Constructs a ResourceSource.
     *
     * @param name           The name of the resource source.
     * @param resource       The type of resource this source provides.
     * @param maxFigures     Maximum number of figures this source can accommodate.
     * @param maxFigureColors Maximum number of different players allowed.
     * @throws IllegalArgumentException If the resource is not food or a valid resource.
     */
    public ResourceSource(String name, Effect resource, int maxFigures, int maxFigureColors) {
        if (!resource.isResourceOrFood()) {
            throw new IllegalArgumentException("Resource must be food or resource");
        }
        this.name = name;
        this.resource = resource;
        this.maxFigures = maxFigures;
        this.maxFigureColors = maxFigureColors;
        this.figures = new ArrayList<>();
    }

    /**
     * Attempts to place a specified number of figures for a player at this resource source.
     *
     * @param player      The player placing the figures.
     * @param figureCount The number of figures to place.
     * @return True if figures were placed successfully, false otherwise.
     */
    @Override
    public boolean placeFigures(Player player, int figureCount) {
        if (!canPlaceFigures(player, figureCount)) {
            return false;
        }
        for (int i = 0; i < figureCount; i++) {
            figures.add(player.playerOrder());
        }
        return true;
    }

    /**
     * Determines if placing figures is a valid action for the player.
     *
     * @param player The player attempting the action.
     * @param count  The number of figures to place.
     * @return The status of the action as a HasAction enum.
     */
    @Override
    public HasAction tryToPlaceFigures(Player player, int count) {
        if (!player.playerBoard().hasFigures(count)) {
            return HasAction.NO_ACTION_POSSIBLE;
        }
        if (canPlaceFigures(player, count)) {
            return HasAction.WAITING_FOR_PLAYER_ACTION;
        }
        return HasAction.NO_ACTION_POSSIBLE;
    }

    /**
     * Processes an action to collect resources for a player.
     *
     * @param player         The player performing the action.
     * @param inputResources Resources required as input (should be empty for resource sources).
     * @param outputResources Resources collected from this source.
     * @return The result of the action as an ActionResult enum.
     */
    @Override
    public ActionResult makeAction(Player player, Collection<Effect> inputResources, Collection<Effect> outputResources) {
        if (!hasFiguresFromPlayer(player.playerOrder())) {
            return ActionResult.FAILURE;
        }
        if (!inputResources.isEmpty() || outputResources.size() != countPlayerFigures(player.playerOrder())) {
            return ActionResult.FAILURE;
        }
        for (Effect output : outputResources) {
            if (output != this.resource) {
                return ActionResult.FAILURE;
            }
        }
        return ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE;
    }

    /**
     * Determines if the player can make an action at this resource source.
     *
     * @param player The player attempting the action.
     * @return The status of the action as a HasAction enum.
     */
    @Override
    public HasAction tryToMakeAction(Player player) {
        if (hasFiguresFromPlayer(player.playerOrder())) {
            return HasAction.WAITING_FOR_PLAYER_ACTION;
        }
        return HasAction.NO_ACTION_POSSIBLE;
    }

    /**
     * Skips the action for the specified player.
     *
     * @param player The player skipping the action.
     * @return Always returns false, as skipping is not allowed.
     */
    @Override
    public boolean skipAction(Player player) {
        return false;
    }

    /**
     * Clears figures from this resource source at the start of a new turn.
     *
     * @return Always returns false.
     */
    @Override
    public boolean newTurn() {
        figures.clear();
        return false;
    }

    /**
     * Checks if a player can place the specified number of figures.
     *
     * @param player      The player attempting to place figures.
     * @param figureCount The number of figures to place.
     * @return True if the player can place figures, false otherwise.
     */
    private boolean canPlaceFigures(Player player, int figureCount) {
        if (!player.playerBoard().hasFigures(figureCount)) {
            return false;
        }
        if (figures.size() + figureCount > maxFigures) {
            return false;
        }
        if (hasFiguresFromPlayer(player.playerOrder())) {
            return false;
        }
        if (!figures.isEmpty() && !containsPlayerOrder(figures, player.playerOrder())) {
            int currentColors = countDistinctPlayers();
            if (currentColors >= maxFigureColors) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the specified player has figures at this resource source.
     *
     * @param player The player to check for.
     * @return True if the player has figures here, false otherwise.
     */
    private boolean hasFiguresFromPlayer(PlayerOrder player) {
        return containsPlayerOrder(figures, player);
    }

    /**
     * Counts the number of figures a player has at this resource source.
     *
     * @param player The player to count figures for.
     * @return The number of figures the player has here.
     */
    private int countPlayerFigures(PlayerOrder player) {
        return (int) figures.stream().filter(p -> p.equals(player)).count();
    }

    /**
     * Counts the number of distinct players with figures at this resource source.
     *
     * @return The number of distinct players.
     */
    private int countDistinctPlayers() {
        return (int) figures.stream().distinct().count();
    }

    /**
     * Checks if the specified collection contains the specified player's order.
     *
     * @param collection The collection to search.
     * @param player     The player's order to look for.
     * @return True if the collection contains the player's order, false otherwise.
     */
    private boolean containsPlayerOrder(Collection<PlayerOrder> collection, PlayerOrder player) {
        return collection.stream().anyMatch(p -> p.equals(player));
    }

    /**
     * Returns the state of this resource source as a JSON string.
     *
     * @return A JSON representation of the state.
     */
    public String state() {
        Map<String, Object> state = Map.of(
                "name", name,
                "resource", resource,
                "maxFigures", maxFigures,
                "maxFigureColors", maxFigureColors,
                "figures", figures.stream().map(PlayerOrder::getOrder).toList()
        );
        return new JSONObject(state).toString();
    }
}