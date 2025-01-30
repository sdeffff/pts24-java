package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.ActionResult;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.HasAction;
import sk.uniba.fmph.dcs.stone_age.PlayerOrder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public final class ResourceSource implements InterfaceFigureLocationInternal {
    private final String name;
    private final Effect resource;
    private final int maxFigureColours;
    private final int maxFigures;
    private ArrayList<PlayerOrder> figures;
    private static final int resourceMaxFiguresNumber = 7;
    private final CurrentThrowInterface currentThrow;
    private int playerCount;

    public ResourceSource(final String name, final Effect resource,
                          final CurrentThrowInterface currentThrow, final int playerCount) {
        this.name = name;
        this.resource = resource;
        this.figures = new ArrayList<>();
        this.currentThrow = currentThrow;
        this.maxFigureColours = playerCount;
        if (resource.equals(Effect.FOOD)) {
            maxFigures = Integer.MAX_VALUE;
        } else {
            maxFigures = resourceMaxFiguresNumber;
        }
    }

    @Override
    public boolean placeFigures(final Player player, final int figureCount) {
        if (tryToPlaceFigures(player, figureCount).equals(HasAction.NO_ACTION_POSSIBLE)
                || !player.playerBoard().takeFigures(figureCount)) {
            return false;
        }

        for (int i = 0; i < figureCount; i++) {
            figures.add(player.playerOrder());
        }
        playerCount++;
        return true;
    }

    @Override
    public HasAction tryToPlaceFigures(final Player player, final int count) {
        if (player.playerBoard().hasFigures(count) && !figures.contains(player.playerOrder())
                && maxFigures >= figures.size() + count && maxFigureColours >= playerCount) {
            return HasAction.WAITING_FOR_PLAYER_ACTION;
        }

        return HasAction.NO_ACTION_POSSIBLE;
    }

    @Override
    public ActionResult makeAction(final Player player, final Collection<Effect> inputResources,
                                   final Collection<Effect> outputResources) {
        if (tryToMakeAction(player).equals(HasAction.NO_ACTION_POSSIBLE)) {
            return ActionResult.FAILURE;
        }
        int diceNumber = 0;
        for (PlayerOrder p : figures) {
            if (p.equals(player.playerOrder())) {
                diceNumber++;
            }
        }

        removeFigures(player);
        currentThrow.initiate(player, resource, diceNumber);
        return ActionResult.ACTION_DONE_WAIT_FOR_TOOL_USE;
    }

    @Override
    public boolean skipAction(final Player player) {
        if (tryToMakeAction(player).equals(HasAction.NO_ACTION_POSSIBLE)) {
            return false;
        }

        removeFigures(player);
        return true;
    }

    @Override
    public HasAction tryToMakeAction(final Player player) {
        if (!figures.contains(player.playerOrder())) {
            return HasAction.NO_ACTION_POSSIBLE;
        }

        return HasAction.WAITING_FOR_PLAYER_ACTION;
    }

    @Override
    public boolean newTurn() {
        figures = new ArrayList<>();
        return false;
    }

    public String state() {
        Map<String, String> state = Map.of("name", name, "figures", figures.toString(),
                "resource", resource.toString(), "maxFigures", String.valueOf(maxFigures));
        return new JSONObject(state).toString();
    }

    private void removeFigures(final Player player) {
        ArrayList<PlayerOrder> newFigures = new ArrayList<>();
        for (PlayerOrder p: figures) {
            if (!p.equals(player.playerOrder())) {
                newFigures.add(p);
            }
        }
        playerCount--;
        figures = newFigures;
    }
}
