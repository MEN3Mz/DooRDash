package game.view;

import game.engine.Game;
import game.engine.Constants;
import game.audio.SoundManager;
import game.engine.monsters.Dasher;
import game.engine.monsters.MultiTasker;
import game.engine.monsters.Monster;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class GameView {

    private final Game game;
    private final GameBoardView boardView;
    private final String playerOneName;
    private final String playerTwoName;

    private final BorderPane root;
    private final StackPane mainRoot;

    private final Label currentPlayerLabel;
    private final Label cellInfoLabel;
    private final Label diceInfoLabel;

    private final MonsterInfoPane playerPane;
    private final MonsterInfoPane opponentPane;
    private final TurnLogView playerTurnLogView;
    private final TurnLogView opponentTurnLogView;

    private final ImageView playerDoorView;
    private final ImageView opponentDoorView;

    private final BottomView bottomView;
    private final Button menuButton;
    private final Button testWinButton;

    public GameView(Game game) {
        this(game, "You", "Opponent");
    }

    public GameView(Game game, String playerOneName, String playerTwoName) {

        this.game = game;
        this.playerOneName = playerOneName;
        this.playerTwoName = playerTwoName;
        this.boardView = new GameBoardView(game, playerOneName, playerTwoName);

        this.bottomView = new BottomView();

        root = new BorderPane();
        mainRoot = new StackPane();

        ImageView backgroundView = createBackgroundView();

        // Background first, UI second
        mainRoot.getChildren().addAll(backgroundView, root);

        this.currentPlayerLabel = new Label();

        this.cellInfoLabel = new Label("Cell: none selected");
        this.diceInfoLabel = new Label("Dice: not rolled");

        this.playerPane = new MonsterInfoPane();
        this.opponentPane = new MonsterInfoPane();
        this.playerTurnLogView = new TurnLogView(playerOneName + " Latest Moves");
        this.opponentTurnLogView = new TurnLogView(playerTwoName + " Latest Moves");

        this.playerDoorView = new ImageView();
        this.opponentDoorView = new ImageView();

        setupDoor(playerDoorView);
        setupDoor(opponentDoorView);

        this.menuButton = new Button();
        menuButton.getStyleClass().add("game-menu-button");
        menuButton.setOnMousePressed(e -> SoundManager.playButtonSound());

        this.testWinButton = new Button("Win Test");
        testWinButton.setStyle("""
                -fx-background-color: #b91c1c;
                -fx-background-radius: 10;
                -fx-text-fill: white;
                -fx-font-size: 14px;
                -fx-font-weight: bold;
                -fx-padding: 10 18;
                -fx-border-color: rgba(255,255,255,0.28);
                -fx-border-radius: 10;
                """);
        testWinButton.setOnMousePressed(e -> SoundManager.playButtonSound());

        buildLayout();

        // Load CSS
        mainRoot.getStylesheets().add(
                getClass()
                        .getResource("/game/assets/css/buttons.css")
                        .toExternalForm());

        refresh();
    }

    public void setOnMenu(javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        menuButton.setOnAction(handler);
    }

    public void setOnWinTest(javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        testWinButton.setOnAction(handler);
    }

    public void setGameControlsDisabled(boolean disabled) {
        bottomView.setControlsDisabled(disabled);
        testWinButton.setDisable(disabled);
    }

    private void buildLayout() {

        root.setPadding(new Insets(16));

        HBox leftPanel = createLeftSideContainer();

        HBox rightPanel = createRightSideContainer();

        BorderPane topPanel = createTopPanel();
        HBox bottomPanel = createBottomPanel();

        StackPane centerPanel = new StackPane(boardView.getBoardRoot());

        centerPanel.setPadding(new Insets(10));
        centerPanel.setAlignment(Pos.CENTER);
        centerPanel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        boardView.getBoardRoot().setMaxSize(
                Region.USE_PREF_SIZE,
                Region.USE_PREF_SIZE);

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

        applyPanelStyle(topPanel);
        applyPanelStyle(bottomPanel);
    }

    private BorderPane createTopPanel() {

        currentPlayerLabel.setStyle("""
                -fx-font-size: 22px;
                -fx-font-weight: bold;
                -fx-text-fill: white;
                """);

        HBox leftBox = new HBox(menuButton);
        leftBox.setAlignment(Pos.CENTER_LEFT);
        leftBox.setMinWidth(220);

        HBox centerBox = new HBox(currentPlayerLabel);
        centerBox.setAlignment(Pos.CENTER);

        HBox rightBox = new HBox(testWinButton);
        rightBox.setAlignment(Pos.CENTER_RIGHT);
        rightBox.setMinWidth(220);

        BorderPane panel = new BorderPane();

        panel.setLeft(leftBox);
        panel.setCenter(centerBox);
        panel.setRight(rightBox);

        panel.setPadding(new Insets(14));

        panel.setPrefHeight(80);

        panel.setStyle("""
                -fx-background-color:
                    linear-gradient(to bottom,
                    rgba(31,41,51,0.92),
                    rgba(62,76,89,0.92));

                -fx-background-radius: 18;

                -fx-border-color: rgba(255,255,255,0.18);
                -fx-border-width: 2;
                -fx-border-radius: 18;
                """);

        return panel;
    }

    private HBox createBottomPanel() {

        HBox panel = new HBox(
                15,
                bottomView.getRoot());

        panel.setAlignment(Pos.TOP_CENTER);
        panel.setPadding(Insets.EMPTY);

        panel.setMinSize(
                HBox.USE_PREF_SIZE,
                HBox.USE_PREF_SIZE);

        panel.setMaxSize(
                HBox.USE_PREF_SIZE,
                HBox.USE_PREF_SIZE);

        return panel;
    }

    private void applyPanelStyle(javafx.scene.layout.Region panel) {

        panel.setStyle("""
                -fx-background-color:
                    linear-gradient(to bottom, #1f2933, #3e4c59);

                -fx-background-radius: 14;

                -fx-border-color: #bcccdc;
                -fx-border-width: 2;
                -fx-border-radius: 14;
                """);
    }

    public void refresh() {

        boardView.refreshBoard();

        Monster player = game.getPlayer();
        Monster opponent = game.getOpponent();

        playerPane.updateUI(
                player.getName(),
                player.getClass().getSimpleName(),
                player.getOriginalRole().toString(),
                player.getRole().toString(),
                player.getEnergy(),
                player.getPosition(),
                player.isShielded(),
                player.isFrozen(),
                player.isPoweredUpActivated(),
                player.isConfused(),
                player.isShielded() ? 1 : 0,
                player.isFrozen() ? 1 : 0,
                getPowerUpTurns(player),
                player.getConfusionTurns());

        opponentPane.updateUI(
                opponent.getName(),
                opponent.getClass().getSimpleName(),
                opponent.getOriginalRole().toString(),
                opponent.getRole().toString(),
                opponent.getEnergy(),
                opponent.getPosition(),
                opponent.isShielded(),
                opponent.isFrozen(),
                opponent.isPoweredUpActivated(),
                opponent.isConfused(),
                opponent.isShielded() ? 1 : 0,
                opponent.isFrozen() ? 1 : 0,
                getPowerUpTurns(opponent),
                opponent.getConfusionTurns());

        playerTurnLogView.updateEvents(game.getPlayerEventLog());
        opponentTurnLogView.updateEvents(game.getOpponentEventLog());

        currentPlayerLabel.setText(
                "Current Turn: " + getCurrentPlayerName() + " - " + game.getCurrent().getName());

        bottomView.setPowerUpAvailable(
                game.getCurrent().getEnergy() >= Constants.POWERUP_COST);

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

        ImageView backgroundView = new ImageView(
                loadImage("/game/assets/Background.png"));

        backgroundView.setPreserveRatio(false);

        // Bind background to full window
        backgroundView.fitWidthProperty().bind(mainRoot.widthProperty());
        backgroundView.fitHeightProperty().bind(mainRoot.heightProperty());

        return backgroundView;
    }

    private Image loadImage(String resourcePath) {
        return new Image(getClass().getResourceAsStream(resourcePath));
    }

    public BottomView getBottomView() {
        return bottomView;
    }

    public GameBoardView getBoardView() {
        return boardView;
    }

    private VBox createPlayerInfoColumn(
            MonsterInfoPane infoPane,
            ImageView doorView) {

        VBox container = new VBox(54);

        container.getChildren().addAll(
                infoPane,
                doorView);

        container.setAlignment(Pos.TOP_CENTER);

        container.setPrefWidth(240);
        container.setFillWidth(false);
        applyPanelStyle(container);
        VBox.setMargin(doorView, new Insets(18, 0, 0, 0));

        return container;
    }

    private HBox createLeftSideContainer() {
        VBox playerInfoColumn = createPlayerInfoColumn(playerPane, playerDoorView);

        HBox container = new HBox(12, playerInfoColumn, playerTurnLogView);
        container.setAlignment(Pos.TOP_CENTER);
        container.setPrefWidth(442);
        container.setFillHeight(false);
        container.setStyle("-fx-background-color: transparent;");

        return container;
    }

    private HBox createRightSideContainer() {
        VBox opponentInfoColumn = createPlayerInfoColumn(opponentPane, opponentDoorView);

        HBox container = new HBox(12, opponentTurnLogView, opponentInfoColumn);
        container.setAlignment(Pos.TOP_CENTER);
        container.setPrefWidth(442);
        container.setFillHeight(false);
        container.setStyle("-fx-background-color: transparent;");

        return container;
    }

    private void setupDoor(ImageView doorView) {

        doorView.setFitWidth(120);
        doorView.setFitHeight(120);
        doorView.setPreserveRatio(true);
    }

    private void updateDoorImage(
            ImageView doorView,
            Monster monster) {

        String path;

        if (monster.getRole().toString().equals("LAUGHER")) {
            path = "/game/assets/Doors/blueDoor.png";
        } else {
            path = "/game/assets/Doors/redDoor.png";
        }

        doorView.setImage(loadImage(path));
    }

    private String getCurrentPlayerName() {
        return game.getCurrent() == game.getPlayer() ? playerOneName : playerTwoName;
    }

    private int getPowerUpTurns(Monster monster) {
        if (monster instanceof Dasher) {
            return ((Dasher) monster).getMomentumTurns();
        }

        if (monster instanceof MultiTasker) {
            return ((MultiTasker) monster).getNormalSpeedTurns();
        }

        return monster.isPoweredUpActivated() ? 1 : 0;
    }
}
