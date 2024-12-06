package sk.uniba.fmph.dcs.game_board;

import java.util.*;

public class CivilizationCardDeck {
    private Stack<CivilizationCard> stack;
    public CivilizationCardDeck(Stack<CivilizationCard> stack){
        this.stack = stack;
    }

    public Optional<CivilizationCard> getTop(){
        if(!stack.isEmpty()){
            Optional<CivilizationCard> result = Optional.of(stack.pop());
            return result;
        }
        Optional<CivilizationCard> empty = Optional.empty();
        return empty;
    }

    public String state() {
        List<CivilizationCard> result = new ArrayList<>();
        while (!stack.isEmpty()) {
            result.add(stack.pop());
        }
        Collections.reverse(result);
        return result.toString();

    }
}