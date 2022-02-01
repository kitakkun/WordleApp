package kitakkun.wordle.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class WordView extends HBox {

    private int wordLength;
    private Pane[] panes;

    public WordView() {
        initialize();
    }

    public void initialize() {
        this.setAlignment(Pos.CENTER);
        wordLength = 5;
        panes = new Pane[wordLength];
        for (int i = 0; i < wordLength; i++) {
            StackPane pane = new StackPane();
            pane.setMinSize(50, 50);
            Label letter = new Label("?");
            pane.getStyleClass().add("cell");
            letter.getStyleClass().add("cell-char");
            pane.getChildren().add(letter);
            this.getChildren().add(pane);
            panes[i] = pane;
        }
    }

    public void setChar(int index, char c) {
        Label letter = (Label) panes[index].getChildren().get(0);
        letter.setText(String.valueOf(c));
    }


}
