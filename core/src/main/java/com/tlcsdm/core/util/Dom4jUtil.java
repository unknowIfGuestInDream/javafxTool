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

package com.tlcsdm.core.util;

import cn.hutool.log.StaticLog;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xml.sax.SAXException;

import java.io.*;
import java.net.URL;

/**
 * Dom4j工具类
 * 使用xpath需要引用jaxen
 *
 * @author: unknowIfGuestInDream
 * @date: 2023/4/22 18:34
 */
public class Dom4jUtil {

    private static final String DEFAULT_FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";

    /**
     * 通过url地址,获得document对象
     */
    public static Document read(URL url) {
        Document document = null;
        try {
            SAXReader saxReader = new SAXReader();
            saxReader.setFeature(DEFAULT_FEATURE, true);
            document = saxReader.read(url);
        } catch (DocumentException | SAXException e) {
            StaticLog.error(e);
        }
        return document;
    }

    /**
     * 通过path,获得document对象
     */
    public static Document read(String path) {
        Document document = null;
        try {
            SAXReader saxReader = new SAXReader();
            saxReader.setFeature(DEFAULT_FEATURE, true);
            document = saxReader.read(path);
        } catch (DocumentException | SAXException e) {
            StaticLog.error(e);
        }
        return document;
    }

    /**
     * 创建空的document
     */
    public static Document create() {
        return DocumentHelper.createDocument();
    }

    /**
     * 文本生成document
     */
    public static Document create(String contentXml) {
        Document document = null;
        try {
            document = DocumentHelper.parseText(contentXml);
        } catch (DocumentException e) {
            StaticLog.error(e);
        }
        return document;
    }

    /**
     * document写入指定文件
     */
    public static void doc2XmlFile(Document document, String filename) {
        try {
            OutputFormat format = defaultOutputFormat();
            XMLWriter writer = new XMLWriter(new FileOutputStream(filename), format);
            writer.write(document);
            writer.close();
        } catch (IOException e) {
            StaticLog.error(e);
        }
    }

    /**
     * 转换document对象为字符串
     */
    public static String transDocument2Text(Document document, String path) throws Exception {
        OutputFormat format = OutputFormat.createPrettyPrint();
        StringWriter stringWriter = new StringWriter();
        XMLWriter writer = new XMLWriter(stringWriter, format);
        writer.setOutputStream(new FileOutputStream(path));
        writer.write(document);
        writer.flush();
        return stringWriter.toString();
    }

    /**
     * 格式化，成功返回1
     */
    public int formatXmlFile(String filename) {
        int returnValue = 0;
        try {
            SAXReader saxReader = new SAXReader();
            saxReader.setFeature(DEFAULT_FEATURE, true);
            Document document = saxReader.read(new File(filename));
            XMLWriter writer;
            OutputFormat format = defaultOutputFormat();
            writer = new XMLWriter(new FileWriter(filename), format);
            writer.write(document);
            writer.close();
            returnValue = 1;
        } catch (IOException | DocumentException | SAXException e) {
            StaticLog.error(e);
        }
        return returnValue;
    }

    public static OutputFormat defaultOutputFormat() {
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setTrimText(false);
        return format;
    }
}
