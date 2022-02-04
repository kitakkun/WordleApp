package kitakkun.wordle.view;

import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchResultView extends VBox {

    private ArrayList<String> words;
    private HashMap<String, String> meanings;
    private GridPane table;

    public SearchResultView() {
        initialize();
    }

    public void initialize() {
        words = new ArrayList<>();
        meanings = new HashMap<>();
        table = new GridPane();
        this.getChildren().add(table);
    }

    /**
     * 単語を追加します．
     * @param word
     * @param meaning
     */
    public void addWord(String word, String meaning) {
        words.add(word);
        meanings.put(word, meaning);
    }

    /**
     * ビューを構成します．
     */
    public void constructView() {
        for (int i = 0; i < words.size(); i++) {
            String word = words.get(i);
            String meaning = meanings.get(word);
            Label wordLabel = new Label(word);
            Label meaningLabel = new Label(meaning);
            meaningLabel.setWrapText(true);
            table.addRow(i, wordLabel, meaningLabel);
        }
        table.getColumnConstraints().add(new ColumnConstraints(80));
        table.getColumnConstraints().add(new ColumnConstraints(300));
    }
}
