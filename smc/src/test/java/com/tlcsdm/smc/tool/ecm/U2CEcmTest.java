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

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.map.multi.ListValueMap;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.tlcsdm.core.util.FreemarkerUtil;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.apache.poi.ss.usermodel.Cell;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@DisabledIfSystemProperty(named = "workEnv", matches = "ci")
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
        int categoryStartRow = 3;
        String categorys = """
            categoryId;J
            categoryEnName;K
            categoryJpName;L
            """;

        String resultPath = outputPath + "\\ecm";
        String deviceSheetName = "U2C";
        int startRow = 3;
        String functions = """
            optMaskint;G
            optIntg;G
            optDCLS;G
            optIntrg;I
            optErroroutput;J
            optErrort0;K
            optErrort1;K
            optErrort2;K
            optErrort3;K
            optDelayt;L
            """;

        String tags = """
            psedu;W
            funname;X
            titleabstract;Y
            """;
        String errorSourceIdCol = "A";
        String categoryIdCol = "B";
        String errorSourceNumberCol = "C";
        String errorSourceenNameCol = "D";
        String errorSourceDescCol = "E";
        String errorSourcejpNameCol = "U";

        LinkedHashMap<String, String> operationMap = new LinkedHashMap<>();
        List<String> operationConfigs = StrUtil.splitTrim(functions, "\n");
        for (int i = 0; i < operationConfigs.size(); i++) {
            String operationConfig = operationConfigs.get(i);
            List<String> l = StrUtil.split(operationConfig, ";");
            operationMap.put(l.get(0), l.get(1));
        }
        LinkedHashMap<String, String> tagMap = new LinkedHashMap<>();
        List<String> tagConfigs = StrUtil.splitTrim(tags, "\n");
        for (int i = 0; i < tagConfigs.size(); i++) {
            String tagConfig = tagConfigs.get(i);
            List<String> l = StrUtil.split(tagConfig, ";");
            tagMap.put(l.get(0), l.get(1));
        }

        String products = """
            RH850U2C8;292;O
            RH850U2C4;292;P
            RH850U2C4;144;Q
            RH850U2C4;100;R
            RH850U2C2;144;S
            RH850U2C2;100;T
            """;
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
        // 遍历productMap
        for (String key : productMap.keySet()) {
            List<Map<String, Object>> ErrorSourceInfos = new ArrayList<>();
            String productCol = productMap.get(key);
            // 遍历excel sheet数据
            for (int i = startRow; i <= endRow; i++) {
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
                String errorSourceId = reader.getCell(errorSourceIdCol + i).getStringCellValue();
                String categoryId = reader.getCell(categoryIdCol + i).getStringCellValue();
                int errorSourceNum = (int) reader.getCell(errorSourceNumberCol + i).getNumericCellValue();
                String errorSourceNumber = String.valueOf(errorSourceNum);
                String errorSourceenName = reader.getCell(errorSourceenNameCol + i).getStringCellValue();
                String errorSourcejpName = reader.getCell(errorSourcejpNameCol + i).getStringCellValue();
                String errorSourceDesc = reader.getCell(errorSourceDescCol + i).getStringCellValue();
                // 特殊处理 24-29添加description信息
                if (errorSourceNum < 24 || errorSourceNum > 29) {
                    errorSourceDesc = "";
                }
                List<Map<String, Object>> function = new ArrayList<>();
                boolean optMaskintStatus = false;
                for (String funcId : operationMap.keySet()) {
                    String funcCol = operationMap.get(funcId);
                    String funcSupCondition = reader.getCell(funcCol + i).getStringCellValue();
                    // support 向下判断
                    boolean support = !(funcSupCondition.contains("—") || funcSupCondition.contains("-"));
                    if ("optMaskint".equals(funcId)) {
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
                List<Map<String, Object>> tag = new ArrayList<>();
                for (String tagkey : tagMap.keySet()) {
                    String tagCol = tagMap.get(tagkey);
                    String tagValue = reader.getCell(tagCol + i).getStringCellValue();
                    Map<String, Object> tagMeta = new HashMap<>();
                    if ("psedu".equals(tagkey)) {
                        tagValue = String
                            .valueOf(Boolean.valueOf(!"―".equals(tagValue) && tagValue.trim().length() > 0));
                    }
                    tagMeta.put("key", tagkey);
                    tagMeta.put("value", tagValue);
                    tag.add(tagMeta);
                }
                Map<String, Object> errorSource = new HashMap<>();
                errorSource.put("errorSourceId", errorSourceId);
                errorSource.put("categoryId", categoryId);
                errorSource.put("errorSourceNumber", errorSourceNumber);
                errorSource.put("errorSourceEnName", errorSourceenName);
                errorSource.put("errorSourceJpName", errorSourcejpName);
                errorSource.put("errorSourceDesc", errorSourceDesc.replaceAll("\n", " "));
                errorSource.put("function", function);
                errorSource.put("tag", tag);
                handlerErrorSourceMap(errorSource, key);
                ErrorSourceInfos.add(errorSource);
            }
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("categoryInfos", categoryInfos);
            paramMap.put("errorSourceInfos", ErrorSourceInfos);
            File result = FileUtil.newFile(resultPath + "\\" + key + ".xml");
            FileUtil.appendUtf8String(
                FreemarkerUtil.getTemplateContent(configuration, paramMap, getFtlPath(deviceSheetName)), result);
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

    /**
     * errorSource 数据后续处理
     */
    private void handlerErrorSourceMap(Map<String, Object> errorSource, String product) {
        String errorSourceenName = (String) errorSource.get("errorSourceEnName");
        String errorSourcejpName = (String) errorSource.get("errorSourceJpName");
        errorSourceenName = cleanErrorSourceData(errorSourceenName);
        errorSourcejpName = cleanErrorSourceData(errorSourcejpName);
        if (errorSourceenName.endsWith("*5")) {
            errorSourceenName = StrUtil.replaceLast(errorSourceenName, "*5", "(For debug purpose only)");
            if (errorSourcejpName.endsWith("*5")) {
                errorSourcejpName = StrUtil.replaceLast(errorSourcejpName, "*5", "(デバッグ目的のみ)");
            } else {
                errorSourcejpName += "(デバッグ目的のみ)";
            }
        }
        errorSource.put("errorSourceEnName", errorSourceenName);
        errorSource.put("errorSourceJpName", errorSourcejpName);
    }

    /**
     * 处理使能条件的 * 信息, 默认是support = true下的
     */
    private void handlerOperationSupport(Map<String, Object> operation, String funcSupCondition,
                                         boolean optMaskintStatus) {
        if (funcSupCondition.contains("*")) {
            String mesNum = StrUtil.subAfter(funcSupCondition, "*", true);
            if ("1".equals(mesNum) || "2".equals(mesNum)) {
                operation.put("errorNote", mesNum);
            }
            if ("3".equals(mesNum)) {
                String funcId = operation.get("funcId").toString();
                if ("optDCLS".equals(funcId)) {
                    operation.put("support", String.valueOf(optMaskintStatus));
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
                operation.put("support", String.valueOf(optMaskintStatus));
            }
        }

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
        return data;
    }

    private String getFtlPath(String sheetName) {
        if ("U2C".equals(sheetName)) {
            return "ecm/u2c.ftl";
        }
        return null;
    }

}
