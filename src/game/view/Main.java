package game.view;

import game.controllers.MainMenuController;
import javafx.application.Application;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        new MainMenuController(primaryStage);

        primaryStage.show();

        primaryStage.setFullScreenExitHint("");

        primaryStage.setFullScreenExitKeyCombination(
                KeyCombination.NO_MATCH);

        primaryStage.setFullScreen(true);
    }

    public static void main(String[] args) {
        launch(args);
    }
}