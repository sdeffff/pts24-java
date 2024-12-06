package sk.uniba.fmph.dcs.player_board;

import sk.uniba.fmph.dcs.game_board.CivilizationCard;
import sk.uniba.fmph.dcs.stone_age.Effect;
import sk.uniba.fmph.dcs.stone_age.EndOfGameEffect;
import sk.uniba.fmph.dcs.stone_age.InterfaceFeedTribe;
import sk.uniba.fmph.dcs.stone_age.InterfaceNewTurn;
import sk.uniba.fmph.dcs.stone_age.InterfacePlayerBoardGameBoard;

import java.util.*;

public class PlayerBoardGameBoardFacade
        implements InterfaceFeedTribe, InterfaceNewTurn, InterfacePlayerBoardGameBoard {

    private final PlayerBoard playerBoard;

    public PlayerBoardGameBoardFacade(PlayerBoard playerBoard) {
        this.playerBoard = playerBoard;
    }

    /**
     * Feeds the tribe if there is enough food.
     *
     * @return {@code true} if the tribe is successfully fed, {@code false} otherwise.
     */
    @Override
    public boolean feedTribeIfEnoughFood() {
        return this.playerBoard.getTribeFedStatus().feedTribeIfEnoughFood();
    }

    /**
     * Feeds the tribe using the given resources.
     *
     * @param resources A collection of resources used to feed the tribe.
     * @return {@code true} if the tribe was fed, {@code false} otherwise.
     */
    @Override
    public boolean feedTribe(Collection<Effect> resources) {
        return this.playerBoard.getTribeFedStatus().feedTribe(resources);
    }

    /**
     * Marks the tribe as not fed for this turn and applies penalties if needed.
     *
     * @return {@code true} if feeding was skipped successfully, {@code false} otherwise.
     */
    @Override
    public boolean doNotFeedThisTurn() {
        boolean fed = this.playerBoard.getTribeFedStatus().feedTribeIfEnoughFood();
        if (fed) this.playerBoard.addPoints(-1);
        return fed;
    }

    /**
     * Checks if the tribe is fed.
     *
     * @return {@code true} if the tribe is fed, otherwise {@code false}.
     */
    @Override
    public boolean isTribeFed() {
        return this.playerBoard.getTribeFedStatus().isTribeFed();
    }

    /**
     * Advances the game to the next turn.
     *
     * @return {@code true} if the turn was advanced successfully, otherwise {@code false}.
     */
    @Override
    public boolean newTurn() {
        return this.playerBoard.newTurn();
    }

    /**
     * Grants resources to the player.
     *
     * @param effects An array of resource effects to be provided.
     */
    @Override
    public void giveEffect(Effect[] effects) {
        this.playerBoard.getPlayerResourcesAndFood().giveResources(Arrays.asList(effects));
    }

    /**
     * Assigns end-of-game effects to the player's civilization cards.
     *
     * @param effects A collection of end-of-game effects to add.
     */
    @Override
    public void giveEndOfGameEffect(Collection<EndOfGameEffect> effects) {
        this.playerBoard.getPlayerCivilisationCards().addEndOfGameEffects(effects.toArray(new EndOfGameEffect[0]));
    }

    /**
     * Grants a new civilization card to the player.
     *
     * @param card The civilization card to assign.
     */
    @Override
    public void giveCard(CivilizationCard card) {
        this.playerBoard.getPlayerCivilisationCards().addCard(card);
    }

    /**
     * Removes specified resources from the player's inventory if available.
     *
     * @param resources A collection of resources to remove.
     * @return {@code true} if resources were successfully taken, otherwise {@code false}.
     */
    @Override
    public boolean takeResources(Collection<Effect> resources) {
        if (this.playerBoard.getPlayerResourcesAndFood().hasResources(resources)) {
            this.playerBoard.getPlayerResourcesAndFood().takeResources(resources);
            return true;
        }
        return false;
    }

    /**
     * Adds a new figure to the player's inventory.
     */
    @Override
    public void giveFigure() {
        this.playerBoard.getPlayerFigures().addNewFigure();
    }

    /**
     * Removes the specified number of figures from the player's inventory if available.
     *
     * @param count The number of figures to remove.
     * @return {@code true} if figures were successfully taken, otherwise {@code false}.
     */
    @Override
    public boolean takeFigures(int count) {
        if (this.playerBoard.getPlayerFigures().hasFigures(count)) {
            this.playerBoard.getPlayerFigures().takeFigures(count);
            return true;
        }
        return false;
    }

    /**
     * Checks if the player has at least the specified number of figures.
     *
     * @param count The minimum number of figures to check.
     * @return {@code true} if the player has enough figures, otherwise {@code false}.
     */
    @Override
    public boolean hasFigures(int count) {
        return this.playerBoard.getPlayerFigures().hasFigures(count);
    }

    /**
     * Checks if the player has sufficient tools to meet a specified requirement.
     *
     * @param goal The required tool count to check against.
     * @return {@code true} if the player has enough tools, otherwise {@code false}.
     */
    @Override
    public boolean hasSufficientTools(int goal) {
        return this.playerBoard.getPlayerTools().hasSufficientTools(goal);
    }

    /**
     * Uses a specific tool by its index.
     *
     * @param index The index of the tool to use.
     * @return An {@code Optional} containing the tool's value if valid, or {@code Optional.empty()} otherwise.
     */
    @Override
    public Optional<Integer> useTool(int index) {
        Integer toolValue = this.playerBoard.getPlayerTools().useTool(index);
        return Optional.ofNullable(toolValue);
    }

    /**
     * Retrieves the player's resources and food object for testing purposes.
     *
     * @return The PlayerResourcesAndFood instance.
     */
    public PlayerResourcesAndFood getPlayerResourcesAndFood() {
        return this.playerBoard.getPlayerResourcesAndFood();
    }
}
