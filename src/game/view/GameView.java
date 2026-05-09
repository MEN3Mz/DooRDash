package game.view;

import game.engine.Game;
import game.engine.monsters.Monster;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class GameView {
    private final Game game;
    private final GameBoardView boardView;
    private final BorderPane root;

    private final Label currentPlayerLabel;
    private final Label playerNameLabel;
    private final Label playerRoleLabel;
    private final Label playerEnergyLabel;
    private final Label opponentNameLabel;
    private final Label opponentRoleLabel;
    private final Label opponentEnergyLabel;
    private final Label cellInfoLabel;
    private final Label diceInfoLabel;

    public GameView(Game game) {
        this.game = game;
        this.boardView = new GameBoardView(game);
        this.root = new BorderPane();

        this.currentPlayerLabel = new Label();
        this.playerNameLabel = new Label();
        this.playerRoleLabel = new Label();
        this.playerEnergyLabel = new Label();
        this.opponentNameLabel = new Label();
        this.opponentRoleLabel = new Label();
        this.opponentEnergyLabel = new Label();
        this.cellInfoLabel = new Label("Cell: none selected");
        this.diceInfoLabel = new Label("Dice: not rolled");

        buildLayout();
        refresh();
    }

    private void buildLayout() {
        root.setPadding(new Insets(16));

        VBox leftPanel = createSidePanel("Player", playerNameLabel, playerRoleLabel, playerEnergyLabel);
        VBox rightPanel = createSidePanel("Opponent", opponentNameLabel, opponentRoleLabel, opponentEnergyLabel);
        HBox topPanel = createTopPanel();
        HBox bottomPanel = createBottomPanel();
        StackPane centerPanel = new StackPane(boardView.getBoardRoot());

        centerPanel.setPadding(new Insets(10));
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

    private HBox createTopPanel() {
        HBox panel = new HBox(currentPlayerLabel);
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(12));
        currentPlayerLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        return panel;
    }

    private HBox createBottomPanel() {
        HBox panel = new HBox(30, cellInfoLabel, diceInfoLabel);
        panel.setAlignment(Pos.CENTER_LEFT);
        panel.setPadding(new Insets(12));
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

    public BorderPane getRoot() {
        return root;
    }
}
