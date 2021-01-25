package sample;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

public class LinesOperator {

    private final VBox vboxLeft;
    private final VBox vboxRight;
    public Line leftLine;
    public Line rightLine;
    public Line helperLine;
    public TextField inputField;
    private boolean lockedLeft = true;
    private boolean lockedRight = true;
    private boolean lockedHelper = true;

    public LinesOperator(TextField inputField, Line leftLine, Line rightLine, Line helperLine, VBox vboxLeft, VBox vboxRight) {
        this.vboxLeft = vboxLeft;
        this.vboxRight = vboxRight;
        this.leftLine = leftLine;
        this.rightLine = rightLine;
        this.inputField = inputField;
        this.helperLine = helperLine;

        Platform.runLater(() -> {
                    leftLine.setStartX(inputField.getLayoutX() + 5);
                    leftLine.setStartY(inputField.getLayoutY() + inputField.getPrefHeight() / 2);
                    leftLine.setEndX(inputField.getLayoutX() + 5);
                    leftLine.setEndY(inputField.getLayoutY() + inputField.getPrefHeight() / 2);

                    rightLine.setStartX(inputField.getLayoutX() + inputField.getPrefWidth() - 5);
                    rightLine.setStartY(inputField.getLayoutY() + inputField.getPrefHeight() / 2);
                    rightLine.setEndX(inputField.getLayoutX() + inputField.getPrefWidth() - 5);
                    rightLine.setEndY(inputField.getLayoutY() + inputField.getPrefHeight() / 2);

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

    public void drawHelperLineEvent(boolean left, int index) {
        if (lockedHelper) return;
        drawHelperLine(left, index);
    }

    public void drawLine(boolean left, int index) {
        if (index < 0) return;

        Platform.runLater(() -> {

            if (left) {
                Bounds b = vboxLeft.localToScreen((vboxLeft.getChildren().get(0)).getBoundsInLocal());
                b = leftLine.screenToLocal(b);

                leftLine.setStartX(b.getCenterX() + b.getWidth() / 2);
                leftLine.setStartY(b.getCenterY() + b.getHeight() * index);
                lockedLeft = false;
            } else {
                Bounds b = vboxRight.localToScreen((vboxRight.getChildren().get(0)).getBoundsInLocal());
                b = rightLine.screenToLocal(b);

                rightLine.setEndX(b.getCenterX() - b.getWidth() / 2);
                rightLine.setEndY(b.getCenterY() + b.getHeight() * index);
                lockedRight = false;
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
            lockedHelper = false;
        });
    }

    public void resetLines() {
        lockedLeft = true;
        lockedRight = true;
        lockedHelper = true;

        Platform.runLater(() -> {

            leftLine.setStartX(leftLine.getEndX());
            leftLine.setStartY(leftLine.getEndY());

            rightLine.setEndX(rightLine.getStartX());
            rightLine.setEndY(rightLine.getStartY());

            helperLine.setStartX(helperLine.getEndX());
            helperLine.setStartY(helperLine.getEndY());
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
            lockedHelper = true;
            helperLine.setStartX(helperLine.getEndX());
            helperLine.setStartY(helperLine.getEndY());
        });
    }
}
