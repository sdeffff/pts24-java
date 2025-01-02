package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.InterfaceToolUse;

import java.util.*;

public class CurrentThrow implements InterfaceToolUse {

    private Effect effectType;
    private int res;
    private Player currentPlayer;
    private int totalSum, divisor;
    private boolean isEffectUsed;

    private static class ThrowDices {
        public static int[] rollDice(int count) {
            Random random = new Random();
            return random.ints(count, 1, 7).toArray(); // Simulate rolling `count` dice with values 1-6.
        }
    }

    public CurrentThrow() {
        isEffectUsed = false;
        res = 0;
        totalSum = 0;
        divisor = 1;
    }

    public void initiate(Player player, Effect effect, int diceCount) {
        isEffectUsed = false;
        this.effectType = effect;
        this.currentPlayer = player;

        if (diceCount <= 0) {
            throw new IllegalArgumentException("Number of dice must be positive.");
        }

        int[] diceResults = ThrowDices.rollDice(diceCount);
        this.totalSum = Arrays.stream(diceResults).sum();

        this.divisor = switch (effect) {
            case FOOD -> 2;
            case WOOD -> 3;
            case CLAY -> 4;
            case STONE -> 5;
            case GOLD -> 6;
            default -> 1;
        };

        this.res = this.totalSum / this.divisor;
    }

    public int getResultValue() {
        return res;
    }

    /**
        @param toolIndex The index of the tool being used.
        @return True if the tool was successfully used, false otherwise.
    */
    @Override
    public boolean useTool(int toolIndex) {
        if (!canUseTools()) {
            return false;
        }

        Optional<Optional<Integer>> optionalToolValue = currentPlayer.playerBoard().useTool(toolIndex);
        if (optionalToolValue.isEmpty()) {
            return false;
        }

        optionalToolValue.flatMap(inner -> inner).ifPresentOrElse(
                value -> this.totalSum += value,
                () -> { return; }
        );

        this.res = this.totalSum / this.divisor;
        return true;
    }

    @Override
    public boolean canUseTools() {
        return effectType != null && effectType.isResourceOrFood();
    }

    @Override
    public boolean finishUsingTools() {
        if (isEffectUsed || effectType == null) {
            return false;
        }

        Effect[] effects = new Effect[res];
        Arrays.fill(effects, effectType);

        currentPlayer.playerBoard().giveEffect(List.of(effects));
        isEffectUsed = true;
        return true;
    }
}