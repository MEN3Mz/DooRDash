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
                ThemeManager.loadStylesheet("/game/assets/css/menu.css"));
        root.getStylesheets().add(
                ThemeManager.loadStylesheet("/game/assets/css/player-names-view.css"));

        playerOneField = createNameField("Player 1 name");
        playerTwoField = createNameField("Player 2 name");
        continueButton = createMenuButton("Continue");
        cancelButton = createMenuButton("Back");
        errorLabel = new Label();
        errorLabel.getStyleClass().add("player-names-error");

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
        title.getStyleClass().add("player-names-title");

        Label subtitle = new Label("Enter both names before choosing a side.");
        subtitle.getStyleClass().add("player-names-subtitle");

        HBox buttons = new HBox(12, cancelButton, continueButton);
        buttons.setAlignment(Pos.CENTER);

        VBox content = new VBox(12, title, subtitle, playerOneField, playerTwoField, errorLabel, buttons);
        content.setAlignment(Pos.CENTER);
        content.setMaxWidth(460);
        content.setMaxHeight(560);
        content.getStyleClass().add("player-names-popup");

        return content;
    }

    private TextField createNameField(String promptText) {
        TextField field = new TextField();
        field.setPromptText(promptText);
        field.setMaxWidth(320);
        field.getStyleClass().add("player-name-field");

        return field;
    }

    private Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(170);
        button.setPrefHeight(46);
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
