package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import java.security.cert.PolicyNode;
import java.util.ArrayList;

public class TablesOperator {


    private final VBox vboxLeft;
    private final VBox vboxRight;

    ArrayList<Integer> leftTable = new ArrayList<>();
    ArrayList<Integer> rightTable = new ArrayList<>();

    public TablesOperator(VBox vboxLeft, VBox vboxRight) {
        this.vboxLeft = vboxLeft;
        this.vboxRight = vboxRight;
    }

    public void highlight(boolean leftSide, int index, boolean successful) {
        if (leftSide) {
            ((Label) vboxLeft.getChildren().get(index)).setBackground(new Background(new BackgroundFill(successful ? Color.GREENYELLOW : Color.web("FF0000CD"), new CornerRadii(5.0), null)));
            leftTable.add(index);
        } else {
            ((Label) vboxRight.getChildren().get(index)).setBackground(new Background(new BackgroundFill(successful ? Color.GREENYELLOW : Color.web("FF0000CD"), new CornerRadii(5.0), null)));
            rightTable.add(index);
        }
    }

    public void highlight(boolean leftSide, int index, Color color) {
        if (leftSide) {
            ((Label) vboxLeft.getChildren().get(index)).setBackground(new Background(new BackgroundFill(color, new CornerRadii(5.0), null)));
            leftTable.add(index);
        } else {
            ((Label) vboxRight.getChildren().get(index)).setBackground(new Background(new BackgroundFill(color, new CornerRadii(5.0), null)));
            rightTable.add(index);
        }
    }

    // maybe in Platform.runlater() think is that what we want
    public void refreshTables() {
        refreshTables(true);
        refreshTables(false);
    }

    public void refreshTables(boolean left) {
        if (left) {
            for (int i : leftTable) {
                ((Label) vboxLeft.getChildren().get(i)).setBackground(new Background(new BackgroundFill(Color.AZURE, new CornerRadii(5.0), null)));
            }
            leftTable = new ArrayList<>();
        } else {
            for (int i : rightTable) {
                ((Label) vboxRight.getChildren().get(i)).setBackground(new Background(new BackgroundFill(Color.AZURE, new CornerRadii(5.0), null)));
            }
            rightTable = new ArrayList<>();
        }
    }
}
