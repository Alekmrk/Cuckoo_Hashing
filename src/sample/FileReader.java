package sample;

import javafx.scene.control.TextArea;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class FileReader {
    private TextArea textArea;
    private boolean changed = false;
    StringBuilder words = new StringBuilder();


    public FileReader(TextArea textArea) {
        this.textArea = textArea;
        textArea.textProperty().addListener((observableValue, s, t1) -> changed = true);

    }

    public String nextWord() {
        reloadText();
        if (words.length() == 0) {
            return null;
        } else {
            StringTokenizer wordTokens = new StringTokenizer(textArea.getText());
            String prvaRec;
            try {
                prvaRec = wordTokens.nextToken();
            } catch (NoSuchElementException e) {
                words.setLength(0);
                textArea.setText("");
                return null;
            }
            try {
                String drugaRec = wordTokens.nextToken();
                words.delete(0, words.indexOf(drugaRec, words.indexOf(prvaRec) + 1));
                textArea.setText(words.toString());
            } catch (NoSuchElementException e) {
                words.setLength(0);
                textArea.setText("");
            }
            return prvaRec;
        }
    }

    private void reloadText() {
        if (!changed) return;
        words = new StringBuilder(textArea.getText());
        changed = false;
    }

    public void clearAll() {
        textArea.setText("");
        changed = false;
        words = new StringBuilder();
    }
}
