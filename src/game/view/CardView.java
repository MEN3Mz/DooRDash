package game.view;

import game.audio.SoundManager;
import game.engine.cards.Card;
import game.engine.cards.ConfusionCard;
import game.engine.cards.EnergyStealCard;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class CardView {

    private final StackPane root;
    private ImageView rearFace;
    private StackPane frontFace;
    private Button closeButton;

    public CardView(Card card, String pulledBy) {
        root = new StackPane();
        root.getStylesheets().add(
                ThemeManager.loadStylesheet("/game/assets/css/menu.css"));
        root.getStylesheets().add(
                ThemeManager.loadStylesheet("/game/assets/css/card-view.css"));

        Rectangle overlayBackground = createOverlayBackground();
        StackPane content = createContent(card, pulledBy);

        root.getChildren().addAll(overlayBackground, content);
        StackPane.setAlignment(content, Pos.CENTER);
        playCardRevealAnimation(content);
    }

    public Parent getRoot() {
        return root;
    }

    public void setOnClose(EventHandler<ActionEvent> handler) {
        closeButton.setOnAction(handler);
    }

    private StackPane createContent(Card card, String pulledBy) {
        ImageView cardFrame = new ImageView(ImageCache.get("/game/assets/cards/PulledCards.png"));
        cardFrame.setFitWidth(420);
        cardFrame.setFitHeight(600);
        cardFrame.setPreserveRatio(true);
        cardFrame.setSmooth(true);

        VBox details = new VBox(10);
        details.setAlignment(Pos.TOP_CENTER);
        details.setMaxWidth(300);
        details.setPadding(new Insets(62, 0, 0, 0));

        Label title = new Label("Card Drawn");
        title.getStyleClass().add("card-title");

        Label nameLabel = new Label(card.getName());
        nameLabel.setWrapText(true);
        nameLabel.setMaxWidth(350);
        nameLabel.setAlignment(Pos.CENTER);
        nameLabel.getStyleClass().add("card-name");

        Label typeLabel = createDetailLabel("Type: " + getCardType(card));
        Label pulledByLabel = createDetailLabel("Pulled by: " + pulledBy);
        Label extraEffectLabel = createExtraEffectLabel(card);

        Label descriptionLabel = new Label(card.getDescription());
        descriptionLabel.setWrapText(true);
        descriptionLabel.setMaxWidth(340);
        descriptionLabel.setAlignment(Pos.CENTER);
        descriptionLabel.getStyleClass().add("card-description");

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        closeButton = createMenuButton("Continue");

        details.getChildren().addAll(
                title,
                nameLabel,
                typeLabel,
                pulledByLabel);

        if (extraEffectLabel != null) {
            details.getChildren().add(extraEffectLabel);
        }

        details.getChildren().addAll(descriptionLabel, spacer, closeButton);
        details.setMinHeight(470);
        details.setPrefHeight(470);
        details.setMaxHeight(470);

        frontFace = new StackPane(cardFrame, details);
        frontFace.setAlignment(Pos.CENTER);
        frontFace.setVisible(false);

        rearFace = new ImageView(ImageCache.get("/game/assets/cards/PulledCardsRear.png"));
        rearFace.setFitWidth(420);
        rearFace.setFitHeight(600);
        rearFace.setPreserveRatio(true);
        rearFace.setSmooth(true);

        StackPane content = new StackPane(rearFace, frontFace);
        content.setAlignment(Pos.CENTER);
        content.setPrefSize(420, 600);
        content.setMinSize(420, 600);
        content.setMaxSize(420, 600);
        content.getStyleClass().add("card-popup");

        return content;
    }

    private void playCardRevealAnimation(StackPane content) {
        content.setOpacity(0);
        content.setScaleX(0.35);
        content.setScaleY(0.35);
        content.setRotate(-10);

        FadeTransition fade = new FadeTransition(Duration.millis(260), content);
        fade.setFromValue(0);
        fade.setToValue(1);

        ScaleTransition scale = new ScaleTransition(Duration.millis(360), content);
        scale.setFromX(0.35);
        scale.setFromY(0.35);
        scale.setToX(1);
        scale.setToY(1);

        RotateTransition rotate = new RotateTransition(Duration.millis(360), content);
        rotate.setFromAngle(-10);
        rotate.setToAngle(0);

        ParallelTransition enter = new ParallelTransition(fade, scale, rotate);

        PauseTransition holdRear = new PauseTransition(Duration.millis(280));

        ScaleTransition closeFlip = new ScaleTransition(Duration.millis(180), content);
        closeFlip.setFromX(1);
        closeFlip.setToX(0.04);
        closeFlip.setOnFinished(e -> {
            rearFace.setVisible(false);
            frontFace.setVisible(true);
        });

        ScaleTransition openFlip = new ScaleTransition(Duration.millis(220), content);
        openFlip.setFromX(0.04);
        openFlip.setToX(1);

        SequentialTransition reveal = new SequentialTransition(enter, holdRear, closeFlip, openFlip);
        reveal.play();
    }

    private Label createDetailLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("card-detail");

        return label;
    }

    private Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(240);
        button.setPrefHeight(48);
        button.getStyleClass().add("menu-button");
        button.getStyleClass().add("menu-font");
        button.getStyleClass().add("card-continue-button");
        button.setOnMouseEntered(e -> SoundManager.playHoverSound());
        button.setOnMousePressed(e -> SoundManager.playButtonSound());

        return button;
    }

    private String getCardType(Card card) {
        return card.getClass().getSimpleName().replace("Card", "");
    }

    private Label createExtraEffectLabel(Card card) {
        if (card instanceof EnergyStealCard) {
            return createDetailLabel("Energy: " + ((EnergyStealCard) card).getEnergy());
        }

        if (card instanceof ConfusionCard) {
            return createDetailLabel("Duration: " + ((ConfusionCard) card).getDuration() + " turns");
        }

        return null;
    }

    private Rectangle createOverlayBackground() {
        Rectangle backgroundView = new Rectangle();

        backgroundView.setFill(Color.color(0, 0, 0, 0.72));
        backgroundView.widthProperty().bind(root.widthProperty());
        backgroundView.heightProperty().bind(root.heightProperty());

        return backgroundView;
    }
}
