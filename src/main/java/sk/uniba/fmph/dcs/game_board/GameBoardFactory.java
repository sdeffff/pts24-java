package sk.uniba.fmph.dcs.game_board;

import java.util.ArrayList;

public final class GameBoardFactory {

    private GameBoardFactory() {
    }

    public static GameBoard createGameBoard(final ArrayList<Player> players, final ThrowInterface throw1,
                                            final CurrentThrow currentThrow, final ArrayList<Building> buildings) {
        CivilizationCardDeck deck = new CivilizationCardDeck();
        return new GameBoard(deck, buildings, players, throw1, currentThrow);
    }

}