package game.view;

import game.view.StatusEffectPane;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MonsterInfoPane extends Pane {
    private Label posLabel;
    private Label energyLabel;
    private ProgressBar energyBar;
    private StatusEffectPane effectsPane;
    private ImageView capsuleFrame; // Renamed to capsuleFrame

    public MonsterInfoPane() {
        this.setMinWidth(220);
        this.setMaxWidth(220);
        this.setPrefWidth(220);
        this.setMinHeight(350);
        this.setMaxHeight(350);
        this.setPrefHeight(350);

        capsuleFrame = new ImageView();
        capsuleFrame.setFitWidth(300);
        capsuleFrame.setFitHeight(80);
        capsuleFrame.setPreserveRatio(false);
        capsuleFrame.setSmooth(true);
        capsuleFrame.setLayoutX(-20);
        capsuleFrame.setLayoutY(340);

        posLabel = new Label("0");
        posLabel.setFont(Font.font("Arial Rounded MT Bold", FontWeight.BOLD, 32));
        posLabel.setLayoutX(12);
        posLabel.setLayoutY(227);
        posLabel.setStyle("-fx-text-fill: #102033;");
        posLabel.setEffect(new DropShadow(3, Color.WHITE));

        energyBar = new ProgressBar(1.0);
        energyBar.setPrefWidth(90);
        energyBar.setLayoutX(71);
        energyBar.setLayoutY(360);

        energyLabel = new Label("0");
        energyLabel.setFont(Font.font("Arial Rounded MT Bold", FontWeight.BOLD, 32));
        energyLabel.setLayoutX(79);
        energyLabel.setLayoutY(410);
        energyLabel.setStyle("-fx-text-fill: #102033;");
        energyLabel.setEffect(new DropShadow(3, Color.WHITE));

        effectsPane = new StatusEffectPane();
        effectsPane.setLayoutX(18);
        effectsPane.setLayoutY(273);

        this.getChildren().addAll(capsuleFrame, energyBar, effectsPane, posLabel, energyLabel);
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
            java.net.URL frameRes = getClass().getResource(framePath);
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
            java.net.URL res = getClass().getResource(bgImg);
            if (res != null) {
                this.setStyle(
                        "-fx-background-image: url('" + res.toExternalForm() + "');" +
                                "-fx-background-size: 100% 100%;" +
                                "-fx-background-repeat: no-repeat;");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
