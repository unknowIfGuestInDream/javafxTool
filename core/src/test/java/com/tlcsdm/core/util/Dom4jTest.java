package com.tlcsdm.core.util;

import cn.hutool.core.io.resource.ResourceUtil;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.util.List;

/**
 * @author: unknowIfGuestInDream
 * @date: 2023/4/22 19:57
 */
public class Dom4jTest {
    @Test
    public void method1() throws Exception {
        // 创建解析器
        SAXReader reader = new SAXReader();//这个是用来读取文件内容的
        Document doc = reader.read(ResourceUtil.getResource("util/dom4j/students.xml").getPath()); //指定要读取的文件
        System.out.println(doc.asXML()); //打印出文件
    }

    //实现对XML文件的复制
    @Test
    public void method2() throws Exception {
        // 得到Document
        SAXReader reader = new SAXReader();
        Document doc = reader.read(ResourceUtil.getResource("util/dom4j/students.xml").getPath());
        // 保存Document，指定将写入的目的文件（复制功能）
        Dom4jUtil.transDocument2Text(doc, ResourceUtil.getResource("util/dom4j/students_copy.xml").getPath());
    }

    //遍历Document
    @Test
    public void method3() throws Exception {
        // 要遍历文档，首先要得到Document对象
        SAXReader reader = new SAXReader();
        Document doc = reader.read(ResourceUtil.getResource("util/dom4j/students.xml").getPath());

        //获取根元素
        Element root = doc.getRootElement();
        //获取根元素中所有student元素
        List<Element> stuEleList = root.elements("student");
        // 循环遍历所有学生元素
        for (Element stuEle : stuEleList) {
            //获取学生元素的number
            String number = stuEle.attributeValue("number");
            //获取学生元素名为name的子元素的文本内容
            String name = stuEle.elementText("name");
            //获取学生元素名为age的子元素的文本内容
            String age = stuEle.elementText("age");
            //获取学生元素名为sex的子元素的文本内容
            String sex = stuEle.elementText("sex");
            System.out.println(number + ", " + name + ", " + age + ", " + sex);
        }
    }

    //添加元素
    @Test
    public void method4() throws Exception {
        // 得到Document
        SAXReader reader = new SAXReader();
        Document doc = reader.read(ResourceUtil.getResource("util/dom4j/students_copy.xml").getPath());

        //获取root元素
        Element root = doc.getRootElement();
        Element stuEle = root.addElement("student"); //添加了student元素
        // 给stuEle添加属性，名为number，值为1003
        stuEle.addAttribute("number", "1003");
        // 分别为stuEle添加名为name、age、sex的子元素，并为子元素设置文本内容
        stuEle.addElement("name").setText("wangWu");
        stuEle.addElement("age").setText("18");
        stuEle.addElement("sex").setText("male");

        // 设置保存的格式化器  1. \t，使用什么来完成缩进 2. true, 是否要添加换行
        OutputFormat format = new OutputFormat("\t", true);
        format.setTrimText(true); //去掉空白
        // 在创建Writer时，指定格式化器
        XMLWriter writer = new XMLWriter(new FileOutputStream(ResourceUtil.getResource("util/dom4j/students_copy.xml").getPath()), format);
        writer.write(doc);
    }

    //修改元素
    @Test
    public void method5() throws Exception {

        // 得到Document
        SAXReader reader = new SAXReader();
        Document doc = reader.read(ResourceUtil.getResource("util/dom4j/students_copy.xml").getPath());

        //使用XPath找到符合条件的元素
        // 查询student元素，条件是number属性的值为1003
        Element stuEle = (Element) doc.selectSingleNode("//student[@number='ITCAST_1003']");
        //修改stuEle的age子元素内容为81
        stuEle.element("age").setText("81");
        //修改stuEle的sex子元素的内容为female
        stuEle.element("sex").setText("female");

    }

    //删除元素
    @Test
    public void method6() throws Exception {

        // 得到Document
        SAXReader reader = new SAXReader();
        Document doc = reader.read(ResourceUtil.getResource("util/dom4j/students_copy.xml").getPath());

        // 查找student元素，条件是name子元素的文本内容为wangWu
        Element stuEle = (Element) doc.selectSingleNode("//student[name='wangWu']");

        // 2. 获取父元素，使用父元素删除指定子元素
        stuEle.getParent().remove(stuEle);
    }
}
