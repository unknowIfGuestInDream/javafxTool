/*
 * Copyright (c) 2019, 2023 unknowIfGuestInDream
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of unknowIfGuestInDream, any associated website, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL UNKNOWIFGUESTINDREAM BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.tlcsdm.qe.tools;

import com.tlcsdm.core.javafx.util.Config;
import com.tlcsdm.core.javafx.util.FxmlUtil;
import com.tlcsdm.core.util.CoreConstant;
import com.tlcsdm.qe.QeSample;
import com.tlcsdm.qe.tools.dali.AbstractDaliConfigurationController;
import com.tlcsdm.qe.util.I18nUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.ResourceBundle;

/**
 * 测试用.
 *
 * @author unknowIfGuestInDream
 * @since 1.0.0
 */
public class DaliDemo extends QeSample {

    @FXML
    private Group grpSetting;
    @FXML
    private CheckBox enableLedModules, enableColourColtrol;
    @FXML
    private Button btnLedModules, btnColourControl;
    @FXML
    private CheckBox enablePushButtons, enableAbsoluteInputDevices, enableOccupancySensors, enableLightSensors;
    @FXML
    private Button btnPushButtons, btnAbsoluteInputDevices, btnOccupancySensors, btnLightSensors;
    @FXML
    private ImageView imgBoard;
    @FXML
    private ComboBox<String> cmbBoard, cmbCompiler;
    @FXML
    private TitledPane detailPane;

    private AbstractDaliConfigurationController daliConfigurationController;

    @Override
    public boolean isVisible() {
        String value = System.getProperty(CoreConstant.JVM_WORKENV);
        return CoreConstant.JVM_WORKENV_TEST.equals(value);
    }

    @Override
    public Node getPanel(Stage stage) {
        FXMLLoader fxmlLoader = FxmlUtil.loadFxmlFromResource(
            DaliDemo.class.getResource("/com/tlcsdm/qe/fxml/daliConfig.fxml"),
            ResourceBundle.getBundle(I18nUtils.getBasename(), Config.defaultLocale));
        return fxmlLoader.getRoot();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public String getSampleId() {
        return "daliConfig";
    }

    @Override
    public String getSampleName() {
        return "DALI Config";
    }

    @Override
    public String getOrderKey() {
        return "daliConfig";
    }

    @Override
    public String getSampleDescription() {
        return "此组件测试用";
    }

    @Override
    public String getSampleVersion() {
        return "1.0.0-Beta";
    }

    @Override
    public boolean hasControlPanel() {
        return false;
    }

    @FXML
    public void initialize() {
        initializeOption();
        initializeUI();

        initializeUserDataBindings();
        initializeBindings();
        initializeUserData();
    }

    private void initializeOption() {
        btnLedModules.disableProperty().bind(enableLedModules.selectedProperty().not());
        btnColourControl.disableProperty().bind(enableColourColtrol.selectedProperty().not());
        btnPushButtons.disableProperty().bind(enablePushButtons.selectedProperty().not());
        btnAbsoluteInputDevices.disableProperty().bind(enableAbsoluteInputDevices.selectedProperty().not());
        btnOccupancySensors.disableProperty().bind(enableOccupancySensors.selectedProperty().not());
        btnLightSensors.disableProperty().bind(enableLightSensors.selectedProperty().not());

        cmbBoard.getItems().add("EZ-0012");
        cmbBoard.getSelectionModel().select(0);
        cmbCompiler.getItems().addAll("Renesas CCRL", "ICC-RL", "LLVM");
        cmbCompiler.getSelectionModel().select(0);

        detailPane.setVisible(false);
    }

    private void initializeUI() {
        imgBoard.setImage(new Image(getClass().getResource("/com/tlcsdm/qe/static/QeTool.png").toExternalForm()));
    }

    @FXML
    public void showDetailPane(ActionEvent event) {
        if (!detailPane.isVisible()) {
            detailPane.setVisible(true);
        }
        if (event.getSource() instanceof Button button) {
            String text = button.getText();
            if ("Variable".equals(text)) {
                FXMLLoader fxmlLoader = FxmlUtil.loadFxmlFromResource(
                    DaliDemo.class.getResource("/com/tlcsdm/qe/fxml/dali/variable.fxml"));
                detailPane.setContent(fxmlLoader.getRoot());
                detailPane.setText("Variable");
                daliConfigurationController = fxmlLoader.getController();
            } else if ("Memory bank".equals(text)) {
                FXMLLoader fxmlLoader = FxmlUtil.loadFxmlFromResource(
                    DaliDemo.class.getResource("/com/tlcsdm/qe/fxml/dali/memoryBank.fxml"));
                detailPane.setContent(fxmlLoader.getRoot());
                detailPane.setText("Memory bank");
                daliConfigurationController = fxmlLoader.getController();
            } else {
                FXMLLoader fxmlLoader = FxmlUtil.loadFxmlFromResource(
                    DaliDemo.class.getResource("/com/tlcsdm/qe/fxml/dali/unsupport.fxml"));
                detailPane.setContent(fxmlLoader.getRoot());
                detailPane.setText(text);
                // null
                daliConfigurationController = fxmlLoader.getController();
            }
        }
    }
}
