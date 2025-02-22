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

package com.tlcsdm.smc.codeDev.ecm;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import com.tlcsdm.core.util.GroovyUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * U2X系列公共类.
 *
 * @author unknowIfGuestInDream
 * @since 1.0.3
 */
public abstract sealed class AbstractU2XFamilyScript extends AbstractEcmScript permits U2AEcmScript, U2CEcmScript {

    @Override
    protected Map<String, Object> dealErrorSourceData(ExcelReader reader, int rowNum, String product) {
        String errorSourceIdCol = errorSourceIdColField.getText();
        String categoryIdCol = categoryIdColField.getText();
        String errorSourceNumberCol = errorSourceNumberColField.getText();
        String errorSourceEnNameCol = errorSourceEnNameColField.getText();
        String errorSourceDescCol = errorSourceDescColField.getText();
        String errorSourceJpNameCol = errorSourceJpNameColField.getText();

        String functions = functionConfigField.getText();
        int optErrortIndex = 0;
        List<String> operationConfigs = StrUtil.splitTrim(functions, "\n");
        for (int i = 0; i < operationConfigs.size(); i++) {
            String operationConfig = operationConfigs.get(i);
            List<String> l = StrUtil.split(operationConfig, ";");
            if ("optErrort".equals(l.get(0))) {
                optErrortIndex = i;
            }
        }
        LinkedHashMap<String, String> operationMap = createOperationMap();
        LinkedHashMap<String, String> tagMap = createTagMap();

        String errorSourceId = reader.getCell(errorSourceIdCol + rowNum).getStringCellValue();
        String categoryId = reader.getCell(categoryIdCol + rowNum).getStringCellValue();
        int errorSourceNum = (int) reader.getCell(errorSourceNumberCol + rowNum).getNumericCellValue();
        String errorSourceNumber = String.valueOf(errorSourceNum);
        String errorSourceEnName = reader.getCell(errorSourceEnNameCol + rowNum).getStringCellValue();
        String errorSourceJpName = reader.getCell(errorSourceJpNameCol + rowNum).getStringCellValue();
        String errorSourceDesc = "";
        if (!errorSourceDescCol.isEmpty()) {
            errorSourceDesc = reader.getCell(errorSourceDescCol + rowNum).getStringCellValue();
        }
        // 特殊处理 24-29添加description信息
        if (errorSourceNum < 24 || errorSourceNum > 29) {
            errorSourceDesc = "";
        }
        List<Map<String, Object>> function = new ArrayList<>(operationMap.size());
        boolean optMaskintStatus = false;
        for (String funcId : operationMap.keySet()) {
            String funcCol = operationMap.get(funcId);
            String funcSupCondition = reader.getCell(funcCol + rowNum).getStringCellValue();
            // support 向下判断
            boolean support = !(funcSupCondition.contains("—") || funcSupCondition.contains("-"));
            if ("optMaskint".equals(funcId)) {
                optMaskintStatus = support;
            }
            Map<String, Object> operation = new HashMap<>(4);
            operation.put("funcId", funcId);
            operation.put("support", String.valueOf(support));
            operation.put("errorNote", "");
            // function数据后置处理
            handlerOperationSupport(operation, funcSupCondition, optMaskintStatus);
            function.add(operation);
        }
        List<Map<String, Object>> tag = buildTagData(tagMap, reader, rowNum);
        Map<String, Object> errorSource = new HashMap<>(16);
        errorSource.put("errorSourceId", errorSourceId);
        errorSource.put("categoryId", categoryId);
        errorSource.put("errorSourceNumber", errorSourceNumber);
        errorSource.put("errorSourceEnName", errorSourceEnName);
        errorSource.put("errorSourceJpName", errorSourceJpName);
        errorSource.put("errorSourceDesc", errorSourceDesc.replaceAll("\n", " "));
        errorSource.put("function", function);
        errorSource.put("tag", tag);
        handlerErrorSourceMap(errorSource, product, optErrortIndex);

        return errorSource;
    }

    /**
     * errorSource 数据后续处理
     */
    protected void handlerErrorSourceMap(Map<String, Object> errorSource, String product, int optErrortIndex) {
        if(product.startsWith("RH850U2C2") && errorSource.get("categoryId").equals("OSTM")){
            return;
        }
        GroovyUtil.invokeMethod(getGroovyPath(), "handlerErrorSourceMap", errorSource, product, optErrortIndex);
    }

    /**
     * function数据后置处理
     */
    protected void handlerOperationSupport(Map<String, Object> operation, String funcSupCondition,
                                           boolean optMaskintStatus) {
        GroovyUtil.invokeMethod(getGroovyPath(), "handlerOperationSupport", operation, funcSupCondition,
            optMaskintStatus);
    }
}
