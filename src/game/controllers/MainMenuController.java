package game.controllers;

import game.view.MainMenuView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class MainMenuController {

    private final Stage stage;
    private final MainMenuView view;
    private StackPane overlayPane;

    public MainMenuController(Stage stage) {
        this.stage = stage;
        this.view = new MainMenuView();

        bindEvents();
        show();
    }

    private void bindEvents() {

        view.setOnStartGame(e -> {
            System.out.println("Start Game clicked");
            new ChooseSideController(stage);
        });

        view.setOnSettings(e -> {
            System.out.println("Settings clicked");
            // new SettingsController(stage);
        });

        view.setOnHowToPlay(e -> {
            System.out.println("How to Play clicked");
            showHowToPlayOverlay();
        });
    }

    public void showHowToPlayOverlay() {
        showOverlay(createHowToPlayContent());
    }

    public VBox createHowToPlayContent() {

        VBox content = new VBox(18);
        content.setAlignment(Pos.TOP_CENTER);

        content.prefHeightProperty().bind(stage.heightProperty().multiply(0.8));
        content.maxHeightProperty().bind(stage.heightProperty().multiply(0.8));
        content.setMaxWidth(820);

        content.setStyle("""
                -fx-background-color:
                    linear-gradient(to bottom right,
                    rgba(8,16,26,0.96),
                    rgba(15,25,40,0.96));

                -fx-background-radius: 24;
                -fx-border-radius: 24;
                -fx-border-width: 2;
                -fx-border-color: rgba(255,255,255,0.12);
                -fx-padding: 28;
                -fx-effect:
                    dropshadow(three-pass-box,
                    rgba(0,0,0,0.55),
                    24, 0, 0, 8);
                """);

        Label title = new Label("DooR DasH: Scare vs Laugh Touchdown");

        title.setStyle("""
                -fx-font-size: 30px;
                -fx-font-weight: 900;
                -fx-text-fill: white;
                """);

        Label subtitle = new Label("\"We scare because we care.\"   •   \"We laugh, that’s our path.\"");

        subtitle.setStyle("""
                -fx-font-size: 14px;
                -fx-text-fill: #cfe4ff;
                -fx-font-style: italic;
                """);

        VBox instructionsBox = new VBox(18);
        instructionsBox.setPadding(new Insets(5));

        instructionsBox.getChildren().addAll(
                createSection("Introduction", """
                        Welcome to Monstropolis!

                        Compete as a SCARER or LAUGHER in a high-energy race across the Floor.
                        Collect energy, activate monster abilities, avoid hazards,
                        and become the first monster to reach Boo's Door.

                        Victory requires BOTH:
                        • Reaching Cell 99
                        • Having at least 1000 energy
                        """),

                createSection("Game Setup", """
                        • Choose a side: SCARER or LAUGHER
                        • A monster from your chosen role is selected
                        • Your opponent gets a random monster from the opposite role
                        • Both players begin at Cell 0
                        • The board contains 100 zigzag cells
                        """),

                createSection("Cell Types", """
                        • Door Cells
                          - Matching role → gain energy
                          - Wrong role → lose energy
                          - Effects apply to your ENTIRE TEAM

                        • Monster Cells
                          - Same role → free powerup activation
                          - Opposite role → possible energy swap

                        • Conveyor Belts
                          - Instantly move forward

                        • Contamination Socks
                          - Move backward
                          - Lose 100 energy

                        • Card Cells
                          - Draw powerful random cards

                        • Normal Cells
                          - No effects
                        """),

                createSection("Cards", """
                        • Swapper Card
                          Swap positions with opponent

                        • Energy Steal Cards
                          Steal 50 / 100 / 150 energy

                        • Start Over Cards
                          Send a player back to Cell 0

                        • Shield Card
                          Blocks the next negative energy effect

                        • Confusion Card
                          Temporarily swaps player roles
                        """),

                createSection("Monster Types", """
                        • Dasher
                          - Permanent 2x movement
                          - Powerup: 3x speed for 3 turns

                        • Dynamo
                          - Double all energy gains & losses
                          - Powerup: Freeze opponent

                        • Multitasker
                          - Half movement speed
                          - +200 bonus on all energy changes

                        • Schemer
                          - +10 bonus on energy changes
                          - Powerup: Steal from all monsters
                        """),

                createSection("Turn Sequence", """
                        1. Activate powerup optionally
                        2. Roll dice
                        3. Move across the board
                        4. Trigger cell effect
                        5. Update game state
                        6. Switch turns

                        NOTE:
                        You cannot finish your move on an occupied cell.
                        """),

                createSection("Winning", """
                        Reach Cell 99, Boo's Door,
                        AND
                        Have at least 1000 energy.

                        The Floor awaits.
                        Only one monster will emerge victorious.
                        """));

        ScrollPane scrollPane = new ScrollPane(instructionsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);

        scrollPane.setStyle("""
                -fx-background-color: transparent;
                -fx-background: transparent;
                """);

        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        Button closeButton = new Button("Close");

        closeButton.setStyle("""
                -fx-background-color:
                    linear-gradient(#70b1ff 0%, #1a5cad 50%, #0a3b75 51%, #114b91 100%);
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-font-size: 16px;
                -fx-background-radius: 14;
                -fx-padding: 12 28 12 28;
                -fx-border-color: rgba(255,255,255,0.18);
                -fx-border-radius: 14;
                """);

        closeButton.setOnAction(e -> hideOverlay());

        content.getChildren().addAll(
                title,
                subtitle,
                scrollPane,
                closeButton);

        return content;
    }

    private VBox createSection(String title, String body) {

        Label titleLabel = new Label(title);

        titleLabel.setStyle("""
                -fx-font-size: 20px;
                -fx-font-weight: 900;
                -fx-text-fill: white;
                """);

        Label bodyLabel = new Label(body);
        bodyLabel.setWrapText(true);

        bodyLabel.setStyle("""
                -fx-font-size: 14px;
                -fx-line-spacing: 5px;
                -fx-text-fill: #dce8f5;
                """);

        VBox section = new VBox(10, titleLabel, bodyLabel);

        section.setStyle("""
                -fx-background-color: rgba(255,255,255,0.05);
                -fx-background-radius: 18;
                -fx-border-color: rgba(255,255,255,0.08);
                -fx-border-radius: 18;
                -fx-padding: 18;
                """);

        return section;
    }

    public void showOverlay(VBox content) {

        hideOverlay();

        Rectangle overlayBackground = new Rectangle();
        overlayBackground.setFill(Color.color(0, 0, 0, 0.82));
        overlayBackground.widthProperty().bind(stage.widthProperty());
        overlayBackground.heightProperty().bind(stage.heightProperty());

        overlayPane = new StackPane();
        overlayPane.getChildren().addAll(overlayBackground, content);

        StackPane.setAlignment(content, Pos.CENTER);

        ((StackPane) stage.getScene().getRoot()).getChildren().add(overlayPane);
    }

    public void hideOverlay() {
        if (overlayPane != null) {
            ((StackPane) stage.getScene().getRoot()).getChildren().remove(overlayPane);
            overlayPane = null;
        }
    }

    private void show() {
        if (stage.getScene() == null) {
            stage.setScene(new Scene(view.getRoot()));
        } else {
            stage.getScene().setRoot(view.getRoot());
        }

        stage.show();
    }
}