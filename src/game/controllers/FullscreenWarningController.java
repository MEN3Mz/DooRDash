package game.controllers;

import game.view.ThemeManager;
import javafx.animation.TranslateTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class FullscreenWarningController implements Initializable {
    @FXML
    private Label alertLabel;
    @FXML
    private Button fullscreenButton;

    private Runnable onFullscreen;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fullscreenButton.setStyle(ThemeManager.getFullscreenButtonStyle());
        Platform.runLater(() -> animateLabel());
    }

    private void animateLabel() {
        // Move up
        TranslateTransition moveUp = new TranslateTransition(Duration.seconds(1), alertLabel);
        moveUp.setByY(-35);
        moveUp.setAutoReverse(false);

        // Move down
        TranslateTransition moveDown = new TranslateTransition(Duration.seconds(1), alertLabel);
        moveDown.setByY(35);
        moveDown.setAutoReverse(false);

        // Sequential animation (up then down)
        SequentialTransition bounce = new SequentialTransition(moveUp, moveDown);
        bounce.setCycleCount(TranslateTransition.INDEFINITE);
        bounce.play();
    }

    public void setOnFullscreen(Runnable onFullscreen) {
        this.onFullscreen = onFullscreen;
        fullscreenButton.setOnAction(event -> {
            if (this.onFullscreen != null) {
                this.onFullscreen.run();
            }
        });
    }

}
