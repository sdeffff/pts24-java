package sk.uniba.fmph.dcs.game_board;

import java.util.*;

public class CivilizationCardDeckFactory {
    
    /**
     * Creates a production deck with shuffled cards
     */
    public static CivilizationCardDeck createProductionDeck(List<CivilizationCard> cards) {
        Stack<CivilizationCard> stack = new Stack<>();
        List<CivilizationCard> shuffledCards = new ArrayList<>(cards);
        Collections.shuffle(shuffledCards);
        stack.addAll(shuffledCards);
        return new CivilizationCardDeck(stack);
    }
    
    /**
     * Creates a test deck with cards in specified order
     */
    public static CivilizationCardDeck createTestDeck(List<CivilizationCard> orderedCards) {
        Stack<CivilizationCard> stack = new Stack<>();
        // Add in reverse order so they come out in the specified order
        for (int i = orderedCards.size() - 1; i >= 0; i--) {
            stack.push(orderedCards.get(i));
        }
        return new CivilizationCardDeck(stack);
    }
    
    /**
     * Creates an empty deck for testing
     */
    public static CivilizationCardDeck createEmptyDeck() {
        return new CivilizationCardDeck(new Stack<>());
    }
}
