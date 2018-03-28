package tapsi.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.stage.Stage;
import tapsi.Main;
import tapsi.logic.Team;

import java.net.URL;
import java.util.ResourceBundle;

public class GroupsController implements Initializable, ControllerInterface {

    @FXML
    private ListView<String> groupListView;

    private Stage stage;
    ObservableList<String> group;
    private ContextMenu contextMenu;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Setup of the groupListView an all Event- , Callback- handler.
     *
     * @param location  {@link URL}
     * @param resources {@link ResourceBundle}
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupListView();
        exampleData();
    }

    private void setupListView() {
        groupListView.setEditable(true);

        groupListView.setCellFactory(lv -> {
            ListCell<String> cell = new TeamListCell();

            cell.setOnDragDetected(event -> {
                if (!cell.isEmpty()) {
                    Dragboard db = cell.startDragAndDrop(TransferMode.MOVE);
                    db.setDragView(cell.snapshot(null, null));
                    ClipboardContent cc = new ClipboardContent();
                    cc.putString(cell.getText());
                    db.setContent(cc);
                    event.consume();
                }
            });

            cell.setOnDragOver(event -> {
                if (event.getGestureSource() != cell && event.getDragboard().hasString()) {
                    event.acceptTransferModes(TransferMode.MOVE);
                    event.consume();
                }
            });

            cell.setOnDragEntered(event -> {
                if (event.getDragboard().hasString() && cell.getText() != null) {
                    cell.setStyle("-fx-control-inner-background: derive(palegreen, 50%);");
                } else {
                    cell.setStyle("-fx-control-inner-background: derive(red, 50%);");
                }

                event.consume();
            });

            cell.setOnDragExited(event -> {
                /* mouse moved away, remove the graphical cues */
                cell.setStyle("-fx-control-inner-background: derive(-fx-base,80%);");
                event.consume();
            });

            cell.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasString()) {
                    ObservableList<String> items = groupListView.getItems();
                    int draggedIndex = items.indexOf(db.getString());
                    String draggedPerson = groupListView.getItems().remove(draggedIndex);

                    int dropIndex = draggedIndex;

                    if (!(cell.isEmpty() && cell.getIndex() != groupListView.getItems().size())) {
                        dropIndex = cell.getIndex();
                    }

                    groupListView.getItems().add(dropIndex, draggedPerson);

                    event.setDropCompleted(true);
                    groupListView.getSelectionModel().select(dropIndex);
                    event.consume();
                }
            });
            return cell;
        });

        EventHandler<MouseEvent> mouseEventHandle = (MouseEvent event) -> {
            handleMouseClicked(event);
        };

        groupListView.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventHandle);
    }

    private void handleMouseClicked(MouseEvent event) {
        if (contextMenu != null)
            contextMenu.hide();

        if (event.getButton().equals(MouseButton.SECONDARY)) {
            contextMenu = new ContextMenu();
            MenuItem menuItem = new MenuItem("Team löschen");
            menuItem.setOnAction(ae -> deleteItem(groupListView.getSelectionModel().getSelectedIndex()));
            MenuItem menuItem1 = new MenuItem("Team hinzufügen");
            menuItem1.setOnAction(ae -> addItem(groupListView.getSelectionModel().getSelectedIndex()));
            contextMenu.getItems().addAll(menuItem, menuItem1);
            contextMenu.show(groupListView, event.getScreenX(), event.getScreenY());
        }
    }

    private void deleteItem(int index) {
        group.remove(index);
    }

    private void addItem(int index) {
        group.add(index, "Name");
        groupListView.getSelectionModel().select(index);
    }

    private void exampleData() {
        group = FXCollections.observableArrayList(
                new Team("1. Team").getName(),
                new Team("2. Team").getName(),
                new Team("3. Team").getName(),
                new Team("4. Team").getName()
        );

        groupListView.setItems(group);
    }

    public class TeamListCell extends ListCell<String> {
        private final TextField textField = new TextField();

        public TeamListCell() {
            textField.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.ESCAPE) {
                    cancelEdit();
                } else if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.TAB) {
                    groupListView.getSelectionModel().select(getIndex() + 1);
                }
            });
            textField.setOnAction(e -> {
                group.remove(getIndex());
                group.add(getIndex(), textField.getText());
                setText(textField.getText());
                setContentDisplay(ContentDisplay.TEXT_ONLY);
            });
            setGraphic(textField);
        }

        @Override
        protected void updateItem(String string, boolean empty) {
            super.updateItem(string, empty);
            if (isEditing()) {
                textField.setText(string);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            } else {
                setContentDisplay(ContentDisplay.TEXT_ONLY);
                if (empty) {
                    setText(null);
                } else {
                    setText(string);
                }
            }
        }

        @Override
        public void startEdit() {
            super.startEdit();
            textField.setText(getItem());
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            textField.requestFocus();
            textField.selectAll();
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
            setText(getItem());
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }
    }

    @FXML
    void btnOkOnClicked(ActionEvent event) {
        Main.getStage().show();
        stage.close();
    }

    @FXML
    void btnAbortOnClick(ActionEvent event) {
        Main.getStage().show();
        stage.close();
    }

    @Override
    public void onBtnGroupSettings() {
        stage.show();
        Main.getStage().hide();
    }

    @Override
    public void onBtnMode() {

    }
}
