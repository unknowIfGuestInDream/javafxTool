/*
 * Copyright (c) 2024 unknowIfGuestInDream.
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
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.Test;

/**
 * @author unknowIfGuestInDream
 */
class CSTest {

    @Test
    void dom4jParse() throws DocumentException {
        SAXReader reader = new SAXReader();
        Document doc = reader.read(ResourceUtil.getResource("util/sample.mtpj").getPath());
        //System.out.println(doc.asXML());
    }

    @Test
    void parse() throws ParserConfigurationException, IOException, SAXException {
        // 创建一个DocumentBuilder
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        // 解析XML文件
        org.w3c.dom.Document document = builder.parse(new File(ResourceUtil.getResource("util/sample.mtpj").getPath()));

        // 获取根元素
        Element root = document.getDocumentElement();

        // 获取所有employee元素
        NodeList employeeList = root.getElementsByTagName("Class");

        NodeList cpu = root.getElementsByTagName("GeneralOptionCpu-DefaultValue");
        System.out.println(cpu.item(0).getTextContent());

        for (int i = 0; i < employeeList.getLength(); i++) {
            Element employee = (Element) employeeList.item(i);

            // 获取name元素的文本内容
            String name = employee.getElementsByTagName("Instance").item(0).getTextContent();

            System.out.println("Employee Name: " + name);
            System.out.println("---------------");
        }
    }
}
