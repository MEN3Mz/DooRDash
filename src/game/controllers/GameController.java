package game.controllers;

import game.audio.SoundManager;
import game.engine.Game;
import game.engine.Constants;
import game.engine.Role;
import game.engine.cards.Card;
import game.engine.cells.ContaminationSock;
import game.engine.cells.ConveyorBelt;
import game.engine.monsters.Monster;
import game.view.CardView;
import game.view.GameOverView;
import game.view.GameView;
import game.view.HowToPlayView;
import game.view.SettingsView;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class GameController {

    private final Stage stage;
    private final Game game;
    private final GameView view;
    private final String playerOneName;
    private final String playerTwoName;
    private Parent overlayPane;
    private String lastTurnPlayerName;

    public GameController(Stage stage, Role role) throws Exception {
        this(stage, role, "You", "Opponent");
    }

    public GameController(Stage stage, Role role, String playerOneName, String playerTwoName) throws Exception {
        this.stage = stage;
        this.playerOneName = playerOneName;
        this.playerTwoName = playerTwoName;
        this.game = new Game(role);
        this.view = new GameView(game, playerOneName, playerTwoName);

        bindEvents();
        show();
    }

    private void bindEvents() {

        view.getBottomView().setOnRollDice(e -> {
            try {
                SoundManager.playDiceRollSound();
                lastTurnPlayerName = getCurrentPlayerName();
                int rolledValue = game.playTurn();
                playLandedCellSound();

                view.getBottomView().setDiceValue(rolledValue);
                view.refresh();
                handleCardDrawnOrGameOver();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        view.getBottomView().setOnPowerUp(e -> {
            try {
                if (game.getCurrent().getEnergy() >= Constants.POWERUP_COST) {
                    SoundManager.playPowerUpSound();
                } else {
                    SoundManager.playInvalidSound();
                }
                game.usePowerup();
                view.refresh();
                handleGameOver();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        view.setOnMenu(e -> showPauseMenuOverlay());
        view.setOnWinTest(e -> triggerWinTest());
    }

    private void triggerWinTest() {
        game.forceCurrentWinForTesting();
        view.refresh();
        handleGameOver();
    }

    private void handleCardDrawnOrGameOver() {
        Card drawnCard = game.getLastDrawnCard();

        if (drawnCard != null) {
            showCardOverlay(drawnCard, lastTurnPlayerName);
            return;
        }

        handleGameOver();
    }

    private void playLandedCellSound() {
        if (game.getBoard().getLastLandedCell() instanceof ConveyorBelt) {
            SoundManager.playBeltSound();
        } else if (game.getBoard().getLastLandedCell() instanceof ContaminationSock) {
            SoundManager.playSockSound();
        }
    }

    private void showCardOverlay(Card card, String pulledBy) {
        hideOverlay();

        CardView cardView = new CardView(card, pulledBy);
        overlayPane = cardView.getRoot();

        cardView.setOnClose(e -> {
            hideOverlay();
            handleGameOver();
        });

        view.getRoot().getChildren().add(overlayPane);
    }

    private String getCurrentPlayerName() {
        return game.getCurrent() == game.getPlayer() ? playerOneName : playerTwoName;
    }

    private void handleGameOver() {
        if (!game.isGameOver()) {
            return;
        }

        view.setGameControlsDisabled(true);

        if (game.getWinner() != null) {
            System.out.println(game.getWinner().getName() + " wins!");
            showGameOverView(game.getWinner());
        }
    }

    private void showPauseMenuOverlay() {
        hideOverlay();

        Rectangle overlayBackground = new Rectangle();
        overlayBackground.setFill(Color.color(0, 0, 0, 0.72));
        overlayBackground.widthProperty().bind(stage.widthProperty());
        overlayBackground.heightProperty().bind(stage.heightProperty());

        VBox content = new VBox(20);
        content.setMaxWidth(520);
        content.setMaxHeight(420);
        content.setStyle("""
                -fx-background-color:
                    linear-gradient(to bottom right,
                    rgba(8,16,26,0.96),
                    rgba(15,25,40,0.96));

                -fx-background-radius: 24;
                -fx-border-radius: 24;
                -fx-border-width: 2;
                -fx-border-color: rgba(255,255,255,0.12);
                -fx-padding: 36;
                -fx-effect:
                    dropshadow(three-pass-box,
                    rgba(0,0,0,0.55),
                    24, 0, 0, 8);
                """);
        content.setAlignment(Pos.CENTER);

        Label title = new Label("Game Paused");
        title.setStyle("""
                -fx-font-size: 30px;
                -fx-font-weight: 900;
                -fx-text-fill: white;
                """);

        Button resumeButton = createPauseButton("Resume Game");
        resumeButton.setOnAction(e -> hideOverlay());

        Button howToPlayButton = createPauseButton("How To Play");
        howToPlayButton.setOnAction(e -> showHowToPlayOverlay());

        Button settingsButton = createPauseButton("Settings");
        settingsButton.setOnAction(e -> showSettingsOverlay());

        Button backToMenuButton = createPauseButton("Back To Main Menu");
        backToMenuButton.setOnAction(e -> {
            hideOverlay();
            new MainMenuController(stage);
        });

        Button exitButton = createPauseButton("Exit Game");
        exitButton.setOnAction(e -> stage.close());

        content.getChildren().addAll(
                title,
                resumeButton,
                howToPlayButton,
                settingsButton,
                backToMenuButton,
                exitButton);

        overlayPane = new StackPane();
        overlayPane.getStylesheets().add(
                getClass().getResource("/game/assets/css/menu.css").toExternalForm());
        ((StackPane) overlayPane).getChildren().addAll(overlayBackground, content);
        StackPane.setAlignment(content, Pos.CENTER);

        view.getRoot().getChildren().add(overlayPane);
    }

    private Button createPauseButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(320);
        button.setPrefHeight(58);
        button.getStyleClass().add("menu-button");
        button.getStyleClass().add("menu-font");
        button.setOnMousePressed(e -> SoundManager.playButtonSound());

        return button;
    }

    private void showGameOverView(Monster winner) {
        GameOverView gameOverView = new GameOverView(winner);
        gameOverView.setOnMainMenu(e -> new MainMenuController(stage));

        if (stage.getScene() == null) {
            stage.setScene(new Scene(gameOverView.getRoot()));
        } else {
            stage.getScene().setRoot(gameOverView.getRoot());
        }
    }

    private void showHowToPlayOverlay() {
        hideOverlay();

        HowToPlayView howToPlayView = new HowToPlayView();
        overlayPane = howToPlayView.getRoot();

        howToPlayView.setOnBack(e -> showPauseMenuOverlay());
        view.getRoot().getChildren().add(overlayPane);
    }

    private void showSettingsOverlay() {
        hideOverlay();

        SettingsView settingsView = new SettingsView();
        overlayPane = settingsView.getRoot();

        settingsView.setOnBack(e -> showPauseMenuOverlay());
        view.getRoot().getChildren().add(overlayPane);
    }

    private void hideOverlay() {
        if (overlayPane != null) {
            view.getRoot().getChildren().remove(overlayPane);
            overlayPane = null;
        }
    }

    private void show() {
        if (stage.getScene() == null) {
            stage.setScene(new Scene(view.getRoot()));
        } else {
            stage.getScene().setRoot(view.getRoot());
        }

        stage.show();
    }
}
