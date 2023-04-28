package com.tlcsdm.smc.tool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import cn.hutool.core.io.FileUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;

/**
 * DTS U2C trigger source xml生成
 */
@EnabledOnOs({ OS.WINDOWS, OS.MAC })
public class TriggerSourceDTSU2CXmlTest {
    // excel的父级目录路径
    private final String parentDirectoryPath = "C:\\workspace\\test";
    // excel文件名称
    private final String excelName = "u2c_DTS_Transfer_request_Table.xlsx";
    // sheet名
    private final String sheetName = "DTS trigger";
    // 数据开始行
    private final int beginRowNum = 5;
    // 数据结束行
    private final int endRowNum = 132;
    // List初始容量
    private final int initialCapacity = 647;
    // group0-3 value所在列
    private final String group0ValueLine = "C";
    private final String group1ValueLine = "E";
    private final String group2ValueLine = "G";
    private final String group3ValueLine = "I";
    private final String group4ValueLine = "K";

    // group0-3所在列
    private String group0Line;
    private String group1Line;
    private String group2Line;
    private String group3Line;
    private String group4Line;
    private String resultFileName;

    @BeforeEach
    public void init() {

    }

    @AfterEach
    public void result() {
        readData();
    }

    @Test
    public void DTSU2C8_B() {
        initGroupLine("L", "M", "N", "O", "P");
        resultFileName = "DTSC8BTriggerSource.xml";
    }

    @Test
    public void DTSU2C4_B() {
        initGroupLine("V", "W", "X", "Y", "Z");
        resultFileName = "DTSC4BTriggerSource.xml";
    }

    @Test
    public void DTSU2C2_B() {
        initGroupLine("AA", "AB", "AC", "AD", "AE");
        resultFileName = "DTSC2BTriggerSource.xml";
    }

    @Test
    public void DTSU2C8_D() {
        initGroupLine("AU", "AV", "AW", "AX", "AY");
        resultFileName = "DTSC8DTriggerSource.xml";
    }

    @Test
    public void DTSU2C4_D() {
        initGroupLine("BE", "BF", "BG", "BH", "BI");
        resultFileName = "DTSC4DTriggerSource.xml";
    }

    @Test
    public void DTSU2C2_D() {
        initGroupLine("BJ", "BK", "BL", "BM", "BN");
        resultFileName = "DTSC2DTriggerSource.xml";
    }

    // 读取数据
    private void readData() {
        ExcelReader reader = ExcelUtil.getReader(FileUtil.file(parentDirectoryPath, excelName), sheetName);
        File file = FileUtil.newFile(parentDirectoryPath + "\\triggerSource\\" + resultFileName);
        if (file.exists()) {
            FileUtil.del(file);
        }
        List<String> contentsList = new ArrayList<>(initialCapacity);
        contentsList.add("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        contentsList.add("<!DOCTYPE xml>");
        contentsList.add("<!-- this file was auto-generated. Do not modify it manually -->");
        contentsList.add("<DTCTriggerSource>");
        contentsList.add("	<Dependence Dependence=\"\" />");

        for (int i = beginRowNum; i <= endRowNum; i++) {
            contentsList.add("	<TriggerSource Channel=\"" + (i - beginRowNum) + "\"");
            contentsList.add("		Group0TriggerInfo=\""
                    + getXmlGroupValue(reader, getGroup0Line() + i, group0ValueLine + i) + "\"");
            contentsList.add("		Group1TriggerInfo=\""
                    + getXmlGroupValue(reader, getGroup1Line() + i, group1ValueLine + i) + "\"");
            contentsList.add("		Group2TriggerInfo=\""
                    + getXmlGroupValue(reader, getGroup2Line() + i, group2ValueLine + i) + "\"");
            contentsList.add("		Group3TriggerInfo=\""
                    + getXmlGroupValue(reader, getGroup3Line() + i, group3ValueLine + i) + "\"");
            contentsList.add("		Group4TriggerInfo=\""
                    + getXmlGroupValue(reader, getGroup4Line() + i, group4ValueLine + i) + "\" />");
        }

        contentsList.add("</DTCTriggerSource>");
        contentsList.add("");
        FileUtil.appendUtf8Lines(contentsList, file);
        reader.close();
    }

    // 获取生成到xml中的值
    private String getXmlGroupValue(ExcelReader reader, String group0LineCell, String group0ValueLineCell) {
        if ("-".equals(reader.getCell(group0LineCell).getStringCellValue())) {
            return "Reserved";
        }
        return reader.getCell(group0ValueLineCell).getStringCellValue();
    }

    // 初始化变量
    private void initGroupLine(String group0Line, String group1Line, String group2Line, String group3Line,
            String group4Line) {
        setGroup0Line(group0Line);
        setGroup1Line(group1Line);
        setGroup2Line(group2Line);
        setGroup3Line(group3Line);
        setGroup4Line(group4Line);
    }

    public String getGroup0Line() {
        return group0Line;
    }

    public void setGroup0Line(String group0Line) {
        this.group0Line = group0Line;
    }

    public String getGroup1Line() {
        return group1Line;
    }

    public void setGroup1Line(String group1Line) {
        this.group1Line = group1Line;
    }

    public String getGroup2Line() {
        return group2Line;
    }

    public void setGroup2Line(String group2Line) {
        this.group2Line = group2Line;
    }

    public String getGroup3Line() {
        return group3Line;
    }

    public void setGroup3Line(String group3Line) {
        this.group3Line = group3Line;
    }

    public String getGroup4Line() {
        return group4Line;
    }

    public void setGroup4Line(String group4Line) {
        this.group4Line = group4Line;
    }
}
