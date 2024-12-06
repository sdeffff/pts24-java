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
    private static final int STONE_IN_QUARY = 12;
    private static final int GOLD_IN_RIVER = 10;
    private static final int WOOD_IN_FOREST = 28;
    private static final int FOOD_IN_HUNTING_GROUNDS = Integer.MAX_VALUE; // No limit for food gathering.
    private static final int BUILDING_PILES = 4;
    private static final int CIVILIZATION_CARDS_PLACES = 4;

    private CivilizationCard[] civilizationCards;

    /**
     * Initializes the game board with the given players and buildings.
     *
     * @param players   A collection of players participating in the game.
     * @param buildings An array of building tiles available on the board.
     */
    public GameBoard(final Collection<Player> players, final Building[] buildings) {
        ToolMakerHutsFields fields = new ToolMakerHutsFields(players.size());
        locations = new HashMap<>();

        locations.put(Location.HUT, new PlaceOnHutAdaptor(fields));
        locations.put(Location.FIELD, new PlaceOnFieldsAdaptor(fields));
        locations.put(Location.TOOL_MAKER, new PlaceOnToolMakerAdaptor(fields));

        // Add resources
        locations.put(Location.CLAY_MOUND, new ResourceSource("Clay Mound", Effect.CLAY, CLAY_IN_CLAY_MOUND, 1));
        locations.put(Location.FOREST, new ResourceSource("Forest", Effect.WOOD, WOOD_IN_FOREST, 1));
        locations.put(Location.QUARY, new ResourceSource("Quarry", Effect.STONE, STONE_IN_QUARY, 1));
        locations.put(Location.RIVER, new ResourceSource("River", Effect.GOLD, GOLD_IN_RIVER, 1));
        locations.put(Location.HUNTING_GROUNDS, new ResourceSource("Hunting Grounds", Effect.FOOD, FOOD_IN_HUNTING_GROUNDS, Integer.MAX_VALUE));

        // Add building tiles
        ArrayList<Location> buildingTiles = new ArrayList<>();
        buildingTiles.add(Location.BUILDING_TILE1);
        buildingTiles.add(Location.BUILDING_TILE2);
        buildingTiles.add(Location.BUILDING_TILE3);
        buildingTiles.add(Location.BUILDING_TILE4);

        for (int i = 0; i < BUILDING_PILES; i++) {
            locations.put(buildingTiles.get(i), new BuildingTile(buildings[i]));
        }

        generateCards();
    }

    /**
     * Generates civilization cards for the game deck.
     */
    private void generateCards() {
        //...
    }

    /**
     * Retrieves the combined state of all locations on the game board.
     *
     * @return A JSON string representing the current state of the game board.
     */
    @Override
    public String state() {
        Map<String, String> states = new HashMap<>();

        for (var entry : locations.entrySet()) {
            states.put(entry.getKey().toString(), entry.getValue().state());
        }

        //Converting to JSON
        return new JSONObject(states).toString(2); // Pretty-print with 2-space indentation
    }
}
