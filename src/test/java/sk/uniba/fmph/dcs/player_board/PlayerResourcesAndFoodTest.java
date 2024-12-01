package sk.uniba.fmph.dcs.player_board;

import junit.framework.TestCase;
import sk.uniba.fmph.dcs.stone_age.Effect;

import java.util.Arrays;
import java.util.Collections;

public class PlayerResourcesAndFoodTest extends TestCase {

    private PlayerResourcesAndFood playerResources;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        playerResources = new PlayerResourcesAndFood();
    }

    public void testHasResources() {
        // Player starts with 12 food
        assertTrue(playerResources.hasResources(Collections.nCopies(5, Effect.FOOD)));
        assertFalse(playerResources.hasResources(Collections.nCopies(13, Effect.FOOD)));

        // Add some resources
        playerResources.giveResources(Arrays.asList(Effect.WOOD, Effect.WOOD, Effect.STONE));
        assertTrue(playerResources.hasResources(Arrays.asList(Effect.WOOD, Effect.STONE)));
        assertFalse(playerResources.hasResources(Arrays.asList(Effect.WOOD, Effect.CLAY)));
    }

    public void testTakeResources() {
        // Take resources when player has enough
        boolean result = playerResources.takeResources(Collections.nCopies(5, Effect.FOOD));
        assertTrue(result);
        assert(areStringsEqual("CLAY: 0, GOLD: 0, FOOD: 7, WOOD: 0, STONE: 0", playerResources.state()));


        // Try to take more resources than available
        result = playerResources.takeResources(Collections.nCopies(10, Effect.FOOD));
        assertFalse(result);
        assert(areStringsEqual("CLAY: 0, GOLD: 0, FOOD: 7, WOOD: 0, STONE: 0", playerResources.state()));

    }

    public void testGiveResources() {
        // Give resources to the player
        playerResources.giveResources(Arrays.asList(Effect.WOOD, Effect.CLAY, Effect.GOLD));
        assert(areStringsEqual("CLAY: 1, GOLD: 1, FOOD: 12, WOOD: 1, STONE: 0", playerResources.state()));
    }

    public void testNumberOfResourcesForFinalPoints() {
        // Initially, player has no resources other than food
        assertEquals(0, playerResources.numberOfResourcesForFinalPoints());

        // Add some resources
        playerResources.giveResources(Arrays.asList(Effect.WOOD, Effect.CLAY, Effect.STONE, Effect.GOLD));
        assertEquals(4, playerResources.numberOfResourcesForFinalPoints());
    }

    public void testState() {
        // Test initial state
        String state = playerResources.state();
        assert(areStringsEqual("CLAY: 0, GOLD: 0, FOOD: 12, WOOD: 0, STONE: 0", state));

        // Add resources and test state
        playerResources.giveResources(Arrays.asList(Effect.WOOD, Effect.CLAY));
        state = playerResources.state();
        assert(areStringsEqual("CLAY: 1, GOLD: 0, FOOD: 12, WOOD: 1, STONE: 0", state));
    }

    private static boolean areStringsEqual(String str1, String str2) {
        // Если длины строк разные, они точно не равны
        if (str1.length() != str2.length()) {
            return false;
        }

        // Преобразуем строки в массивы символов
        char[] chars1 = str1.toCharArray();
        char[] chars2 = str2.toCharArray();

        // Сортируем массивы символов
        Arrays.sort(chars1);
        Arrays.sort(chars2);

        // Сравниваем отсортированные массивы
        return Arrays.equals(chars1, chars2);
    }
}