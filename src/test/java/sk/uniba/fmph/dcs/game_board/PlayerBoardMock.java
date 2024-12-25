package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.*;
import java.util.*;

public class PlayerBoardMock implements InterfacePlayerBoardMock {
    private int figureCount = 5;
    private List<CivilizationCard> cards = new ArrayList<>();
    private List<Effect> effects = new ArrayList<>();
    
    @Override
    public boolean hasFigures(int count) {
        return count <= figureCount;
    }
    
    @Override
    public boolean takeFigures(int count) {
        if (count <= figureCount) {
            figureCount -= count;
            return true;
        }
        return false;
    }
    
    @Override
    public boolean takeResources(Collection<Effect> resources) {
        return true; // Always allow taking resources in tests
    }
    
    @Override
    public void giveCard(CivilizationCard card) {
        if (card != null) {
            cards.add(card);
        }
    }
    
    @Override
    public void giveEffect(Collection<Effect> newEffects) {
        effects.addAll(newEffects);
    }
    
    // Test helper methods
    public int getFigureCount() {
        return figureCount;
    }
    
    public List<CivilizationCard> getCards() {
        return Collections.unmodifiableList(cards);
    }
    
    public List<Effect> getEffects() {
        return Collections.unmodifiableList(effects);
    }
    
    public void reset() {
        figureCount = 5;
        cards.clear();
        effects.clear();
    }
}
