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

package com.tlcsdm.core.javafx.util;

import cn.hutool.core.io.FileUtil;
import com.tlcsdm.core.javafx.FxApp;
import com.tlcsdm.core.util.Dom4jUtil;
import javafx.stage.FileChooser;
import org.dom4j.Document;
import org.dom4j.Element;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户数据导出导入
 * <pre>{@code
 * Such as:
 * <javafxTool>
 *   <smc>
 *     <entry key=""></entry>
 *   </smc>
 * </javafxTool>
 *
 * }
 * </pre>
 *
 * @author unknowIfGuestInDream
 * @date 2022/12/11 19:03
 */
public class FxXmlHelper {

    private static final String ROOT_ELEMENT = "javafxTool";
    private static final String COMMON_ELEMENT = "common";
    private static final String ENTRY_ELEMENT = "entry";
    private static final String KEY_ATTRIBUTE = "key";

    private FxXmlHelper() {
    }

    /**
     * projectName
     */
    public static void exportData(String projectName) {
        exportData(projectName, null);
    }

    public static void exportData(String projectName, List<String> keys) {
        boolean includeCommon = false;
        if (keys == null || keys.isEmpty()) {
            keys = new ArrayList<>();
            keys.add(projectName);
            includeCommon = true;
        }
        FileChooser outputChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("jfxt", "*.jfxt");
        outputChooser.getExtensionFilters().add(extFilter);
        outputChooser.setInitialFileName(projectName);
        File output = outputChooser.showSaveDialog(FxApp.primaryStage);
        if (output != null) {
            if (output.exists()) {
                FileUtil.del(output);
            }
            Document document = Dom4jUtil.create();
            // document.addDocType(ROOT_ELEMENT, null,
            // "http://java.sun.com/dtd/properties.dtd");
            Element root = document.addElement(ROOT_ELEMENT);
            Element s = root.addElement(projectName);
            Map<String, Object> maps = new LinkedHashMap<>();
            keys.forEach(key -> maps.putAll(FxXmlUtil.getValues(key)));
            maps.forEach((k, v) -> {
                Element entry = s.addElement(ENTRY_ELEMENT);
                entry.addAttribute(KEY_ATTRIBUTE, k);
                entry.setText(v.toString());
            });
            if (includeCommon) {
                Element common = root.addElement(COMMON_ELEMENT);
                Map<String, Object> comMap = FxXmlUtil.getValues(COMMON_ELEMENT);
                comMap.forEach((k, v) -> {
                    Element entry = common.addElement(ENTRY_ELEMENT);
                    entry.addAttribute(KEY_ATTRIBUTE, k);
                    entry.setText(v.toString());
                });
            }
            Dom4jUtil.doc2XmlFile(document, output.getPath());
        }
    }

    public static boolean importData(String projectName) {
        FileChooser outputChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("jfxt", "*.jfxt");
        outputChooser.getExtensionFilters().add(extFilter);
        File output = outputChooser.showOpenDialog(FxApp.primaryStage);
        if (output != null) {
            Document document = Dom4jUtil.read(output.getPath());
            if (ROOT_ELEMENT.equals(document.getRootElement().getName())) {
                document.getRootElement().elements().forEach(element -> {
                    if (projectName.equals(element.getName()) || COMMON_ELEMENT.equals(element.getName())) {
                        element.elements().forEach(e -> FxXmlUtil.set(e.attribute(KEY_ATTRIBUTE).getValue(), e.getText()));
                    }
                });
                return true;
            }
        }
        return false;
    }

}
