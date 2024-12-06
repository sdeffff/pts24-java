package sk.uniba.fmph.dcs.player_board;

import sk.uniba.fmph.dcs.stone_age.InterfaceGetState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerTools implements InterfaceGetState {
    private final int[] tools;
    private final boolean[] usedTools;
    private final int maxMultiplyUseTools = 3;
    private final int maxToolsCount = 6;
    private int totalToolsCount;
    private int roundToolsCount;

    public PlayerTools(){
        this.tools  = new int[6];
        this.usedTools = new boolean[3];
        Arrays.fill(tools, -1);
        Arrays.fill(usedTools, false);
    }

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

    public void addSingleUseTool(int strength) {
        for(int i = maxMultiplyUseTools - 1; i < tools.length; i++){
            if(tools[i] == -1){
                tools[i] = strength;
                break;
            }
        }



        public Optional<Integer> useTool(int index) {
            Optional<Integer> toReturn = Optional.empty();
            if(index >= maxToolsCount){
                return toReturn;
            }
            if (index > 2){
                if(tools[index] != -1){
                    toReturn = Optional.of(tools[index]);
                    totalToolsCount -= tools[index];
                    roundToolsCount -= tools[index];
                    tools[index] = -1;
                }
            } else {
                if(tools[index] != -1 && !usedTools[index]) {
                    roundToolsCount = roundToolsCount - tools[index];
                    usedTools[index] = true;
                    toReturn = Optional.of(tools[index]);
                }

            }
            return toReturn;
        }

        public boolean hasSufficientTools(int goal){
            return goal <= roundToolsCount;
        }


        @Override
        public String state() {
            Map<String, Object> state = Map.of(
                    "tools", tools,
                    "usedTools", usedTools,
                    "totalToolsCount", totalToolsCount,
                    "roundToolsCount", roundToolsCount
            );
            return new JSONObject(state).toString();
        }

        public int getTotalToolsCount() {
            return totalToolsCount;
        }

        public boolean[] getUsedTools() {
            return usedTools;
        }

        public int[] getTools() {
            return tools;
        }

        public int getRoundToolsCount() {
            return roundToolsCount;
        }
    }