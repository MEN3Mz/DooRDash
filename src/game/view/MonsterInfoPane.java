package game.view;

import game.view.StatusEffectPane;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class MonsterInfoPane extends Pane {
    private static final double CARD_WIDTH = 180;
    private static final double EGYPTIAN_SCARER_IMAGE_WIDTH = 205;
    private static final double CARD_HEIGHT = 287;
    private static final double EGYPTIAN_TALL_IMAGE_HEIGHT = 302;
    private static final double PANEL_HEIGHT = 405;

    private Label posLabel;
    private Label energyLabel;
    private Label energyChangeLabel;
    private ProgressBar energyBar;
    private StatusEffectPane effectsPane;
    private ImageView capsuleFrame; // Renamed to capsuleFrame
    private Integer previousEnergy;
    private Timeline energyShake;

    public MonsterInfoPane() {
        getStylesheets().add(ThemeManager.loadStylesheet("/game/assets/css/monster-info-pane.css"));
        this.setMinWidth(CARD_WIDTH);
        this.setMaxWidth(CARD_WIDTH);
        this.setPrefWidth(CARD_WIDTH);
        this.setMinHeight(PANEL_HEIGHT);
        this.setMaxHeight(PANEL_HEIGHT);
        this.setPrefHeight(PANEL_HEIGHT);

        capsuleFrame = new ImageView();
        capsuleFrame.setFitWidth(240);
        capsuleFrame.setFitHeight(64);
        capsuleFrame.setPreserveRatio(false);
        capsuleFrame.setSmooth(true);
        capsuleFrame.setLayoutX(-18);
        capsuleFrame.setLayoutY(ThemeManager.isRetro() ? 293 : 296);

        posLabel = new Label("0");
        posLabel.setFont(Font.font("Arial Rounded MT Bold", FontWeight.BOLD, 26));
        posLabel.setPrefWidth(52);
        posLabel.setAlignment(Pos.CENTER);
        posLabel.setLayoutX(7);
        posLabel.setLayoutY(180);
        posLabel.getStyleClass().add("monster-position-label");
        posLabel.setEffect(new DropShadow(4, Color.BLACK));

        energyBar = new ProgressBar(1.0);
        energyBar.setPrefWidth(74);
        energyBar.setLayoutX(53);
        energyBar.setLayoutY(311);

        energyLabel = new Label("0");
        energyLabel.setFont(Font.font("Arial Rounded MT Bold", FontWeight.BOLD, 28));
        energyLabel.setPrefWidth(120);
        energyLabel.setAlignment(Pos.CENTER);
        energyLabel.setLayoutX(30);
        energyLabel.setLayoutY(360);
        energyLabel.getStyleClass().add("monster-energy-label");
        energyLabel.setEffect(new DropShadow(3, Color.WHITE));

        energyChangeLabel = new Label();
        energyChangeLabel.setFont(Font.font("Arial Rounded MT Bold", FontWeight.BOLD, 13));
        energyChangeLabel.setPrefWidth(58);
        energyChangeLabel.setAlignment(Pos.CENTER_LEFT);
        energyChangeLabel.setLayoutX(128);
        energyChangeLabel.setLayoutY(368);
        energyChangeLabel.setEffect(new DropShadow(3, Color.BLACK));

        effectsPane = new StatusEffectPane();
        effectsPane.setLayoutX(23);
        effectsPane.setLayoutY(228);

        this.getChildren().addAll(capsuleFrame, energyBar, effectsPane, posLabel, energyLabel, energyChangeLabel);
    }

    public void updateUI(String name, String type, String originalRole, String currentRole,
            int energy, int pos, boolean shield, boolean frozen,
            boolean powerUp, boolean confused) {
        updateUI(name, type, originalRole, currentRole, energy, pos, shield, frozen, powerUp, confused,
                shield ? 1 : 0,
                frozen ? 1 : 0,
                powerUp ? 1 : 0,
                confused ? 1 : 0);
    }

    public void updateUI(String name, String type, String originalRole, String currentRole,
            int energy, int pos, boolean shield, boolean frozen,
            boolean powerUp, boolean confused,
            int shieldTurns, int frozenTurns, int powerUpTurns, int confusionTurns) {

        posLabel.setText(String.valueOf(pos));
        posLabel.setFont(Font.font("Arial Rounded MT Bold", FontWeight.BOLD, pos >= 100 ? 21 : 26));
        updateEnergyChangeLabel(energy);
        energyLabel.setText(String.valueOf(energy));

        double progress = energy / 1000.0;
        energyBar.setProgress(progress);

        String liquidColor;
        if (progress <= 0.3)
            liquidColor = "#ff4d4d";
        else if (progress <= 0.5)
            liquidColor = "#ffcc00";
        else
            liquidColor = "#2ecc71";

        energyBar.setStyle(
                "-fx-background-radius: 10; " +
                        "-fx-border-radius: 10; " +
                        "-fx-padding: 2px; " +
                        "-fx-background-color: linear-gradient(to bottom, #444, #888, #444); " + // Metallic Track
                        "-fx-accent: " + liquidColor + "; " +
                        "-fx-control-inner-background: linear-gradient(to bottom, derive(" + liquidColor + ", 40%) 0%, "
                        + liquidColor + " 50%, derive(" + liquidColor + ", -20%) 100%);");

        effectsPane.updateEffects(
                shield,
                frozen,
                powerUp,
                confused,
                shieldTurns,
                frozenTurns,
                powerUpTurns,
                confusionTurns);

        try {
            String framePath = "/game/assets/MonstersInfo/capsule_frame.png";
            java.net.URL frameRes = ThemeManager.resolveImageUrl(framePath);
            if (frameRes != null) {
                capsuleFrame.setImage(new Image(frameRes.toExternalForm()));
            }
        } catch (Exception e) {

        }

        String bgImg = "";
        String folderPath = "/game/assets/MonstersInfo/";

        boolean isConfused = (originalRole != null && !originalRole.equalsIgnoreCase(currentRole));

        if (isConfused) {
            switch (name) {
                case "Mike Wazowski":
                    bgImg = folderPath + "confused_mike.png";
                    break;
                case "Celia Mae":
                    bgImg = folderPath + "confused_celia.png";
                    break;
                case "george":
                    bgImg = folderPath + "confused_george.png";
                    break;
                case "Fungus":
                    bgImg = folderPath + "confused_fungus.png";
                    break;
                case "Yeti":
                    bgImg = folderPath + "confused_yeti.png";
                    break;
                case "James P. Sullivan":
                    bgImg = folderPath + "confused_james.png";
                    break;
                case "Randall Boggs":
                    bgImg = folderPath + "confused_randall.png";
                    break;
                case "Roz":
                    bgImg = folderPath + "confused_roz.png";
                    break;
                case "Henry J. Waternoose":
                    bgImg = folderPath + "confused_henry.png";
                    break;
                default:
                    bgImg = folderPath + "confused_default.png";
            }
        } else if (currentRole.equalsIgnoreCase("LAUGHER")) {
            switch (name) {
                case "Mike Wazowski":
                    bgImg = folderPath + "laugher_mike.png";
                    break;
                case "Celia Mae":
                    bgImg = folderPath + "laugher_celia.png";
                    break;
                case "george":
                    bgImg = folderPath + "laugher_george.png";
                    break;
                case "Fungus":
                    bgImg = folderPath + "laugher_fungus.png";
                    break;
                case "Yeti":
                    bgImg = folderPath + "laugher_yeti.png";
                    break;
                default:
                    bgImg = folderPath + "laugher_default.png";
            }
        } else if (currentRole.equalsIgnoreCase("SCARER")) {
            switch (name) {
                case "James P. Sullivan":
                    bgImg = folderPath + "scarer_james.png";
                    break;
                case "Randall Boggs":
                    bgImg = folderPath + "scarer_randall.png";
                    break;
                case "Roz":
                    bgImg = folderPath + "scarer_roz.png";
                    break;
                case "Henry J. Waternoose":
                    bgImg = folderPath + "scarer_henry.png";
                    break;
                default:
                    bgImg = folderPath + "scarer_default.png";
            }
        }

        try {
            java.net.URL res = ThemeManager.resolveImageUrl(bgImg);
            if (res != null) {
                double backgroundImageWidth = getBackgroundImageWidth(currentRole);
                double backgroundImageHeight = getBackgroundImageHeight(currentRole, isConfused);
                String backgroundPosition = ThemeManager.isAncientEgyptian() && isConfused ? "center -8px" : "top center";
                this.setStyle(
                        "-fx-background-image: url('" + res.toExternalForm() + "');" +
                                "-fx-background-size: " + backgroundImageWidth + "px " + backgroundImageHeight + "px;" +
                                "-fx-background-position: " + backgroundPosition + ";" +
                                "-fx-background-repeat: no-repeat;");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double getBackgroundImageWidth(String currentRole) {
        if (ThemeManager.isAncientEgyptian()
                && currentRole != null
                && currentRole.equalsIgnoreCase("SCARER")) {
            return EGYPTIAN_SCARER_IMAGE_WIDTH;
        }

        return CARD_WIDTH;
    }

    private double getBackgroundImageHeight(String currentRole, boolean isConfused) {
        if (ThemeManager.isAncientEgyptian()
                && (isConfused || (currentRole != null && currentRole.equalsIgnoreCase("SCARER")))) {
            return EGYPTIAN_TALL_IMAGE_HEIGHT;
        }

        return CARD_HEIGHT;
    }

    private void updateEnergyChangeLabel(int energy) {
        if (previousEnergy == null) {
            energyChangeLabel.setText("");
            previousEnergy = energy;
            return;
        }

        int change = energy - previousEnergy;
        previousEnergy = energy;

        if (change == 0) {
            energyChangeLabel.setText("");
            return;
        }

        energyChangeLabel.setText(change > 0 ? "+" + change : String.valueOf(change));
        energyChangeLabel.getStyleClass().removeAll("energy-change-gain", "energy-change-loss");
        energyChangeLabel.getStyleClass().add(change > 0 ? "energy-change-gain" : "energy-change-loss");
        playEnergyShake();
    }

    private void playEnergyShake() {
        if (energyShake != null) {
            energyShake.stop();
        }

        energyShake = new Timeline(
                new KeyFrame(Duration.ZERO, event -> setEnergyShakeOffset(0)),
                new KeyFrame(Duration.millis(45), event -> setEnergyShakeOffset(-4)),
                new KeyFrame(Duration.millis(90), event -> setEnergyShakeOffset(4)),
                new KeyFrame(Duration.millis(135), event -> setEnergyShakeOffset(-3)),
                new KeyFrame(Duration.millis(180), event -> setEnergyShakeOffset(3)),
                new KeyFrame(Duration.millis(225), event -> setEnergyShakeOffset(0)));
        energyShake.play();
    }

    private void setEnergyShakeOffset(double offset) {
        capsuleFrame.setTranslateX(offset);
        energyBar.setTranslateX(offset);
    }
}
