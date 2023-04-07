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

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;

import com.tlcsdm.core.util.FreemarkerUtil;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.map.multi.ListValueMap;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import javafx.scene.Node;
import javafx.stage.Stage;

/**
 * C1M的ECM脚本
 *
 * @author: unknowIfGuestInDream
 * @date: 2023/3/26 21:17
 */
public class C1MEcmScript extends AbstractEcmScript {

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
                opMaskableInpt;G
                opEFInpt;H
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
                RH850C1MA2;252;252;-
                """);

        errorSourceDescColLabel.setDisable(true);
        errorSourceDescColField.setDisable(true);
    }

    @Override
    protected void dealData() {
        // 输入值获取
        String excel = excelField.getText();
        String outputPath = outputField.getText();
        String deviceSheetName = sheetNameField.getText();
        int deviceStartRow = Integer.parseInt(startRowField.getText());
        // Category
        String categorySheetName = categorySheetNameField.getText();
        int categoryStartRow = Integer.parseInt(categoryStartRowField.getText());
        String categorys = categoryConfigField.getText();
        // Error Source
        String functions = functionConfigField.getText();
        String errorSourceIdCol = errorSourceIdColField.getText();
        String categoryIdCol = categoryIdColField.getText();
        String errorSourceNumberCol = errorSourceNumberColField.getText();
        String errorSourceEnNameCol = errorSourceEnNameColField.getText();
        String errorSourceJpNameCol = errorSourceJpNameColField.getText();
        String products = productConfigField.getText();

        String resultPath = outputPath + outParentFolder;

        LinkedHashMap<String, String> operationMap = new LinkedHashMap<>();
        List<String> operationConfigs = StrUtil.splitTrim(functions, "\n");
        for (int i = 0; i < operationConfigs.size(); i++) {
            String operationConfig = operationConfigs.get(i);
            List<String> l = StrUtil.split(operationConfig, ";");
            operationMap.put(l.get(0), l.get(1));
        }
        LinkedHashMap<String, String> productMap = new LinkedHashMap<>();
        List<String> productConfigs = StrUtil.splitTrim(products, "\n");
        ListValueMap<String, String> productsInfo = new ListValueMap<>();
        for (String productConfig : productConfigs) {
            List<String> l = StrUtil.split(productConfig, ";");
            productMap.put(l.get(0) + "_" + l.get(1), l.get(2));
            productsInfo.putValue(l.get(0), l.get(1));
        }
        // category 配置数据
        LinkedHashMap<String, String> categoryMap = new LinkedHashMap<>();
        List<String> categoryConfigs = StrUtil.splitTrim(categorys, "\n");
        for (String categoryConfig : categoryConfigs) {
            List<String> l = StrUtil.split(categoryConfig, ";");
            categoryMap.put(l.get(0), l.get(1));
        }
        // category 数据处理
        ExcelReader categoryReader = ExcelUtil.getReader(FileUtil.file(excel), categorySheetName);
        int categoryEndRow = categoryReader.getRowCount();
        List<Map<String, Object>> categoryInfos = new ArrayList<>();
        for (int i = categoryStartRow; i <= categoryEndRow; i++) {
            Map<String, Object> categoryInfo = new HashMap<>();
            for (String key : categoryMap.keySet()) {
                Cell cell = categoryReader.getCell(categoryMap.get(key) + i);
                if (cell == null) {
                    continue;
                }
                String value = cell.getStringCellValue();
                if (StrUtil.isBlank(value)) {
                    continue;
                }
                categoryInfo.put(key, value);
            }
            if (categoryInfo.isEmpty()) {
                continue;
            }
            categoryInfos.add(categoryInfo);
        }
        categoryReader.close();

        ExcelReader reader = ExcelUtil.getReader(FileUtil.file(excel), deviceSheetName);
        int endRow = reader.getRowCount();
        // 清空resultPath下文件
        FileUtil.clean(resultPath);
        // 遍历productMap
        for (String key : productMap.keySet()) {
            List<Map<String, Object>> ErrorSourceInfos = new ArrayList<>();
            // 遍历excel sheet数据
            for (int i = deviceStartRow; i <= endRow; i++) {
                Cell cell = reader.getCell(errorSourceIdCol + i);
                if (cell == null) {
                    continue;
                }
                if (StrUtil.isBlank(cell.getStringCellValue())) {
                    continue;
                }
                String errorSourceId = reader.getCell(errorSourceIdCol + i).getStringCellValue();
                String categoryId = reader.getCell(categoryIdCol + i).getStringCellValue();
                String errorSourceNumber = String
                        .valueOf((int) reader.getCell(errorSourceNumberCol + i).getNumericCellValue());
                String errorSourceenName = reader.getCell(errorSourceEnNameCol + i).getStringCellValue();
                String errorSourcejpName = reader.getCell(errorSourceJpNameCol + i).getStringCellValue();
                List<Map<String, Object>> function = new ArrayList<>();
                boolean optMaskintStatus = false;
                for (String funcId : operationMap.keySet()) {
                    String funcCol = operationMap.get(funcId);
                    String funcSupCondition = reader.getCell(funcCol + i).getStringCellValue();
                    // support 向下判断
                    boolean support = !(funcSupCondition.contains("—") || funcSupCondition.contains("-"));
                    if ("opMaskableInpt".equals(funcId)) {
                        optMaskintStatus = support;
                    }
                    Map<String, Object> operation = new HashMap<>();
                    operation.put("funcId", funcId);
                    operation.put("support", String.valueOf(support));
                    operation.put("errorNote", "");
                    // 数据后置处理
                    handlerOperationSupport(operation, funcSupCondition, optMaskintStatus);
                    function.add(operation);
                }
                Map<String, Object> errorSource = new HashMap<>();
                errorSource.put("errorSourceId", errorSourceId);
                errorSource.put("categoryId", categoryId);
                errorSource.put("errorSourceNumber", errorSourceNumber);
                errorSource.put("errorSourceEnName", errorSourceenName);
                errorSource.put("errorSourceJpName", errorSourcejpName);
                errorSource.put("function", function);
                handlerErrorSourceMap(errorSource, key, 0);
                ErrorSourceInfos.add(errorSource);
            }
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("categoryInfos", categoryInfos);
            paramMap.put("errorSourceInfos", ErrorSourceInfos);
            File result = FileUtil.newFile(resultPath + "\\" + key + ".xml");
            FileUtil.appendUtf8String(FreemarkerUtil.getTemplateContent(paramMap, getFtlPath()), result);
        }
        reader.close();
        // 后续文件合并
        // 待删除文件
        List<String> delFileNames = new ArrayList<>();
        for (String device : productsInfo.keySet()) {
            List<String> list = productsInfo.get(device);
            if (list.size() == 1) {
                FileUtil.rename(FileUtil.file(resultPath, device + "_" + list.get(0) + ".xml"), device + ".xml", true);
                continue;
            }
            for (int i = 0; i < list.size() - 1; i++) {
                for (int j = i + 1; j < list.size(); j++) {
                    String orgName = device + "_" + list.get(i) + ".xml";
                    String comName = device + "_" + list.get(j) + ".xml";
                    boolean b = FileUtil.contentEquals(FileUtil.file(resultPath, orgName),
                            FileUtil.file(resultPath, comName));
                    if (b) {
                        if (!delFileNames.contains(orgName) && !delFileNames.contains(comName)) {
                            String deviceName = device + ".xml";
                            if (FileUtil.file(resultPath, deviceName).exists()) {
                                deviceName = device + "-" + UUID.fastUUID() + ".xml";
                            }
                            FileUtil.copyFile(FileUtil.file(resultPath, orgName),
                                    FileUtil.file(resultPath, deviceName));
                        }
                        if (!delFileNames.contains(orgName)) {
                            delFileNames.add(orgName);
                        }
                        if (!delFileNames.contains(comName)) {
                            delFileNames.add(comName);
                        }
                    }
                }
            }
        }
        for (String fileName : delFileNames) {
            FileUtil.del(FileUtil.file(resultPath, fileName));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public String getSampleId() {
        return "c1MEcmScript";
    }

    @Override
    public String getSampleName() {
        return "C1MEcmScript";
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
    private void handlerErrorSourceMap(Map<String, Object> errorSource, String product, int optErrortIndex) {
        String errorSourceenName = (String) errorSource.get("errorSourceEnName");
        String errorSourcejpName = (String) errorSource.get("errorSourceJpName");
        errorSourceenName = cleanErrorSourceData(errorSourceenName);
        errorSourcejpName = cleanErrorSourceData(errorSourcejpName);
        errorSource.put("errorSourceEnName", errorSourceenName);
        errorSource.put("errorSourceJpName", errorSourcejpName);
    }

    /**
     * 处理使能条件的 * 信息, 默认是support = true下的
     */
    private void handlerOperationSupport(Map<String, Object> operation, String funcSupCondition,
            boolean optMaskintStatus) {
        // Do nothing
    }

    /**
     * 清洗ErrorSource数据
     */
    private String cleanErrorSourceData(String data) {
        data = data.replaceAll("  ", " ");
        if (data.contains(" (")) {
            data = StrUtil.replace(data, " (", "(");
        }
        if (data.contains("\n")) {
            List<String> list = StrUtil.split(data, "\n");
            data = list.get(0);
            for (int i = 1; i < list.size(); i++) {
                data += " ";
                data += list.get(i).replaceFirst("- ", "-");
            }
        }
        data = data.replaceAll("-", " - ");
        data = data.replaceAll("  ", " ");
        if (data.contains(" - bit")) {
            data = data.replaceAll(" - bit", "-bit");
        }
        if (data.contains("P - Bus")) {
            data = data.replaceAll("P - Bus", "P-Bus");
        }
        if (data.contains("I - Bus")) {
            data = data.replaceAll("I - Bus", "I-Bus");
        }
        if (data.contains("H - Bus")) {
            data = data.replaceAll("H - Bus", "H-Bus");
        }
        if (data.contains("*")) {
            List<String> list = StrUtil.split(data, "*");
            data = list.get(0);
        }
        return data;
    }

    @Override
    protected String getFtlPath() {
        return "smc/ecm/c1m.ftl";
    }
}
