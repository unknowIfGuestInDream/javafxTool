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

package com.tlcsdm.jfxcommon.code;

import cn.hutool.core.swing.clipboard.ClipboardUtil;
import com.tlcsdm.core.exception.UnsupportedFeatureException;
import com.tlcsdm.core.javafx.dialog.FxNotifications;
import com.tlcsdm.core.javafx.helper.ImageViewHelper;
import com.tlcsdm.core.javafx.util.Config;
import com.tlcsdm.core.javafx.util.FxmlUtil;
import com.tlcsdm.jfxcommon.CommonSample;
import com.tlcsdm.jfxcommon.util.I18nUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 颜色代码工具.
 *
 * @author unknowIfGuestInDream
 * @since 1.0.1
 */
public class ColorCode extends CommonSample {
    @FXML
    public TextField sysTextField;
    @FXML
    public TextField rgbTextField;
    @FXML
    public TextField argbTextField;
    @FXML
    public TextField rgbaTextField;
    @FXML
    public TextField hslTextField;
    @FXML
    public TextField hsvTextField;
    @FXML
    public Button sysCopyButton;
    @FXML
    public Button rgbCopyButton;
    @FXML
    public Button argbCopyButton;
    @FXML
    public Button rgbaCopyButton;
    @FXML
    public Button hslCopyButton;
    @FXML
    public Button hsvCopyButton;
    @FXML
    public ColorPicker colorSelect1ColorPicker;

    private Notifications notificationBuilder;

    @Override
    public String getSampleId() {
        return "colorCode";
    }

    @Override
    public String getSampleName() {
        return I18nUtils.get("common.tool.code.colorCode");
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
    public Node getPanel(Stage stage) {
        FXMLLoader fxmlLoader = FxmlUtil.loadFxmlFromResource(
            ColorCode.class.getResource("/com/tlcsdm/jfxcommon/fxml/colorCode.fxml"),
            ResourceBundle.getBundle(I18nUtils.getBasename(), Config.defaultLocale));
        return fxmlLoader.getRoot();
    }

    @Override
    public Node getControlPanel() {
        return super.getControlPanel();
    }

    @Override
    public String getOrderKey() {
        return "colorCode";
    }

    @Override
    public ImageView getSampleImageIcon() {
        return ImageViewHelper.get("rgb");
    }

    @Override
    public boolean hasControlPanel() {
        return false;
    }

    public static void main(String[] args) {
        launch(args);
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
        colorSelect1ColorPicker.setOnAction((value) -> {
            setColorTextField(colorSelect1ColorPicker.getValue());
        });
        sysTextField.setOnKeyReleased((event) -> {
            setColorTextField(Color.valueOf(((TextField) event.getSource()).getText()));
        });
        rgbTextField.setOnKeyReleased((event) -> {
            setColorTextField(Color.valueOf(((TextField) event.getSource()).getText()));
        });
        argbTextField.setOnKeyReleased((event) -> {
            setColorTextField(Color.valueOf(((TextField) event.getSource()).getText()));
        });
        rgbaTextField.setOnKeyReleased((event) -> {
            setColorTextField(Color.valueOf(((TextField) event.getSource()).getText()));
        });
        hslTextField.setOnKeyReleased((event) -> {
            setColorTextField(Color.valueOf(((TextField) event.getSource()).getText()));
        });
        hsvTextField.setOnKeyReleased((event) -> {
            setColorTextField(hsvToRgb(((TextField) event.getSource()).getText()));
        });
    }

    private void initializeUI() {
        // Do nothing
    }

    @FXML
    private void sysCopyAction(ActionEvent event) {
        ClipboardUtil.setStr(sysTextField.getText());
        notificationBuilder.text(I18nUtils.get("common.button.copyResult.success"));
        notificationBuilder.showInformation();
    }

    @FXML
    private void rgbCopyAction(ActionEvent event) {
        ClipboardUtil.setStr(rgbTextField.getText());
        notificationBuilder.text(I18nUtils.get("common.button.copyResult.success"));
        notificationBuilder.showInformation();
    }

    @FXML
    private void argbCopyAction(ActionEvent event) {
        ClipboardUtil.setStr(argbTextField.getText());
        notificationBuilder.text(I18nUtils.get("common.button.copyResult.success"));
        notificationBuilder.showInformation();
    }

    @FXML
    private void rgbaCopyAction(ActionEvent event) {
        ClipboardUtil.setStr(rgbaTextField.getText());
        notificationBuilder.text(I18nUtils.get("common.button.copyResult.success"));
        notificationBuilder.showInformation();
    }

    @FXML
    private void hslCopyAction(ActionEvent event) {
        ClipboardUtil.setStr(hslTextField.getText());
        notificationBuilder.text(I18nUtils.get("common.button.copyResult.success"));
        notificationBuilder.showInformation();
    }

    @FXML
    private void hsvCopyAction(ActionEvent event) {
        ClipboardUtil.setStr(hsvTextField.getText());
        notificationBuilder.text(I18nUtils.get("common.button.copyResult.success"));
        notificationBuilder.showInformation();
    }

    public void setColorTextField(Color color) {
        String hsb = String.format("hsl(%d, %d%%, %d%%)",
            (int) (color.getHue()),
            (int) (color.getSaturation() * 100),
            (int) (color.getBrightness() * 100));
        colorSelect1ColorPicker.setValue(color);
        sysTextField.setText(colorToHex(color));
        rgbTextField.setText(
            String.format("rgb(%d,%d,%d)", (int) (color.getRed() * 255), (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255)));
        argbTextField.setText(String.format("#%02X", color.hashCode()));
        rgbaTextField.setText(
            String.format("rgba(%d,%d,%d,%.2f)", (int) (color.getRed() * 255), (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255), color.getOpacity()));
        hslTextField.setText(hsb);
        double[] hsv = RGBtoHSV(color.getRed(), color.getGreen(), color.getBlue());
        hsvTextField.setText(String.format("hsv(%.2f,%.2f,%.2f)", hsv[0], hsv[1], hsv[2]));
    }

    public String colorToHex(Color c) {
        return c != null ? String.format((Locale) null, "#%02x%02x%02x", Math.round(c.getRed() * 255.0),
            Math.round(c.getGreen() * 255.0), Math.round(c.getBlue() * 255.0)).toUpperCase() : null;
    }

    public double[] RGBtoHSV(double r, double g, double b) {
        double h, s, v;
        double min, max, delta;
        min = Math.min(Math.min(r, g), b);
        max = Math.max(Math.max(r, g), b);
        // V
        v = max;
        delta = max - min;
        // S
        if (max != 0) {
            s = delta / max;
        } else {
            s = 0;
            h = -1;
            return new double[]{h, s, v};
        }
        // H
        if (r == max) {
            // between yellow & magenta
            h = (g - b) / delta;
        } else if (g == max) {
            // between cyan & yellow
            h = 2 + (b - r) / delta;
        } else {
            // between magenta & cyan
            h = 4 + (r - g) / delta;
        }
        // degrees
        h *= 60;
        if (h < 0) {
            h += 360;
        }
        if (Double.isNaN(h)) {
            h = 0;
        }
        return new double[]{h, s, v};
    }

    public Color hsvToRgb(String hsv) {
        String[] hsvs = hsv.substring(4, hsv.length() - 1).split(",");
        return hsvToRgb(Float.parseFloat(hsvs[0]), Float.parseFloat(hsvs[1]), Float.parseFloat(hsvs[2]));
    }

    public Color hsvToRgb(float hue, float saturation, float value) {
        int h = (int) (hue / 60);
        float f = hue / 60 - h;
        float p = value * (1 - saturation);
        float q = value * (1 - f * saturation);
        float t = value * (1 - (1 - f) * saturation);

        switch (h) {
            case 0:
                return new Color(value, t, p, 1);
            case 1:
                return new Color(q, value, p, 1);
            case 2:
                return new Color(p, value, t, 1);
            case 3:
                return new Color(p, q, value, 1);
            case 4:
                return new Color(t, p, value, 1);
            case 5:
                return new Color(value, p, q, 1);
            default:
                throw new UnsupportedFeatureException(
                    "Something went wrong when converting from HSV to RGB. Input was " + hue + ", " + saturation + ", " + value);
        }
    }

}
