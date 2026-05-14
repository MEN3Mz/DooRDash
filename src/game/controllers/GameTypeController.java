package game.controllers;

import game.view.GameTypeView;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameTypeController {

    private final Stage stage;
    private final GameTypeView view;

    public GameTypeController(Stage stage) {
        this.stage = stage;
        this.view = new GameTypeView();

        bindEvents();
        show();
    }

    private void bindEvents() {
        view.setOnContinue(e -> new ChooseSideController(stage));
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
