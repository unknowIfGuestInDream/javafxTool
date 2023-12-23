/*
 * Copyright (c) 2019, 2023 unknowIfGuestInDream
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

package com.tlcsdm.smc.codeDev.ecm;

import cn.hutool.poi.excel.ExcelReader;
import com.tlcsdm.core.factory.InitializingFactory;
import com.tlcsdm.core.util.GroovyUtil;
import com.tlcsdm.core.util.InterfaceScanner;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * C1M的ECM脚本.
 *
 * @author unknowIfGuestInDream
 * @since 1.0.1
 */
public final class C1MEcmScript extends AbstractEcmScript {

    @Override
    protected void initDefaultValue() {
        super.initDefaultValue();
        startRowField.setNumber(BigDecimal.valueOf(2));
        sheetNameField.setText("C1M");
        categoryConfigField.setText("""
            categoryId;B
            categoryEnName;C
            categoryJpName;D
            """);
        functionConfigField.setText("""
            optMaskableInpt;G
            optEFInpt;H
            optIntrg;I
            optErroroutput;J
            optDelayt;K
            """);
        errorSourceIdColField.setText("A");
        categoryIdColField.setText("B");
        errorSourceNumberColField.setText("C");
        errorSourceEnNameColField.setText("E");
        errorSourceJpNameColField.setText("L");
        productConfigField.setText("""
            RH850C1MA2;252;-
            """);
        tagConfigField.setText("""
            psedu;N
            funname;O
            titleabstract;P
            """);

        errorSourceDescColLabel.setDisable(true);
        errorSourceDescColField.setDisable(true);
    }

    @Override
    public TitledPane createErrorSourceControl() {
        TitledPane titledPane = super.createErrorSourceControl();
        GridPane grid = (GridPane) titledPane.getContent();

        grid.getChildren().remove(errorSourceDescColLabel);
        grid.getChildren().remove(errorSourceDescColField);
        return titledPane;
    }

    @Override
    public void initializeUserDataBindings() {
        super.initializeUserDataBindings();
        userData.remove("errorSourceDesc");
    }

    @Override
    protected Map<String, Object> dealErrorSourceData(ExcelReader reader, int rowNum, String product) {
        String errorSourceIdCol = errorSourceIdColField.getText();
        String categoryIdCol = categoryIdColField.getText();
        String errorSourceNumberCol = errorSourceNumberColField.getText();
        String errorSourceEnNameCol = errorSourceEnNameColField.getText();
        String errorSourceJpNameCol = errorSourceJpNameColField.getText();
        LinkedHashMap<String, String> operationMap = createOperationMap();
        LinkedHashMap<String, String> tagMap = createTagMap();

        String errorSourceId = reader.getCell(errorSourceIdCol + rowNum).getStringCellValue();
        String categoryId = reader.getCell(categoryIdCol + rowNum).getStringCellValue();
        String errorSourceNumber = String
            .valueOf((int) reader.getCell(errorSourceNumberCol + rowNum).getNumericCellValue());
        String errorSourceenName = reader.getCell(errorSourceEnNameCol + rowNum).getStringCellValue();
        String errorSourcejpName = reader.getCell(errorSourceJpNameCol + rowNum).getStringCellValue();
        List<Map<String, Object>> function = new ArrayList<>();
        boolean optMaskintStatus = false;
        for (String funcId : operationMap.keySet()) {
            String funcCol = operationMap.get(funcId);
            String funcSupCondition = reader.getCell(funcCol + rowNum).getStringCellValue();
            // support 向下判断
            boolean support = !(funcSupCondition.contains("—") || funcSupCondition.contains("-"));
            if ("optMaskableInpt".equals(funcId)) {
                optMaskintStatus = support;
            }
            Map<String, Object> operation = new HashMap<>(4);
            operation.put("funcId", funcId);
            operation.put("support", String.valueOf(support));
            operation.put("errorNote", "");
            // 数据后置处理
            handlerOperationSupport(operation, funcSupCondition, optMaskintStatus);
            function.add(operation);
        }
        List<Map<String, Object>> tag = new ArrayList<>(tagMap.size());
        for (String tagkey : tagMap.keySet()) {
            String tagCol = tagMap.get(tagkey);
            String tagValue = reader.getCell(tagCol + rowNum).getStringCellValue();
            Map<String, Object> tagMeta = new HashMap<>();
            if ("psedu".equals(tagkey)) {
                tagValue = String.valueOf(Boolean.valueOf(!"―".equals(tagValue) && !tagValue.trim().isEmpty()));
            }
            tagMeta.put("key", tagkey);
            tagMeta.put("value", tagValue);
            tag.add(tagMeta);
        }
        Map<String, Object> errorSource = new HashMap<>(16);
        errorSource.put("errorSourceId", errorSourceId);
        errorSource.put("categoryId", categoryId);
        errorSource.put("errorSourceNumber", errorSourceNumber);
        errorSource.put("errorSourceEnName", errorSourceenName);
        errorSource.put("errorSourceJpName", errorSourcejpName);
        errorSource.put("function", function);
        errorSource.put("tag", tag);
        handlerErrorSourceMap(errorSource);
        return errorSource;
    }

    public static void main(String[] args) {
        InterfaceScanner.invoke(InitializingFactory.class, "initialize");
        launch(args);
    }

    @Override
    public String getSampleId() {
        return "c1MEcmScript";
    }

    @Override
    public String getSampleName() {
        return "C1MECMScript";
    }

    @Override
    public Node getPanel(Stage stage) {
        return super.getPanel(stage);
    }

    @Override
    public String getOrderKey() {
        return "C1MEcmScript";
    }

    /**
     * errorSource 数据后续处理
     */
    private void handlerErrorSourceMap(Map<String, Object> errorSource) {
        GroovyUtil.invokeMethod(getGroovyPath(), "handlerErrorSourceMap", errorSource);
    }

    /**
     * 处理使能条件的 * 信息, 默认是support = true下的
     */
    private void handlerOperationSupport(Map<String, Object> operation, String funcSupCondition,
                                         boolean optMaskintStatus) {
        // Do nothing
    }

    @Override
    protected String getFtlPath() {
        return "smc/ecm/c1m.ftl";
    }

    @Override
    protected String getGroovyPath() {
        return "codeDev/ecm/c1m.groovy";
    }
}
