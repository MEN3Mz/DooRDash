package game.controllers;

import game.audio.SoundManager;
import game.engine.Game;
import game.engine.Constants;
import game.engine.Role;
import game.engine.cards.Card;
import game.engine.cards.ConfusionCard;
import game.engine.cells.ContaminationSock;
import game.engine.cells.ConveyorBelt;
import game.engine.cells.DoorCell;
import game.engine.exceptions.InvalidMoveException;
import game.engine.exceptions.OutOfEnergyException;
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
import javafx.scene.input.KeyCode;
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
        SoundManager.playGameMusic();

        bindEvents();
        show();
    }

    private void bindEvents() {

        view.getBottomView().setOnRollDice(e -> {
            try {
                SoundManager.playDiceRollSound();
                lastTurnPlayerName = getCurrentPlayerName();
                boolean playerWasShielded = game.getPlayer().isShielded();
                boolean opponentWasShielded = game.getOpponent().isShielded();
                boolean playerWasFrozen = game.getPlayer().isFrozen();
                boolean opponentWasFrozen = game.getOpponent().isFrozen();
                Monster actingMonster = game.getCurrent();
                int actingMonsterEnergyBefore = actingMonster.getEnergy();
                int rolledValue = game.playTurn();
                playLandedCellSound();
                playEnergyChangeSound(actingMonster, actingMonsterEnergyBefore);
                playStatusChangeSounds(playerWasShielded, opponentWasShielded, playerWasFrozen, opponentWasFrozen);

                view.getBottomView().setDiceValue(rolledValue);
                view.refresh();
                handleCardDrawnOrGameOver();

            } catch (InvalidMoveException ex) {
                SoundManager.playInvalidSound();
                view.getBottomView().setDiceValue(game.getLastRolledValue());
                view.refresh();
                System.out.println("Invalid move: " + ex.getMessage() + " Roll again.");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        view.getBottomView().setOnPowerUp(e -> {
            try {
                boolean playerWasShielded = game.getPlayer().isShielded();
                boolean opponentWasShielded = game.getOpponent().isShielded();
                boolean playerWasFrozen = game.getPlayer().isFrozen();
                boolean opponentWasFrozen = game.getOpponent().isFrozen();
                if (game.getCurrent().getEnergy() >= Constants.POWERUP_COST) {
                    SoundManager.playPowerUpSound();
                } else {
                    SoundManager.playInvalidSound();
                }
                game.usePowerup();
                playStatusChangeSounds(playerWasShielded, opponentWasShielded, playerWasFrozen, opponentWasFrozen);
                view.refresh();
                handleGameOver();

            } catch (OutOfEnergyException ex) {
                view.refresh();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        view.setOnMenu(e -> showPauseMenuOverlay());
    }

    private void handleCardDrawnOrGameOver() {
        Card drawnCard = game.getLastDrawnCard();

        if (drawnCard != null) {
            if (drawnCard instanceof ConfusionCard) {
                SoundManager.playConfusionSound();
            }
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
        } else if (game.getBoard().getLastLandedCell() instanceof DoorCell) {
            DoorCell doorCell = (DoorCell) game.getBoard().getLastLandedCell();
            if (doorCell.wasLastLandingEnergyChangeActivation()) {
                SoundManager.playDoorSound();
            }
        }
    }

    private void playStatusChangeSounds(
            boolean playerWasShielded,
            boolean opponentWasShielded,
            boolean playerWasFrozen,
            boolean opponentWasFrozen) {

        playShieldChangeSound(playerWasShielded, game.getPlayer().isShielded());
        playShieldChangeSound(opponentWasShielded, game.getOpponent().isShielded());
        playFreezeChangeSound(playerWasFrozen, game.getPlayer().isFrozen());
        playFreezeChangeSound(opponentWasFrozen, game.getOpponent().isFrozen());
    }

    private void playEnergyChangeSound(Monster monster, int energyBefore) {
        if (game.getBoard().getLastLandedCell() instanceof ConveyorBelt
                || game.getBoard().getLastLandedCell() instanceof ContaminationSock) {
            return;
        }

        int energyAfter = monster.getEnergy();

        if (energyAfter < energyBefore) {
            SoundManager.playDamageSound();
        } else if (energyAfter > energyBefore) {
            SoundManager.playEnergyIncreaseSound();
        }
    }

    private void playShieldChangeSound(boolean wasShielded, boolean isShielded) {
        if (!wasShielded && isShielded) {
            SoundManager.playShieldAddSound();
        } else if (wasShielded && !isShielded) {
            SoundManager.playShieldRemoveSound();
        }
    }

    private void playFreezeChangeSound(boolean wasFrozen, boolean isFrozen) {
        if (!wasFrozen && isFrozen) {
            SoundManager.playFreezeSound();
        } else if (wasFrozen && !isFrozen) {
            SoundManager.playUnfreezeSound();
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
            SoundManager.stopMusic();
            playWinSound(game.getWinner());
            showGameOverView(game.getWinner());
        }
    }

    private void playWinSound(Monster winner) {
        if (winner.getRole() == Role.LAUGHER) {
            SoundManager.playLaughWinSound();
        } else {
            SoundManager.playScareWinSound();
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
        content.getStyleClass().add("pause-popup");
        content.setAlignment(Pos.CENTER);

        Label title = new Label("Game Paused");
        title.getStyleClass().add("pause-title");

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
        overlayPane.getStylesheets().add(
                getClass().getResource("/game/assets/css/pause-view.css").toExternalForm());
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
        button.setOnMouseEntered(e -> SoundManager.playHoverSound());
        button.setOnMousePressed(e -> SoundManager.playButtonSound());

        return button;
    }

    private void showGameOverView(Monster winner) {
        GameOverView gameOverView = new GameOverView(
                winner,
                game.getPlayer(),
                game.getOpponent(),
                getPlayerNameForMonster(winner));
        gameOverView.setOnMainMenu(e -> {
            SoundManager.stopAllEffects();
            new MainMenuController(stage);
        });
        gameOverView.setOnExit(e -> {
            SoundManager.stopAllEffects();
            stage.close();
        });

        if (stage.getScene() == null) {
            stage.setScene(new Scene(gameOverView.getRoot()));
        } else {
            stage.getScene().setRoot(gameOverView.getRoot());
        }
    }

    private String getPlayerNameForMonster(Monster monster) {
        return monster == game.getPlayer() ? playerOneName : playerTwoName;
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
        installTestKeyHandlers();
    }

    private void installTestKeyHandlers() {
        stage.getScene().setOnKeyPressed(event -> {
            if (game.isGameOver()) {
                return;
            }

            if (event.getCode() == KeyCode.W) {
                game.forceCurrentPositionForTesting(Constants.WINNING_POSITION);
                view.refresh();
                handleGameOver();
                event.consume();
            } else if (event.getCode() == KeyCode.E) {
                game.forceCurrentEnergyForTesting(Constants.WINNING_ENERGY);
                view.refresh();
                handleGameOver();
                event.consume();
            }
        });
    }
}
