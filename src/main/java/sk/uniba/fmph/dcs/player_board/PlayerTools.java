package sk.uniba.fmph.dcs.player_board;

import sk.uniba.fmph.dcs.stone_age.InterfaceGetState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerTools implements InterfaceGetState {

    private final int[] tools = new int[3];
    private final boolean[] usedTools = new boolean[3];
    private int totalToolsCount;
    private int roundToolsCount;
    private final List<Integer> additionalTools = new ArrayList<>();

    public void newTurn(){
        Arrays.fill(usedTools, false);
        roundToolsCount = totalToolsCount;
    }

    public int getTools() {
        return this.totalToolsCount;
    }

    public void addTool(){
        if(totalToolsCount < 12) {
            totalToolsCount++;
            int position = totalToolsCount % 3;
            int value = 1 + totalToolsCount / 3;
            tools[position] = value;
        }
    }

    public void addSingleUseTool(int strength){
        additionalTools.add(strength);
        totalToolsCount += strength;
        roundToolsCount += strength;
    }

    public int useTool(int index){
        if(index > 2){
            int additionalIndex = index % 3;
            int additionalToolValue = additionalTools.get(additionalIndex);
            totalToolsCount -= additionalToolValue;
            roundToolsCount -= additionalToolValue;
            additionalTools.remove(additionalIndex);
            return additionalToolValue;
        }else {
            roundToolsCount = roundToolsCount - tools[index];
            usedTools[index] = true;
            return tools[index];
        }
    }

    public boolean hasSufficientTools(int goal){
        return goal <= roundToolsCount;
    }


    @Override
    public String state() {
        StringBuilder toReturn = new StringBuilder("Player have: ");
        for (int i = 0; i < 6; i++) {
            if (!usedTools[i] && i != 2) {
                toReturn.append(tools[i]).append(", ");
            } else if (!usedTools[i]) {
                toReturn.append(tools[i]);
            }
        }
        toReturn.append(" to use.\n");
        if(!additionalTools.isEmpty()){
            toReturn.append("Single use tools cards: ");
            for(int x: additionalTools){
                toReturn.append(x).append(" ");
            }
            toReturn.append(".\n");
        }
        return toReturn.toString();
    }

}
