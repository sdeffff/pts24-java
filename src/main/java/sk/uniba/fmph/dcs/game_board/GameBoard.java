package sk.uniba.fmph.dcs.game_board;

public class GameBoard {
    private String state;

    public GameBoard() {
        this.state = "Game started!";
    }

    public String state() {
        return this.state;
    }

    public boolean newTurn() {
        this.state = "New turn...";
        return false;
    }
}