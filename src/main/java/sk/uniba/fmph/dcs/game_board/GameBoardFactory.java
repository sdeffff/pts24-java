package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.*;
import java.util.*;

public class GameBoardFactory {
    public static GameBoard createGameBoard(
            List<Player> players,
            ThrowInterface throwImpl,
            List<CivilizationCard> civilizationCards,
            List<Building> buildings) {
        
        CurrentThrowInterface currentThrow = new CurrentThrow(throwImpl);
        
        // Create civilization card deck using factory
        CivilizationCardDeck cardDeck = CivilizationCardDeckFactory.createProductionDeck(civilizationCards);
        List<Building> orderedBuildings = new ArrayList<>(buildings);
        
        return new GameBoard(
            players,
            orderedBuildings.toArray(new Building[0]),
            orderedCards.toArray(new CivilizationCard[0]),
            currentThrow
        );
    }

    public static GameBoard createTestGameBoard(
            List<Player> players,
            ThrowInterface throwImpl,
            List<CivilizationCard> orderedCards,
            List<Building> orderedBuildings,
            boolean skipShuffle) {
        
        CurrentThrowInterface currentThrow = new CurrentThrow(throwImpl);
        
        // Use cards and buildings in specified order for testing
        return new GameBoard(
            players,
            orderedBuildings.toArray(new Building[0]),
            orderedCards.toArray(new CivilizationCard[0]),
            currentThrow
        );
    }

    public static GameBoard createDefaultGameBoard(List<Player> players) {
        // Create default implementation with standard game rules
        return createGameBoard(
            players,
            new Throw(),
            getDefaultCivilizationCards(),
            getDefaultBuildings()
        );
    }

    private static List<CivilizationCard> getDefaultCivilizationCards() {
        // Default civilization cards as per game rules
        List<CivilizationCard> cards = new ArrayList<>();
        // Add standard game cards...
        return cards;
    }

    private static List<Building> getDefaultBuildings() {
        // Default buildings as per game rules
        List<Building> buildings = new ArrayList<>();
        // Add standard game buildings...
        return buildings;
    }
}
