package game.view;

import javafx.application.Application;
import javafx.stage.Stage;
import game.engine.Game;
import game.engine.Role;
import game.engine.cells.DoorCell;
import javafx.scene.Scene;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Game game = new Game(Role.LAUGHER);
        GameBoardView boardView = new GameBoardView(game);
        game.getPlayer().setPosition(10);
        boardView.refreshBoard();
        Scene scene = new Scene(boardView.getBoardRoot(), 900, 900);

        primaryStage.setTitle("Board Test");
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}