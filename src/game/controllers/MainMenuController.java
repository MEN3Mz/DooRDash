package game.controllers;

import game.view.MainMenuView;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainMenuController {

    private final Stage stage;
    private final MainMenuView view;

    public MainMenuController(Stage stage) {
        this.stage = stage;
        this.view = new MainMenuView();

        bindEvents();
        show();
    }

    private void bindEvents() {

        // START GAME → go to role selection
        view.setOnStartGame(e -> {
            System.out.println("Start Game clicked");
            new ChooseSideController(stage);
        });

        // SETTINGS (placeholder for now)
        view.setOnSettings(e -> {
            System.out.println("Settings clicked");
            // new SettingsController(stage);
        });

        // HOW TO PLAY (placeholder for now)
        view.setOnHowToPlay(e -> {
            System.out.println("How to Play clicked");
            // new HowToPlayController(stage);
        });
    }

    private void show() {

        if (stage.getScene() == null) {
            stage.setScene(new Scene(view.getRoot()));
        } else {
            stage.getScene().setRoot(view.getRoot());
        }
    }
}