package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;

import java.util.*;

public class CivilizationCardDeck {

    private final Stack<CivilizationCard> cardStack;

    public CivilizationCardDeck(Stack<CivilizationCard> cardStack) {
        this.cardStack = cardStack;
    }

    //Gets card that are at the top of the deck
    public Optional<CivilizationCard> getTop() {
        if (cardStack.isEmpty()) {
            return Optional.empty();
        }
        else {
            return Optional.of(cardStack.pop());
        }
    }

    public String state() {
        Map<String, String> stateMap = new HashMap<>();
        stateMap.put("cardDeck", cardStack.toString());
        return new JSONObject(stateMap).toString();
    }
}
