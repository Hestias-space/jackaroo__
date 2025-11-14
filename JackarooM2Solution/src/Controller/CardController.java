package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.card.Card;
import model.card.standard.Standard;
import model.card.standard.Suit;

public class CardController {
    // FXML Components
    @FXML private VBox root;
    @FXML private Label nameLabel;
    @FXML private Label descriptionLabel;
    @FXML private HBox detailsBox;

    private boardController mainController;

    // State
    private Card card;
    private boolean isCPU;

    public void setCard(Card card, boolean isCPU) {
        this.card = card;
        this.isCPU = isCPU;
        refreshCard();
        setupInteractivity();
    }

    private void refreshCard() {
        detailsBox.getChildren().clear();

        if (isCPU) {
            configureCPUCard();
        } else {
            configureHumanCard();
        }
    }

    private void configureCPUCard() {
        root.setStyle("-fx-background-color: #e0e0e0; -fx-border-color: #777;");
        nameLabel.setText("[CPU Card]");
        descriptionLabel.setText("");
    }

    private void configureHumanCard() {
        root.setStyle("-fx-background-color: white; -fx-border-color: #999;");
        nameLabel.setText(card.getName());
        descriptionLabel.setText(card.getDescription());

        if (card instanceof Standard) {
            addStandardCardDetails((Standard) card);
        }
    }

    private void addStandardCardDetails(Standard card) {
        String suitSymbol = getSuitSymbol(card.getSuit());
        String colorStyle = isRedSuit(card.getSuit())
                ? "-fx-text-fill: red;"
                : "-fx-text-fill: black;";

        detailsBox.getChildren().addAll(
                new Label("Rank: " + card.getRank()),
                new Label("Suit: " + suitSymbol) {{
                    setStyle(colorStyle);
                }}
        );
    }

    private void setupInteractivity() {
        if (!isCPU) {
            root.setOnMouseEntered(e -> root.setStyle("-fx-background-color: #ffffcc;"));
            root.setOnMouseExited(e -> refreshCard());
            root.setOnMouseClicked(e -> handleCardClick()); // ✅ add this
        } else {
            root.setOnMouseEntered(null);
            root.setOnMouseExited(null);
            root.setOnMouseClicked(null); // ignore clicks for CPU cards
        }
    }


    public void setSelected(boolean selected) {
        if (selected && !isCPU) {
            root.setStyle("-fx-background-color: #ffcc00; -fx-border-width: 2;");
        } else {
            refreshCard();
        }
    }

    // Helper methods
    private boolean isRedSuit(Suit suit) {
        return suit == Suit.HEART || suit == Suit.DIAMOND;
    }

    private String getSuitSymbol(Suit suit) {
        return switch(suit) {
            case HEART -> "♥";
            case DIAMOND -> "♦";
            case SPADE -> "♠";
            case CLUB -> "♣";
        };
    }

    private void handleCardClick() {
        if (!isCPU) {
            try {
                // Get the main controller
                boardController mainController = (boardController) root.getScene().getUserData();

                // Notify main controller of card selection
                mainController.handleCardSelection(card);

                // Visual feedback
                setSelected(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void setMainController(boardController controller) {
        this.mainController = controller;
    }



    public VBox getRoot() {
        return root;
    }
}