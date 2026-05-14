package game.controllers;

import game.engine.Game;
import game.engine.Role;
import game.view.GameView;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GameController {

    private final Stage stage;
    private final Game game;
    private final GameView view;
    private StackPane overlayPane;

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

        view.setOnMenu(e -> showPauseMenuOverlay());
    }

    private void showPauseMenuOverlay() {

        Rectangle overlayBackground = new Rectangle();
        overlayBackground.setFill(Color.color(0, 0, 0, 0.7));
        overlayBackground.widthProperty().bind(stage.widthProperty());
        overlayBackground.heightProperty().bind(stage.heightProperty());

        VBox content = new VBox(20);
        content.setMaxWidth(400);
        content.setMaxHeight(300);
        content.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 15;" +
                        "-fx-padding: 30;" +
                        "-fx-border-color: #333;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 15;");
        content.setAlignment(Pos.CENTER);

        Text title = new Text("Game Paused");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-fill: #333;");

        Button resumeButton = new Button("Resume Game");
        resumeButton.setStyle(
                "-fx-background-color: #4CAF50;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-padding: 12 24;" +
                        "-fx-pref-width: 200;");
        resumeButton.setOnAction(e -> hideOverlay());

        Button howToPlayButton = new Button("How to Play");
        howToPlayButton.setStyle(
                "-fx-background-color: #2196F3;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-padding: 12 24;" +
                        "-fx-pref-width: 200;");
        howToPlayButton.setOnAction(e -> showHowToPlayOverlay());

        Button backToMenuButton = new Button("Back to Main Menu");
        backToMenuButton.setStyle(
                "-fx-background-color: #f44336;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-padding: 12 24;" +
                        "-fx-pref-width: 200;");
        backToMenuButton.setOnAction(e -> {
            hideOverlay();
            new MainMenuController(stage);
        });

        content.getChildren().addAll(
                title,
                resumeButton,
                howToPlayButton,
                backToMenuButton);

        overlayPane = new StackPane();
        overlayPane.getChildren().addAll(overlayBackground, content);
        StackPane.setAlignment(content, Pos.CENTER);

        ((StackPane) stage.getScene().getRoot()).getChildren().add(overlayPane);
    }

    private void showHowToPlayOverlay() {
        hideOverlay();

        MainMenuController menuController = new MainMenuController(stage);
        menuController.showHowToPlayOverlay();
    }

    private void hideOverlay() {
        if (overlayPane != null) {
            ((StackPane) stage.getScene().getRoot()).getChildren().remove(overlayPane);
            overlayPane = null;
        }
    }

    private void show() {
        if (stage.getScene() == null) {
            stage.setScene(new Scene(view.getRoot()));
        } else {
            stage.getScene().setRoot(view.getRoot());
        }

        stage.show();
    }
}