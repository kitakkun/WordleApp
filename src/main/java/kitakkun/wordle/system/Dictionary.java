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
                words.addAll(Arrays.stream(line.split(",")).toList());
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        ArrayList<String> words = loadWords(Objects.requireNonNull(getClass().getResource("/words/words_alpha.txt")));
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

    private ArrayList<String> loadWords(URL url) {
        ArrayList<String> words = new ArrayList<>();
        try {
            File file = Paths.get(url.toURI()).toFile();
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            for (String line : br.lines().toList()) {
                words.addAll(Arrays.stream(line.split(",")).toList());
            }
            br.close();
        } catch (IOException | URISyntaxException e) {
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
        word = word.toLowerCase();
        for (String w : words) {
            if (w.equals(word)) {
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
