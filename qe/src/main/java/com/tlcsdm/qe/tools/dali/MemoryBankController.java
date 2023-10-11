package com.tlcsdm.qe.tools.dali;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;

/**
 * @author unknowIfGuestInDream
 */
public class MemoryBankController extends AbstractDaliConfigurationController {
    @FXML
    private TabPane channelPane;
    @FXML
    private TextField txt0AddrOfLastMemoryLocation, txt0AddrOfLastMemoryBank, txt0GtinByte0, txt0GtinByte1, txt0GtinByte2,
        txt0GtinByte3, txt0GtinByte4, txt0GtinByte5, txt0FirmwareMajorVersion, txt0FirmwareMinorVersion, txt0VersionNum101,
        txt0VersionNum102, txt0VersionNum103, txt0LogicalControlGearNum, txt0LogicalControlDeviceNum, txt0IndentifyNumberByte0,
        txt0IndentifyNumberByte1, txt0IndentifyNumberByte2, txt0IndentifyNumberByte3, txt0IndentifyNumberByte4,
        txt0IndentifyNumberByte5, txt0IndentifyNumberByte6, txt0IndentifyNumberByte7, txt0HardwareMajorVersion, txt0HardwareMinorVersion,
        txt0CurrentBusUnit, txt0LogicalControlGearIndex;
    @FXML
    private CheckBox enable0CurrentBusUnit;
    @FXML
    private CheckBox enableMemoryBank1, enable1IndecatorType;
    @FXML
    private TextField txt1AddrOfLastMemoryLocation, txt1IndecatorType, txt1OemGtinByte0, txt1OemGtinByte1, txt1OemGtinByte2,
        txt1OemGtinByte3, txt1OemGtinByte4, txt1OemGtinByte5, txt1OemIndentifyNumberByte0, txt1OemIndentifyNumberByte1,
        txt1OemIndentifyNumberByte2, txt1OemIndentifyNumberByte3, txt1OemIndentifyNumberByte4, txt1OemIndentifyNumberByte5,
        txt1OemIndentifyNumberByte6, txt1OemIndentifyNumberByte7;

    @FXML
    public void initialize() {
        initializeOption();
        initializeUI();
    }

    private void initializeOption() {
        txt0CurrentBusUnit.disableProperty().bind(enable0CurrentBusUnit.selectedProperty().not());

        txt1AddrOfLastMemoryLocation.disableProperty().bind(enableMemoryBank1.selectedProperty().not());
        enable1IndecatorType.disableProperty().bind(enableMemoryBank1.selectedProperty().not());
        txt1IndecatorType.disableProperty().bind(enableMemoryBank1.selectedProperty().not().or(enable1IndecatorType.selectedProperty().not()));
        txt1OemGtinByte0.disableProperty().bind(enableMemoryBank1.selectedProperty().not());
        txt1OemGtinByte1.disableProperty().bind(enableMemoryBank1.selectedProperty().not());
        txt1OemGtinByte2.disableProperty().bind(enableMemoryBank1.selectedProperty().not());
        txt1OemGtinByte3.disableProperty().bind(enableMemoryBank1.selectedProperty().not());
        txt1OemGtinByte4.disableProperty().bind(enableMemoryBank1.selectedProperty().not());
        txt1OemGtinByte5.disableProperty().bind(enableMemoryBank1.selectedProperty().not());
        txt1OemIndentifyNumberByte0.disableProperty().bind(enableMemoryBank1.selectedProperty().not());
        txt1OemIndentifyNumberByte1.disableProperty().bind(enableMemoryBank1.selectedProperty().not());
        txt1OemIndentifyNumberByte2.disableProperty().bind(enableMemoryBank1.selectedProperty().not());
        txt1OemIndentifyNumberByte3.disableProperty().bind(enableMemoryBank1.selectedProperty().not());
        txt1OemIndentifyNumberByte4.disableProperty().bind(enableMemoryBank1.selectedProperty().not());
        txt1OemIndentifyNumberByte5.disableProperty().bind(enableMemoryBank1.selectedProperty().not());
        txt1OemIndentifyNumberByte6.disableProperty().bind(enableMemoryBank1.selectedProperty().not());
        txt1OemIndentifyNumberByte7.disableProperty().bind(enableMemoryBank1.selectedProperty().not());
    }

    private void initializeUI() {
        if (channelPane.getTabs().size() != 3) {
            Tab tab1 = new Tab("Channel 1");
            tab1.setClosable(false);
            Tab tab2 = new Tab("Channel 2");
            tab2.setClosable(false);
            channelPane.getTabs().addAll(tab1, tab2);
        }
        channelPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(newValue.getText());
        });
    }
}
