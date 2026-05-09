package game.view;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        MainMenuView mainMenuView = new MainMenuView();
        mainMenuView.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
