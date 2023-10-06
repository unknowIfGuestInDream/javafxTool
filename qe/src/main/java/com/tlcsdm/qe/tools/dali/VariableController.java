package com.tlcsdm.qe.tools.dali;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author unknowIfGuestInDream
 */
public class VariableController extends AbstractDaliConfigurationController implements Initializable {

    @FXML
    TabPane gearVariableTabPane;
    @FXML
    private TextField txtVersionNumber, txtPhm, txtPowerOnLevel, txtSysFailLevel, txtMinLevel, txtMaxLevel, txtShortAddress,
        txtRandomAddressH, txtRandomAddressM, txtRandomAddressL;
    @FXML
    private ComboBox<String> cmbExtendFadeTimeMulti, cmbOperatingMode;
    @FXML
    private ComboBox<FadeData> cmbFadeTime, cmbFadeRate, cmbExtendFadeTimeBase;
    @FXML
    private CheckBox enableGroup0, enableGroup1, enableGroup2, enableGroup3, enableGroup4, enableGroup5, enableGroup6,
        enableGroup7, enableGroup8, enableGroup9, enableGroup10, enableGroup11, enableGroup12, enableGroup13, enableGroup14, enableGroup15;
    @FXML
    private TextField txtScene0, txtScene1, txtScene2, txtScene3, txtScene4, txtScene5, txtScene6, txtScene7, txtScene8,
        txtScene9, txtScene10, txtScene11, txtScene12, txtScene13, txtScene14, txtScene15;
    @FXML
    private CheckBox enableScene0, enableScene1, enableScene2, enableScene3, enableScene4, enableScene5, enableScene6,
        enableScene7, enableScene8, enableScene9, enableScene10, enableScene11, enableScene12, enableScene13, enableScene14, enableScene15;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeOption();
        initializeUI();
    }

    private void initializeOption() {
        txtScene0.disableProperty().bind(enableScene0.selectedProperty().not());
        txtScene1.disableProperty().bind(enableScene1.selectedProperty().not());
        txtScene2.disableProperty().bind(enableScene2.selectedProperty().not());
        txtScene3.disableProperty().bind(enableScene3.selectedProperty().not());
        txtScene4.disableProperty().bind(enableScene4.selectedProperty().not());
        txtScene5.disableProperty().bind(enableScene5.selectedProperty().not());
        txtScene6.disableProperty().bind(enableScene6.selectedProperty().not());
        txtScene7.disableProperty().bind(enableScene7.selectedProperty().not());
        txtScene8.disableProperty().bind(enableScene8.selectedProperty().not());
        txtScene9.disableProperty().bind(enableScene9.selectedProperty().not());
        txtScene10.disableProperty().bind(enableScene10.selectedProperty().not());
        txtScene11.disableProperty().bind(enableScene11.selectedProperty().not());
        txtScene12.disableProperty().bind(enableScene12.selectedProperty().not());
        txtScene13.disableProperty().bind(enableScene13.selectedProperty().not());
        txtScene14.disableProperty().bind(enableScene14.selectedProperty().not());
        txtScene15.disableProperty().bind(enableScene15.selectedProperty().not());

        cmbOperatingMode.getItems().addAll("0");
        cmbOperatingMode.getSelectionModel().select(0);

        cmbFadeTime.getItems().addAll(new FadeData(0, "no fade", "0"), new FadeData(1, "0.7sec", "0.707"),
            new FadeData(2, "1.0sec", "1.000"), new FadeData(3, "1.4sec", "1.414"), new FadeData(4, "2.0sec", "2.000"),
            new FadeData(5, "2.8sec", "2.828"), new FadeData(6, "4.0sec", "4.000"), new FadeData(7, "5.7sec", "5.657"),
            new FadeData(8, "8.0sec", "8.000"), new FadeData(9, "11.3sec", "11.314"),
            new FadeData(10, "16.0sec", "16.000"), new FadeData(11, "22.6sec", "22.627"),
            new FadeData(12, "32.0sec", "32.000"), new FadeData(13, "45.3sec", "45.255"),
            new FadeData(14, "64.0sec", "64.000"), new FadeData(15, "90.5sec", "90.510"));
        cmbFadeTime.getSelectionModel().select(0);

        cmbFadeRate.getItems().addAll(new FadeData(1, "357.8steps/sec", "357.796"),
            new FadeData(2, "253steps/sec", "253.000"), new FadeData(3, "179steps/sec", "178.898"),
            new FadeData(4, "127steps/sec", "126.500"), new FadeData(5, "89.4steps/sec", "89.449"),
            new FadeData(6, "63.3steps/sec", "63.250"), new FadeData(7, "44.7steps/sec", "44.725"),
            new FadeData(8, "31.6steps/sec", "31.625"), new FadeData(9, "22.4steps/sec", "22.362"),
            new FadeData(10, "15.8steps/sec", "15.813"), new FadeData(11, "11.2steps/sec", "11.181"),
            new FadeData(12, "7.9steps/sec", "7.906"), new FadeData(13, "5.6steps/sec", "5.591"),
            new FadeData(14, "4.0steps/sec", "3.953"), new FadeData(15, "2.8steps/sec", "2.795"));
        cmbFadeRate.getSelectionModel().select(6);

        cmbExtendFadeTimeBase.getItems().addAll(new FadeData(1, "", "0000"), new FadeData(1, "", "0000"),
            new FadeData(2, "", "0001"), new FadeData(3, "", "0010"), new FadeData(4, "", "0011"),
            new FadeData(5, "", "0100"), new FadeData(6, "", "0101"), new FadeData(7, "", "0110"),
            new FadeData(8, "", "0111"), new FadeData(9, "", "1000"), new FadeData(10, "", "1001"),
            new FadeData(11, "", "1010"), new FadeData(12, "", "1011"), new FadeData(13, "", "1100"),
            new FadeData(14, "", "1101"), new FadeData(15, "", "1110"), new FadeData(16, "", "1111"));
        cmbExtendFadeTimeBase.getSelectionModel().select(0);

        cmbExtendFadeTimeMulti.getItems().addAll("0 ms", "100 ms", "1 s", "10 s", "1 min");
        cmbExtendFadeTimeMulti.getSelectionModel().select(0);

        setIntFormatter(txtPhm, txtPowerOnLevel, txtSysFailLevel, txtMinLevel, txtMaxLevel, txtShortAddress, txtRandomAddressH,
            txtRandomAddressM, txtRandomAddressL, txtScene0, txtScene1, txtScene2, txtScene3, txtScene4, txtScene5, txtScene6,
            txtScene7, txtScene8, txtScene9, txtScene10, txtScene11, txtScene12, txtScene13, txtScene14, txtScene15);
    }

    private void initializeUI() {
        if (gearVariableTabPane.getTabs().size() != 3) {
            Tab tab1 = new Tab("Channel 1");
            tab1.setClosable(false);
            Tab tab2 = new Tab("Channel 2");
            tab2.setClosable(false);
            gearVariableTabPane.getTabs().addAll(tab1, tab2);
        }
        gearVariableTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(newValue.getText());
        });
    }

    record FadeData(int setting, String desc, String value) {

        @Override
        public String toString() {
            if (desc == null || desc.isEmpty()) {
                return String.valueOf(setting);
            }
            return setting + "(" + desc + ")";
        }
    }

}
