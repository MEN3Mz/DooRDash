package game.view;

import javafx.geometry.Pos;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class StatusEffectPane extends HBox {
    private ImageView shieldIcon;
    private ImageView freezeIcon;
    private ImageView momentumIcon;
    private ImageView confusionIcon;

    private final ColorAdjust desaturated = new ColorAdjust();
    private final double INACTIVE_OPACITY = 0.09;
    private final double ACTIVE_OPACITY = 1.0;

    public StatusEffectPane() {
        this.setAlignment(Pos.BOTTOM_CENTER);
        this.setSpacing(15); 
        this.setMaxWidth(Double.MAX_VALUE);
        this.setPrefHeight(50);

        desaturated.setSaturation(-4.0); 
        desaturated.setBrightness(-0.7);

        shieldIcon = createIcon("/game/assets/MonstersInfo/shield.png");
        freezeIcon = createIcon("/game/assets/MonstersInfo/snowflake.png");
        momentumIcon = createIcon("/game/assets/MonstersInfo/power.png");
        confusionIcon = createIcon("/game/assets/MonstersInfo/confusion.png");

        this.getChildren().addAll(shieldIcon, freezeIcon, momentumIcon, confusionIcon);
        
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
        
        iv.setFitWidth(35);
        iv.setPreserveRatio(true);
        
        return iv;
    }

    public void updateEffects(boolean hasShield, boolean isFrozen, boolean hasMomentum, boolean hasFocus) {
        applyStyle(shieldIcon, hasShield);
        applyStyle(freezeIcon, isFrozen);
        applyStyle(momentumIcon, hasMomentum);
        applyStyle(confusionIcon, hasFocus);
    }

    private void applyStyle(ImageView iv, boolean isActive) {
        if (isActive) {
            iv.setEffect(null); 
            iv.setOpacity(ACTIVE_OPACITY);
        } else {
            iv.setEffect(desaturated); 
            iv.setOpacity(INACTIVE_OPACITY);
        }
    }
}