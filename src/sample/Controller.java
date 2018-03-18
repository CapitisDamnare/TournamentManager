package sample;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML private Label rdyLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Load data");
        setRdyLabel("jup");
    }

    public void setRdyLabel(String text) {
        rdyLabel.setText(text);
    }
}
