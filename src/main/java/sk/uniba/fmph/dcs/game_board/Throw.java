package sk.uniba.fmph.dcs.game_board;

import java.util.Random;

public class Throw implements ThrowInterface {
    
    public Throw() {}


    @Override
    public int[] throw_(int dices) {
        int[] result = new int[dices];
        Random rand = new Random();
        for (int i = 0; i < dices; i++) {
            result[i] = rand.nextInt(6)+1;
        }
        return result;
    }
}
