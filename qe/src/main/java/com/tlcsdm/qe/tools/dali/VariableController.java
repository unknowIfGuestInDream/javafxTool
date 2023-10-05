package com.tlcsdm.qe.tools.dali;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author unknowIfGuestInDream
 */
public class VariableController implements Initializable {

    @FXML
    TabPane gearVariableTabPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (gearVariableTabPane.getTabs().size() != 3) {
            Tab tab1 = new Tab("Channel 1");
            tab1.setClosable(false);
            Tab tab2 = new Tab("Channel 2");
            tab2.setClosable(false);
            gearVariableTabPane.getTabs().addAll(tab1, tab2);
        }
    }
}
