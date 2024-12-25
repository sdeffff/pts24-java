package sk.uniba.fmph.dcs.game_board;

import java.util.*;

public class CivilizationCardDeck {
    private final Stack<CivilizationCard> stack;
    
    public CivilizationCardDeck(Stack<CivilizationCard> stack) {
        this.stack = stack;
    }

    public Optional<CivilizationCard> getTop() {
        if (!stack.isEmpty()) {
            return Optional.of(stack.pop());
        }
        return Optional.empty();
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public String state() {
        Stack<CivilizationCard> tempStack = new Stack<>();
        StringBuilder result = new StringBuilder("[");
        
        // Copy cards to temp stack to preserve order
        while (!stack.isEmpty()) {
            CivilizationCard card = stack.pop();
            tempStack.push(card);
            result.append(card.toString());
            if (!stack.isEmpty()) {
                result.append(", ");
            }
        }
        
        // Restore original stack
        while (!tempStack.isEmpty()) {
            stack.push(tempStack.pop());
        }
        
        result.append("]");
        return result.toString();
    }
}
