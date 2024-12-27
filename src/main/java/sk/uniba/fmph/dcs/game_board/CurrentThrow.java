package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.InterfaceToolUse;

import java.util.*;

public class CurrentThrow implements InterfaceToolUse {

    private Effect effectType;
    private int resultValue;
    private Player currentPlayer;
    private int totalSum, divisor;
    private boolean isUsed;

    private static class ThrowDices {
        public static int[] rollDice(int count) {
            Random random = new Random();
            return random.ints(count, 1, 7).toArray(); // Simulate rolling `count` dice with values 1-6.
        }
    }

    public CurrentThrow() {
        isUsed = false;
        resultValue = 0;
        totalSum = 0;
        divisor = 1;
    }

    public void initiate(Player player, Effect effect, int diceCount) {
        isUsed = false;
        this.effectType = effect;
        this.currentPlayer = player;

        if (diceCount <= 0) {
            throw new IllegalArgumentException("Number of dice must be positive.");
        }

        int[] diceResults = ThrowDices.rollDice(diceCount);
        this.totalSum = Arrays.stream(diceResults).sum();

        // Determine divisor based on the resource type.
        this.divisor = switch (effect) {
            case FOOD -> 2;
            case WOOD -> 3;
            case CLAY -> 4;
            case STONE -> 5;
            case GOLD -> 6;
            default -> 1;
        };

        this.resultValue = this.totalSum / this.divisor;
    }

    public int getResultValue() {
        return resultValue;
    }

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

        this.resultValue = this.totalSum / this.divisor;
        return true;
    }

    @Override
    public boolean canUseTools() {
        return effectType != null && effectType.isResourceOrFood();
    }

    @Override
    public boolean finishUsingTools() {
        if (isUsed || effectType == null) {
            return false;
        }

        Effect[] effectArray = new Effect[resultValue];
        Arrays.fill(effectArray, effectType);

        currentPlayer.playerBoard().giveEffect(List.of(effectArray));
        isUsed = true;
        return true;
    }
}