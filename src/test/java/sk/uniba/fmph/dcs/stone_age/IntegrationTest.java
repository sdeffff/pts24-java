package sk.uniba.fmph.dcs.stone_age;

import org.junit.Test;
import sk.uniba.fmph.dcs.game_board.*;
import sk.uniba.fmph.dcs.player_board.PlayerBoard;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IntegrationTest {

    private static class ThrowMock implements ThrowInterface {
        private ArrayList<Integer> rolls;
        @Override
        public ArrayList<Integer> throwDice(int dices) {
            ArrayList<Integer> result = new ArrayList<>();
            for (int i = 0; i < dices; i++) {
                result.add(rolls.get(i));
            }
            return result;
        }

        @Override
        public void setRolls(ArrayList<Integer> rolls) {
            this.rolls = rolls;
        }

        public void setToSix(int numberOfDice) {
            ArrayList<Integer> rolls = new ArrayList<>();
            for (int i = 0; i < numberOfDice; i++) {
                rolls.add(6);
            }
            this.rolls = rolls;
        }
    }

    private static class ObserverMock implements InterfaceStoneAgeObserver {
        String state;
        @Override
        public void update(String gameState) {
            this.state = gameState;
        }
    }
    public static ArrayList<CivilizationCard> cardsInitiate() {
        ArrayList<CivilizationCard> cards = new ArrayList<>();
        CivilizationCard card1 = new CivilizationCard(List.of(ImmediateEffect.ThrowStone, ImmediateEffect.ThrowStone),
                List.of(EndOfGameEffect.SHAMAN));
        CivilizationCard card2 = new CivilizationCard(List.of(ImmediateEffect.GOLD),
                List.of(EndOfGameEffect.POTTERY));
        CivilizationCard card3 = new CivilizationCard(List.of(ImmediateEffect.ArbitraryResource),
                List.of(EndOfGameEffect.TRANSPORT));
        CivilizationCard card4 = new CivilizationCard(List.of(ImmediateEffect.AllPlayersTakeReward),
                List.of(EndOfGameEffect.TOOL_MAKER));
        CivilizationCard card5 = new CivilizationCard(List.of(ImmediateEffect.CARD),
                List.of(EndOfGameEffect.WRITING));
        CivilizationCard card6 = new CivilizationCard(List.of(ImmediateEffect.ThrowWood),
                List.of(EndOfGameEffect.SUNDIAL));
        CivilizationCard card7 = new CivilizationCard(List.of(ImmediateEffect.ThrowStone, ImmediateEffect.ThrowStone),
                List.of(EndOfGameEffect.SHAMAN));
        CivilizationCard card8 = new CivilizationCard(List.of(ImmediateEffect.GOLD),
                List.of(EndOfGameEffect.POTTERY));
        CivilizationCard card9 = new CivilizationCard(List.of(ImmediateEffect.ArbitraryResource),
                List.of(EndOfGameEffect.TRANSPORT));
        CivilizationCard card10 = new CivilizationCard(List.of(ImmediateEffect.AllPlayersTakeReward),
                List.of(EndOfGameEffect.TOOL_MAKER));
        CivilizationCard card11 = new CivilizationCard(List.of(ImmediateEffect.CARD),
                List.of(EndOfGameEffect.WRITING));
        CivilizationCard card12 = new CivilizationCard(List.of(ImmediateEffect.ThrowWood),
                List.of(EndOfGameEffect.SUNDIAL));
        cards.add(card1);
        cards.add(card2);
        cards.add(card3);
        cards.add(card4);
        cards.add(card5);
        cards.add(card6);
        cards.add(card7);
        cards.add(card8);
        cards.add(card9);
        cards.add(card10);
        cards.add(card11);
        cards.add(card12);
        return cards;
    }

    private StoneAgeGame gameInitiate(ArrayList<Integer> players, ArrayList<InterfaceStoneAgeObserver> observers,
                                      ThrowInterface throwMock, ArrayList<CivilizationCard> cards) {

        CurrentThrow currentThrow = new CurrentThrow(throwMock);

        ArrayList<Building> buildings = new ArrayList<>();
        Building building1 = new ArbitraryBuilding(3);
        Building building2 = new VariableBuilding(1, 1);
        Building building3 = new SimpleBuilding(List.of(Effect.WOOD, Effect.WOOD, Effect.GOLD));
        Building building4 = new SimpleBuilding(List.of(Effect.CLAY, Effect.STONE, Effect.GOLD));
        Building building5 = new VariableBuilding(5, 3);
        Building building6 = new ArbitraryBuilding(3);
        Building building7 = new VariableBuilding(1, 1);
        Building building8 = new SimpleBuilding(List.of(Effect.WOOD, Effect.WOOD));
        Building building9 = new SimpleBuilding(List.of(Effect.CLAY));
        Building building10 = new VariableBuilding(2, 2);
        buildings.add(building1);
        buildings.add(building2);
        buildings.add(building3);
        buildings.add(building4);
        buildings.add(building5);
        buildings.add(building6);
        buildings.add(building7);
        buildings.add(building8);
        buildings.add(building9);
        buildings.add(building10);

        return new StoneAgeGame(players, observers, currentThrow, throwMock, cards, buildings);
    }

    @Test
    public void mainTest() {
        ArrayList<InterfaceStoneAgeObserver> observers = new ArrayList<>();
        ObserverMock observer1 = new ObserverMock();
        ObserverMock observer2 = new ObserverMock();
        observers.add(observer1);
        observers.add(observer2);
        ArrayList<Integer> players = new ArrayList<>();
        players.add(1);
        players.add(2);

        ThrowMock throwMock = new ThrowMock();
        StoneAgeGame game = gameInitiate(players, observers, throwMock, cardsInitiate());
        ArrayList<PlayerBoard> playerBoards = game.getPlayerBoards();
        PlayerBoard playerBoard1 = playerBoards.get(0);
        PlayerBoard playerBoard2 = playerBoards.get(1);
        Map<Effect, Integer> resources1 = playerBoard1.playerResourcesAndFood().getResources();
        Map<Effect, Integer> resources2 = playerBoard2.playerResourcesAndFood().getResources();

        assertTrue(game.placeFigures(1, Location.HUT, 2));
        assertTrue(game.placeFigures(2, Location.HUNTING_GROUNDS, 5));
        assertTrue(game.placeFigures(1, Location.FOREST, 3));

        assertTrue(game.makeAction(1, Location.HUT, null, null));
        throwMock.setToSix(5);
        assertTrue(game.makeAction(1, Location.FOREST, null, null));
        assertTrue(game.makeAction(2, Location.HUNTING_GROUNDS, null, null));

        //12 was starting food, -6 for feeding tribe
        assertEquals(6, (int) resources1.get(Effect.FOOD));     //12 - 6
        //3 figures, each one rolled 6
        assertEquals(6, (int) resources1.get(Effect.WOOD));     //3 * 6 / 2
        //12 - 5 for feeding + 5 figures, each with 6 roll
        assertEquals(22, (int) resources2.get(Effect.FOOD));    // 12 - 5 + 5*6/2

        assertTrue(game.placeFigures(2, Location.FOREST, 5));
        assertTrue(game.placeFigures(1, Location.HUNTING_GROUNDS, 6));

        assertTrue(game.makeAction(2, Location.FOREST, null, null));
        throwMock.setToSix(6);
        assertTrue(game.makeAction(1, Location.HUNTING_GROUNDS, null, null));

        //5 figures, each with 6 roll
        assertEquals(10, (int) resources2.get(Effect.WOOD));    //5*6 / 3
        assertEquals(18, (int) resources1.get(Effect.FOOD));    //6*6 / 2

        assertTrue(game.placeFigures(1, Location.TOOL_MAKER, 1));
        assertTrue(game.placeFigures(2, Location.HUT, 2));
        assertTrue(game.placeFigures(1, Location.FOREST, 3));
        assertTrue(game.placeFigures(2, Location.CLAY_MOUND, 3));
        assertTrue(game.placeFigures(1, Location.CLAY_MOUND, 2));

        assertTrue(game.makeAction(1, Location.TOOL_MAKER, null, null));
        ArrayList<Integer> rolls = new ArrayList<>();
        rolls.add(6);
        rolls.add(5);
        rolls.add(6);
        throwMock.setRolls(rolls);
        assertTrue(game.makeAction(1, Location.FOREST, null, null));
        assertTrue(game.useTools(1, 0));
        throwMock.setToSix(6);
        assertTrue(game.makeAction(1, Location.CLAY_MOUND, null, null));
        assertTrue(game.makeAction(2, Location.HUT, null, null));
        assertTrue(game.makeAction(2, Location.CLAY_MOUND, null, null));

        assertEquals(4, (int) resources2.get(Effect.CLAY));     //3 * 6 / 4
        //player1 already had 6 wood, he rolled 6,6,5 and used 1 tool, so the final roll is 18
        assertEquals(12, (int) resources1.get(Effect.WOOD));    //6 + 3 * 6 / 3
        assertEquals(3, (int) resources1.get(Effect.CLAY));     //2 * 6 / 4

        assertTrue(game.placeFigures(2, Location.TOOL_MAKER, 1));
        assertTrue(game.placeFigures(1, Location.BUILDING_TILE1, 1));
        assertTrue(game.placeFigures(2, Location.BUILDING_TILE2, 1));
        assertTrue(game.placeFigures(1, Location.CIVILISATION_CARD1, 1));
        assertTrue(game.placeFigures(2, Location.FIELD, 1));
        assertTrue(game.placeFigures(1, Location.HUNTING_GROUNDS, 4));
        assertTrue(game.placeFigures(2, Location.HUNTING_GROUNDS, 3));

        assertTrue(game.makeAction(2, Location.TOOL_MAKER,null, null));
        assertTrue(game.makeAction(2, Location.HUNTING_GROUNDS,null, null));
        assertTrue(game.noMoreToolsThisThrow(2));
        assertTrue(game.makeAction(2, Location.FIELD,null, null));
        assertTrue(game.makeAction(2, Location.BUILDING_TILE2,List.of(Effect.WOOD, Effect.CLAY), null));
        assertTrue(game.makeAction(1, Location.HUNTING_GROUNDS, null, null));
        assertTrue(game.noMoreToolsThisThrow(1));
        assertTrue(game.makeAction(1, Location.BUILDING_TILE1, List.of(Effect.CLAY), null));
        assertTrue(game.makeAction(1, Location.CIVILISATION_CARD1, List.of(Effect.WOOD, Effect.WOOD, Effect.WOOD, Effect.WOOD), List.of(Effect.GOLD)));

        assertEquals(18, (int) resources1.get(Effect.FOOD));   //6 + 4*6 / 2
        assertEquals(8, (int) resources1.get(Effect.WOOD));    //12 - 4
        assertEquals(1, (int) resources1.get(Effect.GOLD));
        assertEquals(15, (int) resources2.get(Effect.FOOD));   //6 + 3*6 / 2

        assertTrue(game.placeFigures(1, Location.TOOL_MAKER, 1));
        assertTrue(game.placeFigures(2, Location.HUT, 2));
        assertTrue(game.placeFigures(1, Location.CIVILISATION_CARD4, 1));
        assertTrue(game.placeFigures(2, Location.CIVILISATION_CARD3, 1));
        assertTrue(game.placeFigures(1, Location.CIVILISATION_CARD1, 1));
        assertTrue(game.placeFigures(2, Location.CIVILISATION_CARD2, 1));
        assertTrue(game.placeFigures(1, Location.BUILDING_TILE2, 1));
        assertTrue(game.placeFigures(2, Location.FOREST, 2));
        assertTrue(game.placeFigures(1, Location.FOREST, 2));

        assertTrue(game.makeAction(1, Location.TOOL_MAKER, null, null));
        assertTrue(game.makeAction(1, Location.FOREST, null, null));
        assertTrue(game.useTools(1, 0));
        assertTrue(game.noMoreToolsThisThrow(1));
        rolls = new ArrayList<>();
        rolls.add(5);
        rolls.add(6);
        throwMock.setRolls(rolls);
        assertTrue(game.makeAction(1, Location.CIVILISATION_CARD1,
                List.of(Effect.WOOD, Effect.WOOD, Effect.WOOD, Effect.WOOD), null));
        assertTrue(game.makeAllPlayersTakeARewardChoice(1, Effect.TOOL));
        assertTrue(game.makeAction(1, Location.CIVILISATION_CARD4, List.of(Effect.WOOD), null));
        assertTrue(game.makeAction(1, Location.BUILDING_TILE2, List.of(Effect.GOLD, Effect.GOLD, Effect.WOOD), null));
        throwMock.setToSix(10);
        assertTrue(game.makeAction(2, Location.FOREST, null, null));
        assertTrue(game.noMoreToolsThisThrow(2));
        assertTrue(game.makeAction(2, Location.HUT, null, null));
        assertTrue(game.makeAction(2, Location.CIVILISATION_CARD2, List.of(Effect.WOOD, Effect.WOOD, Effect.WOOD), null));
        assertTrue(game.makeAction(2, Location.CIVILISATION_CARD3, List.of(Effect.WOOD, Effect.WOOD), null));
        assertTrue(game.useTools(2,0));

        assertEquals(12, (int) resources1.get(Effect.FOOD));

        assertTrue(game.placeFigures(2, Location.HUNTING_GROUNDS, 7));
        assertTrue(game.placeFigures(1, Location.BUILDING_TILE2, 1));
        assertTrue(game.placeFigures(1, Location.TOOL_MAKER, 1));
        assertTrue(game.placeFigures(1, Location.HUNTING_GROUNDS, 4));

        assertTrue(game.makeAction(2, Location.HUNTING_GROUNDS, null, null));
        assertTrue(game.noMoreToolsThisThrow(2));
        assertTrue(game.makeAction(1, Location.BUILDING_TILE2, List.of(Effect.WOOD), null));
        assertTrue(game.makeAction(1, Location.TOOL_MAKER, null, null));
        assertTrue(game.makeAction(1, Location.HUNTING_GROUNDS, null, null));
        assertTrue(game.useTools(1, 0));
        assertTrue(game.useTools(1, 1));
        assertTrue(game.useTools(1, 2));

        //6 + (4*6 + 4)/2
        assertEquals(20, (int) resources1.get(Effect.FOOD));
        assertEquals(2, (int) resources1.get(Effect.CLAY));
        assertEquals(5, (int) resources1.get(Effect.WOOD));

        boolean isEnded = false;
        try {
            assertTrue(game.placeFigures(1, Location.HUNTING_GROUNDS, 5));
        } catch (AssertionError e) {
            isEnded = true;
        }
        assertTrue(isEnded);
        assertEquals( 37,playerBoard1.getPoints());
        assertEquals(3, (int) resources2.get(Effect.CLAY));
        assertEquals(12, (int) resources2.get(Effect.WOOD));
        assertEquals(33, playerBoard2.getPoints());
    }

    @Test
    public void noFoodTest() {
        ArrayList<InterfaceStoneAgeObserver> observers = new ArrayList<>();
        ObserverMock observer1 = new ObserverMock();
        ObserverMock observer2 = new ObserverMock();
        observers.add(observer1);
        observers.add(observer2);
        ArrayList<Integer> players = new ArrayList<>();
        players.add(1);
        players.add(2);

        ThrowMock throwMock = new ThrowMock();
        StoneAgeGame game = gameInitiate(players, observers, throwMock, cardsInitiate());
        ArrayList<PlayerBoard> playerBoards = game.getPlayerBoards();
        PlayerBoard playerBoard1 = playerBoards.get(0);
        PlayerBoard playerBoard2 = playerBoards.get(1);
        Map<Effect, Integer> resources1 = playerBoard1.playerResourcesAndFood().getResources();
        Map<Effect, Integer> resources2 = playerBoard2.playerResourcesAndFood().getResources();

        throwMock.setToSix(10);
        assertTrue(game.placeFigures(1, Location.FOREST, 5));
        assertTrue(game.placeFigures(2, Location.CLAY_MOUND, 5));

        assertTrue(game.makeAction(1, Location.FOREST, null, null));
        assertTrue(game.makeAction(2, Location.CLAY_MOUND, null, null));

        assertTrue(game.placeFigures(2, Location.FOREST, 5));
        assertTrue(game.placeFigures(1, Location.CLAY_MOUND, 5));

        assertTrue(game.makeAction(2, Location.FOREST, null, null));
        assertTrue(game.makeAction(1, Location.CLAY_MOUND, null, null));

        assertTrue(game.placeFigures(1, Location.FOREST, 5));
        assertTrue(game.placeFigures(2, Location.CLAY_MOUND, 5));

        assertTrue(game.makeAction(1, Location.FOREST, null, null));
        assertTrue(game.makeAction(2, Location.CLAY_MOUND, null, null));

        assertEquals(20, (int) resources1.get(Effect.WOOD));
        assertEquals(7, (int) resources1.get(Effect.CLAY));
        assertEquals(10, (int) resources2.get(Effect.WOOD));
        assertEquals(14, (int) resources2.get(Effect.CLAY));

        assertTrue(game.feedTribe(1, List.of(Effect.WOOD, Effect.WOOD, Effect.WOOD, Effect.FOOD, Effect.FOOD)));
        assertTrue(game.doNotFeedThisTurn(2));

        //24 for resources
        assertEquals(24, playerBoard1.getPoints());
        //24 for resources, -10 for tribe not fed
        assertEquals(14, playerBoard2.getPoints());
    }

    @Test
    public void NoCivilisationCardsEndOfGame() {
        ArrayList<InterfaceStoneAgeObserver> observers = new ArrayList<>();
        ObserverMock observer1 = new ObserverMock();
        ObserverMock observer2 = new ObserverMock();
        observers.add(observer1);
        observers.add(observer2);
        ArrayList<Integer> players = new ArrayList<>();
        players.add(1);
        players.add(2);

        ThrowMock throwMock = new ThrowMock();
        ArrayList<CivilizationCard> cards = new ArrayList<>();
        cards.add(new CivilizationCard(List.of(ImmediateEffect.ThrowStone, ImmediateEffect.ThrowStone),
                List.of(EndOfGameEffect.SHAMAN)));
        cards.add(new CivilizationCard(List.of(ImmediateEffect.ThrowStone, ImmediateEffect.ThrowStone),
                List.of(EndOfGameEffect.SHAMAN)));
        cards.add(new CivilizationCard(List.of(ImmediateEffect.ThrowStone, ImmediateEffect.ThrowStone),
                List.of(EndOfGameEffect.SHAMAN)));
        cards.add(new CivilizationCard(List.of(ImmediateEffect.ThrowStone, ImmediateEffect.ThrowStone),
                List.of(EndOfGameEffect.SHAMAN)));
        StoneAgeGame game = gameInitiate(players, observers, throwMock, cards);

        throwMock.setToSix(10);
        assertTrue(game.placeFigures(1, Location.FOREST, 5));
        assertTrue(game.placeFigures(2, Location.HUNTING_GROUNDS, 5));

        assertTrue(game.makeAction(1, Location.FOREST, null, null));
        assertTrue(game.makeAction(2, Location.HUNTING_GROUNDS, null, null));

        assertTrue(game.placeFigures(2, Location.HUNTING_GROUNDS, 5));
        assertTrue(game.placeFigures(1, Location.CIVILISATION_CARD4, 1));
        assertTrue(game.placeFigures(1, Location.HUNTING_GROUNDS, 4));

        assertTrue(game.makeAction(2, Location.HUNTING_GROUNDS, null, null));
        assertTrue(game.makeAction(1, Location.HUNTING_GROUNDS, null, null));
        assertTrue(game.makeAction(1, Location.CIVILISATION_CARD4, List.of(Effect.WOOD), null));

        boolean isEnded = false;
        try {
            assertTrue(game.placeFigures(1, Location.HUNTING_GROUNDS, 5));
        } catch (AssertionError e) {
            isEnded = true;
        }
        assertTrue(isEnded);
    }
}