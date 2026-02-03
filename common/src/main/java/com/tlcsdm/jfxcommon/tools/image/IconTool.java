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

import cn.hutool.core.io.FileUtil;
import cn.hutool.log.StaticLog;
import com.tlcsdm.core.exception.UnExpectedResultException;
import com.tlcsdm.core.javafx.dialog.FxAlerts;
import com.tlcsdm.core.javafx.dialog.FxNotifications;
import com.tlcsdm.core.javafx.helper.ImageViewHelper;
import com.tlcsdm.core.javafx.util.Config;
import com.tlcsdm.core.javafx.util.FileChooserUtil;
import com.tlcsdm.core.javafx.util.FxmlUtil;
import com.tlcsdm.core.javafx.util.ImageUtil;
import com.tlcsdm.core.javafx.util.ImgToolUtil;
import com.tlcsdm.core.javafx.util.OSUtil;
import com.tlcsdm.core.util.DependencyUtil;
import com.tlcsdm.jfxcommon.CommonSample;
import com.tlcsdm.jfxcommon.util.I18nUtils;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.Notifications;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

/**
 * 图标生成工具.
 *
 * @author unknowIfGuestInDream
 * @since 1.0.1
 */
public class IconTool extends CommonSample {

    @FXML
    protected TextField iconFilePathTextField;
    @FXML
    protected Button chooseOriginalPathButton;
    @FXML
    protected TextField iconTargetPathTextField;
    @FXML
    protected Button chooseTargetPathButton;
    @FXML
    protected CheckBox isCornerCheckBox;
    @FXML
    protected Slider cornerSizeSlider;
    @FXML
    protected CheckBox isKeepAspectRatioCheckBox;
    @FXML
    protected ChoiceBox<String> iconFormatChoiceBox;
    @FXML
    protected TextField iconNameTextField;
    @FXML
    protected CheckBox iosIconCheckBox;
    @FXML
    protected CheckBox androidCheckBox;
    @FXML
    protected Button buildIconButton;
    @FXML
    protected ImageView iconImageView;
    @FXML
    protected Button buildIconTargetImageButton;
    @FXML
    protected ImageView iconTargetImageView;
    @FXML
    protected Spinner<Integer> widthSpinner;
    @FXML
    protected Spinner<Integer> heightSpinner;
    @FXML
    protected Button addSizeButton;
    @FXML
    protected Button resettingSizeButton;
    @FXML
    protected Button allSelectButton;
    @FXML
    protected Button inverseButton;
    @FXML
    protected Button allNotSelectButton;
    @FXML
    protected FlowPane iconSizeFlowPane;
    @FXML
    protected CheckBox isWatermarkCheckBox;
    @FXML
    protected TextField watermarkPathTextField;
    @FXML
    protected Button selectWatermarkButton;
    @FXML
    protected ImageView watermarkImageView;
    @FXML
    protected ChoiceBox<Positions> watermarkPositionChoiceBox;
    @FXML
    protected Slider watermarkOpacitySlider;
    @FXML
    protected Slider outputQualitySlider;

    private final String[] iconSizeStrings = new String[]{"16*16", "20*20", "28*28", "29*29", "32*32", "36*36", "40*40",
        "48*48", "50*50", "57*57", "58*58", "60*60", "64*64", "72*72", "76*76", "80*80", "87*87", "90*90", "96*96",
        "100*100", "108*108", "114*114", "120*120", "128*128", "144*144", "152*152", "155*155", "167*167",
        "180*180", "192*192", "256*256", "512*512"};
    private final String[] iconFormatStrings = new String[]{"png", "jpg", "gif", "jpeg", "bmp", "ico", "icns", "xpm"};
    private final Positions[] watermarkPositions = Positions.values();
    private Notifications notificationBuilder;

    @Override
    public String getSampleId() {
        return "iconTool";
    }

    @Override
    public String getSampleName() {
        return I18nUtils.get("common.tool.image.iconTool.sampleName");
    }

    @Override
    public String getSampleVersion() {
        return "1.0.1";
    }

    @Override
    public String getSampleDescription() {
        return I18nUtils.get("common.tool.image.iconTool.sampleDesc");
    }

    @Override
    public Node getPanel(Stage stage) {
        FXMLLoader fxmlLoader = FxmlUtil.loadFxmlFromResource(
            IconTool.class.getResource("/com/tlcsdm/jfxcommon/fxml/iconTool.fxml"),
            ResourceBundle.getBundle(I18nUtils.getBasename(), Config.defaultLocale));
        return fxmlLoader.getRoot();
    }

    @Override
    public Node getControlPanel() {
        return super.getControlPanel();
    }

    @Override
    public String getOrderKey() {
        return "iconTool";
    }

    @Override
    public ImageView getSampleImageIcon() {
        return ImageViewHelper.get("icon");
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
        resettingSize();
        setSliderLabelFormatter(watermarkOpacitySlider, "0.0");
        setSliderLabelFormatter(outputQualitySlider, "0.0");
        setSpinnerValueFactory(widthSpinner, 1, Integer.MAX_VALUE, 1, 1);
        setSpinnerValueFactory(heightSpinner, 1, Integer.MAX_VALUE, 1, 1);
        iconFormatChoiceBox.getItems().addAll(iconFormatStrings);
        iconFormatChoiceBox.setValue(iconFormatStrings[0]);
        watermarkPositionChoiceBox.getItems().addAll(watermarkPositions);
        watermarkPositionChoiceBox.setValue(watermarkPositions[8]);
    }

    private void initializeUI() {
        FileChooserUtil.setOnDrag(iconFilePathTextField, FileChooserUtil.FileType.FILE);
        FileChooserUtil.setOnDrag(iconTargetPathTextField, FileChooserUtil.FileType.FOLDER);
        FileChooserUtil.setOnDrag(watermarkPathTextField, FileChooserUtil.FileType.FILE);
        for (Node node : iconSizeFlowPane.getChildren()) {
            ((CheckBox) node).selectedProperty().addListener((observable, oldValue, newValue) -> {
                boolean isAllSelect = true;
                for (Node node1 : iconSizeFlowPane.getChildren()) {
                    if (!((CheckBox) node1).isSelected()) {
                        isAllSelect = false;
                    }
                }
                setSelectButtonDisable(isAllSelect);
            });
        }
        iconFilePathTextField.textProperty().addListener((arg0, oldValue, newValue) -> {
            if (StringUtils.isEmpty(newValue)) {
                iconImageView.setImage(null);
                return;
            }
            Image image = ImageUtil.getFXImage(newValue);
            iconImageView.setImage(image);
        });
        watermarkPathTextField.textProperty().addListener((arg0, oldValue, newValue) -> {
            if (StringUtils.isEmpty(newValue)) {
                watermarkImageView.setImage(null);
                return;
            }
            Image image = ImageUtil.getFXImage(newValue);
            watermarkImageView.setImage(image);
        });
    }

    /**
     * 选择图标图片.
     */
    @FXML
    private void chooseOriginalPathAction(ActionEvent event) {
        File file = FileChooserUtil.chooseFile(new FileChooser.ExtensionFilter("All Images", "*.*"),
            new FileChooser.ExtensionFilter("JPG", "*.jpg"), new FileChooser.ExtensionFilter("PNG", "*.png"),
            new FileChooser.ExtensionFilter("gif", "*.gif"), new FileChooser.ExtensionFilter("jpeg", "*.jpeg"),
            new FileChooser.ExtensionFilter("bmp", "*.bmp"));
        if (file != null) {
            iconFilePathTextField.setText(file.getPath());
            Image image = ImageUtil.getFXImage(file);
            iconImageView.setImage(image);
        }
    }

    /**
     * 选择生成目录（为空则为原图片目录）
     */
    @FXML
    private void chooseTargetPathAction(ActionEvent event) {
        File file = FileChooserUtil.chooseDirectory();
        if (file != null) {
            iconTargetPathTextField.setText(file.getPath());
        }
    }

    @FXML
    private void addSizeAction(ActionEvent event) {
        addSizeAction(widthSpinner.getValue() + "*" + heightSpinner.getValue());
    }

    /**
     * 重置图片尺寸选择CheckBox
     */
    @FXML
    private void resettingSizeAction(ActionEvent event) {
        resettingSize();
    }

    /**
     * 生成图片
     */
    @FXML
    private void buildIconAction(ActionEvent event) {
        try {
            String iconFilePathText = iconFilePathTextField.getText();
            String iconTargetPathText = iconTargetPathTextField.getText();
            String iconFormat = iconFormatChoiceBox.getValue();
            if (StringUtils.isEmpty(iconFilePathText)) {
                notificationBuilder.text("图标未选择！");
                notificationBuilder.showWarning();
                return;
            }
            File iconFilePathFile = new File(iconFilePathText);
            if (!iconFilePathFile.exists()) {
                notificationBuilder.text("图标加载失败！");
                notificationBuilder.showError();
                return;
            }
            File iconTargetPathFile;
            if (StringUtils.isEmpty(iconTargetPathText)) {
                iconTargetPathFile = new File(iconFilePathText).getParentFile();
            } else {
                iconTargetPathFile = new File(iconTargetPathText);
            }
            String iconFileName = FileUtil.getName(iconFilePathFile);
            if (StringUtils.isNotEmpty(iconNameTextField.getText())) {
                iconFileName = iconNameTextField.getText();
            }

            for (Node node : iconSizeFlowPane.getChildren()) {
                CheckBox checkBox = ((CheckBox) node);
                if (checkBox.isSelected()) {
                    String[] checkBoxText = checkBox.getText().split("\\*");
                    int width = Integer.parseInt(checkBoxText[0]);
                    int height = Integer.parseInt(checkBoxText[1]);
                    String fileName = iconFileName + "-" + width;
                    BufferedImage bufferedImage = getBufferedImage(iconFilePathFile, width, height);
                    ImageUtil.writeImage(bufferedImage,
                        new File(iconTargetPathFile.getPath(), fileName + "." + iconFormat));
                }
            }
            if (iosIconCheckBox.isSelected()) {
                File appIconAppiconsetPathFile = new File(iconTargetPathFile.getPath() + "/ios/AppIcon.appiconset/");
                File contentsFile = new File(Objects.requireNonNull(
                        IconTool.class.getResource("/com/tlcsdm/jfxcommon/static/data/iosAppIcon/Contents.json"))
                    .getFile());
                FileUtils.copyFileToDirectory(contentsFile, appIconAppiconsetPathFile);
                BufferedImage bufferedImage512 = getBufferedImage(iconFilePathFile, 512, 512);
                ImageUtil.writeImage(bufferedImage512,
                    new File(iconTargetPathFile.getPath() + "/ios", "iTunesArtwork.png"));
                BufferedImage bufferedImage1024 = getBufferedImage(iconFilePathFile, 1024, 1024);
                ImageUtil.writeImage(bufferedImage1024,
                    new File(iconTargetPathFile.getPath() + "/ios", "iTunesArtwork@2x.png"));
            }
            if (androidCheckBox.isSelected()) {
                File appIconAppiconsetPathFile = new File(iconTargetPathFile.getPath() + "/android/");
                appIconAppiconsetPathFile.mkdirs();
                BufferedImage bufferedImage512 = getBufferedImage(iconFilePathFile, 512, 512);
                ImageUtil.writeImage(bufferedImage512, new File(appIconAppiconsetPathFile, "ic_launcher.png"));
                final String iconTargetPathFileString = iconTargetPathFile.getPath() + "/android/";
                Map<String, Integer> sizeMap = new HashMap<String, Integer>();
                sizeMap.put("mipmap-hdpi", 72);
                sizeMap.put("mipmap-ldpi", 36);
                sizeMap.put("mipmap-mdpi", 48);
                sizeMap.put("mipmap-xhdpi", 96);
                sizeMap.put("mipmap-xxhdpi", 114);
                sizeMap.put("mipmap-xxxhdpi", 192);
                sizeMap.forEach((key, value) -> {
                    try {
                        new File(iconTargetPathFileString + key).mkdirs();
                        BufferedImage bufferedImage = getBufferedImage(iconFilePathFile, value, value);
                        ImageUtil.writeImage(bufferedImage,
                            new File(iconTargetPathFileString + key, "ic_launcher.png"));
                    } catch (Exception e) {
                        StaticLog.error(e);
                    }
                });
            }
            notificationBuilder.text("生成图片成功");
            notificationBuilder.showInformation();
            OSUtil.openAndSelectedFile(iconTargetPathFile.getPath());
        } catch (IOException e) {
            FxAlerts.exception(e);
        }
    }

    /**
     * 生成预览图片
     */
    @FXML
    private void buildIconTargetImageAction(ActionEvent event) {
        try {
            String iconFilePathText = iconFilePathTextField.getText();
            if (StringUtils.isEmpty(iconFilePathText)) {
                notificationBuilder.text("图标未选择");
                notificationBuilder.showWarning();
                return;
            }
            File iconFilePathFile = new File(iconFilePathText);
            if (!iconFilePathFile.exists()) {
                notificationBuilder.text("图标加载失败");
                notificationBuilder.showError();
                return;
            }
            BufferedImage bufferedImage = getBufferedImage(iconFilePathFile, 150, 150);
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            iconTargetImageView.setImage(image);
        } catch (IOException | UnExpectedResultException e) {
            FxAlerts.exception(e);
        }
    }

    @FXML
    private void allSelectAction(ActionEvent event) {
        for (Node node : iconSizeFlowPane.getChildren()) {
            ((CheckBox) node).setSelected(true);
        }
        setSelectButtonDisable(true);
    }

    @FXML
    private void inverseAction(ActionEvent event) {
        for (Node node : iconSizeFlowPane.getChildren()) {
            CheckBox checkBox = ((CheckBox) node);
            checkBox.setSelected(!checkBox.isSelected());
        }
        allSelectButton.setDisable(false);
        inverseButton.setDisable(false);
        allNotSelectButton.setDisable(false);
    }

    @FXML
    private void allNotSelectAction(ActionEvent event) {
        for (Node node : iconSizeFlowPane.getChildren()) {
            ((CheckBox) node).setSelected(false);
        }
        setSelectButtonDisable(false);
    }

    @FXML
    private void selectWatermarkAction(ActionEvent event) {
        File file = FileChooserUtil.chooseFile(new FileChooser.ExtensionFilter("All Images", "*.*"),
            new FileChooser.ExtensionFilter("JPG", "*.jpg"), new FileChooser.ExtensionFilter("PNG", "*.png"),
            new FileChooser.ExtensionFilter("gif", "*.gif"), new FileChooser.ExtensionFilter("jpeg", "*.jpeg"),
            new FileChooser.ExtensionFilter("bmp", "*.bmp"));
        if (file != null) {
            try {
                watermarkPathTextField.setText(file.getPath());
                Image image = ImageUtil.getFXImage(file);
                watermarkImageView.setImage(image);
            } catch (Exception e) {
                notificationBuilder.text("水印图片加载异常");
                notificationBuilder.showError();
            }
        }
    }

    /**
     * 设置按钮状态
     */
    private void setSelectButtonDisable(boolean isAllSelect) {
        if (isAllSelect) {
            allSelectButton.setDisable(true);
            inverseButton.setDisable(true);
            allNotSelectButton.setDisable(false);
        } else {
            allSelectButton.setDisable(false);
            inverseButton.setDisable(false);
            allNotSelectButton.setDisable(true);
        }
    }

    /**
     * 重置图片尺寸选择CheckBox
     */
    public void resettingSize() {
        iconSizeFlowPane.getChildren().clear();
        for (String iconSizeString : iconSizeStrings) {
            addSizeAction(iconSizeString);
        }
    }

    public void addSizeAction(String text) {
        addSizeAction(text, true);
    }

    /**
     * 添加图片尺寸选择CheckBox
     */
    public void addSizeAction(String text, boolean isSelect) {
        CheckBox checkBox = new CheckBox(text);
        checkBox.setPrefWidth(90);
        checkBox.setSelected(isSelect);
        checkBox.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                MenuItem menu_Remove = new MenuItem("删除选中尺寸");
                menu_Remove.setOnAction(event1 -> {
                    iconSizeFlowPane.getChildren().remove(checkBox);
                });
                checkBox.setContextMenu(new ContextMenu(menu_Remove));
            }
        });
        iconSizeFlowPane.getChildren().add(checkBox);
    }

    /**
     * 根据条件获取生成的图片
     */
    private BufferedImage getBufferedImage(File iconFilePathFile, int width, int height) throws
        UnExpectedResultException, IOException {
        Thumbnails.Builder<BufferedImage> builderFile = Thumbnails.of(ImageUtil.getBufferedImage(iconFilePathFile));
        builderFile.size(width, height).keepAspectRatio(isKeepAspectRatioCheckBox.isSelected())
            .outputFormat(iconFormatChoiceBox.getValue())
            .outputQuality(outputQualitySlider.getValue());
        if (isWatermarkCheckBox.isSelected()) {
            String watermarkPathText = watermarkPathTextField.getText();
            if (StringUtils.isNotEmpty(watermarkPathText)) {
                try {
                    builderFile.watermark(watermarkPositionChoiceBox.getValue(),
                        ImageIO.read(new File(watermarkPathText)),
                        (float) watermarkOpacitySlider.getValue());
                } catch (Exception e) {
                    throw new UnExpectedResultException("The watermark image loads abnormally！");
                }
            }
        }
        BufferedImage bufferedImage = builderFile.asBufferedImage();
        if (isCornerCheckBox.isSelected()) {
            ImgToolUtil imgToolUtil = new ImgToolUtil(bufferedImage);
            imgToolUtil.corner((float) cornerSizeSlider.getValue());
            bufferedImage = imgToolUtil.getImage();
        }
        return bufferedImage;
    }

    public void setSliderLabelFormatter(Slider slider, String formatter) {
        slider.setLabelFormatter(new StringConverter<>() {
            @Override
            public String toString(Double object) {
                DecimalFormat decimalFormat = new DecimalFormat(formatter);
                return decimalFormat.format(object);
            }

            @Override
            public Double fromString(String string) {
                return Double.valueOf(string);
            }
        });
    }

    public static void setSpinnerValueFactory(Spinner spinner, Number min, Number max, Number initialValue,
        Number amountToStepBy) {
        if (min instanceof Integer) {
            SpinnerValueFactory.IntegerSpinnerValueFactory secondStart_0svf = new SpinnerValueFactory.IntegerSpinnerValueFactory(
                (int) min, (int) max,
                (int) initialValue, (int) amountToStepBy);
            spinner.setValueFactory(secondStart_0svf);
            spinner.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    spinner.getValueFactory().setValue(Integer.parseInt(newValue));
                } catch (Exception e) {
                    StaticLog.warn("Digit int conversion is abnormal. newValue:" + newValue);
                    spinner.getEditor().setText(oldValue);
                }
            });
        } else if (min instanceof Double) {
            SpinnerValueFactory.DoubleSpinnerValueFactory secondStart_0svf = new SpinnerValueFactory.DoubleSpinnerValueFactory(
                (double) min, (double) max,
                (double) initialValue, (double) amountToStepBy);
            spinner.setValueFactory(secondStart_0svf);
            spinner.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    spinner.getValueFactory().setValue(Double.parseDouble(newValue));
                } catch (Exception e) {
                    StaticLog.warn("The number double conversion is abnormal. newValue:" + newValue);
                    spinner.getEditor().setText(oldValue);
                }
            });
        }
    }

}
