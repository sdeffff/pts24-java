package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.EndOfGameEffect;
import sk.uniba.fmph.dcs.stone_age.ImmediateEffect;

import java.util.ArrayList;
import java.util.List;

public final class GameBoardFactory {
    private static final int NUMBER_OF_RESOURCES_ARB_BUILDING_7 = 7;
    private static final int NUMBER_OF_RESOURCES_VAR_BUILDING_4 = 4;
    private static final int NUMBER_OF_RESOURCE_TYPES_2 = 2;
    private static final int NUMBER_OF_RESOURCE_TYPES_4 = 4;
    private static final int DESIRED_RESULT_IN_THROW = 6;

    private GameBoardFactory() {
    }

    public static GameBoard createGameBoard(final ArrayList<Player> players) {
        ArrayList<CivilizationCard> cards = new ArrayList<>();
        cards.add(new CivilizationCard(List.of(ImmediateEffect.AllPlayersTakeReward),
                List.of(EndOfGameEffect.FARMER)));
        cards.add(new CivilizationCard(List.of(ImmediateEffect.STONE, ImmediateEffect.STONE),
                List.of(EndOfGameEffect.TRANSPORT)));
        cards.add(new CivilizationCard(List.of(ImmediateEffect.CARD), List.of(EndOfGameEffect.WRITING)));
        cards.add(new CivilizationCard(List.of(ImmediateEffect.ArbitraryResource, ImmediateEffect.ArbitraryResource),
                List.of(EndOfGameEffect.MEDICINE)));
        cards.add(new CivilizationCard(List.of(ImmediateEffect.ThrowWood),
                List.of(EndOfGameEffect.SHAMAN, EndOfGameEffect.SHAMAN)));
        cards.add(new CivilizationCard(List.of(ImmediateEffect.ThrowWood), List.of(EndOfGameEffect.BUILDER)));
        CivilizationCardDeck deck = new CivilizationCardDeck();

        ArrayList<Building> buildings = new ArrayList<>();
        buildings.add(new ArbitraryBuilding(NUMBER_OF_RESOURCES_ARB_BUILDING_7));
        buildings.add(new VariableBuilding(NUMBER_OF_RESOURCES_VAR_BUILDING_4, NUMBER_OF_RESOURCE_TYPES_4));
        buildings.add(new SimpleBuilding(List.of(Effect.WOOD, Effect.WOOD, Effect.GOLD)));
        buildings.add(new SimpleBuilding(List.of(Effect.CLAY, Effect.STONE, Effect.GOLD)));
        buildings.add(new VariableBuilding(NUMBER_OF_RESOURCES_VAR_BUILDING_4, NUMBER_OF_RESOURCE_TYPES_2));
        return new GameBoard(deck, buildings, players, DESIRED_RESULT_IN_THROW);
    }
}