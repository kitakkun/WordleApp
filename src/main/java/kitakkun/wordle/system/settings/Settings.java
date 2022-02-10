package kitakkun.wordle.system.settings;

import kitakkun.wordle.system.Dictionary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.HashMap;

public class Settings {

    // 外部保存設定ファイルの場所
    public static final String FILE_PATH = "./settings.properties";

    private int wordLength; // 単語長
    private int attemptLimit; // 試行回数制限
    private Dictionary dictionary; // 辞書
    private Dictionary answerDictionary; // 答え用辞書
    private boolean permitShorterWords; // 短い単語入力を許可するか
    private boolean isDarkTheme;    // ダークテーマを用いるかどうか

    private final HashMap<String, Dictionary> dictionaries;
    private final HashMap<String, String> dictionaryPaths;

    public Settings() {
        wordLength = 5;
        attemptLimit = 6;
        dictionaries = new HashMap<>();
        dictionaries.put("default", new Dictionary());
        dictionaryPaths = new HashMap<>();
        dictionary = dictionaries.get("default");
        answerDictionary = dictionaries.get("default");
        permitShorterWords = false;
        isDarkTheme = false;
    }

    public int getWordLength() {
        return wordLength;
    }

    public void setWordLength(int wordLength) {
        this.wordLength = wordLength;
    }

    public int getAttemptLimit() {
        return attemptLimit;
    }

    public void setAttemptLimit(int attemptLimit) {
        this.attemptLimit = attemptLimit;
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public Dictionary getAnswerDictionary() {
        return answerDictionary;
    }

    public void setAnswerDictionary(Dictionary answerDictionary) {
        this.answerDictionary = answerDictionary;
    }

    public boolean permitShorterWords() {
        return permitShorterWords;
    }

    public void setPermitShorterWords(boolean permitShorterWords) {
        this.permitShorterWords = permitShorterWords;
    }

    public boolean isDarkTheme() {
        return isDarkTheme;
    }

    public void setDarkTheme(boolean darkTheme) {
        isDarkTheme = darkTheme;
    }

    public void addDictionary(String key, Dictionary dictionary) {
        dictionaries.put(key, dictionary);
    }

    public Dictionary getDictionaryByKey(String key) {
        return dictionaries.getOrDefault(key, null);
    }

    public String getKeyByDictionary(Dictionary dictionary) {
        for (String key : dictionaries.keySet()) {
            if (dictionaries.get(key).equals(dictionary)) {
                return key;
            }
        }
        return null;
    }

    public String[] getDictionaryKeys() {
        return dictionaries.keySet().toArray(new String[0]);
    }

    public void addDictionaryFile(File file) {
        dictionary = new Dictionary(file);
        String path = file.getAbsolutePath();
        String key = file.getName();
        key = key.substring(0, key.lastIndexOf('.'));
        key = resolveKeyDuplication(key);
        dictionaries.put(key, dictionary);
        dictionaryPaths.put(key, path);
    }

    private String resolveKeyDuplication(String key) {
        if (dictionaries.containsKey(key)) {
            int number = 1;
            while (!dictionaries.containsKey(key + number)) {
                number++;
            }
            return key + number;
        } else {
            return key;
        }
    }

    public String[] getDictionaryPaths() {
        return dictionaryPaths.values().toArray(new String[0]);
    }
}
