package kitakkun.wordle.view;

import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import kitakkun.wordle.system.Dictionary;

import java.net.Authenticator;

public class DictionarySearchView extends VBox {

    private TextField searchTextField;
    private ListView<String> wordListView;
    private Dictionary dictionary;

    public DictionarySearchView() {
        initialize();
    }

    public void initialize() {
        wordListView = new ListView<>();
        searchTextField = new TextField();
        searchTextField.setPromptText("Enter a word to search.");
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (dictionary == null) {
                System.out.println("Dictionary hasn't been set yet.");
            } else {
                String[] words = dictionary.search(newValue);
                updateWordList(words);
            }
        });
        wordListView.setFocusTraversable(false);
        this.getChildren().addAll(searchTextField, wordListView);
    }

    private void updateWordList(String[] words) {
        wordListView.getItems().clear();
        for (String word : words) {
            wordListView.getItems().add(word);
        }
    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }
}
