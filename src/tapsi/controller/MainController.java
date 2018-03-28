package tapsi.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.*;
import javafx.scene.text.Text;
import javafx.util.Callback;
import tapsi.logic.Game;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

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
    private ContextMenu contextMenu;
    private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTreeView();
        setupTableView();
        createSampleData();
    }

    /**
     * Creates a custom editable table view. Links the table columns to the Game class.
     * Creates and handles the drag an drop event
     */
    private void setupTableView() {

        // Todo: Need an additional column for the time
        ObservableList<? extends TableColumn<?, ?>> columns = vrTableView.getColumns();
        TableColumn first = columns.get(0);
        TableColumn second = columns.get(1);
        TableColumn third = columns.get(2);
        TableColumn fourth = columns.get(3);
        TableColumn fifth = columns.get(4);
        TableColumn sixth = columns.get(5);

        Callback<TableColumn, TableCell> cellFactory =
                new Callback<TableColumn, TableCell>() {
                    public TableCell call(TableColumn p) {
                        return new EditingCell();
                    }
                };

        // Todo: setCellFactory should call cellFactory!
        third.setCellFactory(cellFactory);
        third.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Game, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Game, String> event) {
                        ((Game) event.getTableView().getItems().get(
                                event.getTablePosition().getRow())
                        ).setHomeTeamScore(event.getNewValue());
                    }
                }
        );

        fifth.setCellFactory(cellFactory);
        fifth.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Game, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Game, String> event) {
                        ((Game) event.getTableView().getItems().get(
                                event.getTablePosition().getRow())
                        ).setGuestTeamScore(event.getNewValue());
                    }
                }
        );

        first.setCellValueFactory(new PropertyValueFactory<Game, String>("gameNumber"));
        second.setCellValueFactory(new PropertyValueFactory<Game, String>("homeTeam"));
        third.setCellValueFactory(new PropertyValueFactory<Game, String>("homeTeamScore"));
        fourth.setCellValueFactory(new PropertyValueFactory<Game, String>("versus"));
        fifth.setCellValueFactory(new PropertyValueFactory<Game, String>("guestTeamScore"));
        sixth.setCellValueFactory(new PropertyValueFactory<Game, String>("guestTeam"));

        // Drag and Drop implementation
        vrTableView.setRowFactory(tv -> {
            TableRow<Game> row = new TableRow<>();

            row.setOnDragDetected(event -> {
                if (!row.isEmpty()) {
                    Integer index = row.getIndex();
                    Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
                    db.setDragView(row.snapshot(null, null));
                    ClipboardContent cc = new ClipboardContent();
                    cc.put(SERIALIZED_MIME_TYPE, index);
                    db.setContent(cc);
                    event.consume();
                }
            });

            row.setOnDragOver(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    if (row.getIndex() != ((Integer) db.getContent(SERIALIZED_MIME_TYPE)).intValue()) {
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                        event.consume();
                    }
                }
            });

            row.setOnDragEntered(event -> {
                if (!row.isEmpty()) {
                    row.setStyle("-fx-control-inner-background: derive(palegreen, 50%);");
                } else {
                    row.setStyle("-fx-control-inner-background: derive(red, 50%);");
                }

                event.consume();
            });

            row.setOnDragExited(event -> {
                /* mouse moved away, remove the graphical cues */
                row.setStyle("-fx-control-inner-background: derive(-fx-base,80%);");
                event.consume();
            });

            row.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    int draggedIndex = (Integer) db.getContent(SERIALIZED_MIME_TYPE);
                    Game draggedPerson = vrTableView.getItems().remove(draggedIndex);

                    int dropIndex = draggedIndex;

                    if (!row.isEmpty()) {
                        dropIndex = row.getIndex();
                    }
                    vrTableView.getItems().add(dropIndex, draggedPerson);

                    event.setDropCompleted(true);
                    vrTableView.getSelectionModel().select(dropIndex);
                    event.consume();
                }
            });
            return row;
        });

        first.setStyle("-fx-alignment: CENTER;");
        second.setStyle("-fx-alignment: CENTER-LEFT;");
        third.setStyle("-fx-alignment: CENTER;");
        fourth.setStyle("-fx-alignment: CENTER;");
        fifth.setStyle("-fx-alignment: CENTER;");
        sixth.setStyle("-fx-alignment: CENTER-RIGHT;");
    }

    /**
     * Custom class which extends from TableCell and handles the custom editing function.
     * ESC for cancel and restore old values.
     * ENTER accepts the input
     * TAB accepts the input and should focus the next row
     */
    class EditingCell extends TableCell<Game, String> {

        private TextField textField;

        public EditingCell() {
        }

        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                createTextField();
                setText(null);
                setGraphic(textField);
                textField.selectAll();
                textField.requestFocus();
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            setText((String) getItem());
            setGraphic(null);
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setText(null);
                    setGraphic(textField);
                } else {
                    setText(getString());
                    setGraphic(null);
                }
            }
        }

        private void createTextField() {
            textField = new TextField(getString());
            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
            textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> arg0,
                                    Boolean arg1, Boolean arg2) {
                    if (!arg2) {
                        commitEdit(textField.getText());
                        textField.setFocusTraversable(true);
                    }
                }
            });
            textField.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    setText(getString());
                    setGraphic(null);
                }
            });
            // Todo: Implement TAB should focus the next row
        }

        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }

    private void setupTreeView() {
        rootItem = new TreeItem<>("Tournaments");
        rootItem.setExpanded(true);
        menuTree.setRoot(rootItem);

        EventHandler<MouseEvent> mouseEventHandle = (MouseEvent event) -> {
            handleMouseClicked(event);
        };

        menuTree.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventHandle);
    }

    /**
     * Creates the context menu and handles click events
     *
     * @param event {@link MouseEvent}
     */
    private void handleMouseClicked(MouseEvent event) {
        if (contextMenu != null)
            contextMenu.hide();
        Node node = event.getPickResult().getIntersectedNode();
        // Accept clicks only on node cells, and not on empty spaces of the TreeView
        if (node instanceof Text || (node instanceof TreeCell && ((TreeCell) node).getText() != null)) {
            String name = (String) ((TreeItem) menuTree.getSelectionModel().getSelectedItem()).getValue();

            // Todo: Check if node has a parent
            String parent = (String) ((TreeItem) menuTree.getSelectionModel().getSelectedItem().getParent()).getValue();

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

            // Todo: Set method for contextMenu action
            if (event.getButton().equals(MouseButton.SECONDARY)) {
                switch (name) {
                    case "Vorrunde":
                        contextMenu = new ContextMenu();
                        MenuItem Ventry1 = new MenuItem("Ganz");
//                        entry1.setOnAction(ae -> ...);
                        MenuItem Ventry2 = new MenuItem("was anderes");
//                        entry2.setOnAction(ae -> ...);
                        contextMenu.getItems().addAll(Ventry1, Ventry2);
                        contextMenu.show(menuTree, event.getScreenX(), event.getScreenY());
                        break;
                    case "Settings":
                        contextMenu = new ContextMenu();
                        MenuItem Sentry1 = new MenuItem("Test with Icon");
//                        entry1.setOnAction(ae -> ...);
                        MenuItem Sentry2 = new MenuItem("Test without Icon");
//                        entry2.setOnAction(ae -> ...);
                        contextMenu.getItems().addAll(Sentry1, Sentry2);
                        contextMenu.show(menuTree, event.getScreenX(), event.getScreenY());
                        break;
                }
            }
        }
    }

    /**
     * Populates the vrTableView with sample data
     */
    private void createSampleData() {
        ObservableList<Game> games = FXCollections.observableArrayList(
                new Game("1", "10:00", "Home Team A", "Guest Team B"),
                new Game("2", "10:00", "Home Team C", "Guest Team D"),
                new Game("3", "10:00", "Home Team E", "Guest Team F"),
                new Game("4", "10:00", "Home Team G", "Guest Team H")
        );
        vrTableView.setItems(games);
    }

    /**
     * Triggers the btnGroupSettings event
     *
     * @param event {@link ActionEvent}
     */
    @FXML
    void btnGroupSettingsOnClick(ActionEvent event) {
        Observer.btnGroupSettings();
    }

    @FXML
    void btnModeOnClick(ActionEvent event) {

    }

    /**
     * Creates a new tournament project with all TreeItem children
     *
     * @param event {@link ActionEvent}
     */
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
