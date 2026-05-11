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
    private static final double LOGO_WIDTH_RATIO = 0.4;
    private static final String SOUND_ON_TEXT = "Sound: ON";
    private static final String SOUND_OFF_TEXT = "Sound: OFF";
    private static final String SOUND_ON_STYLE = "-fx-background-color: darkgray; -fx-text-fill: white; -fx-font-weight: bold;";
    private static final String SOUND_OFF_STYLE = "-fx-background-color: #555555; -fx-text-fill: lightgray; -fx-font-weight: bold;";
    private static final String MENU_BUTTON_BASE_STYLE = "-fx-background-color: "
            + "linear-gradient(#70b1ff 0%, #1a5cad 50%, #0a3b75 51%, #114b91 100%), "
            + "linear-gradient(#202020 0%, #111111 100%), "
            + "linear-gradient(#3e5e8e, #2e4a77); "
            + "-fx-background-insets: 0,1,2; "
            + "-fx-background-radius: 5,4,3; "
            + "-fx-text-fill: white; "
            + "-fx-font-weight: bold; "
            + "-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );";
    private static final String MENU_BUTTON_PRESSED_STYLE = "-fx-background-color: "
            + "linear-gradient(#1a5cad 0%, #0a3b75 50%, #051d3a 51%, #082a52 100%), "
            + "linear-gradient(#101010 0%, #000000 100%), "
            + "linear-gradient(#2e4a77, #1a2b47); "
            + "-fx-background-insets: 0,1,2; "
            + "-fx-background-radius: 5,4,3; "
            + "-fx-text-fill: #bbbbbb; "
            + "-fx-translate-y: 2px; "
            + "-fx-effect: null;";

    private final StackPane root;
    private final Button startButton;
    private final Button settingsButton;
    private final Button howToPlayButton;
    private final Button soundButton;

    public MainMenuView() {

        root = new StackPane();

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

    public Button getStartButton() {
        return startButton;
    }

    public Button getSettingsButton() {
        return settingsButton;
    }

    public Button getHowToPlayButton() {
        return howToPlayButton;
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

        // Make it fill the entire window
        backgroundView.fitWidthProperty().bind(root.widthProperty());
        backgroundView.fitHeightProperty().bind(root.heightProperty());

        return backgroundView;
    }

    private ImageView createLogoView() {
        ImageView logoView = new ImageView(loadImage("/game/assets/LOGO.png"));
        logoView.setPreserveRatio(true);
        logoView.setFitWidth(420 * LOGO_WIDTH_RATIO / 0.4);
        return logoView;
    }

    private BorderPane createOverlay(
            ImageView logoView,
            Button startButton,
            Button howToPlayButton,
            Button settingsButton,
            Button soundButton) {
        VBox menuContainer = createMenuContainer(startButton, howToPlayButton, settingsButton);

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

    private VBox createMenuContainer(Button... buttons) {
        VBox menuContainer = new VBox(20);
        menuContainer.setAlignment(Pos.CENTER);
        menuContainer.setStyle("-fx-background-color: transparent;");
        menuContainer.getChildren().addAll(buttons);
        return menuContainer;
    }

    private Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(320);
        button.setPrefHeight(58);
        applyButtonStyle(button, false);
        button.setOnMousePressed(event -> applyButtonStyle(button, true));
        button.setOnMouseReleased(event -> applyButtonStyle(button, false));
        return button;
    }

    private Button createSoundButton() {
        Button soundButton = new Button(SOUND_ON_TEXT);
        soundButton.setStyle(SOUND_ON_STYLE);
        soundButton.setOnAction(event -> toggleSoundButton(soundButton));
        return soundButton;
    }

    private void applyButtonStyle(Button button, boolean pressed) {
        String baseStyle = pressed ? MENU_BUTTON_PRESSED_STYLE : MENU_BUTTON_BASE_STYLE;
        button.setStyle(baseStyle + "-fx-font-size: 22px;");
    }

    private void toggleSoundButton(Button soundButton) {
        boolean soundIsOn = SOUND_ON_TEXT.equals(soundButton.getText());
        soundButton.setText(soundIsOn ? SOUND_OFF_TEXT : SOUND_ON_TEXT);
        soundButton.setStyle(soundIsOn ? SOUND_OFF_STYLE : SOUND_ON_STYLE);
    }

    private Image loadImage(String resourcePath) {
        return new Image(getClass().getResourceAsStream(resourcePath));
    }
}
