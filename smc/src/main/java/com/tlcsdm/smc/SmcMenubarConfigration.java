package com.tlcsdm.smc;

import static org.controlsfx.control.action.ActionUtils.ACTION_SEPARATOR;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;

import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionGroup;
import org.controlsfx.control.action.ActionUtils;

import com.tlcsdm.frame.MenubarConfigration;
import com.tlcsdm.smc.util.I18nUtils;
import com.tlcsdm.smc.util.SmcConstant;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuBar;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SmcMenubarConfigration implements MenubarConfigration {

	private Stage stage;

	private final Action contactSupport = new Action(I18nUtils.get("smc.menubar.help.contactSupport"), actionEvent -> {
		Desktop d = Desktop.getDesktop();
		try {
			URI address = new URI(SmcConstant.GITHUB_PROJECT_SUPPORT_URL);
			d.browse(address);
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
	});

	private final Action submitFeedback = new Action(I18nUtils.get("smc.menubar.help.submitFeedback"), actionEvent -> {
		Desktop d = Desktop.getDesktop();
		try {
			URI address = new URI(SmcConstant.GITHUB_PROJECT_FEEDBACK_URL);
			d.browse(address);
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
	});

	private final Action about = new Action(I18nUtils.get("smc.menubar.help.about"), actionEvent -> {
		Alert alert = new Alert(Alert.AlertType.NONE);
		alert.setResizable(false);
		alert.setTitle("About javafxTool");
		alert.setHeaderText("head");
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.initOwner(stage);
		ButtonType linkButton = new ButtonType("Open source project", ButtonBar.ButtonData.LEFT);
		ButtonType closeButton = new ButtonType("Close", ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().addAll(linkButton, closeButton);
		alert.setContentText("Each cell of this column (except for the "
				+ "separator in the middle) has a particular css " + "class for changing its color.\n\n\n\n\n\n");
		alert.show();
		alert.resultProperty().addListener(o -> {
			if (linkButton.equals(alert.getResult())) {
				// TODO
			}
		});
	});

	private final Collection<? extends Action> actions = List.of(new ActionGroup(I18nUtils.get("smc.menubar.setting")),
			new ActionGroup(I18nUtils.get("smc.menubar.help"), contactSupport, submitFeedback, ACTION_SEPARATOR,
					about));

	/**
	 * 初始化action
	 */
	private void initActions() {
		// Do nothing
	}

	@Override
	public MenuBar setMenuBar(MenuBar menuBar, Stage stage) {
		this.stage = stage;
		initActions();
		ActionUtils.updateMenuBar(menuBar, actions);
		return menuBar;
	}

}
