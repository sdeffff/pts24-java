package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static sk.uniba.fmph.dcs.stone_age.ImmediateEffect.AllPlayersTakeReward;

/**
 * Represents the game board for the Stone Age game, managing player interactions
 * with various locations and resources.
 */
public final class GameBoard implements InterfaceGetState {
    private final ArrayList<Player> players; // List of players in the game.
    private final ToolMakerHutFields toolMakerHutFields; // ToolMaker fields specific to players.
    private final CurrentThrow currentThrow; // Current throw utility.
    private final CivilizationCardDeck deck; // Deck of Civilization Cards.
    private final RewardMenu rewardMenu; // Reward menu for players.
    private final Map<Location, InterfaceFigureLocationInternal> locations; // Map of locations on the board.
    private static final int REQUIRED_RESOURCES_CARD4 = 1;
    private static final int REQUIRED_RESOURCES_CARD3 = 2;
    private static final int REQUIRED_RESOURCES_CARD2 = 3;
    private static final int REQUIRED_RESOURCES_CARD1 = 4;
    private static final int NUMBER_BUILDING_TILES = 4;

    /**
     * Constructs the game board.
     *
     * @param deck               The CivilizationCardDeck used in the game.
     * @param buildings          The list of buildings available in the game.
     * @param players            The list of players in the game.
     * @param desiredResultThrow The initial throw result.
     */
    public GameBoard(final CivilizationCardDeck deck, final ArrayList<Building> buildings,
                     final ArrayList<Player> players, final int desiredResultThrow) {
        this.deck = deck;
        this.players = players;
        this.currentThrow = new CurrentThrow(); // Assuming CurrentThrow is meant for reuse
        this.rewardMenu = new RewardMenu(players);
        this.toolMakerHutFields = new ToolMakerHutFields(players.size());

        locations = new HashMap<>();
        locations.put(Location.TOOL_MAKER, new PlaceOnToolMakerAdaptor(toolMakerHutFields));
        locations.put(Location.HUT, new PlaceOnHutAdaptor());
        locations.put(Location.FIELD, new PlaceOnFieldsAdaptor(toolMakerHutFields));

        Map<ImmediateEffect, EvaluateCivilizationCardImmediateEffect> evaluate = initializeEvaluationMap();

        locations.put(Location.HUNTING_GROUNDS,
                new ResourceSource("HuntingGrounds", Effect.FOOD, desiredResultThrow, players.size()));
        locations.put(Location.FOREST, new ResourceSource("Forest", Effect.WOOD, desiredResultThrow, players.size()));
        locations.put(Location.CLAY_MOUND, new ResourceSource("ClayMound", Effect.CLAY, desiredResultThrow, players.size()));
        locations.put(Location.RIVER, new ResourceSource("River", Effect.GOLD, desiredResultThrow, players.size()));

        // Adapting CivilizationCardPlace initialization
        CivilizationCardPlace card4 = new CivilizationCardPlace(REQUIRED_RESOURCES_CARD4,
                new ArrayList<>(), deck, evaluate.get(ImmediateEffect.CARD));
        CivilizationCardPlace card3 = new CivilizationCardPlace(REQUIRED_RESOURCES_CARD3,
                new ArrayList<>(), deck, evaluate.get(ImmediateEffect.CARD));
        CivilizationCardPlace card2 = new CivilizationCardPlace(REQUIRED_RESOURCES_CARD2,
                new ArrayList<>(), deck, evaluate.get(ImmediateEffect.CARD));
        CivilizationCardPlace card1 = new CivilizationCardPlace(REQUIRED_RESOURCES_CARD1,
                new ArrayList<>(), deck, evaluate.get(ImmediateEffect.CARD));

        locations.put(Location.CIVILISATION_CARD4, card4);
        locations.put(Location.CIVILISATION_CARD3, card3);
        locations.put(Location.CIVILISATION_CARD2, card2);
        locations.put(Location.CIVILISATION_CARD1, card1);

        ArrayList<Building> buildings1 = new ArrayList<>();
        ArrayList<Building> buildings2 = new ArrayList<>();
        ArrayList<Building> buildings3 = new ArrayList<>();
        ArrayList<Building> buildings4 = new ArrayList<>();
        for (int i = 0; i < buildings.size(); i++) {
            switch (i % NUMBER_BUILDING_TILES) {
                case 0 -> buildings1.add(buildings.get(i));
                case 1 -> buildings2.add(buildings.get(i));
                case 2 -> buildings3.add(buildings.get(i));
                case NUMBER_BUILDING_TILES - 1 -> buildings4.add(buildings.get(i));
                default -> {
                }
            }
        }
        locations.put(Location.BUILDING_TILE1, new BuildingTile(buildings1.getFirst()));
        locations.put(Location.BUILDING_TILE2, new BuildingTile(buildings2.getFirst()));
        locations.put(Location.BUILDING_TILE3, new BuildingTile(buildings3.getFirst()));
        locations.put(Location.BUILDING_TILE4, new BuildingTile(buildings4.getFirst()));

    }

    /**
     * Initializes the evaluation map for Civilization Card effects.
     *
     * @return A map linking ImmediateEffects to their evaluators.
     */
    private Map<ImmediateEffect, EvaluateCivilizationCardImmediateEffect> initializeEvaluationMap() {
        Map<ImmediateEffect, EvaluateCivilizationCardImmediateEffect> evaluate = new HashMap<>();

        // Using the new GetSomethingThrow constructor with CurrentThrow and Effect
        evaluate.put(ImmediateEffect.ThrowWood, new GetSomethingThrow(currentThrow, Effect.WOOD));
        evaluate.put(ImmediateEffect.ThrowClay, new GetSomethingThrow(currentThrow, Effect.CLAY));
        evaluate.put(ImmediateEffect.ThrowStone, new GetSomethingThrow(currentThrow, Effect.STONE));
        evaluate.put(ImmediateEffect.ThrowGold, new GetSomethingThrow(currentThrow, Effect.GOLD));

        // Other effects remain unchanged
        evaluate.put(ImmediateEffect.WOOD, new GetSomethingFixed(Effect.WOOD));
        evaluate.put(ImmediateEffect.CLAY, new GetSomethingFixed(Effect.CLAY));
        evaluate.put(ImmediateEffect.STONE, new GetSomethingFixed(Effect.STONE));
        evaluate.put(ImmediateEffect.GOLD, new GetSomethingFixed(Effect.GOLD));
        evaluate.put(ImmediateEffect.FOOD, new GetSomethingFixed(Effect.FOOD));
        evaluate.put(ImmediateEffect.TOOL, new GetSomethingFixed(Effect.TOOL));
        evaluate.put(ImmediateEffect.CARD, new GetCard(deck));
        evaluate.put(ImmediateEffect.ArbitraryResource, new GetCard(deck));
        evaluate.put(AllPlayersTakeReward, new AllPlayersTakeReward(rewardMenu, players.size()));

        return evaluate;
    }

    public RewardMenu getRewardMenu() {
        return rewardMenu;
    }

    public Map<Location, InterfaceFigureLocation> getLocationToFigureLocationMap() {
        Map<Location, InterfaceFigureLocation> map = new HashMap<>();
        for (Location l : locations.keySet()) {
            map.put(l, new FigureLocationAdaptor(locations.get(l), players));
        }
        return map;
    }

    @Override
    public String state() {
        Map<String, String> state = new HashMap<>();
        state.put("AllPlayers", players.toString());
        state.put("ToolMakeHutFields", toolMakerHutFields.state());
        for (Location l : locations.keySet()) {
            if (l.equals(Location.TOOL_MAKER) || l.equals(Location.HUT) || l.equals(Location.FIELD)) {
                continue;
            }
            state.put(l.toString(), locations.get(l).toString());
        }
        state.put("CurrentThrow", String.valueOf(currentThrow));
        state.put("CardDeck", deck.state());
        state.put("RewardMenu", rewardMenu.state());
        return new JSONObject(state).toString();
    }
}