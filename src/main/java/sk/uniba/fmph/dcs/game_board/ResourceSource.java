package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.ActionResult;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.HasAction;
import sk.uniba.fmph.dcs.stone_age.PlayerOrder;

import java.util.*;

public class ResourceSource implements InterfaceFigureLocationInternal {
    private final String resourceName;
    private final Effect resourceType;
    private final int maxAllowedFigures;
    private final int maxFigureColors;
    private final Map<PlayerOrder, Integer> playerFigures;

    public ResourceSource(String name, Effect resource, int maxFigures, int maxFigureColours) {
        this.playerFigures = new HashMap<>();
        this.resourceName = name;
        this.resourceType = resource;
        this.maxAllowedFigures = maxFigures;
        this.maxFigureColors = maxFigureColours;
    }

    /**
        @param player The player placing the figures.
        @param figureCount The number of figures to place.
        @return True if the figures were placed successfully, false otherwise.
    */
    @Override
    public boolean placeFigures(Player player, int figureCount) {
        if (tryToPlaceFigures(player, figureCount) == HasAction.NO_ACTION_POSSIBLE) {
            return false;
        }

        PlayerOrder order = player.playerOrder();
        playerFigures.merge(order, figureCount, Integer::sum);
        player.playerBoard().takeFigures(figureCount);
        return true;
    }

    /**
        @param player The player attempting to place figures.
        @param figureCount The number of figures being checked.
        @return The action's status after attempting to place the figures.
    */
    @Override
    public HasAction tryToPlaceFigures(Player player, int figureCount) {
        if (totalPlacedFigures() + figureCount > maxAllowedFigures) {
            return HasAction.NO_ACTION_POSSIBLE;
        }
        if (!player.playerBoard().hasFigures(figureCount)) {
            return HasAction.NO_ACTION_POSSIBLE;
        }

        PlayerOrder order = player.playerOrder();
        if (playerFigures.containsKey(order)) {
            return HasAction.WAITING_FOR_PLAYER_ACTION;
        } else if (playerFigures.keySet().size() < maxFigureColors) {
            return HasAction.WAITING_FOR_PLAYER_ACTION;
        }
        return HasAction.NO_ACTION_POSSIBLE;
    }

    /**
        @param player The player performing the action.
        @param inputResources Resources provided by the player.
        @param outputResources Resources output from the action.
        @return The result of the action (e.g., success or failure).
    */
    @Override
    public ActionResult makeAction(Player player, Collection<Effect> inputResources, Collection<Effect> outputResources) {
        if (tryToMakeAction(player) == HasAction.NO_ACTION_POSSIBLE) {
            return ActionResult.FAILURE;
        }

        PlayerOrder order = player.playerOrder();
        int figureCount = playerFigures.getOrDefault(order, 0);

        player.playerBoard().takeFigures(-figureCount);
        playerFigures.remove(order);

        return ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE;
    }

    /**
        @param player The player choosing to skip the action.
        @return True if the skip was successful, false otherwise.
    */
    @Override
    public boolean skipAction(Player player) {
        PlayerOrder order = player.playerOrder();
        if (!playerFigures.containsKey(order)) {
            return false;
        }

        // Return figures
        int figureCount = playerFigures.get(order);
        player.playerBoard().takeFigures(-figureCount);
        playerFigures.remove(order);
        return true;
    }

    /**
        @param player The player attempting to perform the action.
        @return The action's status after attempting.
    */
    @Override
    public HasAction tryToMakeAction(Player player) {
        return playerFigures.containsKey(player.playerOrder()) ? HasAction.WAITING_FOR_PLAYER_ACTION : HasAction.NO_ACTION_POSSIBLE;
    }

    @Override
    public boolean newTurn() {
        playerFigures.clear();
        return false;
    }

    public String state() {
        Map<String, String> map = new HashMap<>();
        map.put("name", resourceName);
        map.put("resource", resourceType.toString());
        map.put("maxFigures", String.valueOf(maxAllowedFigures));
        map.put("maxFigureColours", String.valueOf(maxFigureColors));
        map.put("figures", playerFigures.toString());
        return new JSONObject(map).toString();
    }

    private int totalPlacedFigures() {
        return playerFigures.values().stream().mapToInt(Integer::intValue).sum();
    }
}
