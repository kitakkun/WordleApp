package kitakkun.wordle;

import javafx.scene.Scene;
import javafx.stage.Stage;
import kitakkun.wordle.system.Settings;

public class SettingWindow extends Stage {

    private final Settings settings;
    private final Scene scene;
    private final SettingView settingView;

    SettingWindow(Settings settings) {
        this.settings = settings;
        this.settingView = new SettingView(settings);
        scene = new Scene(settingView);
        this.setScene(scene);
    }

}
