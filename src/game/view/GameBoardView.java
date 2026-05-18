package game.view;

import game.engine.Game;
import game.engine.cells.Cell;
import game.engine.cells.ContaminationSock;
import game.engine.cells.ConveyorBelt;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeLineCap;

public class GameBoardView {
    private static final double BOARD_PIXEL_SIZE = CellView.CELL_SIZE * 10;

    private final GridPane boardGrid;
    private final CellView[][] cellViews;
    private final StackPane boardRoot;
    private final Pane overlayPane;
    private final Game game;
    private final String playerOneName;
    private final String playerTwoName;

    public GameBoardView(Game game) {
        this(game, "You", "Opponent");
    }

    public GameBoardView(Game game, String playerOneName, String playerTwoName) {
        this.game = game;
        this.playerOneName = playerOneName;
        this.playerTwoName = playerTwoName;
        this.cellViews = new CellView[10][10];
        this.boardGrid = new GridPane();
        this.boardRoot = new StackPane();
        this.overlayPane = new Pane();

        overlayPane.setMouseTransparent(true);
        overlayPane.setManaged(false);

        boardGrid.setMinSize(BOARD_PIXEL_SIZE, BOARD_PIXEL_SIZE);
        boardGrid.setPrefSize(BOARD_PIXEL_SIZE, BOARD_PIXEL_SIZE);
        boardGrid.setMaxSize(BOARD_PIXEL_SIZE, BOARD_PIXEL_SIZE);
        boardRoot.setMinSize(BOARD_PIXEL_SIZE, BOARD_PIXEL_SIZE);
        boardRoot.setPrefSize(BOARD_PIXEL_SIZE, BOARD_PIXEL_SIZE);
        boardRoot.setMaxSize(BOARD_PIXEL_SIZE, BOARD_PIXEL_SIZE);
        overlayPane.setMinSize(BOARD_PIXEL_SIZE, BOARD_PIXEL_SIZE);
        overlayPane.setPrefSize(BOARD_PIXEL_SIZE, BOARD_PIXEL_SIZE);
        overlayPane.setMaxSize(BOARD_PIXEL_SIZE, BOARD_PIXEL_SIZE);

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

        int playerPosition = game.getPlayer().getPosition();
        int opponentPosition = game.getOpponent().getPosition();
        int playerPreviousPosition = game.getPlayerPreviousPosition();
        int opponentPreviousPosition = game.getOpponentPreviousPosition();

        for (int index = 0; index < 100; index++) {
            int[] pos = game.getBoard().indexToRowCol(index);
            int row = pos[0];
            int col = pos[1];

            cellViews[row][col].updateCell(
                    cells[row][col],
                    index,
                    game.getPlayer(),
                    game.getOpponent(),
                    playerPosition,
                    opponentPosition,
                    playerPreviousPosition,
                    opponentPreviousPosition,
                    playerOneName,
                    playerTwoName);
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
                addTransportArrow(index, index + sock.getEffect(), Color.web("#C94A4A"), isActiveTransportArrow(index));
            } else if (cells[row][col] instanceof ConveyorBelt) {
                ConveyorBelt belt = (ConveyorBelt) cells[row][col];
                addTransportArrow(index, index + belt.getEffect(), Color.web("#6FCF97"), isActiveTransportArrow(index));
            }
        }
    }

    private boolean isActiveTransportArrow(int fromIndex) {
        Cell lastLandedCell = game.getBoard().getLastLandedCell();

        if (!(lastLandedCell instanceof ContaminationSock) && !(lastLandedCell instanceof ConveyorBelt)) {
            return false;
        }

        return game.getBoard().getLastLandedPosition() == fromIndex;
    }

    private void addTransportArrow(int fromIndex, int toIndex, Color color, boolean active) {
        if (toIndex < 0 || toIndex >= 100) {
            return;
        }

        int[] from = game.getBoard().indexToRowCol(fromIndex);
        int[] to = game.getBoard().indexToRowCol(toIndex);

        CellView fromCell = cellViews[from[0]][from[1]];
        CellView toCell = cellViews[to[0]][to[1]];

        Bounds gridBounds = boardGrid.getBoundsInParent();
        Bounds fromBounds = fromCell.getBoundsInParent();
        Bounds toBounds = toCell.getBoundsInParent();

        double fromCenterX = gridBounds.getMinX() + fromBounds.getMinX() + fromBounds.getWidth() / 2;
        double fromCenterY = gridBounds.getMinY() + fromBounds.getMinY() + fromBounds.getHeight() / 2;
        double toCenterX = gridBounds.getMinX() + toBounds.getMinX() + toBounds.getWidth() / 2;
        double toCenterY = gridBounds.getMinY() + toBounds.getMinY() + toBounds.getHeight() / 2;

        double deltaX = toCenterX - fromCenterX;
        double deltaY = toCenterY - fromCenterY;
        double distance = Math.hypot(deltaX, deltaY);
        if (distance == 0) {
            return;
        }

        double targetInset = Math.min(toBounds.getWidth(), toBounds.getHeight()) / 2.0;
        double targetEdgeX = toCenterX - (deltaX / distance) * targetInset;
        double targetEdgeY = toCenterY - (deltaY / distance) * targetInset;

        Line line = new Line(fromCenterX, fromCenterY, targetEdgeX, targetEdgeY);
        line.setStroke(color);
        line.setStrokeWidth(active ? 11 : 6);
        line.setStrokeLineCap(StrokeLineCap.ROUND);
        line.setOpacity(active ? 0.98 : 0.22);

        double angle = Math.atan2(targetEdgeY - fromCenterY, targetEdgeX - fromCenterX);
        double arrowLength = 18;
        double arrowWidth = 10;

        double x1 = targetEdgeX - arrowLength * Math.cos(angle) + arrowWidth * Math.sin(angle);
        double y1 = targetEdgeY - arrowLength * Math.sin(angle) - arrowWidth * Math.cos(angle);

        double x2 = targetEdgeX - arrowLength * Math.cos(angle) - arrowWidth * Math.sin(angle);
        double y2 = targetEdgeY - arrowLength * Math.sin(angle) + arrowWidth * Math.cos(angle);

        Polygon arrowHead = new Polygon(
                targetEdgeX, targetEdgeY,
                x1, y1,
                x2, y2);

        arrowHead.setFill(color);
        arrowHead.setOpacity(active ? 0.98 : 0.22);

        if (active) {
            DropShadow glow = new DropShadow(18, color);
            line.setEffect(glow);
            arrowHead.setEffect(new DropShadow(18, color));
        }

        overlayPane.getChildren().addAll(line, arrowHead);
    }

    public StackPane getBoardRoot() {
        return boardRoot;
    }
}
