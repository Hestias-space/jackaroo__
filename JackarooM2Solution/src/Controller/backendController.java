package Controller;

import engine.Game;
import exception.*;
import javafx.event.ActionEvent;
import model.card.standard.King;
import model.player.Marble;
import model.player.Player;

public class backendController {
    private final Game game;
    private final Player player;

    public backendController(Game game, Player player) {
        this.game = game;
        this.player= player;
    }

    // ================= GAME ACTION HANDLERS =================




    public void handlePlay(ActionEvent event) {
        boolean isHumanTurn = game.getActivePlayerColour().equals(game.getPlayers().get(0).getColour());
        try {
            game.playPlayerTurn();
            game.endPlayerTurn();

            if (!isHumanTurn) {
                handleCPUTurn();
            }
        } catch (GameException e) {
            showError("Play Error", e.getMessage());
        }
    }

    private void handleCPUTurn() {
        // CPU turn logic using existing game methods
        try {
            game.playPlayerTurn();
            game.endPlayerTurn();
        } catch (GameException e) {
            showError("CPU Play Error", e.getMessage());
        }
    }

    public void handleFieldMarble(ActionEvent event) {
        try {
            game.fieldMarble();
            game.endPlayerTurn();
        } catch (CannotFieldException | IllegalDestroyException e) {
            showError("Fielding Error", e.getMessage());
        }
    }

    public void handleMove(Marble marble, int steps, ActionEvent event) {
        try {
            boolean destroy = player.getSelectedCard() instanceof King;
            game.getBoard().moveBy(marble, steps, destroy);
            game.endPlayerTurn();
        } catch (GameException e) {
            showError("Move Error", e.getMessage());
        }
    }

    public void handleSwap(Marble marble1, Marble marble2, ActionEvent event) {
        try {
            game.getBoard().swap(marble1, marble2);
            game.endPlayerTurn();
        } catch (GameException e) {
            showError("Swap Error", e.getMessage());
        }
    }

    // ================= ERROR HANDLING =================

    private void showError(String title, String message) {
        // Implement error display (e.g., show alert)
        System.err.println(title + ": " + message);
    }


}