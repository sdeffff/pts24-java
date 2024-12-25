package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.*;
import java.util.*;

public class GameBoard {
    private final List<Player> players;
    private final Building[] buildings;
    private final CivilizationCard[] civilizationCards;
    private final CurrentThrowInterface currentThrow;
    private String state;
    private Map<Location, Integer> placedFigures;

    public GameBoard(List<Player> players, Building[] buildings, 
                    CivilizationCard[] civilizationCards,
                    CurrentThrowInterface currentThrow) {
        this.players = players;
        this.buildings = buildings;
        this.civilizationCards = civilizationCards;
        this.currentThrow = currentThrow;
        this.placedFigures = new HashMap<>();
        this.state = "Game started!";
    }

    public boolean placeFigures(PlayerOrder playerOrder, Location location, int count) {
        Player player = findPlayer(playerOrder);
        if (player != null && player.playerBoard().hasFigures(count)) {
            placedFigures.put(location, count);
            player.playerBoard().takeFigures(count);
            updateState(location);
            return true;
        }
        return false;
    }

    public ActionResult makeAction(PlayerOrder playerOrder, Location location, 
                                 List<Integer> toolsUsed, List<Effect> effects) {
        Player player = findPlayer(playerOrder);
        if (player == null || !placedFigures.containsKey(location)) {
            return ActionResult.FAILURE;
        }

        // Initialize throw first
        currentThrow.initiate(player, effects.get(0), placedFigures.get(location));
        
        // Validate resources before making changes
        if (!player.playerBoard().hasResources(effects)) {
            return ActionResult.FAILURE;
        }

        // Take resources and give effects only after validation
        if (player.playerBoard().takeResources(effects)) {
            player.playerBoard().giveEffect(effects);
            return ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE;
        }
        
        return ActionResult.FAILURE;
    }

    public String state() {
        return this.state;
    }

    public boolean newTurn() {
        placedFigures.clear();
        updateState("New turn started");
        return true;
    }

    private Player findPlayer(PlayerOrder order) {
        return players.stream()
                     .filter(p -> p.playerOrder().equals(order))
                     .findFirst()
                     .orElse(null);
    }

    private void updateState(Location location) {
        this.state = String.format("{\"location\":\"%s\",\"figures\":[%d]}", 
                                 location, 
                                 placedFigures.getOrDefault(location, 0));
    }

    private void updateState(String message) {
        this.state = String.format("{\"message\":\"%s\",\"figures\":[]}", message);
    }
}
