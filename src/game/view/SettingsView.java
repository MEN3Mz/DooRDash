package game.view;

import game.audio.SoundManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SettingsView {
    private static final String SOUND_ON_TEXT = "Sound: ON";
    private static final String SOUND_OFF_TEXT = "Sound: OFF";

    private final StackPane root;
    private final Slider musicSlider;
    private final Slider effectsSlider;
    private final Button soundToggleButton;
    private final Button backButton;

    public SettingsView() {
        root = new StackPane();
        root.getStylesheets().add(
                getClass().getResource("/game/assets/css/menu.css").toExternalForm());
        root.getStylesheets().add(
                getClass().getResource("/game/assets/css/settings-view.css").toExternalForm());

        musicSlider = createMusicSlider();
        effectsSlider = createEffectsSlider();
        soundToggleButton = createSoundToggleButton();
        backButton = createMenuButton("Back");

        Rectangle overlayBackground = createOverlayBackground();
        VBox content = createContent();

        root.getChildren().addAll(overlayBackground, content);
        StackPane.setAlignment(content, Pos.CENTER);
    }

    public Parent getRoot() {
        return root;
    }

    public void setOnBack(EventHandler<ActionEvent> handler) {
        backButton.setOnAction(handler);
    }

    private VBox createContent() {
        Label title = new Label("Settings");
        title.getStyleClass().add("settings-title");

        Label musicLabel = createSectionLabel("Background Music");
        Label effectsLabel = createSectionLabel("Game Sounds");

        VBox content = new VBox(18, title, soundToggleButton, musicLabel, musicSlider, effectsLabel, effectsSlider, backButton);
        content.setAlignment(Pos.CENTER);
        content.setMaxWidth(520);
        content.getStyleClass().add("settings-popup");

        return content;
    }

    private Label createSectionLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("settings-section-label");

        return label;
    }

    private Slider createMusicSlider() {
        Slider slider = createSlider(SoundManager.getMusicVolume());
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            SoundManager.setMusicVolume(newValue.doubleValue() / 100.0);
            refreshSoundToggleButton();
        });

        return slider;
    }

    private Slider createEffectsSlider() {
        Slider slider = createSlider(SoundManager.getEffectsVolume());
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            SoundManager.setEffectsVolume(newValue.doubleValue() / 100.0);
            refreshSoundToggleButton();
        });

        return slider;
    }

    private Slider createSlider(double initialValue) {
        Slider slider = new Slider(0, 100, initialValue * 100);
        slider.setMaxWidth(360);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMajorTickUnit(25);
        slider.setBlockIncrement(5);

        return slider;
    }

    private Button createSoundToggleButton() {
        Button button = createMenuButton("");
        button.setPrefWidth(180);
        button.setPrefHeight(42);
        button.setOnAction(e -> {
            SoundManager.setSoundOn(!SoundManager.isSoundOn());
            musicSlider.setValue(SoundManager.getMusicVolume() * 100);
            effectsSlider.setValue(SoundManager.getEffectsVolume() * 100);
            refreshSoundToggleButton();
        });
        refreshSoundToggleButton(button);

        return button;
    }

    private void refreshSoundToggleButton() {
        refreshSoundToggleButton(soundToggleButton);
    }

    private void refreshSoundToggleButton(Button button) {
        boolean soundOn = SoundManager.isSoundOn();
        button.setText(soundOn ? SOUND_ON_TEXT : SOUND_OFF_TEXT);
        button.getStyleClass().removeAll("sound-on", "sound-off");
        button.getStyleClass().add(soundOn ? "sound-on" : "sound-off");
    }

    private Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(240);
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
