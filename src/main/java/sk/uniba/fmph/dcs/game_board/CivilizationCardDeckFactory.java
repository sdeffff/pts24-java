package sk.uniba.fmph.dcs.game_board;

import java.util.*;

public class CivilizationCardDeckFactory {
    public static CivilizationCardDeck createProductionDeck() {
        Stack<CivilizationCard> stack = new Stack<>();
        // Add all production cards
        List<CivilizationCard> cards = new ArrayList<>();
        // TODO: Add actual production cards here
        
        // Shuffle and create stack
        Collections.shuffle(cards);
        stack.addAll(cards);
        return new CivilizationCardDeck(stack);
    }

    public static CivilizationCardDeck createTestDeck(List<CivilizationCard> orderedCards) {
        Stack<CivilizationCard> stack = new Stack<>();
        // Add cards in reverse order so they come out in the specified order
        for (int i = orderedCards.size() - 1; i >= 0; i--) {
            stack.push(orderedCards.get(i));
        }
        return new CivilizationCardDeck(stack);
    }
}
