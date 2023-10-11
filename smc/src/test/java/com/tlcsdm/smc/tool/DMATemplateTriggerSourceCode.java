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

package com.tlcsdm.smc.tool;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.tlcsdm.core.util.CoreConstant;
import com.tlcsdm.core.util.FreemarkerUtil;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 根据DMA triggersource 手册生成plugin setting&binding code 和 r_cg_dma.h相关代码
 */
@DisabledIfSystemProperty(named = "workEnv", matches = "ci")
public class DMATemplateTriggerSourceCode {

    private static Configuration configuration;

    @BeforeAll
    public static void init() {
        configuration = new Configuration(Configuration.VERSION_2_3_32);
        try {
            configuration.setDirectoryForTemplateLoading(new File(ResourceUtil.getResource("templates").getPath()));
            configuration.setDefaultEncoding(CoreConstant.ENCODING_UTF_8);
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            configuration.setSetting(Configuration.CACHE_STORAGE_KEY, "strong:20,soft:250");
        } catch (IORuntimeException | IOException | TemplateException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void dealData() {
        // 输入值获取
        String parentDirectoryPath = "C:\\workspace\\test";
        List<String> groups = StrUtil.splitTrim("C,E", ",");
        String excelName = "u2c_sDMAC_Transfer_request_Table.xlsx";
        String outputPath = "C:\\workspace\\test";
        String resultPath = outputPath + "\\dmaCode";
        String xmlFileNameAndStartCol = """
            RH850U2C8;292;F
            RH850U2C4;292;J
            RH850U2C2;176;L
            RH850U2C8;176;T
            RH850U2C4;156;X
            RH850U2C2;144;Z
            """;
        String sheetName = "sDMAC transfer request";
        String macroTemplate = "_DMAC_GRP{groupNum}_REQUEST_{factor}";
        int startRow = 5;
        int endRow = 260;
        int offset = 4;
        int defineLength = 60;
        String offsetString = CharSequenceUtil.repeat(" ", offset);

        List<TransferRequest> transferRequests = new ArrayList<>();
        List<String> xmlConfigs = StrUtil.splitTrim(xmlFileNameAndStartCol, "\n");
        for (String xmlConfig : xmlConfigs) {
            List<String> l = StrUtil.split(xmlConfig, ";");
            TransferRequest transferRequest = new TransferRequest(l.get(0), l.get(1), l.get(2));
            transferRequests.add(transferRequest);
        }

        // 清空resultPath下文件
        FileUtil.clean(resultPath);
        // 处理数据
        ExcelReader reader = ExcelUtil.getReader(FileUtil.file(parentDirectoryPath, excelName), sheetName);

        // 文件内容获取
        List<Map<String, Object>> bindingContent = new ArrayList<>();
        List<Map<String, Object>> cgdmaContent = new ArrayList<>();
        List<Map<String, Object>> groupList = new ArrayList<>();

        Map<String, Object> map = new HashMap<>();
        map.put("offset", offsetString);
        map.put("groups", groupList);
        map.put("bindingContent", bindingContent);
        map.put("cgdmaContent", cgdmaContent);
        int groupNum = 0;
        for (String group : groups) {
            Map<String, Object> g = new HashMap<>();
            List<Map<String, Object>> settingContent = new ArrayList<>();
            g.put("groupNum", groupNum);
            g.put("settingContent", settingContent);

            String defaultSelection = "";
            // 模板赋值使用
            Map<String, String> paramMap = MapUtil.builder("offset", offsetString)
                .put("groupNum", String.valueOf(groupNum)).build();
            for (int i = startRow; i <= endRow; i++) {
                Map<String, Object> setting = new HashMap<>();
                Map<String, Object> cgdma = new HashMap<>();
                Map<String, Object> binding = new HashMap<>();
                String factor = reader.getCell(group + i).getStringCellValue();
                if ("Reserve".equals(factor)) {
                    continue;
                }
                if (defaultSelection.length() == 0) {
                    defaultSelection = factor;
                }
                paramMap.put("factor", factor);
                String macro = StrUtil.format(macroTemplate, paramMap);

                setting.put("factor", factor);

                binding.put("factor", factor);
                binding.put("groupNum", groupNum);
                binding.put("macro", macro);

                cgdma.put("factor", factor);
                cgdma.put("groupNum", groupNum);
                cgdma.put("macro", macro);
                cgdma.put("hex", "0x" + String.format("%08x", i - startRow).toUpperCase() + "UL");
                cgdma.put("offset", " ");
                if (macro.length() < defineLength - 8) {
                    cgdma.put("offset", CharSequenceUtil.repeat(" ", defineLength - macro.length() - 8));
                }
                cgdmaContent.add(cgdma);
                bindingContent.add(binding);
                settingContent.add(setting);
            }
            g.put("defaultSelection", defaultSelection);
            // 后置处理
            groupList.add(g);
            // 当前循环结束，开始下一次循环
            groupNum++;
        }

        File setting = FileUtil.newFile(resultPath + "\\setting.xml");
        FileUtil.appendUtf8String(FreemarkerUtil.getTemplateContent(configuration, map, "setting.ftl"), setting);
        File binding = FileUtil.newFile(resultPath + "\\binding.xml");
        FileUtil.appendUtf8String(FreemarkerUtil.getTemplateContent(configuration, map, "binding.ftl"), binding);
        File cgdma = FileUtil.newFile(resultPath + "\\r_cg_dma.h");
        FileUtil.appendUtf8String(FreemarkerUtil.getTemplateContent(configuration, map, "cgdma.ftl"), cgdma);

        reader.close();
    }

    record TransferRequest(String device, String pins, String startCol) {
    }
}
