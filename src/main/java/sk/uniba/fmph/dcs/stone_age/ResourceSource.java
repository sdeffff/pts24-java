package sk.uniba.fmph.dcs.stone_age;

import java.util.ArrayList;
import java.util.Collection;
import org.json.JSONObject;
import sk.uniba.fmph.dcs.game_board.Player;

import java.util.Map;

public final class ResourceSource implements InterfaceFigureLocationInternal {
    private final String name;
    private final Effect resource;
    private final int maxFigures;
    private final int maxFigureColors;
    private final ArrayList<PlayerOrder> figures;
    
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

    @Override
    public boolean placeFigures(Player player, int figureCount) {
        // Check if player can place figures here
        if (!canPlaceFigures(player, figureCount)) {
            return false;
        }

        // Add the figures
        for (int i = 0; i < figureCount; i++) {
            figures.add(player.playerOrder());
        }
        return true;
    }

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

    @Override
    public ActionResult makeAction(Player player, Collection<Effect> inputResources, 
            Collection<Effect> outputResources) {
        // Verify it's this player's figures
        if (!hasFiguresFromPlayer(player.playerOrder())) {
            return ActionResult.FAILURE;
        }

        // Resource sources don't take input resources
        if (!inputResources.isEmpty()) {
            return ActionResult.FAILURE;
        }

        // Resource sources must output exactly one type of resource per figure
        int playerFigureCount = countPlayerFigures(player.playerOrder());
        if (outputResources.size() != playerFigureCount) {
            return ActionResult.FAILURE;
        }
        
        for (Effect output : outputResources) {
            if (output != this.resource) {
                return ActionResult.FAILURE;
            }
        }

        return ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE;
    }

    @Override
    public HasAction tryToMakeAction(Player player) {
        if (hasFiguresFromPlayer(player.playerOrder())) {
            return HasAction.WAITING_FOR_PLAYER_ACTION;
        }
        return HasAction.NO_ACTION_POSSIBLE;
    }

    @Override
    public boolean skipAction(Player player) {
        return false; // Can't skip resource gathering
    }

    @Override
    public boolean newTurn() {
        figures.clear();
        return false; // Resource sources don't trigger game end
    }

    private boolean canPlaceFigures(Player player, int figureCount) {
        // Check if player has enough figures
        if (!player.playerBoard().hasFigures(figureCount)) {
            return false;
        }

        // Check if space available
        if (figures.size() + figureCount > maxFigures) {
            return false;
        }

        // Check if player already has figures here
        if (hasFiguresFromPlayer(player.playerOrder())) {
            return false;
        }

        // Check number of different players
        if (!figures.isEmpty() && !containsPlayerOrder(figures, player.playerOrder())) {
            int currentColors = countDistinctPlayers();
            if (currentColors >= maxFigureColors) {
                return false;
            }
        }

        return true;
    }

    private boolean hasFiguresFromPlayer(PlayerOrder player) {
        return containsPlayerOrder(figures, player);
    }

    private int countPlayerFigures(PlayerOrder player) {
        int count = 0;
        for (PlayerOrder p : figures) {
            if (p.equals(player)) {
                count++;
            }
        }
        return count;
    }

    private int countDistinctPlayers() {
        return (int) figures.stream().distinct().count();
    }

    private boolean containsPlayerOrder(Collection<PlayerOrder> collection, PlayerOrder player) {
        return collection.stream().anyMatch(p -> p.equals(player));
    }

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
