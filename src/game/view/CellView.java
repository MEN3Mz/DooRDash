package game.view;

import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import game.engine.cells.*;
import game.engine.monsters.*;
import javafx.geometry.*;

import java.util.Random;

import game.engine.Role;
import javafx.scene.image.ImageView;

public class CellView extends StackPane {
    private final Label numberLabel;
    private final Label typeLabel;
    private static final String[] DOOR_IMAGE_PATHS = {
            "/game/assets/Doors/blueDoor.png",
            "/game/assets/Doors/redDoor.png",
            "/game/assets/Doors/openDoor.png"
    };

    private static final String OPEN_DOOR_PATH = "/game/assets/Doors/openDoor.png";
    private static final Random RANDOM = new Random();

    private final ImageView doorImageView;
    private Image chosenDoorImage;

    public CellView() {
        numberLabel = new Label();
        typeLabel = new Label();
        this.getChildren().add(numberLabel);
        this.getChildren().add(typeLabel);

        StackPane.setAlignment(numberLabel, Pos.TOP_LEFT);
        StackPane.setAlignment(typeLabel, Pos.BOTTOM_CENTER);

        setPrefSize(70, 70);
        setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-background-color: lightgray;");

        StackPane.setMargin(numberLabel, new Insets(4));
        StackPane.setMargin(typeLabel, new Insets(4));

        doorImageView = new ImageView();
        doorImageView.setFitWidth(52);
        doorImageView.setFitHeight(52);
        doorImageView.setPreserveRatio(true);

        this.getChildren().add(doorImageView);

        StackPane.setAlignment(doorImageView, Pos.CENTER);

    }

    public void updateCell(Cell cell, int index) {
        numberLabel.setText("" + index);

        if (cell instanceof CardCell) {
            typeLabel.setText("Card");
        }

        else if (cell instanceof ContaminationSock) {

            typeLabel.setText("Sock");

        }

        else if (cell instanceof ConveyorBelt) {
            typeLabel.setText("Belt");
        }

        else if (cell instanceof DoorCell) {
            DoorCell doorCell = (DoorCell) cell;
            setStyle(
                    "-fx-border-color: #1E4F73; -fx-border-width: 2; -fx-background-color: linear-gradient(to bottom, #8FD3FF, #3D79A6);");

            if (doorCell.isActivated()) {
                doorImageView.setImage(loadImage(OPEN_DOOR_PATH));
            } else {
                if (chosenDoorImage == null) {
                    if (doorCell.getRole() == Role.LAUGHER)
                        doorImageView.setImage(loadImage(DOOR_IMAGE_PATHS[0]));
                    else
                        doorImageView.setImage(loadImage(DOOR_IMAGE_PATHS[1]));
                }
            }

        }

        else if (cell instanceof MonsterCell) {
            typeLabel.setText("Monster");
            if (((MonsterCell) cell).getCellMonster().getRole() == Role.LAUGHER) {

                if (((MonsterCell) cell).getCellMonster() instanceof Dasher) {

                } else if (((MonsterCell) cell).getCellMonster() instanceof Dynamo) {

                } else if (((MonsterCell) cell).getCellMonster() instanceof Schemer) {

                } else if (((MonsterCell) cell).getCellMonster() instanceof MultiTasker) {

                }
            } else {

                if (((MonsterCell) cell).getCellMonster() instanceof Dasher) {

                } else if (((MonsterCell) cell).getCellMonster() instanceof Dynamo) {

                } else if (((MonsterCell) cell).getCellMonster() instanceof Schemer) {

                } else if (((MonsterCell) cell).getCellMonster() instanceof MultiTasker) {

                }
            }
        } else {
            setStyle(
                    "-fx-border-color: #3C4148; -fx-border-width: 2; -fx-background-color: linear-gradient(to bottom, #F4F7FA, #AEB7C1);");
            typeLabel.setText("Normal");
        }
    }

    private Image loadImage(String path) {
        return new Image(getClass().getResourceAsStream(path));
    }

    private Image getRandomDoorImage() {
        String path = DOOR_IMAGE_PATHS[RANDOM.nextInt(DOOR_IMAGE_PATHS.length)];
        return loadImage(path);
    }

}