package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

public class TablesOperator {
    // everything is called with Platform.runLater();

    private final VBox vboxLeft;
    private final VBox vboxRight;

    private ArrayList<Integer> leftTable = new ArrayList<>();
    private ArrayList<Integer> rightTable = new ArrayList<>();

    public TablesOperator(VBox vboxLeft, VBox vboxRight) {
        this.vboxLeft = vboxLeft;
        this.vboxRight = vboxRight;
    }

    // da se doda kod brisanja samo ono sto je oznaceno ovde da se lako izdboji i izbrise
    public void highlight(boolean leftSide, int index, boolean successful) {
        if (leftSide) {
            Platform.runLater(() -> {
                ((Label) vboxLeft.getChildren().get(index)).setBackground(new Background(new BackgroundFill(successful ? Color.GREENYELLOW : Color.web("FF0000CD"), new CornerRadii(5.0), null)));
                leftTable.add(index);
            });
        } else {
            Platform.runLater(() -> {
                ((Label) vboxRight.getChildren().get(index)).setBackground(new Background(new BackgroundFill(successful ? Color.GREENYELLOW : Color.web("FF0000CD"), new CornerRadii(5.0), null)));
                rightTable.add(index);
            });
        }
    }

    public void highlight(boolean leftSide, int index, Color color) {
        if (leftSide) {
            Platform.runLater(() -> {
                ((Label) vboxLeft.getChildren().get(index)).setBackground(new Background(new BackgroundFill(color, new CornerRadii(5.0), null)));
                leftTable.add(index);
            });
        } else {
            Platform.runLater(() -> {
                ((Label) vboxRight.getChildren().get(index)).setBackground(new Background(new BackgroundFill(color, new CornerRadii(5.0), null)));
                rightTable.add(index);
            });
        }
    }

    // maybe in Platform.runlater() think is that what we want
    public void refreshTables() {
        refreshTables(true);
        refreshTables(false);
    }

    public void refreshTables(boolean left) {
        if (left) {
            Platform.runLater(() -> {
                for (int i : leftTable) {
                    ((Label) vboxLeft.getChildren().get(i)).setBackground(new Background(new BackgroundFill(Color.AZURE, new CornerRadii(5.0), null)));
                }

                leftTable = new ArrayList<>();
            });
        } else {
            Platform.runLater(() -> {
                for (int i : rightTable) {
                    ((Label) vboxRight.getChildren().get(i)).setBackground(new Background(new BackgroundFill(Color.AZURE, new CornerRadii(5.0), null)));
                }
                rightTable = new ArrayList<>();
            });
        }
    }

    public void resetTables() {
        Platform.runLater(() -> {
            vboxRight.getChildren().clear();
            vboxLeft.getChildren().clear();
        });
        leftTable = new ArrayList<>();
        rightTable = new ArrayList<>();
    }

    public void expandTables(int size) {
        Platform.runLater(() -> {
            int currIndex = vboxLeft.getChildren().size();
            for (int i = 0; i < size; i++) {
                vboxLeft.getChildren().add(createDefaultLabel(currIndex + i));
                vboxRight.getChildren().add(createDefaultLabel(currIndex + i));
            }
        });
    }

    private Label createDefaultLabel(int currIndex) {
        Label label = new Label("");
        label.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-wrap-text:false; -fx-border-color:black;");
        label.setPadding(new Insets(5));
        label.setAlignment(Pos.CENTER);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setBackground(new Background(new BackgroundFill(Color.AZURE, new CornerRadii(5.0), null)));
        label.setTextAlignment(TextAlignment.CENTER);

        // create a menu
        ContextMenu contextMenu = new ContextMenu();

        // create menuItems
        MenuItem copy = new MenuItem("Copy");
        MenuItem index = new MenuItem("Index [" + currIndex + "]");
        SeparatorMenuItem separator = new SeparatorMenuItem();
        copy.setOnAction((ActionEvent e) -> {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(label.getText());
            clipboard.setContent(content);
        });

        // add menu items to menu
        contextMenu.getItems().addAll(copy, separator, index);
        label.setContextMenu(contextMenu);

        return label;
    }

    public StringBuilder exportTables() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("\"LeftTable\" : {");
        int i = 0;
        for (Node n : vboxLeft.getChildren()) {
            if (i != 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append("\n\"").append(i).append("\" : \"").append(((Label) n).getText()).append("\"");
            i++;
        }
        stringBuilder.append("\n},\n");

        stringBuilder.append("\"RightTable\" : {");
        i = 0;
        for (Node n : vboxRight.getChildren()) {
            if (i != 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append("\n\"").append(i).append("\" : \"").append(((Label) n).getText()).append("\"");
            i++;
        }
        stringBuilder.append("\n}\n");

        return stringBuilder;
    }
}
