package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {


    public Button button2;
    public Label label2;
    @FXML
    private Label label1;
    @FXML
    private Button button1;

    public void buttonClck(ActionEvent actionEvent) {
        label1.setText("Ovo je test");
    }

    public void buttonClick2(ActionEvent actionEvent) {
        label2.setText("Ovo je test2");
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


}
