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

import cn.hutool.log.StaticLog;
import com.tlcsdm.core.exception.UnExpectedResultException;
import com.tlcsdm.core.javafx.FxApp;
import com.tlcsdm.core.javafx.dialog.FxButtonType;
import com.tlcsdm.core.javafx.dialog.FxDialog;
import com.tlcsdm.core.javafx.dialog.FxNotifications;
import com.tlcsdm.core.javafx.helper.LayoutHelper;
import com.tlcsdm.core.javafx.util.Config;
import com.tlcsdm.core.javafx.util.FxmlUtil;
import com.tlcsdm.core.javafx.util.ScreenUtil;
import com.tlcsdm.jfxcommon.CommonSample;
import com.tlcsdm.jfxcommon.util.I18nUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式工具.
 *
 * @author unknowIfGuestInDream
 * @since 1.0.1
 */
public class RegexTester extends CommonSample {
    @FXML
    protected TextField regexTextField;
    @FXML
    protected Button regulatButton;
    @FXML
    protected TableView<Map<String, String>> examplesTableView;
    @FXML
    protected TableColumn<Map<String, String>, String> examplesTableColumn0;
    @FXML
    protected TableColumn<Map<String, String>, String> examplesTableColumn1;
    @FXML
    protected TextArea sourceTextArea;
    @FXML
    protected TextArea matchTextArea;
    @FXML
    protected TableView<Map<String, String>> matchTableView;
    @FXML
    protected TableColumn<Map<String, String>, String> matchTableColumn0;
    @FXML
    protected TableColumn<Map<String, String>, String> matchTableColumn1;
    @FXML
    protected TableColumn<Map<String, String>, String> matchTableColumn2;
    @FXML
    protected TableColumn<Map<String, String>, String> matchTableColumn3;
    @FXML
    protected TableColumn<Map<String, String>, String> matchTableColumn4;
    @FXML
    protected Button resetButton;
    @FXML
    protected Button aboutRegularButton;
    @FXML
    protected CheckBox ignoreCaseCheckBox;
    @FXML
    protected TextField replaceTextField;
    @FXML
    protected CheckBox isReplaceCheckBox;

    private Notifications notificationBuilder;

    @Override
    public String getSampleId() {
        return "regexTester";
    }

    @Override
    public String getSampleName() {
        return I18nUtils.get("common.tool.code.regexTester.sampleName");
    }

    @Override
    public String getSampleVersion() {
        return "1.0.1";
    }

    @Override
    public String getSampleDescription() {
        return I18nUtils.get("common.tool.code.regexTester.sampleDesc");
    }

    @Override
    public Node getPanel(Stage stage) {
        FXMLLoader fxmlLoader = FxmlUtil.loadFxmlFromResource(
            RegexTester.class.getResource("/com/tlcsdm/jfxcommon/fxml/regexTester.fxml"),
            ResourceBundle.getBundle(I18nUtils.getBasename(), Config.defaultLocale));
        return fxmlLoader.getRoot();
    }

    @Override
    public Node getControlPanel() {
        return super.getControlPanel();
    }

    @Override
    public String getOrderKey() {
        return "regexTester";
    }

    @Override
    public ImageView getSampleImageIcon() {
        return LayoutHelper.iconView(getClass().getResource("/com/tlcsdm/jfxcommon/static/icon/regex.png"));
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

    }

    private void initializeUI() {
        examplesTableColumn0.setCellValueFactory(new MapValueFactory("column0"));
        examplesTableColumn1.setCellValueFactory(new MapValueFactory("column1"));
        PropertiesConfiguration pcfg = new PropertiesConfiguration();
        try {
            pcfg.read(new InputStreamReader(Objects.requireNonNull(
                RegexTester.class.getResource("/com/tlcsdm/jfxcommon/static/data/regexData.properties")).openStream()));
        } catch (ConfigurationException | IOException e) {
            throw new UnExpectedResultException("Not found data.", e);
        }
        pcfg.getKeys().forEachRemaining((String key) -> {
            Map<String, String> map = new HashMap<>();
            map.put("column0", key);
            map.put("column1", pcfg.getString(key));
            examplesTableView.getItems().add(map);
        });
        matchTableColumn0.setCellValueFactory(new MapValueFactory("column0"));
        matchTableColumn1.setCellValueFactory(new MapValueFactory("column1"));
        matchTableColumn2.setCellValueFactory(new MapValueFactory("column2"));
        matchTableColumn3.setCellValueFactory(new MapValueFactory("column3"));
        matchTableColumn4.setCellValueFactory(new MapValueFactory("column4"));
        matchTableColumn1.setCellFactory(TextFieldTableCell.forTableColumn());
        matchTableColumn2.setCellFactory(TextFieldTableCell.forTableColumn());

        examplesTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                regexTextField.setText(examplesTableView.getSelectionModel().getSelectedItem().get("column1"));
            }
        });
        regexTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            regulatAction(null);
        });
        sourceTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            regulatAction(null);
        });
    }

    @FXML
    private void regulatAction(ActionEvent event) {
        String regexText = regexTextField.getText().trim();
        String sourceText = sourceTextArea.getText().trim();
        String replaceText = replaceTextField.getText();
        matchTableView.getItems().clear();
        matchTextArea.setText(null);
        Pattern p;
        if (ignoreCaseCheckBox.isSelected()) {
            // 不区分大小写
            p = Pattern.compile(regexText, Pattern.CASE_INSENSITIVE);
        } else {
            p = Pattern.compile(regexText);
        }
        // 用Pattern类的matcher()方法生成一个Matcher对象
        Matcher m = p.matcher(sourceText);
        StringBuffer sb = new StringBuffer();
        StringBuffer rsb = new StringBuffer();
        // 使用find()方法查找第一个匹配的对象
        boolean result = m.find();
        // 使用循环找出模式匹配的内容替换之,再将内容加到sb里
        // 匹配总数
        int cnt = 0;
        int start;
        int end;
        while (result) {
            // 替换匹配
            m.appendReplacement(rsb, replaceText);
            cnt++;
            sb.append("\n");
            start = m.start();
            end = m.end();
            String matchText = sourceText.substring(start, end);
            sb.append("Match[").append(cnt).append("]: ");
            sb.append(matchText);
            sb.append(" [start: ").append(start).append(", end: ").append(end).append("]");
            String str0 = m.group();
            String str1 = "";
            try {
                //捕获的子序列
                str1 = m.group(1);
            } catch (IllegalStateException | IndexOutOfBoundsException e) {
                StaticLog.error(e);
            }
            Map<String, String> map = new HashMap<>();
            map.put("column0", Integer.toString(cnt));
            map.put("column1", str0);
            map.put("column2", str1);
            map.put("column3", Integer.toString(start));
            map.put("column4", Integer.toString(end));
            matchTableView.getItems().add(map);
            result = m.find();
        }
        sb.insert(0, "\n匹配总数: " + cnt);
        sb.insert(0, "\t直接匹配判断: " + sourceText.matches(regexText));
        if (isReplaceCheckBox.isSelected() && !replaceText.isEmpty()) {
            m.appendTail(rsb);
            sb.append("\n\n替换匹配后内容: \n").append(rsb);
        }
        matchTextArea.setText(!sb.isEmpty() ? sb.substring(1) : "");
    }

    @FXML
    private void resetAction(ActionEvent event) {
        regexTextField.setText(null);
        sourceTextArea.setText(null);
        matchTextArea.setText(null);
        matchTableView.getItems().clear();
    }

    @FXML
    private void aboutRegularAction(ActionEvent event) {
        String url = Objects.requireNonNull(
                RegexTester.class.getResource("/com/tlcsdm/jfxcommon/static/data/regexAbout.html"))
            .toExternalForm();
        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();
        webEngine.load(url);
        VBox vbox = new VBox();
        vbox.getChildren().add(browser);
        VBox.setVgrow(browser, Priority.ALWAYS);
        FxDialog<VBox> dialog = new FxDialog<VBox>()
            .setTitle("正则表达式教程")
            .setOwner(FxApp.primaryStage).setPrefSize(ScreenUtil.getScreenWeight() * 0.74,
                ScreenUtil.getScreenHeight() * 0.8).setResizable(true).setBody(vbox)
            .setButtonTypes(FxButtonType.CLOSE);
        dialog.setButtonHandler(FxButtonType.CLOSE, (e, s) -> s.close());
        dialog.show();
    }

}
