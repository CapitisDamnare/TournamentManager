package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.util.Callback;

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
    private TableView<Game> vrTableView;

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

        EventHandler<MouseEvent> mouseEventHandle = (MouseEvent event) -> {
            handleMouseClicked(event);
        };

        menuTree.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventHandle);

        ObservableList<Game> games = FXCollections.observableArrayList(
                new Game("1", "10:00", "Home Team A", "Guest Team B"),
                new Game("2", "10:00", "Home Team C", "Guest Team D"),
                new Game("3", "10:00", "Home Team E", "Guest Team F"),
                new Game("4", "10:00", "Home Team G", "Guest Team H")
        );

        ObservableList<? extends TableColumn<?, ?>> columns = vrTableView.getColumns();
        TableColumn first = columns.get(0);
        TableColumn second = columns.get(1);
        TableColumn third = columns.get(2);
        TableColumn fourth = columns.get(3);
        TableColumn fifth = columns.get(4);
        TableColumn sixt = columns.get(5);

        first.setCellValueFactory(new PropertyValueFactory<Game, String>("gameNumber"));
        first.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Game, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Game, String> event) {
                        System.out.println("Cell Edit event called");
                        ((Game) event.getTableView().getItems().get(
                                event.getTablePosition().getRow())
                        ).setGameNumber(event.getNewValue());
                    }
                }
        );
        second.setCellValueFactory(new PropertyValueFactory<Game, String>("homeTeam"));
        third.setCellValueFactory(new PropertyValueFactory<Game, String>("homeTeamScore"));
        fourth.setCellValueFactory(new PropertyValueFactory<Game, String>("versus"));
        fifth.setCellValueFactory(new PropertyValueFactory<Game, String>("guestTeamScore"));
        sixt.setCellValueFactory(new PropertyValueFactory<Game, String>("guestTeam"));

        vrTableView.setItems(games);
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

    private void handleMouseClicked(MouseEvent event) {
        Node node = event.getPickResult().getIntersectedNode();
        // Accept clicks only on node cells, and not on empty spaces of the TreeView
        if (node instanceof Text || (node instanceof TreeCell && ((TreeCell) node).getText() != null)) {
            String name = (String) ((TreeItem) menuTree.getSelectionModel().getSelectedItem()).getValue();

            // Todo: Check if node has a parent
            String parent = (String) ((TreeItem) menuTree.getSelectionModel().getSelectedItem().getParent()).getValue();
            System.out.println("Node click: " + name + "\n Button: " + event.getButton() + "\n Parent: " + parent);

            SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();

            if (event.getButton().equals(MouseButton.PRIMARY)) {
                switch (name) {
                    case "Vorrunde":
                        selectionModel.select(1);
                        break;
                    case "Settings":
                        selectionModel.select(0);
                        break;
                }
            }
        }
    }
}
