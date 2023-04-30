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
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 根据DMA triggersource 手册生成plugin setting&binding code 和 r_cg_dma.h相关代码
 */
@DisabledIfEnvironmentVariable(named = "GITHUB_ACTIONS", matches = "true", disabledReason = "Not support in github action")
public class DMATriggerSourceCode {

    @Test
    public void dealData() {
        // 输入值获取
        String parentDirectoryPath = "C:\\workspace\\test";
        List<String> groups = StrUtil.splitTrim("C,E", ",");
        String excelName = "u2c_sDMAC_Transfer_request_Table.xlsx";
        String outputPath = "C:\\workspace\\test";
        String resultPath = outputPath + "\\dmaCode";
        String xmlFileNameAndStartCol = """
                RH850U2C8-B;F
                RH850U2C4-B;J
                RH850U2C2-B;L
                RH850U2C8-D;T
                RH850U2C4-D;X
                RH850U2C2-D;Z
                """;
        String sheetName = "sDMAC transfer request";
        String macroTemplate = "_DMAC_GRP{groupNum}_REQUEST_{factor}";
        String tagTemplate = """
                {offset}<tagBinding id="Trigger{factor}" key="Trigger_Source" value="{macro}">
                {offset}    <and>
                {offset}        <simpleCondition optionId="requestSource" valueId="HWRequestGrp{groupNum}">
                {offset}        </simpleCondition>
                {offset}        <simpleCondition optionId="triggerSourceGrp{groupNum}" valueId="{factor}">
                {offset}        </simpleCondition>
                {offset}    </and>
                {offset}</tagBinding>""";
        int startRow = 5;
        int endRow = 260;
        int offset = 4;
        int defineLength = 70;
        String offsetString = CharSequenceUtil.repeat(" ", offset);
        String macroValueTemplate = "({hex}) /* DMAC group {groupNum} {factor} */";

        List<TransferRequest> transferRequests = new ArrayList<>();
        List<String> xmlConfigs = StrUtil.splitTrim(xmlFileNameAndStartCol, "\n");
        for (String xmlConfig : xmlConfigs) {
            List<String> l = StrUtil.split(xmlConfig, ";");
            TransferRequest transferRequest = new TransferRequest(l.get(0), l.get(1));
            transferRequests.add(transferRequest);
        }

        // 清空resultPath下文件
        FileUtil.clean(resultPath);
        // 处理数据
        ExcelReader reader = ExcelUtil.getReader(FileUtil.file(parentDirectoryPath, excelName), sheetName);

        // 文件内容获取
        List<String> settingContent = new ArrayList<>();
        List<String> bindingContent = new ArrayList<>();
        List<String> cgdmaContent = new ArrayList<>();
        int groupNum = 0;
        for (String group : groups) {
            List<String> triggerContent = new ArrayList<>();
            List<String> tagContent = new ArrayList<>();
            List<String> defineContent = new ArrayList<>();
            String defaultSelection = "";
            Map<String, String> paramMap = MapUtil.builder("offset", offsetString)
                    .put("groupNum", String.valueOf(groupNum)).build();
            for (int i = startRow; i <= endRow; i++) {
                String factor = reader.getCell(group + i).getStringCellValue();
                if ("Reserve".equals(factor)) {
                    continue;
                }
                if (defaultSelection.length() == 0) {
                    defaultSelection = factor;
                }
                paramMap.put("factor", factor);
                // setting
                String staticItem = StrUtil.format("""
                        {offset}    <staticItem enabled="true" id="{factor}" name="{factor}"/>""", paramMap);
                triggerContent.add(staticItem);

                String macro = StrUtil.format(macroTemplate, paramMap);
                paramMap.put("macro", macro);
                // bingding
                String tag = StrUtil.format(tagTemplate, paramMap);
                tagContent.add(tag);
                // r_cg_dma
                StringBuilder define = new StringBuilder("#define " + macro);
                paramMap.put("hex", "0x" + String.format("%08x", i - startRow).toUpperCase() + "UL");
                if (macro.length() < defineLength - 8) {
                    define.append(CharSequenceUtil.repeat(" ", defineLength - macro.length() - 8));
                    define.append(StrUtil.format(macroValueTemplate, paramMap));
                }
                defineContent.add(define.toString());
            }
            // 后置处理
            triggerContent.add(0, StrUtil.format(
                    """
                            {offset}<option defaultSelection="{defaultSelection}" enabled="true" id="triggerSourceGrp{groupNum}" name="triggerSourceGrp{groupNum}">""",
                    MapUtil.builder(paramMap).put("defaultSelection", defaultSelection).build()));
            triggerContent.add(offsetString + "</option>");
            // 当前循环结束，开始下一次循环
            settingContent.addAll(triggerContent);
            bindingContent.addAll(tagContent);
            cgdmaContent.addAll(defineContent);
            groupNum++;
        }
        File setting = FileUtil.newFile(resultPath + "\\setting.xml");
        FileUtil.appendUtf8Lines(settingContent, setting);
        File binding = FileUtil.newFile(resultPath + "\\binding.xml");
        FileUtil.appendUtf8Lines(bindingContent, binding);
        File cgdma = FileUtil.newFile(resultPath + "\\r_cg_dma.h");
        FileUtil.appendUtf8Lines(cgdmaContent, cgdma);

        reader.close();
    }

    record TransferRequest(String device, String startCol) {
    }
}
