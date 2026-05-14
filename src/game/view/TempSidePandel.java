package game.view;

import game.view.MonsterInfoPane;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class TempSidePandel extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
        	
            BorderPane root = new BorderPane();

            MonsterInfoPane scarerPane = new MonsterInfoPane();
            MonsterInfoPane laugherPane = new MonsterInfoPane();
            
            root.setLeft(scarerPane);
            root.setRight(laugherPane);

            scarerPane.updateUI("ROZ", "Tank", "SCARER", "SCARER", 300, 12, true, false, false, true);
            

            laugherPane.updateUI("james", "Dasher", "LAUGHER", "SCARER", 1000, 55, false, true, true, false);


            Scene scene = new Scene(root, 1000, 600);
            primaryStage.setTitle("Monster & Status HUD - Milestone 3 Test");
            primaryStage.setScene(scene);
            primaryStage.show();
            

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
