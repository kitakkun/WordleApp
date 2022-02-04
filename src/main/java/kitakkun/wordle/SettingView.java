package kitakkun.wordle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import kitakkun.wordle.system.Settings;

import java.io.IOException;
import java.util.ResourceBundle;

public class SettingView extends Pane {

    @FXML
    private ComboBox dictComboBox;
    @FXML
    private ComboBox ansDictComboBox;
    @FXML
    private Spinner<Integer> attemptLimitSpinner, wordLengthSpinner;
    @FXML
    private CheckBox permitShorterWord;
    @FXML
    private RadioButton lightRadioBtn, darkRadioBtn;
    private ToggleGroup themeToggle;

    private final Settings settings;

    public SettingView(Settings settings) {
        this.settings = settings;
        ResourceBundle bundle = ResourceBundle.getBundle("/bundles/UIResources");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("setting-view.fxml"), bundle);
        fxmlLoader.setControllerFactory(param -> this);
        try {
            this.getChildren().add(fxmlLoader.load());
            setupViews();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupViews() {
        wordLengthSpinner.getValueFactory().setValue(settings.getWordLength());
        attemptLimitSpinner.getValueFactory().setValue(settings.getAttemptLimit());

        themeToggle = new ToggleGroup();
        themeToggle.getToggles().add(lightRadioBtn);
        themeToggle.getToggles().add(darkRadioBtn);
        darkRadioBtn.setOnAction(event -> {
            if (darkRadioBtn.isSelected()) {
                getStylesheets().add(getClass().getResource("/css/dark.css").toExternalForm());
            }
        });
        lightRadioBtn.setOnAction(event -> {
            if (lightRadioBtn.isSelected()) {
                getStylesheets().remove(getClass().getResource("/css/dark.css").toExternalForm());
            }
        });
        if (settings.isDarkTheme()) {
            themeToggle.selectToggle(darkRadioBtn);
            getStylesheets().add(getClass().getResource("/css/dark.css").toExternalForm());
        } else {
            themeToggle.selectToggle(lightRadioBtn);
        }
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
        settings.setPermitShorterWords(permitShorterWord.isSelected());
        settings.setDarkTheme(darkRadioBtn.isSelected());
    }
}
