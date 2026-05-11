package game.view;

import javafx.application.Application;
import javafx.stage.Stage;
import game.controllers.MainMenuController;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        new MainMenuController(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}