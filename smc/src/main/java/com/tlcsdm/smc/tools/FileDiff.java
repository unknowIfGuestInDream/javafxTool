package com.tlcsdm.smc.tools;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionUtils;
import org.controlsfx.control.action.ActionUtils.ActionTextBehavior;

import com.tlcsdm.smc.SmcSample;
import com.tlcsdm.smc.util.DiffHandleUtils;
import com.tlcsdm.smc.util.I18nUtils;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * 检测指定路径下文件内容长度是否超过120
 * 
 * @author unknowIfGuestInDream
 */
public class FileDiff extends SmcSample {

	private TextField originalField;
	private TextField compareField;
	private TextField outputField;
	private WebView webView;

	private final Action generate = new Action(I18nUtils.get("smc.menubar.file.exit"), actionEvent -> {
		// 对比 两个文件，获得不同点
		List<String> diffString = DiffHandleUtils.diffString(originalField.getText(), compareField.getText());
		String template = DiffHandleUtils.getDiffHtml(diffString);
		webView.getEngine().loadContent(template);
		// todo 提示
	});

	private final Action download = new Action(I18nUtils.get("smc.menubar.file.exit"), actionEvent -> {
		List<String> diffString = DiffHandleUtils.diffString(originalField.getText(), compareField.getText());
		// 生成一个diff.html文件，打开便可看到两个文件的对比
		DiffHandleUtils.generateDiffHtml(diffString, outputField.getText() + "\\diff.html");
		// todo 提示
	});

	private final Collection<? extends Action> actions = List.of(generate, download);

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public String getSampleName() {
		return I18nUtils.get("smc.sampleName.fileDiff");
	}

	@Override
	public Node getPanel(Stage stage) {
		initActions();
		GridPane grid = new GridPane();
		grid.setVgap(12);
		grid.setHgap(12);
		grid.setPadding(new Insets(24));

		ToolBar toolBar = ActionUtils.createToolBar(actions, ActionTextBehavior.SHOW);
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("text files", "*.txt", "*.c", "*.h",
				"*.java", "*.html");
		// original
		Label originalLabel = new Label("原文件: ");
		originalField = new TextField();
		originalField.setMaxWidth(Double.MAX_VALUE);
		FileChooser originalFileChooser = new FileChooser();
		originalFileChooser.getExtensionFilters().add(extFilter);
		originalFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("all", "*"));
		Button originalButton = new Button("选择");
		originalField.setEditable(false);
		originalButton.setOnAction(arg0 -> {
			File file = originalFileChooser.showOpenDialog(stage);
			if (file != null) {
				originalField.setText(file.getPath());
				originalFileChooser.setInitialDirectory(file.getParentFile());
			}
		});

		// compare
		Label compareLabel = new Label("比对文件: ");
		compareField = new TextField();
		compareField.setMaxWidth(Double.MAX_VALUE);
		FileChooser compareFileChooser = new FileChooser();
		compareFileChooser.getExtensionFilters().add(extFilter);
		compareFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("all", "*"));
		Button compareButton = new Button("选择");
		compareField.setEditable(false);
		compareButton.setOnAction(arg0 -> {
			File file = compareFileChooser.showOpenDialog(stage);
			if (file != null) {
				compareField.setText(file.getPath());
				compareFileChooser.setInitialDirectory(file.getParentFile());
			}
		});

		// output
		Label outputLabel = new Label("输出路径: ");
		outputField = new TextField();
		outputField.setMaxWidth(Double.MAX_VALUE);
		DirectoryChooser outputChooser = new DirectoryChooser();
		Button outputButton = new Button("选择");
		outputField.setEditable(false);
		outputButton.setOnAction(arg0 -> {
			File file = outputChooser.showDialog(stage);
			if (file != null) {
				outputField.setText(file.getPath());
				outputChooser.setInitialDirectory(file);
			}
		});

		// webView
		webView = new WebView();
		webView.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

		grid.add(toolBar, 0, 0, 3, 1);
		grid.add(originalLabel, 0, 1);
		grid.add(originalButton, 1, 1);
		grid.add(originalField, 2, 1);
		grid.add(compareLabel, 0, 2);
		grid.add(compareButton, 1, 2);
		grid.add(compareField, 2, 2);
		grid.add(outputLabel, 0, 3);
		grid.add(outputButton, 1, 3);
		grid.add(outputField, 2, 3);
		grid.add(webView, 0, 4, 3, 1);

		return grid;
	}

	@Override
	public String getOrderKey() {
		return "FillDiff";
	}

	@Override
	public String getSampleDescription() {
		return I18nUtils.get("smc.sampleName.fileDiff.description");
	}

	private void initActions() {
		generate.setGraphic(getImageView("/com/tlcsdm/smc/static/menubar/exit.png"));
		download.setGraphic(getImageView("/com/tlcsdm/smc/static/menubar/exit.png"));
	}

	private ImageView getImageView(String url) {
		return new ImageView(new Image(Objects.requireNonNull(getClass().getResource(url)).toExternalForm()));
	}

}
