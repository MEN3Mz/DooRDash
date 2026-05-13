package game.view;

import game.engine.Game;
import game.engine.monsters.Monster;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
    private final Label playerNameLabel;
    private final Label playerRoleLabel;
    private final Label playerEnergyLabel;
    private final Label opponentNameLabel;
    private final Label opponentRoleLabel;
    private final Label opponentEnergyLabel;
    private final Label cellInfoLabel;
    private final Label diceInfoLabel;

    private final BottomView bottomView;
    private final Button menuButton;

    public GameView(Game game) {

        this.game = game;
        this.boardView = new GameBoardView(game);
        bottomView = new BottomView();

        root = new BorderPane();
        mainRoot = new StackPane();

        ImageView backgroundView = createBackgroundView();

        // Background first, UI second
        mainRoot.getChildren().addAll(backgroundView, root);

        this.currentPlayerLabel = new Label();
        this.playerNameLabel = new Label();
        this.playerRoleLabel = new Label();
        this.playerEnergyLabel = new Label();
        this.opponentNameLabel = new Label();
        this.opponentRoleLabel = new Label();
        this.opponentEnergyLabel = new Label();
        this.cellInfoLabel = new Label("Cell: none selected");
        this.diceInfoLabel = new Label("Dice: not rolled");
        this.menuButton = new Button("Menu");

        buildLayout();
        refresh();
    }

    public void setOnMenu(javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        menuButton.setOnAction(handler);
    }

    private void buildLayout() {

        root.setPadding(new Insets(16));

        VBox leftPanel = createSidePanel("Player", playerNameLabel, playerRoleLabel, playerEnergyLabel);
        VBox rightPanel = createSidePanel("Opponent", opponentNameLabel, opponentRoleLabel, opponentEnergyLabel);
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

        BorderPane.setAlignment(bottomPanel, Pos.CENTER);

        applyPanelStyle(leftPanel);
        applyPanelStyle(rightPanel);
        applyPanelStyle(topPanel);
        applyPanelStyle(bottomPanel);
    }

    private VBox createSidePanel(String title, Label nameLabel, Label roleLabel, Label energyLabel) {
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        VBox panel = new VBox(12, titleLabel, nameLabel, roleLabel, energyLabel);
        panel.setPrefWidth(220);
        panel.setAlignment(Pos.TOP_LEFT);
        panel.setPadding(new Insets(16));
        return panel;
    }

    private BorderPane createTopPanel() {

        currentPlayerLabel.setStyle("""
                -fx-font-size: 22px;
                -fx-font-weight: bold;
                -fx-text-fill: white;
                """);

        panelStyles(menuButton);

        HBox leftBox = new HBox(menuButton);
        leftBox.setAlignment(Pos.CENTER_LEFT);
        leftBox.setMinWidth(160);

        HBox centerBox = new HBox(currentPlayerLabel);
        centerBox.setAlignment(Pos.CENTER);

        BorderPane panel = new BorderPane();

        panel.setLeft(leftBox);
        panel.setCenter(centerBox);

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

    private void panelStyles(Button button) {

        button.setStyle("""
                -fx-background-color:
                    linear-gradient(#70b1ff 0%, #1a5cad 50%, #0a3b75 51%, #114b91 100%);

                -fx-text-fill: white;
                -fx-font-size: 15px;
                -fx-font-weight: bold;

                -fx-padding: 10 22 10 22;

                -fx-background-radius: 12;
                -fx-border-radius: 12;

                -fx-border-color: rgba(255,255,255,0.18);
                """);
    }

    private HBox createBottomPanel() {

        HBox panel = new HBox(
                15,
                bottomView.getRoot());

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
        updateMonsterInfo(game.getPlayer(), playerNameLabel, playerRoleLabel, playerEnergyLabel);
        updateMonsterInfo(game.getOpponent(), opponentNameLabel, opponentRoleLabel, opponentEnergyLabel);
        currentPlayerLabel.setText("Current Turn: " + game.getCurrent().getName());
    }

    private void updateMonsterInfo(Monster monster, Label nameLabel, Label roleLabel, Label energyLabel) {
        nameLabel.setText("Name: " + monster.getName());
        roleLabel.setText("Role: " + monster.getRole());
        energyLabel.setText("Energy: " + monster.getEnergy());
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
        return bottomView;
    }

    public GameBoardView getBoardView() {
        return boardView;
    }

}