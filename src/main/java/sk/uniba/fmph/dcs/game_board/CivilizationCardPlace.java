package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.*;

import java.util.*;

/**
 * Represents the area on the game board where players interact with Civilization Cards.
 * Handles figure placement, actions related to card acquisition, and resets at the end of each turn.
 */
public class CivilizationCardPlace implements InterfaceFigureLocationInternal {
    // The number of resources required to acquire a civilization card
    private final int requiredResources;

    // Evaluates and applies the immediate effect of a civilization card
    private final EvaluateCivilizationCardImmediateEffect evaluateCivilizationCardImmediateEffect;

    // The deck of civilization cards
    private final CivilizationCardDeck deck;

    // Stores the player orders (figures) currently placed in this location
    private final List<PlayerOrder> figures;

    // The topmost civilization card available for acquisition
    private CivilizationCard currentCivilizationCard;

    // Tracks whether an action has already been made this turn
    private boolean actionMade;

    /**
     * Constructor for CivilizationCardPlace.
     *
     * @param requiredResources The number of resources needed to acquire a card.
     * @param figures           A collection to track figures placed in this location.
     */
    public CivilizationCardPlace(int requiredResources, List<PlayerOrder> figures, 
                                 CivilizationCardDeck deck, 
                                 EvaluateCivilizationCardImmediateEffect evaluateEffect) {
        this.requiredResources = requiredResources;
        this.figures = figures;
        this.actionMade = false;
        this.deck = deck;
        this.evaluateCivilizationCardImmediateEffect = evaluateEffect;
    }

    /**
     * Places a specified number of figures on this location for a player.
     *
     * @param player      The player attempting to place figures.
     * @param figureCount The number of figures to place.
     * @return True if figures were successfully placed, otherwise false.
     */
    @Override
    public boolean placeFigures(Player player, int figureCount) {
        if (!canPlaceFigures(player, figureCount)) {
            return false;
        }
        figures.add(player.playerOrder());
        return true;
    }

    private boolean canPlaceFigures(Player player, int figureCount) {
        return player.playerBoard().hasFigures(figureCount) && figures.isEmpty() && player.playerBoard()
                .takeFigures(figureCount);
    }

    /**
     * Attempts to validate and place figures for a player.
     *
     * @param player The player attempting to place figures.
     * @param count  The number of figures to place.
     * @return The action status as HasAction.
     */
    @Override
    public HasAction tryToPlaceFigures(Player player, int count) {
        if (isInvalidPlacement(player, count)) {
            return HasAction.NO_ACTION_POSSIBLE;
        }
        figures.add(player.playerOrder());
        return HasAction.AUTOMATIC_ACTION_DONE;
    }

    private boolean isInvalidPlacement(Player player, int count) {
        return player == null || count < 1 || count > 10 || !canPlaceFigures(player, count);
    }

    /**
     * Executes the action associated with acquiring a civilization card.
     *
     * @param player         The player performing the action.
     * @param inputResources Resources the player is using to acquire the card.
     * @param outputResources Effects/resources gained from the card (unused here).
     * @return The result of the action as boolean.
     */
    @Override
    public boolean makeAction(Player player, Collection<Effect> inputResources, Collection<Effect> outputResources) {
        if (!canMakeAction(player, inputResources)) {
            return boolean.FAILURE;
        }
        applyCardEffects(player, outputResources);
        currentCivilizationCard = deck.getTop().orElse(null);
        actionMade = true;
        return boolean.ACTION_DONE;
    }

    private boolean canMakeAction(Player player, Collection<Effect> inputResources) {
        return !actionMade && figures.contains(player.playerOrder()) && inputResources.size() >= requiredResources && player.playerBoard().takeResources(inputResources);
    }

    private void applyCardEffects(Player player, Collection<Effect> outputResources) {
        player.playerBoard().giveCard(currentCivilizationCard);
        if (currentCivilizationCard != null) {
            for (ImmediateEffect effect : currentCivilizationCard.getImmediateEffectType()) {
                evaluateCivilizationCardImmediateEffect.performEffect(player, Effect.CARD); // Default to CARD effect
            }
        }
        if (!outputResources.isEmpty()) {
            player.playerBoard().giveEffect(outputResources);
        }
    }

    /**
     * Allows a player to skip the action for this location.
     *
     * @param player The player skipping the action.
     * @return True after successfully skipping the action.
     */
    @Override
    public boolean skipAction(Player player) {
        figures.remove(player.playerOrder());
        actionMade = true;
        return true;
    }

    /**
     * Checks if the player can perform the action associated with this location.
     *
     * @param player The player attempting the action.
     * @return The action status as HasAction.
     */
    @Override
    public HasAction tryToMakeAction(Player player) {
        return actionMade || !figures.contains(player.playerOrder()) ? HasAction.NO_ACTION_POSSIBLE : HasAction.AUTOMATIC_ACTION_DONE;
    }

    /**
     * Resets the location for the next game round.
     *
     * @return True after successfully resetting.
     */
    @Override
    public boolean newTurn() {
        actionMade = false;
        figures.clear();
        currentCivilizationCard = deck.getTop().orElse(null);
        return true;
    }

    public String state() {
        return "CivilizationCardPlace State:\n" +
                "  Current Card: " +
                (currentCivilizationCard != null ? currentCivilizationCard.toString() : "None") +
                "\n" +
                "  Action Made: " + actionMade + "\n" +
                "  Figures Placed: " + figures.size() + " " +
                (figures.isEmpty() ? "(None)" : figures) + "\n" +
                "  Deck Status: " + deck.state();
    }
}

