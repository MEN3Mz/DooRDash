package game.view;

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

    private static final String SELECT_BUTTON_STYLE = "-fx-background-color: rgba(9, 17, 26, 0.82);"
            + "-fx-text-fill: white;"
            + "-fx-font-weight: bold;"
            + "-fx-font-size: 16px;"
            + "-fx-background-radius: 12;"
            + "-fx-padding: 12 24 12 24;"
            + "-fx-border-color: rgba(255,255,255,0.45);"
            + "-fx-border-radius: 12;";
    private static final String START_BUTTON_STYLE = "-fx-background-color: "
            + "linear-gradient(#70b1ff 0%, #1a5cad 50%, #0a3b75 51%, #114b91 100%), "
            + "linear-gradient(#202020 0%, #111111 100%), "
            + "linear-gradient(#3e5e8e, #2e4a77); "
            + "-fx-background-insets: 0,1,2; "
            + "-fx-background-radius: 5,4,3; "
            + "-fx-text-fill: white;"
            + "-fx-font-weight: bold;"
            + "-fx-font-size: 18px;"
            + "-fx-padding: 12 34 12 34;"
            + "-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 );";
    private static final String DISABLED_START_BUTTON_STYLE = "-fx-background-color: rgba(80, 88, 98, 0.88);"
            + "-fx-text-fill: #d7dde4;"
            + "-fx-font-weight: bold;"
            + "-fx-font-size: 18px;"
            + "-fx-background-radius: 16;"
            + "-fx-padding: 12 34 12 34;";

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
        titleLabel.setStyle("-fx-font-size: 34px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label subtitleLabel = new Label("Select how you want to play.");
        subtitleLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #e4edf7;");

        VBox content = new VBox(22, titleLabel, subtitleLabel, modesRow);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(28));
        content.setMaxWidth(Double.MAX_VALUE);

        BorderPane overlay = new BorderPane();
        overlay.setCenter(content);
        BorderPane.setAlignment(continueButton, Pos.BOTTOM_CENTER);
        BorderPane.setMargin(continueButton, new Insets(0, 0, 28, 0));
        overlay.setBottom(continueButton);
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.18);");
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
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: 900; -fx-text-fill: white;");

        Label descriptionLabel = new Label(description);
        descriptionLabel.setWrapText(true);
        descriptionLabel.setMaxWidth(360);
        descriptionLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: #edf4fb; -fx-line-spacing: 4;");

        VBox textBox = new VBox(12, titleLabel, descriptionLabel);
        textBox.setAlignment(Pos.TOP_LEFT);
        textBox.setPadding(new Insets(34));
        textBox.setMaxWidth(400);

        BorderPane overlayPanel = new BorderPane();
        overlayPanel.setLeft(textBox);
        overlayPanel.setStyle(
                "-fx-background-color: linear-gradient(to right, rgba(4, 10, 18, 0.78), rgba(4, 10, 18, 0.06));");

        StackPane card = new StackPane(imageView, overlayPanel);
        card.setPrefSize(620, 720);
        card.setMaxSize(620, 720);
        card.setStyle("-fx-background-radius: 24; -fx-border-radius: 24; -fx-border-color: rgba(255,255,255,0.16);");
        StackPane.setAlignment(overlayPanel, Pos.CENTER_LEFT);
        StackPane.setAlignment(chooseButton, Pos.BOTTOM_CENTER);
        StackPane.setMargin(chooseButton, new Insets(0, 0, 26, 0));
        card.getChildren().add(chooseButton);

        return card;
    }

    private Button createSelectButton(String text, GameType gameType) {
        Button button = new Button(text);
        button.setStyle(SELECT_BUTTON_STYLE);
        button.setOnAction(event -> updateSelection(gameType));

        return button;
    }

    private Button createContinueButton() {
        Button button = new Button("Continue");
        button.setDisable(true);
        button.setStyle(DISABLED_START_BUTTON_STYLE);

        return button;
    }

    private void updateSelection(GameType gameType) {
        selectedGameType = gameType;

        applyModeState(singlePlayerImageView, gameType == GameType.SINGLE_PLAYER);
        applyModeState(multiplayerImageView, gameType == GameType.MULTIPLAYER);

        continueButton.setDisable(gameType == null);
        continueButton.setStyle(gameType == null ? DISABLED_START_BUTTON_STYLE : START_BUTTON_STYLE);
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
