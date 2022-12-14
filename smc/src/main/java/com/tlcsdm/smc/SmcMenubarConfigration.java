package com.tlcsdm.smc;

import cn.hutool.core.util.StrUtil;
import com.tlcsdm.core.javafx.FxApp;
import com.tlcsdm.core.javafx.dialog.FxAlerts;
import com.tlcsdm.core.javafx.dialog.SystemSettingDialog;
import com.tlcsdm.core.javafx.util.Config;
import com.tlcsdm.core.javafx.util.ConfigureUtil;
import com.tlcsdm.core.javafx.util.JavaFxSystemUtil;
import com.tlcsdm.frame.FXSampler;
import com.tlcsdm.frame.MenubarConfigration;
import com.tlcsdm.smc.util.I18nUtils;
import com.tlcsdm.smc.util.SmcConstant;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionCheck;
import org.controlsfx.control.action.ActionGroup;
import org.controlsfx.control.action.ActionUtils;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.*;

import static org.controlsfx.control.action.ActionUtils.ACTION_SEPARATOR;

public class SmcMenubarConfigration implements MenubarConfigration {

	private final Stage stage = FXSampler.getStage();

	private final Action restart = new Action(I18nUtils.get("smc.menubar.file.restart"),
			actionEvent -> Platform.runLater(() -> {
				stage.close();
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				JavaFxSystemUtil.initSystemLocal();
				new FXSampler().start(new Stage());
			}));

	private final Action exit = new Action(I18nUtils.get("smc.menubar.file.exit"), actionEvent -> FXSampler.doExit());

	private final Action systemSetting = new Action(I18nUtils.get("smc.menubar.setting.systemSetting"), actionEvent -> {
		SystemSettingDialog.openSystemSettings(I18nUtils.get("smc.menubar.setting.systemSetting"));
	});

	private final Action contactSupport = new Action(I18nUtils.get("smc.menubar.help.contactSupport"),
			actionEvent -> openWeb(SmcConstant.GITHUB_PROJECT_SUPPORT_URL));

	private final Action submitFeedback = new Action(I18nUtils.get("smc.menubar.help.submitFeedback"),
			actionEvent -> openWeb(SmcConstant.GITHUB_PROJECT_FEEDBACK_URL));

	private final Action openLogDir = new Action(I18nUtils.get("smc.menubar.help.openLogDir"),
			actionEvent -> JavaFxSystemUtil.openDirectory("logs/smc/"));

	private final Action openSysConfig = new Action(I18nUtils.get("smc.menubar.help.openSysConfigDir"),
			actionEvent -> JavaFxSystemUtil.openDirectory(ConfigureUtil.getConfigurePath(Config.CONFIG_FILE_NAME)));

	private final Action openUserData = new Action(I18nUtils.get("smc.menubar.help.openUserData"),
			actionEvent -> JavaFxSystemUtil.openDirectory(ConfigureUtil.getConfigurePath(Config.USERDATA_FILE_NAME)));

	private final Action about = new Action(I18nUtils.get("smc.menubar.help.about"), actionEvent -> {
		Alert alert = new Alert(Alert.AlertType.NONE);
		alert.getDialogPane().setStyle("-fx-min-width: 480; -fx-min-height: 360;");
		alert.setResizable(false);
		alert.setTitle(I18nUtils.get("smc.menubar.help.about.title"));
		alert.setHeaderText(I18nUtils.get("smc.menubar.help.about.headerText"));
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.initOwner(stage);

		if (stage.getIcons().size() > 0) {
			ImageView imageView = new ImageView(stage.getIcons().get(0));
			imageView.setFitHeight(80);
			imageView.setFitWidth(80);
			alert.setGraphic(imageView);
		}

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

	private final Action release = new Action(I18nUtils.get("smc.menubar.help.release"),
			actionEvent -> openWeb(SmcConstant.PROJECT_RELEASE_URL));

	CheckLangAction chinese = new CheckLangAction(SmcConstant.LANGUAGE_CHINESE);
	CheckLangAction english = new CheckLangAction(SmcConstant.LANGUAGE_ENGLISH);
	CheckLangAction japanese = new CheckLangAction(SmcConstant.LANGUAGE_JAPANESE);

	private final Collection<? extends Action> actions = List.of(
			new ActionGroup(I18nUtils.get("smc.menubar.file"), getImageView("/com/tlcsdm/core/static/menubar/file.png"),
					restart, exit),
			new ActionGroup(I18nUtils.get("smc.menubar.setting"),
					getImageView("/com/tlcsdm/core/static/menubar/setting.png"), systemSetting,
					new ActionGroup(I18nUtils.get("smc.menubar.setting.language"),
							getImageView("/com/tlcsdm/core/static/menubar/language.png"), chinese, english, japanese)),
			new ActionGroup(I18nUtils.get("smc.menubar.help"), getImageView("/com/tlcsdm/core/static/menubar/help.png"),
					openSysConfig, openLogDir, openUserData, ACTION_SEPARATOR, contactSupport, submitFeedback,
					ACTION_SEPARATOR, release, about));

	/**
	 * ?????????action
	 */
	private void initActions() {
		restart.setGraphic(getImageView("/com/tlcsdm/core/static/menubar/restart.png"));
		exit.setGraphic(getImageView("/com/tlcsdm/core/static/menubar/exit.png"));
		systemSetting.setGraphic(getImageView("/com/tlcsdm/core/static/menubar/system.png"));
		contactSupport.setGraphic(getImageView("/com/tlcsdm/core/static/menubar/support.png"));
		submitFeedback.setGraphic(getImageView("/com/tlcsdm/core/static/menubar/feedback.png"));
		about.setGraphic(getImageView("/com/tlcsdm/core/static/menubar/about.png"));
		openLogDir.setGraphic(getImageView("/com/tlcsdm/core/static/menubar/folder.png"));
		openSysConfig.setGraphic(getImageView("/com/tlcsdm/core/static/menubar/sysConfig.png"));
		openUserData.setGraphic(getImageView("/com/tlcsdm/core/static/menubar/userData.png"));
		release.setGraphic(getImageView("/com/tlcsdm/core/static/menubar/release.png"));
		// ????????????
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

	private ImageView getImageView(String url) {
		return new ImageView(new Image(Objects.requireNonNull(FxApp.class.getResource(url)).toExternalForm()));
	}

	private void openWeb(String url) {
		Desktop d = Desktop.getDesktop();
		try {
			URI address = new URI(url);
			d.browse(address);
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
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
							JavaFxSystemUtil.initSystemLocal();
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
