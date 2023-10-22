/*
 * Copyright (c) 2023 unknowIfGuestInDream
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

package com.tlcsdm.jfxcommon.tools;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.tlcsdm.core.exception.UnExpectedResultException;
import com.tlcsdm.core.javafx.FxApp;
import com.tlcsdm.core.javafx.control.FxButton;
import com.tlcsdm.core.javafx.control.FxTextInput;
import com.tlcsdm.core.javafx.controlsfx.FxAction;
import com.tlcsdm.core.javafx.dialog.FxNotifications;
import com.tlcsdm.core.javafx.helper.LayoutHelper;
import com.tlcsdm.core.javafx.util.FileChooserUtil;
import com.tlcsdm.core.util.CoreConstant;
import com.tlcsdm.core.util.CoreUtil;
import com.tlcsdm.jfxcommon.CommonSample;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionUtils;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据格式转换.
 * 需要工厂模式设计进行，且结果不应该使用freemarker生成，结果的使能条件应交给子类实现，
 * 目前暂缓开发，在完善设计后在发布.
 *
 * @author unknowIfGuestInDream
 * @since 1.0.1
 */
public class DataFormatConvert extends CommonSample {

    private final FileChooser outputChooser = new FileChooser();
    private final ObservableList<String> datasourceList = FXCollections.observableArrayList();
    private ComboBox<String> cmbDatasource;
    private TextField dataField;
    private final Notifications notificationBuilder = FxNotifications.defaultNotify();

    private final Action generate = FxAction.generate(actionEvent -> {
        FileChooser.ExtensionFilter extFilter = FileChooserUtil.excelFilter();
        outputChooser.getExtensionFilters().add(extFilter);
        File output = outputChooser.showSaveDialog(FxApp.primaryStage);
        if (output != null) {
            String resultFileName = output.getName();
            String resultPath = output.getParent();
            outputChooser.setInitialDirectory(output.getParentFile());
            if (output.exists()) {
                FileUtil.del(output);
            }
            //bindUserData();
        }
    });

    private final Collection<? extends Action> actions = List.of(generate);

    @Override
    public void initializeBindings() {
        super.initializeBindings();
        FileChooserUtil.setOnDrag(dataField, FileChooserUtil.FileType.FILE);
        initializeDataSource();
    }

    private void initializeDataSource() {
        datasourceList.add("XML");
        if (CoreUtil.hasClass("org.apache.commons.csv.CSVParser")) {
            datasourceList.add("CSV");
        }
        if (CoreUtil.hasClass("org.apache.poi.Version")) {
            datasourceList.add("Excel");
        }
        if (CoreUtil.hasClass("com.fasterxml.jackson.databind.ObjectMapper")) {
            datasourceList.add("JSON");
        }
        cmbDatasource.valueProperty().addListener((observable, oldValue, newValue) -> {
            dataField.clear();
        });
        cmbDatasource.getSelectionModel().select(0);
    }

    @Override
    public void initializeUserDataBindings() {
        super.initializeUserDataBindings();
    }

    @Override
    public Node getPanel(Stage stage) {
        GridPane grid = new GridPane();
        grid.setVgap(12);
        grid.setHgap(12);
        grid.setPadding(new Insets(10));

        ToolBar toolBar = ActionUtils.createToolBar(actions, ActionUtils.ActionTextBehavior.SHOW);
        GridPane.setHgrow(toolBar, Priority.ALWAYS);

        Label datasourceLabel = new Label("data from");
        cmbDatasource = new ComboBox<>(datasourceList);
        cmbDatasource.setMaxWidth(Double.MAX_VALUE);

        Label sheetNameLabel = new Label("textarea");
        dataField = new TextField();
        dataField.setEditable(false);
        Button dataButton = FxButton.choose();
        dataButton.setOnAction(arg0 -> chooseDataSource());
        GridPane.setHgrow(dataField, Priority.ALWAYS);

        TabPane resultPane = new TabPane();
        initTabPane(resultPane);

        grid.add(toolBar, 0, 0, 3, 1);
        grid.add(datasourceLabel, 0, 1);
        grid.add(cmbDatasource, 1, 1, 2, 1);
        grid.add(sheetNameLabel, 0, 2);
        grid.add(dataButton, 1, 2);
        grid.add(dataField, 2, 2);
        grid.add(resultPane, 0, 3, 3, 1);
        return grid;
    }

    /**
     * 选择数据源文件.
     */
    private void chooseDataSource() {
        FileChooser.ExtensionFilter filter = getFilter(cmbDatasource.getValue());
        FileChooser dataChooser = new FileChooser();
        dataChooser.getExtensionFilters().add(filter);
        File file = dataChooser.showOpenDialog(FxApp.primaryStage);
        if (file != null) {
            dataField.setText(file.getPath());
        }
    }

    private void initTabPane(TabPane resultPane) {
        resultPane.getTabs().addAll(createMarkdownTab(), createXmlTab(), createExcelTab(), createJsonTab());
    }

    private Tab createMarkdownTab() {
        Tab tab = new Tab("Markdown");
        tab.setClosable(false);
        VBox vbox = new VBox();
        vbox.getChildren().addAll(new CheckBox("第一行作为标题"), new CheckBox("使用精简格式"));
        tab.setContent(vbox);
        return tab;
    }

    private Tab createXmlTab() {
        Tab tab = new Tab("XML");
        tab.setClosable(false);
        VBox vbox = new VBox();
        vbox.getChildren().addAll(new CheckBox("第一行作为标题"), new CheckBox("使用精简格式"));
        tab.setContent(vbox);
        return tab;
    }

    private Tab createExcelTab() {
        Tab tab = new Tab("Excel");
        tab.setClosable(false);
        VBox vbox = new VBox();
        vbox.getChildren().addAll(new CheckBox("第一行作为标题"), new CheckBox("使用精简格式"));
        tab.setContent(vbox);
        return tab;
    }

    private Tab createJsonTab() {
        Tab tab = new Tab("JSON");
        tab.setClosable(false);
        VBox vbox = new VBox();
        vbox.getChildren().addAll(new CheckBox("第一行作为标题"), new CheckBox("使用精简格式"));
        tab.setContent(vbox);
        return tab;
    }

    private FileChooser.ExtensionFilter getFilter(String value) {
        if ("XML".equals(value)) {
            return FileChooserUtil.xmlFilter();
        } else if ("CSV".equals(value)) {
            return FileChooserUtil.csvFilter();
        } else if ("Excel".equals(value)) {
            return FileChooserUtil.xlsxFilter();
        } else if ("JSON".equals(value)) {
            return FileChooserUtil.jsonFilter();
        } else {
            throw new UnExpectedResultException("Unexpected value: " + cmbDatasource.getValue());
        }
    }

    @Override
    public Node getControlPanel() {
        String content = """
            """;
        Map<String, String> map = new HashMap<>();
        return FxTextInput.textArea(StrUtil.format(content, map));
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public String getSampleId() {
        return "dataFormatConvert";
    }

    @Override
    public String getSampleName() {
        return "DataFormatConvert";
    }

    @Override
    public String getSampleVersion() {
        return "1.0.1";
    }

    @Override
    public String getSampleDescription() {
        return "";
    }

    @Override
    public ImageView getSampleImageIcon() {
        return LayoutHelper.iconView(getClass().getResource("/com/tlcsdm/jfxcommon/static/icon/data.png"));
    }

    @Override
    public String getOrderKey() {
        return "dataFormatConvert";
    }

    /**
     * 此结果在开发环境和打包后执行结果不同，原因是打包后执行SPI配置时需要从应用模块的配置中读取.
     * 但在开发环境由于模块化原因即使SPI配置中未配置实现也可以读取到.
     */
    @Override
    public boolean isVisible() {
        String value = System.getProperty(CoreConstant.JVM_WORKENV);
        return CoreConstant.JVM_WORKENV_DEV.equals(value);
//        if (!CoreUtil.hasClass("freemarker.cache.TemplateLoader")) {
//            return false;
//        }
//        TemplateLoader templateLoader = FreemarkerUtil.configuration().getTemplateLoader();
//        if (templateLoader instanceof MultiTemplateLoader loader) {
//            for (int i = 0; i < loader.getTemplateLoaderCount(); i++) {
//                if (loader.getTemplateLoader(i) instanceof ClassTemplateLoader l) {
//                    if (l.getBasePackagePath().contains(CommonConstant.FREEMARKER_BASE_PACKAGE_PATH)) {
//                        return true;
//                    }
//                }
//            }
//        }
//        return false;
    }

}
