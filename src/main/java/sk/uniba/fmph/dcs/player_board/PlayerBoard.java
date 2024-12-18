package sk.uniba.fmph.dcs.player_board;

import sk.uniba.fmph.dcs.stone_age.InterfaceGetState;
import sk.uniba.fmph.dcs.stone_age.TribeFedStatus;

import org.json.JSONObject;
import java.util.*;

/**
 * Represents the player board, managing resources, figures, tools, civilization cards, and game progress.
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

    /**
     * Creates a PlayerBoard with the specified components.
     *
     * @param pcc The player's civilization cards.
     * @param pf  The player's figures.
     * @param prf The player's resources and food.
     * @param pt  The player's tools.
     * @param tfs The player's tribe fed status.
     */
    public PlayerBoard(final PlayerCivilisationCards pcc, final PlayerFigures pf, final PlayerResourcesAndFood prf,
                       final PlayerTools pt, final TribeFedStatus tfs) {
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
     * Creates a PlayerBoard with default components.
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
     * Prepares the player board for a new turn.
     * Resets tribe feeding status, available figures, and tool usage.
     */
    public void newTurn() {
        this.tribeFedStatus.newTurn();
        this.playerFigures.newTurn();
        this.playerTools.newTurn();
    }

    /**
     * Gets the player's resources and food.
     *
     * @return The {@code PlayerResourcesAndFood} object.
     */
    public PlayerResourcesAndFood getPlayerResourcesAndFood() {
        return this.playerResourcesAndFood;
    }

    /**
     * Gets the player's figures.
     *
     * @return The {@code PlayerFigures} object.
     */
    public PlayerFigures getPlayerFigures() {
        return this.playerFigures;
    }

    /**
     * Gets the player's tools.
     *
     * @return The {@code PlayerTools} object.
     */
    public PlayerTools getPlayerTools() {
        return this.playerTools;
    }

    /**
     * Gets the player's civilization cards.
     *
     * @return The {@code PlayerCivilisationCards} object.
     */
    public PlayerCivilisationCards getPlayerCivilisationCards() {
        return this.playerCivilisationCards;
    }

    /**
     * Gets the player's tribe feeding status.
     *
     * @return The {@code TribeFedStatus} object.
     */
    public TribeFedStatus getTribeFedStatus() {
        return this.tribeFedStatus;
    }

    /**
     * Adds points to the player's total score.
     *
     * @param points The number of points to add (can be negative).
     * @return The updated total score.
     */
    public int addPoints(final int points) {
        this.points += points;
        return this.points;
    }

    /**
     * Increases the count of houses owned by the player by one.
     */
    public void addHouse() {
        this.houses++;
    }

    /**
     * Adds end-of-game points to the player's score based on game components.
     * Points are calculated from civilization cards, tools, fields, figures, and resources.
     * Ensures this operation is performed only once.
     */
    public void addEndOfGamePoints() {
        if (this.endOfGamePointsAdded) {
            return;
        }
        this.points += this.playerCivilisationCards.calculateEndOfGameCivilisationCardsPoints(
                this.houses,
                this.playerTools.getTools().length,
                this.tribeFedStatus.getFields(),
                this.playerFigures.getTotalFigures()
        );
        this.points += this.playerResourcesAndFood.numberOfResourcesForFinalPoints();
        this.endOfGamePointsAdded = true;
    }

    /**
     * Gets a structured summary of the player's board state.
     *
     * @return A string representation of the player board's current state.
     */
    @Override
    public String state() {
        Map<String, Object> state = Map.of(
                "points", this.points,
                "houses", this.houses,
                "resourcesAndFood", this.playerResourcesAndFood.state(),
                "figures", this.playerFigures.state(),
                "tools", this.playerTools.state(),
                "civilizationCards", this.playerCivilisationCards.state(),
                "tribeFeedingStatus", this.tribeFedStatus.state()
        );
        return new JSONObject(state).toString();
    }
}