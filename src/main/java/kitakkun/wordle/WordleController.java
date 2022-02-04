package kitakkun.wordle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import kitakkun.wordle.system.*;
import kitakkun.wordle.view.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
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

    private Settings settings;
    private Wordle wordle;

    public void initialize() {
        settings = new Settings();
        readyWordle();
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
    protected void readyWordle() {
        wordle = new Wordle(settings.getAnswerDictionary().getRandomWord(settings.getWordLength()));
        dsView.setDictionary(settings.getDictionary());
        wiView.ready(wordle, settings.getAttemptLimit());
        messageBox.setText(String.format("Imagine %d letters word.", settings.getWordLength()));
        kbView.releaseAllKeys();
        try {
            Stage window = (Stage) wiView.getScene().getWindow();
            window.sizeToScene();
        } catch (NullPointerException ignore) { }
    }

    @FXML
    protected void openSettings() {
        SettingWindow window = new SettingWindow(settings);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setOnHiding(windowEvent -> readyWordle());
        window.show();
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