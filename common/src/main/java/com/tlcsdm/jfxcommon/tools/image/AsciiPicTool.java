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

import com.tlcsdm.core.javafx.dialog.FxAlerts;
import com.tlcsdm.core.javafx.helper.LayoutHelper;
import com.tlcsdm.core.javafx.util.Config;
import com.tlcsdm.core.javafx.util.FileChooserUtil;
import com.tlcsdm.core.javafx.util.FxmlUtil;
import com.tlcsdm.core.javafx.util.ImageUtil;
import com.tlcsdm.core.util.DependencyUtil;
import com.tlcsdm.jfxcommon.CommonSample;
import com.tlcsdm.jfxcommon.util.I18nUtils;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Ascii 图片工具.
 *
 * @author unknowIfGuestInDream
 * @since 1.0.1
 */
public class AsciiPicTool extends CommonSample {
    @FXML
    public TextField filePathTextField;
    @FXML
    public Button filePathButton;
    @FXML
    public Button buildBannerButton;
    @FXML
    public Button buildBase4Button;
    @FXML
    public ComboBox<String> imageSizeComboBox;
    @FXML
    public ImageView imageImageView;
    @FXML
    public TextArea codeTextArea;
    @FXML
    public TextArea base64TextArea;

    private final String[] imageSizeItems = new String[]{I18nUtils.get(
        "common.tool.image.asciiPic.imageSizeItems.no"), "60*60", "120*120", "256*256", "512*512"};

    @Override
    public String getSampleId() {
        return "asciiPic";
    }

    @Override
    public String getSampleName() {
        return I18nUtils.get("common.tool.image.asciiPic.sampleName");
    }

    @Override
    public String getSampleVersion() {
        return "1.0.1";
    }

    @Override
    public String getSampleDescription() {
        return I18nUtils.get("common.tool.image.asciiPic.sampleDesc");
    }

    @Override
    public Node getPanel(Stage stage) {
        FXMLLoader fxmlLoader = FxmlUtil.loadFxmlFromResource(
            AsciiPicTool.class.getResource("/com/tlcsdm/jfxcommon/fxml/asciiPicTool.fxml"),
            ResourceBundle.getBundle(I18nUtils.getBasename(), Config.defaultLocale));
        return fxmlLoader.getRoot();
    }

    @Override
    public Node getControlPanel() {
        return super.getControlPanel();
    }

    @Override
    public String getOrderKey() {
        return "asciiPic";
    }

    @Override
    public ImageView getSampleImageIcon() {
        return LayoutHelper.iconView(getClass().getResource("/com/tlcsdm/jfxcommon/static/icon/asciiPic.png"));
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
        imageSizeComboBox.getItems().addAll(imageSizeItems);
        imageSizeComboBox.getSelectionModel().select(0);
        FileChooserUtil.setOnDrag(filePathTextField, FileChooserUtil.FileType.FILE);
        filePathTextField.textProperty().addListener(
            (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                try {
                    Image image = ImageUtil.getFXImage(newValue);
                    imageImageView.setImage(image);
                    imageImageView.setFitWidth(image.getWidth());
                    imageImageView.setFitHeight(image.getHeight());
                } catch (Exception e) {
                    FxAlerts.exception(e);
                }
            });
        codeTextArea.textProperty().addListener(
            (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                buildBase64ToImage(newValue);
            });
    }

    private void initializeUI() {
        // Do nothing
    }

    @FXML
    private void filePathAction(ActionEvent event) {
        File file = FileChooserUtil.chooseFile();
        if (file != null) {
            filePathTextField.setText(file.getPath());
        }
    }

    @FXML
    private void buildBannerAction(ActionEvent event) {
        String path = filePathTextField.getText();
        // 字符串由复杂到简单
        final String base = "@#&$%*o!;.";
        try {
            StringBuilder stringBuffer = new StringBuilder();
            BufferedImage image = ImageUtil.getBufferedImage(path);
            if (!I18nUtils.get("common.tool.image.asciiPic.imageSizeItems.no").equals(imageSizeComboBox.getValue())) {
                String[] size = imageSizeComboBox.getValue().split("\\*");
                image = Thumbnails.of(image).size(Integer.parseInt(size[0]), Integer.parseInt(size[1]))
                    .asBufferedImage();
            }
            for (int y = 0; y < image.getHeight(); y += 2) {
                for (int x = 0; x < image.getWidth(); x++) {
                    final int pixel = image.getRGB(x, y);
                    final int r = (pixel & 0xff0000) >> 16, g = (pixel & 0xff00) >> 8, b = pixel & 0xff;
                    final float gray = 0.299f * r + 0.578f * g + 0.114f * b;
                    final int index = Math.round(gray * (base.length() + 1) / 255);
                    stringBuffer.append(index >= base.length() ? " " : String.valueOf(base.charAt(index)));
                }
                stringBuffer.append("\n");
            }
            codeTextArea.setText(stringBuffer.toString());
        } catch (final IOException e) {
            FxAlerts.exception(e);
        }
    }

    @FXML
    private void buildBase64Action(ActionEvent event) {
        String path = filePathTextField.getText();
        try {
            String encodeBase64 = Base64.encodeBase64String(FileUtils.readFileToByteArray(new File(path)));
            base64TextArea.setText(encodeBase64);
        } catch (final IOException e) {
            FxAlerts.exception(e);
        }
    }

    public void buildBase64ToImage(String base64) {
        if (Base64.isBase64(base64)) {
            try {
                byte[] base64Byte = Base64.decodeBase64(base64);
                Image image = ImageUtil.getFXImage(base64Byte);
                imageImageView.setImage(image);
                imageImageView.setFitWidth(image.getWidth());
                imageImageView.setFitHeight(image.getHeight());
            } catch (Exception e) {
                FxAlerts.exception(e);
            }
        }
    }

}
