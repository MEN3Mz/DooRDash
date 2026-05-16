package game.view;

import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class TurnLogView extends VBox {

    private final VBox entriesBox;

    public TurnLogView(String titleText) {
        getStylesheets().add(getClass().getResource("/game/assets/css/turn-log-view.css").toExternalForm());
        getStyleClass().add("turn-log");
        setSpacing(10);
        setPadding(new Insets(14));
        setPrefWidth(190);
        setMaxWidth(190);

        Label title = new Label(titleText);
        title.setWrapText(true);
        title.getStyleClass().add("turn-log-title");

        entriesBox = new VBox(8);
        getChildren().addAll(title, entriesBox);
    }

    public void updateEvents(List<String> events) {
        entriesBox.getChildren().clear();

        if (events.isEmpty()) {
            entriesBox.getChildren().add(createEntry("No moves yet."));
            return;
        }

        int start = Math.max(0, events.size() - 3);
        for (int i = events.size() - 1; i >= start; i--) {
            entriesBox.getChildren().add(createEntry(events.get(i)));
        }
    }

    private Label createEntry(String message) {
        Label entry = new Label(message);
        entry.setWrapText(true);
        entry.setMaxWidth(160);
        entry.getStyleClass().add("turn-log-entry");

        return entry;
    }
}
