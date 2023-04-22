package com.tlcsdm.core.util;

import cn.hutool.log.StaticLog;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

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

    /**
     * 通过url地址,获得document对象
     */
    public static Document read(URL url) {
        Document document = null;
        try {
            SAXReader saxReader = new SAXReader();
            document = saxReader.read(url);
        } catch (DocumentException e) {
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
            document = saxReader.read(path);
        } catch (DocumentException e) {
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
            OutputFormat format = OutputFormat.createPrettyPrint();
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
            Document document = saxReader.read(new File(filename));
            XMLWriter writer = null;
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("GBK");
            writer = new XMLWriter(new FileWriter(filename), format);
            writer.write(document);
            writer.close();
            returnValue = 1;
        } catch (IOException | DocumentException e) {
            StaticLog.error(e);
        }
        return returnValue;
    }
}
