package game.view;

import game.audio.SoundManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class HowToPlayView {

    private final StackPane root;
    private Button backButton;

    public HowToPlayView() {
        root = new StackPane();
        root.getStylesheets().add(
                getClass().getResource("/game/assets/css/menu.css").toExternalForm());
        root.getStylesheets().add(
                getClass().getResource("/game/assets/css/how-to-play-view.css").toExternalForm());

        Rectangle overlayBackground = createOverlayBackground();
        VBox content = createHowToPlayContent();

        root.getChildren().addAll(overlayBackground, content);
        StackPane.setAlignment(content, Pos.CENTER);
    }

    public Parent getRoot() {
        return root;
    }

    public void setOnBack(EventHandler<ActionEvent> handler) {
        backButton.setOnAction(handler);
    }

    private VBox createHowToPlayContent() {

        VBox content = new VBox(18);
        content.setAlignment(Pos.TOP_CENTER);

        content.prefHeightProperty().bind(root.heightProperty().multiply(0.8));
        content.maxHeightProperty().bind(root.heightProperty().multiply(0.8));
        content.setMaxWidth(820);

        content.getStyleClass().add("how-to-play-popup");

        Label title = new Label("DooR DasH: Scare vs Laugh Touchdown");

        title.getStyleClass().add("how-to-play-title");

        Label subtitle = new Label("\"We scare because we care.\"   -   \"We laugh, that's our path.\"");

        subtitle.getStyleClass().add("how-to-play-subtitle");

        VBox instructionsBox = new VBox(18);
        instructionsBox.setPadding(new Insets(5));

        instructionsBox.getChildren().addAll(
                createSection("Introduction", """
                        Welcome to Monstropolis!

                        Compete as a SCARER or LAUGHER in a high-energy race across the Floor.
                        Collect energy, activate monster abilities, avoid hazards,
                        and become the first monster to reach Boo's Door.

                        Victory requires BOTH:
                        - Reaching Cell 99
                        - Having at least 1000 energy
                        """),

                createSection("Game Setup", """
                        - Choose a side: SCARER or LAUGHER
                        - A monster from your chosen role is selected
                        - Your opponent gets a random monster from the opposite role
                        - Both players begin at Cell 0
                        - The board contains 100 zigzag cells
                        """),

                createSection("Cell Types", """
                        - Door Cells
                          - Matching role -> gain energy
                          - Wrong role -> lose energy
                          - Effects apply to your ENTIRE TEAM

                        - Monster Cells
                          - Same role -> free powerup activation
                          - Opposite role -> possible energy swap

                        - Conveyor Belts
                          - Instantly move forward

                        - Contamination Socks
                          - Move backward
                          - Lose 100 energy

                        - Card Cells
                          - Draw powerful random cards

                        - Normal Cells
                          - No effects
                        """),

                createSection("Cards", """
                        - Swapper Card
                          Swap positions with opponent

                        - Energy Steal Cards
                          Steal 50 / 100 / 150 energy

                        - Start Over Cards
                          Send a player back to Cell 0

                        - Shield Card
                          Blocks the next negative energy effect

                        - Confusion Card
                          Temporarily swaps player roles
                        """),

                createSection("Monster Types", """
                        - Dasher
                          - Permanent 2x movement
                          - Powerup: 3x speed for 3 turns

                        - Dynamo
                          - Double all energy gains & losses
                          - Powerup: Freeze opponent

                        - Multitasker
                          - Half movement speed
                          - +200 bonus on all energy changes

                        - Schemer
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

        scrollPane.getStyleClass().add("how-to-play-scroll");

        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        backButton = new Button("Back");
        backButton.setPrefWidth(220);
        backButton.setPrefHeight(48);
        backButton.getStyleClass().add("menu-button");
        backButton.getStyleClass().add("menu-font");
        backButton.setOnMouseEntered(e -> SoundManager.playHoverSound());
        backButton.setOnMousePressed(e -> SoundManager.playButtonSound());

        content.getChildren().addAll(
                title,
                subtitle,
                scrollPane,
                backButton);

        return content;
    }

    private VBox createSection(String title, String body) {

        Label titleLabel = new Label(title);

        titleLabel.getStyleClass().add("how-to-play-section-title");

        Label bodyLabel = new Label(body);
        bodyLabel.setWrapText(true);

        bodyLabel.getStyleClass().add("how-to-play-section-body");

        VBox section = new VBox(10, titleLabel, bodyLabel);

        section.getStyleClass().add("how-to-play-section");

        return section;
    }

    private Rectangle createOverlayBackground() {
        Rectangle backgroundView = new Rectangle();

        backgroundView.setFill(Color.color(0, 0, 0, 0.72));
        backgroundView.widthProperty().bind(root.widthProperty());
        backgroundView.heightProperty().bind(root.heightProperty());

        return backgroundView;
    }
}
