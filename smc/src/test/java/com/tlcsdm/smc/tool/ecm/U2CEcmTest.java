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

package com.tlcsdm.smc.tool.ecm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.tlcsdm.core.util.FreemarkerUtil;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class U2CEcmTest {

    private static Configuration configuration;

    @BeforeAll
    public static void init() {
        configuration = new Configuration(Configuration.VERSION_2_3_32);
        try {
            configuration.setDirectoryForTemplateLoading(new File(ResourceUtil.getResource("templates").getPath()));
            configuration.setDefaultEncoding("utf-8");
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            configuration.setSetting(Configuration.CACHE_STORAGE_KEY, "strong:20,soft:250");
        } catch (IORuntimeException | IOException | TemplateException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void dealData() {
        // 输入值获取
        String parentDirectoryPath = "C:\\workspace\\test\\ecmSpec";
        String excelName = "ErrorSource.xlsx";
        String outputPath = "C:\\workspace\\test";

        String categorySheetName = "Category";
        int categoryStartRow = 2;
        String categorys = """
                categoryId;F
                categoryEnName;G
                categoryJpName;H
                """;

        String resultPath = outputPath + "\\ecm";
        String deviceSheetName = "U2A";
        int startRow = 3;
        String functions = """
                optMaskint;G
                optIntg;G
                optDCLS;G
                optIntrg;I
                optErroroutput;J
                optErrort;K
                optDelayt;L
                """;
        String errorSourceIdCol = "A";
        String categoryIdCol = "B";
        String errorSourceNumberCol = "C";
        String errorSourceenNameCol = "D";
        String errorSourcejpNameCol = "W";

        LinkedHashMap<String, String> operationMap = new LinkedHashMap<>();
        List<String> operationConfigs = StrUtil.splitTrim(functions, "\n");
        for (String operationConfig : operationConfigs) {
            List<String> l = StrUtil.split(operationConfig, ";");
            operationMap.put(l.get(0), l.get(1));
        }
        String products = """
                RH850U2A16516;N
                RH850U2A16373;O
                RH850U2A16292;P
                RH850U2A8516;Q
                RH850U2A8373;R
                RH850U2A8292;S
                RH850U2A6292176;T
                RH850U2A6156;U
                RH850U2A6144;V
                """;
        LinkedHashMap<String, String> productMap = new LinkedHashMap<>();
        List<String> productConfigs = StrUtil.splitTrim(products, "\n");
        for (String productConfig : productConfigs) {
            List<String> l = StrUtil.split(productConfig, ";");
            productMap.put(l.get(0), l.get(1));
        }
        // category 配置数据
        LinkedHashMap<String, String> categoryMap = new LinkedHashMap<>();
        List<String> categoryConfigs = StrUtil.splitTrim(categorys, "\n");
        for (String categoryConfig : categoryConfigs) {
            List<String> l = StrUtil.split(categoryConfig, ";");
            categoryMap.put(l.get(0), l.get(1));
        }
        // category 数据处理
        ExcelReader categoryReader = ExcelUtil.getReader(FileUtil.file(parentDirectoryPath, excelName),
                categorySheetName);
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

        ExcelReader reader = ExcelUtil.getReader(FileUtil.file(parentDirectoryPath, excelName), deviceSheetName);
        int endRow = reader.getRowCount();
        // 清空resultPath下文件
        FileUtil.clean(resultPath);
        // 便利products
        for (String key : productMap.keySet()) {
            List<Map<String, Object>> ErrorSourceInfos = new ArrayList<>();
            String productCol = productMap.get(key);
            // 遍历excel sheet数据
            for (int i = startRow; i <= endRow; i++) {
                // todo 判断合并的key计算 ArrayList equals
                Cell cell = reader.getCell(errorSourceIdCol + i);
                if (cell == null) {
                    continue;
                }
                if (StrUtil.isBlank(cell.getStringCellValue())) {
                    continue;
                }
                String productCondition = reader.getCell(productCol + i).getStringCellValue();
                if (productCondition.contains("—") || productCondition.contains("-")) {
                    continue;
                }
                // todo 合并标记
                String errorSourceId = reader.getCell(errorSourceIdCol + i).getStringCellValue();
                String categoryId = reader.getCell(categoryIdCol + i).getStringCellValue();
                String errorSourceNumber = String
                        .valueOf((int) reader.getCell(errorSourceNumberCol + i).getNumericCellValue());
                String errorSourceenName = reader.getCell(errorSourceenNameCol + i).getStringCellValue();
                String errorSourcejpName = reader.getCell(errorSourcejpNameCol + i).getStringCellValue();
                List<Map<String, Object>> function = new ArrayList<>();
                for (String funcId : operationMap.keySet()) {
                    String funcCol = operationMap.get(funcId);
                    String funcSupCondition = reader.getCell(funcCol + i).getStringCellValue();
                    // support 向下判断
                    boolean support = !(funcSupCondition.contains("—") || funcSupCondition.contains("-"));
                    Map<String, Object> operation = new HashMap<>();
                    operation.put("funcId", funcId);
                    operation.put("support", String.valueOf(support));
                    operation.put("errorNote", "");
                    // 数据后置处理
                    handlerOperationSupport(operation, funcSupCondition);
                    function.add(operation);
                }
                Map<String, Object> errorSource = new HashMap<>();
                errorSource.put("errorSourceId", errorSourceId);
                errorSource.put("categoryId", categoryId);
                errorSource.put("errorSourceNumber", errorSourceNumber);
                errorSource.put("errorSourceenName", errorSourceenName);
                errorSource.put("errorSourcejpName", errorSourcejpName);
                errorSource.put("function", function);
                handlerErrorSourceMap(errorSource, key);
                ErrorSourceInfos.add(errorSource);
            }
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("categoryInfos", categoryInfos);
            paramMap.put("errorSourceInfos", ErrorSourceInfos);
            // todo 触发合并条件，中断
            File result = FileUtil.newFile(resultPath + "\\" + key + ".xml");
            FileUtil.appendUtf8String(
                    FreemarkerUtil.getTemplateContent(configuration, paramMap, getFtlPath(deviceSheetName)), result);
        }
        reader.close();
    }

    /**
     * errorSource 数据后续处理
     */
    private void handlerErrorSourceMap(Map<String, Object> errorSource, String product) {
        String errorSourceenName = (String) errorSource.get("errorSourceenName");
        String errorSourcejpName = (String) errorSource.get("errorSourcejpName");
        errorSourceenName = cleanErrorSourceData(errorSourceenName);
        errorSourcejpName = cleanErrorSourceData(errorSourcejpName);
        errorSource.put("errorSourceenName", errorSourceenName);
        errorSource.put("errorSourcejpName", errorSourcejpName);

        List<Map<String, Object>> function = (List<Map<String, Object>>) errorSource.get("function");
        List<Map<String, Object>> extraFunc = new ArrayList<>();
        for (Map<String, Object> map : function) {
            String funcId = map.get("funcId").toString();
            if ("optErrort".equals(funcId)) {
                String support = map.get("support").toString();
                String errorNote = map.get("errorNote").toString();
                int size = 0;
                if (product.startsWith("RH850U2A16")) {
                    size = 4;
                    generateErrort(size, support, errorNote, extraFunc);
                    function.remove(map);
                } else if (product.startsWith("RH850U2A8") || product.startsWith("RH850U2A6")) {
                    size = 2;
                    generateErrort(size, support, errorNote, extraFunc);
                    function.remove(map);
                } else {
                    break;
                }
            }
        }
        function.addAll(extraFunc);
    }

    private void generateErrort(int size, String support, String errorNote, List<Map<String, Object>> extraFunc) {
        for (int i = 0; i < size; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("funcId", "optErrort" + i);
            map.put("support", support);
            map.put("errorNote", errorNote);
            extraFunc.add(map);
        }
    }

    /**
     *  处理使能条件的 * 信息, 默认是support = true下的
     */
    private void handlerOperationSupport(Map<String, Object> operation, String funcSupCondition) {
        if (funcSupCondition.contains("*")) {
            String mesNum = StrUtil.subAfter(funcSupCondition, "*", true);
            if ("1".equals(mesNum) || "2".equals(mesNum)) {
                operation.put("errorNote", mesNum);
            }
            if ("5".equals(mesNum)) {
                String funcId = operation.get("funcId").toString();
                if ("optDCLS".equals(funcId)) {
                    operation.put("support", "true");
                }
                if ("optIntg".equals(funcId)) {
                    operation.put("support", "false");
                }
            }
        } else {
            String funcId = operation.get("funcId").toString();
            if ("optDCLS".equals(funcId)) {
                operation.put("support", "false");
            }
            if ("optIntg".equals(funcId)) {
                operation.put("support", "true");
            }
        }

    }

    /**
     * 清洗ErrorSource数据
     */
    private String cleanErrorSourceData(String data) {
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
        if (data.endsWith("*7")) {
            data = StrUtil.replaceLast(data, "*7", "(For debug purpose only)");
        }
        return data;
    }

    private String getFtlPath(String sheetName) {
        if ("U2A".equals(sheetName)) {
            return "ecm/u2a.ftl";
        }
        return null;
    }

}
