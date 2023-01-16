package com.tlcsdm.smc;

import static org.controlsfx.control.action.ActionUtils.ACTION_SEPARATOR;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionCheck;
import org.controlsfx.control.action.ActionUtils;

import com.tlcsdm.core.javafx.FxApp;
import com.tlcsdm.core.javafx.controlsfx.FxAction;
import com.tlcsdm.core.javafx.controlsfx.FxActionGroup;
import com.tlcsdm.core.javafx.dialog.FxAlerts;
import com.tlcsdm.core.javafx.helper.LayoutHelper;
import com.tlcsdm.core.javafx.util.Config;
import com.tlcsdm.core.javafx.util.ConfigureUtil;
import com.tlcsdm.core.javafx.util.JavaFxSystemUtil;
import com.tlcsdm.core.util.CoreUtil;
import com.tlcsdm.frame.FXSampler;
import com.tlcsdm.frame.MenubarConfigration;
import com.tlcsdm.smc.util.I18nUtils;
import com.tlcsdm.smc.util.SmcConstant;

import cn.hutool.core.util.StrUtil;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuBar;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SmcMenubarConfigration implements MenubarConfigration {

	private final Stage stage = FXSampler.getStage();

	private final Action restart = FxAction.restart(actionEvent -> Platform.runLater(() -> {
		stage.close();
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		new FXSampler().start(new Stage());
	}));

	private final Action exit = FxAction.exit(actionEvent -> FXSampler.doExit());

	private final Action systemSetting = FxAction.systemSetting();

	private final Action contactSupport = FxAction
			.contactSupport(actionEvent -> CoreUtil.openWeb(SmcConstant.GITHUB_PROJECT_SUPPORT_URL));

	private final Action submitFeedback = FxAction
			.submitFeedback(actionEvent -> CoreUtil.openWeb(SmcConstant.GITHUB_PROJECT_FEEDBACK_URL));

	private final Action openLogDir = FxAction.openLogDir(actionEvent -> JavaFxSystemUtil.openDirectory("logs/smc/"));

	private final Action openSysConfig = FxAction.openSysConfig(
			actionEvent -> JavaFxSystemUtil.openDirectory(ConfigureUtil.getConfigurePath(Config.CONFIG_FILE_NAME)));

	private final Action openUserData = FxAction.openUserData(
			actionEvent -> JavaFxSystemUtil.openDirectory(ConfigureUtil.getConfigurePath(Config.USERDATA_FILE_NAME)));

	private final Action about = FxAction.about(actionEvent -> {
		Alert alert = new Alert(Alert.AlertType.NONE);
		alert.getDialogPane().setStyle("-fx-min-width: 480; -fx-min-height: 360;");
		alert.setResizable(false);
		alert.setTitle(I18nUtils.get("smc.menubar.help.about.title"));
		alert.setHeaderText(I18nUtils.get("smc.menubar.help.about.headerText"));
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.initOwner(stage);
		ImageView imageView = LayoutHelper.iconView(FxApp.appIcon, 80);
		alert.setGraphic(imageView);
		ButtonType closeButton = new ButtonType(I18nUtils.get("smc.menubar.help.about.button.close"),
				ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().addAll(closeButton);
		Map<String, String> map = new HashMap<>(32);
		map.put("versionLabel", I18nUtils.get("smc.menubar.help.about.contentText.version"));
		map.put("version", Config.JAVAFX_TOOL_VERSION);
		map.put("dateLabel", I18nUtils.get("smc.menubar.help.about.contentText.date"));
		map.put("date", Config.JAVAFX_TOOL_PUBLISHDATE);
		map.put("licenseNameLabel", I18nUtils.get("smc.menubar.help.about.contentText.licenseName"));
		map.put("licenseName", SmcConstant.PROJECT_LICENSE_NAME);
		map.put("licenseUrlLabel", I18nUtils.get("smc.menubar.help.about.contentText.licenseUrl"));
		map.put("licenseUrl", SmcConstant.PROJECT_LICENSE_URL);
		map.put("authorLabel", I18nUtils.get("smc.menubar.help.about.contentText.author"));
		map.put("author", SmcConstant.PROJECT_AUTHOR);
		map.put("projectUrlLabel", I18nUtils.get("smc.menubar.help.about.contentText.projectUrl"));
		map.put("projectUrl", SmcConstant.GITHUB_PROJECT_URL);
		map.put("technicalSupport", I18nUtils.get("smc.menubar.help.about.contentText.technicalSupport"));
		map.put("openSourceSoftware", I18nUtils.get("smc.menubar.help.about.contentText.openSourceSoftware"));
		map.put("copyright", SmcConstant.PROJECT_COPYRIGHT);
		String context = """
				{versionLabel}: {version}
				{dateLabel}: {date}
				{licenseNameLabel}: {licenseName}
				{licenseUrlLabel}: {licenseUrl}

				{authorLabel}: {author}
				{projectUrlLabel}: {projectUrl}

				{technicalSupport}: [{openSourceSoftware}]
				{copyright}
				""";
		alert.setContentText(StrUtil.format(context, map));
		alert.show();
		alert.resultProperty().addListener(o -> {
			if (closeButton.equals(alert.getResult())) {
				alert.close();
			}
		});
	});

	private final Action release = FxAction.release(actionEvent -> CoreUtil.openWeb(SmcConstant.PROJECT_RELEASE_URL));

	CheckLangAction chinese = new CheckLangAction(SmcConstant.LANGUAGE_CHINESE);
	CheckLangAction english = new CheckLangAction(SmcConstant.LANGUAGE_ENGLISH);
	CheckLangAction japanese = new CheckLangAction(SmcConstant.LANGUAGE_JAPANESE);

	private final Collection<? extends Action> actions = List.of(FxActionGroup.file(restart, exit),
			FxActionGroup.setting(systemSetting, FxActionGroup.language(chinese, english, japanese)),
			FxActionGroup.help(openSysConfig, openLogDir, openUserData, ACTION_SEPARATOR, contactSupport,
					submitFeedback, ACTION_SEPARATOR, release, about));

	/**
	 * 初始化action
	 */
	private void initActions() {
		// 语言设置
		if (Config.defaultLocale.equals(Locale.ENGLISH)) {
			english.setSelected(true);
		} else if (Config.defaultLocale.equals(Locale.SIMPLIFIED_CHINESE)) {
			chinese.setSelected(true);
		} else if (Config.defaultLocale.equals(Locale.JAPANESE)) {
			japanese.setSelected(true);
		}
		chinese.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if (oldValue) {
				return;
			}
			if (newValue) {
				english.setSelected(false);
				japanese.setSelected(false);
			}
		});
		english.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if (oldValue) {
				return;
			}
			if (newValue) {
				japanese.setSelected(false);
				chinese.setSelected(false);
			}
		});
		japanese.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if (oldValue) {
				return;
			}
			if (newValue) {
				english.setSelected(false);
				chinese.setSelected(false);
			}
		});
	}

	@Override
	public MenuBar setMenuBar(MenuBar menuBar) {
		initActions();
		ActionUtils.updateMenuBar(menuBar, actions);
		return menuBar;
	}

	@ActionCheck
	private static class CheckLangAction extends Action {

		public CheckLangAction(String name) {
			super(name);
			init();
		}

		private void init() {
			setEventHandler(ae -> {
				String languageType = getText();
				if (SmcConstant.LANGUAGE_CHINESE.equals(languageType)) {
					if (Config.defaultLocale == Locale.SIMPLIFIED_CHINESE) {
						return;
					}
					Config.set(Config.Keys.Locale, Locale.SIMPLIFIED_CHINESE);
				} else if (SmcConstant.LANGUAGE_ENGLISH.equals(languageType)) {
					if (Config.defaultLocale == Locale.ENGLISH) {
						return;
					}
					Config.set(Config.Keys.Locale, Locale.ENGLISH);
				} else if (SmcConstant.LANGUAGE_JAPANESE.equals(languageType)) {
					if (Config.defaultLocale == Locale.JAPANESE) {
						return;
					}
					Config.set(Config.Keys.Locale, Locale.JAPANESE);
				}
				if (FxAlerts.confirmOkCancel(I18nUtils.get("smc.menubar.setting.language.dialog.title"),
						I18nUtils.get("smc.menubar.setting.language.dialog.message"))) {
					FXSampler.getStage().close();
					Platform.runLater(() -> {
						try {
							new FXSampler().start(new Stage());
						} catch (Exception e) {
							e.printStackTrace();
						}
					});
				}
			});
		}
	}

}
