/*
 * Copyright (c) 2023 unknowIfGuestInDream
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

import cn.hutool.core.util.StrUtil;
import com.tlcsdm.core.javafx.control.FxTextInput;
import com.tlcsdm.core.javafx.controlsfx.FxAction;
import com.tlcsdm.core.javafx.dialog.FxNotifications;
import com.tlcsdm.core.javafx.helper.LayoutHelper;
import com.tlcsdm.core.javafx.util.Config;
import com.tlcsdm.core.javafx.util.FileChooserUtil;
import com.tlcsdm.core.javafx.util.FxmlUtil;
import com.tlcsdm.core.javafx.util.OSUtil;
import com.tlcsdm.core.util.YuiCompressUtil;
import com.tlcsdm.qe.QeSample;
import com.tlcsdm.qe.util.I18nUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * js/css压缩.
 *
 * @author unknowIfGuestInDream
 * @since 1.0.0
 */
public class Compress extends QeSample implements Initializable {

    @FXML
    private Button btnJsCompress;
    @FXML
    private CheckBox enableMunge, enableVerbose, enableOptimizations, enablePreserveAllSemiColons, enableLinebreakpos;
    @FXML
    private TextField txtLinebreakpos;
    @FXML
    private TextArea txtJsCode, txtJsResult;
    @FXML
    private Button btnCssCompress;
    @FXML
    private CheckBox enableCssLinebreakpos;
    @FXML
    private TextField txtCssLinebreakpos;
    @FXML
    private TextArea txtCssCode, txtCssResult;
    private final Notifications notificationBuilder = FxNotifications.defaultNotify();

    @Override
    public String getSampleId() {
        return "compress";
    }

    @Override
    public String getSampleName() {
        return I18nUtils.get("qe.tool.compress.sampleName");
    }

    @Override
    public String getSampleDescription() {
        return I18nUtils.get("qe.tool.compress.description");
    }

    @Override
    public String getSampleVersion() {
        return "1.0.1";
    }

    @Override
    public Node getPanel(Stage stage) {
        FXMLLoader fxmlLoader = FxmlUtil.loadFxmlFromResource(
            Compress.class.getResource("/com/tlcsdm/qe/fxml/compress.fxml"),
            ResourceBundle.getBundle(I18nUtils.getBasename(), Config.defaultLocale));
        return fxmlLoader.getRoot();
    }

    @Override
    public Node getControlPanel() {
        String content = """
            {note}
            {enableMunge}: {enableMungeDesc}
            {enableVerbose}: {enableVerboseDesc}
            {enableOptimizations}: {enableOptimizationsDesc}
            {enablePreserveAllSemiColons}: {enablePreserveAllSemiColonsDesc}
            {enableLinebreakpos}: {enableLinebreakposDesc}
            """;

        Map<String, String> map = new HashMap<>(32);
        map.put("note", I18nUtils.get("qe.tool.compress.description.note"));
        map.put("enableMunge", I18nUtils.get("qe.tool.compress.check.enableMunge"));
        map.put("enableVerbose", I18nUtils.get("qe.tool.compress.check.enableVerbose"));
        map.put("enableOptimizations", I18nUtils.get("qe.tool.compress.check.enableOptimizations"));
        map.put("enablePreserveAllSemiColons", I18nUtils.get("qe.tool.compress.check.enablePreserveAllSemiColons"));
        map.put("enableLinebreakpos", I18nUtils.get("qe.tool.compress.check.enableLinebreakpos"));
        map.put("enableMungeDesc", I18nUtils.get("qe.tool.compress.check.enableMunge.description"));
        map.put("enableVerboseDesc", I18nUtils.get("qe.tool.compress.check.enableVerbose.description"));
        map.put("enableOptimizationsDesc", I18nUtils.get("qe.tool.compress.check.enableOptimizations.description"));
        map.put("enablePreserveAllSemiColonsDesc",
            I18nUtils.get("qe.tool.compress.check.enablePreserveAllSemiColons.description"));
        map.put("enableLinebreakposDesc", I18nUtils.get("qe.tool.compress.check.enableLinebreakpos.description"));
        return FxTextInput.textArea(StrUtil.format(content, map));
    }

    @Override
    public String getOrderKey() {
        return "compress";
    }

    @Override
    public ImageView getSampleImageIcon() {
        return LayoutHelper.iconView(getClass().getResource("/com/tlcsdm/qe/static/icon/compress.png"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeUserDataBindings();
        initializeBindings();
        initializeUserData();

        initializeUI();
    }

    @Override
    public void initializeBindings() {
        super.initializeBindings();
        txtLinebreakpos.disableProperty().bind(enableLinebreakpos.selectedProperty().not());
        btnJsCompress.disableProperty().bind(txtJsCode.textProperty().isEmpty());
        btnCssCompress.disableProperty().bind(txtCssCode.textProperty().isEmpty());
        txtCssLinebreakpos.disableProperty().bind(enableCssLinebreakpos.selectedProperty().not());
        FileChooserUtil.setOnDragByOpenFile(txtJsCode);
        FileChooserUtil.setOnDragByOpenFile(txtCssCode);
    }

    @Override
    public void initializeUserDataBindings() {
        super.initializeUserDataBindings();
        userData.put("enableMunge", enableMunge);
        userData.put("enableVerbose", enableVerbose);
        userData.put("enableOptimizations", enableOptimizations);
        userData.put("enablePreserveAllSemiColons", enablePreserveAllSemiColons);
        userData.put("enableLinebreakpos", enableLinebreakpos);
        userData.put("txtLinebreakpos", txtLinebreakpos);
        userData.put("enableCssLinebreakpos", enableCssLinebreakpos);
        userData.put("txtCssLinebreakpos", txtCssLinebreakpos);
    }

    private void initializeUI() {
        btnJsCompress
            .setGraphic(LayoutHelper.iconView(FxAction.class.getResource("/com/tlcsdm/core/static/icon/generate.png")));
        btnCssCompress
            .setGraphic(LayoutHelper.iconView(FxAction.class.getResource("/com/tlcsdm/core/static/icon/generate.png")));
    }

    /**
     * 压缩js.
     */
    @FXML
    public void compressJs(ActionEvent actionEvent) {
        int linebreakpos = enableLinebreakpos.isSelected() ? Integer.parseInt(txtLinebreakpos.getText()) : -1;
        String result = YuiCompressUtil.compressJS(txtJsCode.getText(), linebreakpos, enableMunge.isSelected(),
            enableVerbose.isSelected(), enablePreserveAllSemiColons.isSelected(), !enableOptimizations.isSelected());
        if (result.isEmpty()) {
            notificationBuilder.text(I18nUtils.get("qe.tool.compress.button.compress.fail"));
            notificationBuilder.showInformation();
            return;
        }
        txtJsResult.setText(result);
        OSUtil.writeToClipboard(result);
        notificationBuilder.text(I18nUtils.get("qe.tool.compress.button.compress.success"));
        notificationBuilder.showInformation();
        bindUserData();
    }

    /**
     * 压缩css.
     */
    @FXML
    public void compressCss(ActionEvent actionEvent) {
        int linebreakpos = enableCssLinebreakpos.isSelected() ? Integer.parseInt(txtCssLinebreakpos.getText()) : -1;
        String result = YuiCompressUtil.compressCSS(txtCssCode.getText(), linebreakpos);
        if (result.isEmpty()) {
            notificationBuilder.text(I18nUtils.get("qe.tool.compress.button.compress.fail"));
            notificationBuilder.showInformation();
            return;
        }
        txtCssResult.setText(result);
        OSUtil.writeToClipboard(result);
        notificationBuilder.text(I18nUtils.get("qe.tool.compress.button.compress.success"));
        notificationBuilder.showInformation();
        bindUserData();
    }
}
