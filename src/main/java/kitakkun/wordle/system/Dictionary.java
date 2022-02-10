package kitakkun.wordle.system;

import javafx.scene.layout.Pane;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.PatternSyntaxException;

/**
 * 単語を管理するクラス
 */
public class Dictionary {

    private final String[] words;

    /**
     * コンストラクタ
     */
    public Dictionary() {
        ArrayList<String> words = new ArrayList<>();
        try {
            InputStream is = getClass().getResourceAsStream("/words/words_alpha.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            for (String line : br.lines().toList()) {
                words.addAll(Arrays.stream(line.replace(" ", "").split(",")).toList());
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.words = words.toArray(new String[0]);
        System.out.printf("%d words was loaded.\n", this.words.length);
    }

    public Dictionary(String[] words) {
        this.words = words;
        System.out.printf("%d words was loaded.\n", this.words.length);
    }

    public Dictionary(URL url) {
        ArrayList<String> words = loadWords(url);
        this.words = words.toArray(new String[0]);
        System.out.printf("%d words was loaded.\n", this.words.length);
    }

    public Dictionary(File file) {
        ArrayList<String> words = loadWords(file);
        this.words = words.toArray(new String[0]);
        System.out.printf("%d words was loaded.\n", this.words.length);
    }

    private ArrayList<String> loadWords(URL url) {
        ArrayList<String> words = new ArrayList<>();
        try {
            File file = Paths.get(url.toURI()).toFile();
            words = loadWords(file);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return words;
    }

    private ArrayList<String> loadWords(File file) {
        ArrayList<String> words = new ArrayList<>();
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            for (String line : br.lines().toList()) {
                words.addAll(Arrays.stream(line.replace(" ", "").split(",")).toList());
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }

    /**
     * 単語が存在するか
     * @param word 調べたい単語
     * @return 存在するかどうか
     */
    public boolean isExist(String word) {
        for (String w : words) {
            if (w.equalsIgnoreCase(word)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 辞書内からランダムな単語を取得します．
     * @return ランダムな単語
     */
    public String getRandomWord() {
        int index = (int)(Math.random() * words.length);
        return words[index];
    }

    /**
     * 文字列長を指定してランダムな単語を取得します．
     * @param length 文字列長
     * @return 指定文字数のランダムな単語
     */
    public String getRandomWord(int length) {
        String[] words = Arrays.stream(this.words).toList().stream().filter(word -> word.length() == length).toList().toArray(new String[0]);
        int index = (int)(Math.random() * words.length);
        return words[index];
    }

    public int getWordCount() {
        return words.length;
    }

    public String[] search(String regex) {
        ArrayList<String> searched = new ArrayList<>();
        try {
            for (String word : words) {
                if (word.toLowerCase().matches(regex)) {
                    searched.add(word);
                }
            }
        } catch (PatternSyntaxException e) {
            e.printStackTrace();
        }
        return searched.toArray(new String[0]);
    }
}
