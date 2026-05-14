package game.view;

import game.audio.SoundManager;
import game.engine.Role;
import game.engine.monsters.Monster;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class GameOverView {

    private final StackPane root;
    private final Button mainMenuButton;

    public GameOverView(Monster winner) {
        root = new StackPane();
        root.getStylesheets().add(
                getClass().getResource("/game/assets/css/menu.css").toExternalForm());

        mainMenuButton = createMenuButton("Back To Main Menu");

        ImageView backgroundView = createBackgroundView();
        VBox content = createContent(winner);

        root.getChildren().addAll(backgroundView, content);
        StackPane.setAlignment(content, Pos.CENTER);
    }

    public Parent getRoot() {
        return root;
    }

    public void setOnMainMenu(EventHandler<ActionEvent> handler) {
        mainMenuButton.setOnAction(handler);
    }

    private VBox createContent(Monster winner) {
        VBox content = new VBox(24);
        content.setAlignment(Pos.CENTER);
        content.setMaxWidth(760);
        content.setStyle("""
                -fx-background-color:
                    linear-gradient(to bottom right,
                    rgba(8,16,26,0.96),
                    rgba(15,25,40,0.96));

                -fx-background-radius: 24;
                -fx-border-radius: 24;
                -fx-border-width: 2;
                -fx-border-color: rgba(255,255,255,0.12);
                -fx-padding: 46;
                -fx-effect:
                    dropshadow(three-pass-box,
                    rgba(0,0,0,0.55),
                    24, 0, 0, 8);
                """);

        Label title = new Label("Game Over");
        title.setStyle("""
                -fx-font-size: 42px;
                -fx-font-weight: 900;
                -fx-text-fill: white;
                """);

        Label teamLabel = new Label(getTeamName(winner.getOriginalRole()) + " Team Won!");
        teamLabel.setStyle("""
                -fx-font-size: 30px;
                -fx-font-weight: 900;
                -fx-text-fill: #cfe4ff;
                """);

        Label winnerLabel = new Label("Winner: " + winner.getName());
        winnerLabel.setStyle("""
                -fx-font-size: 18px;
                -fx-font-weight: bold;
                -fx-text-fill: #dce8f5;
                """);

        content.getChildren().addAll(title, teamLabel, winnerLabel, mainMenuButton);
        return content;
    }

    private Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(320);
        button.setPrefHeight(58);
        button.getStyleClass().add("menu-button");
        button.getStyleClass().add("menu-font");
        button.setOnMousePressed(e -> SoundManager.playButtonSound());

        return button;
    }

    private String getTeamName(Role role) {
        return role == Role.LAUGHER ? "Laughers" : "Scarers";
    }

    private ImageView createBackgroundView() {
        ImageView backgroundView = new ImageView(loadImage("/game/assets/Background.png"));

        backgroundView.setPreserveRatio(false);
        backgroundView.fitWidthProperty().bind(root.widthProperty());
        backgroundView.fitHeightProperty().bind(root.heightProperty());

        return backgroundView;
    }

    private Image loadImage(String path) {
        return new Image(getClass().getResourceAsStream(path));
    }
}
