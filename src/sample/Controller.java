package sample;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.TextAlignment;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static java.lang.Thread.sleep;

public class Controller implements Initializable {

    public ButtonBar buttonBar;
    public RadioButton stepByStepButton;
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

    boolean stepByStep = false;

    private LinesOperator linesOperator;
    private TablesOperator tablesOperator;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // ne znam da li treba ovo ili ne, ali kao za svaki slucaj
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
        tablesOperator = new TablesOperator(vboxLeft, vboxRight);
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
        label.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-wrap-text:false; -fx-border-color:black;");
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
            Platform.runLater(() -> ((Label) vboxLeft.getChildren().get(index)).setText(key));

        } else {
            T2.add(index, key);
            Platform.runLater(() -> ((Label) vboxRight.getChildren().get(index)).setText(key));
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
            String res = T2.get(x2);
            T2.set(x2, "");
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

            tablesOperator.highlight(true, x1, (T1.get(x1).equals(input)));
            tablesOperator.highlight(false, x2, (T2.get(x2).equals(input)));

            //document.getElementById("message").innerHTML = "<font color='green'>FOUND</font>";
            //document.getElementById("message").innerHTML = "<font color='red'>NOT FOUND</font>";
            found = (T1.get(x1).equals(input)) || (T2.get(x2).equals(input));
        } else {
            //document.getElementById("message").innerHTML = "";
            currIndexRight = -1;
            currIndexLeft = -1;
            refreshBackground();
        }
        Platform.runLater(() -> inputField.requestFocus());
    }

    private void refreshBackground(boolean left) {

        linesOperator.resetLines(left);
        tablesOperator.refreshTables(left);
        Platform.runLater(() -> inputField.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5.0), null))));
    }

    private void refreshBackground() {
        linesOperator.resetLines();
        tablesOperator.refreshTables();
        Platform.runLater(() -> inputField.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5.0), null))));
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
        //ne mora
        //inputChange();
        //mainPane.setDisable(true);
        buttonBar.setDisable(true);
        inputField.setEditable(false);
        new Thread(() -> {

            String curr = inputField.getText();
            if (found) {
                inputField.setBackground(new Background(new BackgroundFill(Color.ORANGERED, new CornerRadii(5.0), null)));
                buttonBar.setDisable(false);
                inputField.setEditable(true);
                return;
            }
            Platform.runLater(() -> inputField.requestFocus());

            //refreshBackground();
            if (input.length() > 0) {
                if (addLeft(input, 16)) {
                    if (T1.get(currIndexLeft).equals(curr)) {
                        tablesOperator.highlight(true, currIndexLeft, true);
                        linesOperator.drawLine(true, currIndexLeft);
                        refreshBackground(false);
                    } else {
                        tablesOperator.highlight(false, currIndexRight, true);
                        linesOperator.drawLine(true, currIndexRight);
                        refreshBackground(true);
                    }
                    Platform.runLater(() -> {
                        inputField.setText("");
                        inputField.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, new CornerRadii(5.0), null)));
                    });
                } else {
                    inputChange();
                    Platform.runLater(() -> inputField.setBackground(new Background(new BackgroundFill(Color.ORANGERED, new CornerRadii(5.0), null))));
                }
                buttonBar.setDisable(false);
                inputField.setEditable(true);
                return;
            }
            inputChange();
            Platform.runLater(() -> inputField.setBackground(new Background(new BackgroundFill(Color.ORANGERED, new CornerRadii(5.0), null))));
            buttonBar.setDisable(false);
            inputField.setEditable(true);
        }).start();
    }

    private void stepByStep(boolean left, String s, int index) {
        if (!stepByStep) {
            return;
        }
        Platform.runLater(() -> {
            refreshBackground(!left);
            inputField.setText(s);
            linesOperator.drawLine(left, index);
            tablesOperator.highlight(left, index, Color.YELLOW);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean addLeft(String key, int i) {
        if (i < 0) {
            inputField.setText(key);
            return false;
        }
        int index = hash(key, 1);
        String s = putKey(index, key, true);
        stepByStep(true, s, index);
        if (!s.equals("")) {
            return addRight(s, i - 1);
        }
        return true;
    }

    private boolean addRight(String key, int i) {
        // ne moze da se desi
        if (i < 0) {
            inputField.setText(key);
            return false;
        }
        //ovde treba da se hajlajtuje desno polje i da postoji desna strelica a ostalo da se disejbluje
        int index = hash(key, 2);
        String s = putKey(index, key, false);
        stepByStep(false, s, index);
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

    public void changeStepByStep(ActionEvent actionEvent) {
        stepByStep = stepByStepButton.isSelected();
    }
}
