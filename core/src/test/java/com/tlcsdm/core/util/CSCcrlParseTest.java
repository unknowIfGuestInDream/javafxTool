/*
 * Copyright (c) 2025 unknowIfGuestInDream.
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

package com.tlcsdm.core.util;

import cn.hutool.core.io.resource.ResourceUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;
import org.junit.jupiter.api.extension.ExtendWith;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 解析mtpj构建ccrl命令
 */
@ExtendWith(JunitMethodExecutionTimeExtension.class)
class CSCcrlParseTest {

    private static File project = null;
    private static String compilerPath = null;
    private static File compileFile = null;
    private static String projectName = null;
    private static String deviceFamily = null;
    private static String[] tagNames = null;

    @BeforeAll
    static void init() {
        project = new File(ResourceUtil.getResource("util/ccrl.mtpj").getFile());
        compilerPath = "C:\\Program Files (x86)\\Renesas Electronics\\CS+\\CC\\CC-RL\\V1.12.01\\bin";
        compileFile = new File(compilerPath);
        projectName = "ccrl_project";
        deviceFamily = "RL78";
        tagNames = new String[]{"COptionG-0", "COptionGLine-0", "GeneralOptionCpu-0",
            //Language standard specification
            "COptionLangC-0", "CppOptionLangOfCppSourceFile-0",
            //Preprocessor control
            "COptionD-0", "COptionU-0", "COptionI-0", "COptionPreinclude-0",
            //Optimization
            "COptionOsize-0", "COptionOunroll-0", "COptionOdeleteStaticFunc-0",
            "COptionOinlineLevel-0", "COptionOinlineSize-0", "COptionOpipeline-0", "COptionOtailCall-0", "COptionOintermodule-0",
            "COptionOaliasAnsi-0", "COptionOsameCode-0 ", "COptionObranchChaining-0", "COptionOalign-0", "COptionGoptimize-0",
            //Code generation changing
            "COptionDblSize-0", "COptionSignedChar-0", "COptionSignedBitfield-0", "COptionSwitch-0", "COptionVolatile-0",
            "COptionMergeString-0", "COptionPack-0", "COptionStuffBSS-0", "COptionStuffData-0", "COptionStuffConst-0",
            //Extensions
            "COptionLargeVariable-0",
            //Compiler transition support
            "COptionConvertCc-0", "COptionUnalignedPointerForCA78K0R-0",
            //rlink
            //Input control
            "LinkOptionDefine-0", "LinkOptionInput-0", "LinkOptionLibrary-0", "LinkOptionBinary-0", "LinkOptionEntry-0",
            "LinkOptionEntryPoint-0", "LibOptionAllowDuplicateModuleName-0",
            //Output control
            "HexOptionForm-0", "LinkOptionDebug-0", "LinkOptionRom-0", "LinkOptionMessage-0", "LinkOptionVect-0", "LinkOptionVectN-0",
            //List output
            "LinkOptionShowVector-0", "LinkOptionShowStruct-0", "LinkOptionShowRelocationAttribute-0", "LinkOptionShowCFI-0",
            "LinkOptionShowSymbol-0", "LinkOptionShowReference-0", "LinkOptionShowXreference-0", "LinkOptionShowTotalSize-0",
            //Optimization
            "LinkOptionOptimize-0", "LinkOptionOptimizeSymbolDelete-0", "LinkOptionOptimizeBranch-0",
            "LinkOptionSectionForbid-0", "LinkOptionAbsoluteForbid-0", "LinkOptionSymbolForbid-0",
            //Section specification
            "LinkOptionStart-0", "LinkOptionFSymbol-0", "LinkOptionUserOptByte-0", "LinkOptionUserOptByteValue-0", "LinkOptionOcdbg-0",
            "LinkOptionOcdbgValue-0", "LinkOptionSecurityOptByte-0", "LinkOptionSecurityOptByteValue-0", "LinkOptionSecurityId-0",
            "LinkOptionFlashSecurityId-0", "LinkOptionAutoSectionLayout-0", "LinkOptionSplitSection-0", "LinkOptionDspMemoryArea-0",
            "LinkOptionDebugMonitorSetting-0", "LinkOptionDebugMonitor-0", "LinkOptionOcdTr-0",
            //Other
            "LinkOptionCompress-0", "LinkOptionMemory-0", "LinkOptionTotalSize-0", "LinkOptionLogo-0",
            //dspasm
            "DSPAsmOptionOutputAsm-0", "DSPAsmOptionOutputVerilog-0", "DSPAsmOptionTextmacro-0", "DSPAsmOptionDefine-0",
            "DSPAsmOptionAllowTextMacroRedefine-0", "DSPAsmOptionCore-0", "DSPAsmOptionCodeSectionStart-0", "DSPAsmOptionDataSectionStart-0",
            "DSPAsmOptionLabel-0", "DSPAsmOptionMacroIdentifyExact-0"
        };
    }

    @Test
    @DisabledIfSystemProperty(named = "workEnv", matches = "ci")
    void parseXpath() {
        // 构造 XPath 查询语句（用 | 分隔）
        String xpathExpr = Arrays.stream(tagNames)
            .map(tag -> "//" + tag)
            .reduce((a, b) -> a + " | " + b)
            .orElse("");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document = null;
        try {
            // 加载 XML 文档
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(project);
            XPathFactory xpathfactory = XPathFactory.newInstance();
            // 关闭限制 #2229
            xpathfactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, false);
            // 执行 XPath 查询
            XPath xpath = xpathfactory.newXPath();
            NodeList nodeList = (NodeList) xpath.evaluate(xpathExpr, document, XPathConstants.NODESET);

            // 构造标签->值映射
            Map<String, String> result = new HashMap<>();

            // 把找到的节点填入映射
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                result.put(node.getNodeName(), node.getTextContent().trim());
            }

            // 输出结果
            for (String tag : tagNames) {
                String value = result.getOrDefault(tag, null);
                if (value != null) {
                    System.out.println(tag + " = " + value);
                } else {
                    System.out.println(tag + " 未找到");
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException |
                 XPathFactoryConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void parseDoc() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document = null;
        try {
            // 加载 XML 文档
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(project);

            // 结果保存为 Map：标签名 -> 内容（如果不存在则为 null 或 ""）
            Map<String, String> result = new LinkedHashMap<>();

            Element root = document.getDocumentElement();

            for (String tag : tagNames) {
                NodeList nodes = root.getElementsByTagName(tag);
                if (nodes != null && nodes.getLength() > 0) {
                    String value = nodes.item(0).getTextContent().trim();
                    result.put(tag, value);
                } else {
                    result.put(tag, null); // 或者 "" / "未找到"
                }
            }

            // 输出结果
            for (Map.Entry<String, String> entry : result.entrySet()) {
                System.out.println(entry.getKey() + " = " + (entry.getValue() != null ? entry.getValue() : "【未找到】"));
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
