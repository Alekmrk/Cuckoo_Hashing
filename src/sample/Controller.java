package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Canvas canvas;
    @FXML
    private VBox sideRight;
    @FXML
    private VBox sideLeft;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    public void mouseClicked(MouseEvent mouseEvent) {
        System.out.println("Clicked on Canvas");
    }
}
