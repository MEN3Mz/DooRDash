package game.gui;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        GameView view = new GameView(); // This is the line that was crashing
        view.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}