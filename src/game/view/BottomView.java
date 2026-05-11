package game.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.util.Random;

public class BottomView {

    private final HBox root;
    private final ImageView diceView;
    private final Button rollButton;
    private final Button powerUpButton;

    public BottomView() {

        diceView = new ImageView();
        diceView.setFitWidth(80);
        diceView.setFitHeight(80);
        diceView.setPreserveRatio(true);

        setDiceValue(5);

        rollButton = new Button("Roll Dice");
        powerUpButton = new Button("Power Up");

        rollButton.getStyleClass().add("roll-button");
        powerUpButton.getStyleClass().add("power-up-button");

        root = new HBox(15, powerUpButton, diceView, rollButton);
        root.setAlignment(Pos.CENTER_LEFT);

        root.getStylesheets().add(
                getClass().getResource("/game/assets/css/buttons.css").toExternalForm());
    }

    // ------------------------
    // UI UPDATE METHODS
    // ------------------------

    public void setDiceValue(int value) {
        Image image = new Image(
                getClass().getResourceAsStream(
                        "/game/assets/dice/dice" + value + ".png"));

        diceView.setImage(image);
    }

    // ------------------------
    // CONTROLLER HOOKS
    // ------------------------

    public void setOnRollDice(EventHandler<ActionEvent> handler) {
        rollButton.setOnAction(handler);
    }

    public void setOnPowerUp(EventHandler<ActionEvent> handler) {
        powerUpButton.setOnAction(handler);
    }

    // ------------------------
    // GETTERS
    // ------------------------

    public HBox getRoot() {
        return root;
    }

    public void animateDiceRoll(int finalValue, Runnable onFinished) {
        Random random = new Random();

        Timeline timeline = new Timeline();

        for (int i = 0; i < 12; i++) {
            int fakeValue = random.nextInt(6) + 1;

            timeline.getKeyFrames().add(
                    new KeyFrame(Duration.millis(i * 80), e -> setDiceValue(fakeValue)));
        }

        timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(12 * 80), e -> {
                    setDiceValue(finalValue);
                    if (onFinished != null)
                        onFinished.run();
                }));

        timeline.play();
    }

}