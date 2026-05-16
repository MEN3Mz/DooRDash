package game.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class StatusEffectPane extends HBox {
    private ImageView shieldIcon;
    private ImageView freezeIcon;
    private ImageView momentumIcon;
    private ImageView confusionIcon;
    private Label shieldCounter;
    private Label freezeCounter;
    private Label momentumCounter;
    private Label confusionCounter;

    private final ColorAdjust desaturated = new ColorAdjust();
    private final double INACTIVE_OPACITY = 0.09;
    private final double ACTIVE_OPACITY = 1.0;

    public StatusEffectPane() {
        getStylesheets().add(getClass().getResource("/game/assets/css/monster-info-pane.css").toExternalForm());
        this.setAlignment(Pos.BOTTOM_CENTER);
        this.setSpacing(4);
        this.setMaxWidth(Double.MAX_VALUE);
        this.setPrefHeight(42);

        desaturated.setSaturation(-4.0);
        desaturated.setBrightness(-0.7);

        shieldIcon = createIcon("/game/assets/MonstersInfo/shield.png");
        freezeIcon = createIcon("/game/assets/MonstersInfo/snowflake.png");
        momentumIcon = createIcon("/game/assets/MonstersInfo/power.png");
        confusionIcon = createIcon("/game/assets/MonstersInfo/confusion.png");

        shieldCounter = createCounter();
        freezeCounter = createCounter();
        momentumCounter = createCounter();
        confusionCounter = createCounter();

        this.getChildren().addAll(
                createIconStack(shieldIcon, shieldCounter),
                createIconStack(freezeIcon, freezeCounter),
                createIconStack(momentumIcon, momentumCounter),
                createIconStack(confusionIcon, confusionCounter));

        updateEffects(false, false, false, false);
    }

    private ImageView createIcon(String path) {
        java.net.URL resource = getClass().getResource(path);

        if (resource == null) {
            System.err.println("Error: Could not find image at " + path);
            return new ImageView();
        }

        String urlString = resource.toExternalForm();
        Image img = new Image(urlString);
        ImageView iv = new ImageView(img);

        iv.setFitWidth(28);
        iv.setPreserveRatio(true);

        return iv;
    }

    public void updateEffects(boolean hasShield, boolean isFrozen, boolean hasMomentum, boolean isConfused) {
        updateEffects(hasShield, isFrozen, hasMomentum, isConfused,
                hasShield ? 1 : 0,
                isFrozen ? 1 : 0,
                hasMomentum ? 1 : 0,
                isConfused ? 1 : 0);
    }

    public void updateEffects(
            boolean hasShield,
            boolean isFrozen,
            boolean hasMomentum,
            boolean isConfused,
            int shieldTurns,
            int frozenTurns,
            int momentumTurns,
            int confusionTurns) {

        applyStyle(shieldIcon, shieldCounter, hasShield, shieldTurns);
        applyStyle(freezeIcon, freezeCounter, isFrozen, frozenTurns);
        applyStyle(momentumIcon, momentumCounter, hasMomentum, momentumTurns);
        applyStyle(confusionIcon, confusionCounter, isConfused, confusionTurns);
    }

    private void applyStyle(ImageView iv, Label counter, boolean isActive, int turns) {
        if (isActive) {
            iv.setEffect(null);
            iv.setOpacity(ACTIVE_OPACITY);
            counter.setText(String.valueOf(Math.max(1, turns)));
            counter.setOpacity(ACTIVE_OPACITY);
        } else {
            iv.setEffect(desaturated);
            iv.setOpacity(INACTIVE_OPACITY);
            counter.setText("0");
            counter.setOpacity(0.95);
        }
    }

    private VBox createIconStack(ImageView icon, Label counter) {
        VBox stack = new VBox(0, icon, counter);
        stack.setAlignment(Pos.CENTER);

        return stack;
    }

    private Label createCounter() {
        Label label = new Label("0");
        label.getStyleClass().add("status-counter");
        label.setEffect(new DropShadow(2, Color.BLACK));

        return label;
    }
}
