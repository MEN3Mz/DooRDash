package game.controllers;

import game.engine.Game;
import game.engine.Role;
import game.engine.monsters.Monster;
import game.view.GameView;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

public class GameController {

    private final Stage stage;
    private final Game game;
    private final GameView view;

    public GameController(Stage stage, Role role) throws Exception {
        this.stage = stage;

        this.game = new Game(role);
        this.view = new GameView(game);

        bindEvents();
        show();
    }

    private void bindEvents() {
        view.getBottomView().setOnRollDice(e -> {
            try {
                int rolledValue = game.playTurn();

                view.getBottomView().setDiceValue(rolledValue);
                view.refresh();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        view.getBottomView().setOnPowerUp(e -> {

            try {
                game.usePowerup();

                view.refresh();
            } catch (Exception ex) {
                ex.printStackTrace();
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