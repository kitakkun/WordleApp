package kitakkun.wordle.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import javax.crypto.spec.PSource;
import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class KeyBoardView extends VBox {

    private HashMap<Character, Pane> keyPanes;
    private Color colorOnReleased;
    private Color colorOnPressed;

    public KeyBoardView() {
        colorOnPressed = Color.LIGHTGRAY;
        colorOnReleased = Color.WHITE;
        initialize();
    }

    public void initialize() {
        keyPanes = new HashMap<>();
        String letters = "QWERTYUIOPASDFGHJKLZXCVBNM";
        HBox hBox = null;
        for (char c : letters.toCharArray()) {
            if (c == 'Q' || c == 'A' || c == 'Z') {
                hBox = new HBox();
                hBox.setAlignment(Pos.CENTER);
                this.getChildren().add(hBox);
            }
            StackPane pane = new StackPane();
            pane.setStyle("-fx-background-color: white;");
            pane.setMinSize(30,30);
            Label label = new Label(String.valueOf(c));
            pane.getChildren().add(label);
            StackPane.setAlignment(label, Pos.CENTER);
            hBox.getChildren().add(pane);
            keyPanes.put(c, pane);
        }
    }

    public void setKeyColor(char c, Color color) {
        int red = (int)(256 * color.getRed());
        int blue = (int)(256 * color.getBlue());
        int green = (int)(256 * color.getGreen());
        if (keyPanes.containsKey(c)) {
            String style = String.format("-fx-background-color: rgb(%d,%d,%d)", red, green, blue);
            keyPanes.get(c).setStyle(style);
        }
    }

    public void onKeyPressed(KeyEvent event) {
        String text = event.getText();
        if (!text.equals("")) {
            char c = text.toUpperCase().charAt(0);
            setKeyColor(c, colorOnPressed);
        }
    }

    public void onKeyReleased(KeyEvent event) {
        String text = event.getText();
        if (!text.equals("")) {
            char c = text.toUpperCase().charAt(0);
            setKeyColor(c, colorOnReleased);
        }
    }

    public void releaseAllKeys() {
        for (char c : keyPanes.keySet()) {
            setKeyColor(c, colorOnReleased);
        }
    }
}
