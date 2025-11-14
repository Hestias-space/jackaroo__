package Controller;

import engine.Game;
import engine.board.Board;
import engine.board.Cell;
import exception.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.Colour;
import model.card.Card;
import model.card.standard.*;
import model.player.Marble;
import model.player.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class boardController {

    // UI Components
    @FXML private GridPane boardGrid;
    @FXML private Label currentPlayerLabel;
    @FXML private Button playTurnBtn;
    @FXML private Label turnLabel;
    @FXML private HBox handPane;
    //@FXML private VBox firePitPane;
    @FXML private HBox firePitPane;


    @FXML private Pane rootPane;

    // Game State
    private Game game;
    private Board board;
    private int controllerTurnCount = 0;
    private Card selectedCard;
    private int[][] trackCoordinates;

    // ================= INITIALIZATION =================
    public void initializeBackend(Game game) {
        this.game = game;
        this.board = game.getBoard();
        rootPane.requestFocus();
        updateUI();
    }

    public void initializeBackend(Game game, GridPane grid, int[][] trackCoordinates) {
        this.game = game;
        this.board = game.getBoard();
        this.trackCoordinates = trackCoordinates;
        updateUI();
    }

    // ================= GAME ACTIONS =================
    @FXML
    private void handlePlay() {
        try {
            game.playPlayerTurn();
            game.endPlayerTurn();
            controllerTurnCount++;
            updateUI();

            if (game.getActivePlayerColour() != Colour.RED) {
                playCPUTurns();
            }
        } catch (GameException e) {
            handleGameException(e);
        }
    }

    @FXML
    private void handleKeyPress(KeyEvent event) {
        if (event.isControlDown() && event.getCode() == KeyCode.F4) {
            fieldMarble();
        }
    }

    private void fieldMarble() {
        try {
            game.fieldMarble();
            updateUI();
        } catch (CannotFieldException e) {
            showAlert("Cannot Field Marble", e.getMessage());
        } catch (IllegalDestroyException e) {
            showAlert("Illegal Destruction", e.getMessage());
        }
    }

    // ================= CARD HANDLING =================
    void handleCardSelection(Card card) {
        try {
            Player currentPlayer = getCurrentPlayer();
            if (currentPlayer == null) {
                throw new InvalidCardException("No active player found");
            }

            if (card.equals(selectedCard)) {
                currentPlayer.deselectAll();
                selectedCard = null;
            } else {
                game.selectCard(card);
                selectedCard = card;
                handleSpecialCardInput(card);
            }

            updateUI();
        } catch (InvalidCardException e) {
            showAlert("Invalid Card", e.getMessage());
        }
    }

    private void handleSpecialCardInput(Card card) {
        if (card instanceof Seven) {
            showSplitDistanceDialog();
        } else if (card instanceof Ace) {
            showAceMoveChoiceDialog();
        } else if (card instanceof King) {
            showKingWarning();
        }
    }

    private void showSplitDistanceDialog() {
        TextInputDialog dialog = new TextInputDialog("3");
        dialog.setTitle("Seven Card");
        dialog.setHeaderText("Split Your Move");
        dialog.setContentText("Distance for first marble (1-6):");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(distance -> {
            try {
                int split = Integer.parseInt(distance);
                game.editSplitDistance(split);
            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "Please enter a valid number");
            } catch (SplitOutOfRangeException e) {
                showAlert("Invalid Split", "Split must be between 1-6");
            }
        });
    }

    private void showAceMoveChoiceDialog() {
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(1, 1, 11);
        dialog.setTitle("Ace Card");
        dialog.setHeaderText("Choose Your Move");
        dialog.setContentText("Move 1 or 11 spaces:");

        Optional<Integer> result = dialog.showAndWait();
        result.ifPresent(move -> {
            // Implementation depends on how you want to handle Ace moves
        });
    }

    private void showKingWarning() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("King Card");
        alert.setHeaderText("Warning: King Card Selected");
        alert.setContentText("This card will destroy any opponent's marble it lands on!");
        alert.showAndWait();
    }

    // ================= MARBLE HANDLING =================
    // In BoardController.java

    private ArrayList<Marble> tempSelectedMarbles; // Track marble selections manually



    // ================= EXCEPTION HANDLING =================
    private void handleGameException(GameException e) {
        if (e instanceof ActionException) {
            handleActionException((ActionException) e);
        } else if (e instanceof InvalidSelectionException) {
            showAlert("Selection Error", e.getMessage());
        } else {
            showAlert("Game Error", e.getMessage());
        }
    }

    private void handleActionException(ActionException e) {
        if (e instanceof IllegalMovementException) {
            showAlert("Illegal Move", e.getMessage());
        } else if (e instanceof IllegalSwapException) {
            showAlert("Illegal Swap", e.getMessage());
        } else if (e instanceof IllegalDestroyException) {
            showAlert("Illegal Destruction", e.getMessage());
        } else if (e instanceof CannotFieldException) {
            showAlert("Cannot Field Marble", e.getMessage());
        } else if (e instanceof CannotDiscardException) {
            showAlert("Cannot Discard", e.getMessage());
        } else {
            showAlert("Action Error", e.getMessage());
        }
    }

    // ================= PLAYER MANAGEMENT =================
    private Player getCurrentPlayer() {
        try {
            Colour activeColour = game.getActivePlayerColour();
            for (Player player : game.getPlayers()) {
                if (player.getColour() == activeColour) {
                    return player;
                }
            }
            throw new InvalidCardException("No active player found");
        } catch (Exception e) {
            showAlert("Player Error", e.getMessage());
            return null;
        }
    }

    private void playCPUTurns() {
        new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                try {
                    Thread.sleep(2000);

                    Platform.runLater(() -> {
                        try {
                            game.playPlayerTurn();
                            game.endPlayerTurn();
                            updateUI();
                        } catch (GameException e) {
                            handleGameException(e);
                        }
                    });
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    // ================= UI UPDATES =================
    private void updateUI() {
        updateTurnLabel();
        updatePlayerHand();
        updateBoardState();
        updateFirePit();
    }

    private void updateTurnLabel() {
        Colour activeColour = game.getActivePlayerColour();
        currentPlayerLabel.setText(activeColour + "'s Turn");
        turnLabel.setText("Move: " + (controllerTurnCount + 1));
    }

    private void updatePlayerHand() {
        handPane.getChildren().clear();

        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer != null) {
            boolean isHuman = !currentPlayer.getClass().getSimpleName().equals("CPU");

            for (Card card : currentPlayer.getHand()) {
                Node cardNode = createCardUI(card, !isHuman);

                if (card.equals(selectedCard)) {
                    cardNode.setStyle("-fx-effect: dropshadow(gaussian, gold, 10, 0.5, 0, 0);");
                }

                cardNode.setOnMouseClicked(e -> handleCardSelection(card));
                handPane.getChildren().add(cardNode);
            }
        }
    }

    private Node createCardUI(Card card, boolean isCPU) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CardComponent.fxml"));
            Node cardNode = loader.load();
            CardController controller = loader.getController();
            controller.setCard(card, isCPU);
            return cardNode;
        } catch (IOException e) {
            e.printStackTrace();
            return new Label("Card Error");
        }
    }

    private void updateBoardState() {
        boardGrid.getChildren().forEach(node -> {
            if (node instanceof StackPane) {
                StackPane cell = (StackPane) node;
                cell.getChildren().removeIf(n -> n instanceof Circle);
            }
        });

        List<Cell> track = board.getTrack();
        for (int i = 0; i < track.size(); i++) {
            Cell cell = track.get(i);
            if (cell.getMarble() != null && i < trackCoordinates.length) {
                int[] coord = trackCoordinates[i];
                StackPane uiCell = (StackPane) boardGrid.getChildren()
                        .filtered(n -> GridPane.getRowIndex(n) == coord[1] &&
                                GridPane.getColumnIndex(n) == coord[0])
                        .get(0);

                Circle marble = new Circle(10);
                marble.setFill(javafx.scene.paint.Color.valueOf(
                        cell.getMarble().getColour().toString()));

                if (cell.getMarble().getColour() == game.getActivePlayerColour()) {
                    marble.setEffect(new DropShadow(15, Color.GOLD));
                }

                uiCell.getChildren().add(marble);
            }
        }
    }

    private void updateFirePit() {
        firePitPane.getChildren().clear();

        for (Card card : game.getFirePit()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/card.fxml"));
                VBox cardNode = loader.load();

                CardController controller = loader.getController();
                controller.setCard(card, true); // true = CPU/fire pit = non-clickable

                firePitPane.getChildren().add(cardNode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    // ================= HELPER METHODS =================
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showWinMessage(Colour winner) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText(winner.toString() + " player has won the game!");

        try {
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(getClass().getResource("/images/win_icon.jpg").toExternalForm()));
        } catch (Exception e) {
            // Icon not critical
        }

        alert.showAndWait();
    }




}