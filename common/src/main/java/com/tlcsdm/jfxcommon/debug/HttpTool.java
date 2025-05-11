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

package com.tlcsdm.jfxcommon.debug;

import cn.hutool.log.StaticLog;
import com.tlcsdm.core.javafx.dialog.FxNotifications;
import com.tlcsdm.core.javafx.helper.ImageViewHelper;
import com.tlcsdm.core.javafx.richtext.InformationArea;
import com.tlcsdm.core.javafx.richtext.JsonCodeArea;
import com.tlcsdm.core.javafx.util.Config;
import com.tlcsdm.core.javafx.util.FxmlUtil;
import com.tlcsdm.core.util.CoreUtil;
import com.tlcsdm.core.util.DependencyUtil;
import com.tlcsdm.core.util.HttpUtil;
import com.tlcsdm.core.util.JacksonUtil;
import com.tlcsdm.jfxcommon.CommonSample;
import com.tlcsdm.jfxcommon.code.ColorCode;
import com.tlcsdm.jfxcommon.util.I18nUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.Notifications;

import java.net.URLEncoder;
import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Http调试工具.
 *
 * @author unknowIfGuestInDream
 */
public class HttpTool extends CommonSample {

    @FXML
    private TextField urlTextField;
    @FXML
    private ChoiceBox<String> methodChoiceBox;
    @FXML
    private Button sendButton, toBrowerButton, addParamsDataButton, addParamsHeaderButton, addParamsCookieButton;
    @FXML
    private CheckBox paramsDataCheckBox, paramsDataIsStringCheckBox, paramsHeaderCheckBox, paramsCookieCheckBox;
    @FXML
    private TextArea paramsDataTextArea;
    @FXML
    private JsonCodeArea ResponseBodyTextArea;
    @FXML
    private InformationArea ResponseHeaderTextArea;
    @FXML
    private TableView<Map<String, String>> paramsDataTableView;
    @FXML
    private TableColumn<Map<String, String>, String> paramsDataNameTableColumn, paramsDataValueTableColumn, paramsDataRemarkTableColumn;
    @FXML
    private TableView<Map<String, String>> paramsHeaderTableView;
    @FXML
    private TableColumn<Map<String, String>, String> paramsHeaderNameTableColumn, paramsHeaderValueTableColumn, paramsHeaderRemarkTableColumn;
    @FXML
    private TableView<Map<String, String>> paramsCookieTableView;
    @FXML
    private TableColumn<Map<String, String>, String> paramsCookieNameTableColumn, paramsCookieValueTableColumn, paramsCookieRemarkTableColumn;

    private final Notifications notificationBuilder = FxNotifications.defaultNotify();
    private final String[] methodStrings = new String[]{"GET", "POST", "HEAD", "PUT", "PATCH", "DELETE"};
    private final ObservableList<Map<String, String>> paramsDatatableData = FXCollections.observableArrayList();
    private final ObservableList<Map<String, String>> paramsHeadertableData = FXCollections.observableArrayList();
    private final ObservableList<Map<String, String>> paramsCookietableData = FXCollections.observableArrayList();

    @Override
    public String getSampleId() {
        return "httpTool";
    }

    @Override
    public String getSampleName() {
        return "Http调试工具";
    }

    @Override
    public String getSampleVersion() {
        return "1.0.1";
    }

    @Override
    public String getSampleDescription() {
        return I18nUtils.get("common.tool.debug.scanPortTool.sampleDesc");
    }

    @Override
    public Node getPanel(Stage stage) {
        FXMLLoader fxmlLoader = FxmlUtil.loadFxmlFromResource(
            ColorCode.class.getResource("/com/tlcsdm/jfxcommon/fxml/httpTool.fxml"),
            ResourceBundle.getBundle(I18nUtils.BASENAME, Config.defaultLocale));
        return fxmlLoader.getRoot();
    }

    @Override
    public String getOrderKey() {
        return "HttpTool";
    }

    @Override
    public ImageView getSampleImageIcon() {
        return ImageViewHelper.get("http");
    }

    @Override
    public boolean hasControlPanel() {
        return false;
    }

    @Override
    public boolean isVisible() {
        return DependencyUtil.hasRichTextFX();
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
        methodChoiceBox.getItems().addAll(methodStrings);
        methodChoiceBox.setValue(methodStrings[0]);
        setTableColumnMapValueFactory(paramsDataNameTableColumn, "name");
        setTableColumnMapValueFactory(paramsDataValueTableColumn, "value");
        setTableColumnMapValueFactory(paramsDataRemarkTableColumn, "remark");
        setTableColumnMapValueFactory(paramsHeaderNameTableColumn, "name");
        setTableColumnMapValueFactory(paramsHeaderValueTableColumn, "value");
        setTableColumnMapValueFactory(paramsHeaderRemarkTableColumn, "remark");
        setTableColumnMapValueFactory(paramsCookieNameTableColumn, "name");
        setTableColumnMapValueFactory(paramsCookieValueTableColumn, "value");
        setTableColumnMapValueFactory(paramsCookieRemarkTableColumn, "remark");
        paramsDataTableView.setItems(paramsDatatableData);
        paramsHeaderTableView.setItems(paramsHeadertableData);
        paramsCookieTableView.setItems(paramsCookietableData);
    }

    private void initializeUI() {
        paramsDataIsStringCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    paramsDataTextArea.setVisible(true);
                    paramsDataTableView.setVisible(false);
                } else {
                    paramsDataTextArea.setVisible(false);
                    paramsDataTableView.setVisible(true);
                }
            }
        });
        setTableViewOnMouseClicked(paramsDataTableView, paramsDatatableData);
        setTableViewOnMouseClicked(paramsHeaderTableView, paramsHeadertableData);
        setTableViewOnMouseClicked(paramsCookieTableView, paramsCookietableData);
        if (DependencyUtil.hasJackson()) {
            MenuItem compressJsonMenuItem = new MenuItem("压缩JSON");
            compressJsonMenuItem.setOnAction(event -> {
                ResponseBodyTextArea.setText(JacksonUtil.compactJson(ResponseBodyTextArea.getText()));
            });
            MenuItem formatJsonMenuItem = new MenuItem("格式化JSON");
            formatJsonMenuItem.setOnAction(event -> {
                try {
                    String prettyJsonString = JacksonUtil.formatJson(ResponseBodyTextArea.getText());
                    ResponseBodyTextArea.setText("null".equals(prettyJsonString) ? "" : prettyJsonString);
                } catch (Exception e) {
                    StaticLog.debug("格式化错误:" + e.getMessage());
                    notificationBuilder.text("格式化错误:" + e.getMessage());
                    notificationBuilder.showError();
                }
            });
            ResponseBodyTextArea.setContextMenu(new ContextMenu(compressJsonMenuItem, formatJsonMenuItem));
        }
    }

    @FXML
    private void sendAction(ActionEvent event) {
        String url = urlTextField.getText().trim();
        if (StringUtils.isEmpty(url)) {
            notificationBuilder.text("请输入网站！！！");
            notificationBuilder.showWarning();
            return;
        }
        Map<String, String> paramsMap = new HashMap<>();
        Map<String, String> headerMap = new HashMap<>();
        if (paramsDataCheckBox.isSelected()) {
            for (Map<String, String> map : paramsDatatableData) {
                paramsMap.put(map.get("name"), map.get("value"));
            }
        }
        if (paramsHeaderCheckBox.isSelected()) {
            for (Map<String, String> map : paramsHeadertableData) {
                headerMap.put(map.get("name"), map.get("value"));
            }
        }
        if (paramsCookieCheckBox.isSelected()) {
            StringBuilder paramsCookieBuffer = new StringBuilder();
            for (Map<String, String> map : paramsCookietableData) {
                paramsCookieBuffer.append(map.get("name")).append("=").append(map.get("value")).append(";");
            }
            if (!paramsCookieBuffer.isEmpty()) {
                paramsCookieBuffer.deleteCharAt(paramsCookieBuffer.length() - 1);
            }
            headerMap.put("Cookie", paramsCookieBuffer.toString());
        }
        String methodString = methodChoiceBox.getValue();

        HttpResponse<String> response = null;
        if ("GET".equals(methodString)) {
            StringBuffer paramsDataBuffer = new StringBuffer();
            if (!paramsMap.isEmpty()) {
                if (url.contains("?")) {
                    paramsDataBuffer.append("&");
                } else {
                    paramsDataBuffer.append("?");
                }
                paramsMap.forEach((key, value) -> {
                    paramsDataBuffer.append(
                        URLEncoder.encode(key, StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode(value,
                        StandardCharsets.UTF_8)).append("&");
                });
                paramsDataBuffer.deleteCharAt(paramsDataBuffer.length() - 1);
            }
            url += paramsDataBuffer.toString();
            response = HttpUtil.doGet(url, headerMap);
        } else {
            StringBuilder sb = new StringBuilder();
            for (Map<String, String> map : paramsDatatableData) {
                sb.append(map.get("name"));
                sb.append("=");
                sb.append(map.get("value"));
                sb.append("&");
            }
            String body = sb.toString();
            if ("POST".equals(methodString)) {
                if (paramsDataIsStringCheckBox.isSelected()) {
                    if (paramsDataCheckBox.isSelected()) {
                        response = HttpUtil.doPost(url, headerMap, paramsDataTextArea.getText());
                    } else {
                        response = HttpUtil.doPost(url, headerMap, "");
                    }
                } else {
                    response = HttpUtil.doPost(url, headerMap, body);
                }
            } else if ("HEAD".equals(methodString)) {
                response = HttpUtil.doHead(url, headerMap);
            } else if ("PUT".equals(methodString)) {
                response = HttpUtil.doPut(url, headerMap, body);
            } else if ("PATCH".equals(methodString)) {
                response = HttpUtil.doPatch(url, headerMap, body);
            } else if ("DELETE".equals(methodString)) {
                response = HttpUtil.doDelete(url, headerMap);
            }
        }

        if (response == null || response.statusCode() != 200) {
            notificationBuilder.text("请求失败");
            notificationBuilder.showError();
            return;
        }
        HttpHeaders headers = response.headers();

        StringBuffer headerStringBuffer = new StringBuffer();
        headers.map().forEach((name, values) -> {
            String value = String.join(", ", values);
            headerStringBuffer.append(name).append(":").append(value).append("\n");
        });
        if (!headerStringBuffer.isEmpty()) {
            headerStringBuffer.deleteCharAt(0);
        }

        ResponseHeaderTextArea.setText(headerStringBuffer.toString());
        String res = response.body();
        if (DependencyUtil.hasJackson()) {
            res = JacksonUtil.formatJson(res);
        }
        ResponseBodyTextArea.setText(res);
    }

    @FXML
    void addParamsDataAction(ActionEvent event) {
        paramsDatatableData.add(new HashMap<>());
    }

    @FXML
    void addParamsHeaderAction(ActionEvent event) {
        paramsHeadertableData.add(new HashMap<>());
    }

    @FXML
    void addParamsCookieAction(ActionEvent event) {
        paramsCookietableData.add(new HashMap<>());
    }

    @FXML
    private void toBrowerAction(ActionEvent event) {
        String url = urlTextField.getText();
        if (url.isEmpty()) {
            return;
        }
        CoreUtil.openWeb(url);
    }

    private void setTableColumnMapValueFactory(TableColumn tableColumn, String name) {
        tableColumn.setCellValueFactory(new MapValueFactory(name));
        tableColumn.setCellFactory(TextFieldTableCell.<Map<String, String>>forTableColumn());
    }

    /**
     * 设置表格右击事件.
     */
    private void setTableViewOnMouseClicked(TableView<Map<String, String>> paramsDataTableView,
        ObservableList<Map<String, String>> paramsDatatableData) {
        paramsDataTableView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY && !paramsDatatableData.isEmpty()) {
                MenuItem menu_Copy = new MenuItem("复制选中行");
                menu_Copy.setOnAction(event1 -> {
                    Map<String, String> tableBean = paramsDataTableView.getSelectionModel().getSelectedItem();
                    Map<String, String> tableBean2 = new HashMap<>(tableBean);
                    paramsDatatableData.add(paramsDataTableView.getSelectionModel().getSelectedIndex(), tableBean2);
                });
                MenuItem menu_Remove = new MenuItem("删除选中行");
                menu_Remove.setOnAction(event1 -> {
                    paramsDatatableData.remove(paramsDataTableView.getSelectionModel().getSelectedIndex());
                });
                MenuItem menu_RemoveAll = new MenuItem("删除所有");
                menu_RemoveAll.setOnAction(event1 -> {
                    paramsDatatableData.clear();
                });
                paramsDataTableView.setContextMenu(new ContextMenu(menu_Copy, menu_Remove, menu_RemoveAll));
            }
        });
    }
}
