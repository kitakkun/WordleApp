module kitakkun.wordle {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jsoup;


    opens kitakkun.wordle to javafx.fxml;
    exports kitakkun.wordle;
    exports kitakkun.wordle.view;
    opens kitakkun.wordle.view to javafx.fxml;
    exports kitakkun.wordle.system;
    opens kitakkun.wordle.system to javafx.fxml;
}