package kitakkun.wordle;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class SettingWindow extends Stage {

    private final Scene scene;
    private final SettingView settingView;

    SettingWindow() {
        this.settingView = new SettingView();
        scene = new Scene(settingView);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/base.css")).toExternalForm());
        this.setScene(scene);
    }

}
