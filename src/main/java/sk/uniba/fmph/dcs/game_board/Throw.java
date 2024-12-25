package sk.uniba.fmph.dcs.game_board;

import java.util.Random;

public class Throw implements ThrowInterface {

    private static final Throw INSTANCE = new Throw();
    
    private final Random rand = new Random();

    private Throw() {}

    public static Throw getInstance() {
        return INSTANCE;
    }

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
