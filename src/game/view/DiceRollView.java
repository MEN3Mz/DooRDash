package game.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DiceRollView {

    private final HBox root;
    private final ImageView diceView;
    private final Button rollButton;
    private boolean animating;

    public DiceRollView() {

        diceView = new ImageView();
        diceView.setFitWidth(80);
        diceView.setFitHeight(80);
        diceView.setPreserveRatio(true);

        // default dice
        setDiceValue(5);

        rollButton = new Button("Roll Dice");

        root = new HBox(15, diceView, rollButton);
        root.setAlignment(Pos.CENTER_LEFT);
    }

    public void setDiceValue(int value) {

        Image image = new Image(
                getClass().getResourceAsStream(
                        "/game/assets/dice/dice" + value + ".png"));

        diceView.setImage(image);
    }

    public Button getRollButton() {
        return rollButton;
    }

    public HBox getRoot() {
        return root;
    }
}