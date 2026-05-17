package game.view;

import game.audio.SoundManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class GameTypeView {

    public enum GameType {
        SINGLE_PLAYER,
        MULTIPLAYER
    }

    private static final String SINGLE_PLAYER_IMAGE = "/game/assets/choose-side/scarersOptions.png";
    private static final String SINGLE_PLAYER_DIMMED_IMAGE = "/game/assets/choose-side/scarersOptionsDark.png";
    private static final String MULTIPLAYER_IMAGE = "/game/assets/choose-side/selectOptionsLaughers.png";
    private static final String MULTIPLAYER_DIMMED_IMAGE = "/game/assets/choose-side/LaughersOptionsDark.png";
    private static final String PAGE_BACKGROUND_IMAGE = "/game/assets/Background.png";

    private final StackPane root;
    private final Button singlePlayerButton;
    private final Button multiplayerButton;
    private final Button continueButton;
    private final ImageView singlePlayerImageView;
    private final ImageView multiplayerImageView;

    private GameType selectedGameType;

    public GameTypeView() {
        ImageView backgroundImageView = createBackgroundView();
        singlePlayerImageView = createModeImageView(loadImage(SINGLE_PLAYER_IMAGE));
        multiplayerImageView = createModeImageView(loadImage(MULTIPLAYER_IMAGE));
        singlePlayerButton = createSelectButton("SINGLE PLAYER", GameType.SINGLE_PLAYER);
        multiplayerButton = createSelectButton("MULTIPLAYER", GameType.MULTIPLAYER);
        continueButton = createContinueButton();

        singlePlayerImageView.getProperties().put("selectedImage", SINGLE_PLAYER_IMAGE);
        singlePlayerImageView.getProperties().put("dimmedImage", SINGLE_PLAYER_DIMMED_IMAGE);
        multiplayerImageView.getProperties().put("selectedImage", MULTIPLAYER_IMAGE);
        multiplayerImageView.getProperties().put("dimmedImage", MULTIPLAYER_DIMMED_IMAGE);

        root = buildRoot(backgroundImageView);
        root.getStylesheets().add(getClass().getResource("/game/assets/css/choose-side-view.css").toExternalForm());
        root.getStylesheets().add(getClass().getResource("/game/assets/css/game-type-view.css").toExternalForm());
        updateSelection(null);
    }

    public Parent getRoot() {
        return root;
    }

    public GameType getSelectedGameType() {
        return selectedGameType;
    }

    public void setOnContinue(EventHandler<ActionEvent> handler) {
        continueButton.setOnAction(handler);
    }

    private StackPane buildRoot(ImageView backgroundImageView) {
        StackPane singlePlayerCard = createModeCard(
                singlePlayerImageView,
                "Single Player",
                "Play against the computer-controlled opposing team.",
                singlePlayerButton);
        StackPane multiplayerCard = createModeCard(
                multiplayerImageView,
                "Multiplayer",
                "Two players share the same game and take turns locally.",
                multiplayerButton);

        HBox modesRow = new HBox(24, singlePlayerCard, multiplayerCard);
        modesRow.setAlignment(Pos.CENTER);
        HBox.setHgrow(singlePlayerCard, Priority.ALWAYS);
        HBox.setHgrow(multiplayerCard, Priority.ALWAYS);
        modesRow.setMaxWidth(Double.MAX_VALUE);

        Label titleLabel = new Label("Choose Game Mode");
        titleLabel.getStyleClass().add("game-type-title");

        Label subtitleLabel = new Label("Select how you want to play.");
        subtitleLabel.getStyleClass().add("game-type-subtitle");

        VBox content = new VBox(22, titleLabel, subtitleLabel, modesRow);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(28));
        content.setMaxWidth(Double.MAX_VALUE);

        BorderPane overlay = new BorderPane();
        overlay.setCenter(content);
        BorderPane.setAlignment(continueButton, Pos.BOTTOM_CENTER);
        BorderPane.setMargin(continueButton, new Insets(0, 0, 28, 0));
        overlay.setBottom(continueButton);
        overlay.getStyleClass().add("game-type-overlay");
        overlay.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        StackPane pageRoot = new StackPane(backgroundImageView, overlay);
        backgroundImageView.fitWidthProperty().bind(pageRoot.widthProperty());
        backgroundImageView.fitHeightProperty().bind(pageRoot.heightProperty());

        return pageRoot;
    }

    private StackPane createModeCard(
            ImageView imageView,
            String title,
            String description,
            Button chooseButton) {

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("game-type-card-title");

        Label descriptionLabel = new Label(description);
        descriptionLabel.setWrapText(true);
        descriptionLabel.setMaxWidth(360);
        descriptionLabel.getStyleClass().add("game-type-card-description");

        VBox textBox = new VBox(12, titleLabel, descriptionLabel);
        textBox.setAlignment(Pos.TOP_LEFT);
        textBox.setPadding(new Insets(34));
        textBox.setMaxWidth(400);

        BorderPane overlayPanel = new BorderPane();
        overlayPanel.setLeft(textBox);
        overlayPanel.getStyleClass().add("game-type-card-overlay");

        StackPane card = new StackPane(imageView, overlayPanel);
        card.setPrefSize(620, 720);
        card.setMaxSize(620, 720);
        card.getStyleClass().add("game-type-card");
        StackPane.setAlignment(overlayPanel, Pos.CENTER_LEFT);
        StackPane.setAlignment(chooseButton, Pos.BOTTOM_CENTER);
        StackPane.setMargin(chooseButton, new Insets(0, 0, 26, 0));
        card.getChildren().add(chooseButton);

        return card;
    }

    private Button createSelectButton(String text, GameType gameType) {
        Button button = new Button(text);
        button.getStyleClass().add("select-button");
        button.setOnMouseEntered(event -> SoundManager.playHoverSound());
        button.setOnAction(event -> updateSelection(gameType));

        return button;
    }

    private Button createContinueButton() {
        Button button = new Button("Continue");
        button.setDisable(true);
        button.getStyleClass().add("start-button");
        button.setOnMouseEntered(event -> {
            if (!button.isDisabled()) {
                SoundManager.playHoverSound();
            }
        });

        return button;
    }

    private void updateSelection(GameType gameType) {
        selectedGameType = gameType;

        applyModeState(singlePlayerImageView, gameType == GameType.SINGLE_PLAYER);
        applyModeState(multiplayerImageView, gameType == GameType.MULTIPLAYER);

        continueButton.setDisable(gameType == null);
    }

    private void applyModeState(ImageView imageView, boolean selected) {
        String selectedPath = (String) imageView.getProperties().get("selectedImage");
        String dimmedPath = (String) imageView.getProperties().get("dimmedImage");

        if (selectedGameType == null) {
            imageView.setImage(loadImage(selectedPath));
            imageView.setOpacity(1.0);
            return;
        }

        imageView.setImage(loadImage(selected ? selectedPath : dimmedPath));
        imageView.setOpacity(selected ? 1.0 : 0.68);
    }

    private ImageView createBackgroundView() {
        ImageView backgroundView = new ImageView(loadImage(PAGE_BACKGROUND_IMAGE));
        backgroundView.setPreserveRatio(false);

        return backgroundView;
    }

    private ImageView createModeImageView(Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(false);
        imageView.setFitWidth(620);
        imageView.setFitHeight(720);
        imageView.setSmooth(true);
        imageView.setCache(true);

        return imageView;
    }

    private Image loadImage(String resourcePath) {
        return new Image(getClass().getResourceAsStream(resourcePath));
    }
}
