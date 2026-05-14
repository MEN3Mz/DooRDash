package game.controllers;

import game.view.HowToPlayView;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HowToPlayController {

    private final Stage stage;
    private final HowToPlayView view;

    public HowToPlayController(Stage stage) {
        this.stage = stage;
        this.view = new HowToPlayView();

        bindEvents();
        show();
    }

    private void bindEvents() {
        view.setOnBack(e -> new MainMenuController(stage));
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