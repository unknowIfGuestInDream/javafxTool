package com.tlcsdm.smc;

import com.tlcsdm.core.javafx.util.JavaFxSystemUtil;
import com.tlcsdm.frame.FXSampler;
import com.tlcsdm.frame.MenubarConfigration;
import com.tlcsdm.smc.util.I18nUtils;
import com.tlcsdm.smc.util.SmcConstant;
import javafx.application.Platform;
import javafx.scene.Node;
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
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static org.controlsfx.control.action.ActionUtils.ACTION_SEPARATOR;

public class SmcMenubarConfigration implements MenubarConfigration {

	private final Stage stage = FXSampler.getStage();

	private final Action restart = new Action(I18nUtils.get("smc.menubar.file.restart"),
			actionEvent -> Platform.runLater(() -> {
				stage.close();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				new FXSampler().start(new Stage());
			}));

	private final Action exit = new Action(I18nUtils.get("smc.menubar.file.exit"),
			actionEvent -> FXSampler.doExit());

	private final Action contactSupport = new Action(I18nUtils.get("smc.menubar.help.contactSupport"),
			actionEvent -> openWeb(SmcConstant.GITHUB_PROJECT_SUPPORT_URL));

	private final Action submitFeedback = new Action(I18nUtils.get("smc.menubar.help.submitFeedback"),
			actionEvent -> openWeb(SmcConstant.GITHUB_PROJECT_FEEDBACK_URL));

	private final Action openLogDir = new Action(I18nUtils.get("smc.menubar.help.openLogDir"),
			actionEvent -> JavaFxSystemUtil.openDirectory("logs/"));

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
				""".formatted(I18nUtils.get("smc.menubar.help.about.contentText.version"), "1.0.0",
				I18nUtils.get("smc.menubar.help.about.contentText.date"), "2022-11-17",
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

	private final Collection<? extends Action> actions = List.of(
			new ActionGroup(I18nUtils.get("smc.menubar.file"), getImageView("/com/tlcsdm/smc/static/menubar/file.png"),
					restart, exit),
			new ActionGroup(I18nUtils.get("smc.menubar.setting"),
					getImageView("/com/tlcsdm/smc/static/menubar/setting.png"),
					new ActionGroup(I18nUtils.get("smc.menubar.setting.language"),
							getImageView("/com/tlcsdm/smc/static/menubar/language.png"), new CheckLangAction("中文简体"),
							new CheckLangAction("English"), new CheckLangAction("日本語"))),
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
		public CheckLangAction(String name, Node image) {
			super(name);
			setGraphic(image);
			init();
		}

		public CheckLangAction(String name) {
			super(name);
			init();
		}

		private void init() {
			setEventHandler(ae -> {
				// getText()
//				if ("简体中文".equals(languageType)) {
//					Config.set(Config.Keys.Locale, Locale.SIMPLIFIED_CHINESE);
//				} else if ("English".equals(languageType)) {
//					Config.set(Config.Keys.Locale, Locale.US);
//				}
//				AlertUtil.showInfoAlert(indexController.getBundle().getString("SetLanguageText"));
				//FxAlerts.info
//				XJavaFxToolApplication.getStage().close();
//				Platform.runLater(() -> {
//					try {
//						XJavaFxSystemUtil.initSystemLocal();    // 初始化本地语言
//						new XJavaFxToolApplication().start(new Stage());
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				});
			});
			this.selectedProperty().addListener((arg0, oldValue, newValue) -> {
				// TODO 三个语言按钮单独设立，在这里进行实现只能选择一个，外加一个全局项目属性
			});
		}
	}

}
