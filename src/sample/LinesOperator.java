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
    public Line helperLine;
    public TextField inputField;
    private boolean lockedLeft = true;
    private boolean lockedRight = true;

    public LinesOperator(TextField inputField, Line leftLine, Line rightLine, Line helperLine, VBox vboxLeft, VBox vboxRight) {
        this.vboxLeft = vboxLeft;
        this.vboxRight = vboxRight;
        this.leftLine = leftLine;
        this.rightLine = rightLine;
        this.inputField = inputField;
        this.helperLine = helperLine;

        Platform.runLater(() -> {
                    leftLine.setStartX(inputField.getLayoutX());
                    leftLine.setStartY(inputField.getLayoutY());
                    leftLine.setEndX(inputField.getLayoutX());
                    leftLine.setEndY(inputField.getLayoutY() + inputField.getPrefHeight());

                    rightLine.setStartX(inputField.getLayoutX() + inputField.getPrefWidth());
                    rightLine.setStartY(inputField.getLayoutY());
                    rightLine.setEndX(inputField.getLayoutX() + inputField.getPrefWidth());
                    rightLine.setEndY(inputField.getLayoutY() + inputField.getPrefHeight());

                    helperLine.setStartX(inputField.getLayoutX() + inputField.getPrefWidth() / 2);
                    helperLine.setStartY(inputField.getLayoutY());
                    helperLine.setEndX(inputField.getLayoutX() + inputField.getPrefWidth() / 2);
                    helperLine.setEndY(inputField.getLayoutY());
                    helperLine.getStrokeDashArray().addAll(25d, 20d, 5d, 20d);

                    inputField.requestFocus();
                }
        );
    }

    public void drawLineEvent(boolean left, int index) {
        if (left && lockedLeft) return;
        if (!left && lockedRight) return;
        drawLine(left, index);

    }

    public void drawLine(boolean left, int index) {
        // stavim neku naznaku da ako je iz onog eventa da proverava jos neku vrednost
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
                // ne mora valjda ovo
                leftLine.setEndX(inputField.getLayoutX());
                leftLine.setEndY(inputField.getLayoutY() + inputField.getHeight() / 2);
                lockedLeft = false;
                //System.out.println(leftLine);
            } else {
                Bounds b = vboxRight.localToScreen((vboxRight.getChildren().get(0)).getBoundsInLocal());
                b = rightLine.screenToLocal(b);
                rightLine.setStartX(inputField.getLayoutX() + inputField.getWidth());
                rightLine.setStartY(inputField.getLayoutY() + inputField.getHeight() / 2);
                // ne mora valjda ovo jedino ako zabaguje nekad
                rightLine.setEndX(b.getCenterX() - b.getWidth() / 2);
                rightLine.setEndY(b.getCenterY() + b.getHeight() * index);
                lockedRight = false;
                //System.out.println(rightLine);
            }
        });
    }

    public void drawHelperLine(boolean left, int index) {
        if (index < 0) return;
        Platform.runLater(() -> {
            Bounds b = left ? vboxLeft.localToScreen((vboxLeft.getChildren().get(0)).getBoundsInLocal()) : vboxRight.localToScreen((vboxRight.getChildren().get(0)).getBoundsInLocal());
            b = helperLine.screenToLocal(b);
            if (left) {
                helperLine.setStartX(b.getCenterX() + b.getWidth() / 2);
            } else {
                helperLine.setStartX(b.getCenterX() - b.getWidth() / 2);
            }
            helperLine.setStartY(b.getCenterY() + b.getHeight() * index);
            helperLine.setEndX(inputField.getLayoutX() + inputField.getWidth() / 2);
            helperLine.setEndY(inputField.getLayoutY());
        });
    }

    public void resetLines() {
        Platform.runLater(() -> {
            leftLine.setStartX(leftLine.getEndX());
            leftLine.setStartY(leftLine.getEndY());

            rightLine.setEndX(rightLine.getStartX());
            rightLine.setEndY(rightLine.getStartY());

            helperLine.setStartX(helperLine.getEndX());
            helperLine.setStartY(helperLine.getEndY());
            lockedLeft = true;
            lockedRight = true;
        });
    }

    public void resetLines(boolean left) {
        Platform.runLater(() -> {
            if (left) {
                leftLine.setStartX(leftLine.getEndX());
                leftLine.setStartY(leftLine.getEndY());
                lockedLeft = true;
            } else {
                rightLine.setEndX(rightLine.getStartX());
                rightLine.setEndY(rightLine.getStartY());
                lockedRight = true;
            }
            helperLine.setStartX(helperLine.getEndX());
            helperLine.setStartY(helperLine.getEndY());
        });
    }


}
