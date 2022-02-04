package kitakkun.wordle.system;

public class Settings {
    private int wordLength; // 単語長
    private int attemptLimit; // 試行回数制限
    private Dictionary dictionary; // 辞書
    private Dictionary answerDictionary; // 答え用辞書
    private boolean permitShorterWords; // 短い単語入力を許可するか
    private boolean isDarkTheme;    // ダークテーマを用いるかどうか

    public Settings() {
        wordLength = 5;
        attemptLimit = 6;
        dictionary = new Dictionary();
        answerDictionary = new Dictionary();
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

}
