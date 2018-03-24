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

        new Observer();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root, 600, 600));
        Controller controller = loader.getController();
        primaryStage.show();
        primaryStage.setMinWidth(700);
        primaryStage.setMinHeight(700);

        FXMLLoader loader1 = new FXMLLoader(getClass().getResource("groups.fxml"));
        Parent root1 = loader1.load();
        Stage stage1 = new Stage();
        stage1.setScene(new Scene(root1, 400, 600));
        GroupsController groupsController = loader1.getController();
        groupsController.setStage(stage1);
        stage1.setMinWidth(400);
        stage1.setMinHeight(600);

        Observer.addListener(groupsController);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
