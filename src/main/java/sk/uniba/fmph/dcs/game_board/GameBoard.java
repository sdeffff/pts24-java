<<<<<<< HEAD
package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.InterfaceGetState;
import sk.uniba.fmph.dcs.stone_age.Location;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GameBoard implements InterfaceGetState {
    private final Map<Location, InterfaceFigureLocationInternal> locations;
    private static final int CLAY_IN_CLAY_MOUND = 18;
    private static final int STONE_IN_QUARRY = 12;
    private static final int GOLD_IN_RIVER = 10;
    private static final int WOOD_IN_FOREST = 28;

    private static final int BUILDING_PILES = 4;

    public GameBoard(final Collection<Player> players, final Building[] buildings) {

    }

    /**
     * @return state combined from everything on the game board
     */
    @Override
    public String state() {
        Map<Location, String> states = new HashMap<>();

        for (var x : locations.keySet()) {
            states.put(x, locations.get(x).state());
        }

        var ret = new JSONObject(states);
        return ret.toString();
    }
=======
package sk.uniba.fmph.dcs.game_board;

public class GameBoard {
    private String state;

    public GameBoard() {
        this.state = "Game started!";
    }

    public String state() {
        return this.state;
    }

    public boolean newTurn() {
        this.state = "New turn...";
        return false;
    }
>>>>>>> main
}