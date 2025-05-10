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

package com.tlcsdm.jfxcommon.debug;

import cn.hutool.core.net.SSLContextBuilder;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.log.StaticLog;
import com.tlcsdm.core.exception.UnExpectedResultException;
import com.tlcsdm.core.javafx.dialog.FxNotifications;
import com.tlcsdm.core.javafx.helper.ImageViewHelper;
import com.tlcsdm.core.javafx.util.Config;
import com.tlcsdm.core.javafx.util.FxmlUtil;
import com.tlcsdm.core.util.JacksonUtil;
import com.tlcsdm.jfxcommon.CommonSample;
import com.tlcsdm.jfxcommon.code.ColorCode;
import com.tlcsdm.jfxcommon.util.I18nUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.Notifications;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 端口扫描工具.
 *
 * @author unknowIfGuestInDream
 */
public class ScanPortTool extends CommonSample {

    @FXML
    protected TextField hostTextField;
    @FXML
    protected Button scanButton;
    @FXML
    protected TextField diyPortTextField;
    @FXML
    protected TextField ipFilterTextField;
    @FXML
    protected TextField portFilterTextField;
    @FXML
    protected FlowPane commonPortFlowPane;
    @FXML
    protected Button parseDomainButton;
    @FXML
    protected TextField domainIpTextField;
    @FXML
    protected Button getNatIpAddressButton;
    @FXML
    protected TextField natIpTextField;
    @FXML
    protected TextField natIpAddressTextField;
    @FXML
    protected TableView<Map<String, String>> connectStatusTableView;
    @FXML
    protected TableColumn<Map<String, String>, String> ipTableColumn;
    @FXML
    protected TableColumn<Map<String, String>, String> portTableColumn;
    @FXML
    protected TableColumn<Map<String, String>, String> statusTableColumn;
    private final ObservableList<Map<String, String>> connectStatusTableData = FXCollections.observableArrayList();
    String[] portStrings = new String[]{
        "ftp:21,22,telnet:23,smtp:25,http:80",
        "dns:53,tftp:69,snmp:161,162",
        "1158,1433,1521,2100,3128,26,69",
        "3306,3389,7001,8080,8081,110,143",
        "9080,9090,43958,443,465,995,1080",
        "79,88,113,220",
        "1-65535"
    };

    private Notifications notificationBuilder;

    @Override
    public String getSampleId() {
        return "scanPortTool";
    }

    @Override
    public String getSampleName() {
        return I18nUtils.get("common.tool.debug.scanPortTool.sampleName");
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
            ColorCode.class.getResource("/com/tlcsdm/jfxcommon/fxml/scanPortTool.fxml"),
            ResourceBundle.getBundle(I18nUtils.BASENAME, Config.defaultLocale));
        return fxmlLoader.getRoot();
    }

    @Override
    public Node getControlPanel() {
        return super.getControlPanel();
    }

    @Override
    public String getOrderKey() {
        return "scanPortTool";
    }

    @Override
    public ImageView getSampleImageIcon() {
        return ImageViewHelper.get("internet");
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
        for (String portString : portStrings) {
            CheckBox checkBox = new CheckBox(portString);
            checkBox.setId(portString);
            if (!"1-65535".equals(portString)) {
                checkBox.setSelected(true);
            }
            commonPortFlowPane.getChildren().add(checkBox);
        }
        setTableColumnMapValueFactory(ipTableColumn, "ip", true, null);
        setTableColumnMapValueFactory(portTableColumn, "port", true, null);
        setTableColumnMapValueFactory(statusTableColumn, "status", true, null);
        connectStatusTableView.setItems(connectStatusTableData);
    }

    private void initializeUI() {
        // Do nothing
    }

    @FXML
    private void scanAction(ActionEvent event) {
        try {
            List<String> portsList = new ArrayList<>();
            for (Node child : commonPortFlowPane.getChildren()) {
                CheckBox checkBox = (CheckBox) child;
                if (checkBox.isSelected()) {
                    addPort(checkBox.getText(), portsList);
                }
            }
            String diyPortString = diyPortTextField.getText();
            if (StringUtils.isNotEmpty(diyPortString)) {
                addPort(diyPortString, portsList);
            }
            String[] hosts = hostTextField.getText().split(",");
            hosts = ArrayUtils.removeElements(hosts,
                ipFilterTextField.getText().split(","));
            List<String> portFilters = Arrays.asList(
                portFilterTextField.getText().split(","));
            portsList.removeAll(portFilters);
            connectStatusTableData.clear();
            for (String host : hosts) {
                // 如果输入的是主机名，尝试获取ip地址
                InetAddress address = InetAddress.getByName(host);
                for (String port : portsList) {
                    ThreadUtil.execute(() -> {
                        Map<String, String> dataRow = new HashMap<String, String>();
                        dataRow.put("ip", host);
                        dataRow.put("port", port);
                        try {
                            // 定义套接字
                            Socket socket = new Socket();
                            // 传递ip和端口
                            SocketAddress socketAddress = new InetSocketAddress(address,
                                Integer.valueOf(port));
                            socket.connect(socketAddress, 1000);
                            // 对目标主机的指定端口进行连接，超时后连接失败
                            socket.close();
                            // 关闭端口
                            dataRow.put("status", "开放");
                            // 在文本区域里更新消息
                        } catch (Exception e) {
                            dataRow.put("status", "关闭");
                        }
                        Platform.runLater(() -> {
                            connectStatusTableData.add(dataRow);
                        });
                    });
                }
            }
        } catch (Exception e) {
            StaticLog.error("Scan exception.", e);
        }
    }

    @FXML
    private void parseDomainAction(ActionEvent event) {
        try {
            String[] hosts = hostTextField.getText().split(",");
            List<String> domainIps = new ArrayList<>();
            for (String host : hosts) {
                try {
                    InetAddress address = InetAddress.getByName(host);
                    domainIps.add(address.getHostAddress());
                } catch (UnknownHostException e) {
                    StaticLog.error("Failed to resolve domain name！", e);
                }
            }
            domainIpTextField.setText(String.join(",", domainIps));
        } catch (Exception e) {
            StaticLog.error("Exception in domain name resolution.", e);
        }
    }

    @FXML
    private void getNatIpAddressAction(ActionEvent event) {
        try {
            AtomicReference<String> result = new AtomicReference<>("");
            HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).followRedirects(
                    HttpClient.Redirect.NORMAL)
                .sslContext(SSLContextBuilder.create().build()).connectTimeout(Duration.ofMillis(1000)).build();
            HttpRequest request = HttpRequest.newBuilder(URI.create("https://ip.cn/api/index?ip=&type=0")).GET()
                .headers("Content-Type", "application/json", "User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36 Edg/105.0.1343.50")
                .build();
            var future = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(response -> {
                if (response.statusCode() != 200) {
                    throw new UnExpectedResultException(response.body());
                }
                return response.body();
            }).thenAccept(body -> {
                result.set(body);
            });
            try {
                future.get(3, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException | UnExpectedResultException e) {
                StaticLog.error(e);
            }
            var map = JacksonUtil.json2Map(result.get(), String.class, String.class);
            if (map == null || map.isEmpty()) {
                return;
            }
            natIpTextField.setText(map.get("ip"));
            natIpAddressTextField.setText(map.get("address"));
        } catch (Exception e) {
            notificationBuilder.text("Exception in obtaining external network IP");
            notificationBuilder.showError();
            StaticLog.error("Exception in obtaining external network IP：", e);
        }
    }

    //添加端口号
    private void addPort(String portString, List<String> portsList) {
        String[] ports = portString.split(",");
        for (String port : ports) {
            if (port.contains(":")) {
                portsList.add(port.split(":")[1]);
            } else if (port.contains("-")) {
                int startPort = Integer.parseInt(port.split("-")[0]);
                int entPort = Integer.parseInt(port.split("-")[1]);
                for (int i = startPort; i <= entPort; i++) {
                    portsList.add(Integer.toString(i));
                }
            } else {
                portsList.add(port);
            }
        }
    }

    private void setTableColumnMapValueFactory(TableColumn tableColumn, String name, boolean isEdit,
        Runnable onEditCommitHandle) {
        tableColumn.setCellValueFactory(new MapValueFactory(name));
        tableColumn.setCellFactory(TextFieldTableCell.<Map<String, String>>forTableColumn());
        if (isEdit) {
            tableColumn.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<Map<String, String>, String>>) t -> {
                t.getRowValue().put(name, t.getNewValue());
                if (onEditCommitHandle != null) {
                    onEditCommitHandle.run();
                }
            });
        }
    }

}
