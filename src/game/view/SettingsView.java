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

    private final StackPane root;
    private final Slider musicSlider;
    private final Slider effectsSlider;
    private final Button backButton;

    public SettingsView() {
        root = new StackPane();
        root.getStylesheets().add(
                getClass().getResource("/game/assets/css/menu.css").toExternalForm());

        musicSlider = createMusicSlider();
        effectsSlider = createEffectsSlider();
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
        title.setStyle("""
                -fx-font-size: 30px;
                -fx-font-weight: 900;
                -fx-text-fill: white;
                """);

        Label musicLabel = createSectionLabel("Background Music");
        Label effectsLabel = createSectionLabel("Game Sounds");

        VBox content = new VBox(18, title, musicLabel, musicSlider, effectsLabel, effectsSlider, backButton);
        content.setAlignment(Pos.CENTER);
        content.setMaxWidth(520);
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

    private Label createSectionLabel(String text) {
        Label label = new Label(text);
        label.setStyle("""
                -fx-font-size: 17px;
                -fx-font-weight: bold;
                -fx-text-fill: #cfe4ff;
                """);

        return label;
    }

    private Slider createMusicSlider() {
        Slider slider = createSlider(SoundManager.getMusicVolume());
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            SoundManager.setMusicVolume(newValue.doubleValue() / 100.0);
        });

        return slider;
    }

    private Slider createEffectsSlider() {
        Slider slider = createSlider(SoundManager.getEffectsVolume());
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            SoundManager.setEffectsVolume(newValue.doubleValue() / 100.0);
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

    private Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(240);
        button.setPrefHeight(52);
        button.getStyleClass().add("menu-button");
        button.getStyleClass().add("menu-font");
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
