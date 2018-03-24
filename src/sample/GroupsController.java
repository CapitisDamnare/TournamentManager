package sample;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class GroupsController implements ControllerInterface {

    @FXML
    private ListView<String> groupListView;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void onBtnGroupSettings() {
        System.out.println("got here");
        stage.show();
    }

    @Override
    public void onBtnMode() {

    }
}
