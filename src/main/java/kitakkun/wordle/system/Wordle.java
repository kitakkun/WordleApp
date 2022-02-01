package kitakkun.wordle.system;

import java.util.*;

/**
 * Wordleのアルゴリズムを担うクラス
 */
public class Wordle {

    private final String word;
    private final HashSet<Character> exactLetters;
    private final HashSet<Character> existLetters;
    private final HashSet<Character> notExistLetters;

    /**
     * コンストラクタ
     * @param word 答えとなる単語
     */
    public Wordle(String word) {
        this.word = word.toUpperCase();
        existLetters = new HashSet<>();
        exactLetters = new HashSet<>();
        notExistLetters = new HashSet<>();
    }

    /**
     * 文字が単語内に含まれているかを返します
     * @param c 含まれているか調べる文字
     * @return 含まれているかどうか
     */
    public boolean contains(char c) {
        return word.contains(String.valueOf(c).toUpperCase());
    }

    /**
     * 文字が指定した位置に存在するかどうか返します
     * @param c 調べる文字
     * @param index 文字の位置
     * @return 指定位置に文字が存在するかどうか
     */
    public boolean isExact(char c, int index) {
        return word.substring(index, index + 1).equals(String.valueOf(c).toUpperCase());
    }

    /**
     * 答えの単語を取得します
     * @return 答えに設定されている単語
     */
    public String getWord() {
        return word;
    }

    /**
     * 答えの単語の文字数を取得します
     */
    public int getWordLength() {
        return word.length();
    }

    /**
     * 単語が正解か判定します
     * @param word 判定対象の文字列
     * @return 判定結果
     */
    public boolean isCorrect(String word) {
        return this.word.equals(word);
    }

    /**
     * 入力の単語と答えの単語を比較します
     * @param word 入力単語
     * @return 比較結果
     */
    public Judge[] compare(String word) {
        Judge[] judges = new Judge[this.word.length()];
        for (int i = 0; i < judges.length; i++) {
            char c = word.charAt(i);
            if (isExact(c, i)) {
                judges[i] = Judge.EXACT;
            } else if (contains(c)) {
                judges[i] = Judge.EXIST;
            } else {
                judges[i] = Judge.NOT_EXIST;
            }
        }
        /*
          重複文字に対する処理
          ABBEYのような英単語において，EAGLEと入力したとき，正解の単語にEは一つしか存在しないため，EAGLEの1つ目のEのみをハイライトしたい
          EXIST および EXACT の判定を受けた文字について調整を行う
         */
        Map<Character, Integer> counts = countChars(this.word);  // 正解の単語の各文字の数をカウント
        for (char c : counts.keySet()) {
            // 文字cの判定結果を取得(ex: EXACT: 1, EXIST: 1, NOT_EXIST: 0)
            Map<Judge, Integer> judgeCounts = countCharJudges(word, judges, c);
            // 判定結果における文字cの数を取得
            int existSum = judgeCounts.get(Judge.EXACT) + judgeCounts.get(Judge.EXIST);
            // 正解単語に含まれる文字cの数
            int charCount = counts.get(c);
            // 後ろから順に調べて実在数と判定数を揃える
            for (int i = judges.length - 1; i >= 0 && charCount < existSum; i--) {
                if (judges[i] == Judge.EXIST && word.charAt(i) == c) {
                    judges[i] = Judge.NOT_EXIST;
                    existSum--;
                }
            }
        }

        /*
            判定結果の内部記憶
         */
        for (int i = 0; i < judges.length; i++) {
            switch (judges[i]) {
                case EXACT -> exactLetters.add(word.charAt(i));
                case EXIST -> existLetters.add(word.charAt(i));
                case NOT_EXIST -> notExistLetters.add(word.charAt(i));
            }
        }
        return judges;
    }

    /**
     * 文字列内の文字をそれぞれカウントします
     * @param str 対象の文字列
     * @return 各文字のカウント
     */
    private HashMap<Character, Integer> countChars(String str) {
        HashMap<Character, Integer> counts = new HashMap<>();
        for (char c : str.toCharArray()) {
            counts.put(c, countChar(str, c));
        }
        return counts;
    }

    /**
     * 文字列内の文字をカウントします
     * @param str 対象の文字列
     * @param target カウント対象の文字
     * @return 文字列に含まれる指定文字の数
     */
    private int countChar(String str, char target) {
        int count = 0;
        for (char c : str.toCharArray()) {
            if (c == target) {
                count++;
            }
        }
        return count;
    }

    private HashMap<Judge, Integer> countCharJudges(String str, Judge[] judges, char target) {
        HashMap<Judge, Integer> counts = new HashMap<>();
        counts.put(Judge.EXACT, 0);
        counts.put(Judge.EXIST, 0);
        counts.put(Judge.NOT_EXIST, 0);
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == target) {
                counts.put(judges[i], counts.get(judges[i]) + 1);
            }
        }
        return counts;
    }

    public char[] getExactLetters() {
        return exactLetters.toString().toCharArray();
    }

    public char[] getExistLetters() {
        return existLetters.toString().toCharArray();
    }

    public char[] getNotExistLetters() {
        return notExistLetters.toString().toCharArray();
    }

}
