package game.view;

import game.audio.SoundManager;
import game.engine.cards.Card;
import game.engine.cards.ConfusionCard;
import game.engine.cards.EnergyStealCard;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CardView {

    private final StackPane root;
    private Button closeButton;

    public CardView(Card card, String pulledBy) {
        root = new StackPane();
        root.getStylesheets().add(
                getClass().getResource("/game/assets/css/menu.css").toExternalForm());

        Rectangle overlayBackground = createOverlayBackground();
        VBox content = createContent(card, pulledBy);

        root.getChildren().addAll(overlayBackground, content);
        StackPane.setAlignment(content, Pos.CENTER);
    }

    public Parent getRoot() {
        return root;
    }

    public void setOnClose(EventHandler<ActionEvent> handler) {
        closeButton.setOnAction(handler);
    }

    private VBox createContent(Card card, String pulledBy) {
        VBox content = new VBox(10);
        content.setAlignment(Pos.CENTER);
        content.setPrefSize(420, 420);
        content.setMinSize(420, 420);
        content.setMaxSize(420, 420);
        content.setStyle("""
                -fx-background-color:
                    linear-gradient(to bottom right,
                    rgba(8,16,26,0.96),
                    rgba(15,25,40,0.96));

                -fx-background-radius: 24;
                -fx-border-radius: 24;
                -fx-border-width: 2;
                -fx-border-color: rgba(255,255,255,0.12);
                -fx-padding: 24;
                -fx-effect:
                    dropshadow(three-pass-box,
                    rgba(0,0,0,0.55),
                    24, 0, 0, 8);
                """);

        Label title = new Label("Card Drawn");
        title.setStyle("""
                -fx-font-size: 24px;
                -fx-font-weight: 900;
                -fx-text-fill: white;
                """);

        Label nameLabel = new Label(card.getName());
        nameLabel.setWrapText(true);
        nameLabel.setMaxWidth(350);
        nameLabel.setAlignment(Pos.CENTER);
        nameLabel.setStyle("""
                -fx-font-size: 20px;
                -fx-font-weight: 900;
                -fx-text-fill: #cfe4ff;
                """);

        Label typeLabel = createDetailLabel("Type: " + getCardType(card));
        Label pulledByLabel = createDetailLabel("Pulled by: " + pulledBy);
        Label extraEffectLabel = createExtraEffectLabel(card);

        Label descriptionLabel = new Label(card.getDescription());
        descriptionLabel.setWrapText(true);
        descriptionLabel.setMaxWidth(340);
        descriptionLabel.setAlignment(Pos.CENTER);
        descriptionLabel.setStyle("""
                -fx-font-size: 14px;
                -fx-line-spacing: 5px;
                -fx-text-fill: #dce8f5;
                """);

        closeButton = createMenuButton("Continue");

        content.getChildren().addAll(
                title,
                nameLabel,
                typeLabel,
                pulledByLabel);

        if (extraEffectLabel != null) {
            content.getChildren().add(extraEffectLabel);
        }

        content.getChildren().addAll(descriptionLabel, closeButton);

        return content;
    }

    private Label createDetailLabel(String text) {
        Label label = new Label(text);
        label.setStyle("""
                -fx-font-size: 13px;
                -fx-font-weight: bold;
                -fx-text-fill: #dce8f5;
                """);

        return label;
    }

    private Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(240);
        button.setPrefHeight(48);
        button.getStyleClass().add("menu-button");
        button.getStyleClass().add("menu-font");
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
