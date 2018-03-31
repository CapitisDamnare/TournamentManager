package tapsi.controller;

import com.sun.istack.internal.NotNull;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
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
    //private ObservableList<String> group;
    private List<ObservableList<String>> groupsList = new ArrayList<>();
    private ContextMenu contextMenu;
    private int groupCount = 0;
    private final int MAX_GROUP_COUNT = 6;
    private List<VBox> vBoxList;

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
        setupGroupButton();
    }

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
            VBox vBox = new VBox();
            Label label = new Label(nameListIterator.next());
            ListView<String> listView = new ListView<>();

            ObservableList<String> group = FXCollections.observableArrayList(
                    new Team("1. Team").getName()
            );
            groupsList.add(group);
            listView.setItems(group);

            setupListViewProperties(listView, vBox, label);
            vBox.getChildren().addAll(label, listView);

            vBoxList.add(vBox);
        }
    }

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

    private void deleteItem(ListView<String> listView, int index) {
        int groupIndex = getIndexViewByID(listView.getId());

        if (groupIndex != 0)
            groupsList.get(groupIndex).remove(index);
    }

    private void addItem(ListView<String> listView, int index) {
        int groupIndex = getIndexViewByID(listView.getId());

        if (groupIndex != 0) {
            groupsList.get(groupIndex).add(index, "Name");
            listView.refresh();
            listView.getSelectionModel().select(index);
            listView.layout();
            listView.edit(index);
        }
    }

    private void setupGroupButton() {
        groupBtnItem1.setOnAction(event1 -> showGroupList(0));
        groupBtnItem2.setOnAction(event1 -> showGroupList(1));
        groupBtnItem3.setOnAction(event1 -> showGroupList(2));
        groupBtnItem4.setOnAction(event1 -> showGroupList(3));
        groupBtnItem5.setOnAction(event1 -> showGroupList(4));
        groupBtnItem6.setOnAction(event1 -> showGroupList(5));
    }

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

        listView.setEditable(true);
        listView.nodeOrientationProperty().setValue(NodeOrientation.INHERIT);
        listView.setMinWidth(Region.USE_COMPUTED_SIZE);
        listView.setMinHeight(Region.USE_COMPUTED_SIZE);
        listView.setMaxWidth(Region.USE_COMPUTED_SIZE);
        listView.setMaxHeight(Region.USE_COMPUTED_SIZE);
        listView.setPrefWidth(Region.USE_COMPUTED_SIZE);
        listView.setPrefHeight(Region.USE_COMPUTED_SIZE);

        listView.setCellFactory(lv -> handleCellFactory(lv));

        EventHandler<MouseEvent> mouseEventHandle = (MouseEvent event) -> {
            handleMouseClicked(event);
        };

        listView.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventHandle);
    }

    private void showGroupList(int count) {
        if (groupCount == count || MAX_GROUP_COUNT == count)
            return;

        hBoxGroups.getChildren().clear();
        hBoxGroups.getChildren().add(mainVBox);

        for (int iterator = 0; iterator < count; iterator++) {
            hBoxGroups.getChildren().add(vBoxList.get(iterator));
        }
        groupCount = count;
    }

    private int getIndexViewByID(String id) {
        ListIterator<VBox> vBoxListIterator = vBoxList.listIterator();

        while (vBoxListIterator.hasNext()) {
            int index = vBoxListIterator.nextIndex();
            VBox vBox = vBoxListIterator.next();
            ListView<String> listView = (ListView<String>) vBox.getChildren().get(1);
            System.out.println("getID: " + listView.getId());
            if (listView.getId().equals(id))
                return index;
        }
        return 0;
    }

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
                // Todo: Check here... there is something wrong ... i get the wrong listview and wrong group to the listview
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
