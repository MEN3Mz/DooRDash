package game.view;

import java.util.HashMap;
import java.util.Map;

import game.engine.Game;
import game.engine.Board;
import game.engine.Role;
import game.audio.SoundManager;
import game.engine.monsters.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class GameView {
    private static final double DESIGN_WIDTH = 2140;
    private static final double DESIGN_HEIGHT = 1080;

    private final Game game;
    private final GameBoardView boardView;
    private final String playerOneName;
    private final String playerTwoName;

    private final BorderPane root;
    private final StackPane mainRoot;
    private final StackPane uiLayer;

    private final Label currentPlayerLabel;

    private final MonsterInfoPane playerPane;
    private final MonsterInfoPane opponentPane;
    private final Label playerHeaderLabel;
    private final Label opponentHeaderLabel;
    private final TurnLogView playerTurnLogView;
    private final TurnLogView opponentTurnLogView;
    private final ImageView[] cardDeckRearViews;
    private final Label cardDeckCountLabel;

    private final ImageView playerDoorView;
    private final ImageView opponentDoorView;
    private final Label playerTeamLabel;
    private final Label opponentTeamLabel;
    private final HBox playerStationedMonsterBox;
    private final HBox opponentStationedMonsterBox;
    private final ImageView playerSidePanelImage;
    private final ImageView opponentSidePanelImage;
    private HBox playerSideContainer;
    private HBox opponentSideContainer;
    private Timeline confusionShake;

    private Label playerMonsterTypeLabel;
    private Label playerPowerUpInfoLabel;
    private Label playerPassiveTraitLabel;

    private Label opponentMonsterTypeLabel;
    private Label opponentPowerUpInfoLabel;
    private Label opponentPassiveTraitLabel;
    private final Map<Monster, Integer> stationedMonsterPreviousEnergies;

    private final BottomView bottomView;
    private final Button menuButton;

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
        uiLayer = new StackPane(root);
        setupResponsiveGameScale();

        ImageView backgroundView = createBackgroundView();

        // Background first, UI second
        mainRoot.getChildren().addAll(backgroundView, uiLayer);

        this.currentPlayerLabel = new Label();

        this.playerPane = new MonsterInfoPane();
        this.opponentPane = new MonsterInfoPane();
        this.playerHeaderLabel = createMonsterHeader();
        this.opponentHeaderLabel = createMonsterHeader();
        this.playerTurnLogView = new TurnLogView(playerOneName + " Latest Moves");
        this.opponentTurnLogView = new TurnLogView(playerTwoName + " Latest Moves");
        this.cardDeckRearViews = new ImageView[4];
        this.cardDeckCountLabel = new Label();

        this.playerDoorView = new ImageView();
        this.opponentDoorView = new ImageView();
        this.playerTeamLabel = createTeamLabel();
        this.opponentTeamLabel = createTeamLabel();
        this.playerStationedMonsterBox = createStationedMonsterBox();
        this.opponentStationedMonsterBox = createStationedMonsterBox();
        this.playerSidePanelImage = new ImageView();
        this.opponentSidePanelImage = new ImageView();
        this.stationedMonsterPreviousEnergies = new HashMap<>();

        playerMonsterTypeLabel = createMonsterDetailLabel();
        playerPowerUpInfoLabel = createMonsterDetailLabel();
        playerPassiveTraitLabel = createMonsterDetailLabel();
        opponentMonsterTypeLabel = createMonsterDetailLabel();
        opponentPowerUpInfoLabel = createMonsterDetailLabel();
        opponentPassiveTraitLabel = createMonsterDetailLabel();

        Monster player = game.getPlayer();
        Monster opponent = game.getOpponent();
        updateMonsterDetails(player, playerMonsterTypeLabel, playerPassiveTraitLabel, playerPowerUpInfoLabel);
        updateMonsterDetails(opponent, opponentMonsterTypeLabel, opponentPassiveTraitLabel, opponentPowerUpInfoLabel);

        setupDoor(playerDoorView);
        setupDoor(opponentDoorView);

        this.menuButton = new Button();
        menuButton.getStyleClass().add("game-menu-button");
        menuButton.setOnMousePressed(e -> SoundManager.playButtonSound());

        buildLayout();
        addMenuPanel();

        // Load CSS
        mainRoot.getStylesheets().add(
                ThemeManager.loadStylesheet("/game/assets/css/buttons.css"));

        refresh();
    }

    public void setOnMenu(javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        menuButton.setOnAction(handler);
    }

    public void setGameControlsDisabled(boolean disabled) {
        bottomView.setControlsDisabled(disabled);
    }

    private void buildLayout() {

        root.setMinSize(DESIGN_WIDTH, DESIGN_HEIGHT);
        root.setPrefSize(DESIGN_WIDTH, DESIGN_HEIGHT);
        root.setMaxSize(DESIGN_WIDTH, DESIGN_HEIGHT);
        root.setPadding(new Insets(12));

        HBox leftPanel = createLeftSideContainer();

        HBox rightPanel = createRightSideContainer();

        StackPane topPanel = createTopPanel();
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

        BorderPane.setMargin(topPanel, new Insets(26, 0, 8, 0));
        BorderPane.setMargin(bottomPanel, new Insets(16, 0, 0, 0));
        BorderPane.setMargin(leftPanel, new Insets(0, 6, 0, 0));
        BorderPane.setMargin(rightPanel, new Insets(0, 0, 0, 6));

        BorderPane.setAlignment(centerPanel, Pos.CENTER);
        BorderPane.setAlignment(leftPanel, Pos.CENTER_RIGHT);
        BorderPane.setAlignment(rightPanel, Pos.CENTER_LEFT);
        BorderPane.setAlignment(bottomPanel, Pos.CENTER);

        applyPanelStyle(bottomPanel);
    }

    private void addMenuPanel() {
        ImageView panelImage = new ImageView(loadImage("/game/assets/panels/menuPanel.png"));
        panelImage.setFitWidth(80);
        panelImage.setFitHeight(80);
        panelImage.setPreserveRatio(true);
        panelImage.setSmooth(true);

        StackPane menuPanel = new StackPane(panelImage, menuButton);
        menuPanel.setPrefSize(80, 80);
        menuPanel.setMinSize(80, 80);
        menuPanel.setMaxSize(80, 80);
        menuPanel.setPickOnBounds(false);

        uiLayer.getChildren().add(menuPanel);
        StackPane.setAlignment(menuPanel, Pos.TOP_LEFT);
        StackPane.setMargin(menuPanel, new Insets(12, 0, 0, 175));

        HBox cardDeckIndicator = createCardDeckIndicator();
        uiLayer.getChildren().add(cardDeckIndicator);
        StackPane.setAlignment(cardDeckIndicator, Pos.BOTTOM_LEFT);
        StackPane.setMargin(cardDeckIndicator, new Insets(0, 0, 26, 150));
    }

    private void setupResponsiveGameScale() {
        uiLayer.setMinSize(DESIGN_WIDTH, DESIGN_HEIGHT);
        uiLayer.setPrefSize(DESIGN_WIDTH, DESIGN_HEIGHT);
        uiLayer.setMaxSize(DESIGN_WIDTH, DESIGN_HEIGHT);

        DoubleBinding fitScale = Bindings.min(
                Bindings.min(
                        mainRoot.widthProperty().divide(DESIGN_WIDTH),
                        mainRoot.heightProperty().divide(DESIGN_HEIGHT)),
                1.0);

        uiLayer.scaleXProperty().bind(fitScale);
        uiLayer.scaleYProperty().bind(fitScale);
        StackPane.setAlignment(uiLayer, Pos.CENTER);
    }

    private StackPane createTopPanel() {

        currentPlayerLabel.setStyle("""
                -fx-font-size: %spx;
                -fx-font-weight: bold;
                -fx-text-fill: white;
                """.formatted(ThemeManager.isRetro() ? 16 : 22)
                + ThemeManager.getThemeFontInlineStyle() + getRetroNeonTextStyle());
        currentPlayerLabel.setEffect(getTextEffect(4));
        HBox content = new HBox(currentPlayerLabel);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(6, 22, 0, 22));
        content.setPrefWidth(650);
        content.setMaxWidth(650);

        currentPlayerLabel.setMaxWidth(650 - 44);
        currentPlayerLabel.setAlignment(Pos.CENTER);

        ImageView panelImage = new ImageView(loadImage("/game/assets/panels/top panel.png"));
        panelImage.setPreserveRatio(false);
        panelImage.setFitWidth(650);
        panelImage.setFitHeight(150);
        panelImage.setSmooth(true);

        StackPane panel = new StackPane(panelImage, content);
        panel.setPrefHeight(54);
        panel.setMinHeight(54);
        panel.setMaxHeight(54);

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

        panel.setStyle(ThemeManager.getBottomPanelStyle());
    }

    public void refresh() {

        boardView.refreshBoard();

        Monster player = game.getPlayer();
        Monster opponent = game.getOpponent();
        playerHeaderLabel.setText(player.getName() + "\nOriginal Role: " + player.getOriginalRole());
        opponentHeaderLabel.setText(opponent.getName() + "\nOriginal Role: " + opponent.getOriginalRole());
        updateMonsterDetails(player, playerMonsterTypeLabel, playerPassiveTraitLabel, playerPowerUpInfoLabel);
        updateMonsterDetails(opponent, opponentMonsterTypeLabel, opponentPassiveTraitLabel, opponentPowerUpInfoLabel);

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
        updateCardDeckIndicator();

        currentPlayerLabel.setText(
                "Current Turn: " + getCurrentPlayerName() + " - " + game.getCurrent().getName());

        bottomView.setPowerUpAvailable(game.canCurrentUsePowerup());

        updateDoorImage(playerDoorView, player);
        updateDoorImage(opponentDoorView, opponent);
        updateSidePanelImage(playerSidePanelImage, player);
        updateSidePanelImage(opponentSidePanelImage, opponent);
        updateTeamSection(playerTeamLabel, playerStationedMonsterBox, player.getRole());
        updateTeamSection(opponentTeamLabel, opponentStationedMonsterBox, opponent.getRole());
        updateSidePanelFocus();
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
        return ThemeManager.loadImage(resourcePath);
    }

    public BottomView getBottomView() {
        return bottomView;
    }

    public GameBoardView getBoardView() {
        return boardView;
    }

    public void playConfusionShake() {
        if (confusionShake != null) {
            confusionShake.stop();
        }

        confusionShake = new Timeline(
                new KeyFrame(Duration.ZERO, event -> setConfusionShakeOffset(0)),
                new KeyFrame(Duration.millis(45), event -> setConfusionShakeOffset(-9)),
                new KeyFrame(Duration.millis(90), event -> setConfusionShakeOffset(9)),
                new KeyFrame(Duration.millis(135), event -> setConfusionShakeOffset(-7)),
                new KeyFrame(Duration.millis(180), event -> setConfusionShakeOffset(7)),
                new KeyFrame(Duration.millis(225), event -> setConfusionShakeOffset(-4)),
                new KeyFrame(Duration.millis(270), event -> setConfusionShakeOffset(4)),
                new KeyFrame(Duration.millis(315), event -> setConfusionShakeOffset(0)));
        confusionShake.play();
    }

    private void setConfusionShakeOffset(double offset) {
        setNodeShakeOffset(boardView.getBoardRoot(), offset);
        setNodeShakeOffset(playerSideContainer, offset);
        setNodeShakeOffset(opponentSideContainer, -offset);
    }

    private void setNodeShakeOffset(Node node, double offset) {
        if (node != null) {
            node.setTranslateX(offset);
        }
    }

    private StackPane createPlayerInfoColumn(
            Label headerLabel,
            MonsterInfoPane infoPane,
            ImageView doorView,
            Label teamLabel,
            HBox stationedMonsterBox,
            ImageView sidePanelImage,
            Label MonsterTypeLabel, Label PowerUpInfoLabel, Label PassiveTraitLabel) {

        VBox content = new VBox(4);
        content.setPadding(new Insets(ThemeManager.isRetro() ? 60 : 54, 0, 28, 0));

        content.getChildren().addAll(
                headerLabel,
                infoPane,
                doorView,
                teamLabel,
                stationedMonsterBox,
                MonsterTypeLabel, PassiveTraitLabel,
                PowerUpInfoLabel);

        content.setAlignment(Pos.TOP_CENTER);
        content.setPrefWidth(430);
        content.setMaxWidth(430);
        content.setFillWidth(false);
        VBox.setMargin(doorView, new Insets(-8, 0, 0, 0));

        sidePanelImage.setImage(loadImage("/game/assets/panels/sidePanel.png"));
        sidePanelImage.setPreserveRatio(true);
        sidePanelImage.setFitWidth(430);
        sidePanelImage.setSmooth(true);

        StackPane container = new StackPane(sidePanelImage, content);
        container.setAlignment(Pos.TOP_CENTER);
        container.setPrefWidth(430);
        container.setMinWidth(430);
        container.setMaxWidth(430);
        container.setPrefHeight(862);
        container.setMinHeight(862);
        container.setMaxHeight(862);

        return container;
    }

    private HBox createLeftSideContainer() {
        StackPane playerInfoColumn = createPlayerInfoColumn(
                playerHeaderLabel,
                playerPane,
                playerDoorView,
                playerTeamLabel,
                playerStationedMonsterBox,
                playerSidePanelImage,
                playerMonsterTypeLabel,
                playerPowerUpInfoLabel,
                playerPassiveTraitLabel);

        playerSideContainer = new HBox(8, playerInfoColumn, playerTurnLogView);
        playerSideContainer.setAlignment(Pos.TOP_CENTER);
        playerSideContainer.setPrefWidth(620);
        HBox.setMargin(playerTurnLogView, new Insets(80, 0, 0, 0));
        playerSideContainer.setFillHeight(false);
        playerSideContainer.setStyle("-fx-background-color: transparent;");

        return playerSideContainer;
    }

    private HBox createCardDeckIndicator() {
        StackPane cardStack = createCardRearStack();

        cardDeckCountLabel.setPrefWidth(86);
        cardDeckCountLabel.setAlignment(Pos.CENTER_LEFT);
        cardDeckCountLabel.setTextAlignment(TextAlignment.LEFT);
        cardDeckCountLabel.setStyle("""
                -fx-font-size: %spx;
                -fx-font-weight: 900;
                -fx-text-fill: white;
                -fx-padding: 2;
                """.formatted(ThemeManager.isRetro() ? 10 : 20)
                + ThemeManager.getThemeFontInlineStyle() + getRetroNeonTextStyle());
        cardDeckCountLabel.setEffect(getTextEffect(4));

        HBox indicator = new HBox(8, cardStack, cardDeckCountLabel);
        indicator.setPrefSize(172, 84);
        indicator.setMinSize(172, 84);
        indicator.setMaxSize(172, 84);
        indicator.setAlignment(Pos.CENTER);

        updateCardDeckIndicator();
        return indicator;
    }

    private void updateCardDeckIndicator() {
        for (ImageView cardRearView : cardDeckRearViews) {
            if (cardRearView != null) {
                cardRearView.setImage(loadImage("/game/assets/cards/PulledCardsRear.png"));
            }
        }
        int cardsLeft = Board.getCards() == null ? 0 : Board.getCards().size();
        cardDeckCountLabel.setText(String.valueOf(cardsLeft));
    }

    private StackPane createCardRearStack() {
        StackPane stack = new StackPane();
        stack.setPrefSize(78, 78);
        stack.setMinSize(78, 78);
        stack.setMaxSize(78, 78);

        for (int i = 0; i < cardDeckRearViews.length; i++) {
            ImageView cardRearView = new ImageView();
            cardRearView.setFitWidth(54);
            cardRearView.setFitHeight(72);
            cardRearView.setPreserveRatio(true);
            cardRearView.setSmooth(true);
            cardRearView.setRotate(-8 + i * 5);
            cardRearView.setTranslateX(i * 6);
            cardRearView.setTranslateY(-i * 2);
            cardRearView.setEffect(new DropShadow(4, Color.BLACK));
            cardDeckRearViews[i] = cardRearView;
            stack.getChildren().add(cardRearView);
        }

        updateCardDeckIndicator();
        return stack;
    }

    private HBox createRightSideContainer() {
        StackPane opponentInfoColumn = createPlayerInfoColumn(
                opponentHeaderLabel,
                opponentPane,
                opponentDoorView,
                opponentTeamLabel,
                opponentStationedMonsterBox,
                opponentSidePanelImage,
                opponentMonsterTypeLabel,
                opponentPowerUpInfoLabel,
                opponentPassiveTraitLabel);

        opponentSideContainer = new HBox(8, opponentTurnLogView, opponentInfoColumn);
        opponentSideContainer.setAlignment(Pos.TOP_CENTER);
        opponentSideContainer.setPrefWidth(620);
        HBox.setMargin(opponentTurnLogView, new Insets(80, 0, 0, 0));

        opponentSideContainer.setFillHeight(false);
        opponentSideContainer.setStyle("-fx-background-color: transparent;");

        return opponentSideContainer;
    }

    private void updateSidePanelFocus() {
        boolean playerTurn = game.getCurrent() == game.getPlayer();

        playerSideContainer.setOpacity(playerTurn ? 1.0 : 0.48);
        opponentSideContainer.setOpacity(playerTurn ? 0.48 : 1.0);
    }

    private void updateSidePanelImage(ImageView sidePanelImage, Monster monster) {
        String path;

        if (monster.isFrozen()) {
            path = "/game/assets/panels/frozenSidePanel.png";
        } else if (monster.isShielded()) {
            path = "/game/assets/panels/shieldedSidePanel.png";
        } else {
            path = "/game/assets/panels/sidePanel.png";
        }

        sidePanelImage.setImage(loadImage(path));
        updateSidePanelImageSize(sidePanelImage, monster);
    }

    private void updateSidePanelImageSize(ImageView sidePanelImage, Monster monster) {
        if (ThemeManager.isAncientEgyptian()) {
            sidePanelImage.setFitWidth(monster.isShielded() ? 390 : 430);
            return;
        }

        sidePanelImage.setFitWidth(430);
    }

    private Label createTeamLabel() {
        Label label = new Label();
        label.setPrefWidth(372);
        label.setAlignment(Pos.CENTER);
        label.setWrapText(true);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setStyle("""
                -fx-font-size: %spx;
                -fx-font-weight: 900;
                -fx-text-fill: white;
                -fx-padding: 0 4 0 4;
                """.formatted(ThemeManager.isRetro() ? 10 : 14)
                + ThemeManager.getThemeFontInlineStyle() + getRetroNeonTextStyle());
        label.setEffect(getTextEffect(4));

        return label;
    }

    private Label createMonsterDetailLabel() {
        Label label = new Label();
        label.setPrefWidth(300);
        label.setMinWidth(300);
        label.setMaxWidth(300);
        label.setMinHeight(Region.USE_PREF_SIZE);
        label.setMaxHeight(Region.USE_PREF_SIZE);
        label.setWrapText(true);
        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setStyle("""
                -fx-font-size: %spx;
                -fx-font-weight: 800;
                -fx-text-fill: white;
                -fx-padding: 0 2 0 2;
                -fx-line-spacing: %spx;
                """.formatted(ThemeManager.isRetro() ? 7 : 9, ThemeManager.isRetro() ? 4 : -1)
                + ThemeManager.getThemeFontInlineStyle() + getRetroNeonTextStyle());
        label.setEffect(getTextEffect(4));

        return label;
    }

    private void updateMonsterDetails(
            Monster monster,
            Label typeLabel,
            Label passiveLabel,
            Label powerUpLabel) {

        typeLabel.setText("Type: " + getMonsterType(monster));
        passiveLabel.setText(ThemeManager.isRetro()
                ? "Traits:\n" + getMonsterPassiveTrait(monster)
                : "Traits: " + getMonsterPassiveTrait(monster));
        powerUpLabel.setText(ThemeManager.isRetro()
                ? "Power-Up:\n" + getPlayerPowerUpInfo(monster)
                : "Power-Up: " + getPlayerPowerUpInfo(monster));
    }

    private HBox createStationedMonsterBox() {
        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER);
        box.setPrefWidth(372);
        box.setMinHeight(54);

        return box;
    }

    private void updateTeamSection(Label teamLabel, HBox monsterBox, Role role) {
        teamLabel.setText(role == Role.LAUGHER ? "Team Laughers" : "Team Scarers");
        monsterBox.getChildren().clear();

        for (Monster monster : Board.getStationedMonsters()) {
            if (monster.getOriginalRole() == role) {
                ImageView portrait = new ImageView(ImageCache.get(getMonsterImagePath(monster)));
                portrait.setFitWidth(26);
                portrait.setFitHeight(26);
                portrait.setPreserveRatio(true);
                portrait.setSmooth(true);
                portrait.setEffect(new DropShadow(3, Color.BLACK));

                Label energyLabel = createStationedMonsterEnergyLabel(monster.getEnergy());
                Label changeLabel = createStationedMonsterChangeLabel(monster);

                VBox monsterStack = new VBox(1, portrait, energyLabel, changeLabel);
                monsterStack.setAlignment(Pos.CENTER);
                monsterStack.setMinWidth(42);
                monsterStack.setPrefWidth(42);
                monsterBox.getChildren().add(monsterStack);
            }
        }
    }

    private Label createStationedMonsterEnergyLabel(int energy) {
        Label label = new Label(String.valueOf(energy));
        label.setAlignment(Pos.CENTER);
        label.setStyle("""
                -fx-font-size: %spx;
                -fx-font-weight: 900;
                -fx-text-fill: white;
                """.formatted(ThemeManager.isRetro() ? 7 : 9)
                + ThemeManager.getThemeFontInlineStyle() + getRetroNeonTextStyle());
        label.setEffect(getTextEffect(3));

        return label;
    }

    private Label createStationedMonsterChangeLabel(Monster monster) {
        Integer previousEnergy = stationedMonsterPreviousEnergies.get(monster);
        int currentEnergy = monster.getEnergy();
        stationedMonsterPreviousEnergies.put(monster, currentEnergy);

        Label label = new Label();
        label.setMinHeight(10);
        label.setAlignment(Pos.CENTER);
        label.setStyle("""
                -fx-font-size: 8px;
                -fx-font-weight: 900;
                -fx-text-fill: transparent;
                """);

        if (previousEnergy == null) {
            return label;
        }

        int change = currentEnergy - previousEnergy;
        if (change == 0) {
            return label;
        }

        label.setText(change > 0 ? "+" + change : String.valueOf(change));
        label.setStyle(change > 0
                ? "-fx-font-size: " + (ThemeManager.isRetro() ? "7px" : "8px")
                        + "; -fx-font-weight: 900; -fx-text-fill: "
                        + getGainColor() + ";" + ThemeManager.getThemeFontInlineStyle() + getRetroNeonTextStyle()
                : "-fx-font-size: " + (ThemeManager.isRetro() ? "7px" : "8px")
                        + "; -fx-font-weight: 900; -fx-text-fill: "
                        + getLossColor() + ";" + ThemeManager.getThemeFontInlineStyle() + getRetroNeonTextStyle());
        label.setEffect(getTextEffect(2));

        return label;
    }

    private Label createMonsterHeader() {
        Label label = new Label();
        label.setPrefWidth(372);
        label.setAlignment(Pos.CENTER);
        label.setWrapText(true);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setStyle("""
                -fx-font-size: %spx;
                -fx-font-weight: 900;
                -fx-text-fill: white;
                -fx-padding: 4 6 4 6;
                """.formatted(ThemeManager.isRetro() ? 9 : 13)
                + ThemeManager.getThemeFontInlineStyle() + getRetroNeonTextStyle());
        label.setEffect(getTextEffect(4));

        return label;
    }

    private String getRetroNeonTextStyle() {
        if (!ThemeManager.isRetro()) {
            return "";
        }

        return """
                -fx-effect: dropshadow(three-pass-box, rgba(0, 229, 255, 0.95), 8, 0, 0, 1);
                """;
    }

    private DropShadow getTextEffect(double defaultRadius) {
        if (!ThemeManager.isRetro()) {
            return new DropShadow(defaultRadius, Color.BLACK);
        }

        DropShadow cyanGlow = new DropShadow(10, Color.web("#00e5ff"));
        cyanGlow.setSpread(0.25);

        DropShadow pinkGlow = new DropShadow(7, Color.web("#ff4fd8"));
        pinkGlow.setSpread(0.18);
        pinkGlow.setInput(cyanGlow);

        return pinkGlow;
    }

    private String getGainColor() {
        if (ThemeManager.isRetro()) {
            return "#39ff9f";
        }

        return ThemeManager.isAncientEgyptian() ? "#76d85d" : "#35e66b";
    }

    private String getLossColor() {
        return ThemeManager.isRetro() ? "#ff4fd8" : "#ff3b3b";
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

    private String getMonsterImagePath(Monster monster) {
        String name = monster.getName();

        switch (name) {
            case "James P. Sullivan":
                return "/game/assets/Monsters/sulli.png";
            case "Mike Wazowski":
                return "/game/assets/Monsters/mike.png";
            case "Celia Mae":
                return "/game/assets/Monsters/celia.png";
            case "Roz":
                return "/game/assets/Monsters/roz.png";
            case "Fungus":
                return "/game/assets/Monsters/Fungus.png";
            case "Henry J. Waternoose":
                return "/game/assets/Monsters/henry.png";
            case "Yeti":
                return "/game/assets/Monsters/yeti.png";
            case "Randall Boggs":
                return "/game/assets/Monsters/andal.png";
            default:
                return null;
        }
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

    private String getMonsterType(Monster monster) {

        if (monster instanceof Dasher) {
            return "Dasher";
        } else if (monster instanceof MultiTasker) {
            return "Multi-Tasker";
        } else if (monster instanceof Dynamo) {
            return "Dynamo";
        } else if (monster instanceof Schemer) {
            return "Schemer";
        }
        return null;

    }

    private String getPlayerPowerUpInfo(Monster monster) {
        if (monster instanceof Dasher) {
            if (ThemeManager.isRetro()) {
                return "Gain 3x speed\nfor 3 turns";
            }
            return "Gain 3x movement speed" + "\n" + " for the next 3 turns";
        }

        if (monster instanceof MultiTasker) {
            if (ThemeManager.isRetro()) {
                return "Move normal speed\nfor 2 turns";
            }
            return "Move at normal speed for" + "\n" + " the next 2 turns";
        }

        if (monster instanceof Dynamo) {
            if (ThemeManager.isRetro()) {
                return "Freeze opponent\nfor next turn";
            }
            return "Freezes the opponent, making " + "\n" + " them skip their entire next turn";
        }
        if (monster instanceof Schemer) {
            if (ThemeManager.isRetro()) {
                return "Steal energy from\nall other monsters\nignores shield";
            }
            return "Steals energy from ALL " + "\n"
                    + " other monsters present (scarers - laughers)" + "\n" + " Not affected by the shield.";
        }
        return null;
    }

    private String getMonsterPassiveTrait(Monster monster) {
        if (monster instanceof Dasher) {
            if (ThemeManager.isRetro()) {
                return "2x dice movement";
            }
            return "Base dice movement is" + "\n" + " doubled (2x speed)";
        }

        if (monster instanceof MultiTasker) {
            if (ThemeManager.isRetro()) {
                return "Half dice movement\nenergy changes +200";
            }
            return "Half speed (1/2 dice movement) & All" + "\n" +
                    " energy changes receive +200 bonus";

        }
        if (monster instanceof Dynamo) {
            if (ThemeManager.isRetro()) {
                return "Energy changes\nare doubled";
            }
            return "All Energy Changes" + "\n" + " are doubled ";
        }
        if (monster instanceof Schemer) {
            if (ThemeManager.isRetro()) {
                return "Energy changes\nget +10 bonus";
            }
            return "All Energy Changes" + "\n" + " get +10 bonus";
        }
        return null;

    }
}
