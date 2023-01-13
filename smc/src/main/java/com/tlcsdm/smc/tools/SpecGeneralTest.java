package com.tlcsdm.smc.tools;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.controlsfx.control.Notifications;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionUtils;

import com.tlcsdm.core.exception.UnExpectedResultException;
import com.tlcsdm.core.javafx.control.FxButton;
import com.tlcsdm.core.javafx.control.FxTextInput;
import com.tlcsdm.core.javafx.controlsfx.FxAction;
import com.tlcsdm.core.javafx.dialog.ExceptionDialog;
import com.tlcsdm.core.javafx.dialog.FxNotifications;
import com.tlcsdm.smc.SmcSample;
import com.tlcsdm.smc.util.I18nUtils;

import cn.hutool.core.util.StrUtil;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * 为specGeneral测试文档的测试生成差异文件, 提高测试效率
 *
 * @author: unknowIfGuestInDream
 * @date: 2022/12/8 23:12
 */
public class SpecGeneralTest extends SmcSample {

	private TextField excelField;
	private FileChooser excelFileChooser;
	private TextField generalField;
	private DirectoryChooser generalChooser;
	private TextField ignoreSheetField;
	private TextField markSheetField;
	private TextField startCellField;
	private TextField generalFileCellField;
	private TextField endCellColumnField;
	private final Notifications notificationBuilder = FxNotifications.defaultNotify();

	private final Action generate = FxAction.generate(actionEvent -> {
		ExceptionDialog exceptionDialog = new ExceptionDialog(new UnExpectedResultException("request called failed."));
		exceptionDialog.show();
	});

	private final Collection<? extends Action> actions = List.of(generate);

	@Override
	public Node getPanel(Stage stage) {
		GridPane grid = new GridPane();
		grid.setVgap(12);
		grid.setHgap(12);
		grid.setPadding(new Insets(24));

		ToolBar toolBar = ActionUtils.createToolBar(actions, ActionUtils.ActionTextBehavior.SHOW);
		toolBar.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		toolBar.setPrefWidth(Double.MAX_VALUE);
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("excel file", "*.xlsx");

		Label excelLabel = new Label(I18nUtils.get("smc.tool.fileDiff.label.original") + ": ");
		excelField = new TextField();
		excelField.setMaxWidth(Double.MAX_VALUE);
		excelFileChooser = new FileChooser();
		excelFileChooser.getExtensionFilters().add(extFilter);

		Button excelButton = FxButton.choose();
		excelField.setEditable(false);
		excelButton.setOnAction(arg0 -> {
			File file = excelFileChooser.showOpenDialog(stage);
			if (file != null) {
				excelField.setText(file.getPath());
				excelFileChooser.setInitialDirectory(file.getParentFile());
			}
		});

		Label generalLabel = new Label(I18nUtils.get("smc.tool.fileDiff.label.output") + ": ");
		generalField = new TextField();
		generalField.setMaxWidth(Double.MAX_VALUE);
		generalChooser = new DirectoryChooser();
		Button generalButton = FxButton.choose();
		generalField.setEditable(false);
		generalButton.setOnAction(arg0 -> {
			File file = generalChooser.showDialog(stage);
			if (file != null) {
				generalField.setText(file.getPath());
				generalChooser.setInitialDirectory(file);
			}
		});

		Label ignoreSheetLabel = new Label(I18nUtils.get("smc.tool.codeStyleLength120.label.ignoreFile") + ": ");
		ignoreSheetField = new TextField();
		ignoreSheetField.setPrefWidth(Double.MAX_VALUE);
		ignoreSheetField.setPromptText(I18nUtils.get("smc.tool.textfield.promptText.list"));

		Label markSheetLabel = new Label(I18nUtils.get("smc.tool.codeStyleLength120.label.ignoreFile") + ": ");
		markSheetField = new TextField();
		markSheetField.setPrefWidth(Double.MAX_VALUE);
		markSheetField.setPromptText(I18nUtils.get("smc.tool.textfield.promptText.list"));

		Label startCellLabel = new Label("startCell: ");
		startCellField = new TextField();

		Label endCellColumnLabel = new Label("endCellColumn: ");
		endCellColumnField = new TextField();

		Label generalFileCellLabel = new Label("generalFileCell: ");
		generalFileCellField = new TextField();

		ignoreSheetField.setText("Overview, Summary, Sample-CT");
		startCellField.setText("C19");
		endCellColumnField.setText("F");
		generalFileCellField.setText("C15");

		grid.add(toolBar, 0, 0, 3, 1);
		grid.add(excelLabel, 0, 1);
		grid.add(excelButton, 1, 1);
		grid.add(excelField, 2, 1);
		grid.add(generalLabel, 0, 2);
		grid.add(generalButton, 1, 2);
		grid.add(generalField, 2, 2);
		grid.add(ignoreSheetLabel, 0, 3);
		grid.add(ignoreSheetField, 1, 3, 2, 1);
		grid.add(markSheetLabel, 0, 4);
		grid.add(markSheetField, 1, 4, 2, 1);
		grid.add(startCellLabel, 0, 5);
		grid.add(startCellField, 1, 5, 2, 1);
		grid.add(endCellColumnLabel, 0, 6);
		grid.add(endCellColumnField, 1, 6, 2, 1);
		grid.add(generalFileCellLabel, 0, 7);
		grid.add(generalFileCellField, 1, 7, 2, 1);
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

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public String getSampleId() {
		return "specGeneralTest";
	}

	@Override
	public String getSampleName() {
		return I18nUtils.get("smc.sampleName.specGeneralTest");
	}

	@Override
	public String getSampleVersion() {
		return "1.0.0";
	}

	@Override
	public String getOrderKey() {
		return "SpecGeneralTest";
	}

	@Override
	public String getSampleDescription() {
		return I18nUtils.get("smc.sampleName.specGeneralTest.description");
	}

}
