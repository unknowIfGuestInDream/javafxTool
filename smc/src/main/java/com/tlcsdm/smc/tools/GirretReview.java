package com.tlcsdm.smc.tools;

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
import com.tlcsdm.core.javafx.FxApp;
import com.tlcsdm.core.javafx.control.FxTextInput;
import com.tlcsdm.core.javafx.control.NumberTextField;
import com.tlcsdm.core.javafx.dialog.FxNotifications;
import com.tlcsdm.core.javafx.helper.LayoutHelper;
import com.tlcsdm.smc.SmcSample;
import com.tlcsdm.smc.util.I18nUtils;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionUtils;
import org.controlsfx.control.action.ActionUtils.ActionTextBehavior;
import org.controlsfx.control.textfield.TextFields;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 检测指定路径下文件内容长度是否超过120
 *
 * @author unknowIfGuestInDream
 */
public class GirretReview extends SmcSample {

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

	private final Notifications notificationBuilder = FxNotifications.defaultNotify();
	FileChooser outPutChooser = new FileChooser();

	private final Action generate = new Action(I18nUtils.get("smc.tool.button.generate"), actionEvent -> {
		outPutChooser.setInitialFileName(
				"GirretComments_" + StrUtil.subBefore(ownerEmailField.getText(), "@", false) + ".xlsx");
		File file = outPutChooser.showSaveDialog(FxApp.primaryStage);
		if (file != null) {
			if (!StrUtil.endWith(file.getName(), ".xlsx")) {
				notificationBuilder.text(I18nUtils.get("smc.tool.codeStyleLength120.button.generate.warn.message2"));
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
			HttpClient client = HttpClient.newBuilder().version(Version.HTTP_1_1).followRedirects(Redirect.NORMAL)
					.connectTimeout(Duration.ofMillis(5000)).authenticator(authenticator).cookieHandler(manager)
					.build();
			// changes请求路径
			String changesRequestUrl = girretUrlField.getText() + "changes/?O=%s&S=%s&n=%s&q=%s";
			// comments请求路径
			String commentsRequestUrl = girretUrlField.getText() + "changes/{}~{}/comments";
			boolean changesEnd = false;
			int paramS = 0;
			String paramQ = defaultParamQ;
			if (!ownerEmailField.getText().startsWith(userNameField.getText())) {
				paramQ = "owner:" + ownerEmailField.getText();
			}
			String resultFileName = file.getName();
			String resultPath = file.getParent();
			outPutChooser.setInitialDirectory(file.getParentFile());
			outPutChooser.setInitialFileName(resultFileName);
			int paramN = Integer.parseInt(limitField.getText());
			List<Map<String, String>> changesList = new ArrayList<>();
			List<Map<String, String>> commentsList = new ArrayList<>();
			// 开始获取结果
			for (; ; ) {
				String url = String.format(changesRequestUrl, URLEncoder.encode(paramO, StandardCharsets.UTF_8), paramS,
						paramN, URLEncoder.encode(paramQ, StandardCharsets.UTF_8));
				HttpRequest request = HttpRequest.newBuilder(URI.create(url)).GET().headers("Content-Type",
								"application/json", "User-Agent",
								"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36 Edg/105.0.1343.50")
						.build();
				HttpResponse<String> response = null;
				try {
					response = client.send(request, HttpResponse.BodyHandlers.ofString());
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}
				if (response.statusCode() == 200) {
					String result = response.body();
					if (result.startsWith(")]}'")) {
						result = result.substring(4);
					}
					JSONArray array = JSONUtil.parseArray(result);
					handleChanges(array, changesEnd, paramN, changesList);
					if (changesEnd) {
						break;
					}
					paramS = paramS + paramN;
				} else {
					notificationBuilder.text("changes request call failed");
					notificationBuilder.showError();
					StaticLog.error("changes request call failed", response.body());
					break;
				}
			}
			try {
				handleComments(commentsRequestUrl, resultPath, resultFileName, client, changesList, commentsList);
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}

			notificationBuilder.text(I18nUtils.get("smc.tool.fileDiff.button.generate.success"));
			notificationBuilder.showInformation();
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

		Label userNameLabel = new Label("User Name: ");
		userNameField = TextFields.createClearableTextField();

		Label passwdLabel = new Label("Password: ");
		passwdField = TextFields.createClearablePasswordField();

		Label ownerEmailLabel = new Label("Owner Email: ");
		ownerEmailField = TextFields.createClearableTextField();

		Label limitLabel = new Label("Limit: ");
		limitField = new NumberTextField(BigDecimal.valueOf(50));

		Label ignoreGirretNumberLabel = new Label("Ignore Girret Number: ");
		ignoreGirretNumberField = TextFields.createClearableTextField();
		ignoreGirretNumberField.setPromptText(I18nUtils.get("smc.tool.textfield.promptText.list"));

		Label startDateLabel = new Label("Start Date: ");
		startDatePicker = new DatePicker();
		Button startDateClearButton = new Button(I18nUtils.get("smc.tool.button.clear"));
		startDateClearButton.setOnAction(arg0 -> {
			startDatePicker.setValue(null);
		});
		startDatePicker.setEditable(false);

		Label reserveJsonLabel = new Label("Reserve Json: ");
		reserveJsonCheck = new CheckBox();

		Label girretUrlLabel = new Label("Girret Url: ");
		girretUrlField = new TextField();

		// 初始化赋值
		limitField.setNumber(BigDecimal.valueOf(50));
		reserveJsonCheck.setSelected(true);
		girretUrlField.setText("http://172.29.44.217/");

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
				{generateButton}:
				{generateDesc}
				{Required} {checkDirLabel}, {checkFileTypeLabel}, {ignoreFileLabel}

				{Note}
				{checkFileTypeLabel} {emptyDesc} {promptTextList}
				{ignoreFileLabel} {emptyDesc} {promptTextList}
				""";
		Map<String, String> map = new HashMap<>();
		map.put("generateButton", I18nUtils.get("smc.tool.button.generate"));
		map.put("generateDesc", I18nUtils.get("smc.tool.codeStyleLength120.control.textarea1"));
		map.put("Required", I18nUtils.get("smc.tool.control.required"));
		map.put("checkDirLabel", I18nUtils.get("smc.tool.codeStyleLength120.label.checkDir"));
		map.put("checkFileTypeLabel", I18nUtils.get("smc.tool.codeStyleLength120.label.checkFileType"));
		map.put("ignoreFileLabel", I18nUtils.get("smc.tool.codeStyleLength120.label.ignoreFile"));
		map.put("Note", I18nUtils.get("smc.tool.control.note"));
		map.put("emptyDesc", I18nUtils.get("smc.tool.textfield.empty.desc"));
		map.put("promptTextList", I18nUtils.get("smc.tool.textfield.promptText.list"));
		return FxTextInput.textArea(StrUtil.format(content, map));
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public String getSampleId() {
		return "GirretReview";
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
		generate.setGraphic(LayoutHelper.iconView(getClass().getResource("/com/tlcsdm/smc/static/icon/generate.png")));
		notificationBuilder.owner(FxApp.primaryStage);
	}

	/**
	 * girret changes 数据处理
	 */
	private void handleChanges(JSONArray array, boolean changesEnd, int paramN, List<Map<String, String>> changesList) {
		for (int i = 0; i < array.size(); i++) {
			if (changesFilter(array.get(i), array, i)) {
				// todo startDatePicker.getValue() submit 判断后 changesEnd
				Map<String, String> map = new HashMap<>();
				map.put("girretNum", String.valueOf(array.getByPath("[" + i + "]._number")));
				map.put("project", String.valueOf(array.getByPath("[" + i + "].project")));
				map.put("changeId", String.valueOf(array.getByPath("[" + i + "].change_id")));
				map.put("subject", String.valueOf(array.getByPath("[" + i + "].subject")));
				map.put("created", String.valueOf(array.getByPath("[" + i + "].created")).replace(".000000000", ""));
				map.put("submitted",
						String.valueOf(array.getByPath("[" + i + "].submitted")).replace(".000000000", ""));
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
		if (!Objects.equals(ownerEmailField.getText(), array.getByPath("[" + i + "].owner.email"))) {
			return false;
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
	private void handleComments(String commentsRequestUrl, String resultPath, String resultFileName, HttpClient client,
								List<Map<String, String>> changesList, List<Map<String, String>> commentsList)
			throws IOException, InterruptedException {
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
						String commentAutherUserName = String.valueOf(array.getByPath("[" + j + "].author.username"));
						Map<String, String> comment = new HashMap<>(changesList.get(i));
						if ("Done".equals(commentMessage)
								|| comment.get("ownerUserName").equals(commentAutherUserName)) {
							continue;
						}
						comment.put("commentFileName", vo.getKey());
						comment.put("commentAutherUserName", commentAutherUserName);
						comment.put("commentAutherName", String.valueOf(array.getByPath("[" + j + "].author.name")));
						comment.put("commentMessage", commentMessage);
						commentsList.add(comment);
					}
				}
			} else {
				notificationBuilder.text("comments request call failed");
				notificationBuilder.showError();
				StaticLog.error("comments request call failed", response.body());
				break;
			}
			ThreadUtil.safeSleep(200);
		}
		handleResult(resultPath, resultFileName, changesList, commentsList);
	}

	/**
	 * 数据结果处理
	 */
	private void handleResult(String resultPath, String resultFileName, List<Map<String, String>> changesList,
							  List<Map<String, String>> commentsList) {
		if (commentsList.size() == 0) {
			notificationBuilder.text("No need comments");
			notificationBuilder.showInformation();
			return;
		}
		ExcelWriter writer = ExcelUtil.getWriter(FileUtil.file(resultPath, resultFileName));
		setExcelStyle(writer);
		writer.setOnlyAlias(true);
		writer.addHeaderAlias("girretNum", "Girret Number");
		writer.addHeaderAlias("changeId", "changeId");
		writer.addHeaderAlias("subject", "提交信息");
		writer.addHeaderAlias("commentAutherName", "指摘人");
		writer.addHeaderAlias("commentMessage", "指摘信息");
		writer.addHeaderAlias("submitted", "合并时间");
		writer.addHeaderAlias("created", "开始时间");
		writer.addHeaderAlias("insertions", "新增行数");
		writer.addHeaderAlias("deletions", "删除行数");
		writer.addHeaderAlias("commentFileName", "指摘文件名");
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
