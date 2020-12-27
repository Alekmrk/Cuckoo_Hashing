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
                //ako nema prva rec znaci da je ostao string space, tab ili new row
                words.setLength(0);
                textArea.setText("");
                return null;
            }
            try {
                // [rva rec postoji, treba da nadjemo do koliko treba da skratimo string
                String drugaRec = wordTokens.nextToken();
                // brisemo od nula do druge reci, moramo da stavimo ovo ->words.indexOf(prvaRec) + 1) ako se desi da su dve reci za redom iste
                words.delete(0, words.indexOf(drugaRec, words.indexOf(prvaRec) + 1));
                textArea.setText(words.toString());
            } catch (NoSuchElementException e) {
                // nema druge reci, znaci skracujemo sve
                words.setLength(0);
                textArea.setText("");
            }
            //vracamo pronadjenu rec
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
