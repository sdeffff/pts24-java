
package sk.uniba.fmph.dcs.game_board;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.stone_age.ActionResult;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.HasAction;
import sk.uniba.fmph.dcs.stone_age.PlayerOrder;
import sk.uniba.fmph.dcs.stone_age.ImmediateEffect;

import java.util.Collection;
import java.util.Optional;
import java.util.Map;
import java.util.LinkedList;

public final class CivilizationCardPlace implements InterfaceFigureLocationInternal {
    private final int requiredResources;
    private PlayerOrder figure;
    private Optional<CivilizationCard> optionalCard;
    private CivilizationCardDeck deck;
    private CivilizationCardPlace next;
    private Map<ImmediateEffect, EvaluateCivilizationCardImmediateEffect> evaluate;

    public CivilizationCardPlace(final CivilizationCardDeck deck, final Optional<CivilizationCard> card,
                                 final CivilizationCardPlace next, final int requiredResources,
                                 final Map<ImmediateEffect, EvaluateCivilizationCardImmediateEffect> evaluate) {
        this.optionalCard = card;
        this.deck = deck;
        this.requiredResources = requiredResources;
        this.evaluate = evaluate;
        this.next = next;
        figure = null;
    }

    @Override
    public boolean placeFigures(final Player player, final int figureCount) {
        if (player.playerBoard().takeFigures(figureCount) &&
                tryToPlaceFigures(player, figureCount).equals(HasAction.WAITING_FOR_PLAYER_ACTION)) {
            figure = player.playerOrder();
            return true;
        }
        return false;
    }

    @Override
    public HasAction tryToPlaceFigures(final Player player, final int count) {
        if (figure != null || count != 1 ||
                !player.playerBoard().hasFigures(count)) {
            return HasAction.NO_ACTION_POSSIBLE;
        }
        return HasAction.WAITING_FOR_PLAYER_ACTION;
    }

    //outputResources contains one Effect which represents player`s choice if immediate effect is GetChoice, or is empty otherwise
    @Override
    public ActionResult makeAction(final Player player,
                                   final Collection<Effect> inputResources, final Collection<Effect> outputResources) {
        if (tryToMakeAction(player).equals(HasAction.NO_ACTION_POSSIBLE) ||
                inputResources.size() < requiredResources) {
            return ActionResult.FAILURE;
        }

        for (Effect e: inputResources) {
            if (!e.isResource()) {
                return ActionResult.FAILURE;
            }
        }
        if (!player.playerBoard().takeResources(inputResources)) {
            return ActionResult.FAILURE;
        }
        CivilizationCard card = optionalCard.get();
        ActionResult result = null;
        Effect choice = null;
        for (ImmediateEffect effect: card.getImmediateEffectType()) {
            if (effect.equals(ImmediateEffect.ArbitraryResource)) {
                if (!outputResources.iterator().hasNext()) {
                    return ActionResult.FAILURE;
                }
                choice = outputResources.iterator().next();
            }
            if (!evaluate.get(effect).tryToPerformEffect(player, choice)) {
                return ActionResult.FAILURE;
            }
        }
        for (ImmediateEffect effect: card.getImmediateEffectType()) {
            result = evaluate.get(effect).performEffect(player, choice);
        }
        player.playerBoard().giveEndOfGameEffect(card.getEndOfGameEffectType());
        optionalCard = Optional.empty();
        figure = null;
        return result;
    }

    @Override
    public boolean skipAction(final Player player) {
        if (tryToMakeAction(player).equals(HasAction.NO_ACTION_POSSIBLE)) {
            return false;
        }

        figure = null;
        return true;
    }

    @Override
    public HasAction tryToMakeAction(final Player player) {
        if (!player.playerOrder().equals(figure)) {
            return HasAction.NO_ACTION_POSSIBLE;
        }
        return HasAction.WAITING_FOR_PLAYER_ACTION;
    }

    @Override
    public boolean newTurn() {
        LinkedList<CivilizationCard> cards = new LinkedList<>();
        if (getNewCardsLeft(this, cards, 0)) {
            return true;
        }
        CivilizationCardPlace currentPlace = this;
        while (currentPlace != null) {
            currentPlace.optionalCard = Optional.of(cards.remove());
            currentPlace = currentPlace.next;
        }

        return false;
    }

    private boolean getNewCardsLeft(final CivilizationCardPlace currentPlace, final LinkedList<CivilizationCard> cards, int count) {
        currentPlace.optionalCard.ifPresent(cards::add);
        count++;

        if (currentPlace.next == null) {
            while (cards.size() < count) {
                if (deck.state().isEmpty()) {
                    return true;
                }
                cards.add(deck.getTop().get());
            }
            return false;
        }
        return getNewCardsLeft(currentPlace.next, cards, count);
    }

    public String state() {
        Map<String, String> state;
        if (figure == null) {
            state = Map.of("notInitialized", "null");
        } else if (next == null) {
            state = Map.of("requiredResources", String.valueOf(requiredResources),
                    "figure", figure.toString(), "OptionalCard",
                    optionalCard.toString(), "next", "null");
        } else {
            state = Map.of("requiredResources", String.valueOf(requiredResources),
                    "figure", figure.toString(), "OptionalCard",
                    optionalCard.toString(), "next", next.state());
        }
        return new JSONObject(state).toString();
    }

    public Optional<CivilizationCard> getOptionalCard() {
        return optionalCard;
    }
}