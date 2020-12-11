package sample;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.TextAlignment;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static java.lang.Thread.sleep;

public class Controller implements Initializable {

    public VBox vboxLeft;
    public AnchorPane midPane;
    public VBox vboxRight;
    public TextField inputField;
    public Line leftLine;
    public Line rightLine;
    public ScrollPane scroll;
    private int currIndexLeft=-1;
    private int currIndexRight=-1;
    private final double inputXStart = 443.0;
    private double inputYStart = 466.0;

    int r1 = (int) Math.round(Math.random() * 1000000000);
    int r2 = (int) Math.round(Math.random() * 1000000000);

    int tableSize = 15;
    double step = 0;
    ArrayList<String> T1 = new ArrayList<>();
    ArrayList<String> T2 = new ArrayList<>();
    String input = "";
    boolean found = false;

    public void expandTables(double times) {


        //inputField.requestFocus();
        //inputField.setText("");

        // Requesting focus in separate Thread because at the time of initialize() controls are not yet ready to handle focus.
        Platform.runLater(() -> inputField.requestFocus());

        for (int i = tableSize; i < tableSize * times; i++) {
            Label label = new Label("");
            label.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-wrap-text:true; -fx-border-color:black;");
            label.setPadding(new Insets(5));
            label.setAlignment(Pos.CENTER);
            label.setMaxWidth(Double.MAX_VALUE);
            label.setTextAlignment(TextAlignment.CENTER);
            T1.add("");

            vboxLeft.getChildren().add(label);
        }

        for (int i = tableSize; i < tableSize * times; i++) {
            Label label = new Label("");
            label.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-wrap-text:true; -fx-border-color:black;");
            label.setPadding(new Insets(5));
            label.setAlignment(Pos.CENTER);
            label.setMaxWidth(Double.MAX_VALUE);
            label.setTextAlignment(TextAlignment.CENTER);
            T2.add("");

            vboxRight.getChildren().add(label);
        }


        tableSize *= times;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        //inputField.requestFocus();
        //inputField.setText("");

        // Requesting focus in separate Thread because at the time of initialize() controls are not yet ready to handle focus.


        for (int i = 0; i < tableSize; i++) {
            Label label = new Label("");
            label.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-wrap-text:true; -fx-border-color:black;");
            label.setPadding(new Insets(5));
            label.setAlignment(Pos.CENTER);
            label.setMaxWidth(Double.MAX_VALUE);
            label.setTextAlignment(TextAlignment.CENTER);
            T1.add("");

            vboxLeft.getChildren().add(label);
        }

        for (int i = 0; i < tableSize; i++) {
            Label label = new Label("");
            label.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-wrap-text:true; -fx-border-color:black;");
            label.setPadding(new Insets(5));
            label.setAlignment(Pos.CENTER);
            label.setMaxWidth(Double.MAX_VALUE);
            label.setTextAlignment(TextAlignment.CENTER);
            T2.add("");

            vboxRight.getChildren().add(label);

        }


        //expandTables(1);
        putKey(hash("TEST1", 1), "TEST1", true);
        putKey(hash("TEST2", 2), "TEST2", false);

        Platform.runLater(() -> inputField.requestFocus());

        leftLine.setStartX(inputField.getLayoutX());
        leftLine.setStartY(inputField.getLayoutY());
        leftLine.setEndX(inputField.getLayoutX());
        leftLine.setEndY(inputField.getLayoutY() + inputField.getPrefHeight());
        rightLine.setStartX(inputField.getLayoutX() + inputField.getPrefWidth());
        rightLine.setStartY(inputField.getLayoutY());
        rightLine.setEndX(inputField.getLayoutX() + inputField.getPrefWidth());
        rightLine.setEndY(inputField.getLayoutY() + inputField.getPrefHeight());


        scroll.vvalueProperty().addListener((observable, oldValue, newValue) -> {
            drawLine(true, currIndexLeft);
            drawLine(false, currIndexRight);
        });

        // disparrays();
        //((Label)vboxLeft.getChildren().get(3)).setPrefWidth(150);
        //((Label)vboxLeft.getChildren().get(3)).setMaxHeight(400);
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

        refreshArrays();
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

    // process change in input box
    public void inputChange() {
        input = inputField.getText();
        //clearfield(); just disable arrows
        if (input.length() > 0) {
            int x1 = hash(input, 1);
            int x2 = hash(input, 2);
            //draw arrows
            //arrow(x1, 1, 0, 1);
            //arrow(x2, 2, 0, 1);
            currIndexLeft=x1;
            currIndexRight=x2;
            drawLine(true,x1);
            drawLine(false,x2);

            //obrisi sve hajlajtovano
            refreshArrays();
            Platform.runLater(() -> inputField.requestFocus());
            // osenci sta treba
            highlight(true, x1, (T1.get(x1).equals(input)));
            highlight(false, x2, (T2.get(x2).equals(input)));

            //document.getElementById("message").innerHTML = "<font color='green'>FOUND</font>";
            //document.getElementById("message").innerHTML = "<font color='red'>NOT FOUND</font>";
            found = (T1.get(x1).equals(input)) || (T2.get(x2).equals(input));
        } else {
            //document.getElementById("message").innerHTML = "";
            refreshArrays();
            currIndexRight=-1;
            currIndexLeft=-1;
        }
        inputField.requestFocus();
    }

    private void refreshArrays() {


        ObservableList<Node> children = vboxLeft.getChildren();
        for (Node l : children) {
            ((Label) l).setBackground(new Background(new BackgroundFill(Color.AZURE, new CornerRadii(5.0), null)));
        }
        children = vboxRight.getChildren();
        for (Node l : children) {
            ((Label) l).setBackground(new Background(new BackgroundFill(Color.AZURE, new CornerRadii(5.0), null)));
        }

        inputField.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5.0), null)));
    }

    public void highlight(boolean leftSide, int index, boolean successful) {
        if (leftSide) {
            ((Label) vboxLeft.getChildren().get(index)).setBackground(new Background(new BackgroundFill(successful ? Color.GREEN : Color.RED, new CornerRadii(5.0), null)));
        } else {
            ((Label) vboxRight.getChildren().get(index)).setBackground(new Background(new BackgroundFill(successful ? Color.GREEN : Color.RED, new CornerRadii(5.0), null)));
        }
    }

    public void delete() {
        //input = inputField.getText();
        inputChange();
        if (!found) return;
        if (input.length() > 0) {
            removeKey(input);
        }
    }

    public void add() {
        inputChange();
        String curr=inputField.getText();
        if (found) {
            inputField.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(5.0), null)));
            return;
        }
        if (input.length() > 0) {
            if (addLeft(input, 16)) {
                if(T1.get(currIndexLeft).equals(curr)){
                highlight(true,currIndexLeft,true);}
                else{
                highlight(false,currIndexRight,true);
                }}
            inputField.setText("");
            return;
        }
        inputChange();
        inputField.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(5.0), null)));
    }

    private boolean addLeft(String key, int i) {
        if (i < 0) {
            inputField.setText(key);
            return false;
        }
        String s = putKey(hash(key, 1), key, true);
        if (!s.equals("")) {
            inputField.setText(s);
            return addRight(s, i - 1);
        }
        return true;
    }

    private boolean addRight(String key, int i) {
        if (i < 0) {
            inputField.setText(key);
            return false;
        }
        String s = putKey(hash(key, 2), key, false);
        if (!s.equals("")) {
            return addLeft(s, i - 1);
        }
        return true;
    }

    // dummy hash function loosely based on http://stackoverflow.com/questions/7616461/generate-a-hash-from-string-in-javascript-jquery
    // note: this is fine for visualization, but not for any real application
    int hash(String stri, int variant) {
        int hash = 0, i, chr, len;
        if (stri.length() == 0) return hash;
        for (i = 0, len = stri.length(); i < len; i++) {
            chr = stri.charAt(i);
            hash = ((hash << 5) - hash) + chr + (chr * ((variant == 1) ? r1 : r2) << i);
            hash |= 0;
        }
        hash = Math.abs(hash) % tableSize;
        return hash;
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        //drawLine();
    }

    public void drawLine(boolean left, int index) {//boolean left, int index
        //System.out.println(inputField.getB);
        if((left&&currIndexLeft<0)||(!left&&currIndexRight<0)){
            return;
        }
        new Thread(() -> {

            if (left) {
                Bounds b = vboxLeft.localToScreen((vboxLeft.getChildren().get(0)).getBoundsInLocal());
                b = leftLine.screenToLocal(b);
                leftLine.setStartX(b.getCenterX() + b.getWidth() / 2);
                leftLine.setStartY(b.getCenterY() + b.getHeight() * index);
                leftLine.setEndX(inputField.getLayoutX());
                leftLine.setEndY(inputField.getLayoutY() + inputField.getHeight() / 2);
            } else {
                Bounds b = vboxRight.localToScreen((vboxRight.getChildren().get(0)).getBoundsInLocal());
                b = rightLine.screenToLocal(b);
                rightLine.setStartX(inputField.getLayoutX() + inputField.getWidth());
                rightLine.setStartY(inputField.getLayoutY() + inputField.getHeight() / 2);
                rightLine.setEndX(b.getCenterX() - b.getWidth() / 2);
                rightLine.setEndY(b.getCenterY() + b.getHeight() * index);
            }
        }).start();

    }

    public void delHalf(ActionEvent actionEvent) {
        expandTables(2);
        this.mouseClicked(null);
    }

    public void scrollStart(ScrollEvent scrollEvent) {

    }

    public void scrollEnd(ScrollEvent scrollEvent) {
        //drawLine();

    }

    public void scroll(ScrollEvent scrollEvent) {


    }
}
