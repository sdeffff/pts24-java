package sk.uniba.fmph.dcs.player_board;

import sk.uniba.fmph.dcs.stone_age.Effect;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * The {@code PlayerResourcesAndFood} class represents a player's resources and food in the Stone Age game.
 * It manages the player's inventory by allowing the addition and removal of resources,
 * checking resource availability, and calculating resources for final scoring.
 */
public class PlayerResourcesAndFood {
    /**
     * A map that keeps track of the quantity of each type of resource the player has.
     * The key is an {@link Effect} representing the resource type, and the value is the quantity.
     */
    private final Map<Effect, Integer> resources;

    /**
     * Constructs a new {@code PlayerResourcesAndFood} instance and initializes the player's resources.
     * The player starts with a default amount of food and zero of other resources.
     */
    public PlayerResourcesAndFood() {
        this.resources = new HashMap<>();
        initializeResources();
    }

    /**
     * Initializes the player's starting resources.
     * By default, the player starts with 12 food tokens and zero of all other resources.
     */
    private void initializeResources() {
        resources.put(Effect.FOOD, 12); // Players start with 12 food tokens
        resources.put(Effect.WOOD, 0);
        resources.put(Effect.CLAY, 0);
        resources.put(Effect.STONE, 0);
        resources.put(Effect.GOLD, 0);
    }

    /**
     * Checks if the player has at least the required amount of each resource specified in the collection.
     *
     * @param requiredResources a collection of {@link Effect} representing the required resources.
     * @return {@code true} if the player has enough of each required resource; {@code false} otherwise.
     */
    public boolean hasResources(Collection<Effect> requiredResources) {
        Map<Effect, Integer> requiredCounts = new HashMap<>();
        // Count the number of each required resource
        for (Effect resource : requiredResources) {
            requiredCounts.put(resource, requiredCounts.getOrDefault(resource, 0) + 1);
        }
        // Check if the player has enough of each required resource
        for (Map.Entry<Effect, Integer> entry : requiredCounts.entrySet()) {
            Effect resource = entry.getKey();
            int requiredAmount = entry.getValue();
            int availableAmount = resources.getOrDefault(resource, 0);
            if (availableAmount < requiredAmount) {
                return false;
            }
        }
        return true;
    }

    /**
     * Removes the specified resources from the player's inventory if the player has enough.
     * If the player lacks any of the required resources, no resources are removed.
     *
     * @param resourcesToTake a collection of {@link Effect} representing the resources to remove.
     * @return {@code true} if the resources were successfully removed; {@code false} if the player did not have enough resources.
     */
    public boolean takeResources(Collection<Effect> resourcesToTake) {
        if (!hasResources(resourcesToTake)) {
            return false;
        }
        // Remove each resource from the player's inventory
        for (Effect resource : resourcesToTake) {
            resources.put(resource, resources.get(resource) - 1);
        }
        return true;
    }

    /**
     * Adds the specified resources to the player's inventory.
     *
     * @param resourcesToAdd a collection of {@link Effect} representing the resources to add.
     */
    public void giveResources(Collection<Effect> resourcesToAdd) {
        for (Effect resource : resourcesToAdd) {
            resources.put(resource, resources.getOrDefault(resource, 0) + 1);
        }
    }

    /**
     * Calculates the total number of non-food resources the player has for final scoring.
     * In the Stone Age game, resources other than food contribute to the player's score at the end of the game.
     *
     * @return the total number of non-food resources.
     */
    public int numberOfResourcesForFinalPoints() {
        int total = 0;
        for (Map.Entry<Effect, Integer> entry : resources.entrySet()) {
            Effect resource = entry.getKey();
            if (resource != Effect.FOOD) {
                total += entry.getValue();
            }
        }
        return total;
    }

    /**
     * Returns a string representation of the player's current resources.
     * The string includes each resource type and the quantity the player has.
     *
     * @return a string representing the player's resources, formatted as "RESOURCE: quantity, ...".
     */
    public String state() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Effect, Integer> entry : resources.entrySet()) {
            sb.append(entry.getKey().toString())
                    .append(": ")
                    .append(entry.getValue())
                    .append(", ");
        }
        // Remove trailing comma and space if necessary
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 2); // Remove trailing ", "
        }
        return sb.toString();
    }
}