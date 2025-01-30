package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.InterfacePlayerBoardGameBoard;
import sk.uniba.fmph.dcs.stone_age.PlayerOrder;

public record Player(PlayerOrder playerOrder, InterfacePlayerBoardGameBoard playerBoard) {

}