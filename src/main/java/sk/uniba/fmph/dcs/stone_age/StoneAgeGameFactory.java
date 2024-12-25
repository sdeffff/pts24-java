package sk.uniba.fmph.dcs.stone_age;

import sk.uniba.fmph.dcs.game_board.*;
import java.util.*;

public class StoneAgeGameFactory {
    public static StoneAgeGame createGame(
            List<Integer> playerIds,
            ThrowInterface throwImpl,
            List<CivilizationCard> civilizationCards,
            List<Building> buildings,
            InterfaceStoneAgeObserver observer) {
        
        // Create game with controlled random elements
        GameBoard gameBoard = GameBoardFactory.createGameBoard(
            createPlayers(playerIds),
            throwImpl,
            civilizationCards,
            buildings
        );
        
        return new StoneAgeGame(gameBoard, observer);
    }

    public static StoneAgeGame createDefaultGame(
            List<Integer> playerIds,
            InterfaceStoneAgeObserver observer) {
        return createGame(
            playerIds,
            new Throw(),
            getDefaultCivilizationCards(),
            getDefaultBuildings(),
            observer
        );
    }

    private static List<Player> createPlayers(List<Integer> playerIds) {
        List<Player> players = new ArrayList<>();
        for (Integer id : playerIds) {
            players.add(new Player(
                new PlayerOrder(id, playerIds.size()),
                new PlayerBoard()
            ));
        }
        return players;
    }

    private static List<CivilizationCard> getDefaultCivilizationCards() {
        return GameBoardFactory.getDefaultCivilizationCards();
    }

    private static List<Building> getDefaultBuildings() {
        return GameBoardFactory.getDefaultBuildings();
    }
}
