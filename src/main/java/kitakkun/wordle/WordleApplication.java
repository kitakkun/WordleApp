package kitakkun.wordle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class WordleApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(WordleApplication.class.getResource("wordle-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        WordleController c = fxmlLoader.getController();
        scene.addEventHandler(KeyEvent.KEY_PRESSED, c::onKeyPressed);
        scene.addEventHandler(KeyEvent.KEY_RELEASED, c::onKeyReleased);
        stage.setTitle("Wordle");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}