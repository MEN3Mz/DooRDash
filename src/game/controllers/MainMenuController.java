package game.controllers;

import game.audio.SoundManager;
import game.view.HowToPlayView;
import game.view.MainMenuView;
import game.view.PlayerNamesView;
import game.view.SettingsView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainMenuController {

    private final Stage stage;
    private final MainMenuView view;
    private Parent howToPlayOverlay;
    private Parent playerNamesOverlay;
    private Parent settingsOverlay;

    public MainMenuController(Stage stage) {
        this.stage = stage;
        this.view = new MainMenuView();
        SoundManager.playMainMenuMusic();

        bindEvents();
        show();
    }

    private void bindEvents() {

        view.setOnStartGame(e -> {
            System.out.println("Start Game clicked");
            showPlayerNamesOverlay();
        });

        view.setOnSettings(e -> {
            System.out.println("Settings clicked");
            showSettingsOverlay();
        });

        view.setOnHowToPlay(e -> {
            System.out.println("How to Play clicked");
            showHowToPlayOverlay();
        });

        view.setOnExitGame(e -> stage.close());
    }

    private void showHowToPlayOverlay() {
        if (howToPlayOverlay != null) {
            return;
        }

        HowToPlayView howToPlayView = new HowToPlayView();
        howToPlayOverlay = howToPlayView.getRoot();

        howToPlayView.setOnBack(e -> hideHowToPlayOverlay());
        ((StackPane) view.getRoot()).getChildren().add(howToPlayOverlay);
    }

    private void hideHowToPlayOverlay() {
        if (howToPlayOverlay == null) {
            return;
        }

        ((StackPane) view.getRoot()).getChildren().remove(howToPlayOverlay);
        howToPlayOverlay = null;
    }

    private void showPlayerNamesOverlay() {
        if (playerNamesOverlay != null) {
            return;
        }

        PlayerNamesView playerNamesView = new PlayerNamesView();
        playerNamesOverlay = playerNamesView.getRoot();

        playerNamesView.setOnCancel(e -> hidePlayerNamesOverlay());
        playerNamesView.setOnContinue(e -> {
            String playerOneName = playerNamesView.getPlayerOneName();
            String playerTwoName = playerNamesView.getPlayerTwoName();

            if (playerOneName.isEmpty() || playerTwoName.isEmpty()) {
                playerNamesView.setError("Please enter both player names.");
                return;
            }

            System.out.println("Player 1: " + playerOneName);
            System.out.println("Player 2: " + playerTwoName);
            hidePlayerNamesOverlay();
            new ChooseSideController(stage, playerOneName, playerTwoName);
        });

        ((StackPane) view.getRoot()).getChildren().add(playerNamesOverlay);
    }

    private void hidePlayerNamesOverlay() {
        if (playerNamesOverlay == null) {
            return;
        }

        ((StackPane) view.getRoot()).getChildren().remove(playerNamesOverlay);
        playerNamesOverlay = null;
    }

    private void showSettingsOverlay() {
        if (settingsOverlay != null) {
            return;
        }

        SettingsView settingsView = new SettingsView();
        settingsOverlay = settingsView.getRoot();

        settingsView.setOnBack(e -> hideSettingsOverlay());
        ((StackPane) view.getRoot()).getChildren().add(settingsOverlay);
    }

    private void hideSettingsOverlay() {
        if (settingsOverlay == null) {
            return;
        }

        ((StackPane) view.getRoot()).getChildren().remove(settingsOverlay);
        settingsOverlay = null;
        view.refreshSoundButton();
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
