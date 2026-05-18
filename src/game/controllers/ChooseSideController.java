package game.controllers;

import game.engine.Role;
import game.view.ChooseSideView;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChooseSideController {

    private final Stage stage;
    private final ChooseSideView view;
    private final String playerOneName;
    private final String playerTwoName;

    public ChooseSideController(Stage stage) {
        this(stage, "You", "Opponent");
    }

    public ChooseSideController(Stage stage, String playerOneName, String playerTwoName) {
        this.stage = stage;
        this.playerOneName = playerOneName;
        this.playerTwoName = playerTwoName;
        this.view = new ChooseSideView();

        bindEvents();
        show();
    }

    private void bindEvents() {

        // Role selection (optional, only UI feedback already handled in View)

        view.setOnStartGame(e -> {

            Role selectedRole = view.getSelectedRole();

            if (selectedRole == null)
                return;

            try {
                new GameController(stage, selectedRole, playerOneName, playerTwoName);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        });
    }

    private void show() {

        if (stage.getScene() == null) {
            stage.setScene(new Scene(view.getRoot()));
        } else {
            stage.getScene().setRoot(view.getRoot());
        }
    }
}
