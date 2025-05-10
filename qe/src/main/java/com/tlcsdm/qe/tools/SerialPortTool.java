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

import cn.hutool.log.StaticLog;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.tlcsdm.core.javafx.helper.LayoutHelper;
import com.tlcsdm.core.javafx.util.Config;
import com.tlcsdm.core.javafx.util.FxmlUtil;
import com.tlcsdm.core.util.CoreConstant;
import com.tlcsdm.qe.QeSample;
import com.tlcsdm.qe.util.I18nUtils;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * 串口助手
 *
 * @author unknowIfGuestInDream
 * @since 1.0.0
 */
public class SerialPortTool extends QeSample implements Initializable {

    //串口名称选择器
    @FXML
    private ComboBox<SerialPort> serPort;
    //波特率选择器
    @FXML
    private ComboBox<String> serPortSpeed;
    //检验位选择器
    @FXML
    private ComboBox<String> serPortCheckBit;
    //数据位选择器
    @FXML
    private ComboBox<String> serPortDataBit;
    //停止位选择器
    @FXML
    private ComboBox<String> serPortStopBit;
    //流控选择器
    @FXML
    private ComboBox<String> serFlowControl;
    @FXML
    private Button serPortOpenBtn;
    //16进制接收显示开关
    @FXML
    private CheckBox recvShowHex;
    //显示时间
    @FXML
    private CheckBox recvShowTime;
    //暂停接收
    @FXML
    private CheckBox recvStopShow;
    @FXML
    private Button recvClear;
    //16进制发送开关
    @FXML
    private CheckBox sendHex;
    //定时发送开关
    @FXML
    private CheckBox sendCycle;
    @FXML
    private TextField sendCycleRap;
    @FXML
    private Button sendClear;
    @FXML
    private Label sendCount;
    @FXML
    private Label recvCount;
    @FXML
    private Button CountReset;
    @FXML
    private TextArea sendTextAear;
    @FXML
    private TextArea recvTextAear;
    @FXML
    private Button sendBtn;

    private final Timeline circularSending = new Timeline();
    private volatile long waitTime = 1000;

    @Override
    public Node getPanel(Stage stage) {
        FXMLLoader fxmlLoader = FxmlUtil.loadFxmlFromResource(
            getClass().getResource("/com/tlcsdm/qe/fxml/serialPortTool.fxml"),
            ResourceBundle.getBundle(I18nUtils.BASENAME, Config.defaultLocale));
        return fxmlLoader.getRoot();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public String getSampleId() {
        return "serialPortTool";
    }

    @Override
    public String getSampleName() {
        return "SerialPort Tool";
    }

    @Override
    public String getOrderKey() {
        return "serialPortTool";
    }

    @Override
    public String getSampleDescription() {
        return "SerialPort Tool";
    }

    @Override
    public String getSampleVersion() {
        return "1.0.1";
    }

    @Override
    public boolean hasControlPanel() {
        return false;
    }

    @Override
    public boolean isVisible() {
        String value = System.getProperty(CoreConstant.JVM_WORKENV);
        return CoreConstant.JVM_WORKENV_DEV.equals(value) || CoreConstant.JVM_WORKENV_TEST.equals(value);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeUserDataBindings();
        initializeBindings();
        initializeUserData();

        initializeUI();
    }

    /**
     * 设置刷新串口信息按钮被点击时的事件处理器: 重新获取本机当前串口信息
     */
    //    private void setRefreshSerialPortInfoButtonOnActionEventHandler() {
    //        refreshSerialPortInfoButton.setOnAction(event -> {
    //            ObservableList<String> items = serialPortNameChoiceBox.getItems();
    //            items.clear();
    //            items.addAll(SerialPortManager.findPorts());
    //        });
    //    }

    /**
     * 设置连接按钮被点击时的事件处理器: 连接串口，更新连接状态
     */
    //    private void setConnectButtonEventHandlerOnAction() {
    //        connectButton.setOnAction(event -> {
    //            connectStateLabel.setText(ConnectionStateEnum.CONNECTING.getDescription());
    //            connectStateLabel.setStyle(ConnectionStateEnum.CONNECTING.getTextColor());
    //            String serialPortName = serialPortNameChoiceBox.getValue();
    //            String baudRate = baudRateComboBox.getValue();
    //            try {
    //                SerialPortManager.closePort(serialPort);
    //                serialPort = SerialPortManager.openPort(serialPortName, Integer.parseInt(baudRate));
    //                SerialPortManager.addListener(
    //                    serialPort, new ElectronicScaleParser(), new ElectronicScaleProcessor(dataTextArea));
    //                connectStateLabel.setText(ConnectionStateEnum.CONNECTED.getDescription());
    //                connectStateLabel.setStyle(ConnectionStateEnum.CONNECTED.getTextColor());
    //            } catch (Exception e) {
    //                log.error("串口名称: {}, 波特率: {}, 连接失败:", serialPortName, baudRate, e);
    //                connectStateLabel.setText(ConnectionStateEnum.CONNECT_FAIL.getDescription());
    //                connectStateLabel.setStyle(ConnectionStateEnum.CONNECT_FAIL.getTextColor());
    //            }
    //        });
    //    }

    /**
     * 设置断开连接按钮点击时的事件处理器: 断开串口连接
     */
    //    private void setDisconnectButtonOnActionEventHandler() {
    //        disconnectButton.setOnAction(event -> {
    //            SerialPortManager.closePort(serialPort);
    //            connectStateLabel.setText(ConnectionStateEnum.DISCONNECTED.getDescription());
    //            connectStateLabel.setStyle(ConnectionStateEnum.DISCONNECTED.getTextColor());
    //        });
    //    }
    @Override
    public ImageView getSampleImageIcon() {
        return LayoutHelper.iconView(getClass().getResource("/com/tlcsdm/qe/static/icon/serialport.png"));
    }

    //https://gitee.com/aSingleDragon/rs232-client/blob/main/src/main/java/pers/hll/rs232/rs232client/controller/RS232ManageController.java
    // https://github.com/yiaoBang/SerialPortToolFX/blob/master/src/main/java/com/yiaoBang/serialPortToolFX/view/SerialPortView.java

    public void initializeUI() {
        //无限循环发送
        circularSending.setCycleCount(Timeline.INDEFINITE);

        //        recvCount
        //        sendNumber.textProperty().bind(viewModel.getSEND_LONG_PROPERTY().asString());
        //        receiveNumber.textProperty().bind(viewModel.getRECEIVE_LONG_PROPERTY().asString());

        //        // 初始化常用波特率列表
        //        baudRateComboBox.getItems().addAll("9600", "4800", "2400", "1200");
        //        // 设置波特率默认值
        //        baudRateComboBox.setValue("9600");
        //        // 波特率 不接受非数值输入
        //        baudRateComboBox.getEditor().addEventFilter(KeyEvent.KEY_TYPED, event -> {
        //            // 获取键入的字符
        //            String character = event.getCharacter();
        //            // 如果不是数值或删除 则消耗这个事件
        //            if (!(character.matches("\\d") || character.isEmpty())) {
        //                ViewUtil.alertError("只能输入数字(0-9)!");
        //                event.consume();
        //            }
        //        });

        // 初始化当前计算机串口信息
        //        serialPortNameChoiceBox.getItems().addAll(SerialPortManager.findPorts());
        //        if (!serialPortNameChoiceBox.getItems().isEmpty()) {
        //            serialPortNameChoiceBox.setValue(serialPortNameChoiceBox.getItems().get(0));
        //        }
        serPort.setCellFactory(cellFactory);
        serPort.setButtonCell(cellFactory.call(null));
        SerialPort[] ports = SerialPort.getCommPorts();
        if (ports.length != 0) {
            for (SerialPort s : ports) {
                serPort.getItems().add(s);
            }
            serPort.getSelectionModel().select(0);
        }

        String[] speeds = new String[]{
            "100", "300", "600", "1200", "2400", "4800", "9600", "14400", "19200", "38400", "56000", "57600", "115200", "128000", "256000"
        };
        for (String s : speeds) {
            serPortSpeed.getItems().add(s);
        }
        serPortSpeed.setValue("9600");

        //串口检验位设置
        String[] checks = new String[]{
            "NONE", "ODD", "EVEN", "MARK", "SPACE"
        };
        for (String s : checks) {
            serPortCheckBit.getItems().add(s);
        }
        serPortCheckBit.setValue("NONE");

        //数据位设置
        String[] databits = new String[]{
            "5", "6", "7", "8"
        };
        for (String s : databits) {
            serPortDataBit.getItems().add(s);
        }
        serPortDataBit.setValue("8");

        //停止位设置
        String[] stopbits = new String[]{
            "1", "2"
        };
        for (String s : stopbits) {
            serPortStopBit.getItems().add(s);
        }
        serPortStopBit.setValue("1");

        //流控
        //        String[] flowControl = new String[]{
        //            "100", "300", "600", "1200"
        //        };
        //        for (String s : speeds) {
        //            serPortSpeed.getItems().add(s);
        //        }
        //        serPortSpeed.setValue("9600");
        //serFlowControl.getItems().addAll(SerialPort.FLOW_CONTROL_DISABLED);

        //循环发送的等待时间(ms)
        sendCycleRap.textProperty().addListener((o, oldValue, newValue) -> {
            try {
                waitTime = Integer.parseInt(newValue);
                if (waitTime < 1) {
                    waitTime = 1;
                }
            } catch (NumberFormatException e) {
                sendCycleRap.setText(oldValue);
            }
        });

        serPortOpenBtn.setOnAction((ActionEvent event) -> {
            SerialPort serialPort = serPort.getSelectionModel().getSelectedItem();
            if (serialPort == null) {
                return;
            }
            if (serialPort.isOpen()) {
                serialPort.closePort();
                serPortOpenBtn.setText("打开");
                serPort.setDisable(false);
                serPortSpeed.setDisable(false);
                serPortCheckBit.setDisable(false);
                serPortDataBit.setDisable(false);
                serPortStopBit.setDisable(false);
            } else {
                serialPortDataListener listener = new serialPortDataListener();
                serialPort.addDataListener(listener);
                serialPort.openPort();
                //SerialPort.ONE_STOP_BIT
                //SerialPort.NO_PARITY
                serialPort.setComPortParameters(Integer.parseInt(serPortSpeed.getValue()),
                    Integer.parseInt(serPortDataBit.getValue()), Integer.parseInt(serPortStopBit.getValue()),
                    serPortCheckBit.getValue().equals("NONE") ? 0 : serPortCheckBit.getValue().equals("ODD") ? 1 :
                        serPortCheckBit.getValue().equals("EVEN") ? 2 : serPortCheckBit.getValue().equals(
                            "SPACE") ? 3 : 0);
                serialPort.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);
                serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING | SerialPort.TIMEOUT_WRITE_BLOCKING,
                    1000, 1000);

                serPortOpenBtn.setText("关闭");
                serPort.setDisable(true);
                serPortSpeed.setDisable(true);
                serPortCheckBit.setDisable(true);
                serPortDataBit.setDisable(true);
                serPortStopBit.setDisable(true);
            }
        });

        sendBtn.setOnAction(event -> {
            SerialPort serialPort = serPort.getSelectionModel().getSelectedItem();
            if (null == serialPort || (!serialPort.isOpen())) {
                // new AlertBox().display("错误", "请先打开串口");
                return;
            }
            try {
                if (sendHex.isSelected()) {
                    byte[] bytes = hexStringToBytes(sendTextAear.getText());
                    serialPort.writeBytes(bytes, bytes.length);
                    sendCount.setText(String.valueOf(
                        (Integer.parseInt(sendCount.getText()) + hexStringToBytes(sendTextAear.getText()).length)));
                } else {
                    byte[] bytes = sendTextAear.getText().getBytes();
                    serialPort.writeBytes(bytes, bytes.length);
                    sendCount.setText(String.valueOf(
                        (Integer.parseInt(sendCount.getText()) + sendTextAear.getText().getBytes().length)));
                }

            } catch (Exception e) {
                //new AlertBox().display("发送数据错误", e.getMessage());
            }
        });

        recvClear.setOnAction(event -> {
            recvTextAear.setText("");
        });
        sendHex.setOnAction(event -> {
            if (!sendHex.isSelected())
                try {
                    sendTextAear.setText(new String(hexStringToBytes(sendTextAear.getText())));
                } catch (Exception e) {
                    //new AlertBox().display("非法16进制字符", e.getMessage());
                }
            else {
                sendTextAear.setText(bytesToHexString(sendTextAear.getText().getBytes()));
            }
        });
        sendClear.setOnAction(event -> {
            sendTextAear.setText("");
        });
        CountReset.setOnAction(event -> {
            sendCount.setText("0");
            recvCount.setText("0");
        });
        //        sendCycle.setOnAction(event -> {
        //            if (null == serialPort || (!serialPort.isOpened())) {
        //                //new AlertBox().display("错误", "请先打开串口");
        //                sendCycle.setSelected(false);
        //                return;
        //            }
        //            try {
        //                if (sendCycle.isSelected()) {
        //                    sendBtn.setDisable(true);
        //                    sendCycleRap.setDisable(true);
        //                    t = new Timer();
        //
        //                    byte[] sendData = sendHex.isSelected() ? hexStringToBytes(
        //                        sendTextAear.getText()) : sendTextAear.getText().getBytes();
        //
        //                    TimerTask task = new TimerTask() {
        //                        public void run() {
        //                            // task to run goes here
        //                            //System.out.println("Hello !!!");
        //                            try {
        //                                serialPort.writeBytes(sendData);
        //                                Platform.runLater(() -> {
        //                                    sendCount.setText(
        //                                        String.valueOf((Integer.parseInt(sendCount.getText()) + sendData.length)));
        //                                });
        //                            } catch (SerialPortException e) {
        //                                new AlertBox().display("循环发送错误", e.getMessage());
        //                            }
        //                        }
        //                    };
        //                    t.schedule(task, 0, new Long(sendCycleRap.getText()));
        //                } else {
        //                    t.cancel();
        //                    sendBtn.setDisable(false);
        //                    sendCycleRap.setDisable(false);
        //                }
        //            } catch (Exception e) {
        //                //new AlertBox().display("循环发送错误", e.getMessage());
        //            }
        //        });
    }

    Callback<ListView<SerialPort>, ListCell<SerialPort>> cellFactory = listView -> new ListCell<SerialPort>() {
        @Override
        protected void updateItem(SerialPort decorator, boolean empty) {
            super.updateItem(decorator, empty);
            if (empty) {
                setText("");
            } else {
                setText(decorator.getSystemPortName());
            }
        }
    };

    @Override
    public void initializeBindings() {
        super.initializeBindings();
        //        txtLinebreakpos.disableProperty().bind(enableLinebreakpos.selectedProperty().not());
        //        btnJsCompress.disableProperty().bind(txtJsCode.textProperty().isEmpty());
        //        btnCssCompress.disableProperty().bind(txtCssCode.textProperty().isEmpty());
        //        txtCssLinebreakpos.disableProperty().bind(enableCssLinebreakpos.selectedProperty().not());
        //        FileChooserUtil.setOnDragByOpenFile(txtJsCode);
        //        FileChooserUtil.setOnDragByOpenFile(txtCssCode);
    }

    @Override
    public void initializeUserDataBindings() {
        super.initializeUserDataBindings();
        //        userData.put("enableMunge", enableMunge);
        //        userData.put("enableVerbose", enableVerbose);
        //        userData.put("enableOptimizations", enableOptimizations);
        //        userData.put("enablePreserveAllSemiColons", enablePreserveAllSemiColons);
        //        userData.put("enableLinebreakpos", enableLinebreakpos);
        //        userData.put("txtLinebreakpos", txtLinebreakpos);
        //        userData.put("enableCssLinebreakpos", enableCssLinebreakpos);
        //        userData.put("txtCssLinebreakpos", txtCssLinebreakpos);
    }

    @Override
    public void dispose() {
        SerialPort serialPort = serPort.getSelectionModel().getSelectedItem();
        if (serialPort == null) {
            return;
        }
        if (serialPort.isOpen()) {
            serialPort.closePort();
        }
    }

    /**
     * 获得当前计算机所有的串口的名称列表
     *
     * @return 串口名称列表
     */
    private List<String> findPorts() {
        // 获得当前所有可用串口
        SerialPort[] serialPorts = SerialPort.getCommPorts();
        List<String> portNameList = new ArrayList<>();
        // 将可用串口名添加到List并返回该List
        for (SerialPort serialPort : serialPorts) {
            portNameList.add(serialPort.getSystemPortName());
        }
        // 去重
        portNameList = portNameList.stream().distinct().collect(Collectors.toList());
        return portNameList;
    }

    /**
     * 打开串口
     *
     * @param portName 端口名称
     * @param baudRate 波特率
     * @return 串口对象
     */
    private SerialPort openPort(String portName, int baudRate) {
        SerialPort serialPort = SerialPort.getCommPort(portName);
        if (baudRate > 0) {
            serialPort.setBaudRate(baudRate);
        }
        if (!serialPort.isOpen()) {
            serialPort.openPort(1000);
        } else {
            return serialPort;
        }
        // 设置串口的控制流，可以设置为disabled，或者CTS, RTS/CTS, DSR, DTR/DSR, Xon, Xoff, Xon/Xoff等
        serialPort.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);
        // 第一个参数为波特率，默认9600；
        // 第二个参数为每一位的大小，默认8，可以输入5到8之间的值；
        // 第三个参数为停止位大小，只接受内置常量，可以选择(ONE_STOP_BIT, ONE_POINT_FIVE_STOP_BITS, TWO_STOP_BITS)；
        // 第四位为校验位，同样只接受内置常量，可以选择 NO_PARITY, EVEN_PARITY, ODD_PARITY, MARK_PARITY,SPACE_PARITY。
        serialPort.setComPortParameters(baudRate, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
        // 超时设置
        serialPort.setComPortTimeouts(
            SerialPort.TIMEOUT_READ_BLOCKING | SerialPort.TIMEOUT_WRITE_BLOCKING,
            1000, 1000);
        return serialPort;
    }

    /**
     * 从串口读取数据
     *
     * @param serialPort 当前已建立连接的SerialPort对象
     * @return 读取到的数据
     */
    private byte[] readFromPort(SerialPort serialPort) {
        byte[] resultData = new byte[0];
        try {
            if (!serialPort.isOpen()) {
                return new byte[0];
            }
            int i = 0;
            while (serialPort.bytesAvailable() > 0 && i++ < 5) {
                Thread.sleep(20);
            }
            byte[] readBuffer = new byte[serialPort.bytesAvailable()];
            int numRead = serialPort.readBytes(readBuffer, readBuffer.length);
            if (numRead > 0) {
                resultData = readBuffer;
            }
        } catch (InterruptedException e) {
            StaticLog.error("InterruptedException: ", e);
            Thread.currentThread().interrupt();
        }
        return resultData;
    }

    private final static class serialPortDataListener implements SerialPortDataListener {
        @Override
        public int getListeningEvents() {
            return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
        }

        @Override
        public void serialEvent(SerialPortEvent event) {
            byte[] newData = event.getReceivedData();
            System.out.println("Received data of size: " + newData.length);
            for (int i = 0; i < newData.length; ++i) {
                System.out.print((char) newData[i]);
            }
            System.out.println("\n");
        }
    }

    private byte[] hexStringToBytes(String hexString) throws Exception {
        if (hexString == null || hexString.trim().isEmpty()) {
            return new byte[0];
        }
        hexString = hexString.replace(" ", "");
        if (hexString.length() % 2 != 0) {
            hexString = "0" + hexString;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * Convert char to byte
     *
     * @param c char
     * @return byte
     */
    private byte charToByte(char c) throws Exception {
        int i = "0123456789ABCDEF".indexOf(c);
        if (i == -1) {
            throw new Exception("非法的十六进制值");
        }
        return (byte) i;
    }

    public String bytesToHexString(byte[] bArray) {
        if (bArray == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

}
