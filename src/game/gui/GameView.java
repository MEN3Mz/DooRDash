package game.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GameView {
    private static final double DEFAULT_SCENE_WIDTH = 1280;
    private static final double DEFAULT_SCENE_HEIGHT = 720;
    private static final double LOGO_WIDTH_RATIO = 0.4;
    private static final double BUTTON_WIDTH_RATIO = 0.25;
    private static final double BUTTON_HEIGHT_RATIO = 0.08;
    private static final double BUTTON_FONT_RATIO = 0.03;
    private static final String SOUND_ON_TEXT = "Sound: ON";
    private static final String SOUND_OFF_TEXT = "Sound: OFF";
    private static final String SOUND_ON_STYLE =
            "-fx-background-color: darkgray; -fx-text-fill: white; -fx-font-weight: bold;";
    private static final String SOUND_OFF_STYLE =
            "-fx-background-color: #555555; -fx-text-fill: lightgray; -fx-font-weight: bold;";
    private static final String MENU_BUTTON_BASE_STYLE =
            "-fx-background-color: "
                    + "linear-gradient(#70b1ff 0%, #1a5cad 50%, #0a3b75 51%, #114b91 100%), "
                    + "linear-gradient(#202020 0%, #111111 100%), "
                    + "linear-gradient(#3e5e8e, #2e4a77); "
                    + "-fx-background-insets: 0,1,2; "
                    + "-fx-background-radius: 5,4,3; "
                    + "-fx-text-fill: white; "
                    + "-fx-font-weight: bold; "
                    + "-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );";
    private static final String MENU_BUTTON_PRESSED_STYLE =
            "-fx-background-color: "
                    + "linear-gradient(#1a5cad 0%, #0a3b75 50%, #051d3a 51%, #082a52 100%), "
                    + "linear-gradient(#101010 0%, #000000 100%), "
                    + "linear-gradient(#2e4a77, #1a2b47); "
                    + "-fx-background-insets: 0,1,2; "
                    + "-fx-background-radius: 5,4,3; "
                    + "-fx-text-fill: #bbbbbb; "
                    + "-fx-translate-y: 2px; "
                    + "-fx-effect: null;";

    private double currentFontSize = DEFAULT_SCENE_HEIGHT * BUTTON_FONT_RATIO;

    public void start(Stage primaryStage) {
        Button startButton = createMenuButton("Start Game", primaryStage);
        Button settingsButton = createMenuButton("Settings", primaryStage);
        Button howToPlayButton = createMenuButton("How To Play", primaryStage);
        Button soundButton = createSoundButton();

        ImageView backgroundView = createBackgroundView(primaryStage);
        BorderPane overlay = createOverlay(
                primaryStage,
                createLogoView(primaryStage),
                startButton,
                howToPlayButton,
                settingsButton,
                soundButton);

        configureResponsiveStyling(primaryStage, startButton, settingsButton, howToPlayButton);

        StackPane root = new StackPane(backgroundView, overlay);
        Scene scene = new Scene(root, DEFAULT_SCENE_WIDTH, DEFAULT_SCENE_HEIGHT);

        primaryStage.setTitle("DoorDash Game - Menu");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private ImageView createBackgroundView(Stage primaryStage) {
        ImageView backgroundView = new ImageView(loadImage("/game/assets/Background.png"));
        backgroundView.fitWidthProperty().bind(primaryStage.widthProperty());
        backgroundView.fitHeightProperty().bind(primaryStage.heightProperty());
        backgroundView.setPreserveRatio(false);
        return backgroundView;
    }

    private ImageView createLogoView(Stage primaryStage) {
        ImageView logoView = new ImageView(loadImage("/game/assets/LOGO.png"));
        logoView.setPreserveRatio(true);
        logoView.fitWidthProperty().bind(primaryStage.widthProperty().multiply(LOGO_WIDTH_RATIO));
        return logoView;
    }

    private BorderPane createOverlay(
            Stage primaryStage,
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

        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> currentFontSize = newVal.doubleValue() * BUTTON_FONT_RATIO);
        return overlay;
    }

    private VBox createMenuContainer(Button... buttons) {
        VBox menuContainer = new VBox(20);
        menuContainer.setAlignment(Pos.CENTER);
        menuContainer.setStyle("-fx-background-color: transparent;");
        menuContainer.getChildren().addAll(buttons);
        return menuContainer;
    }

    private Button createMenuButton(String text, Stage primaryStage) {
        Button button = new Button(text);
        button.prefWidthProperty().bind(primaryStage.widthProperty().multiply(BUTTON_WIDTH_RATIO));
        button.prefHeightProperty().bind(primaryStage.heightProperty().multiply(BUTTON_HEIGHT_RATIO));
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

    private void configureResponsiveStyling(Stage primaryStage, Button... buttons) {
        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
            currentFontSize = newVal.doubleValue() * BUTTON_FONT_RATIO;
            for (Button button : buttons) {
                applyButtonStyle(button, false);
            }
        });

        for (Button button : buttons) {
            applyButtonStyle(button, false);
        }
    }

    private void applyButtonStyle(Button button, boolean pressed) {
        String baseStyle = pressed ? MENU_BUTTON_PRESSED_STYLE : MENU_BUTTON_BASE_STYLE;
        button.setStyle(baseStyle + "-fx-font-size: " + currentFontSize + "px;");
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
