package sk.uniba.fmph.dcs.game_board;

import static org.junit.Assert.*;
import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.*;

import java.util.*;

public class CivilizationCardDeckTest {
    
    @Test
    public void testDeckInitializationWithoutShuffle() {
        // Create specific ordered cards
        List<CivilizationCard> orderedCards = Arrays.asList(
            new CivilizationCard(List.of(ImmediateEffect.FOOD), List.of(EndOfGameEffect.FARMER)),
            new CivilizationCard(List.of(ImmediateEffect.TOOL), List.of(EndOfGameEffect.BUILDER))
        );
        
        CivilizationCardDeck deck = CivilizationCardDeckFactory.createTestDeck(orderedCards);
        
        // Verify cards come out in the specified order
        Optional<CivilizationCard> firstCard = deck.getTop();
        assertTrue(firstCard.isPresent());
        assertEquals(ImmediateEffect.FOOD, firstCard.get().getImmediateEffectType().get(0));
        
        Optional<CivilizationCard> secondCard = deck.getTop();
        assertTrue(secondCard.isPresent());
        assertEquals(ImmediateEffect.TOOL, secondCard.get().getImmediateEffectType().get(0));
    }
    
    @Test
    public void testFactoryMethodsProduceValidDecks() {
        // Test production deck
        CivilizationCardDeck productionDeck = CivilizationCardDeckFactory.createProductionDeck();
        assertFalse(productionDeck.isEmpty());
        
        // Test test deck
        List<CivilizationCard> testCards = Arrays.asList(
            new CivilizationCard(List.of(ImmediateEffect.FOOD), List.of(EndOfGameEffect.FARMER))
        );
        CivilizationCardDeck testDeck = CivilizationCardDeckFactory.createTestDeck(testCards);
        assertFalse(testDeck.isEmpty());
    }
    
    @Test
    public void testEmptyDeckBehavior() {
        CivilizationCardDeck deck = CivilizationCardDeckFactory.createTestDeck(new ArrayList<>());
        assertTrue(deck.isEmpty());
        assertEquals(Optional.empty(), deck.getTop());
    }
    
    @Test
    public void testDeckStateRepresentation() {
        List<CivilizationCard> cards = Arrays.asList(
            new CivilizationCard(List.of(ImmediateEffect.FOOD), List.of(EndOfGameEffect.FARMER))
        );
        CivilizationCardDeck deck = CivilizationCardDeckFactory.createTestDeck(cards);
        
        String state = deck.state();
        assertTrue(state.contains("FOOD"));
        assertTrue(state.contains("FARMER"));
    }
}
