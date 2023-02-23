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

package com.tlcsdm.smc.tools;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.Authenticator;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionUtils;
import org.controlsfx.control.action.ActionUtils.ActionTextBehavior;
import org.controlsfx.control.textfield.TextFields;

import com.tlcsdm.core.exception.UnExpectedResultException;
import com.tlcsdm.core.javafx.FxApp;
import com.tlcsdm.core.javafx.control.FxButton;
import com.tlcsdm.core.javafx.control.FxTextInput;
import com.tlcsdm.core.javafx.control.NumberTextField;
import com.tlcsdm.core.javafx.controlsfx.FxAction;
import com.tlcsdm.core.javafx.dialog.FxNotifications;
import com.tlcsdm.core.javafx.util.FxXmlUtil;
import com.tlcsdm.smc.SmcSample;
import com.tlcsdm.smc.util.I18nUtils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.net.UserPassAuthenticator;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.StaticLog;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.style.StyleUtil;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * 用于收集girret上指定用户的指摘信息
 *
 * @author unknowIfGuestInDream
 */
public class GirretReview extends SmcSample {

    private final static String defaultGirretUrl = "http://172.29.44.217/";
    private final static String paramO = "81";
    private final static String defaultParamQ = "is:closed -is:ignored (-is:wip OR owner:self) (owner:self OR reviewer:self OR assignee:self OR cc:self)";
    // cookie GerritAccount
    private TextField gerritAccountField;
    // cookie XSRF_TOKEN
    private TextField tokenField;
    // girret userName
    private TextField userNameField;
    // girret password
    private PasswordField passwdField;
    // 被查询的提交者的email
    private TextField ownerEmailField;
    // 每次查询数量
    private NumberTextField limitField;
    // 是否保留json文件到resultPath路径下
    private CheckBox reserveJsonCheck;
    // girret域名
    private TextField girretUrlField;
    // 忽略的girret number
    private TextField ignoreGirretNumberField;
    // 数据起始日期
    private DatePicker startDatePicker;

    private static HttpClient client;
    private List<Map<String, String>> changesList;
    private List<Map<String, String>> commentsList;
    private boolean changesEnd = false;

    private final Notifications notificationBuilder = FxNotifications.defaultNotify();
    FileChooser outPutChooser = new FileChooser();

    private final Action generate = FxAction.generate(actionEvent -> {
        String queryEmail = userNameField.getText();
        if (StrUtil.isNotEmpty(ownerEmailField.getText())) {
            queryEmail = StrUtil.subBefore(ownerEmailField.getText(), "@", false);
        }
        changesEnd = false;
        changesList = new ArrayList<>();
        commentsList = new ArrayList<>();
        outPutChooser.setInitialFileName("GirretComments_" + queryEmail + ".xlsx");
        File file = outPutChooser.showSaveDialog(FxApp.primaryStage);
        if (file != null) {
            try {
                if (!StrUtil.endWith(file.getName(), ".xlsx")) {
                    notificationBuilder
                            .text(I18nUtils.get("smc.tool.codeStyleLength120.button.generate.warn.message2"));
                    notificationBuilder.showWarning();
                    return;
                }
                // 变量初始化
                CookieManager manager = new CookieManager();
                manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
                HttpCookie gerritAccount = new HttpCookie("GerritAccount", gerritAccountField.getText());
                HttpCookie token = new HttpCookie("XSRF_TOKEN", tokenField.getText());
                manager.getCookieStore().add(URI.create(girretUrlField.getText()), gerritAccount);
                manager.getCookieStore().add(URI.create(girretUrlField.getText()), token);
                Authenticator authenticator = new UserPassAuthenticator(userNameField.getText(),
                        passwdField.getText().toCharArray());
                client = HttpClient.newBuilder().version(Version.HTTP_1_1).followRedirects(Redirect.NORMAL)
                        .connectTimeout(Duration.ofMillis(10000)).authenticator(authenticator).cookieHandler(manager)
                        .build();
                // changes请求路径
                String changesRequestUrl = girretUrlField.getText() + "changes/?O=%s&S=%s&n=%s&q=%s";
                // comments请求路径
                String commentsRequestUrl = girretUrlField.getText() + "changes/{}~{}/comments";
                int paramS = 0;
                String paramQ = defaultParamQ;
                if (StrUtil.isNotEmpty(ownerEmailField.getText())
                        && !ownerEmailField.getText().startsWith(userNameField.getText())) {
                    paramQ = "owner:" + ownerEmailField.getText();
                }
                String resultFileName = file.getName();
                String resultPath = file.getParent();
                outPutChooser.setInitialDirectory(file.getParentFile());
                outPutChooser.setInitialFileName(resultFileName);
                if (file.exists()) {
                    FileUtil.del(file);
                }
                int paramN = Integer.parseInt(limitField.getText());
                // 开始获取结果
                for (;;) {
                    String url = String.format(changesRequestUrl, URLEncoder.encode(paramO, StandardCharsets.UTF_8),
                            paramS, paramN, URLEncoder.encode(paramQ, StandardCharsets.UTF_8));
                    HttpRequest request = HttpRequest.newBuilder(URI.create(url)).GET().headers("Content-Type",
                            "application/json", "User-Agent",
                            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36 Edg/105.0.1343.50")
                            .build();
                    HttpResponse<String> response = null;
                    response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    if (response.statusCode() == 200) {
                        String result = response.body();
                        if (result.startsWith(")]}'")) {
                            result = result.substring(4);
                        }
                        JSONArray array = JSONUtil.parseArray(result);
                        handleChanges(array, paramN);
                        if (changesEnd) {
                            break;
                        }
                        paramS = paramS + paramN;
                    } else {
                        throw new UnExpectedResultException(response.body());
                    }
                }
                handleComments(commentsRequestUrl, resultPath, resultFileName);
                bindUserData();
            } catch (Exception e) {
                // FxAlerts.exception(e);
                e.printStackTrace();
            }
        }
    });

    private final Collection<? extends Action> actions = List.of(generate);

    @Override
    public Node getPanel(Stage stage) {
        initComponment();
        GridPane grid = new GridPane();
        grid.setVgap(12);
        grid.setHgap(12);
        grid.setPadding(new Insets(24));

        ToolBar toolBar = ActionUtils.createToolBar(actions, ActionTextBehavior.SHOW);
        toolBar.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        toolBar.setPrefWidth(Double.MAX_VALUE);

        Label gerritAccountLabel = new Label("GerritAccount: ");
        gerritAccountField = TextFields.createClearableTextField();
        gerritAccountField.setPrefWidth(Double.MAX_VALUE);

        Label tokenLabel = new Label("XSRF_TOKEN: ");
        tokenField = TextFields.createClearableTextField();

        Label userNameLabel = new Label(I18nUtils.get("smc.tool.girretReview.label.userName") + ": ");
        userNameField = TextFields.createClearableTextField();

        Label passwdLabel = new Label(I18nUtils.get("smc.tool.girretReview.label.passwd") + ": ");
        passwdField = TextFields.createClearablePasswordField();

        Label ownerEmailLabel = new Label(I18nUtils.get("smc.tool.girretReview.label.ownerEmail") + ": ");
        ownerEmailField = TextFields.createClearableTextField();

        Label limitLabel = new Label(I18nUtils.get("smc.tool.girretReview.label.limit") + ": ");
        limitField = new NumberTextField(BigDecimal.valueOf(50));

        Label ignoreGirretNumberLabel = new Label(
                I18nUtils.get("smc.tool.girretReview.label.ignoreGirretNumber") + ": ");
        ignoreGirretNumberField = TextFields.createClearableTextField();
        ignoreGirretNumberField.setPromptText(I18nUtils.get("smc.tool.textfield.promptText.list"));

        Label startDateLabel = new Label(I18nUtils.get("smc.tool.girretReview.label.startDate") + ": ");
        startDatePicker = new DatePicker();
        Button startDateClearButton = FxButton.clear();
        startDateClearButton.setOnAction(arg0 -> {
            startDatePicker.setValue(null);
        });
        startDatePicker.setEditable(false);

        Label reserveJsonLabel = new Label(I18nUtils.get("smc.tool.girretReview.label.reserveJson") + ": ");
        reserveJsonCheck = new CheckBox();

        Label girretUrlLabel = new Label(I18nUtils.get("smc.tool.girretReview.label.girretUrl") + ": ");
        girretUrlField = new TextField();

        // 初始化赋值
        limitField.setNumber(BigDecimal.valueOf(50));
        reserveJsonCheck.setSelected(true);
        girretUrlField.setText(defaultGirretUrl);

        userData.put("gerritAccount", gerritAccountField);
        userData.put("token", tokenField);
        userData.put("userName", userNameField);
        userData.put("passwd", passwdField);
        userData.put("ownerEmail", ownerEmailField);
        userData.put("limit", limitField);
        userData.put("ignoreGirretNumber", ignoreGirretNumberField);
        userData.put("startDate", startDatePicker);
        userData.put("reserveJson", reserveJsonCheck);
        userData.put("girretUrl", girretUrlField);
        userData.put("outPut", outPutChooser);

        grid.add(toolBar, 0, 0, 3, 1);
        grid.add(gerritAccountLabel, 0, 1);
        grid.add(gerritAccountField, 1, 1, 2, 1);
        grid.add(tokenLabel, 0, 2);
        grid.add(tokenField, 1, 2, 2, 1);
        grid.add(userNameLabel, 0, 3);
        grid.add(userNameField, 1, 3, 2, 1);
        grid.add(passwdLabel, 0, 4);
        grid.add(passwdField, 1, 4, 2, 1);
        grid.add(ownerEmailLabel, 0, 5);
        grid.add(ownerEmailField, 1, 5, 2, 1);
        grid.add(limitLabel, 0, 6);
        grid.add(limitField, 1, 6, 2, 1);
        grid.add(ignoreGirretNumberLabel, 0, 7);
        grid.add(ignoreGirretNumberField, 1, 7, 2, 1);
        grid.add(startDateLabel, 0, 8);
        grid.add(startDateClearButton, 1, 8);
        grid.add(startDatePicker, 2, 8);
        grid.add(reserveJsonLabel, 0, 9);
        grid.add(reserveJsonCheck, 1, 9, 2, 1);
        grid.add(girretUrlLabel, 0, 10);
        grid.add(girretUrlField, 1, 10, 2, 1);

        return grid;
    }

    @Override
    public Node getControlPanel() {
        String content = """
                GerritAccount&XSRF_TOKEN{tokenDesc}
                {userName}&{passwd}{girretUserDesc}
                {ownerEmail}{ownerEmailDesc}
                {limit}{limitDesc}
                {ignoreGirretNumber}{ignoreGirretNumberDesc}
                {startDate}: {startDateDesc}
                {reserveJson}: {reserveJsonDesc}
                {girretUrl}{girretUrlDesc}
                """;
        Map<String, String> map = new HashMap<>(32);
        map.put("tokenDesc", I18nUtils.get("smc.tool.girretReview.control.textarea1"));
        map.put("userName", I18nUtils.get("smc.tool.girretReview.label.userName"));
        map.put("passwd", I18nUtils.get("smc.tool.girretReview.label.passwd"));
        map.put("girretUserDesc", I18nUtils.get("smc.tool.girretReview.control.textarea2"));
        map.put("ownerEmail", I18nUtils.get("smc.tool.girretReview.label.ownerEmail"));
        map.put("ownerEmailDesc", I18nUtils.get("smc.tool.girretReview.control.textarea3"));
        map.put("limit", I18nUtils.get("smc.tool.girretReview.label.limit"));
        map.put("limitDesc", I18nUtils.get("smc.tool.girretReview.control.textarea4"));
        map.put("ignoreGirretNumber", I18nUtils.get("smc.tool.girretReview.label.ignoreGirretNumber"));
        map.put("ignoreGirretNumberDesc", I18nUtils.get("smc.tool.girretReview.control.textarea5"));
        map.put("startDate", I18nUtils.get("smc.tool.girretReview.label.startDate"));
        map.put("startDateDesc", I18nUtils.get("smc.tool.girretReview.control.textarea6"));
        map.put("reserveJson", I18nUtils.get("smc.tool.girretReview.label.reserveJson"));
        map.put("reserveJsonDesc", I18nUtils.get("smc.tool.girretReview.control.textarea7"));
        map.put("girretUrl", I18nUtils.get("smc.tool.girretReview.label.girretUrl"));
        map.put("girretUrlDesc", I18nUtils.get("smc.tool.girretReview.control.textarea8"));
        return FxTextInput.textArea(StrUtil.format(content, map));
    }

    @Override
    protected void initializeUserData() {
        super.initializeUserData();
        // 时间超过12小时，清除girret token信息
        String value = FxXmlUtil.get(getSampleXmlPrefix(), "lastUpdateDate", "");
        if (StrUtil.isEmpty(value)) {
            gerritAccountField.setText("");
            tokenField.setText("");
            return;
        }
        try {
            Duration dur = Duration.between(LocalDateTimeUtil.parse(value, DatePattern.NORM_DATETIME_FORMATTER),
                    LocalDateTimeUtil.now());
            if (dur.toHours() >= 12) {
                gerritAccountField.setText("");
                tokenField.setText("");
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    protected void bindUserDataBefore() {
        super.bindUserDataBefore();
        userData.put("lastUpdateDate", LocalDateTimeUtil.now().format(DatePattern.NORM_DATETIME_FORMATTER));
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public String getSampleId() {
        return "girretReview";
    }

    @Override
    public String getSampleName() {
        return I18nUtils.get("smc.sampleName.girretReview");
    }

    @Override
    public String getSampleVersion() {
        return "1.0.0";
    }

    @Override
    public String getOrderKey() {
        return "GirretReview";
    }

    @Override
    public String getSampleDescription() {
        return I18nUtils.get("smc.sampleName.girretReview.description");
    }

    // 初始化组件
    private void initComponment() {
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("excel file", "*.xlsx");
        outPutChooser.getExtensionFilters().add(extFilter);
    }

    /**
     * girret changes 数据处理
     */
    private void handleChanges(JSONArray array, int paramN) {
        for (int i = 0; i < array.size(); i++) {
            if (changesFilter(array.get(i), array, i)) {
                String submitted = String.valueOf(array.getByPath("[" + i + "].submitted")).replace(".000000000", "");
                if (startDatePicker.getValue() != null) {
                    if ((startDatePicker.getValue().toString() + " 00:00:00").compareTo(submitted) >= 0) {
                        changesEnd = true;
                        break;
                    }
                }
                Map<String, String> map = new HashMap<>();
                map.put("girretNum", String.valueOf(array.getByPath("[" + i + "]._number")));
                map.put("project", String.valueOf(array.getByPath("[" + i + "].project")));
                map.put("changeId", String.valueOf(array.getByPath("[" + i + "].change_id")));
                map.put("subject", String.valueOf(array.getByPath("[" + i + "].subject")));
                map.put("created", String.valueOf(array.getByPath("[" + i + "].created")).replace(".000000000", ""));
                map.put("submitted", submitted);
                map.put("insertions", String.valueOf(array.getByPath("[" + i + "].insertions")));
                map.put("deletions", String.valueOf(array.getByPath("[" + i + "].deletions")));
                map.put("ownerUserName", String.valueOf(array.getByPath("[" + i + "].owner.username")));
                changesList.add(map);
            }
        }
        if (array.size() < paramN) {
            changesEnd = true;
        }
        // 可增加后续数据处理
    }

    /**
     * changes数据过滤
     */
    private boolean changesFilter(Object obj, JSONArray array, int i) {
        // 未合并的提交不统计
        if (!Objects.equals("MERGED", array.getByPath("[" + i + "].status"))) {
            return false;
        }
        // 提交者不是userName的不统计
        String queryEmail = array.getByPath("[" + i + "].owner.email").toString();
        if (StrUtil.isNotEmpty(ownerEmailField.getText())) {
            if (!ownerEmailField.getText().startsWith(userNameField.getText())) {
                if (!Objects.equals(ownerEmailField.getText(), queryEmail)) {
                    return false;
                }
            } else {
                if (!queryEmail.startsWith(userNameField.getText())) {
                    return false;
                }
            }
        } else {
            if (!queryEmail.startsWith(userNameField.getText())) {
                return false;
            }
        }
        List<String> ignoreGirretNumberList = StrUtil.splitTrim(ignoreGirretNumberField.getText(), ",");
        if (ignoreGirretNumberList.size() > 0) {
            if (ignoreGirretNumberList.contains(array.getByPath("[" + i + "].owner._number"))) {
                return false;
            }
        }
        // 可添加自定义过滤条件
        return true;
    }

    /**
     * girret comments 数据处理
     */
    private void handleComments(String commentsRequestUrl, String resultPath, String resultFileName)
            throws IOException, InterruptedException {
        if (changesList.size() == 0) {
            notificationBuilder.text("No need changes");
            notificationBuilder.showInformation();
        }
        for (int i = 0; i < changesList.size(); i++) {
            String url = StrUtil.format(commentsRequestUrl,
                    URLEncoder.encode(changesList.get(i).get("project"), StandardCharsets.UTF_8),
                    changesList.get(i).get("girretNum"));
            HttpRequest request = HttpRequest.newBuilder(URI.create(url)).GET().headers("Content-Type",
                    "application/json", "User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36 Edg/105.0.1343.50")
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                String result = response.body();
                if (result.startsWith(")]}'")) {
                    result = result.substring(4);
                }
                JSONObject jsonObject = JSONUtil.parseObj(result);
                jsonObject.remove("/PATCHSET_LEVEL");
                for (Map.Entry<String, Object> vo : jsonObject.entrySet()) {
                    JSONArray array = JSONUtil.parseArray(vo.getValue());
                    for (int j = 0; j < array.size(); j++) {
                        String commentMessage = String.valueOf(array.getByPath("[" + j + "].message"));
                        String commentAuthorUserName = String.valueOf(array.getByPath("[" + j + "].author.username"));
                        Map<String, String> comment = new HashMap<>(changesList.get(i));
                        if ("Done".equals(commentMessage)
                                || comment.get("ownerUserName").equals(commentAuthorUserName)) {
                            continue;
                        }
                        comment.put("commentFileName", vo.getKey());
                        comment.put("commentAuthorUserName", commentAuthorUserName);
                        comment.put("commentAuthorName", String.valueOf(array.getByPath("[" + j + "].author.name")));
                        comment.put("commentMessage", commentMessage);
                        commentsList.add(comment);
                    }
                }
            } else {
                notificationBuilder.text("comments request call failed");
                notificationBuilder.showError();
                StaticLog.error("comments request call failed. {}", response.body());
                break;
            }
            ThreadUtil.safeSleep(200);
        }
        handleResult(resultPath, resultFileName);
    }

    /**
     * 数据结果处理
     */
    private void handleResult(String resultPath, String resultFileName) {
        if (commentsList.size() == 0) {
            notificationBuilder.text("No need comments");
            notificationBuilder.showInformation();
            return;
        }
        ExcelWriter writer = ExcelUtil.getWriter(FileUtil.file(resultPath, resultFileName));
        setExcelStyle(writer);
        writer.setOnlyAlias(true);
        writer.addHeaderAlias("girretNum", I18nUtils.get("smc.tool.girretReview.result.girretNum"));
        writer.addHeaderAlias("changeId", I18nUtils.get("smc.tool.girretReview.result.changeId"));
        writer.addHeaderAlias("subject", I18nUtils.get("smc.tool.girretReview.result.subject"));
        writer.addHeaderAlias("commentAuthorName", I18nUtils.get("smc.tool.girretReview.result.commentAuthorName"));
        writer.addHeaderAlias("commentMessage", I18nUtils.get("smc.tool.girretReview.result.commentMessage"));
        writer.addHeaderAlias("submitted", I18nUtils.get("smc.tool.girretReview.result.submitted"));
        writer.addHeaderAlias("created", I18nUtils.get("smc.tool.girretReview.result.created"));
        writer.addHeaderAlias("insertions", I18nUtils.get("smc.tool.girretReview.result.insertions"));
        writer.addHeaderAlias("deletions", I18nUtils.get("smc.tool.girretReview.result.deletions"));
        writer.addHeaderAlias("commentFileName", I18nUtils.get("smc.tool.girretReview.result.commentFileName"));
        writer.write(commentsList, true);
        writer.close();
        // 保留json结果文件
        if (reserveJsonCheck.isSelected()) {
            FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(changesList),
                    FileUtil.file(resultPath,
                            LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.PURE_DATETIME_PATTERN)
                                    + "-changes.json"));
            FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(commentsList),
                    FileUtil.file(resultPath,
                            LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.PURE_DATETIME_PATTERN)
                                    + "-comments.json"));
        }
        notificationBuilder.text(I18nUtils.get("smc.tool.fileDiff.button.generate.success"));
        notificationBuilder.showInformation();
    }

    // 设置生成的excel样式
    private void setExcelStyle(ExcelWriter writer) {
        writer.getStyleSet().setAlign(HorizontalAlignment.LEFT, VerticalAlignment.CENTER);
        writer.getHeadCellStyle().setAlignment(HorizontalAlignment.CENTER);
        CellStyle style = writer.getStyleSet().getHeadCellStyle();
        StyleUtil.setColor(style, IndexedColors.LIGHT_YELLOW, FillPatternType.SOLID_FOREGROUND);
        writer.setColumnWidth(0, 15);
        writer.setColumnWidth(1, 45);
        writer.setColumnWidth(2, 50);
        writer.setColumnWidth(3, 15);
        writer.setColumnWidth(4, 60);
        writer.setColumnWidth(5, 40);
        writer.setColumnWidth(6, 40);
        writer.setColumnWidth(7, 10);
        writer.setColumnWidth(8, 10);
        writer.setColumnWidth(9, 80);
    }

}
