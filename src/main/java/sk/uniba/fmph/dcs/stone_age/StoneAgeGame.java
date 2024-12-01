package sk.uniba.fmph.dcs.stone_age;

import java.util.HashMap;
import java.util.Map;

public class StoneAgeGame {
    Map<PlayerOrder, Integer> players;

    public StoneAgeGame(int amount) {
        players = new HashMap<>();
        for (int i = 1; i <= amount; i++) {
            PlayerOrder po = new PlayerOrder(i, amount);
            players.put(po, i);
        }
    }
}


