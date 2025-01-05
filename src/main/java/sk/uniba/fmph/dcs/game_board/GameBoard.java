package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.InterfaceGetState;
import sk.uniba.fmph.dcs.stone_age.Location;

import java.util.*;

/**
 * Represents the game board, managing all locations and available resources for players.
 */
public class GameBoard implements InterfaceGetState {
    private final Map<Location, InterfaceFigureLocationInternal> locations;
    private static final int CLAY_IN_CLAY_MOUND = 18;
    private static final int STONE_IN_QUARRY = 12;
    private static final int GOLD_IN_RIVER = 10;
    private static final int WOOD_IN_FOREST = 28;
    private static final int FOOD_IN_HUNTING_GROUNDS = Integer.MAX_VALUE;
    private static final int BUILDING_PILES = 4;

    private static final String[] RESOURCE_NAMES = {
            "Clay Mound", "Forest", "Quarry", "River", "Hunting Grounds"
    };

    /**
     * Initializes the game board with the given players and buildings.
     *
     * @param players   A collection of players participating in the game.
     * @param buildings An array of building tiles available on the board. Must have at least 4 elements.
     * @throws IllegalArgumentException if buildings is null or has fewer than 4 elements.
     */
    public GameBoard(final Collection<Player> players, final Building[] buildings) {
        if (buildings == null || buildings.length < BUILDING_PILES) {
            throw new IllegalArgumentException("Buildings array must have at least 4 elements.");
        }

        locations = new HashMap<>();
        ToolMakerHutFields fields = new ToolMakerHutFields(players.size());

        locations.put(Location.HUT, new PlaceOnHutAdaptor(fields));
        locations.put(Location.FIELD, new PlaceOnFieldsAdaptor(fields));
        locations.put(Location.TOOL_MAKER, new PlaceOnToolMakerAdaptor(fields));

        locations.put(Location.CLAY_MOUND, new ResourceSource(RESOURCE_NAMES[0], Effect.CLAY, CLAY_IN_CLAY_MOUND, 1));
        locations.put(Location.FOREST, new ResourceSource(RESOURCE_NAMES[1], Effect.WOOD, WOOD_IN_FOREST, 1));
        locations.put(Location.QUARY, new ResourceSource(RESOURCE_NAMES[2], Effect.STONE, STONE_IN_QUARRY, 1));
        locations.put(Location.RIVER, new ResourceSource(RESOURCE_NAMES[3], Effect.GOLD, GOLD_IN_RIVER, 1));
        locations.put(Location.HUNTING_GROUNDS, new ResourceSource(RESOURCE_NAMES[4], Effect.FOOD, FOOD_IN_HUNTING_GROUNDS, Integer.MAX_VALUE));

        //building tiles
        Location[] buildingLocations = {
                Location.BUILDING_TILE1, Location.BUILDING_TILE2, Location.BUILDING_TILE3, Location.BUILDING_TILE4
        };

        for (int i = 0; i < BUILDING_PILES; i++) {
            locations.put(buildingLocations[i], new BuildingTile(buildings[i]));
        }

        generateCards();
    }

    /**
     * Generates civilization cards for the game deck.
     */
    private void generateCards() {
        //..
    }

    /**
     * Retrieves the combined state of all locations on the game board.
     *
     * @return A JSON string representing the current state of the game board.
     */
    @Override
    public String state() {
        Map<String, String> state = new HashMap<>();

        for (var entry : locations.entrySet()) {
            InterfaceFigureLocationInternal location = entry.getValue();
            if (location instanceof InterfaceGetState) {
                state.put(entry.getKey().toString(), ((InterfaceGetState) location).state());
            } else {
                state.put(entry.getKey().toString(), "State not available");
            }
        }

        return new JSONObject(state).toString(2);
    }
}
