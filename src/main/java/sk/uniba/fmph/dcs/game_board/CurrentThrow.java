package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.InterfaceToolUse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.OptionalInt;

public final class CurrentThrow implements InterfaceToolUse, CurrentThrowInterface {

    private final ThrowInterface throw1;
    private Effect throwsFor;
    private int throwResult;
    private Player player;
    private boolean initiated = false;
    private final Map<Effect, Integer> map;

    private static final int WOOD = 3;
    private static final int FOOD = 2;
    private static final int CLAY = 4;
    private static final int STONE = 5;
    private static final int GOLD = 6;
    public CurrentThrow(final ThrowInterface throw1) {
        this.throw1 = throw1;

        map = new HashMap<>();
        map.put(Effect.WOOD, WOOD);
        map.put(Effect.FOOD, FOOD);
        map.put(Effect.CLAY, CLAY);
        map.put(Effect.STONE, STONE);
        map.put(Effect.GOLD, GOLD);
    }

    public void initiate(final Player player, final Effect effect, final int dices) {
        initiated = true;
        this.player = player;
        this.throwsFor = effect;
        ArrayList<Integer> diceRolls = throw1.throwDice(dices);
        throwResult = 0;
        for (int i : diceRolls) {
            throwResult += i;
        }
    }

    @Override
    public boolean useTool(final int idx) {
        if (!initiated) {
            return false;
        }
        OptionalInt tool = player.playerBoard().useTool(idx);
        if (tool.isEmpty()) {
            return false;
        }

        tool.ifPresent(add -> throwResult += add);
        return true;
    }

    @Override
    public boolean canUseTools() {
        if (!initiated) {
            return false;
        }
        if (!player.playerBoard().hasSufficientTools(1)) {
            finishUsingTools();
            return false;
        }

        return true;
    }

    @Override
    public boolean finishUsingTools() {
        if (!initiated) {
            return false;
        }

        int finalNumberOfResources = throwResult / map.get(throwsFor);
        ArrayList<Effect> result = new ArrayList<>();
        for (int i = 0; i < finalNumberOfResources; i++) {
            result.add(throwsFor);
        }

        player.playerBoard().giveEffect(result);
        initiated = false;

        return true;
    }

    public String state() {
        if (!initiated) {
            return "uninitialized";
        }
        Map<String, String> state = Map.of("Player", player.toString(),
                "throwsFor", throwsFor.toString(),
                "currentThrow", String.valueOf(throwResult));
        return new JSONObject(state).toString();
    }

    public int getThrowResult() {
        return throwResult;
    }

    public boolean isInitiated() {
        return initiated;
    }
}