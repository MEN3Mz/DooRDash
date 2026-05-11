package game.view;

import game.engine.Role;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        GameView gameView = new GameView(new game.engine.Game(Role.SCARER));
        Scene scene = new Scene(gameView.getRoot(), 1920, 950);

        primaryStage.setTitle("DoorDash Game - Game View");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
