package sk.uniba.fmph.dcs.player_board;

import sk.uniba.fmph.dcs.stone_age.TribeFedStatus;

public final class PlayerBoardFactory {
    private PlayerBoardFactory() {
    }

    public static PlayerBoard createPlayerBoard() {
        return new PlayerBoard();
    }

    public static PlayerBoard createPlayerBoard(
            final PlayerResourcesAndFood playerResourcesAndFood,
            final PlayerCivilizationCards playerCivilisationCards,
            final PlayerFigures playerFigures,
            final TribeFedStatus tribeFedStatus,
            final PlayerTools playerTools
    ) {
        return new PlayerBoard(
                playerResourcesAndFood,
                playerCivilisationCards,
                playerFigures,
                tribeFedStatus,
                playerTools
        );
    }
}