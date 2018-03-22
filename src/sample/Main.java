package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.awt.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
//        primaryStage.setTitle("Hello World");
//        primaryStage.setScene(new Scene(root, 500, 500));
//        primaryStage.setMinWidth(600);
//        primaryStage.setMinHeight(600);
//        primaryStage.show();
        //Person person = new Person();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root, 500, 500));
        Controller controller = loader.getController();
        primaryStage.show();
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(600);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
