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
        root.getStylesheets().add(
                getClass().getResource("/game/assets/css/card-view.css").toExternalForm());

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
        content.getStyleClass().add("card-popup");

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
        label.getStyleClass().add("card-detail");

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
