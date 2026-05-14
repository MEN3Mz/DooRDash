package game.view;

import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class TurnLogView extends VBox {

    private final VBox entriesBox;

    public TurnLogView(String titleText) {
        setSpacing(10);
        setPadding(new Insets(14));
        setPrefWidth(190);
        setMaxWidth(190);
        setStyle("""
                -fx-background-color:
                    linear-gradient(to bottom,
                    rgba(31,41,51,0.92),
                    rgba(62,76,89,0.92));
                -fx-background-radius: 14;
                -fx-border-color: #bcccdc;
                -fx-border-width: 2;
                -fx-border-radius: 14;
                """);

        Label title = new Label(titleText);
        title.setWrapText(true);
        title.setStyle("""
                -fx-font-size: 14px;
                -fx-font-weight: 900;
                -fx-text-fill: white;
                """);

        entriesBox = new VBox(8);
        getChildren().addAll(title, entriesBox);
    }

    public void updateEvents(List<String> events) {
        entriesBox.getChildren().clear();

        if (events.isEmpty()) {
            entriesBox.getChildren().add(createEntry("No moves yet."));
            return;
        }

        int start = Math.max(0, events.size() - 2);
        for (int i = events.size() - 1; i >= start; i--) {
            entriesBox.getChildren().add(createEntry(events.get(i)));
        }
    }

    private Label createEntry(String message) {
        Label entry = new Label(message);
        entry.setWrapText(true);
        entry.setMaxWidth(160);
        entry.setStyle("""
                -fx-font-size: 11px;
                -fx-line-spacing: 2px;
                -fx-text-fill: #edf4fb;
                -fx-background-color: rgba(255,255,255,0.06);
                -fx-background-radius: 8;
                -fx-padding: 7;
                """);

        return entry;
    }
}
