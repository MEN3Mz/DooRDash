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
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
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

    VBox content = new VBox(14);
    content.setAlignment(Pos.TOP_CENTER);

    content.prefHeightProperty().bind(root.heightProperty().multiply(0.84));
    content.maxHeightProperty().bind(root.heightProperty().multiply(0.84));
    content.setMaxWidth(820);

    content.getStyleClass().add("how-to-play-popup");

    Label title = new Label("DooR DasH: Scare vs Laugh Touchdown");

    title.getStyleClass().add("how-to-play-title");

    Label subtitle = new Label("\"We scare because we care.\"   -   \"We laugh, that's our path.\"");

    subtitle.getStyleClass().add("how-to-play-subtitle");

    VBox instructionsBox = new VBox(12);
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
            """,
            "/game/assets/LOGO.png",
            "/game/assets/Doors/openDoor.png",
            "/game/assets/powerUp/powerUpButton.png"),

        createSection("Game Setup", """
            - Choose a side: SCARER or LAUGHER
            - A monster from your chosen role is selected
            - Your opponent gets a random monster from the opposite role
            - Both players begin at Cell 0
            - The board contains 100 zigzag cells
            """,
            "/game/assets/choose-side/scarersOptions.png",
            "/game/assets/choose-side/selectOptionsLaughers.png",
            "/game/assets/dice/rollDice2.png"),

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
            """,
            "/game/assets/Doors/blueDoor.png",
            "/game/assets/Doors/redDoor.png",
            "/game/assets/transport/belt.png",
            "/game/assets/transport/sock.png",
            "/game/assets/cards/Cards.png"),

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
            """,
            "/game/assets/cards/Cards.png",
            "/game/assets/MonstersInfo/shield.png",
            "/game/assets/MonstersInfo/confusion.png",
            "/game/assets/statusEffects/shieldOverlay.png"),

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
            """,
            "/game/assets/MonstersInfo/laugher_mike.png",
            "/game/assets/MonstersInfo/scarer_henry.png",
            "/game/assets/MonstersInfo/laugher_yeti.png",
            "/game/assets/MonstersInfo/scarer_roz.png"),

        createSection("Turn Sequence", """
            1. Activate powerup optionally
            2. Roll dice
            3. Move across the board
            4. Trigger cell effect
            5. Update game state
            6. Switch turns

            NOTE:
            You cannot finish your move on an occupied cell.
            """,
            "/game/assets/powerUp/powerUpButton-Hover.png",
            "/game/assets/dice/dice6.png",
            "/game/assets/transport/belt.png",
            "/game/assets/Doors/openDoor.png"),

        createSection("Winning", """
            Reach Cell 99, Boo's Door,
            AND
            Have at least 1000 energy.

            The Floor awaits.
            Only one monster will emerge victorious.
            """,
            "/game/assets/WinScreen/LaughersWin.png",
            "/game/assets/WinScreen/ScarerWins.png",
            "/game/assets/Doors/openDoor.png"));

    ScrollPane scrollPane = new ScrollPane(instructionsBox);
    scrollPane.setFitToWidth(true);
    scrollPane.setPannable(true);

    scrollPane.getStyleClass().add("how-to-play-scroll");

    VBox.setVgrow(scrollPane, Priority.ALWAYS);

    backButton = new Button("Back");
    backButton.setPrefWidth(220);
    backButton.setPrefHeight(65);
    backButton.setMinHeight(65);
    backButton.setMaxHeight(65);
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

  private VBox createSection(String title, String body, String... imagePaths) {

    Label titleLabel = new Label(title);

    titleLabel.getStyleClass().add("how-to-play-section-title");

    Label bodyLabel = new Label(body);
    bodyLabel.setWrapText(true);

    bodyLabel.getStyleClass().add("how-to-play-section-body");

    VBox textColumn = new VBox(10, titleLabel, bodyLabel);
    textColumn.getStyleClass().add("how-to-play-text-column");
    HBox.setHgrow(textColumn, Priority.ALWAYS);

    FlowPane visualPane = createVisualPane(imagePaths);

    HBox row = new HBox(12, textColumn, visualPane);
    row.setAlignment(Pos.CENTER_LEFT);

    VBox section = new VBox(row);

    section.getStyleClass().add("how-to-play-section");

    return section;
  }

  private FlowPane createVisualPane(String... imagePaths) {
    FlowPane visualPane = new FlowPane(8, 8);
    visualPane.setAlignment(Pos.CENTER);
    visualPane.setPrefWrapLength(170);
    visualPane.setMinWidth(180);
    visualPane.setMaxWidth(180);
    visualPane.getStyleClass().add("how-to-play-visual-pane");

    for (String imagePath : imagePaths) {
      ImageView imageView = new ImageView(ImageCache.get(imagePath));
      imageView.setFitWidth(getVisualWidth(imagePath));
      imageView.setFitHeight(getVisualHeight(imagePath));
      imageView.setPreserveRatio(true);
      imageView.setSmooth(true);
      imageView.getStyleClass().add("how-to-play-image");
      visualPane.getChildren().add(imageView);
    }

    return visualPane;
  }

  private double getVisualWidth(String imagePath) {
    if (imagePath.contains("WinScreen")) {
      return 78;
    }

    if (imagePath.contains("choose-side") || imagePath.contains("MonstersInfo")) {
      return 66;
    }

    if (imagePath.contains("LOGO")) {
      return 108;
    }

    return 50;
  }

  private double getVisualHeight(String imagePath) {
    if (imagePath.contains("WinScreen")) {
      return 48;
    }

    if (imagePath.contains("choose-side") || imagePath.contains("MonstersInfo")) {
      return 88;
    }

    if (imagePath.contains("LOGO")) {
      return 64;
    }

    return 50;
  }

  private Rectangle createOverlayBackground() {
    Rectangle backgroundView = new Rectangle();

    backgroundView.setFill(Color.color(0, 0, 0, 0.72));
    backgroundView.widthProperty().bind(root.widthProperty());
    backgroundView.heightProperty().bind(root.heightProperty());

    return backgroundView;
  }
}
