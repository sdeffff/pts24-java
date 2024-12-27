package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.ActionResult;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.HasAction;
import sk.uniba.fmph.dcs.stone_age.PlayerOrder;

import java.util.*;

public class ResourceSource implements InterfaceFigureLocationInternal {
    private final String sourceName;
    private final Effect resourceType;
    private final int maxAllowedFigures;
    private final int maxFigureColors;
    private final Map<PlayerOrder, Integer> playerFigures;

    public ResourceSource(String name, Effect resource, int maxFigures, int maxFigureColours) {
        this.playerFigures = new HashMap<>();
        this.sourceName = name;
        this.resourceType = resource;
        this.maxAllowedFigures = maxFigures;
        this.maxFigureColors = maxFigureColours;
    }

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

    @Override
    public HasAction tryToPlaceFigures(Player player, int count) {
        if (totalPlacedFigures() + count > maxAllowedFigures) {
            return HasAction.NO_ACTION_POSSIBLE;
        }
        if (!player.playerBoard().hasFigures(count)) {
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

    @Override
    public ActionResult makeAction(Player player, Collection<Effect> inputResources, Collection<Effect> outputResources) {
        if (tryToMakeAction(player) == HasAction.NO_ACTION_POSSIBLE) {
            return ActionResult.FAILURE;
        }

        PlayerOrder order = player.playerOrder();
        int figureCount = playerFigures.getOrDefault(order, 0);

        // Assuming currentThrow is initialized elsewhere.
        // currentThrow.initiate(player, resource, figureCount);

        player.playerBoard().takeFigures(-figureCount);
        playerFigures.remove(order);

        return ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE;
    }

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
        map.put("name", sourceName);
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
