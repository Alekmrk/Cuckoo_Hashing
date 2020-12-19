package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.TextAlignment;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private ButtonBar buttonBar;
    @FXML
    private RadioButton stepByStepButton;
    @FXML
    private ComboBox<String> dropListLeft;
    @FXML
    private ComboBox<String> dropListRight;
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
    private Line helperLine;
    @FXML
    private ScrollPane scroll;

    private int currIndexLeft = -1;
    private int currIndexRight = -1;
    private int currIndexHelper = -1;
    private boolean currSideHelper = true;

    private int baseTableSize = 15;
    private int tableSize = 15;
    private int resizedTimes = 0;

    private ArrayList<String> T1 = new ArrayList<>();
    private ArrayList<String> T2 = new ArrayList<>();

    private String input = "";

    private boolean found = false;
    private int foundIndex = -1;

    boolean stepByStep = false;

    private LinesOperator linesOperator;
    private TablesOperator tablesOperator;
    private AlgorithmsOperator algorithmsOperator;

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

        linesOperator = new LinesOperator(inputField, leftLine, rightLine, helperLine, vboxLeft, vboxRight);
        tablesOperator = new TablesOperator(vboxLeft, vboxRight);
        algorithmsOperator = new AlgorithmsOperator(dropListLeft, dropListRight);

        putKey(algorithmsOperator.hash("key1", true, tableSize), "key1", true);
        putKey(algorithmsOperator.hash("key2", false, tableSize), "key2", false);

        scroll.vvalueProperty().addListener((observable, oldValue, newValue) -> {
            linesOperator.drawLineEvent(true, currIndexLeft);
            linesOperator.drawLineEvent(false, currIndexRight);
            linesOperator.drawHelperLine(currSideHelper, currIndexHelper);
        });
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
        resizedTimes++;
        // Requesting focus in separate Thread because at the time of initialize() controls are not yet ready to handle focus.
        //Platform.runLater(() -> inputField.requestFocus());
        inputChange();
    }

    public String putKey(int index, String key, boolean sideLeft) {
        String oldKey = sideLeft ? T1.get(index) : T2.get(index);
        if (sideLeft) {
            T1.set(index, key);
            Platform.runLater(() -> ((Label) vboxLeft.getChildren().get(index)).setText(key));

        } else {
            T2.set(index, key);
            Platform.runLater(() -> ((Label) vboxRight.getChildren().get(index)).setText(key));
        }
        return oldKey;
    }

    public boolean removeKey(String key) {

        refreshBackground();
        if (T1.get(foundIndex).equals(key)) {
            ((Label) vboxLeft.getChildren().get(foundIndex)).setText("");
            //String res = T1.get(x1);
            T1.set(foundIndex, "");
            return true;
        }
        if (T2.get(foundIndex).equals(key)) {
            ((Label) vboxRight.getChildren().get(foundIndex)).setText("");
            //String res = T2.get(x2);
            T2.set(foundIndex, "");
            return true;
        }
        return false;
    }

    // process change in input box
    public void inputChange() {

        // ovo nam treba kada dodje do loopa i onda pozovemo inputField set text treba vremena u platformu, i kada se pozove ovo
        //ta set text u platgormu nije izvrsena
        // boolean useInputField = true;
        //if (useInputField)
        input = inputField.getText();

        if (input.length() > 0) {
            int x1 = algorithmsOperator.hash(input, true, tableSize);
            int x2 = algorithmsOperator.hash(input, false, tableSize);

            currIndexLeft = x1;
            currIndexRight = x2;

            refreshBackground();

            linesOperator.drawLine(true, x1);
            linesOperator.drawLine(false, x2);

            Platform.runLater(() -> inputField.requestFocus());

            tablesOperator.highlight(true, x1, (T1.get(x1).equals(input)));
            tablesOperator.highlight(false, x2, (T2.get(x2).equals(input)));

            // ide se redom i ako ga je nasla prva linija druga ne trazi dalje ifound je ispravan
            found = (T1.get(x1).equals(input));
            if (found) {
                foundIndex = x1;
            } else {
                found = (T2.get(x2).equals(input));
                if (found) {
                    foundIndex = x2;
                }
            }
            // ako ni leva ni desna linija nije nasla onda cemo uz pomoc helper da nadjemo
            if (!found) {
                int base = baseTableSize;
                for (int i = 0; i < resizedTimes; i++) {

                    x1 = algorithmsOperator.hash(input, true, base);
                    x2 = algorithmsOperator.hash(input, false, base);
                    found = (T1.get(x1).equals(input));
                    if (found) {
                        linesOperator.drawHelperLine(true, x1);
                        tablesOperator.highlight(true, x1, true);
                        currIndexHelper = x1;
                        currSideHelper = true;
                        foundIndex = x1;
                        break;
                    }
                    found = (T2.get(x2).equals(input));
                    if (found) {
                        linesOperator.drawHelperLine(false, x2);
                        tablesOperator.highlight(false, x2, true);
                        currIndexHelper = x2;
                        currSideHelper = false;
                        foundIndex = x2;
                        break;
                    }

                    base *= 2;
                }
            }
        } else {
            //document.getElementById("message").innerHTML = "";
            currIndexRight = -1;
            currIndexLeft = -1;
            currIndexHelper = -1;
            refreshBackground();
        }
        Platform.runLater(() -> inputField.requestFocus());
    }

    private void refreshBackground(boolean left) {
        // we are removing helper line, if we have need for one we will draw it
        currIndexHelper = -1;
        linesOperator.resetLines(left);
        tablesOperator.refreshTables(left);
        Platform.runLater(() -> inputField.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5.0), null))));
    }

    private void refreshBackground() {
        // removing lines so we don`t draw it while scrolling
        currIndexHelper = -1;
        linesOperator.resetLines();
        tablesOperator.refreshTables();
        Platform.runLater(() -> inputField.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5.0), null))));
    }

    public void delete() {
        //input = inputField.getText();
        inputChange();
        if (!found) {
            Platform.runLater(() -> inputField.setBackground(new Background(new BackgroundFill(Color.ORANGERED, new CornerRadii(5.0), null))));
            return;
        }
        if (input.length() > 0) {
            // This must be done this way because
            // we want to call inputChange() after deletion and also to change background of input field
            boolean keyRemoved = removeKey(input);
            // ne mora, moze i samo da se promeni pozadina
            inputChange();
            if (keyRemoved) {
                Platform.runLater(() -> inputField.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, new CornerRadii(5.0), null))));
            } else {
                Platform.runLater(() -> inputField.setBackground(new Background(new BackgroundFill(Color.ORANGERED, new CornerRadii(5.0), null))));
            }
        }

    }

    public void add() {
        //ne mora
        inputChange();
        //mainPane.setDisable(true);
        buttonBar.setDisable(true);
        inputField.setEditable(false);
        new Thread(() -> {

            String curr = inputField.getText();
            if (found) {
                Platform.runLater(() -> {
                    inputField.setBackground(new Background(new BackgroundFill(Color.ORANGERED, new CornerRadii(5.0), null)));
                    inputField.requestFocus();
                    inputField.selectEnd();
                });
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
                    new Thread(() -> {
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        inputChange();
                        Platform.runLater(() -> inputField.setBackground(new Background(new BackgroundFill(Color.ORANGERED, new CornerRadii(5.0), null))));
                    }).start();

                }
                buttonBar.setDisable(false);
                inputField.setEditable(true);
                Platform.runLater(() -> {
                    inputField.requestFocus();
                    inputField.selectEnd();
                });
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
            Platform.runLater(() -> inputField.setText(key));
            return false;
        }
        int index = algorithmsOperator.hash(key, true, tableSize);
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
            Platform.runLater(() -> inputField.setText(key));
            return false;
        }
        //ovde treba da se hajlajtuje desno polje i da postoji desna strelica a ostalo da se disejbluje
        int index = algorithmsOperator.hash(key, false, tableSize);
        String s = putKey(index, key, false);
        stepByStep(false, s, index);
        if (!s.equals("")) {
            return addLeft(s, i - 1);
        }
        return true;
    }


    public void expand(ActionEvent actionEvent) {
        expandTables(2);
    }

    public void changeStepByStep(ActionEvent actionEvent) {
        stepByStep = stepByStepButton.isSelected();
    }
}
