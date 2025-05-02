/*
 * Copyright (c) 2025 unknowIfGuestInDream.
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

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.StaticLog;
import com.github.gino0631.icns.IcnsBuilder;
import com.github.gino0631.icns.IcnsIcons;
import com.github.gino0631.icns.IcnsType;
import com.tlcsdm.core.javafx.bind.TextInputControlEmptyBinding;
import com.tlcsdm.core.javafx.control.FxButton;
import com.tlcsdm.core.javafx.control.FxTextInput;
import com.tlcsdm.core.javafx.controlsfx.FxAction;
import com.tlcsdm.core.javafx.dialog.FxNotifications;
import com.tlcsdm.core.javafx.helper.LayoutHelper;
import com.tlcsdm.core.javafx.util.FileChooserUtil;
import com.tlcsdm.core.javafx.util.JavaFxSystemUtil;
import com.tlcsdm.core.javafx.util.OSUtil;
import com.tlcsdm.qe.QeSample;
import com.tlcsdm.qe.util.I18nUtils;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.ifok.image.image4j.codec.ico.ICOEncoder;
import net.ifok.image.image4j.util.ConvertUtil;
import net.ifok.image.image4j.util.ImageUtil;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.Imaging;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionUtils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

public class RcpIconTool extends QeSample {

    private TextField pngFileField;
    private TextField outputField;
    private CheckBox icoCheck;
    private CheckBox icnsCheck;
    private CheckBox xpmCheck;
    private final Notifications notificationBuilder = FxNotifications.defaultNotify();
    private FileChooser pngChooser;
    private DirectoryChooser outputChooser;
    private final String fileName = "icon";

    private final Action openOutDir = FxAction.openOutDir(actionEvent -> {
        String outPath = outputField.getText();
        if (StrUtil.isEmpty(outPath)) {
            notificationBuilder.text(I18nUtils.get("qe.tool.button.openOutDir.warnMsg"));
            notificationBuilder.showWarning();
            return;
        }
        JavaFxSystemUtil.openDirectory(outPath);
    });

    private final Action generate = FxAction.generate(actionEvent -> {
        String outPath = outputField.getText();
        File file = new File(outPath, "icons");
        if (file.exists()) {
            FileUtil.clean(file);
        } else {
            file.mkdir();
        }
        if (icoCheck.isSelected()) {
            createICO(file);
        }
        if (icnsCheck.isSelected()) {
            createICNS(file);
        }
        if (xpmCheck.isSelected()) {
            createXPM(file);
        }
        OSUtil.openAndSelectedFile(file);
        bindUserData();
    });

    private final Collection<? extends Action> actions = List.of(generate, openOutDir);

    @Override
    public String getSampleId() {
        return "rcpIconTool";
    }

    @Override
    public String getSampleName() {
        return I18nUtils.get("qe.tool.rcpIconTool.sampleName");
    }

    @Override
    public String getSampleVersion() {
        return "1.0.1";
    }

    @Override
    public String getOrderKey() {
        return "rcpIconTool";
    }

    @Override
    public ImageView getSampleImageIcon() {
        return LayoutHelper.iconView(getClass().getResource("/com/tlcsdm/qe/static/icon/product.png"));
    }

    @Override
    public Node getPanel(Stage stage) {
        initComponment();
        GridPane grid = new GridPane();
        grid.setVgap(12);
        grid.setHgap(12);
        grid.setPadding(new Insets(24));

        ToolBar toolBar = ActionUtils.createToolBar(actions, ActionUtils.ActionTextBehavior.SHOW);
        toolBar.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        toolBar.setPrefWidth(Double.MAX_VALUE);

        Label pngFileLabel = new Label(I18nUtils.get("qe.tool.rcpIconTool.label.pngFile"));
        pngFileField = new TextField();
        pngFileField.setMaxWidth(Double.MAX_VALUE);
        Button pngFileButton = FxButton.choose();
        pngFileField.setEditable(false);
        pngFileButton.setOnAction(arg0 -> {
            File file = pngChooser.showOpenDialog(stage);
            if (file != null) {
                pngFileField.setText(file.getPath());
            }
        });

        Label outputLabel = new Label(I18nUtils.get("qe.tool.rcpIconTool.label.output"));
        outputField = new TextField();
        Button outputButton = FxButton.choose();
        outputField.setEditable(false);
        outputButton.setOnAction(arg0 -> {
            File file = outputChooser.showDialog(stage);
            if (file != null) {
                outputField.setText(file.getPath());
                outputChooser.setInitialDirectory(file);
            }
        });

        Label icoLabel = new Label(I18nUtils.get("qe.tool.rcpIconTool.check.ico"));
        icoCheck = new CheckBox();
        icoCheck.setPrefWidth(Double.MAX_VALUE);

        Label icnsLabel = new Label(I18nUtils.get("qe.tool.rcpIconTool.check.icns"));
        icnsCheck = new CheckBox();

        Label xpmLabel = new Label(I18nUtils.get("qe.tool.rcpIconTool.check.xpm"));
        xpmCheck = new CheckBox();

        icoCheck.setSelected(true);

        grid.add(toolBar, 0, 0, 3, 1);
        grid.add(pngFileLabel, 0, 1);
        grid.add(pngFileButton, 1, 1);
        grid.add(pngFileField, 2, 1);

        grid.add(outputLabel, 0, 2);
        grid.add(outputButton, 1, 2);
        grid.add(outputField, 2, 2);

        grid.add(icoLabel, 0, 3);
        grid.add(icoCheck, 1, 3, 2, 1);
        grid.add(icnsLabel, 0, 4);
        grid.add(icnsCheck, 1, 4, 2, 1);
        grid.add(xpmLabel, 0, 5);
        grid.add(xpmCheck, 1, 5, 2, 1);

        return grid;
    }

    private void initComponment() {
        pngChooser = new FileChooser();
        outputChooser = new DirectoryChooser();
        FileChooser.ExtensionFilter pngFilter = new FileChooser.ExtensionFilter("PNG", "*.png");
        pngChooser.getExtensionFilters().add(pngFilter);
    }

    @Override
    public void initializeBindings() {
        super.initializeBindings();
        BooleanBinding emptyValidation = Bindings.createBooleanBinding(() -> {
                return (!icoCheck.isSelected() && !icnsCheck.isSelected() && !xpmCheck.isSelected()) || pngFileField.getText()
                    .isEmpty() || outputField.getText().isEmpty();
            }, icoCheck.selectedProperty(), icnsCheck.selectedProperty(), xpmCheck.selectedProperty(),
            pngFileField.textProperty(), outputField.textProperty());
        BooleanBinding outputValidation = new TextInputControlEmptyBinding(outputField).build();
        openOutDir.disabledProperty().bind(outputValidation);
        generate.disabledProperty().bind(emptyValidation);
        FileChooserUtil.setOnDrag(pngFileField, FileChooserUtil.FileType.FILE);
        FileChooserUtil.setOnDrag(outputField, FileChooserUtil.FileType.FOLDER);
    }

    //
    @Override
    public void initializeUserDataBindings() {
        super.initializeUserDataBindings();
        userData.put("outPut", outputField);
        userData.put("outputChooser", outputChooser);
        userData.put("icoCheck", icoCheck);
        userData.put("icnsCheck", icnsCheck);
        userData.put("xpmCheck", xpmCheck);
    }

    @Override
    public Node getControlPanel() {
        String content = """
            ICO:
            {icoDesc}

            ICNS:
            {icnsDesc}

            XPM:
            {xpmDesc}
            """;
        Map<String, String> map = new HashMap<>();
        map.put("icoDesc", I18nUtils.get("qe.tool.rcpIconTool.check.ico.description"));
        map.put("icnsDesc", I18nUtils.get("qe.tool.rcpIconTool.check.icns.description"));
        map.put("xpmDesc", I18nUtils.get("qe.tool.rcpIconTool.check.xpm.description"));
        return FxTextInput.textArea(StrUtil.format(content, map));
    }

    public void createICO(File file) {
        File pngFile = new File(pngFileField.getText());
        BufferedImage pngImage = null;
        try {
            // 加载PNG文件
            pngImage = ImageIO.read(pngFile);

            BufferedImage bmpImage = new BufferedImage(
                pngImage.getWidth(),
                pngImage.getHeight(),
                BufferedImage.TYPE_INT_RGB
            );

            // 3. 绘制到新图像，用指定颜色填充透明区域
            Graphics2D graphics = bmpImage.createGraphics();
            graphics.setColor(Color.WHITE);  // 设置背景色（此处为白色）
            graphics.fillRect(0, 0, bmpImage.getWidth(), bmpImage.getHeight());
            graphics.drawImage(pngImage, 0, 0, null);
            graphics.dispose();

            // 2. 创建输出文件
            byte[] bs = Imaging.writeImageToBytes(bmpImage, ImageFormats.BMP);
            BufferedImage bsImage = convert(bs);
            // 目标尺寸列表 (包括 16x16, 32x32, 48x48, 256x256)
            int[][] sizes = {
                {16, 16}, {32, 32}, {48, 48}, {256, 256}
            };

            // 存储生成的图像
            List<BufferedImage> icoImages = new ArrayList<>();

            // 为每个尺寸创建8bit和32bit版本
            for (int[] size : sizes) {
                BufferedImage pi = ImageUtil.scaleImage(bsImage, size[0], size[1]);
                if (size[0] != 256) {
                    // 8bit 深度
                    BufferedImage image8bit = ConvertUtil.convert8(pi);
                    icoImages.add(image8bit);
                }

                // 32bit 深度
                if (pi.getType() != BufferedImage.TYPE_INT_ARGB) {
                    BufferedImage image32bit = ConvertUtil.convert32(pi);
                    icoImages.add(image32bit);
                } else {
                    icoImages.add(pi);
                }
            }

            File outputIcoFile = new File(file, fileName + ".ico");
            ICOEncoder.write(icoImages, outputIcoFile);
            StaticLog.debug("The ico file has been successfully generated");

            icoImages.forEach(i -> i.getGraphics().dispose());
            bsImage.getGraphics().dispose();
            pngImage.getGraphics().dispose();
        } catch (IOException e) {
            StaticLog.error(e);
            if (pngImage != null) {
                pngImage.getGraphics().dispose();
            }
        }
    }

    private BufferedImage convert(byte[] imageBytes) throws IOException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes)) {
            return Imaging.getBufferedImage(bis);
        }
    }

    public void createICNS(File file) {
        File pngFile = new File(pngFileField.getText());
        File outputIcoFile = new File(file, fileName + ".icns");
        BufferedImage pngImage = null;
        try (IcnsBuilder builder = IcnsBuilder.getInstance()) {
            pngImage = ImageIO.read(pngFile);
            builder.add(IcnsType.ICNS_16x16_JPEG_PNG_IMAGE, getImageAsStream(pngImage, 16, 16));
            builder.add(IcnsType.ICNS_32x32_JPEG_PNG_IMAGE, getImageAsStream(pngImage, 32, 32));
            builder.add(IcnsType.ICNS_128x128_JPEG_PNG_IMAGE, getImageAsStream(pngImage, 128, 128));
            builder.add(IcnsType.ICNS_256x256_JPEG_PNG_IMAGE, getImageAsStream(pngImage, 256, 256));
            builder.add(IcnsType.ICNS_512x512_JPEG_PNG_IMAGE, getImageAsStream(pngImage, 512, 512));

            try (IcnsIcons builtIcons = builder.build()) {
                FileOutputStream os = new FileOutputStream(outputIcoFile);
                builtIcons.writeTo(os);
            }
            StaticLog.debug("The icns file has been successfully generated");
            pngImage.getGraphics().dispose();
        } catch (IOException e) {
            StaticLog.error(e);
            if (pngImage != null) {
                pngImage.getGraphics().dispose();
            }
        }
    }

    private ByteArrayInputStream getImageAsStream(BufferedImage source, int targetWidth, int targetHeigth) throws
        IOException {
        BufferedImage bufImage = ImageUtil.scaleImage(source, targetWidth, targetHeigth);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(bufImage, "png", output);
        return new ByteArrayInputStream(output.toByteArray(), 0, output.size());
    }

    public void createXPM(File file) {
        File pngFile = new File(pngFileField.getText());
        File outputIcoFile = new File(file, fileName + ".xpm");
        BufferedImage pngImage = null;
        try {
            pngImage = ImageIO.read(pngFile);
            BufferedImage pi = ImageUtil.scaleImage(pngImage, 512, 512);
            Imaging.writeImage(pi, outputIcoFile, ImageFormats.XPM);
            StaticLog.debug("The xpm file has been successfully generated");
            pngImage.getGraphics().dispose();
        } catch (IOException e) {
            StaticLog.error(e);
            if (pngImage != null) {
                pngImage.getGraphics().dispose();
            }
        }
    }
}
