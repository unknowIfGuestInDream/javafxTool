package com.tlcsdm.smc.tools;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.controlsfx.control.Notifications;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionUtils;
import org.controlsfx.control.action.ActionUtils.ActionTextBehavior;

import com.tlcsdm.core.javafx.FxApp;
import com.tlcsdm.core.javafx.helper.LayoutHelper;
import com.tlcsdm.smc.SmcSample;
import com.tlcsdm.smc.util.I18nUtils;

import cn.hutool.core.util.StrUtil;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * 检测指定路径下文件内容长度是否超过120
 * 
 * @author unknowIfGuestInDream
 */
public class CodeStyleLength120 extends SmcSample {

	private TextField checkDirField;
	private TextField checkFileTypeField;
	private final Notifications notificationBuilder = Notifications.create().hideAfter(Duration.seconds(5))
			.position(Pos.TOP_CENTER);
	FileChooser outPutChooser = new FileChooser();
	private final String resultFileName = "CodeStyleLength120.xlsx";

	private final Action generate = new Action(I18nUtils.get("smc.tool.fileDiff.button.generate"), actionEvent -> {
		File file = outPutChooser.showSaveDialog(FxApp.primaryStage);
		if (file != null) {
			outPutChooser.setInitialDirectory(file.getParentFile());
			outPutChooser.setInitialFileName(file.getName());
			if (!StrUtil.endWith(file.getName(), ".xlsx")) {
				notificationBuilder.text("请保存成xlsx文件");
				notificationBuilder.showError();
				return;
			}
		}

		notificationBuilder.text(I18nUtils.get("smc.tool.fileDiff.button.generate.success"));
		notificationBuilder.showInformation();
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

		Label checkDirLabel = new Label("校验文件夹路径" + ": ");
		checkDirField = new TextField();
		checkDirField.setMaxWidth(Double.MAX_VALUE);
		DirectoryChooser checkDirChooser = new DirectoryChooser();
		Button checkDirButton = new Button(I18nUtils.get("smc.tool.fileDiff.button.choose"));
		checkDirField.setEditable(false);
		checkDirButton.setOnAction(arg0 -> {
			File file = checkDirChooser.showDialog(stage);
			if (file != null) {
				checkDirField.setText(file.getPath());
				checkDirChooser.setInitialDirectory(file);
			}
		});

		Label checkFileTypeLabel = new Label("校验文件类型" + ": ");
		checkFileTypeField = new TextField();
		checkFileTypeField.setPrefWidth(Double.MAX_VALUE);
		checkFileTypeField.setText("c,h");

		grid.add(toolBar, 0, 0, 3, 1);
		grid.add(checkDirLabel, 0, 1);
		grid.add(checkDirButton, 1, 1);
		grid.add(checkDirField, 2, 1);

		grid.add(checkFileTypeLabel, 0, 2);
		grid.add(checkFileTypeField, 1, 2, 2, 1);

		return grid;
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public String getSampleName() {
		return I18nUtils.get("smc.sampleName.codeStyleLength120");
	}

	@Override
	public String getSampleVersion() {
		return "1.0.0";
	}

	@Override
	public String getOrderKey() {
		return "CodeStyleLength120";
	}

	@Override
	public String getSampleDescription() {
		return I18nUtils.get("smc.sampleName.codeStyleLength120.description");
	}

	// 初始化组件
	private void initComponment() {
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("excel file", "*.xlsx");
		outPutChooser.getExtensionFilters().add(extFilter);
		outPutChooser.setInitialFileName(resultFileName);

		generate.setGraphic(LayoutHelper.iconView(getClass().getResource("/com/tlcsdm/smc/static/icon/generate.png")));
		notificationBuilder.owner(FxApp.primaryStage);
	}

}
