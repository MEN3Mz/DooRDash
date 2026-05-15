package game.view;

import java.util.ArrayList;

import game.audio.SoundManager;
import game.engine.Role;
import game.engine.dataloader.DataLoader;
import game.engine.monsters.Monster;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ChooseSideView {
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
    private static final String START_BUTTON_HOVER_STYLE = "-fx-background-color: "
            + "linear-gradient(#a8d2ff 0%, #2f80ed 50%, #0f56a8 51%, #1b65bf 100%), "
            + "linear-gradient(#303030 0%, #161616 100%), "
            + "linear-gradient(#5c7fb4, #3d6094); "
            + "-fx-background-insets: 0,1,2; "
            + "-fx-background-radius: 5,4,3; "
            + "-fx-text-fill: white;"
            + "-fx-font-weight: bold;"
            + "-fx-font-size: 18px;"
            + "-fx-padding: 12 34 12 34;"
            + "-fx-scale-x: 1.03;"
            + "-fx-scale-y: 1.03;"
            + "-fx-effect: dropshadow( three-pass-box , rgba(120,190,255,0.75) , 10, 0.0 , 0 , 1 );";
    private static final String START_BUTTON_PRESSED_STYLE = "-fx-background-color: "
            + "linear-gradient(#1a5cad 0%, #0a3b75 50%, #051d3a 51%, #082a52 100%), "
            + "linear-gradient(#101010 0%, #000000 100%), "
            + "linear-gradient(#2e4a77, #1a2b47); "
            + "-fx-background-insets: 0,1,2; "
            + "-fx-background-radius: 5,4,3; "
            + "-fx-text-fill: #bbbbbb;"
            + "-fx-font-weight: bold;"
            + "-fx-font-size: 18px;"
            + "-fx-padding: 12 34 12 34;"
            + "-fx-translate-y: 2px;"
            + "-fx-effect: none;";
    private static final String DISABLED_START_BUTTON_STYLE = "-fx-background-color: rgba(80, 88, 98, 0.88);"
            + "-fx-text-fill: #d7dde4;"
            + "-fx-font-weight: bold;"
            + "-fx-font-size: 18px;"
            + "-fx-background-radius: 16;"
            + "-fx-padding: 12 34 12 34;";
    private static final String INFO_BUTTON_STYLE = "-fx-background-color: rgba(8, 16, 26, 0.88);"
            + "-fx-text-fill: white;"
            + "-fx-font-weight: bold;"
            + "-fx-font-size: 18px;"
            + "-fx-background-radius: 20;"
            + "-fx-min-width: 38px;"
            + "-fx-min-height: 38px;"
            + "-fx-max-width: 38px;"
            + "-fx-max-height: 38px;"
            + "-fx-border-color: rgba(255,255,255,0.45);"
            + "-fx-border-radius: 20;";
    private static final String DETAILS_PANEL_STYLE = "-fx-background-color: rgba(8, 16, 26, 0.78);"
            + "-fx-background-radius: 18;"
            + "-fx-padding: 18;"
            + "-fx-border-color: rgba(255,255,255,0.18);"
            + "-fx-border-radius: 18;";

    private static final String SCARER_SELECTED_IMAGE = "/game/assets/choose-side/scarersOptions.png";
    private static final String SCARER_DIMMED_IMAGE = "/game/assets/choose-side/scarersOptionsDark.png";
    private static final String LAUGHER_SELECTED_IMAGE = "/game/assets/choose-side/selectOptionsLaughers.png";
    private static final String LAUGHER_DIMMED_IMAGE = "/game/assets/choose-side/LaughersOptionsDark.png";
    private static final String PAGE_BACKGROUND_IMAGE = "/game/assets/Background.png";

    private final ArrayList<Monster> monsters = new ArrayList<>();
    private final StackPane root;
    private final Button scarerButton;
    private final Button laugherButton;
    private final Button startGameButton;
    private final Button scarerInfoButton;
    private final Button laugherInfoButton;
    private final ImageView backgroundImageView;
    private final ImageView scarerImageView;
    private final ImageView laugherImageView;
    private final VBox scarerDetailsBox;
    private final VBox laugherDetailsBox;

    private Role selectedRole;

    public ChooseSideView() {
        loadMonsters();

        backgroundImageView = createBackgroundView();
        scarerImageView = createTeamImageView(loadImage(SCARER_SELECTED_IMAGE));
        laugherImageView = createTeamImageView(loadImage(LAUGHER_SELECTED_IMAGE));
        scarerButton = createSelectButton("I AM A SCARER", Role.SCARER);
        laugherButton = createSelectButton("I AM A LAUGHER", Role.LAUGHER);
        scarerInfoButton = createInfoButton();
        laugherInfoButton = createInfoButton();
        scarerDetailsBox = createDetailsBox(Role.SCARER);
        laugherDetailsBox = createDetailsBox(Role.LAUGHER);
        startGameButton = createStartButton();
        root = buildRoot();

        scarerImageView.getProperties().put("selectedImage", SCARER_SELECTED_IMAGE);
        scarerImageView.getProperties().put("dimmedImage", SCARER_DIMMED_IMAGE);
        laugherImageView.getProperties().put("selectedImage", LAUGHER_SELECTED_IMAGE);
        laugherImageView.getProperties().put("dimmedImage", LAUGHER_DIMMED_IMAGE);
        scarerInfoButton.setOnAction(event -> toggleDetails(scarerDetailsBox));
        laugherInfoButton.setOnAction(event -> toggleDetails(laugherDetailsBox));

        updateSelection(null);
    }

    public Parent getRoot() {
        return root;
    }

    public Role getSelectedRole() {
        return selectedRole;
    }

    public Button getStartGameButton() {
        return startGameButton;
    }

    public Button getScarerButton() {
        return scarerButton;
    }

    public Button getLaugherButton() {
        return laugherButton;
    }

    public void setOnStartGame(EventHandler<ActionEvent> handler) {
        startGameButton.setOnAction(handler);
    }

    public void setOnScarerSelected(EventHandler<ActionEvent> handler) {
        scarerButton.setOnAction(event -> {
            updateSelection(Role.SCARER);
            if (handler != null) {
                handler.handle(event);
            }
        });
    }

    public void setOnLaugherSelected(EventHandler<ActionEvent> handler) {
        laugherButton.setOnAction(event -> {
            updateSelection(Role.LAUGHER);
            if (handler != null) {
                handler.handle(event);
            }
        });
    }

    private StackPane buildRoot() {
        StackPane scarerCard = createTeamCard(
                scarerImageView,
                scarerButton,
                scarerInfoButton,
                scarerDetailsBox);
        StackPane laugherCard = createTeamCard(
                laugherImageView,
                laugherButton,
                laugherInfoButton,
                laugherDetailsBox);

        HBox teamsRow = new HBox(24, scarerCard, laugherCard);
        teamsRow.setAlignment(Pos.CENTER);
        HBox.setHgrow(scarerCard, Priority.ALWAYS);
        HBox.setHgrow(laugherCard, Priority.ALWAYS);
        teamsRow.setMaxWidth(Double.MAX_VALUE);

        Label titleLabel = new Label("Choose Your Side");
        titleLabel.setStyle("-fx-font-size: 34px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label subtitleLabel = new Label("Press ! to reveal their lineup.");
        subtitleLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #e4edf7;");

        VBox content = new VBox(22, titleLabel, subtitleLabel, teamsRow);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(28));
        content.setMaxWidth(Double.MAX_VALUE);

        BorderPane overlay = new BorderPane();
        overlay.setCenter(content);
        BorderPane.setAlignment(startGameButton, Pos.BOTTOM_CENTER);
        BorderPane.setMargin(startGameButton, new Insets(0, 0, 28, 0));
        overlay.setBottom(startGameButton);
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.18);");
        overlay.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        StackPane pageRoot = new StackPane(backgroundImageView, overlay);
        backgroundImageView.fitWidthProperty().bind(pageRoot.widthProperty());
        backgroundImageView.fitHeightProperty().bind(pageRoot.heightProperty());
        return pageRoot;
    }

    private StackPane createTeamCard(
            ImageView imageView,
            Button chooseButton,
            Button infoButton,
            VBox detailsBox) {
        HBox headerRow = new HBox(infoButton);
        headerRow.setAlignment(Pos.TOP_RIGHT);

        VBox overlayContent = new VBox(14, headerRow);
        overlayContent.setAlignment(Pos.TOP_LEFT);
        overlayContent.setPadding(new Insets(24));
        overlayContent.setMaxWidth(360);

        BorderPane overlayPanel = new BorderPane();
        overlayPanel.setLeft(overlayContent);
        overlayPanel.setPadding(new Insets(12));
        overlayPanel.setStyle(
                "-fx-background-color: linear-gradient(to right, rgba(4, 10, 18, 0.78), rgba(4, 10, 18, 0.06));");

        StackPane card = new StackPane(imageView, overlayPanel);
        card.setPrefSize(620, 720);
        card.setMaxSize(620, 720);
        card.setStyle("-fx-background-radius: 24; -fx-border-radius: 24; -fx-border-color: rgba(255,255,255,0.16);");
        StackPane.setAlignment(overlayPanel, Pos.CENTER_LEFT);
        StackPane.setAlignment(chooseButton, Pos.BOTTOM_CENTER);
        StackPane.setMargin(chooseButton, new Insets(0, 0, 26, 0));
        StackPane.setAlignment(detailsBox, Pos.CENTER);
        StackPane.setMargin(detailsBox, new Insets(70, 28, 28, 28));
        card.getChildren().add(chooseButton);
        card.getChildren().add(detailsBox);
        return card;
    }

    private Button createSelectButton(String text, Role role) {
        Button button = new Button(text);
        button.setStyle(SELECT_BUTTON_STYLE);
        button.setOnMousePressed(event -> SoundManager.playButtonSound());
        button.setOnAction(event -> updateSelection(role));
        return button;
    }

    private VBox createDetailsBox(Role role) {
        Label header = new Label("Team Details");
        header.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");

        VBox detailsContent = buildRoleDetailsContent(role);

        ScrollPane scrollPane = new ScrollPane(detailsContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(420);
        scrollPane.setMaxHeight(420);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        VBox detailsBox = new VBox(10, header, scrollPane);
        detailsBox.setStyle(DETAILS_PANEL_STYLE);
        detailsBox.setVisible(false);
        detailsBox.setManaged(false);
        detailsBox.setMaxWidth(500);
        return detailsBox;
    }

    private Button createInfoButton() {
        Button button = new Button("!");
        button.setStyle(INFO_BUTTON_STYLE);
        button.setOnMousePressed(event -> SoundManager.playButtonSound());
        return button;
    }

    private Button createStartButton() {
        Button button = new Button("Start Game");
        button.setDisable(true);
        button.setStyle(DISABLED_START_BUTTON_STYLE);
        button.setOnMouseEntered(event -> {
            if (!button.isDisabled()) {
                button.setStyle(START_BUTTON_HOVER_STYLE);
            }
        });
        button.setOnMouseExited(event -> {
            if (!button.isDisabled()) {
                button.setStyle(START_BUTTON_STYLE);
            }
        });
        button.setOnMousePressed(event -> {
            SoundManager.playButtonSound();
            if (!button.isDisabled()) {
                button.setStyle(START_BUTTON_PRESSED_STYLE);
            }
        });
        button.setOnMouseReleased(event -> {
            if (!button.isDisabled()) {
                button.setStyle(button.isHover() ? START_BUTTON_HOVER_STYLE : START_BUTTON_STYLE);
            }
        });
        return button;
    }

    private void updateSelection(Role role) {
        selectedRole = role;

        applyRoleState(scarerImageView, scarerDetailsBox, role == Role.SCARER);
        applyRoleState(laugherImageView, laugherDetailsBox, role == Role.LAUGHER);

        startGameButton.setDisable(role == null);
        startGameButton.setStyle(role == null ? DISABLED_START_BUTTON_STYLE : START_BUTTON_STYLE);
    }

    private void applyRoleState(ImageView imageView, VBox detailsBox, boolean selected) {
        String selectedPath = (String) imageView.getProperties().get("selectedImage");
        String dimmedPath = (String) imageView.getProperties().get("dimmedImage");

        if (selectedRole == null) {
            imageView.setImage(loadImage(selectedPath));
            imageView.setOpacity(1.0);
            return;
        }

        imageView.setImage(loadImage(selected ? selectedPath : dimmedPath));
        imageView.setOpacity(selected ? 1.0 : 0.68);
    }

    private void toggleDetails(VBox detailsBox) {
        boolean shouldShow = !detailsBox.isVisible();
        detailsBox.setVisible(shouldShow);
        detailsBox.setManaged(shouldShow);
    }

    private VBox buildRoleDetailsContent(Role role) {
        VBox content = new VBox(14);
        content.setFillWidth(true);

        Label overview = new Label(role == Role.LAUGHER
                ? "Team Laughers\nThese monsters rely on speed, flexibility, and efficient movement to create opportunities quickly."
                : "Team Scarers\nThese monsters use power and strategy to maximize their energy collection.");
        overview.setWrapText(true);
        overview.setStyle("-fx-font-size: 13px; -fx-text-fill: #edf4fb; -fx-line-spacing: 4; -fx-font-weight: bold;");

        content.getChildren().add(overview);

        for (Monster monster : monsters) {
            if (monster.getRole() == role) {
                content.getChildren().add(createMonsterInfoCard(monster));
            }
        }

        return content;
    }

    private VBox createMonsterInfoCard(Monster monster) {
        ImageView portrait = new ImageView(loadImage(getMonsterImagePath(monster.getName())));
        portrait.setFitWidth(36);
        portrait.setFitHeight(36);
        portrait.setPreserveRatio(true);
        portrait.setSmooth(true);

        Label nameLabel = new Label(monster.getName());
        nameLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: 800; -fx-text-fill: white;");

        HBox nameRow = new HBox(10, portrait, nameLabel);
        nameRow.setAlignment(Pos.CENTER_LEFT);

        Label detailsLabel = new Label(buildMonsterDetails(monster));
        detailsLabel.setWrapText(true);
        detailsLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #edf4fb; -fx-line-spacing: 3;");

        VBox card = new VBox(8, nameRow, detailsLabel);
        card.setStyle("-fx-background-color: rgba(255,255,255,0.06); -fx-background-radius: 12; -fx-padding: 10;");
        return card;
    }

    private String buildMonsterDetails(Monster monster) {
        return "Type: " + getMonsterType(monster.getName()) + "\n\n"
                + "Personality: " + getPersonality(monster.getName()) + "\n\n"
                + "Starting Energy: " + monster.getEnergy() + "\n\n"
                + "Traits: " + getTraits(monster.getName());
    }

    private String getMonsterType(String monsterName) {
        switch (monsterName) {
            case "Mike Wazowski":
            case "Fungus":
                return "Dasher";
            case "Celia Mae":
            case "Roz":
                return "Multitasker";
            case "James P. Sullivan":
            case "Yeti":
                return "Dynamo";
            case "Randall Boggs":
            case "Henry J. Waternoose":
                return "Schemer";
            default:
                return "Monster";
        }
    }

    private String getPersonality(String monsterName) {
        switch (monsterName) {
            case "Mike Wazowski":
                return "Fast and funny-the comedy speedster";
            case "Fungus":
                return "Timid assistant-quick but nervous";
            case "Celia Mae":
                return "Organized receptionist-handles everything";
            case "Yeti":
                return "Banished snow monster-surprisingly cheerful";
            case "James P. Sullivan":
                return "The top scarer-powerful and confident";
            case "Randall Boggs":
                return "Sneaky and cunning-always has an angle";
            case "Henry J. Waternoose":
                return "Witty and strategic CEO";
            case "Roz":
                return "Always watching-nothing escapes her notice";
            default:
                return "";
        }
    }

    private String getTraits(String monsterName) {
        switch (monsterName) {
            case "Mike Wazowski":
                return "Moves at 2x speed; can activate Momentum Rush for 3x speed for 3 turns.";
            case "Fungus":
                return "Moves at 2x speed; shares the Momentum Rush powerup capability.";
            case "Celia Mae":
                return "Movement is halved, but all energy changes receive a +200 bonus; can use Focus Mode to move at normal speed for 2 turns.";
            case "Yeti":
                return "Energy gains and losses are doubled; can use Energy Freeze to make an opponent skip a turn.";
            case "James P. Sullivan":
                return "Gains and losses are doubled; possesses the Energy Freeze active powerup.";
            case "Randall Boggs":
                return "All energy changes get a +10 bonus (making losses smaller); can use Chain Attack to steal energy from all other monsters.";
            case "Henry J. Waternoose":
                return "Benefits from the +10 energy manipulation bonus and the unshieldable Chain Attack powerup.";
            case "Roz":
                return "Movement is halved, but receives the +200 energy mastery bonus; can activate Focus Mode.";
            default:
                return "";
        }
    }

    private String getMonsterImagePath(String name) {
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

    private void loadMonsters() {
        try {
            monsters.addAll(DataLoader.readMonsters());
        } catch (java.io.IOException exception) {
            monsters.clear();
        }
    }

    private ImageView createBackgroundView() {
        ImageView backgroundView = new ImageView(loadImage(PAGE_BACKGROUND_IMAGE));
        backgroundView.setPreserveRatio(false);
        return backgroundView;
    }

    private ImageView createTeamImageView(Image image) {
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
