package game.view;

import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import game.engine.cells.*;
import game.engine.monsters.*;
import javafx.geometry.*;
import game.engine.Role;
import javafx.scene.image.ImageView;

public class CellView extends StackPane {
    private final Label numberLabel;
    private final Label playerLabel;
    private final Label opponentLabel;
    private final Label doorLabel;

    private static final String[] DOOR_IMAGE_PATHS = {
            "/game/assets/Doors/blueDoor.png",
            "/game/assets/Doors/redDoor.png",
            "/game/assets/Doors/openDoor.png"
    };
    private static final String SOCK_PATH = "/game/assets/transport/sock.png";
    private static final String OPEN_DOOR_PATH = "/game/assets/Doors/openDoor.png";
    private static final String BELT_PATH = "/game/assets/transport/belt.png";
    private static final String CARDS_PATH = "/game/assets/cards/Cards.png";

    private final ImageView doorImageView;
    private final ImageView sockImageView;
    private final ImageView beltImageView;
    private final ImageView cardImageView;
    private final ImageView monsterImageView;

    public CellView() {
        numberLabel = new Label();
        playerLabel = new Label("YOU");
        opponentLabel = new Label("OPP");
        doorLabel = new Label();

        this.getChildren().add(numberLabel);

        StackPane.setAlignment(numberLabel, Pos.TOP_LEFT);
        StackPane.setAlignment(playerLabel, Pos.TOP_RIGHT);
        StackPane.setAlignment(opponentLabel, Pos.BOTTOM_RIGHT);
        StackPane.setAlignment(doorLabel, Pos.BOTTOM_LEFT);

        playerLabel.setVisible(false);
        opponentLabel.setVisible(false);

        setPrefSize(70, 70);
        setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-background-color: lightgray;");

        StackPane.setMargin(numberLabel, new Insets(4));
        StackPane.setMargin(playerLabel, new Insets(4));
        StackPane.setMargin(opponentLabel, new Insets(4));
        StackPane.setMargin(doorLabel, new Insets(4));

        doorImageView = new ImageView();
        doorImageView.setFitWidth(52);
        doorImageView.setFitHeight(52);
        doorImageView.setPreserveRatio(true);

        sockImageView = new ImageView();
        sockImageView.setFitWidth(42);
        sockImageView.setFitHeight(42);
        sockImageView.setPreserveRatio(true);

        beltImageView = new ImageView();
        beltImageView.setFitWidth(48);
        beltImageView.setFitHeight(48);
        beltImageView.setPreserveRatio(true);

        cardImageView = new ImageView();
        cardImageView.setFitWidth(42);
        cardImageView.setFitHeight(42);
        cardImageView.setPreserveRatio(true);

        monsterImageView = new ImageView();
        monsterImageView.setFitWidth(60);
        monsterImageView.setFitHeight(52);
        monsterImageView.setPreserveRatio(true);

        monsterImageView.setSmooth(true);
        doorImageView.setSmooth(true);
        sockImageView.setSmooth(true);
        beltImageView.setSmooth(true);
        cardImageView.setSmooth(true);

        this.getChildren().add(doorImageView);
        this.getChildren().add(sockImageView);
        this.getChildren().add(beltImageView);
        this.getChildren().add(cardImageView);
        this.getChildren().add(monsterImageView);

        StackPane.setAlignment(monsterImageView, Pos.CENTER);
        StackPane.setAlignment(doorImageView, Pos.CENTER);
        StackPane.setAlignment(sockImageView, Pos.CENTER);
        StackPane.setAlignment(beltImageView, Pos.CENTER);
        StackPane.setAlignment(cardImageView, Pos.CENTER);
        this.getChildren().add(playerLabel);
        this.getChildren().add(opponentLabel);
        this.getChildren().add(doorLabel);

    }

    public void updateCell(
            Cell cell,
            int index,
            Monster player,
            Monster opponent,
            int playerPosition,
            int opponentPosition) {

        numberLabel.setText("" + index);
        monsterImageView.setImage(null);
        doorImageView.setImage(null);
        beltImageView.setImage(null);
        cardImageView.setImage(null);
        sockImageView.setImage(null);

        playerLabel.setVisible(false);
        opponentLabel.setVisible(false);
        doorLabel.setVisible(false);
        if (player.getRole() == Role.LAUGHER) {
            playerLabel.setStyle(
                    "-fx-background-color: #2F80ED; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 2 6 2 6;");
            opponentLabel.setStyle(
                    "-fx-background-color: #D64545; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 2 6 2 6;");
        } else {
            opponentLabel.setStyle(
                    "-fx-background-color: #2F80ED; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 2 6 2 6;");
            playerLabel.setStyle(
                    "-fx-background-color: #D64545; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 2 6 2 6;");

        }

        if (playerPosition == index) {
            playerLabel.setVisible(true);
        }

        if (opponentPosition == index) {
            opponentLabel.setVisible(true);
        }

        if (cell instanceof CardCell) {
            cardImageView.setImage(loadImage(CARDS_PATH));
        }

        else if (cell instanceof ContaminationSock) {
            sockImageView.setImage(loadImage(SOCK_PATH));

        }

        else if (cell instanceof ConveyorBelt) {
            beltImageView.setImage(loadImage(BELT_PATH));
        }

        else if (cell instanceof DoorCell) {
            DoorCell doorCell = (DoorCell) cell;
            setStyle(
                    "-fx-border-color: #1E4F73; -fx-border-width: 2; -fx-background-color: linear-gradient(to bottom, #8FD3FF, #3D79A6);");

            if (doorCell.isActivated()) {
                doorImageView.setImage(loadImage(OPEN_DOOR_PATH));
            } else {
                doorLabel.setText("" + doorCell.getEnergy());
                doorLabel.setVisible(true);
                if (doorCell.getRole() == Role.LAUGHER) {
                    doorImageView.setImage(loadImage(DOOR_IMAGE_PATHS[0]));
                    doorLabel.setStyle("-fx-text-fill: #4FC3F7; -fx-font-weight: bold;");
                } else {
                    doorImageView.setImage(loadImage(DOOR_IMAGE_PATHS[1]));
                    doorLabel.setStyle("-fx-text-fill: #E53935; -fx-font-weight: bold;");
                }
            }

        }

        else if (cell instanceof MonsterCell) {

            MonsterCell mc = (MonsterCell) cell;
            if (mc.getCellMonster().getRole() == Role.LAUGHER) {

                monsterImageView.setImage(loadImage(getMonsterImagePath(mc.getCellMonster())));

                setStyle("-fx-border-color: Green ; -fx-border-width: 2");
            } else {

                monsterImageView.setImage(loadImage(getMonsterImagePath(mc.getCellMonster())));
                setStyle("-fx-border-color: red ; -fx-border-width: 2");
            }
        } else {
            setStyle(
                    "-fx-border-color: #3C4148; -fx-border-width: 2; -fx-background-color: linear-gradient(to bottom, #F4F7FA, #AEB7C1);");
            if (index == playerPosition && index != opponentPosition) {
                monsterImageView.setImage(loadImage(getMonsterImagePath(player)));

                playerLabel.setVisible(true);

            }
            if (index == opponentPosition && index != playerPosition) {
                monsterImageView.setImage(loadImage(getMonsterImagePath(opponent)));

                opponentLabel.setVisible(true);

            }
        }
    }

    private Image loadImage(String path) {
        return new Image(getClass().getResourceAsStream(path));
    }

    private String getMonsterImagePath(Monster monster) {
        String name = monster.getName();

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
                return null;
        }
    }

}
