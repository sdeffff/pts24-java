package sk.uniba.fmph.dcs.game_board;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.OptionalInt;
import org.junit.Test;
import sk.uniba.fmph.dcs.stone_age.Effect;

public class VariableBuildingTest {

    @Test
    public void test_calculation_1(){
        ArrayList<Effect> buildingResources = new ArrayList<Effect>();
        buildingResources.add(Effect.WOOD);
        buildingResources.add(Effect.WOOD);
        buildingResources.add(Effect.GOLD);


        VariableBuilding building = new VariableBuilding(3, 2);

        assertEquals(building.build(buildingResources), OptionalInt.of(12));

        ArrayList<Effect> otherResources = new ArrayList<Effect>();
        assertEquals(building.build(otherResources), OptionalInt.empty());
    }

}
