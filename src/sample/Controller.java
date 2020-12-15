package sample;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.TextAlignment;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static java.lang.Thread.sleep;

public class Controller implements Initializable {

    @FXML
    private VBox vboxLeft;
    @FXML
    private VBox vboxRight;
    @FXML
    private TextField inputField;
    @FXML
    private Line leftLine;
    @FXML
    private Line rightLine;
    @FXML
    private ScrollPane scroll;

    private int currIndexLeft = -1;
    private int currIndexRight = -1;

    private int tableSize = 15;

    private int r1 = (int) Math.round(Math.random() * 1000000000);
    private int r2 = (int) Math.round(Math.random() * 1000000000);

    private ArrayList<String> T1 = new ArrayList<>();
    private ArrayList<String> T2 = new ArrayList<>();

    private String input = "";

    private boolean found = false;

    private LinesOperator linesOperator;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < tableSize; i++) {
            T1.add("");
            vboxLeft.getChildren().add(createDefaultLabel());
        }

        for (int i = 0; i < tableSize; i++) {
            T2.add("");
            vboxRight.getChildren().add(createDefaultLabel());
        }

        linesOperator = new LinesOperator(inputField, leftLine, rightLine, vboxLeft, vboxRight);
        putKey(hash("key1", 1), "key1", true);
        putKey(hash("key2", 2), "key2", false);

        scroll.vvalueProperty().addListener((observable, oldValue, newValue) -> {
            linesOperator.drawLine(true, currIndexLeft);
            linesOperator.drawLine(false, currIndexRight);
        });

        // disparrays();
        //((Label)vboxLeft.getChildren().get(3)).setPrefWidth(150);
        //((Label)vboxLeft.getChildren().get(3)).setMaxHeight(400);
    }

    private Label createDefaultLabel() {
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
        Platform.runLater(() -> inputField.requestFocus());
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

    // process change in input box
    public void inputChange() {
        input = inputField.getText();

        if (input.length() > 0) {
            int x1 = hash(input, 1);
            int x2 = hash(input, 2);

            currIndexLeft = x1;
            currIndexRight = x2;

            refreshBackground();

            linesOperator.drawLine(true, x1);
            linesOperator.drawLine(false, x2);

            Platform.runLater(() -> inputField.requestFocus());

            highlight(true, x1, (T1.get(x1).equals(input)));
            highlight(false, x2, (T2.get(x2).equals(input)));

            //document.getElementById("message").innerHTML = "<font color='green'>FOUND</font>";
            //document.getElementById("message").innerHTML = "<font color='red'>NOT FOUND</font>";
            found = (T1.get(x1).equals(input)) || (T2.get(x2).equals(input));
        } else {
            //document.getElementById("message").innerHTML = "";
            currIndexRight = -1;
            currIndexLeft = -1;
            refreshBackground();
        }
        inputField.requestFocus();
    }

    private void refreshBackground() {
        //ovo zna da zeza valjda

        linesOperator.resetLines();

        // optimization just refresh ones with index
        //((Label)vboxLeft.getChildren().get(currIndexLeft)).setBackground(new Background(new BackgroundFill(Color.AZURE, new CornerRadii(5.0), null)));
        //((Label)vboxRight.getChildren().get(currIndexRight)).setBackground(new Background(new BackgroundFill(Color.AZURE, new CornerRadii(5.0), null)));

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
            ((Label) vboxLeft.getChildren().get(index)).setBackground(new Background(new BackgroundFill(successful ? Color.GREENYELLOW : Color.web("FF0000CD"), new CornerRadii(5.0), null)));
        } else {
            ((Label) vboxRight.getChildren().get(index)).setBackground(new Background(new BackgroundFill(successful ? Color.GREENYELLOW : Color.web("FF0000CD"), new CornerRadii(5.0), null)));
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
        String curr = inputField.getText();
        if (found) {
            inputField.setBackground(new Background(new BackgroundFill(Color.ORANGERED, new CornerRadii(5.0), null)));
            return;
        }
        if (input.length() > 0) {
            if (addLeft(input, 16)) {
                if (T1.get(currIndexLeft).equals(curr)) {
                    highlight(true, currIndexLeft, true);
                } else {
                    highlight(false, currIndexRight, true);
                }
                inputField.setText("");
                inputField.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, new CornerRadii(5.0), null)));
            } else {
                inputChange();
                inputField.setBackground(new Background(new BackgroundFill(Color.ORANGERED, new CornerRadii(5.0), null)));
            }
            return;
        }
        inputChange();
        inputField.setBackground(new Background(new BackgroundFill(Color.ORANGERED, new CornerRadii(5.0), null)));
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

    public void expand(ActionEvent actionEvent) {
        expandTables(2);
    }
}
