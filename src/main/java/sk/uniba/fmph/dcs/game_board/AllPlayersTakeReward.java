package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.InterfacePlayerBoardGameBoard;
import sk.uniba.fmph.dcs.stone_age.Player;

import java.util.ArrayList;
import java.util.List;



public class AllPlayersTakeReward implements EvaluateCivilizationCardImmediateEffect{

    private void helper(int[] dices, List<Integer> arrDices) {
        for(int i: dices) {
            arrDices.add(i);
        }
    }

    private RewardMenu menu;
    private int amountOfPlayers;

    AllPlayersTakeReward(RewardMenu menu, int amountOfPlayers) {
        this.amountOfPlayers = amountOfPlayers;
        this.menu = menu;
    }

    @Override
    public boolean performEffect(Player player, Effect choice) {
        List<Integer> dices = new ArrayList<>();
        List<Effect> rewards = menu.rewards;
        helper(Throw.throw_(amountOfPlayers), dices);
        InterfacePlayerBoardGameBoard playerBoard = player.playerBoard();
        if(!dices.contains(rewards.indexOf(choice))){
            return false;
        }
        menu.takeReward(player.playerOrder(), choice);
        return true;
    }
}
