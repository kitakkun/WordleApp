/**
 * jsoup License
 * The jsoup code-base (including source and compiled packages) are distributed under the open source MIT license as described below.
 *
 * The MIT License
 * Copyright © 2009 - 2021 Jonathan Hedley (https://jsoup.org/)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package kitakkun.wordle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
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
    private BorderPane parent;
    @FXML
    private Label messageBox;
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
        parent.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/base.css")).toExternalForm());
        settings = new Settings();
        readyWordle();
    }

    public void onKeyPressed(KeyEvent event) {
        WordleState state = wiView.onKeyPressed(event);
        kbView.onKeyPressed(event);
        String message = generateMessage(state);
        if (message != null) {
            messageBox.setText(message);
        }
        if (state == WordleState.CHECKED || state == WordleState.FINISHED) {
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

    private String generateMessage(WordleState state) {
        return switch (state) {
            case NOT_ON_DICTIONARY -> String.format("The word '%s' doesn't exist on our dictionary.", wiView.getCurrentWord());
            case FINISHED -> String.format("The answer is '%s'.", wiView.getAnswer());
            default -> null;
        };
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
        if (settings.isDarkTheme()) {
            parent.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/dark.css")).toExternalForm());
        } else {
            parent.getStylesheets().remove(Objects.requireNonNull(getClass().getResource("/css/dark.css")).toExternalForm());
        }
        wordle = new Wordle(settings.getAnswerDictionary().getRandomWord(settings.getWordLength()));
        dsView.setDictionary(settings.getDictionary());
        wiView.ready(wordle, settings.getAttemptLimit(), settings.getDictionary());
        wordView.ready(settings.getWordLength());
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