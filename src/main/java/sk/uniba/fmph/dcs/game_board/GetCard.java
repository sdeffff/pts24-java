package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.Player;

import java.util.Optional;

public class GetCard implements EvaluateCivilizationCardImmediateEffect{

    private CivilizationCardDeck deck;

    public GetCard(CivilizationCardDeck deck){
        this.deck = deck;
    }
    @Override
    public boolean performEffect(Player player, Effect effect){
        if (effect.equals(Effect.CARD)) {
            Optional<CivilizationCard> card = deck.getTop();
            if(card.isPresent()) {
                player.playerBoard().giveCard(card.get());
                return true;
            }else{
                return false;
            }

            return true;
        }else{
            return false;
        }
    }
}
