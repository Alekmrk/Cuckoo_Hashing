package sample;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

public class LinesOperator {

    //private int currIndexLeft = 0;
    // private int currIndexRight = 0;
    private VBox vboxLeft;
    private VBox vboxRight;
    public Line leftLine;
    public Line rightLine;
    public TextField inputField;


    public LinesOperator(TextField inputField, Line leftLine, Line rightLine, VBox vboxLeft, VBox vboxRight) {
        this.vboxLeft = vboxLeft;
        this.vboxRight = vboxRight;
        this.leftLine = leftLine;
        this.rightLine = rightLine;
        this.inputField = inputField;

        Platform.runLater(() -> {
                    leftLine.setStartX(inputField.getLayoutX());
                    leftLine.setStartY(inputField.getLayoutY());
                    leftLine.setEndX(inputField.getLayoutX());
                    leftLine.setEndY(inputField.getLayoutY() + inputField.getPrefHeight());
                    rightLine.setStartX(inputField.getLayoutX() + inputField.getPrefWidth());
                    rightLine.setStartY(inputField.getLayoutY());
                    rightLine.setEndX(inputField.getLayoutX() + inputField.getPrefWidth());
                    rightLine.setEndY(inputField.getLayoutY() + inputField.getPrefHeight());
                    inputField.requestFocus();
                }
        );
    }

    public void drawLine(boolean left, int index) {//boolean left, int index
        //System.out.println(inputField.getB);
        if (index < 0) return;
       /* if ((left && currIndexLeft < 0) || (!left && currIndexRight < 0)) {
            //leftLine.setVisible(false);
            return;
        }*/
        Platform.runLater(() -> {

            if (left) {
                Bounds b = vboxLeft.localToScreen((vboxLeft.getChildren().get(0)).getBoundsInLocal());
                b = leftLine.screenToLocal(b);
                leftLine.setStartX(b.getCenterX() + b.getWidth() / 2);
                leftLine.setStartY(b.getCenterY() + b.getHeight() * index);
                leftLine.setEndX(inputField.getLayoutX());
                leftLine.setEndY(inputField.getLayoutY() + inputField.getHeight() / 2);
                //System.out.println(leftLine);
            } else {
                Bounds b = vboxRight.localToScreen((vboxRight.getChildren().get(0)).getBoundsInLocal());
                b = rightLine.screenToLocal(b);
                rightLine.setStartX(inputField.getLayoutX() + inputField.getWidth());
                rightLine.setStartY(inputField.getLayoutY() + inputField.getHeight() / 2);
                rightLine.setEndX(b.getCenterX() - b.getWidth() / 2);
                rightLine.setEndY(b.getCenterY() + b.getHeight() * index);
                //System.out.println(rightLine);
            }
        });
    }

    public void resetLines() {
        Platform.runLater(() -> {
            leftLine.setStartX(leftLine.getEndX());
            leftLine.setStartY(leftLine.getEndY());
            rightLine.setEndX(rightLine.getStartX());
            rightLine.setEndY(rightLine.getStartY());
        });
    }

    public void resetLines(boolean left) {
        Platform.runLater(() -> {
            if (left) {
                leftLine.setStartX(leftLine.getEndX());
                leftLine.setStartY(leftLine.getEndY());
            } else {
                rightLine.setEndX(rightLine.getStartX());
                rightLine.setEndY(rightLine.getStartY());
            }
        });
    }
}
