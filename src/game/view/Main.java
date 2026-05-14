package game.view;

import game.controllers.FullscreenWarningController;
import game.audio.SoundManager;
import game.controllers.MainMenuController;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    private Stage primaryStage;
    private Parent fullscreenWarningOverlay;
    private Label fullscreenExitHint;
    private PauseTransition fullscreenHintTimer;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        SoundManager.startSoundtrack();

        new MainMenuController(primaryStage);

        primaryStage.show();

        primaryStage.setFullScreenExitHint("");
        installFullscreenWarning();
    }

    private void installFullscreenWarning() {
        primaryStage.fullScreenProperty().addListener((observable, wasFullscreen, isFullscreen) -> {
            if (isFullscreen) {
                hideFullscreenWarning();
                showFullscreenExitHint();
            } else {
                hideFullscreenExitHint();
                showFullscreenWarningIfNeeded();
            }
        });

        primaryStage.maximizedProperty().addListener((observable, wasMaximized, isMaximized) -> {
            if (isMaximized && !primaryStage.isFullScreen()) {
                primaryStage.setMaximized(false);
                primaryStage.setFullScreen(true);
            }
        });

        primaryStage.getScene().rootProperty().addListener((observable, oldRoot, newRoot) -> {
            showFullscreenWarningIfNeeded();
        });

        showFullscreenWarningIfNeeded();
    }

    private void showFullscreenWarningIfNeeded() {
        if (primaryStage.isFullScreen() || primaryStage.getScene() == null) {
            return;
        }

        if (!(primaryStage.getScene().getRoot() instanceof StackPane)) {
            return;
        }

        StackPane root = (StackPane) primaryStage.getScene().getRoot();

        if (fullscreenWarningOverlay != null && root.getChildren().contains(fullscreenWarningOverlay)) {
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/game/view/FullscreenWarningView.fxml"));
            fullscreenWarningOverlay = loader.load();

            FullscreenWarningController controller = loader.getController();
            controller.setOnFullscreen(() -> {
                hideFullscreenWarning();
                primaryStage.setFullScreen(true);
            });

            root.getChildren().add(fullscreenWarningOverlay);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void hideFullscreenWarning() {
        if (fullscreenWarningOverlay == null || primaryStage.getScene() == null) {
            return;
        }

        if (primaryStage.getScene().getRoot() instanceof StackPane) {
            ((StackPane) primaryStage.getScene().getRoot()).getChildren().remove(fullscreenWarningOverlay);
        }

        fullscreenWarningOverlay = null;
    }

    private void showFullscreenExitHint() {
        if (primaryStage.getScene() == null || !(primaryStage.getScene().getRoot() instanceof StackPane)) {
            return;
        }

        StackPane root = (StackPane) primaryStage.getScene().getRoot();
        hideFullscreenExitHint();

        fullscreenExitHint = new Label("Press ESC to exit full screen mode");
        fullscreenExitHint.setStyle("""
                -fx-background-color: rgba(0,0,0,0.72);
                -fx-background-radius: 12;
                -fx-text-fill: white;
                -fx-font-size: 18px;
                -fx-font-weight: bold;
                -fx-padding: 14 24;
                """);

        root.getChildren().add(fullscreenExitHint);
        StackPane.setAlignment(fullscreenExitHint, Pos.TOP_CENTER);

        fullscreenHintTimer = new PauseTransition(Duration.seconds(3));
        fullscreenHintTimer.setOnFinished(event -> hideFullscreenExitHint());
        fullscreenHintTimer.playFromStart();
    }

    private void hideFullscreenExitHint() {
        if (fullscreenHintTimer != null) {
            fullscreenHintTimer.stop();
            fullscreenHintTimer = null;
        }

        if (fullscreenExitHint == null || primaryStage.getScene() == null) {
            return;
        }

        if (primaryStage.getScene().getRoot() instanceof StackPane) {
            ((StackPane) primaryStage.getScene().getRoot()).getChildren().remove(fullscreenExitHint);
        }

        fullscreenExitHint = null;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
