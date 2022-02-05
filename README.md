# WordleApp
WordleAppはJavaFXをベースに構成されたWordleゲーム(https://www.powerlanguage.co.uk/wordle/ )のGUIアプリケーションです．

## 動作環境
OpenJDK 17.0.2
OpenJFX 17.0.2

## 実行方法
out/artifacts/Wordle/Wordle.jarをダウンロードし，下記コマンドにより実行します．ただし，JavaFXランタイムは付属しておりませんので，
https://gluonhq.com/products/javafx/ から適切なSDKをダウンロードする必要があります．

```shell
java -jar --module-path /path/to/javafx/lib --add-modules javafx.fxml,javafx.controls Wordle.jar
```

## 使用ライブラリ
- OpenJFX 17.0.2
- jsoup 1.14.3