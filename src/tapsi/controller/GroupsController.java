package tapsi.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.util.Callback;
import tapsi.Main;
import tapsi.logic.Team;

import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class GroupsController implements Initializable, ControllerInterface {

    @FXML
    private ListView<Team> groupListView;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Setup of the groupListView an all Event- , Callback- handler.
     *
     * @param location {@link URL}
     * @param resources {@link ResourceBundle}
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupListView();
        exampleData();
    }

    private void setupListView() {
        groupListView.setEditable(true);
        groupListView.setCellFactory(lv -> new TeamListCell());
    }

    private void exampleData () {
        ObservableList<Team> group = FXCollections.observableArrayList(
                new Team("1. Team"),
                new Team("2. Team"),
                new Team("3. Team"),
                new Team("4. Team")
        );

        groupListView.setItems(group);
    }

    public class TeamListCell extends ListCell<Team> {
        private final TextField textField = new TextField();

        public TeamListCell() {
            textField.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.ESCAPE) {
                    cancelEdit();
                }
            });
            textField.setOnAction(e -> {
                getItem().setName(textField.getText());
                setText(textField.getText());
                setContentDisplay(ContentDisplay.TEXT_ONLY);
            });
            setGraphic(textField);
        }

        @Override
        protected void updateItem(Team team, boolean empty) {
            super.updateItem(team, empty);
            if (isEditing()) {
                textField.setText(team.getName());
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            } else {
                setContentDisplay(ContentDisplay.TEXT_ONLY);
                if (empty) {
                    setText(null);
                } else {
                    setText(team.getName());
                }
            }
        }

        @Override
        public void startEdit() {
            super.startEdit();
            textField.setText(getItem().getName());
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            textField.requestFocus();
            textField.selectAll();
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
            setText(getItem().getName());
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }
    }

    @FXML
    void btnOkOnClicked(ActionEvent event) {
        System.out.println("OK Clicked");
        Main.getStage().show();
        stage.close();
    }

    @FXML
    void btnAbortOnClick(ActionEvent event) {
        System.out.println("Abort Clicked");
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
