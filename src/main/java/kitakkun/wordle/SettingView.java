package kitakkun.wordle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import kitakkun.wordle.system.Settings;

import java.io.IOException;
import java.util.ResourceBundle;

public class SettingView extends Pane {

    @FXML
    private Spinner<Integer> attemptLimitSpinner, wordLengthSpinner;
    @FXML
    private CheckBox permitShorterWord;

    private final Settings settings;

    public SettingView(Settings settings) {
        this.settings = settings;
        ResourceBundle bundle = ResourceBundle.getBundle("/bundles/UIResources");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("setting-view.fxml"), bundle);
        fxmlLoader.setControllerFactory(param -> this);
        try {
            this.getChildren().add(fxmlLoader.load());
            applySettingsToView();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void applySettingsToView() {
        wordLengthSpinner.getValueFactory().setValue(settings.getWordLength());
        attemptLimitSpinner.getValueFactory().setValue(settings.getAttemptLimit());
    }

    public int getWordLength() {
        return wordLengthSpinner.getValue();
    }

    public int getAttemptLimit() {
        return attemptLimitSpinner.getValue();
    }

    public boolean permitShorterWords() {
        return permitShorterWord.isSelected();
    }

    @FXML
    protected void onOKBtnClicked(ActionEvent event) {
        applySettings();
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void onCancelBtnClicked(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    private void applySettings() {
        settings.setWordLength(wordLengthSpinner.getValue());
        settings.setAttemptLimit(attemptLimitSpinner.getValue());
    }
}
