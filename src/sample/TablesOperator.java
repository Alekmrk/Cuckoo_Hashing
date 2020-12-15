package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class TablesOperator {


/*
    TablesOperator(){
        for (int i = 0; i < tableSize; i++) {
            T1.add("");
            vboxLeft.getChildren().add(createDefaultLabel());
        }

        for (int i = 0; i < tableSize; i++) {
            T2.add("");
            vboxRight.getChildren().add(createDefaultLabel());
        }


    }


    public String putKey(int index, String key, boolean sideLeft) {
        String oldKey = sideLeft ? T1.remove(index) : T2.remove(index);
        if (sideLeft) {
            T1.add(index, key);
            ((Label) vboxLeft.getChildren().get(index)).setText(key);
        } else {
            T2.add(index, key);
            ((Label) vboxRight.getChildren().get(index)).setText(key);
        }
        return oldKey;
    }

    public String removeKey(String key) {

        int x1 = hash(key, 1);
        int x2 = hash(key, 2);

        refreshBackground();
        if (T1.get(x1).equals(key)) {
            ((Label) vboxLeft.getChildren().get(x1)).setText("");
            String res = T1.get(x1);
            T1.set(x1, "");
            return res;
        }
        if (T2.get(x2).equals(key)) {
            ((Label) vboxRight.getChildren().get(x2)).setText("");
            String res = T1.get(x2);
            T1.set(x2, "");
            return res;
        }
        return null;
    }

    public void highlight(boolean leftSide, int index, boolean successful) {
        if (leftSide) {
            ((Label) vboxLeft.getChildren().get(index)).setBackground(new Background(new BackgroundFill(successful ? Color.GREEN : Color.RED, new CornerRadii(5.0), null)));
        } else {
            ((Label) vboxRight.getChildren().get(index)).setBackground(new Background(new BackgroundFill(successful ? Color.GREEN : Color.RED, new CornerRadii(5.0), null)));
        }
    }

    public Label createDefaultLabel() {
        Label label = new Label("");
        label.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-wrap-text:true; -fx-border-color:black;");
        label.setPadding(new Insets(5));
        label.setAlignment(Pos.CENTER);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setBackground(new Background(new BackgroundFill(Color.AZURE, new CornerRadii(5.0), null)));
        label.setTextAlignment(TextAlignment.CENTER);
        return label;
    }

    public void expandTables(double times) {
        for (int i = tableSize; i < tableSize * times; i++) {
            T1.add("");
            vboxLeft.getChildren().add(createDefaultLabel());
        }

        for (int i = tableSize; i < tableSize * times; i++) {
            T2.add("");
            vboxRight.getChildren().add(createDefaultLabel());
        }

        tableSize *= times;
        // Requesting focus in separate Thread because at the time of initialize() controls are not yet ready to handle focus.

    }*/

}
