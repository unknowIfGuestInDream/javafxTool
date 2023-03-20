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
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.tlcsdm.core.util.FreemarkerUtil;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

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
        String parentDirectoryPath = "C:\\workspace\\test\\ecm";
        String excelName = "ErrorSource.xlsx";
        String outputPath = "C:\\workspace\\test";
        String resultPath = outputPath + "\\ecm";
        String sheetName = "U2A";
        int startRow = 3;
        String functions = """
                Error flag set;F
                Maskable Interrupt (EI level);G
                Maskable Interrupt (FE level);H
                Internal reset generation;I
                Error output;J
                Error trigger;K
                Delay timer start;L
                Pseudo error generation;M
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

        ExcelReader reader = ExcelUtil.getReader(FileUtil.file(parentDirectoryPath, excelName), sheetName);
        int endRow = reader.getRowCount();
        // 清空resultPath下文件
        FileUtil.clean(resultPath);
        //便利products
        for (String key : productMap.keySet()) {
            Map<String, Object> paramMap = new HashMap<>();
            List<Category> categoryInfos = new ArrayList<>();
            List<ErrorSource> ErrorSourceInfos = new ArrayList<>();
            paramMap.put("CategoryInfos", categoryInfos);
            paramMap.put("ErrorSourceInfos", ErrorSourceInfos);
            String productCol = productMap.get(key);
            //遍历excel sheet数据
            for (int i = startRow; i <= endRow; i++) {
                //todo 判断合并的key计算 ArrayList equals
                if (StrUtil.isBlank(reader.getCell(errorSourceIdCol + i).getStringCellValue())) {
                    continue;
                }
                String productCondition = reader.getCell(productCol + i).getStringCellValue();
                if ("—".equals(productCondition) || "-".equals(productCondition)) {
                    continue;
                }
                //todo 合并标记
                String errorSourceId = reader.getCell(errorSourceIdCol + i).getStringCellValue();
                String categoryId = reader.getCell(categoryIdCol + i).getStringCellValue();
                String errorSourceNumber = reader.getCell(errorSourceNumberCol + i).getStringCellValue();
                String errorSourceenName = reader.getCell(errorSourceenNameCol + i).getStringCellValue();
                String errorSourcejpName = reader.getCell(errorSourcejpNameCol + i).getStringCellValue();
                List<Operation> function = new ArrayList<>();
                for (String funcId : operationMap.keySet()) {
                    String funcCol = operationMap.get(funcId);
                    String funcSupCondition = reader.getCell(funcCol + i).getStringCellValue();
                    //todo 存在 —*8 需要处理
                    boolean support = !("—".equals(funcSupCondition) || "-".equals(funcSupCondition));
                    String errorNote = "";
                    // * 处理
                    if (support) {
                        if (funcSupCondition.contains("*")) {
                            // todo
                        }
                    }
                    Operation operation = new Operation(funcId, String.valueOf(support), errorNote);
                    function.add(operation);
                }
                ErrorSource errorSource = new ErrorSource(errorSourceId, categoryId, errorSourceNumber, errorSourceenName, errorSourcejpName, function);
                ErrorSourceInfos.add(errorSource);
            }

            //todo 触发合并条件，中断
            File result = FileUtil.newFile(resultPath + "\\" + key + ".xml");
            FileUtil.appendUtf8String(FreemarkerUtil.getTemplateContent(configuration, paramMap, getFtlPath(sheetName)), result);
        }
    }

    private String getFtlPath(String sheetName) {
        if ("U2A".equals(sheetName)) {
            return "ecm/u2a.ftl";
        }
        return null;
    }

    record Category(String categoryId, String categoryEnName, String categoryJpName) {
    }

    record Operation(String funcId, String support, String errorNote) {
    }

    record ErrorSource(String errorSourceId, String categoryId, String errorSourceNumber, String errorSourceenName,
                       String errorSourcejpName, List<Operation> function) {
    }

}
