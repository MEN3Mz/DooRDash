package game.view;

import javafx.application.Application;
import javafx.stage.Stage;
import game.engine.Game;
import game.engine.Role;
import javafx.scene.Scene;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Game game = new Game(Role.LAUGHER);
        GameView gameView = new GameView(game);
        Scene scene = new Scene(gameView.getRoot(), 1280, 900);

        primaryStage.setTitle("DoorDash Game");
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
