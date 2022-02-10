package kitakkun.wordle.system.settings;

import kitakkun.wordle.system.Dictionary;
import kitakkun.wordle.system.settings.Settings;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class SettingsReader {

    public static Settings read() {
        Settings settings = new Settings();
        if (!Files.exists(Path.of(Settings.FILE_PATH))) return settings;
        try {
            Properties properties = new Properties();
            File file = new File(Settings.FILE_PATH);
            InputStream is = new FileInputStream(file);
            InputStreamReader ir = new InputStreamReader(is);
            properties.load(ir);

            int wordLength = Integer.parseInt(properties.getProperty("wordLength", "5"));
            int attemptLimit = Integer.parseInt(properties.getProperty("attemptLimit", "6"));
            boolean useDarkTheme = Boolean.parseBoolean(properties.getProperty("useDarkTheme", "false"));
            boolean permitShorterWords = Boolean.parseBoolean(properties.getProperty("permitShorterWords", "false"));
            String dictionaryKey = properties.getProperty("dictionary", "default");
            String answerDictionaryKey = properties.getProperty("answerDictionary", "default");
            Dictionary defaultDictionary = new Dictionary();
            String[] dictionaryPaths = properties.getProperty("dictionaryPaths", "").split(":");
            String[] dictionaryKeys = properties.getProperty("dictionaryKeys", "").split(":");

            for (int i = 0; i < dictionaryPaths.length; i++) {
                if (Files.exists(Path.of(dictionaryPaths[i])) && !dictionaryPaths[i].equals("")) {
                    File dictFile = new File(dictionaryPaths[i]);
                    settings.addDictionaryFile(dictFile);
                }
            }

            settings.setWordLength(wordLength);
            settings.setAttemptLimit(attemptLimit);
            settings.setDarkTheme(useDarkTheme);
            settings.setPermitShorterWords(permitShorterWords);
            settings.setDictionary(settings.getDictionaryByKey(dictionaryKey));
            settings.setAnswerDictionary(settings.getDictionaryByKey(answerDictionaryKey));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return settings;
    }
}
