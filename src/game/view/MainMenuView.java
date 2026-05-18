package game.view;

import game.audio.SoundManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
    private final Button exitButton;
    private final Button soundButton;

    public MainMenuView() {

        root = new StackPane();
        root.getStylesheets().add(
                getClass().getResource("/game/assets/css/menu.css").toExternalForm());
        root.getStylesheets().add(
                getClass().getResource("/game/assets/css/main-menu-view.css").toExternalForm());

        startButton = createMenuButton("Start Game");
        settingsButton = createMenuButton("Settings");
        howToPlayButton = createMenuButton("How To Play");
        exitButton = createMenuButton("Exit Game");
        soundButton = createSoundButton();

        ImageView backgroundView = createBackgroundView();

        BorderPane overlay = createOverlay(
                createLogoView(),
                startButton,
                howToPlayButton,
                settingsButton,
                exitButton,
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

    public void setOnExitGame(EventHandler<ActionEvent> handler) {
        exitButton.setOnAction(handler);
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
            Button exitButton,
            Button soundButton) {

        VBox menuContainer = new VBox(20, startButton, howToPlayButton, settingsButton, exitButton);
        menuContainer.setAlignment(Pos.CENTER);

        BorderPane overlay = new BorderPane();
        overlay.getStyleClass().add("main-menu-overlay");

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
        button.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> SoundManager.playHoverSound());
        button.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            SoundManager.playButtonSound();
            button.getStyleClass().remove("menu-button-active");
            button.getStyleClass().add("menu-button-active");
        });
        button.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> button.getStyleClass().remove("menu-button-active"));
        button.addEventHandler(MouseEvent.MOUSE_EXITED, e -> button.getStyleClass().remove("menu-button-active"));

        return button;
    }

    private Button createSoundButton() {
        Button soundButton = new Button(SOUND_ON_TEXT);

        soundButton.getStyleClass().add("sound-on");
        soundButton.setOnMouseEntered(e -> SoundManager.playHoverSound());
        soundButton.setOnMousePressed(e -> SoundManager.playButtonSound());

        soundButton.setOnAction(e -> {
            SoundManager.setSoundOn(!SoundManager.isSoundOn());
            refreshSoundButton();
        });

        refreshSoundButton(soundButton);

        return soundButton;
    }

    public void refreshSoundButton() {
        refreshSoundButton(soundButton);
    }

    private void refreshSoundButton(Button button) {
        boolean soundOn = SoundManager.isSoundOn();
        button.setText(soundOn ? SOUND_ON_TEXT : SOUND_OFF_TEXT);
        button.getStyleClass().removeAll("sound-on", "sound-off");
        button.getStyleClass().add(soundOn ? "sound-on" : "sound-off");
    }

    private Image loadImage(String path) {
        return new Image(getClass().getResourceAsStream(path));
    }
}
