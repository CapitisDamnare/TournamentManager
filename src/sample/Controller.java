package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private MenuBar menuBar;

    @FXML
    private Button btnMode;

    @FXML
    private MenuItem btnMenuNew;

    @FXML
    private Button btnGroupSettings;

    @FXML
    private TableView<?> vrTableView;

    @FXML
    private Label statusLabel;

    @FXML
    private TabPane tabPane;

    @FXML
    private TreeView<String> menuTree;

    @FXML
    private MenuItem btnMenuDelete;

    private TreeItem<String> rootItem;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rootItem = new TreeItem<>("Tournaments");
        rootItem.setExpanded(true);
        menuTree.setRoot(rootItem);
    }

    public void setRdyLabel(String text) {
        statusLabel.setText(text);
    }

    private void setExampleData() {

    }


    @FXML
    void btnGroupSettingsOnClick(ActionEvent event) {

    }

    @FXML
    void btnModeOnClick(ActionEvent event) {

    }

    @FXML
    void btnMenuNewOnCLick(ActionEvent event) {
        TreeItem<String> tournItem = new TreeItem<>("Torunament1");
        TreeItem<String> settingsItem = new TreeItem<>("Settings");
        TreeItem<String> vorrundeItem = new TreeItem<>("Vorrunde");
        tournItem.getChildren().addAll(settingsItem, vorrundeItem);
        rootItem.getChildren().add(tournItem);
    }

    @FXML
    void btnMenuDeleteOnClick(ActionEvent event) {

    }
}
