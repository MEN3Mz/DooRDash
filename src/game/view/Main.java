package game.view;

import javafx.application.Application;
import javafx.stage.Stage;
import game.engine.Game;
import game.engine.Role;
import javafx.scene.Scene;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Game game = new Game(Role.SCARER);
        GameBoardView boardView = new GameBoardView(game);

        Scene scene = new Scene(boardView.getBoardRoot(), 900, 900);

        primaryStage.setTitle("Board Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}