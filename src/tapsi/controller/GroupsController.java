package tapsi.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tapsi.Main;
import tapsi.logic.Team;

import java.net.URL;
import java.util.*;

public class GroupsController implements Initializable, ControllerInterface {

    @FXML
    private VBox mainVBox;

    @FXML
    private HBox hBoxGroups;

    @FXML
    private ListView<String> groupList0;

    @FXML
    private MenuButton groupButton;

    @FXML
    private MenuItem groupBtnItem1;

    @FXML
    private MenuItem groupBtnItem2;

    @FXML
    private MenuItem groupBtnItem3;

    @FXML
    private MenuItem groupBtnItem4;

    @FXML
    private MenuItem groupBtnItem5;

    @FXML
    private MenuItem groupBtnItem6;

    private Stage stage;
    private ContextMenu contextMenu;

    private int groupCount = 0;
    private final int MAX_GROUP_COUNT = 6;
    private List<VBox> vBoxList;
    private List<ObservableList<String>> groupsList = new ArrayList<>();

    /**
     * Sets the Stage to the controller.
     * The controller is now able to hide and show the stage.
     *
     * @param stage
     */
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
        exampleData();
        setupListView();
        setupGroupButton();
    }

    /**
     * Creates all group list views and sets up the event handlers. (Mouse Event, Key Event)
     * Also binds the list views the observable lists.
     */
    private void setupListView() {

        groupList0.setEditable(true);
        groupList0.setCellFactory(lv -> handleCellFactory(lv));
        EventHandler<MouseEvent> mouseEventHandle = (MouseEvent event) -> {
            handleMouseClicked(event);
        };
        groupList0.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventHandle);

        vBoxList = new ArrayList<>();
        List<String> names = Arrays.asList("Gruppe B", "Gruppe C", "Gruppe D", "Gruppe E", "Gruppe F");
        ListIterator<String> nameListIterator = names.listIterator();

        vBoxList.add(mainVBox);

        while (nameListIterator.hasNext()) {
            int index = nameListIterator.nextIndex() + 1;
            VBox vBox = new VBox();
            Label label = new Label(nameListIterator.next());
            ListView<String> listView = new ListView<>();
            listView.setId("groupList" + String.valueOf(index));

            ObservableList<String> group = FXCollections.observableArrayList();
            groupsList.add(group);
            listView.setItems(group);

            setupListViewProperties(listView, vBox, label);
            vBox.getChildren().addAll(label, listView);

            vBoxList.add(vBox);
        }
    }

    /**
     * Sets the properties for the given nodes
     *
     * @param listView the current ListView
     * @param vBox     the current vBox which holds the list view and the label
     * @param label    the current label
     */
    private void setupListViewProperties(ListView<String> listView, VBox vBox, Label label) {
        label.setAlignment(Pos.CENTER);

        vBox.nodeOrientationProperty().setValue(NodeOrientation.INHERIT);
        vBox.setMinWidth(Region.USE_COMPUTED_SIZE);
        vBox.setMinHeight(Region.USE_COMPUTED_SIZE);
        vBox.setMaxWidth(Region.USE_COMPUTED_SIZE);
        vBox.setMaxHeight(Region.USE_COMPUTED_SIZE);
        vBox.setPrefWidth(Region.USE_COMPUTED_SIZE);
        vBox.setPrefHeight(Region.USE_COMPUTED_SIZE);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(5);
        vBox.setVgrow(listView, Priority.ALWAYS);

        listView.setEditable(true);
        listView.nodeOrientationProperty().setValue(NodeOrientation.INHERIT);
        listView.setMinWidth(Region.USE_COMPUTED_SIZE);
        listView.setMinHeight(Region.USE_COMPUTED_SIZE);
        listView.setMaxWidth(Region.USE_COMPUTED_SIZE);
        listView.setMaxHeight(Region.USE_COMPUTED_SIZE);
        listView.setPrefWidth(200);
        listView.setPrefHeight(200);

        listView.setCellFactory(lv -> handleCellFactory(lv));

        EventHandler<MouseEvent> mouseEventHandle = (MouseEvent event) -> {
            handleMouseClicked(event);
        };

        listView.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventHandle);
    }

    /**
     * Creates a new {@link TeamListCell} class which handles the edit handling.
     * Also defines the drag and drop event.
     *
     * @param listView the current list view
     * @return the customized TeamListCell
     */
    private ListCell<String> handleCellFactory(ListView<String> listView) {
        ListCell<String> cell = new TeamListCell(listView);

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
                ObservableList<String> items = listView.getItems();
                int draggedIndex = items.indexOf(db.getString());
                String draggedPerson = listView.getItems().remove(draggedIndex);

                int dropIndex = draggedIndex;

                if (!(cell.isEmpty() && cell.getIndex() != listView.getItems().size())) {
                    dropIndex = cell.getIndex();
                }

                listView.getItems().add(dropIndex, draggedPerson);

                event.setDropCompleted(true);
                listView.getSelectionModel().select(dropIndex);
                event.consume();
            }
        });
        return cell;
    }

    /**
     * Shows and links the context menu to the current cell in the list view
     *
     * @param event the current {@link MouseEvent}
     */
    private void handleMouseClicked(MouseEvent event) {
        if (contextMenu != null)
            contextMenu.hide();

        ListView<String> listView = (ListView<String>) event.getSource();

        if (event.getButton().equals(MouseButton.SECONDARY)) {
            contextMenu = new ContextMenu();
            MenuItem menuItem = new MenuItem("Team löschen");
            menuItem.setOnAction(ae -> deleteItem(listView, listView.getSelectionModel().getSelectedIndex()));
            MenuItem menuItem1 = new MenuItem("Team davor hinzufügen");
            menuItem1.setOnAction(ae -> addItem(listView, listView.getSelectionModel().getSelectedIndex()));
            MenuItem menuItem2 = new MenuItem("Team danach hinzufügen");
            menuItem2.setOnAction(ae -> addItem(listView, listView.getSelectionModel().getSelectedIndex() + 1));
            contextMenu.getItems().addAll(menuItem, menuItem1, menuItem2);
            contextMenu.show(listView, event.getScreenX(), event.getScreenY());
        }
    }

    /**
     * Deletes the given row/cell of the list view
     *
     * @param listView the current list view which holds the row/cell
     * @param index    the index of the current row/cell
     */
    private void deleteItem(ListView<String> listView, int index) {
        int groupIndex = getIndexViewByID(listView.getId());

        if (groupIndex != -1)
            groupsList.get(groupIndex).remove(index);
    }

    /**
     * Adds an item to the given list view at the given index
     *
     * @param listView the current list view which holds the row/cell
     * @param index the index of the position to add the item
     */
    private void addItem(ListView<String> listView, int index) {
        int groupIndex = getIndexViewByID(listView.getId());
        System.out.println("index: " + index);
        if (groupIndex != -1) {
            if (index == -1)
                index = 0;
            groupsList.get(groupIndex).add(index, "Name");
            listView.refresh();
            listView.getSelectionModel().select(index);
            listView.layout();
            listView.edit(index);
        }
    }

    /**
     * Sets the action event of the group menu button.
     */
    private void setupGroupButton() {
        groupBtnItem1.setOnAction(event1 -> showGroupList(1));
        groupBtnItem2.setOnAction(event1 -> showGroupList(2));
        groupBtnItem3.setOnAction(event1 -> showGroupList(3));
        groupBtnItem4.setOnAction(event1 -> showGroupList(4));
        groupBtnItem5.setOnAction(event1 -> showGroupList(5));
        groupBtnItem6.setOnAction(event1 -> showGroupList(6));
    }

    /**
     * Hides and shows the list views decided by count.
     *
     * @param count the number of list view which should be shown
     */
    private void showGroupList(int count) {
        if (groupCount == count || MAX_GROUP_COUNT < count)
            return;

        hBoxGroups.getChildren().clear();
        for (int iterator = 0; iterator < count; iterator++) {
            hBoxGroups.getChildren().add(vBoxList.get(iterator));
        }
        groupCount = count;
    }

    /**
     * Searches for the list view stored in the vBoxList and returns the index of it.
     *
     * @param id the String id of the list view which should be searched for
     * @return the index of the list view stored in the vBoxList
     */
    private int getIndexViewByID(String id) {
        ListIterator<VBox> vBoxListIterator = vBoxList.listIterator();

        while (vBoxListIterator.hasNext()) {
            int index = vBoxListIterator.nextIndex();
            VBox vBox = vBoxListIterator.next();
            ListView<String> listView = (ListView<String>) vBox.getChildren().get(1);
            if (listView.getId().equals(id))
                return index;
        }
        return -1;
    }

    /**
     * Overwritten {@link ListCell} class which handles the edit events.
     */
    private class TeamListCell extends ListCell<String> {
        private final TextField textField = new TextField();

        public TeamListCell(ListView<String> listView) {
            textField.setOnKeyPressed(e -> {

                if (e.getCode() == KeyCode.ESCAPE) {
                    cancelEdit();
                } else if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.TAB) {
                    if (listView.getSelectionModel().getSelectedItem().equals(""))
                        deleteItem(listView, getIndex());
                    listView.getSelectionModel().select(getIndex() + 1);
                }
            });
            textField.setOnAction(e -> {
                int groupIndex = getIndexViewByID(listView.getId());
                if (groupIndex == -1)
                    return;
                groupsList.get(groupIndex).remove(getIndex());
                groupsList.get(groupIndex).add(getIndex(), textField.getText());
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

    /**
     * Provides the first list view with some example data
     */
    private void exampleData() {
        ObservableList<String> group = FXCollections.observableArrayList(
                new Team("1. Team").getName(),
                new Team("2. Team").getName(),
                new Team("3. Team").getName(),
                new Team("4. Team").getName()
        );

        groupsList.add(group);
        groupList0.setItems(group);
    }

    /**
     * Shows the main stage again and closes the groupWindow stage
     * Also sends a ok signal back to the main stage
     *
     * @param event {@link ActionEvent}
     */
    @FXML
    void btnOkOnClicked(ActionEvent event) {
        Main.getStage().show();
        stage.close();
    }

    /**
     *
     *
     * @param event {@link ActionEvent}
     */
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
