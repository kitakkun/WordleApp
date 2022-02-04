package kitakkun.wordle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import kitakkun.wordle.system.Dictionary;
import kitakkun.wordle.system.Judge;
import kitakkun.wordle.system.Wordle;
import kitakkun.wordle.system.WordleState;
import kitakkun.wordle.view.*;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Locale;
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
    @FXML
    private DictionarySearchView dsView;

    @FXML
    private CheckMenuItem showDict, showKB;

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
        dsView.setDictionary(dictionary);
        kbView.setKeyColor('Q', Color.WHITE);
    }

    public void onKeyPressed(KeyEvent event) {
        WordleState state = wiView.onKeyPressed(event);
        kbView.onKeyPressed(event);

        String message = "No message";
        switch (state) {
            case NOT_ON_DICTIONARY -> message = String.format("The word '%s' doesn't exist on our dictionary.", wiView.getCurrentWord());
            case FINISHED -> message = String.format("The answer is '%s'.", wiView.getAnswer());
            case CHECKED -> {
                String[] words = wiView.getAllInputs();
                String word = words[words.length - 1];
                Judge[] judges = wordle.compare(word);
                for (int i = 0; i < judges.length; i++) {
                    if (judges[i] == Judge.EXACT) {
                        wordView.setChar(i, word.charAt(i));
                    }
                }
            }
        }
        if (state == WordleState.NOT_ON_DICTIONARY || state == WordleState.FINISHED ) {
            messageBox.setText(message);
        }
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
        wordle = new Wordle(answerDictionary.getRandomWord(5));
        wiView.ready(wordle, 10);
        messageBox.setText("Imagine 5 letters word.");
        kbView.releaseAllKeys();
    }

    @FXML
    protected void openSettings() {
        SettingView view = new SettingView();
        Stage stage = new Stage();
        Scene scene = new Scene(view);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @FXML
    protected void showView(ActionEvent event) {
        CheckMenuItem item = (CheckMenuItem) event.getSource();
        String id = item.getId();
        switch (id) {
            case "showKB" -> kbView.setVisible(item.isSelected());
            case "showDict" -> dsView.setVisible(item.isSelected());
        }
    }

    @FXML
    protected void searchWords() {
        Stage stage = new Stage();
        SearchResultView srView = new SearchResultView();
        Scene scene = new Scene(srView, 100, 100);
        stage.setScene(scene);
        String[] words = wiView.getAllInputs();
        for (String word : words) {
            try {
                String url = String.format("https://ejje.weblio.jp/content/%s", word.toLowerCase());
                Document doc = Jsoup.connect(url).get();

                Elements meanings = doc.getElementsByClass("content-explanation");
                if (meanings.size() != 0) {
                    Element meaning = meanings.get(0);
                    srView.addWord(word, meaning.text());
                    System.out.printf("%s: %s\n", word, meaning.text());
                }
                Thread.sleep(1000);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        srView.constructView();
        stage.sizeToScene();
        stage.show();
    }
}