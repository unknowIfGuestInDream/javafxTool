package com.tlcsdm.smc.tool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.tlcsdm.core.exception.UnExpectedResultException;
import com.tlcsdm.core.javafx.dialog.FxAlerts;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;

/**
 * 根据DMA triggersource 手册生成plugin setting&binding code 和 r_cg_dma.h相关代码
 */
public class DMATriggerSourceCode {

    @Test
    public void dealData() {
        // 输入值获取
        String parentDirectoryPath = "C:\\workspace\\test";
        List<String> groups = StrUtil.splitTrim("C,E", ",");
        String excelName = "u2c_sDMAC_Transfer_request_Table.xlsx";
        String outputPath = "C:\\workspace\\test";
        int groupNum = groups.size();
        String xmlFileNameAndStartCol = """
                RH850/U2C8-B#F
                RH850/U2C4-B#J
                RH850/U2C2-B#L
                RH850/U2C8-D#T
                RH850/U2C4-D#X
                RH850/U2C2-D#Z
                """;
        String sheetName = "sDMAC transfer request";
        int startRow = 5;
        int endRow = 260;
        String xmlNameTemplate = "";

        List<String> xmlFileNames = new ArrayList<>();
        List<String> startCols = new ArrayList<>();
        parseXmlConfig(xmlFileNameAndStartCol, xmlFileNames, startCols);

        String resultPath = outputPath + "\\dmaCode";
        // 清空resultPath下文件
        FileUtil.clean(resultPath);
        // 处理数据
        ExcelReader reader = ExcelUtil.getReader(FileUtil.file(parentDirectoryPath, excelName), sheetName);

        for (int i = 0; i < xmlFileNames.size(); i++) {
            File file = FileUtil.newFile(resultPath + "\\" + StrUtil.format(xmlNameTemplate, xmlFileNames.get(i)));
            if (file.exists()) {
                FileUtil.del(file);
            }
            List<String> contentsList = new ArrayList<>();
            contentsList.add("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            contentsList.add("<!DOCTYPE xml>");
            contentsList.add("<!-- this file was auto-generated. Do not modify it manually -->");
            contentsList.add("<DTCTriggerSource>");
            contentsList.add("    <Dependence Dependence=\"\" />");

            int startCol = ExcelUtil.colNameToIndex(startCols.get(i));
            for (int j = startRow; j <= endRow; j++) {
                contentsList.add("    <TriggerSource Channel=\"" + (j - startRow) + "\"");
                for (int k = 0; k < groupNum; k++) {
                    String getGroupLine = ExcelUtil.indexToColName(startCol + k);
                    String content = "        Group" + k + "TriggerInfo=\""
                            + getXmlGroupValue(reader, getGroupLine + j, groups.get(k) + j) + "\"";
                    if (k == groupNum - 1) {
                        content += " />";
                    }
                    contentsList.add(content);
                }
            }

            contentsList.add("</DTCTriggerSource>");
            contentsList.add("");
            FileUtil.appendUtf8Lines(contentsList, file);
        }

        reader.close();
    }

    /**
     * 解析 xmlFileNameAndStartCol 的配置，获取xml文件名和读取的列信息
     */
    private void parseXmlConfig(String xmlFileNameAndStartCol, List<String> xmlFileNames, List<String> startCols) {
        List<String> xmlConfigs = StrUtil.splitTrim(xmlFileNameAndStartCol, "\n");
        for (String xmlConfig : xmlConfigs) {
            List<String> l = StrUtil.split(xmlConfig, "-");
            xmlFileNames.add(l.get(0));
            startCols.add(l.get(1));
        }
        if (xmlFileNames.size() != startCols.size()) {
            FxAlerts.exception(new UnExpectedResultException());
        }
    }

    /**
     * 读取单元格的值
     */
    private String getXmlGroupValue(ExcelReader reader, String groupLineCell, String groupValueLineCell) {
        if ("-".equals(reader.getCell(groupLineCell).getStringCellValue())) {
            return "Reserved";
        }
        return reader.getCell(groupValueLineCell).getStringCellValue();
    }
}
