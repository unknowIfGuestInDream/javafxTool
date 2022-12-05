package com.tlcsdm.smc;

import static org.controlsfx.control.action.ActionUtils.ACTION_SEPARATOR;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionCheck;
import org.controlsfx.control.action.ActionGroup;
import org.controlsfx.control.action.ActionUtils;

import com.tlcsdm.core.javafx.dialog.FxAlerts;
import com.tlcsdm.core.javafx.util.Config;
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

	private final Action contactSupport = new Action(I18nUtils.get("smc.menubar.help.contactSupport"),
			actionEvent -> openWeb(SmcConstant.GITHUB_PROJECT_SUPPORT_URL));

	private final Action submitFeedback = new Action(I18nUtils.get("smc.menubar.help.submitFeedback"),
			actionEvent -> openWeb(SmcConstant.GITHUB_PROJECT_FEEDBACK_URL));

	private final Action openLogDir = new Action(I18nUtils.get("smc.menubar.help.openLogDir"),
			actionEvent -> JavaFxSystemUtil.openDirectory("logs/smc/"));

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
		String context = """
				%s: %s
				%s: %s
				%s: %s
				%s: %s

				%s: %s
				%s: %s

				%s: [%s]
				%s
				""".formatted(I18nUtils.get("smc.menubar.help.about.contentText.version"), Config.JAVAFX_TOOL_VERSION,
				I18nUtils.get("smc.menubar.help.about.contentText.date"), Config.JAVAFX_TOOL_PUBLISHDATE,
				I18nUtils.get("smc.menubar.help.about.contentText.licenseName"), SmcConstant.PROJECT_LICENSE_NAME,
				I18nUtils.get("smc.menubar.help.about.contentText.licenseUrl"), SmcConstant.PROJECT_LICENSE_URL,
				I18nUtils.get("smc.menubar.help.about.contentText.author"), SmcConstant.PROJECT_AUTHOR,
				I18nUtils.get("smc.menubar.help.about.contentText.projectUrl"), SmcConstant.GITHUB_PROJECT_URL,
				I18nUtils.get("smc.menubar.help.about.contentText.technicalSupport"),
				I18nUtils.get("smc.menubar.help.about.contentText.openSourceSoftware"), SmcConstant.PROJECT_COPYRIGHT);
		alert.setContentText(context);
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
			new ActionGroup(I18nUtils.get("smc.menubar.file"), getImageView("/com/tlcsdm/smc/static/menubar/file.png"),
					restart, exit),
			new ActionGroup(I18nUtils.get("smc.menubar.setting"),
					getImageView("/com/tlcsdm/smc/static/menubar/setting.png"),
					new ActionGroup(I18nUtils.get("smc.menubar.setting.language"),
							getImageView("/com/tlcsdm/smc/static/menubar/language.png"), chinese, english, japanese)),
			new ActionGroup(I18nUtils.get("smc.menubar.help"), getImageView("/com/tlcsdm/smc/static/menubar/help.png"),
					openLogDir, ACTION_SEPARATOR, contactSupport, submitFeedback, ACTION_SEPARATOR, release, about));

	/**
	 * 初始化action
	 */
	private void initActions() {
		restart.setGraphic(getImageView("/com/tlcsdm/smc/static/menubar/restart.png"));
		exit.setGraphic(getImageView("/com/tlcsdm/smc/static/menubar/exit.png"));
		contactSupport.setGraphic(getImageView("/com/tlcsdm/smc/static/menubar/support.png"));
		submitFeedback.setGraphic(getImageView("/com/tlcsdm/smc/static/menubar/feedback.png"));
		about.setGraphic(getImageView("/com/tlcsdm/smc/static/menubar/about.png"));
		openLogDir.setGraphic(getImageView("/com/tlcsdm/smc/static/menubar/folder.png"));
		release.setGraphic(getImageView("/com/tlcsdm/smc/static/menubar/release.png"));
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

	private ImageView getImageView(String url) {
		return new ImageView(new Image(Objects.requireNonNull(getClass().getResource(url)).toExternalForm()));
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
