package tapsi;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tapsi.controller.GroupsController;
import tapsi.controller.MainController;
import tapsi.controller.Observer;

public class Main extends Application {

    private static FXMLLoader loader;
    private static FXMLLoader loader1;

    private static Stage stage;
    private static Stage stage1;

    public static FXMLLoader getLoader() {
        return loader;
    }

    public static FXMLLoader getLoader1() {
        return loader1;
    }

    public static Stage getStage() {
        return stage;
    }

    public static Stage getStage1() {
        return stage1;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
//        Parent root = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
//        primaryStage.setTitle("Hello World");
//        primaryStage.setScene(new Scene(root, 500, 500));
//        primaryStage.setMinWidth(600);
//        primaryStage.setMinHeight(600);
//        primaryStage.show();
        //Person person = new Person();

        new Observer();

        loader = new FXMLLoader(getClass().getResource("views/mainWindow.fxml"));
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root, 600, 600));
        MainController mainController = loader.getController();
        primaryStage.show();
        primaryStage.setMinWidth(700);
        primaryStage.setMinHeight(700);

        loader1 = new FXMLLoader(getClass().getResource("views/groupsWindow.fxml"));
        Parent root1 = loader1.load();
        stage1 = new Stage();
        stage1.setScene(new Scene(root1, 400, 400));
        GroupsController groupsController = loader1.getController();
        groupsController.setStage(stage1);
        stage1.setMinWidth(400);
        stage1.setMinHeight(400);

        Observer.addListener(groupsController);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
