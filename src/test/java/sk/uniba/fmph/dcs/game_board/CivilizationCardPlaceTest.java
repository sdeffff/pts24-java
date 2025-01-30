package sk.uniba.fmph.dcs.game_board;

import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import static org.junit.Assert.*;

public class CivilizationCardPlaceTest {

    EvaluateCivilizationCardImmediateEffect e = new EvaluateCivilizationCardImmediateEffect() {
        @Override
        public ActionResult performEffect(Player player, Effect choice) {
            return null;
        }

        @Override
        public boolean tryToPerformEffect(Player player, Effect choice) {
            if (choice == null) {
                return true;
            }
            return !choice.equals(Effect.WOOD);
        }
    };

    @Test
    public void testCalculation() {
        ArrayList<CivilizationCard> cards = new ArrayList<>();
        ArrayList<ImmediateEffect> effects1 = new ArrayList<>();
        effects1.add(ImmediateEffect.ArbitraryResource);
        effects1.add(ImmediateEffect.ThrowStone);
        ArrayList<ImmediateEffect> effects2 = new ArrayList<>();
        effects2.add(ImmediateEffect.POINT);
        Map<ImmediateEffect, EvaluateCivilizationCardImmediateEffect> evaluate = new HashMap<>();
        evaluate.put(ImmediateEffect.ArbitraryResource, e);
        evaluate.put(ImmediateEffect.ThrowStone, e);
        evaluate.put(ImmediateEffect.POINT, e);

        CivilizationCard card1 = new CivilizationCard(effects1, null);
        CivilizationCard card2 = new CivilizationCard(effects2, null);
        CivilizationCard card3 = new CivilizationCard(effects2, null);
        Optional<CivilizationCard> optionalCard1 = Optional.of(card2);
        Optional<CivilizationCard> optionalCard2 = Optional.of(card3);
        Optional<CivilizationCard> optionalCard3 = Optional.of(card1);
        cards.add(card1);
        cards.add(card2);
        CivilizationCardDeck deck = new CivilizationCardDeck();
        CivilizationCardPlace place1 = new CivilizationCardPlace(deck, optionalCard1,null, 1, evaluate);
        CivilizationCardPlace place2 = new CivilizationCardPlace(deck, optionalCard2,place1, 2, evaluate);
        CivilizationCardPlace place3 = new CivilizationCardPlace(deck, optionalCard3,place2, 3, evaluate);

        PlayerBoardMock board1 = new PlayerBoardMock();
        PlayerBoardMock board2 = new PlayerBoardMock();
        PlayerBoardMock board3 = new PlayerBoardMock();
        ArrayList<Effect> resources1 = new ArrayList<>();
        ArrayList<Effect> resources2 = new ArrayList<>();
        ArrayList<Effect> resources3 = new ArrayList<>();
        resources1.add(Effect.WOOD);
        resources1.add(Effect.WOOD);
        resources2.add(Effect.WOOD);
        resources2.add(Effect.WOOD);
        resources3.add(Effect.WOOD);
        resources3.add(Effect.WOOD);
        resources3.add(Effect.WOOD);
        board1.giveEffect(resources1);
        board2.giveEffect(resources2);
        board3.giveEffect(resources3);

        PlayerOrder playerOrder1 = new PlayerOrder(0,3);
        PlayerOrder playerOrder2 = new PlayerOrder(1,3);
        PlayerOrder playerOrder3 = new PlayerOrder(2,3);

        Player player1 = new Player(playerOrder1, board1);
        Player player2 = new Player(playerOrder2, board2);
        Player player3 = new Player(playerOrder3, board3);

        player1.playerBoard().giveFigures(5);
        player2.playerBoard().giveFigures(5);

        assertEquals(place1.tryToPlaceFigures(player1, 2), HasAction.NO_ACTION_POSSIBLE);
        assertEquals(place1.tryToPlaceFigures(player3, 1), HasAction.NO_ACTION_POSSIBLE);
        assertEquals(place1.tryToPlaceFigures(player1, 1), HasAction.WAITING_FOR_PLAYER_ACTION);

        player3.playerBoard().giveFigures(5);

        assertFalse(place1.placeFigures(player1, 2));
        assertTrue(place1.placeFigures(player1, 1));
        assertEquals(place1.tryToPlaceFigures(player2, 1), HasAction.NO_ACTION_POSSIBLE);
        assertEquals(place1.tryToPlaceFigures(player1, 1), HasAction.NO_ACTION_POSSIBLE);
        assertTrue(place2.placeFigures(player2, 1));
        assertTrue(place3.placeFigures(player3, 1));

        ArrayList<Effect> inputResources = new ArrayList<>();
        ArrayList<Effect> outputResources = new ArrayList<>();
        outputResources.add(Effect.WOOD);

        //not enough inputResources
        assertEquals(place1.makeAction(player1, inputResources, outputResources), ActionResult.FAILURE);
        inputResources.add(Effect.WOOD);
        //player2 tries to make action in place1
        assertEquals(place1.makeAction(player2, inputResources, outputResources), ActionResult.FAILURE);
        assertEquals(place1.makeAction(player1, inputResources, outputResources), ActionResult.ACTION_DONE);

        inputResources.add(Effect.BUILDING);
        //wrong inputResource
        assertEquals(place2.makeAction(player2, inputResources, outputResources), ActionResult.FAILURE);
        inputResources.remove(Effect.BUILDING);
        assertTrue(place2.skipAction(player2));

        inputResources.add(Effect.WOOD);
        inputResources.add(Effect.WOOD);

        //tryToPerformEffect fails because of wrong choice
        assertEquals(place3.makeAction(player3, inputResources, outputResources), ActionResult.FAILURE);
        ArrayList<Effect> resource = new ArrayList<>();
        resource.add(Effect.WOOD);
        board3.takeResources(resource);
        outputResources.remove(Effect.WOOD);
        outputResources.add(Effect.CLAY);
        //player does not have input resources
        assertEquals(place3.makeAction(player3, inputResources, outputResources), ActionResult.FAILURE);
        resource.add(Effect.WOOD);
        resource.add(Effect.WOOD);
        board3.giveEffect(resource);
        assertEquals(place3.makeAction(player3, inputResources, outputResources), ActionResult.ACTION_DONE);

        assertFalse(place2.newTurn());
        assertFalse(place1.newTurn());
        assertFalse(place3.newTurn());

        assertEquals(place1.getOptionalCard(), Optional.of(card1));
        assertEquals(place2.getOptionalCard(), Optional.of(card2));
        assertEquals(place3.getOptionalCard(), Optional.of(card3));

        assertTrue(place1.placeFigures(player1, 1));
        inputResources = new ArrayList<>();
        inputResources.add(Effect.WOOD);
        assertEquals(place1.makeAction(player1, inputResources, outputResources), ActionResult.ACTION_DONE);
        assertTrue(place1.newTurn());
    }
}