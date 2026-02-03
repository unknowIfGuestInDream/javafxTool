/*
 * Copyright (c) 2024 unknowIfGuestInDream.
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

package com.tlcsdm.jfxcommon.tools.image;

import cn.hutool.log.StaticLog;
import com.tlcsdm.core.javafx.dialog.FxNotifications;
import com.tlcsdm.core.javafx.helper.LayoutHelper;
import com.tlcsdm.core.javafx.util.Config;
import com.tlcsdm.core.javafx.util.FileChooserUtil;
import com.tlcsdm.core.javafx.util.FxmlUtil;
import com.tlcsdm.core.javafx.util.ImgToolUtil;
import com.tlcsdm.core.javafx.util.OSUtil;
import com.tlcsdm.core.util.DependencyUtil;
import com.tlcsdm.jfxcommon.CommonSample;
import com.tlcsdm.jfxcommon.util.I18nUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.Notifications;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * 图片拆分工具.
 *
 * @author unknowIfGuestInDream
 * @since 1.0.1
 */
public class ImageSplit extends CommonSample {
    @FXML
    public TextField imagePathTextField;
    @FXML
    public Button imagePathButton;
    @FXML
    public TextField outputPathTextField;
    @FXML
    public Button outputPathButton;
    @FXML
    public ComboBox<String> analysisOrientationComboBox;
    @FXML
    public ComboBox<Integer> analysisNumberComboBox;
    @FXML
    public Button analysisImageButton;

    private Notifications notificationBuilder;
    //图片拆分方向
    private final String[] analysisOrientationComboBoxItems = {I18nUtils.get(
        "common.tool.image.imageSplit.combo.orientation.level"), I18nUtils.get(
        "common.tool.image.imageSplit.combo.orientation.vertical")};
    //图片拆分块数
    private final Integer[] analysisNumberComboBoxItems = {2, 3, 4, 5, 6, 7, 8, 9, 10};

    @Override
    public String getSampleId() {
        return "imageSplit";
    }

    @Override
    public String getSampleName() {
        return I18nUtils.get("common.tool.image.imageSplit.sampleName");
    }

    @Override
    public String getSampleVersion() {
        return "1.0.1";
    }

    @Override
    public String getSampleDescription() {
        return super.getSampleDescription();
    }

    @Override
    public boolean hasControlPanel() {
        return false;
    }

    @Override
    public Node getPanel(Stage stage) {
        FXMLLoader fxmlLoader = FxmlUtil.loadFxmlFromResource(
            ImageSplit.class.getResource("/com/tlcsdm/jfxcommon/fxml/imageSplit.fxml"),
            ResourceBundle.getBundle(I18nUtils.getBasename(), Config.defaultLocale));
        return fxmlLoader.getRoot();
    }

    @Override
    public Node getControlPanel() {
        return super.getControlPanel();
    }

    @Override
    public String getOrderKey() {
        return "imageSplit";
    }

    @Override
    public ImageView getSampleImageIcon() {
        return LayoutHelper.iconView(getClass().getResource("/com/tlcsdm/jfxcommon/static/icon/image-split.png"));
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public boolean isVisible() {
        return DependencyUtil.hasThumbnailator();
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
        notificationBuilder = FxNotifications.defaultNotify();
        analysisOrientationComboBox.getItems().addAll(analysisOrientationComboBoxItems);
        analysisOrientationComboBox.getSelectionModel().selectFirst();
        analysisNumberComboBox.getItems().addAll(analysisNumberComboBoxItems);
        analysisNumberComboBox.getSelectionModel().selectFirst();

        FileChooserUtil.setOnDrag(imagePathTextField, FileChooserUtil.FileType.FILE);
        FileChooserUtil.setOnDrag(outputPathTextField, FileChooserUtil.FileType.FOLDER);
    }

    private void initializeUI() {
        // Do nothing
    }

    @FXML
    private void imagePathButtonAction(ActionEvent event) {
        File file = FileChooserUtil.chooseFile();
        if (file != null) {
            imagePathTextField.setText(file.getPath());
        }
    }

    @FXML
    private void outputPathButtonAction(ActionEvent event) {
        File file = FileChooserUtil.chooseDirectory();
        if (file != null) {
            outputPathTextField.setText(file.getPath());
        }
    }

    @FXML
    private void analysisImageButtonAction(ActionEvent event) {
        String imagePathTextFieldString = imagePathTextField.getText();
        if (StringUtils.isBlank(imagePathTextFieldString)) {
            notificationBuilder.text(I18nUtils.get("common.tool.image.imageSplit.message.imagePathText"));
            notificationBuilder.showWarning();
            return;
        }
        String outputPathTextFieldString = outputPathTextField.getText();
        if (StringUtils.isBlank(outputPathTextFieldString)) {
            outputPathTextFieldString = FilenameUtils.getFullPath(imagePathTextFieldString);
        }
        String imageName = FilenameUtils.getBaseName(imagePathTextFieldString);
        String imageExtensionName = FilenameUtils.getExtension(imagePathTextFieldString);
        Integer analysisNumber = analysisNumberComboBox.getValue();
        String analysisOrientation = analysisOrientationComboBox.getValue();
        for (int i = 0; i < analysisNumber; i++) {
            try {
                ImgToolUtil imgToolTest = new ImgToolUtil(imagePathTextFieldString);
                if (I18nUtils.get(
                    "common.tool.image.imageSplit.combo.orientation.level").equals(analysisOrientation)) {
                    imgToolTest.cut(imgToolTest.width() / analysisNumber * i, 0, imgToolTest.width() / analysisNumber,
                        imgToolTest.height());
                } else {
                    imgToolTest.cut(0, imgToolTest.height() / analysisNumber * i, imgToolTest.width(),
                        imgToolTest.height() / analysisNumber);
                }
                imgToolTest.save(outputPathTextFieldString + File.separator + imageName + i + "." + imageExtensionName);

            } catch (IOException e) {
                notificationBuilder.text(I18nUtils.get("common.tool.image.imageSplit.message.error"));
                notificationBuilder.showError();
                StaticLog.error(e);
            }
        }
        OSUtil.openAndSelectedFile(outputPathTextFieldString);
    }

}
