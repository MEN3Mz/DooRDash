package game.view;

import game.engine.Game;
import game.engine.monsters.Monster;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GameView {
    private final Game game;
    private final GameBoardView boardView;
    private final BorderPane root;
    private final StackPane mainRoot;

    private final Label currentPlayerLabel;

    private final Label cellInfoLabel;
    private final Label diceInfoLabel;
    private final MonsterInfoPane playerInfoPane;
    private final MonsterInfoPane opponentInfoPane;

    private final ImageView playerDoorView;
    private final ImageView opponentDoorView;

    private final BottomView buttomView;

    public GameView(Game game) {

        this.game = game;
        this.boardView = new GameBoardView(game);
        buttomView = new BottomView();

        root = new BorderPane();
        mainRoot = new StackPane();

        ImageView backgroundView = createBackgroundView();

        // Background first, UI second
        mainRoot.getChildren().addAll(backgroundView, root);

        this.currentPlayerLabel = new Label();

        this.cellInfoLabel = new Label("Cell: none selected");
        this.diceInfoLabel = new Label("Dice: not rolled");
        playerInfoPane = new MonsterInfoPane();
        opponentInfoPane = new MonsterInfoPane();
        playerDoorView = new ImageView();
        opponentDoorView = new ImageView();

        setupDoor(playerDoorView);
        setupDoor(opponentDoorView);

        buildLayout();
        refresh();
    }

    private void buildLayout() {

        root.setPadding(new Insets(16));

        VBox leftPanel = createSideContainer(
                playerInfoPane,
                playerDoorView);

        VBox rightPanel = createSideContainer(
                opponentInfoPane,
                opponentDoorView);
        BorderPane topPanel = createTopPanel();
        HBox bottomPanel = createBottomPanel();
        StackPane centerPanel = new StackPane(boardView.getBoardRoot());

        centerPanel.setPadding(new Insets(10));
        centerPanel.setAlignment(Pos.CENTER);
        centerPanel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        boardView.getBoardRoot().setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        StackPane.setAlignment(boardView.getBoardRoot(), Pos.CENTER);

        root.setTop(topPanel);
        root.setLeft(leftPanel);
        root.setCenter(centerPanel);
        root.setRight(rightPanel);
        root.setBottom(bottomPanel);

        BorderPane.setMargin(topPanel, new Insets(0, 0, 16, 0));
        BorderPane.setMargin(bottomPanel, new Insets(16, 0, 0, 0));
        BorderPane.setMargin(leftPanel, new Insets(0, 16, 0, 0));
        BorderPane.setMargin(rightPanel, new Insets(0, 0, 0, 16));
        BorderPane.setAlignment(centerPanel, Pos.CENTER);
        BorderPane.setAlignment(leftPanel, Pos.CENTER_RIGHT);
        BorderPane.setAlignment(rightPanel, Pos.CENTER_LEFT);
        BorderPane.setAlignment(bottomPanel, Pos.CENTER);

        applyPanelStyle(leftPanel);
        applyPanelStyle(rightPanel);
        applyPanelStyle(topPanel);
        applyPanelStyle(bottomPanel);
    }

    private BorderPane createTopPanel() {

        Button menuButton = new Button("Menu");

        HBox left = new HBox(menuButton);
        left.setAlignment(Pos.CENTER_LEFT);

        HBox center = new HBox(currentPlayerLabel);
        center.setAlignment(Pos.CENTER);

        BorderPane panel = new BorderPane();
        panel.setLeft(left);
        panel.setCenter(center);

        panel.setPadding(new Insets(12));

        currentPlayerLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        panel.getStylesheets().add(
                getClass().getResource("/game/assets/css/buttons.css").toExternalForm());
        menuButton.getStyleClass().add("menu-button");

        return panel;
    }

    private HBox createBottomPanel() {

        HBox panel = new HBox(
                15,
                buttomView.getRoot());

        panel.setAlignment(Pos.TOP_CENTER);
        panel.setPadding(javafx.geometry.Insets.EMPTY);
        panel.setMinSize(HBox.USE_PREF_SIZE, HBox.USE_PREF_SIZE);
        panel.setMaxSize(HBox.USE_PREF_SIZE, HBox.USE_PREF_SIZE);

        return panel;
    }

    private void applyPanelStyle(javafx.scene.layout.Region panel) {
        panel.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #1f2933, #3e4c59);"
                        + "-fx-background-radius: 14;"
                        + "-fx-border-color: #bcccdc;"
                        + "-fx-border-width: 2;"
                        + "-fx-border-radius: 14;");
    }

    public void refresh() {

        boardView.refreshBoard();

        Monster player = game.getPlayer();
        Monster opponent = game.getOpponent();

        playerInfoPane.updateUI(
                player.getName(),
                player.getClass().getSimpleName(),
                player.getOriginalRole().toString(),
                player.getRole().toString(),
                player.getEnergy(),
                player.getPosition(),
                player.isShielded(),
                player.isFrozen(), player.isPoweredUpActivated(), player.isConfused());

        opponentInfoPane.updateUI(
                opponent.getName(),
                opponent.getClass().getSimpleName(),
                opponent.getOriginalRole().toString(),
                opponent.getRole().toString(),
                opponent.getEnergy(),
                opponent.getPosition(),
                opponent.isShielded(),
                opponent.isFrozen(), opponent.isPoweredUpActivated(), opponent.isConfused());

        currentPlayerLabel.setText(
                "Current Turn: " + game.getCurrent().getName());
        updateDoorImage(playerDoorView, player);
        updateDoorImage(opponentDoorView, opponent);
    }

    public void setCellInfo(String text) {
        cellInfoLabel.setText(text);
    }

    public void setDiceInfo(String text) {
        diceInfoLabel.setText(text);
    }

    public StackPane getRoot() {
        return mainRoot;
    }

    private ImageView createBackgroundView() {
        ImageView backgroundView = new ImageView(loadImage("/game/assets/Background.png"));

        backgroundView.setPreserveRatio(false);

        // Bind to FULL container
        backgroundView.fitWidthProperty().bind(mainRoot.widthProperty());
        backgroundView.fitHeightProperty().bind(mainRoot.heightProperty());

        return backgroundView;
    }

    private Image loadImage(String resourcePath) {
        return new Image(getClass().getResourceAsStream(resourcePath));
    }

    public BottomView getBottomView() {
        return buttomView;
    }

    public GameBoardView getBoardView() {
        return boardView;
    }

    private VBox createSideContainer(MonsterInfoPane infoPane, ImageView doorView) {

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox container = new VBox(20);

        container.getChildren().addAll(
                infoPane,
                spacer,
                doorView);

        container.setAlignment(Pos.TOP_CENTER);

        container.setPrefWidth(240);
        container.setFillWidth(false);

        return container;
    }

    private void setupDoor(ImageView doorView) {
        doorView.setFitWidth(120);
        doorView.setFitHeight(120);
        doorView.setPreserveRatio(true);
    }

    private void updateDoorImage(ImageView doorView, Monster monster) {

        String path;

        if (monster.getRole().toString().equals("LAUGHER")) {
            path = "/game/assets/Doors/blueDoor.png";
        } else {
            path = "/game/assets/Doors/redDoor.png";
        }

        doorView.setImage(loadImage(path));
    }
}