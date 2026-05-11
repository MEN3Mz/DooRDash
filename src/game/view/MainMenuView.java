package game.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MainMenuView {

    private static final String SOUND_ON_TEXT = "Sound: ON";
    private static final String SOUND_OFF_TEXT = "Sound: OFF";

    private final StackPane root;
    private final Button startButton;
    private final Button settingsButton;
    private final Button howToPlayButton;
    private final Button soundButton;

    public MainMenuView() {

        root = new StackPane();
        root.getStylesheets().add(
                getClass().getResource("/game/assets/css/menu.css").toExternalForm());

        startButton = createMenuButton("Start Game");
        settingsButton = createMenuButton("Settings");
        howToPlayButton = createMenuButton("How To Play");
        soundButton = createSoundButton();

        ImageView backgroundView = createBackgroundView();

        BorderPane overlay = createOverlay(
                createLogoView(),
                startButton,
                howToPlayButton,
                settingsButton,
                soundButton);

        root.getChildren().addAll(backgroundView, overlay);
    }

    public Parent getRoot() {
        return root;
    }

    public void setOnStartGame(EventHandler<ActionEvent> handler) {
        startButton.setOnAction(handler);
    }

    public void setOnSettings(EventHandler<ActionEvent> handler) {
        settingsButton.setOnAction(handler);
    }

    public void setOnHowToPlay(EventHandler<ActionEvent> handler) {
        howToPlayButton.setOnAction(handler);
    }

    private ImageView createBackgroundView() {
        ImageView backgroundView = new ImageView(loadImage("/game/assets/Background.png"));

        backgroundView.setPreserveRatio(false);
        backgroundView.fitWidthProperty().bind(root.widthProperty());
        backgroundView.fitHeightProperty().bind(root.heightProperty());

        return backgroundView;
    }

    private ImageView createLogoView() {
        ImageView logoView = new ImageView(loadImage("/game/assets/LOGO.png"));

        logoView.setPreserveRatio(true);
        logoView.setFitWidth(420);

        return logoView;
    }

    private BorderPane createOverlay(
            ImageView logoView,
            Button startButton,
            Button howToPlayButton,
            Button settingsButton,
            Button soundButton) {

        VBox menuContainer = new VBox(20, startButton, howToPlayButton, settingsButton);
        menuContainer.setAlignment(Pos.CENTER);

        BorderPane overlay = new BorderPane();
        overlay.setStyle("-fx-background-color: transparent;");

        overlay.setTop(logoView);
        overlay.setCenter(menuContainer);
        overlay.setBottom(soundButton);

        BorderPane.setAlignment(logoView, Pos.CENTER);
        BorderPane.setMargin(logoView, new Insets(50, 0, 0, 0));

        BorderPane.setAlignment(soundButton, Pos.BOTTOM_LEFT);
        BorderPane.setMargin(soundButton, new Insets(20));

        return overlay;
    }

    private Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(320);
        button.setPrefHeight(58);

        button.getStyleClass().add("menu-button");
        button.getStyleClass().add("menu-font");

        return button;
    }

    private Button createSoundButton() {
        Button soundButton = new Button(SOUND_ON_TEXT);

        soundButton.getStyleClass().add("sound-on");

        soundButton.setOnAction(e -> {
            boolean isOn = SOUND_ON_TEXT.equals(soundButton.getText());

            soundButton.setText(isOn ? SOUND_OFF_TEXT : SOUND_ON_TEXT);

            soundButton.getStyleClass().removeAll("sound-on", "sound-off");
            soundButton.getStyleClass().add(isOn ? "sound-off" : "sound-on");
        });

        return soundButton;
    }

    private Image loadImage(String path) {
        return new Image(getClass().getResourceAsStream(path));
    }
}