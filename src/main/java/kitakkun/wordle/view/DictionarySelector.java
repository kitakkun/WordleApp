package kitakkun.wordle.view;

import javafx.scene.control.ComboBox;
import kitakkun.wordle.system.Dictionary;

import java.util.ArrayList;
import java.util.HashMap;

public class DictionarySelector extends ComboBox<String> {

    private HashMap<String, Dictionary> dictionaries;
    private ArrayList<String> keys;

    public DictionarySelector() {
        dictionaries = new HashMap<>();
        keys = new ArrayList<>();
        getItems().add("default");
        getItems().add("load files...");
        Dictionary dictionary = new Dictionary();
        dictionaries.put("default", dictionary);
    }
}
