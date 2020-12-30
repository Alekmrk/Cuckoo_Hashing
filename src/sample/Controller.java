package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Controller implements Initializable {
    @FXML
    private Group textAreaButtonsGroup;
    @FXML
    private Button tableSizeButton;
    @FXML
    private Button addButton;
    @FXML
    private Label messageLabel;
    @FXML
    private Group algorithmLockGroup;
    @FXML
    private TextField tableSizeField;
    @FXML
    private TextArea textArea;
    @FXML
    private Group stepByStepGroup;
    @FXML
    private Slider speedSlider;
    @FXML
    private Group actionButtonsGroup;
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

    private boolean stepByStep = false;
    private long maxSpeed = 3000;
    private long currSpeed = 1000;

    private boolean algorithmLocked = false;
    private ActionState actionState = ActionState.SUCCESSFUL_ADD;

    private LinesOperator linesOperator;
    private TablesOperator tablesOperator;
    private AlgorithmsOperator algorithmsOperator;
    private FileReader fileReader;
    private boolean tableSizeValid = true;
    private boolean loopFromFile = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // ne znam da li treba ovo ili ne, ali kao za svaki slucaj
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        linesOperator = new LinesOperator(inputField, leftLine, rightLine, helperLine, vboxLeft, vboxRight);
        tablesOperator = new TablesOperator(vboxLeft, vboxRight);
        algorithmsOperator = new AlgorithmsOperator(dropListLeft, dropListRight);
        fileReader = new FileReader(textArea);
        Platform.runLater(() ->
                tableSizeField.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, new CornerRadii(5.0), null)))
        );
        for (int i = 0; i < tableSize; i++) {
            T1.add("");
            T2.add("");
        }
        tablesOperator.expandTables(tableSize);

        setListeners();
        //putKey(algorithmsOperator.hash("key1", true, tableSize), "key1", true);
        //putKey(algorithmsOperator.hash("key2", false, tableSize), "key2", false);
    }

    private void setListeners() {
        scroll.vvalueProperty().addListener((observable, oldValue, newValue) -> {
            linesOperator.drawLineEvent(true, currIndexLeft);
            linesOperator.drawLineEvent(false, currIndexRight);
            linesOperator.drawHelperLineEvent(currSideHelper, currIndexHelper);
        });

        speedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.longValue() == 0) {
                currSpeed = 20;
            } else {
                currSpeed = maxSpeed * newValue.longValue() / 100;
            }
        });

        tableSizeField.textProperty().addListener((observable, oldValue, newValue) -> {

            String number = newValue.replaceAll("[^\\d]", "");
            if (number.equals("")) {
                Platform.runLater(() -> {
                    tableSizeField.setBackground(new Background(new BackgroundFill(Color.ORANGERED, new CornerRadii(5.0), null)));
                    tableSizeField.setText("");
                });
                tableSizeValid = false;
                return;
            }
            try {
                baseTableSize = Integer.parseInt(number);
                Platform.runLater(() ->
                        tableSizeField.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, new CornerRadii(5.0), null)))
                );
                baseTableSize = Math.min(baseTableSize, 1000);
                baseTableSize = Math.max(baseTableSize, 1);
                Platform.runLater(() -> tableSizeField.setText("" + baseTableSize));
                Platform.runLater(() -> tableSizeField.end());
                tableSizeValid = true;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                //ispisi negde gresku
            }

        });
        // swapping focus between addButton and tableSizeButton
        tableSizeField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDefaultButton(!newValue);
            tableSizeButton.setDefaultButton(newValue);
        });
    }

    private void setToDefault() {

        tableSize = baseTableSize;
        tableSizeValid = true;
        resizedTimes = 0;

        loopFromFile = false;
        stepByStep = false;

        found = false;
        foundIndex = -1;

        T1 = new ArrayList<>();
        T2 = new ArrayList<>();

        Platform.runLater(() -> {
            input = "";
            inputField.setText("");
            //inputField.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5.0), null)));
            inputField.requestFocus();
        });
        showMessage(ActionState.PROCESSING);
        tablesOperator.resetTables();
        linesOperator.resetLines();
        tablesOperator.expandTables(tableSize);

        for (int i = 0; i < tableSize; i++) {
            T1.add("");
            T2.add("");
        }
    }

    public void expandTables(double times) {

        for (int i = tableSize; i < tableSize * times; i++) {
            T1.add("");
            T2.add("");
        }

        tablesOperator.expandTables((int) (tableSize * times - tableSize));
        tableSize *= times;
        resizedTimes++;
        // Requesting focus in separate Thread because at the time of initialize() controls are not yet ready to handle focus.
        //Platform.runLater(() -> inputField.requestFocus());
        inputChange(true);
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
    public void inputChange(boolean useInputField) {

        // ovo nam treba kada dodje do loopa i onda pozovemo inputField set text treba vremena u platformu, i kada se pozove ovo
        //ta set text u platformu nije izvrsena
        if (useInputField) {
            input = inputField.getText();
        }
        showMessage(ActionState.PROCESSING);
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
        ensureVisible(false);
    }

    private void refreshBackground(boolean left) {
        // we are removing helper line, if we have need for one we will draw it
        //currIndexHelper = -1;
        linesOperator.resetLines(left);
        tablesOperator.refreshTables(left);
        //Platform.runLater(() -> inputField.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5.0), null))));
    }

    private void refreshBackground() {
        // removing lines so we don`t draw it while scrolling
        //currIndexHelper = -1;
        linesOperator.resetLines();
        tablesOperator.refreshTables();
        showMessage(ActionState.PROCESSING);
        //Platform.runLater(() -> inputField.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5.0), null))));
    }

    private void buttonGroupDisable(boolean disable) {
        if (loopFromFile) {
            return;
        }
        actionButtonsGroup.setDisable(disable);
        textAreaButtonsGroup.setDisable(disable);
    }

    public void add(boolean waitForThread) {
        //ne mora
        inputChange(true);
        buttonGroupDisable(true);
        inputField.setEditable(false);
        actionState = ActionState.PROCESSING;
        Thread t = new Thread(() -> {
            // moze se promeniti na input
            String curr = inputField.getText();
            if (found) {
                Platform.runLater(() -> {
                    //inputField.setBackground(new Background(new BackgroundFill(Color.ORANGERED, new CornerRadii(5.0), null)));
                    inputField.selectEnd();
                    inputField.requestFocus();
                });
                buttonGroupDisable(false);
                inputField.setEditable(true);
                showMessage(ActionState.ALREADY_EXISTS);

                try {
                    if (stepByStep) {
                        Thread.sleep(currSpeed / 2);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return;
            }
            Platform.runLater(() -> inputField.requestFocus());

            //refreshBackground();
            if (input.length() > 0) {
                //mora da se promeni da ne bude 16 nego u zavisnosti od velicine tabele
                if (addLeft(input, 16)) {
                    // ovde baca gresku pri dodavanju, razresi to obaveznoo. index je nekada -1. vise ne valjda
                    if (T1.get(currIndexLeft).equals(curr)) {
                        tablesOperator.highlight(true, currIndexLeft, true);
                        linesOperator.drawLine(true, currIndexLeft);
                        foundIndex = currIndexLeft;
                        refreshBackground(false);
                    } else {
                        tablesOperator.highlight(false, currIndexRight, true);
                        linesOperator.drawLine(true, currIndexRight);
                        foundIndex = currIndexRight;
                        refreshBackground(true);
                    }
                    // vidi da li treba ovde ili dole
                    //input = "";
                    Platform.runLater(() -> {
                        input = "";
                        inputField.setText("");
                        //inputField.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, new CornerRadii(5.0), null)));
                    });
                    // must call it via runLater so we make sure that background is refreshed
                    Platform.runLater(() -> ensureVisible(true));
                    showMessage(ActionState.SUCCESSFUL_ADD);
                } else {
                    //neuspesno dodavanje
                    inputChange(false);
                    //Platform.runLater(() -> inputField.setBackground(new Background(new BackgroundFill(Color.ORANGERED, new CornerRadii(5.0), null))));
                    showMessage(ActionState.INFINITE_LOOP);
                }
                buttonGroupDisable(false);
                inputField.setEditable(true);
                Platform.runLater(() -> {
                    inputField.requestFocus();
                    inputField.selectEnd();
                });

                try {
                    if (stepByStep) {
                        Thread.sleep(currSpeed / 2);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return;
            }
            inputChange(true);
            //Platform.runLater(() -> inputField.setBackground(new Background(new BackgroundFill(Color.ORANGERED, new CornerRadii(5.0), null))));
            showMessage(ActionState.EMPTY_INPUT);
            buttonGroupDisable(false);
            inputField.setEditable(true);

            try {
                if (stepByStep) {
                    Thread.sleep(currSpeed / 2);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return;
        });

        t.start();
        if (waitForThread) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean addLeft(String key, int i) {
        if (i < 0) {
            input = key;
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
            input = key;
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

    private void stepByStep(boolean left, String s, int index) {
        if (!stepByStep) {
            return;
        }
        foundIndex = index;
        ensureVisible(true);
        Platform.runLater(() ->
        {
            refreshBackground(!left);
            inputField.setText(s);
            linesOperator.drawLine(left, index);
            tablesOperator.highlight(left, index, Color.YELLOW);
        });
        try {
            Thread.sleep(currSpeed);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean ensureVisible(boolean explicitRequest) {
        if (!explicitRequest) {
            if (!found) {
                return false;
            }
        }
        double height = scroll.getContent().getBoundsInLocal().getHeight();
        Node node = vboxLeft.getChildren().get(foundIndex);
        double y = node.getBoundsInParent().getMaxY();
        // height of label is 37 by default
        double delta = 37;

        if (y / height > 0.5) {
            delta = -delta;
        }
        // scrolling values range from 0 to 1
        scroll.setVvalue((y - delta) / height);
        Platform.runLater(() -> {
            inputField.requestFocus();
            inputField.end();
        });
        return true;
    }

    private void showMessage(ActionState action) {
        actionState = action;
        switch (actionState) {
            case PROCESSING:
                Platform.runLater(() -> {
                    messageLabel.setText("");
                    messageLabel.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, new CornerRadii(5.0), null)));
                });
                break;
            case SUCCESSFUL_ADD:
                Platform.runLater(() -> {
                    messageLabel.setText("Successfully added!");
                    messageLabel.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, new CornerRadii(5.0), null)));
                });
                break;
            case ALREADY_EXISTS:
                Platform.runLater(() -> {
                    messageLabel.setText("Already exists!");
                    messageLabel.setBackground(new Background(new BackgroundFill(Color.ORANGERED, new CornerRadii(5.0), null)));
                });
                break;
            case INFINITE_LOOP:
                Platform.runLater(() -> {
                    messageLabel.setText("Could not be added. Infinite loop occurred!\nPlease expand tables by clicking on button down below.");
                    messageLabel.setBackground(new Background(new BackgroundFill(Color.MISTYROSE, new CornerRadii(5.0), null)));
                });
                break;
            case EMPTY_INPUT:
                Platform.runLater(() -> {
                    messageLabel.setText("Input must not be empty!");
                    messageLabel.setBackground(new Background(new BackgroundFill(Color.ORANGERED, new CornerRadii(5.0), null)));
                });
                break;
            case SUCCESSFUL_DELETE:
                Platform.runLater(() -> {
                    messageLabel.setText("Successfully deleted!");
                    messageLabel.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, new CornerRadii(5.0), null)));
                });
                break;
            case UNSUCCESSFUL_DELETE:
                Platform.runLater(() -> {
                    messageLabel.setText("Unsuccessful deletion!\nThere is no such item!");
                    messageLabel.setBackground(new Background(new BackgroundFill(Color.ORANGERED, new CornerRadii(5.0), null)));
                });
                break;
            case SUCCESSFUL_FIND:
                Platform.runLater(() -> {
                    messageLabel.setText("Successfully found!");
                    messageLabel.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, new CornerRadii(5.0), null)));
                });
                break;
            case UNSUCCESSFUL_FIND:
                Platform.runLater(() -> {
                    messageLabel.setText("Unsuccessful find!\nThere is no such item!");
                    messageLabel.setBackground(new Background(new BackgroundFill(Color.ORANGERED, new CornerRadii(5.0), null)));
                });
                break;
            case EXPANDING_TABLES:
                Platform.runLater(() -> {
                    messageLabel.setText("Tables are expanded to size: " + tableSize);
                    messageLabel.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, new CornerRadii(5.0), null)));
                });
                break;
            default:
                break;
        }
    }

    private boolean lockAlgorithmsChange() {
        // ovo sluzi da proveri funkcije hesiranja i nista drugo
        if (!algorithmLocked) {
            if (algorithmsOperator.areSame()) {
                Alert alert = new Alert(
                        Alert.AlertType.WARNING,
                        "Hashing functions for both tables are the same.\nAre you sure you want to continue?",
                        ButtonType.YES, ButtonType.NO);
                alert.setTitle("Same hashing functions warning");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.orElse(null) == ButtonType.NO) {
                    return false;
                }
            }
            dropListLeft.setDisable(true);
            dropListRight.setDisable(true);
            algorithmLockGroup.setDisable(true);
            algorithmLocked = true;

        }
        return true;
    }

    public void loadTextAction() {

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Text files", "*.txt");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile == null) {
            // ispisi nesto
        } else {
            try {
                Scanner sc = new Scanner(selectedFile);
                // we just need to use \\Z as delimiter
                sc.useDelimiter("\\Z");
                Platform.runLater(() -> textArea.setText(sc.next()));
            } catch (Exception e) {
                //e.printStackTrace();
                // do nothing
            }
        }
    }

    public void expandAction(ActionEvent actionEvent) {
        expandTables(2);
        showMessage(ActionState.EXPANDING_TABLES);
        Platform.runLater(() -> inputField.end());
    }

    public void changeStepByStepAction(ActionEvent actionEvent) {
        stepByStep = stepByStepButton.isSelected();
        //stepByStepGroup.setVisible(true);
        //stepByStepGroup.setVisible(false);
        stepByStepGroup.setDisable(!stepByStep);
    }

    public void resetAction(ActionEvent actionEvent) {
        // i proveriti da neko ne stisne slucajno
        baseTableSize = 15;
        Platform.runLater(() -> {
                    tableSizeField.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, new CornerRadii(5.0), null)));
                    tableSizeField.setText("" + baseTableSize);
                }
        );
        setToDefault();
        actionButtonsGroup.setDisable(false);
        inputField.setEditable(true);
        dropListLeft.setDisable(false);
        dropListRight.setDisable(false);
        algorithmLockGroup.setDisable(false);
        algorithmLocked = false;
        Platform.runLater(this::refreshBackground);
        //postaviti da moze da se menja size i da mogu algoritmi da se biraju
    }

    public void inputChangeAction(KeyEvent keyEvent) {
        inputChange(true);
    }

    public void addAction(ActionEvent actionEvent) {
        if (!lockAlgorithmsChange()) {
            return;
        }
        add(false);
    }

    public void deleteAction() {
        //input = inputField.getText();
        inputChange(true);
        if (!found) {
            //Platform.runLater(() -> inputField.setBackground(new Background(new BackgroundFill(Color.ORANGERED, new CornerRadii(5.0), null))));
            showMessage(ActionState.UNSUCCESSFUL_DELETE);
            return;
        }
        if (input.length() > 0) {
            // This must be done this way because
            // we want to call inputChange() after deletion and also to change background of input field
            boolean keyRemoved = removeKey(input);
            // ne mora, moze i samo da se promeni pozadina
            inputChange(true);
            if (keyRemoved) {
                //Platform.runLater(() -> inputField.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, new CornerRadii(5.0), null))));
                showMessage(ActionState.SUCCESSFUL_DELETE);
            } else {
                //Platform.runLater(() -> inputField.setBackground(new Background(new BackgroundFill(Color.ORANGERED, new CornerRadii(5.0), null))));
                showMessage(ActionState.UNSUCCESSFUL_DELETE);
            }
        }
    }

    public boolean nextWordAction(ActionEvent actionEvent) {
        if (input.length() > 0) {
            if (actionEvent != null) {
                Alert alert = new Alert(
                        Alert.AlertType.WARNING,
                        "There is already a text in input field.\nThis action will change text in it!\nAre you sure you want to continue?",
                        ButtonType.YES, ButtonType.NO);
                alert.setTitle("Putting text in occupied text field");

                Optional<ButtonType> result = alert.showAndWait();

                if (result.orElse(null) == ButtonType.NO) {
                    return false;
                }
            }
        }
        String newWord = fileReader.nextWord();
        if (newWord != null) {
            Platform.runLater(() -> {
                // ispitati ako se vec nalazi u input polju da li zelimo da idemo nextWord
                inputField.setText(newWord);
                inputChange(true);
            });
            return true;
        }
        return false;
    }

    public void tableSizeAction(ActionEvent actionEvent) {
        if (tableSizeValid) {
            setToDefault();
        } else {
            // vrv alert koji mu kaze da ne valja
        }
    }

    public void findAction(ActionEvent actionEvent) {
        if (!ensureVisible(false)) {
            if (input.equals("")) {
                showMessage(ActionState.EMPTY_INPUT);
                return;
            }
            showMessage(ActionState.UNSUCCESSFUL_FIND);
        } else {
            showMessage(ActionState.SUCCESSFUL_FIND);
        }
    }

    public void startStopAction(ActionEvent actionEvent) {
        // vidi sta treba da se disable-uje
        if (!lockAlgorithmsChange()) {
            return;
        }
        loopFromFile = !loopFromFile;
        if (loopFromFile) {
            new Thread(() -> {
                actionButtonsGroup.setDisable(true);
                textAreaButtonsGroup.setDisable(true);
                while (loopFromFile) {
                    try {
                        if (nextWordAction(null)) {

                            Thread.sleep(100);
                            add(true);

                            // wait for add thread to finish
                            // ispravi ovo da bude dobro
                            if (actionState == ActionState.INFINITE_LOOP) {
                                int i = 3;
                                while (actionState == ActionState.INFINITE_LOOP && i > 0) {
                                    Thread.sleep(2000);
                                    expandTables(2);
                                    showMessage(ActionState.EXPANDING_TABLES);
                                    // prosiri i pozove add opet
                                    Thread.sleep(2000);
                                    add(true);
                                    i--;
                                }
                                if (i == 0) {
                                    loopFromFile = false;
                                    // ispisi da je doslo do greske, doslo je do loopa koji ne moze da se resi
                                }
                            }
                        } else {
                            loopFromFile = false;
                        }
                        Thread.sleep(100);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                actionButtonsGroup.setDisable(false);
                textAreaButtonsGroup.setDisable(false);
            }).start();
        }
    }

    public void clearTextArea(ActionEvent actionEvent) {
        fileReader.clearAll();
    }

    public void nextWordAddAction(ActionEvent actionEvent) {
        if (!lockAlgorithmsChange()) {
            return;
        }
        new Thread(() -> {
            if (nextWordAction(null)) {
                try {
                    Thread.sleep(100);
                    add(false);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void exportFileAction(ActionEvent actionEvent) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\n");
        stringBuilder.append("\"StartingTableSize\" : \"").append(baseTableSize).append("\",\n");
        stringBuilder.append("\"TableSizeMultiplier\" : \"2\",\n");
        stringBuilder.append("\"LeftHashFunction\" : \"").append(algorithmsOperator.getLeftAlgorithm()).append("\",\n");
        stringBuilder.append("\"RightHashFunction\" : \"").append(algorithmsOperator.getRightAlgorithm()).append("\",\n");
        stringBuilder.append(tablesOperator.exportTables());
        stringBuilder.append("}\n");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text and JSON files", "*.json", "*.txt"));
        File selectedFile = fileChooser.showSaveDialog(null);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile));
            writer.write(stringBuilder.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
