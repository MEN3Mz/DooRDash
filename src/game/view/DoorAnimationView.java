package game.view;

import game.engine.Role;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class DoorAnimationView {

    private final StackPane root;
    private ImageView closedDoorView;
    private ImageView openDoorView;
    private Runnable onFinished;

    public DoorAnimationView(Role doorRole) {
        root = new StackPane();

        Rectangle overlayBackground = createOverlayBackground();
        StackPane doorPane = createDoorPane(doorRole);

        root.getChildren().addAll(overlayBackground, doorPane);
        StackPane.setAlignment(doorPane, Pos.CENTER);
        playDoorAnimation(doorPane);
    }

    public Parent getRoot() {
        return root;
    }

    public void setOnFinished(Runnable onFinished) {
        this.onFinished = onFinished;
    }

    private StackPane createDoorPane(Role doorRole) {
        closedDoorView = createDoorImage(getClosedDoorPath(doorRole));
        openDoorView = createDoorImage("/game/assets/Doors/openDoor.png");
        openDoorView.setVisible(false);
        openDoorView.setOpacity(0);

        StackPane doorPane = new StackPane(closedDoorView, openDoorView);
        doorPane.setPrefSize(300, 380);
        doorPane.setMinSize(300, 380);
        doorPane.setMaxSize(300, 380);

        return doorPane;
    }

    private ImageView createDoorImage(String path) {
        ImageView imageView = new ImageView(ImageCache.get(path));
        imageView.setFitWidth(260);
        imageView.setFitHeight(320);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        return imageView;
    }

    private void playDoorAnimation(StackPane doorPane) {
        doorPane.setOpacity(0);
        doorPane.setScaleX(0.65);
        doorPane.setScaleY(0.65);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(180), doorPane);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        ScaleTransition grow = new ScaleTransition(Duration.millis(220), doorPane);
        grow.setFromX(0.65);
        grow.setFromY(0.65);
        grow.setToX(1);
        grow.setToY(1);

        PauseTransition holdClosed = new PauseTransition(Duration.millis(260));

        PauseTransition switchDoor = new PauseTransition(Duration.millis(1));
        switchDoor.setOnFinished(event -> {
            closedDoorView.setVisible(false);
            openDoorView.setVisible(true);
            openDoorView.setOpacity(1);
        });

        PauseTransition holdOpen = new PauseTransition(Duration.millis(520));

        FadeTransition fadeOut = new FadeTransition(Duration.millis(220), doorPane);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        SequentialTransition sequence = new SequentialTransition(
                fadeIn,
                grow,
                holdClosed,
                switchDoor,
                holdOpen,
                fadeOut);

        sequence.setOnFinished(event -> {
            if (onFinished != null) {
                onFinished.run();
            }
        });

        sequence.play();
    }

    private Rectangle createOverlayBackground() {
        Rectangle backgroundView = new Rectangle();

        backgroundView.setFill(Color.color(0, 0, 0, 0.58));
        backgroundView.widthProperty().bind(root.widthProperty());
        backgroundView.heightProperty().bind(root.heightProperty());

        return backgroundView;
    }

    private String getClosedDoorPath(Role doorRole) {
        return doorRole == Role.LAUGHER
                ? "/game/assets/Doors/blueDoor.png"
                : "/game/assets/Doors/redDoor.png";
    }
}
