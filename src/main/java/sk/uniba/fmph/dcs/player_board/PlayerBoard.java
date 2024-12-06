package sk.uniba.fmph.dcs.player_board;

import sk.uniba.fmph.dcs.stone_age.InterfaceGetState;
import sk.uniba.fmph.dcs.stone_age.TribeFedStatus;

/**
 * Represents the player's board, managing resources, figures, tools, houses, and game points.
 */
public class PlayerBoard implements InterfaceGetState {
    private int points;
    private int houses;
    private final PlayerResourcesAndFood playerResourcesAndFood;
    private final PlayerFigures playerFigures;
    private final PlayerTools playerTools;
    private final PlayerCivilisationCards playerCivilisationCards;
    private final TribeFedStatus tribeFedStatus;
    private boolean endOfGamePointsAdded;

    public PlayerBoard(PlayerCivilisationCards pcc, PlayerFigures pf, PlayerResourcesAndFood prf, PlayerTools pt,  TribeFedStatus tfs) {
        this.playerCivilisationCards = pcc;
        this.playerFigures = pf;
        this.playerResourcesAndFood = prf;
        this.playerTools = pt;
        this.tribeFedStatus = tfs;
        this.points = 0;
        this.houses = 0;
        this.endOfGamePointsAdded = false;
    }

    /**
     * Constructs a PlayerBoard with default components.
     */
    public PlayerBoard() {
        this.playerResourcesAndFood = new PlayerResourcesAndFood();
        this.playerFigures = new PlayerFigures();
        this.playerTools = new PlayerTools();
        this.playerCivilisationCards = new PlayerCivilisationCards();
        this.tribeFedStatus = new TribeFedStatus(this.playerFigures);
        this.points = 0;
        this.houses = 0;
        this.endOfGamePointsAdded = false;
    }

    /**
     * Starts a new turn for the player, resetting tribe fed status, figures, and tools.
     */
    public void newTurn() {
        this.tribeFedStatus.newTurn();
        this.playerFigures.newTurn();
        this.playerTools.newTurn();
    }

    /**
     * Retrieves the player's current resources and food.
     *
     * @return The {@code PlayerResourcesAndFood} object.
     */
    public PlayerResourcesAndFood getPlayerResourcesAndFood() {
        return this.playerResourcesAndFood;
    }

    /**
     * Retrieves the player's figures status.
     *
     * @return The {@code PlayerFigures} object.
     */
    public PlayerFigures getPlayerFigures() {
        return this.playerFigures;
    }

    /**
     * Retrieves the player's tools status.
     *
     * @return The {@code PlayerTools} object.
     */
    public PlayerTools getPlayerTools() {
        return this.playerTools;
    }

    /**
     * Retrieves the player's civilization cards.
     *
     * @return The {@code PlayerCivilisationCards} object.
     */
    public PlayerCivilisationCards getPlayerCivilisationCards() {
        return this.playerCivilisationCards;
    }

    /**
     * Retrieves the tribe's current fed status.
     *
     * @return The {@code TribeFedStatus} object.
     */
    public TribeFedStatus getTribeFedStatus() {
        return this.tribeFedStatus;
    }

    /**
     * Adds a specified number of points to the player's total.
     *
     * @param points The points to add (can be negative).
     * @return The updated total points.
     */
    public int addPoints(int points) {
        this.points += points;
        return this.points;
    }

    /**
     * Increments the count of houses owned by the player by one.
     */
    public void addHouse() {
        this.houses++;
    }

    /**
     * Calculates and adds end-of-game points based on game components.
     * Ensures points are added only once.
     */
    public void addEndOfGamePoints() {
        if (this.endOfGamePointsAdded) {
            return;
        }
        this.points += this.playerCivilisationCards.calculateEndOfGameCivilisationCardsPoints(
                this.houses,
                this.playerTools.getTools(),
                this.tribeFedStatus.getFields(),
                this.playerFigures.getTotalFigures()
        );
        this.points += this.playerResourcesAndFood.numberOfResourcesForFinalPoints();
        this.endOfGamePointsAdded = true;
    }

    /**
     * Provides a string representation of the current state of the player board.
     * Includes points, houses, resources, figures, tools, civilization cards, and tribe fed status.
     *
     * @return A string summarizing the player's board state.
     */
    @Override
    public String state() {
        return "Points: " + this.points + ", Houses: " + this.houses + "\n" +
                this.playerResourcesAndFood.state() + "\n" +
                this.playerFigures.state() + "\n" +
                this.playerTools.state() + "\n" +
                this.playerCivilisationCards.state() + "\n" +
                this.tribeFedStatus.state();
    }
}
