package sk.uniba.fmph.dcs.game_board;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.*;

public class CivilizationCardPlaceTest {
    private CivilizationCardPlace cardPlace;
    private Player player;
    private PlayerBoardMock playerBoard;
    private List<PlayerOrder> figures;
    private MockEvaluator evaluator;
    private CivilizationCardDeck deck;
    
    private static class MockEvaluator implements EvaluateCivilizationCardImmediateEffect {
        private boolean effectPerformed = false;
        
        @Override
        public boolean performEffect(Player player, Effect choice) {
            effectPerformed = true;
            return true;
        }
        
        public boolean wasEffectPerformed() {
            return effectPerformed;
        }
        
        public void reset() {
            effectPerformed = false;
        }
    }
    
    @Before
    public void setUp() {
        figures = new ArrayList<>();
        playerBoard = new PlayerBoardMock();
        player = new Player(new PlayerOrder(0, 2), playerBoard);
        evaluator = new MockEvaluator();
        
        // Create a test deck with known cards
        List<CivilizationCard> testCards = Arrays.asList(
            new CivilizationCard(List.of(ImmediateEffect.FOOD), List.of(EndOfGameEffect.Farmer)),
            new CivilizationCard(List.of(ImmediateEffect.TOOL), List.of(EndOfGameEffect.Builder))
        );
        deck = CivilizationCardDeckFactory.createTestDeck(testCards);
        
        cardPlace = new CivilizationCardPlace(2, figures, deck, evaluator);
        cardPlace.newTurn(); // Initialize first card
    }
    
    @Test
    public void testPlaceFiguresSuccess() {
        assertTrue(cardPlace.placeFigures(player, 1));
        assertTrue(figures.contains(player.playerOrder()));
        assertEquals(4, playerBoard.getFigureCount()); // Started with 5, used 1
    }
    
    @Test
    public void testPlaceFiguresNoFigures() {
        // Take all figures first
        playerBoard.takeFigures(5);
        assertFalse(cardPlace.placeFigures(player, 1));
        assertTrue(figures.isEmpty());
    }
    
    @Test
    public void testMakeActionSuccess() {
        figures.add(player.playerOrder());
        
        Collection<Effect> input = Arrays.asList(Effect.WOOD, Effect.WOOD);
        Collection<Effect> output = new ArrayList<>();
        
        assertEquals(ActionResult.ACTION_DONE,
            cardPlace.makeAction(player, input, output));
            
        assertTrue(evaluator.wasEffectPerformed());
        assertEquals(1, playerBoard.getCards().size());
        assertEquals(ImmediateEffect.FOOD, 
            playerBoard.getCards().get(0).getImmediateEffectType().get(0));
    }
    
    @Test
    public void testMakeActionInsufficientResources() {
        figures.add(player.playerOrder());
        
        Collection<Effect> input = Arrays.asList(Effect.WOOD); // Only 1 resource when 2 required
        Collection<Effect> output = new ArrayList<>();
        
        assertEquals(ActionResult.FAILURE,
            cardPlace.makeAction(player, input, output));
            
        assertTrue(playerBoard.getCards().isEmpty());
    }
    
    @Test
    public void testNewTurnResetsStateAndAdvancesCards() {
        figures.add(player.playerOrder());
        cardPlace.newTurn();
        assertTrue(figures.isEmpty());
        
        // Verify we can get the second card after newTurn
        figures.add(player.playerOrder());
        Collection<Effect> input = Arrays.asList(Effect.WOOD, Effect.WOOD);
        Collection<Effect> output = new ArrayList<>();
        
        assertEquals(ActionResult.ACTION_DONE,
            cardPlace.makeAction(player, input, output));
            
        assertEquals(1, playerBoard.getCards().size());
        assertEquals(ImmediateEffect.TOOL,
            playerBoard.getCards().get(0).getImmediateEffectType().get(0));
    }
    @Test
    public void testSociableIntegrationWithDeck() {
        // Create a deck with known cards
        List<CivilizationCard> cards = Arrays.asList(
            new CivilizationCard(List.of(ImmediateEffect.FOOD), List.of(EndOfGameEffect.FARMER)),
            new CivilizationCard(List.of(ImmediateEffect.TOOL), List.of(EndOfGameEffect.BUILDER))
        );
        CivilizationCardDeck deck = CivilizationCardDeckFactory.createTestDeck(cards);
        
        // Create card place with this deck
        CivilizationCardPlace place = new CivilizationCardPlace(2, new ArrayList<>(), deck, evaluator);
        place.newTurn(); // Initialize first card
        
        // Place figures and make action
        place.placeFigures(player, 1);
        Collection<Effect> input = Arrays.asList(Effect.WOOD, Effect.WOOD);
        Collection<Effect> output = new ArrayList<>();
        
        assertEquals(ActionResult.ACTION_DONE, place.makeAction(player, input, output));
        
        // Verify interaction with deck
        assertFalse(deck.isEmpty());
        place.newTurn();
        
        // Verify next card is available
        assertTrue(place.state().contains("TOOL"));
    }

    @Test
    public void testWithReusablePlayerBoardMock() {
        PlayerBoardMock mockBoard = new PlayerBoardMock();
        Player testPlayer = new Player(new PlayerOrder(0, 2), mockBoard);
        
        // Test figure placement
        assertTrue(mockBoard.hasFigures(1));
        assertTrue(cardPlace.placeFigures(testPlayer, 1));
        
        // Test resource consumption
        Collection<Effect> input = Arrays.asList(Effect.WOOD, Effect.WOOD);
        assertTrue(mockBoard.takeResources(input));
        
        // Test card acquisition
        cardPlace.makeAction(testPlayer, input, new ArrayList<>());
        assertEquals(1, mockBoard.getCards().size());
    }
}
