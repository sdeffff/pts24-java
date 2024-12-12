package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.*;
import java.util.Optional;
import java.util.*;

/**
 * The CurrentThrow class represents a player's dice throw in the game.
 * It calculates the result of the dice throw, determines if tools can be used to enhance the throw,
 * and updates the player's resources accordingly.
 */
public class CurrentThrow implements InterfaceToolUse {
    private Effect throwsFor; // The type of effect the player is throwing for (e.g., resources).
    private int throwResult; // Total result of the dice throw.
    private Throw throw_ = new Throw(); // Utility to simulate dice throwing.
    private Player player; // The player performing the throw.
    private int dices; // Number of dices used in the throw.
    private int[] dicesResults; // Array to store results of each dice roll.
    private boolean toolsUsed = false; // Indicates whether tools were used to enhance the throw.

    /**
     * Initiates the throw for a player using the specified effect and number of dices.
     * Prompts the player to use tools if applicable, and updates their resources.
     *
     * @param player The player performing the throw.
     * @param effect The effect for which the dice are thrown.
     * @param dices  The number of dices to be thrown.
     */
    public void initiate(Player player, Effect effect, int dices) {
        this.throwsFor = effect;
        this.player = player;
        this.dices = dices;
        int[] dicesResults = throw_.throw_(dices);
        this.dicesResults = dicesResults;
        throwResult = Arrays.stream(dicesResults).reduce(0, Integer::sum);
        toolsUsed = false;

        if (canUseTools()) {
            System.out.println("You can use tools to get the +1 of " + throwsFor.toString() + ". Do you want to use them? (Yes/No)");
            Scanner scanner = new Scanner(System.in);
            String answer = scanner.nextLine();
            if (answer.equals("Yes")) {
                toolsUsed = useTool(throwResult % throwsFor.points());
            }
        }

        Collection<Effect> effects = new ArrayList<>();
        if (toolsUsed) {
            throwResult += throwResult % throwsFor.points();
        }
        for (int i = 0; i < Math.floorDiv(throwResult, throwsFor.points()); i++) {
            effects.add(throwsFor);
        }
        player.playerBoard().takeResources(effects);
    }

    /**
     * Uses a tool to increase the result of the throw.
     *
     * @param idx The index of the tool to be used.
     * @return True if the tool was successfully used, false otherwise.
     */
    @Override
    public boolean useTool(int idx) {
        Optional<Integer> a = player.playerBoard().useTool(idx);
        if (a.isPresent()) {
            throwResult += a.get();
            toolsUsed = true;
            return true;
        }
        return false;
    }

    /**
     * Checks if tools can be used to enhance the throw result.
     *
     * @return True if tools can be used, false otherwise.
     */
    @Override
    public boolean canUseTools() {
        int goal = throwResult % throwsFor.points();
        if (goal == 0) {
            return false;
        }
        return player.playerBoard().hasSufficientTools(goal);
    }

    /**
     * Finishes the process of using tools. Currently, always returns true.
     *
     * @return True indicating the tools usage process is finished.
     */
    @Override
    public boolean finishUsingTools() {
        return true;
    }

    /**
     * Provides the current state of the throw, including dice results,
     * whether tools were used, and the effects gained by the player.
     *
     * @return A string describing the current state of the throw.
     */
    public String state() {
        StringBuilder state = new StringBuilder();
        state.append("Throw result: ").append(throwResult).append("\n");
        state.append("Threw for: ").append(throwsFor.toString()).append("\n");
        state.append("Number of dices thrown: ").append(dices).append("\n");
        state.append("Dices throw results: ");
        for (int i = 0; i < dicesResults.length; i++) {
            state.append(dicesResults[i]).append(" ");
        }
        state.append("\n");

        if (toolsUsed) {
            state.append("Tools were used.\n");
            state.append("New throw result(after adding tools points): ").append(throwResult).append("\n");
        }
        state.append(throwsFor.toString()).append(" costs ").append(throwsFor.points()).append(" resources, so player got ")
                .append(Math.floorDiv(throwResult, throwsFor.points())).append(" of them.\n");

        return state.toString();
    }
}