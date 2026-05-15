package game.view;

import game.audio.SoundManager;
import game.engine.Role;
import game.engine.monsters.Monster;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class GameOverView {

    private final StackPane root;
    private final Button mainMenuButton;
    private final Button exitButton;

    public GameOverView(Monster winner) {
        this(winner, winner, null, "Player");
    }

    public GameOverView(Monster winner, Monster player, Monster opponent, String winnerPlayerName) {
        root = new StackPane();
        root.getStylesheets().add(
                getClass().getResource("/game/assets/css/menu.css").toExternalForm());

        mainMenuButton = createMenuButton("Main Menu", winner.getOriginalRole());
        exitButton = createMenuButton("Exit Game", winner.getOriginalRole());

        ImageView backgroundView = createBackgroundView(winner);
        VBox content = createContent(winner, player, opponent, winnerPlayerName);

        root.getChildren().addAll(backgroundView, content);
        StackPane.setAlignment(content, Pos.CENTER);
    }

    public Parent getRoot() {
        return root;
    }

    public void setOnMainMenu(EventHandler<ActionEvent> handler) {
        mainMenuButton.setOnAction(handler);
    }

    public void setOnExit(EventHandler<ActionEvent> handler) {
        exitButton.setOnAction(handler);
    }

    private VBox createContent(Monster winner, Monster player, Monster opponent, String winnerPlayerName) {
        VBox content = new VBox(28);
        content.setAlignment(Pos.CENTER);
        content.setMaxWidth(900);

        Label winnerLabel = new Label("Winner: " + winnerPlayerName + " - " + winner.getName());
        winnerLabel.setStyle("""
                -fx-font-size: 26px;
                -fx-font-weight: 900;
                -fx-text-fill: white;
                """);
        winnerLabel.setGraphic(createMonsterIcon(winner));
        winnerLabel.setGraphicTextGap(12);
        winnerLabel.setTextAlignment(TextAlignment.CENTER);
        winnerLabel.setEffect(new DropShadow(5, Color.BLACK));

        HBox energyRow = new HBox(18);
        energyRow.setAlignment(Pos.CENTER);

        if (player != null) {
            energyRow.getChildren().add(createEnergyBox(player, player == winner));
        }

        if (opponent != null) {
            energyRow.getChildren().add(createEnergyBox(opponent, opponent == winner));
        }

        HBox buttonRow = new HBox(18, mainMenuButton, exitButton);
        buttonRow.setAlignment(Pos.CENTER);

        content.getChildren().addAll(winnerLabel, energyRow, buttonRow);
        return content;
    }

    private ImageView createMonsterIcon(Monster monster) {
        ImageView icon = new ImageView(loadImage(getMonsterImagePath(monster)));
        icon.setFitWidth(52);
        icon.setFitHeight(52);
        icon.setPreserveRatio(true);
        icon.setSmooth(true);

        return icon;
    }

    private VBox createEnergyBox(Monster monster, boolean isWinner) {
        VBox box = new VBox(8);
        box.setAlignment(Pos.CENTER);
        box.setPrefWidth(260);
        box.setStyle(isWinner ? getWinningEnergyBoxStyle(monster.getOriginalRole()) : """
                -fx-background-color: rgba(0,0,0,0.55);
                -fx-background-radius: 14;
                -fx-border-color: rgba(255,255,255,0.25);
                -fx-border-radius: 14;
                -fx-padding: 16;
                """);

        Label teamLabel = new Label(getTeamName(monster.getOriginalRole()));
        teamLabel.setStyle("""
                -fx-font-size: 18px;
                -fx-font-weight: 900;
                -fx-text-fill: white;
                """);

        Label monsterLabel = new Label(monster.getName());
        monsterLabel.setStyle("""
                -fx-font-size: 14px;
                -fx-font-weight: bold;
                -fx-text-fill: #dce8f5;
                """);

        Label energyLabel = new Label("Energy: " + monster.getEnergy());
        energyLabel.setStyle("""
                -fx-font-size: 24px;
                -fx-font-weight: 900;
                -fx-text-fill: #cfe4ff;
                """);

        box.getChildren().addAll(teamLabel, monsterLabel, energyLabel);
        return box;
    }

    private String getWinningEnergyBoxStyle(Role role) {
        if (role == Role.LAUGHER) {
            return """
                    -fx-background-color: rgba(30,144,255,0.72);
                    -fx-background-radius: 14;
                    -fx-border-color: rgba(210,235,255,0.95);
                    -fx-border-width: 2;
                    -fx-border-radius: 14;
                    -fx-padding: 16;
                    -fx-effect: dropshadow(three-pass-box, rgba(80,180,255,0.75), 22, 0, 0, 0);
                    """;
        }

        return """
                -fx-background-color: rgba(220,38,38,0.76);
                -fx-background-radius: 14;
                -fx-border-color: rgba(255,220,220,0.95);
                -fx-border-width: 2;
                -fx-border-radius: 14;
                -fx-padding: 16;
                -fx-effect: dropshadow(three-pass-box, rgba(255,80,80,0.75), 22, 0, 0, 0);
                """;
    }

    private Button createMenuButton(String text, Role winnerRole) {
        Button button = new Button(text);
        button.setPrefWidth(260);
        button.setPrefHeight(58);
        button.getStyleClass().add("menu-button");
        button.getStyleClass().add("menu-font");
        if (winnerRole == Role.SCARER) {
            button.setStyle("""
                    -fx-background-color:
                        linear-gradient(#ff8a8a 0%, #b91c1c 50%, #6f0f0f 51%, #9f1717 100%),
                        linear-gradient(#202020 0%, #111111 100%),
                        linear-gradient(#8e3e3e, #772e2e);
                    -fx-background-insets: 0, 1, 2;
                    -fx-background-radius: 5, 4, 3;
                    -fx-text-fill: white;
                    -fx-font-weight: bold;
                    -fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.6), 5, 0, 0, 1);
                    """);
            button.setOnMousePressed(e -> {
                SoundManager.playButtonSound();
                button.setStyle("""
                        -fx-background-color:
                            linear-gradient(#b91c1c 0%, #7f1111 50%, #3f0808 51%, #5f0d0d 100%),
                            linear-gradient(#101010 0%, #000000 100%),
                            linear-gradient(#772e2e, #471a1a);
                        -fx-background-insets: 0, 1, 2;
                        -fx-background-radius: 5, 4, 3;
                        -fx-text-fill: #bbbbbb;
                        -fx-font-weight: bold;
                        -fx-translate-y: 2px;
                        -fx-effect: none;
                        """);
            });
            button.setOnMouseReleased(e -> button.setStyle("""
                    -fx-background-color:
                        linear-gradient(#ff8a8a 0%, #b91c1c 50%, #6f0f0f 51%, #9f1717 100%),
                        linear-gradient(#202020 0%, #111111 100%),
                        linear-gradient(#8e3e3e, #772e2e);
                    -fx-background-insets: 0, 1, 2;
                    -fx-background-radius: 5, 4, 3;
                    -fx-text-fill: white;
                    -fx-font-weight: bold;
                    -fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.6), 5, 0, 0, 1);
                    """));
            return button;
        }
        button.setOnMousePressed(e -> SoundManager.playButtonSound());

        return button;
    }

    private String getTeamName(Role role) {
        return role == Role.LAUGHER ? "Laughers" : "Scarers";
    }

    private ImageView createBackgroundView(Monster winner) {
        ImageView backgroundView = new ImageView(loadImage(getWinBackgroundPath(winner)));

        backgroundView.setPreserveRatio(false);
        backgroundView.fitWidthProperty().bind(root.widthProperty());
        backgroundView.fitHeightProperty().bind(root.heightProperty());

        return backgroundView;
    }

    private String getWinBackgroundPath(Monster winner) {
        if (winner.getOriginalRole() == Role.LAUGHER) {
            return "/game/assets/WinScreen/LaughersWin.png";
        }

        if (getClass().getResource("/game/assets/WinScreen/ScarersWins.png") != null) {
            return "/game/assets/WinScreen/ScarersWins.png";
        }

        return "/game/assets/WinScreen/ScarerWins.png";
    }

    private Image loadImage(String path) {
        return new Image(getClass().getResourceAsStream(path));
    }

    private String getMonsterImagePath(Monster monster) {
        String name = monster.getName();

        switch (name) {
            case "James P. Sullivan":
                return "/game/assets/Monsters/sulli.png";
            case "Mike Wazowski":
                return "/game/assets/Monsters/mike.png";
            case "Celia Mae":
                return "/game/assets/Monsters/celia.png";
            case "Roz":
                return "/game/assets/Monsters/roz.png";
            case "Fungus":
                return "/game/assets/Monsters/Fungus.png";
            case "Henry J. Waternoose":
                return "/game/assets/Monsters/henry.png";
            case "Yeti":
                return "/game/assets/Monsters/yeti.png";
            case "Randall Boggs":
                return "/game/assets/Monsters/andal.png";
            default:
                return "/game/assets/LOGO.png";
        }
    }
}
