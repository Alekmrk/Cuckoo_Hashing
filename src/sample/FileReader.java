package sample;

import java.util.ArrayList;

public class FileReader {

    ArrayList<String> wordList = new ArrayList<>();

    public String nextWord() {
        if (wordList.isEmpty()) {
            return null;
        }else {
            return wordList.remove(0);
        }
    }

}
