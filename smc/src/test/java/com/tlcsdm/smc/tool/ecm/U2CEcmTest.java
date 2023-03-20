package com.tlcsdm.smc.tool.ecm;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import com.tlcsdm.smc.model.ecm.Category;
import com.tlcsdm.smc.model.ecm.ECMInfos;

import cn.hutool.core.util.XmlUtil;

public class U2CEcmTest {

    @Test
    public void write() throws ParserConfigurationException {
        // 输入值获取
        String parentDirectoryPath = "C:\\workspace\\test\\ecm";
        String xmlName = "test.xml";
        ECMInfos ecmInfos = new ECMInfos();
        ecmInfos.getCategoryInfos().add(new Category("OperatingModes", "Operating Modes", "動作モード"));
        Document doc = XmlUtil.beanToXml(ecmInfos);
        doc.setXmlStandalone(true);

//        DocumentType doctype = ;
//        doc.appendChild(doctype);
//        <!DOCTYPE xml>
        XmlUtil.toFile(doc, parentDirectoryPath + "\\" + xmlName, "utf-8");
    }

    @Test
    public void dealData() {
        // 输入值获取
        String parentDirectoryPath = "C:\\workspace\\test\\ecm";
        String excelName = "ErrorSource.xlsx";
    }

}
