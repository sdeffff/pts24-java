package sk.uniba.fmph.dcs.game_board;

import java.util.ArrayList;

public interface ThrowInterface {
    ArrayList<Integer> throwDice(int dices);
    void setRolls(ArrayList<Integer> rolls);
}