package game.view;

import game.audio.SoundManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PlayerNamesView {

    private final StackPane root;
    private final TextField playerOneField;
    private final TextField playerTwoField;
    private final Button continueButton;
    private final Button cancelButton;
    private final Label errorLabel;

    public PlayerNamesView() {
        root = new StackPane();
        root.getStylesheets().add(
                getClass().getResource("/game/assets/css/menu.css").toExternalForm());

        playerOneField = createNameField("Player 1 name");
        playerTwoField = createNameField("Player 2 name");
        continueButton = createMenuButton("Continue");
        cancelButton = createMenuButton("Back");
        errorLabel = new Label();
        errorLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #ffb4b4;");

        Rectangle overlayBackground = createOverlayBackground();
        VBox content = createContent();

        root.getChildren().addAll(overlayBackground, content);
        StackPane.setAlignment(content, Pos.CENTER);
    }

    public Parent getRoot() {
        return root;
    }

    public String getPlayerOneName() {
        return playerOneField.getText().trim();
    }

    public String getPlayerTwoName() {
        return playerTwoField.getText().trim();
    }

    public void setError(String message) {
        errorLabel.setText(message);
    }

    public void setOnContinue(EventHandler<ActionEvent> handler) {
        continueButton.setOnAction(handler);
    }

    public void setOnCancel(EventHandler<ActionEvent> handler) {
        cancelButton.setOnAction(handler);
    }

    private VBox createContent() {
        Label title = new Label("Player Names");
        title.setStyle("""
                -fx-font-size: 30px;
                -fx-font-weight: 900;
                -fx-text-fill: white;
                """);

        Label subtitle = new Label("Enter both names before choosing a side.");
        subtitle.setStyle("""
                -fx-font-size: 15px;
                -fx-text-fill: #cfe4ff;
                """);

        HBox buttons = new HBox(16, cancelButton, continueButton);
        buttons.setAlignment(Pos.CENTER);

        VBox content = new VBox(18, title, subtitle, playerOneField, playerTwoField, errorLabel, buttons);
        content.setAlignment(Pos.CENTER);
        content.setMaxWidth(560);
        content.setStyle("""
                -fx-background-color:
                    linear-gradient(to bottom right,
                    rgba(8,16,26,0.96),
                    rgba(15,25,40,0.96));

                -fx-background-radius: 24;
                -fx-border-radius: 24;
                -fx-border-width: 2;
                -fx-border-color: rgba(255,255,255,0.12);
                -fx-padding: 36;
                -fx-effect:
                    dropshadow(three-pass-box,
                    rgba(0,0,0,0.55),
                    24, 0, 0, 8);
                """);

        return content;
    }

    private TextField createNameField(String promptText) {
        TextField field = new TextField();
        field.setPromptText(promptText);
        field.setMaxWidth(360);
        field.setStyle("""
                -fx-background-color: rgba(255,255,255,0.92);
                -fx-background-radius: 10;
                -fx-border-color: rgba(255,255,255,0.28);
                -fx-border-radius: 10;
                -fx-font-size: 16px;
                -fx-padding: 12 14;
                """);

        return field;
    }

    private Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(220);
        button.setPrefHeight(52);
        button.getStyleClass().add("menu-button");
        button.getStyleClass().add("menu-font");
        button.setOnMouseEntered(e -> SoundManager.playHoverSound());
        button.setOnMousePressed(e -> SoundManager.playButtonSound());

        return button;
    }

    private Rectangle createOverlayBackground() {
        Rectangle backgroundView = new Rectangle();

        backgroundView.setFill(Color.color(0, 0, 0, 0.72));
        backgroundView.widthProperty().bind(root.widthProperty());
        backgroundView.heightProperty().bind(root.heightProperty());

        return backgroundView;
    }
}
