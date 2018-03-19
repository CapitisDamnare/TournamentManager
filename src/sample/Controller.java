package sample;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Label rdyLabel;

    @FXML
    private MenuBar menuBar;

    @FXML
    private Button btnMode;

    @FXML
    private Button btnGroupSettings;

    @FXML
    private TableView<?> vrTableView;

    @FXML
    private Label statusLabel;

    @FXML
    private TabPane tabPane;

    @FXML
    private TreeView<?> menuTree;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Load data");
        setRdyLabel("jup");
    }

    public void setRdyLabel(String text) {
        rdyLabel.setText(text);
    }

    private void setExampleData() {

    }


    @FXML
    void btnGroupSettingsOnClick(ActionEvent event) {

    }

    @FXML
    void btnModeOnClick(ActionEvent event) {

    }
}
