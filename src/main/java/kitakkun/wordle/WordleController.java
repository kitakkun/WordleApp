package kitakkun.wordle;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import kitakkun.wordle.system.Dictionary;
import kitakkun.wordle.system.Wordle;
import kitakkun.wordle.system.WordleState;
import kitakkun.wordle.view.KeyBoardView;
import kitakkun.wordle.view.WordInputView;
import kitakkun.wordle.view.WordView;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Objects;

public class WordleController {

    @FXML
    public Label messageBox;
    @FXML
    private WordInputView wiView;
    @FXML
    private KeyBoardView kbView;
    @FXML
    private WordView wordView;

    private Wordle wordle;
    // 辞書
    private Dictionary dictionary;
    // 正解候補単語の辞書
    private Dictionary answerDictionary;

    public void initialize() {
        dictionary = new Dictionary(Objects.requireNonNull(getClass().getResource("/words/words_alpha.txt")));
        answerDictionary = new Dictionary(Objects.requireNonNull(getClass().getResource("/words/words_alpha.txt")));
        wordle = new Wordle(answerDictionary.getRandomWord(5));
        wiView.ready(wordle, 10);
        wiView.setDictionary(dictionary);
        kbView.setKeyColor('Q', Color.WHITE);
    }

    public void onKeyPressed(KeyEvent event) {
        WordleState state = wiView.onKeyPressed(event);
        kbView.onKeyPressed(event);

        String message = "No message";
        switch (state) {
            case NOT_ON_DICTIONARY -> message = String.format("The word '%s' doesn't exist on our dictionary.", wiView.getCurrentWord());
            case FINISHED -> message = String.format("The answer is '%s'.", wiView.getAnswer());
        }
        messageBox.setText(message);
    }

    public void onKeyReleased(KeyEvent event) {
        kbView.onKeyReleased(event);
        for (char c : wordle.getNotExistLetters()) {
            kbView.setKeyColor(c, Color.GRAY);
        }
        for (char c : wordle.getExistLetters()) {
            kbView.setKeyColor(c, Color.YELLOW);
        }
        for (char c : wordle.getExactLetters()) {
            kbView.setKeyColor(c, Color.GREEN);
        }
    }

    @FXML
    protected void retryWordle() {
        wordle = new Wordle(answerDictionary.getRandomWord());
        wiView.ready(wordle, 10);
        messageBox.setText("Imagine 5 letters word.");
        kbView.releaseAllKeys();
    }
}