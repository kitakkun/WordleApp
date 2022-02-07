package kitakkun.wordle.system;

import java.util.ArrayList;
import java.util.HashMap;

public class Settings {
    private int wordLength; // 単語長
    private int attemptLimit; // 試行回数制限
    private Dictionary dictionary; // 辞書
    private Dictionary answerDictionary; // 答え用辞書
    private boolean permitShorterWords; // 短い単語入力を許可するか
    private boolean isDarkTheme;    // ダークテーマを用いるかどうか

    private HashMap<String, Dictionary> dictionaries;

    public Settings() {
        wordLength = 5;
        attemptLimit = 6;
        dictionaries = new HashMap<>();
        dictionaries.put("default", new Dictionary());
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

    public String[] getDictionaryKeys() {
        return dictionaries.keySet().toArray(new String[0]);
    }
}
