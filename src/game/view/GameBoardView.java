package game.view;

import game.engine.Game;
import game.engine.cells.Cell;
import game.engine.cells.ContaminationSock;
import game.engine.cells.ConveyorBelt;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeLineCap;

public class GameBoardView {
    private final GridPane boardGrid;
    private final CellView[][] cellViews;
    private final StackPane boardRoot;
    private final Pane overlayPane;
    private final Game game;

    public GameBoardView(Game game) {
        this.game = game;
        this.cellViews = new CellView[10][10];
        this.boardGrid = new GridPane();
        this.boardRoot = new StackPane();
        this.overlayPane = new Pane();

        overlayPane.setMouseTransparent(true);
        boardRoot.getChildren().addAll(boardGrid, overlayPane);

        buildBoard();
        refreshBoard();

        boardRoot.widthProperty().addListener((obs, oldVal, newVal) -> redrawOverlayLater());
        boardRoot.heightProperty().addListener((obs, oldVal, newVal) -> redrawOverlayLater());
        boardGrid.layoutBoundsProperty().addListener((obs, oldVal, newVal) -> redrawOverlayLater());
    }

    private void buildBoard() {
        for (int index = 0; index < 100; index++) {
            int[] pos = game.getBoard().indexToRowCol(index);
            int row = pos[0];
            int col = pos[1];

            CellView cellView = new CellView();
            cellViews[row][col] = cellView;

            boardGrid.add(cellView, col, 9 - row);
        }
    }

    public void refreshBoard() {
        Cell[][] cells = game.getBoard().getBoardCells();

        for (int index = 0; index < 100; index++) {
            int[] pos = game.getBoard().indexToRowCol(index);
            int row = pos[0];
            int col = pos[1];
            cellViews[row][col].updateCell(cells[row][col], index, game.getPlayer(), game.getOpponent());
        }

        redrawOverlayLater();
    }

    private void redrawOverlayLater() {
        Platform.runLater(this::redrawOverlay);
    }

    private void redrawOverlay() {
        overlayPane.getChildren().clear();

        Cell[][] cells = game.getBoard().getBoardCells();

        for (int index = 0; index < 100; index++) {
            int[] pos = game.getBoard().indexToRowCol(index);
            int row = pos[0];
            int col = pos[1];

            if (cells[row][col] instanceof ContaminationSock) {
                ContaminationSock sock = (ContaminationSock) cells[row][col];
                addTransportArrow(index, index + sock.getEffect(), Color.web("#C94A4A"));
            } else if (cells[row][col] instanceof ConveyorBelt) {
                ConveyorBelt belt = (ConveyorBelt) cells[row][col];
                addTransportArrow(index, index + belt.getEffect(), Color.web("#6FCF97"));
            }
        }
    }

    private void addTransportArrow(int fromIndex, int toIndex, Color color) {
        if (toIndex < 0 || toIndex >= 100) {
            return;
        }

        int[] from = game.getBoard().indexToRowCol(fromIndex);
        int[] to = game.getBoard().indexToRowCol(toIndex);

        CellView fromCell = cellViews[from[0]][from[1]];
        CellView toCell = cellViews[to[0]][to[1]];

        Bounds fromScene = fromCell.localToScene(fromCell.getBoundsInLocal());
        Bounds toScene = toCell.localToScene(toCell.getBoundsInLocal());
        Bounds overlayScene = overlayPane.localToScene(overlayPane.getBoundsInLocal());

        double fromMinX = fromScene.getMinX();
        double fromMaxX = fromScene.getMaxX();
        double fromMinY = fromScene.getMinY();
        double fromMaxY = fromScene.getMaxY();

        double toMinX = toScene.getMinX();
        double toMaxX = toScene.getMaxX();
        double toMinY = toScene.getMinY();
        double toMaxY = toScene.getMaxY();

        double fromCenterX = (fromMinX + fromMaxX) / 2;
        double fromCenterY = (fromMinY + fromMaxY) / 2;
        double toCenterX = (toMinX + toMaxX) / 2;
        double toCenterY = (toMinY + toMaxY) / 2;

        double dx = toCenterX - fromCenterX;
        double dy = toCenterY - fromCenterY;

        double offsetRatio = 0.28;

        double fromOffsetX = (fromMaxX - fromMinX) * offsetRatio;
        double fromOffsetY = (fromMaxY - fromMinY) * offsetRatio;
        double toOffsetX = (toMaxX - toMinX) * offsetRatio;
        double toOffsetY = (toMaxY - toMinY) * offsetRatio;

        double startX = fromCenterX;
        double startY = fromCenterY;
        double endX = toCenterX;
        double endY = toCenterY;

        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0) {
                startX = fromCenterX + fromOffsetX;
                endX = toCenterX - toOffsetX;
            } else {
                startX = fromCenterX - fromOffsetX;
                endX = toCenterX + toOffsetX;
            }
        } else if (Math.abs(dy) > Math.abs(dx)) {
            if (dy > 0) {
                startY = fromCenterY + fromOffsetY;
                endY = toCenterY - toOffsetY;
            } else {
                startY = fromCenterY - fromOffsetY;
                endY = toCenterY + toOffsetY;
            }
        } else {
            if (dx > 0) {
                startX = fromCenterX + fromOffsetX;
                endX = toCenterX - toOffsetX;
            } else {
                startX = fromCenterX - fromOffsetX;
                endX = toCenterX + toOffsetX;
            }

            if (dy > 0) {
                startY = fromCenterY + fromOffsetY;
                endY = toCenterY - toOffsetY;
            } else {
                startY = fromCenterY - fromOffsetY;
                endY = toCenterY + toOffsetY;
            }
        }

        startX -= overlayScene.getMinX();
        startY -= overlayScene.getMinY();
        endX -= overlayScene.getMinX();
        endY -= overlayScene.getMinY();

        Line line = new Line(startX, startY, endX, endY);
        line.setStroke(color);
        line.setStrokeWidth(8);
        line.setStrokeLineCap(StrokeLineCap.ROUND);

        double angle = Math.atan2(endY - startY, endX - startX);
        double arrowLength = 18;
        double arrowWidth = 10;

        double x1 = endX - arrowLength * Math.cos(angle) + arrowWidth * Math.sin(angle);
        double y1 = endY - arrowLength * Math.sin(angle) - arrowWidth * Math.cos(angle);

        double x2 = endX - arrowLength * Math.cos(angle) - arrowWidth * Math.sin(angle);
        double y2 = endY - arrowLength * Math.sin(angle) + arrowWidth * Math.cos(angle);

        Polygon arrowHead = new Polygon(
                endX, endY,
                x1, y1,
                x2, y2);
        arrowHead.setFill(color);

        overlayPane.getChildren().addAll(line, arrowHead);
    }

    public StackPane getBoardRoot() {
        return boardRoot;
    }
}
