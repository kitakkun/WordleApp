package kitakkun.wordle.view;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import kitakkun.wordle.system.Dictionary;
import kitakkun.wordle.system.Judge;
import kitakkun.wordle.system.Wordle;
import kitakkun.wordle.system.WordleState;

import java.util.Objects;

public class WordInputView extends GridPane {

    private Pane[][] cells;
    private int cursorX = 0, cursorY = 0;

    private Wordle wordle;
    private Dictionary dictionary;

    private boolean isFinished = false;

    private int wordLength = 5;
    private int attemptLimit = 6;

    public WordInputView() {
        initialize();
    }

    public void initialize() {
        this.setOnMouseClicked(mouseEvent -> requestFocus());
        this.setAlignment(Pos.CENTER);
        initTable(wordLength, attemptLimit);
        dictionary = new Dictionary();
    }

    private void initTable(int wordLength, int attemptLimit) {
        getChildren().clear();
        getRowConstraints().clear();
        getColumnConstraints().clear();
        setHgap(3);
        setVgap(3);
        cells = new Pane[attemptLimit][wordLength];
        for (int i = 0; i < attemptLimit; i++) {
            for (int j = 0; j < wordLength; j++) {
                StackPane pane = new StackPane();
                Label charLabel = new Label();
                StackPane.setAlignment(charLabel, Pos.CENTER);
                pane.getStyleClass().add("letter-cell");
                pane.getChildren().add(charLabel);
                add(pane, j, i);
                cells[i][j] = pane;
            }
        }
        RowConstraints rc = new RowConstraints(50);
        ColumnConstraints cc = new ColumnConstraints(50);
        for (int i = 0; i < getRowCount(); i++) {
            getRowConstraints().add(rc);
        }
        for (int i = 0; i < getColumnCount(); i++) {
            getColumnConstraints().add(cc);
        }
    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public void ready(Wordle wordle, int attemptLimit) {
        this.wordle = wordle;
        this.wordLength = wordle.getWordLength();
        this.attemptLimit = attemptLimit;
        cursorX = 0;
        cursorY = 0;
        isFinished = false;
        initTable(wordLength, attemptLimit);
    }

    /**
     * アルファベットを入力します．
     * @param c 入力された文字
     */
    public WordleState input(char c) {
        WordleState state;
        if (!isCursorXOnEnd()) {
            Label charLabel = (Label) cells[cursorY][cursorX].getChildren().get(0);
            charLabel.setText(String.valueOf(c).toUpperCase());
            moveCursorX("forward");
            state = WordleState.INPUT;
        } else {
            state = WordleState.NO_INPUT;
        }
        return state;
    }

    /**
     * 一文字削除します．
     */
    public WordleState backspace() {
        WordleState state = WordleState.BACKSPACE;
        moveCursorX("backward");
        if (cursorX != cells[0].length) {
            Label charLabel = (Label) cells[cursorY][cursorX].getChildren().get(0);
            charLabel.setText("");
        }
        return state;
    }

    /**
     * 単語を確定します．
     */
    public WordleState enter() {
        String word = getCurrentWord();
        WordleState state;
        // 単語が最後まで入力されていない場合
        if (!isCursorXOnEnd()) {
            state = WordleState.NOT_ENOUGH_LETTERS;
        } else if (!dictionary.isExist(word)) {
            System.out.printf("The word, \"%s\" doesn't exist on our dictionary\n", word);
            state = WordleState.NOT_ON_DICTIONARY;
        } else {
            Judge[] judges = wordle.compare(word);
            updateWordCells(judges);
            moveCursorY("forward");
            resetCursorX();
            isFinished = wordle.isCorrect(word);
            if (wordle.isCorrect(word) || isFinished()) {
                state = WordleState.FINISHED;
            } else {
                state = WordleState.CHECKED;
            }
        }
        return state;
    }

    /**
     * セルの状態を更新します
     * @param judges 判定情報
     */
    private void updateWordCells(Judge[] judges) {
        animateCells(judges);
    }

    private void animateCells(Judge[] judges) {
        Timeline timeline = new Timeline();
        double duration = 0;
        double span = 200;
        for (int i = 0; i < judges.length; i++) {
            Pane cell = cells[cursorY][i];
            Label label = (Label) cell.getChildren().get(0);
            Judge judge = judges[i];
            cell.setRotationAxis(new Point3D(1, 0, 0));
            timeline.getKeyFrames().addAll(
                    new KeyFrame(new Duration(duration * span), new KeyValue(cell.rotateProperty(), 0)),
                    new KeyFrame(new Duration((duration + 1) * span), new KeyValue(cell.rotateProperty(), 90)),
                    new KeyFrame(new Duration((duration + 1) * span), e -> cell.getStyleClass().add(getStyleClassByJudge(judge))),
                    new KeyFrame(new Duration((duration + 1) * span), e -> label.setTextFill(Color.WHITE)),
                    new KeyFrame(new Duration((duration + 2) * span), new KeyValue(cell.rotateProperty(), 0))
            );
            duration += 2;
        }
        timeline.play();
    }

    private String getStyleClassByJudge(Judge judge) {
        return switch (judge) {
            case EXACT -> "exact-bg";
            case EXIST -> "exist-bg";
            case NOT_EXIST -> "not-exist-bg";
        };
    }

    /**
     * カーソルが終端まで来ているかを判定します
     */
    private boolean isCursorXOnEnd() {
        return cursorX == wordLength;
    }

    private boolean isCursorYOnEnd() {
        return cursorY == attemptLimit;
    }

    public boolean isFinished() {
        return isFinished || isCursorYOnEnd();
    }

    public void printCursor() {
        System.out.printf("X: %d, Y: %d\n", cursorX, cursorY);
    }

    /**
     * 現在入力されている文字列を取得します．
     */
    public String getCurrentWord() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cursorX; i++) {
            Label charLabel = (Label) cells[cursorY][i].getChildren().get(0);
            sb.append(charLabel.getText());
        }
        return sb.toString();
    }

    private void resetCursorX() {
        cursorX = 0;
    }

    private void moveCursorX(String direction) {
        if (direction.equals("forward")) {
            if (cursorX < wordLength) {
                cursorX++;
            }
        } else if (direction.equals("backward")){
            if (cursorX > 0) {
                cursorX--;
            }
        }
    }

    private void moveCursorY(String direction) {
        if (direction.equals("forward")) {
            if (cursorY < attemptLimit) {
                cursorY++;
            }
        } else if (direction.equals("backward")){
            if (cursorY > 0) {
                cursorY--;
            }
        }
    }

    public WordleState onKeyPressed(KeyEvent keyEvent) {
        String input = keyEvent.getText();
        KeyCode code = keyEvent.getCode();
        WordleState state = WordleState.NULL;
        if (isFinished()) {
            state = WordleState.FINISHED;
        } else if (input.matches("[a-z]|[A-Z]")) {
            state = input(input.charAt(0));
        } else if (code == KeyCode.BACK_SPACE) {
            state = backspace();
        } else if (code == KeyCode.ENTER) {
            state = enter();
        }
        return state;
    }

    public String getAnswer() {
        return wordle.getWord();
    }

    public String[] getAllInputs() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cursorY; i++) {
            for (int j = 0; j < wordLength; j++) {
                Label label = (Label) cells[i][j].getChildren().get(0);
                sb.append(label.getText());
            }
            sb.append(",");
        }
        return sb.toString().split(",");
    }
}
