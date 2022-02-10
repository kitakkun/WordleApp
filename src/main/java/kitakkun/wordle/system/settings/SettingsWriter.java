package kitakkun.wordle.system.settings;

import kitakkun.wordle.system.settings.Settings;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class SettingsWriter {

    public static void write(Settings settings) {
        Properties properties = new Properties();
        properties.put("wordLength", String.valueOf(settings.getWordLength()));
        properties.put("attemptLimit", String.valueOf(settings.getAttemptLimit()));
        properties.put("useDarkTheme", String.valueOf(settings.isDarkTheme()));
        properties.put("permitShorterWords", String.valueOf(settings.permitShorterWords()));
        properties.put("dictionary", settings.getKeyByDictionary(settings.getDictionary()));
        properties.put("answerDictionary", settings.getKeyByDictionary(settings.getAnswerDictionary()));
        properties.put("dictionaryPaths", String.join(":", settings.getDictionaryPaths()));
        File file = new File(Settings.FILE_PATH);
        try {
            OutputStream os = new FileOutputStream(file);
            properties.store(os, "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
