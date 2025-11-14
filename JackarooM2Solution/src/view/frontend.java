package view;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.Colour;
import model.player.Player;

import java.io.IOException;
import java.util.ArrayList;
import engine.board.Cell;

import engine.Game;
public class frontend {




    private static final int ROWS = 35;
    private static final int COLS = 35;
    private static final int CELL_SIZE = 15;
    private static final double MARBLE_RADIUS = 7;
    private static final int [][] trackCoordinates = {
            {34,17}, {34,15}, {33,15}, {32,15}, {31,15}, {30,15}, {29,15}, {29,14},
            {28,13}, {27,12}, {26,11}, {25,10}, {24,9}, {23,8}, {22,7}, {21,6},
            {20,5}, {19,5}, {19,4}, {19,3}, {19,2}, {19,1}, {19,0}, {18,0},
            {17,0}, {16,0}, {15,0}, {15,1}, {15,2}, {15,3}, {15,4}, {15,5},
            {14,5}, {13,6}, {12,7}, {11,8}, {10,9}, {9,10}, {8,11}, {7,12},
            {6,13}, {5,14}, {5,15}, {4,15}, {3,15}, {2,15}, {1,15}, {0,15},
            {0,16}, {0,17}, {0,18}, {0,19}, {1,19}, {2,19}, {3,19}, {4,19},
            {5,19}, {5,20}, {6,21}, {7,22}, {8,23}, {9,24}, {10,25}, {11,26},
            {12,27}, {13,28}, {14,29}, {15,29}, {15,30}, {15,31}, {15,32}, {15,33},
            {15,34}, {16,34}, {17,34}, {18,34}, {19,34}, {19,33}, {19,32}, {19,31},
            {19,30}, {19,29}, {20,29}, {21,28}, {22,27}, {23,26}, {24,25}, {25,24},
            {26,23}, {27,22}, {28,21}, {29,20}, {29,19}, {30,19}, {31,19}, {32,19},
            {33,19}, {34,19}, {34,18}, {34,17}, {34,16}
    };
    private static final int [][][] homezoneCoordinates ={ // Player 1 (Red) - Coordinates: {31,23}, {30,23}, {30,24}, {31,24}
            {{31,23}, {30,23}, {30,24}, {31,24}},

            // Player 2 (Green) - Coordinates: {23,3}, {23,2}, {24,2}, {24,3}
            {{23,3}, {23,2}, {24,2}, {24,3}},

            // Player 3 (Blue) - Coordinates: {2,10}, {2,11}, {3,10}, {3,11}
            {{2,10}, {2,11}, {3,10}, {3,11}},

            // Player 4 (Yellow) - Coordinates: {10,31}, {10,32}, {11,31}, {11,32}
            {{10,31}, {10,32}, {11,31}, {11,32}}};

    private static final int [][][] safezoneCoordinates ={
            // Player 1 (Red) - Coordinates: {29,17}, {30,17}, {31,17}, {32,17}
            {{29,17}, {30,17}, {31,17}, {32,17}},

            // Player 2 (Green) - Coordinates: {17,5}, {17,4}, {17,3}, {17,2}
            {{17,5}, {17,4}, {17,3}, {17,2}},

            // Player 3 (Blue) - Coordinates: {5,17}, {4,17}, {3,17}, {2,17}
            {{5,17}, {4,17}, {3,17}, {2,17}},

            // Player 4 (Yellow) - Coordinates: {17,29}, {17,30}, {17,31}, {17,32}
            {{17,29}, {17,30}, {17,31}, {17,32}}
    };

    private Label firePitLabel;

    public Scene createMainScene(Stage primaryStage) throws IOException {
        Font customFont = Font.loadFont(getClass().getResourceAsStream("/font/PublicPixel-rv0pA.ttf"), 16);


        Button button = new Button("START");
        button.setPrefSize(300, 100);
        button.setTextFill(Color.WHITE);
        button.setFont(Font.font(customFont.getName(), FontWeight.BOLD, 45));
        button.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));

        button.setOnMouseEntered(e -> {
            Color semiTransparentGray = Color.rgb(255, 255, 255, 0.15);
            button.setBackground(new Background(new BackgroundFill(semiTransparentGray, null, Insets.EMPTY)));
        });

        button.setOnMouseExited(e -> {
            button.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, Insets.EMPTY)));
        });

        Button button2 = new Button("Shortcut");
        button2.setPrefSize(200, 75);
        button2.setTextFill(Color.LIGHTGOLDENRODYELLOW);
        button2.setFont(Font.font(customFont.getName(), FontWeight.BOLD, 18));
        button2.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));

        button2.setOnMouseEntered(e -> {
            Color semiTransparentGray = Color.rgb(255, 255, 255, 0.15);
            button2.setBackground(new Background(new BackgroundFill(semiTransparentGray, null, Insets.EMPTY)));
        });

        button2.setOnMouseExited(e -> {
            button2.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, Insets.EMPTY)));
        });

        DropShadow textGlow = new DropShadow();
        textGlow.setColor(Color.PINK);
        textGlow.setOffsetX(0);
        textGlow.setOffsetY(0);
        textGlow.setWidth(20);
        textGlow.setHeight(20);
        textGlow.setSpread(0.7);
        button.setEffect(textGlow);

        button2.setEffect(textGlow);
        button2.setOnAction(e -> onShortcutButtonClicked());


        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setPadding(Insets.EMPTY);
        root.getChildren().addAll(button, button2);

        Image backgroundImage = new Image(getClass().getResource("/images/1385019.jpg").toExternalForm());

        BackgroundImage bgImage = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, false)
        );

        root.setBackground(new Background(bgImage));

        Scene scene = new Scene(root, 1400, 800);
        root.prefWidthProperty().bind(scene.widthProperty());
        root.prefHeightProperty().bind(scene.heightProperty());

        // ✅ Set button action to switch scene
        button.setOnAction(e -> switchToNewScene(primaryStage));


        return scene;
    }

    public void switchToNewScene(Stage stage) {
        // Load custom font
        Font customFont = Font.loadFont(getClass().getResourceAsStream("/font/PublicPixel-rv0pA.ttf"), 16);

        // Load and configure the GIF
        Image gifImage = new Image(getClass().getResource("/images/background.gif").toExternalForm());
        ImageView gifView = new ImageView(gifImage);
        gifView.setPreserveRatio(true);
        gifView.setFitWidth(2000); // match the scene width
        gifView.setFitHeight(900); // match the scene height


        // BACK BUTTON (top-left)
        Button backButton = new Button("← Back");
        backButton.setFont(Font.font(customFont.getName(), FontWeight.BOLD, 18));
        backButton.setTextFill(Color.WHITE);
        //backButton.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        HBox topLeftBar = new HBox(backButton);
        topLeftBar.setAlignment(Pos.TOP_LEFT);
        topLeftBar.setPadding(new Insets(10));

// Set the background of the HBox behind the button to black
        topLeftBar.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));


        backButton.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));

        backButton.setOnMouseEntered(e -> backButton.setBackground(new Background(new BackgroundFill(Color.HOTPINK, CornerRadii.EMPTY, Insets.EMPTY))));
        backButton.setOnMouseExited(e -> backButton.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY))));

        backButton.setOnAction(e -> {
            try {
                stage.setMaximized(false);
                stage.setScene(createMainScene(stage));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });


        // TITLE
        Label title = new Label("Enter Player Name");
        title.setFont(Font.font(customFont.getName(), FontWeight.BOLD, 36));
        title.setTextFill(Color.WHITE);

        // PLAYER FIELDS
        TextField[] playerFields = new TextField[4];
        VBox fieldsBox = new VBox(10);
        fieldsBox.setAlignment(Pos.CENTER);
        for (int i = 0; i < 4; i++) {
            TextField field = new TextField();
            field.setPromptText("Player " + (i + 1));
            field.setFont(Font.font(customFont.getName(), 18));
            field.setMaxWidth(300);
            playerFields[i] = field;
            fieldsBox.getChildren().add(field);
        }

        // PLAY BUTTON
        Button startGameButton = new Button("Play");
        startGameButton.setFont(Font.font(customFont.getName(), FontWeight.BOLD, 28));
        startGameButton.setTextFill(Color.WHITE);
        startGameButton.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        startGameButton.setDisable(true);

        startGameButton.setOnMouseEntered(e -> startGameButton.setBackground(new Background(new BackgroundFill(Color.HOTPINK, null, Insets.EMPTY))));
        startGameButton.setOnMouseExited(e -> startGameButton.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, Insets.EMPTY))));

        // Set the CPU fields to be non-editable
        for (int i = 1; i < 4; i++) {
            playerFields[i].setText("CPU " + i);
            playerFields[i].setEditable(false);  // Disable editing
        }

// Handle the start button action
        startGameButton.setOnAction(e -> {
            String[] names = new String[4];
            // Get the player's name from the first TextField
            names[0] = playerFields[0].getText().trim();
            // Assign the already set CPU names
            for (int i = 1; i < 4; i++) {
                names[i] = playerFields[i].getText();
            }

            createGameScene(stage, names);
        }

        );


// Enable "Play" only when the player's name field is filled
        playerFields[0].textProperty().addListener((obs, oldVal, newVal) -> {
            startGameButton.setDisable(newVal.trim().isEmpty());
        });


        // Form stack (title + fields + play button)
        VBox form = new VBox(15, title, fieldsBox, startGameButton);
        form.setAlignment(Pos.CENTER);


        // Stack GIF and UI
        StackPane stackPane = new StackPane(gifView, form);
        stackPane.setAlignment(Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setTop(topLeftBar);
        root.setCenter(stackPane);

        Scene scene = new Scene(root, 2000, 900);


        stage.setScene(scene);
        stage.setMaximized(true);
    }


    // Placeholder for switching to game scene (you'll need to implement this)
    public void createGameScene(Stage stage, String[] playerNames){
        // 1. Store current window state


        Game game = null;
        try {
            game = new Game(playerNames[0]);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        double currentWidth = stage.getWidth();
        double currentHeight = stage.getHeight();
        boolean wasMaximized = stage.isMaximized();

        // 2. Create root layout
        BorderPane root = new BorderPane();

        // 3. Create game grid with proper track numbering
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(1);
        grid.setVgap(1);
        grid.setStyle("-fx-background-color: transparent;");
        grid.setPrefSize(COLS * CELL_SIZE, ROWS * CELL_SIZE);

        StackPane[][] cellMap = new StackPane[ROWS][COLS];
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                StackPane cell = new StackPane();
                cell.setPrefSize(CELL_SIZE, CELL_SIZE);
                grid.add(cell, col, row);
                cellMap[row][col] = cell;
            }
        }

        ArrayList<Cell> track = game.getBoard().getTrack();
        ArrayList<Player> players = game.getPlayers();

        for (int i = 0; i < trackCoordinates.length && i < track.size(); i++) {
            int row = trackCoordinates[i][0];
            int col = trackCoordinates[i][1];
            Color fillColor = Color.LIGHTPINK;

            Circle circle = new Circle(MARBLE_RADIUS, fillColor);
            circle.setStroke(Color.BLACK);
            cellMap[row][col].getChildren().add(circle);
            cellMap[row][col].setUserData(track.get(i));
        }

        // Create home and safe zones (these will remain rectangular)
        createHomeZones(cellMap);
        createSafeZones(cellMap);



        // 3. Add player panels
        HBox playerPanels = createPlayerPanels(playerNames);
        root.setTop(playerPanels);

        // 4. Add card hand area
        HBox cardHand = createCardHand();
        root.setBottom(cardHand);

        // 5. Add status bar
        VBox statusBar = createStatusBar();
        root.setLeft(statusBar);

        // Create firepit display (simple text version)
        StackPane firePitDisplay = new StackPane();
        firePitDisplay.setAlignment(Pos.CENTER);
        this.firePitLabel = new Label("Firepit: Empty");
        firePitLabel.setStyle("-fx-text-fill: black; -fx-font-size: 14; -fx-font-weight: bold;");
        firePitDisplay.getChildren().add(firePitLabel);
        
        // Position firepit in center of board
        StackPane boardWithFirePit = new StackPane();
        boardWithFirePit.getChildren().addAll(grid, firePitDisplay);
        root.setCenter(boardWithFirePit);
        Scene gameScene = new Scene(root, currentWidth, currentHeight);
        stage.setScene(gameScene);
        stage.setMaximized(wasMaximized);
        stage.setTitle("Jackaroo");
    }
    private HBox createPlayerPanels(String[] playerNames) {
        HBox playerPanels = new HBox(10);
        playerPanels.setPadding(new Insets(10));
        playerPanels.setStyle("-fx-background-color: #333;");

        for (int i = 0; i < 4; i++) {
            VBox playerPanel = new VBox(5);
            playerPanel.setAlignment(Pos.CENTER);

            // Player color indicator
            Circle colorIndicator = new Circle(15, getPlayerColor(i));

            // Player name
            Label nameLabel = new Label(playerNames[i]);
            nameLabel.setStyle("-fx-text-fill: white;");

            // Marble count
            Label marbleCount = new Label("Marbles: 4");
            marbleCount.setStyle("-fx-text-fill: white;");

            // Card count
            Label cardCount = new Label("Cards: 4");
            cardCount.setStyle("-fx-text-fill: white;");

            playerPanel.getChildren().addAll(colorIndicator, nameLabel, marbleCount, cardCount);
            playerPanels.getChildren().add(playerPanel);
        }

        return playerPanels;
    }
    private static Color getColorFor(Colour colour) {
        if (colour == Colour.RED) return Color.web("#ff4d4d");
        if (colour == Colour.GREEN) return Color.web("#66cc66");
        if (colour == Colour.YELLOW) return Color.web("#ffcc00");
        if (colour == Colour.BLUE) return Color.web("#3399ff");
        return Color.LIGHTGRAY;
    }
    private Color getPlayerColor(int index) {
        return switch(index) {
            case 0 -> Color.RED;
            case 1 -> Color.GREEN;
            case 2 -> Color.BLUE;
            case 3 -> Color.YELLOW;
            default -> Color.WHITE;
        };
    }
    private HBox createCardHand() {
        HBox cardHand = new HBox(10);
        cardHand.setPadding(new Insets(10));
        cardHand.setAlignment(Pos.CENTER);
        cardHand.setStyle("-fx-background-color: #222;");

        // Sample cards - replace with actual game cards
        for (int i = 0; i < 4; i++) {
            Button cardButton = createCardButton("Card " + (i+1));
            cardHand.getChildren().add(cardButton);
        }

        return cardHand;
    }
    private Button createCardButton(String cardName) {
        Button card = new Button(cardName);
        card.setPrefSize(120, 180);
        card.setStyle("-fx-background-color: white; -fx-border-color: black;");

        card.setOnAction(e -> {
            // Handle card selection
            System.out.println("Selected: " + cardName);
        });

        return card;
    }
    private VBox createStatusBar() {
        VBox statusBar = new VBox(10);
        statusBar.setPadding(new Insets(10));
        statusBar.setStyle("-fx-background-color: #444; -fx-border-color: #666; -fx-border-width: 2;");

        // Current turn display
        HBox turnInfo = new HBox(10);
        Label currentTurnLabel = new Label("Current Turn:");
        currentTurnLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");


        turnInfo.getChildren().addAll(currentTurnLabel);

        // Next turn display
        HBox nextTurnInfo = new HBox(10);
        Label nextTurnLabel = new Label("Next Player:");
        nextTurnLabel.setStyle("-fx-text-fill: #ccc;");


        nextTurnInfo.getChildren().addAll(nextTurnLabel);




        HBox buttonPanel = new HBox(10);
        buttonPanel.setAlignment(Pos.CENTER);

        // Deselect Card button
        Button deselectCardButton = new Button("Deselect Card");
        deselectCardButton.setStyle("-fx-base: #5c6bc0;");
        deselectCardButton.setOnAction(e -> {

        });

        // Deselect Marble button
        Button deselectMarbleButton = new Button("Deselect Marble");
        deselectMarbleButton.setStyle("-fx-base: #5c6bc0;");
        deselectMarbleButton.setOnAction(e -> {

        });

        // Move button
        Button moveButton = new Button("Move");
        styleActionButton(moveButton, "#4CAF50"); // Green color


        // Swap button
        Button swapButton = new Button("Swap");
        styleActionButton(swapButton, "#2196F3"); // Blue color

        buttonPanel.getChildren().addAll(moveButton,swapButton,deselectCardButton, deselectMarbleButton);

        // Add all components to status bar
        statusBar.getChildren().addAll(
                turnInfo,
                nextTurnInfo,
                new Separator(),
                buttonPanel
        );

        return statusBar;
    }
    private void createHomeZones(StackPane[][] cellMap) {

        Color[] colors = {Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN};

        for (int player = 0; player < 4; player++) {
            for (int marble = 0; marble < 4; marble++) {
                int row = homezoneCoordinates[player][marble][0];
                int col = homezoneCoordinates[player][marble][1];

                Circle marbleCircle = new Circle(5, colors[player]);
                marbleCircle.setStroke(Color.BLACK);

                StackPane cell = cellMap[row][col];
                cell.setStyle("-fx-background-color: #e0e0e0; -fx-border-color: #a0a0a0; -fx-border-width: 1;");
                cell.getChildren().add(marbleCircle);
            }
        }
    }
    private void createSafeZones(StackPane[][] cellMap) {
        Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW};

        for (int player = 0; player < 4; player++) {
            for (int cell = 0; cell < 4; cell++) {
                int col = safezoneCoordinates[player][cell][0];
                int row = safezoneCoordinates[player][cell][1];

                StackPane safeCell = cellMap[row][col];
                safeCell.getChildren().clear(); // Clear any existing nodes

                // Create circular background
                Circle circle = new Circle(CELL_SIZE/2.0); // Slightly smaller than cell
                circle.setFill(Color.web("#f0f8ff"));  // Light blue fill
                circle.setStroke(Color.web("#4682b4")); // Steel blue border
                circle.setStrokeWidth(1);


                // Add to the cell
                safeCell.getChildren().add(circle);
            }
        }
    }
    private void styleActionButton(Button button, String color) {
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-weight: bold;");
        button.setPadding(new Insets(5, 15, 5, 15));
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: derive(" + color + ", 20%); -fx-text-fill: white;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white;"));
    }
    public void onShortcutButtonClicked() {
        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: #191970;");


        Font customFont = Font.loadFont(getClass().getResourceAsStream("/font/PublicPixel-rv0pA.ttf"), 16);

        Label title = new Label("Shortcut Keys");
        title.setFont(customFont);
        title.setTextFill(Color.WHITE);

        Label w = new Label("CTRL & F4 - To Field Marble");
        w.setFont(Font.font(customFont.getName(), FontWeight.BOLD, 28));
        w.setTextFill(Color.WHITE);

        ImageView leftIcon = new ImageView(new Image(getClass().getResourceAsStream("/CHB.png")));
        leftIcon.setFitWidth(24);
        leftIcon.setFitHeight(24);

        ImageView rightIcon = new ImageView(new Image(getClass().getResourceAsStream("/CHB.png")));
        rightIcon.setFitWidth(24);
        rightIcon.setFitHeight(24);

        HBox labeledRow = new HBox(10, leftIcon, w, rightIcon);
        labeledRow.setAlignment(Pos.CENTER);

        root.getChildren().addAll(title, labeledRow);

        Scene scene = new Scene(root, 900, 250);

        Stage stage = new Stage();
        stage.setTitle("Shortcuts");
        stage.getIcons().add(new Image(getClass().getResource("/Images/r.png").toExternalForm()));

        stage.setScene(scene);
        stage.show();
    }

}