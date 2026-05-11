package game.controllers;

import game.engine.Game;
import game.engine.Role;
import game.engine.monsters.Monster;
import game.view.ChooseSideView;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ChooseSideController {

    private final Stage stage;
    private final ChooseSideView view;

    public ChooseSideController(Stage stage) {
        this.stage = stage;
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

            // 1. Create monsters (or load them properly)
            List<Monster> monsters = loadMonsters();

            // 2. Create Game engine
            Game game = null;
            try {
                game = new Game(selectedRole);
            } catch (IOException e1) {

                e1.printStackTrace();
            }

            // 3. Move to GameController
            try {
                new GameController(stage, selectedRole);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        });
    }

    private List<Monster> loadMonsters() {
        try {
            return game.engine.dataloader.DataLoader.readMonsters();
        } catch (Exception e) {
            return List.of();
        }
    }

    private void show() {
        stage.setScene(new Scene(view.getRoot()));
        stage.show();
    }
}