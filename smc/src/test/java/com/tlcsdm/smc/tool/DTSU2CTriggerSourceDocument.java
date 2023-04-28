package com.tlcsdm.smc.tool;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import cn.hutool.core.io.FileUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.cell.CellLocation;

@EnabledOnOs({ OS.WINDOWS, OS.MAC })
public class DTSU2CTriggerSourceDocument {
    // excel的父级目录路径
    private final String parentDirectoryPath = "C:\\workspace\\test";
    // excel文件名称
    private final String excelName = "u2c_DTS_Transfer_request_Table.xlsx";
    // 模板文件名
    private String resultFileName = "DTS_RH850U2C_request_table.xlsx";

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

    private final String number = "2-02-001-";
    private final String control = "ComboBox";
    private final String labelEn = "Trigger resource";
    private final String labelJa = "起動要因";
    private final String condition = "Always enable";
    private final String conditionCol = "CL";
    // 数据开始写入行
    private final int beginWriteRowNum = 3;
    private int startConditionX;
    // 数据开始写入行
    private final List<ArrayList<String>> groupLines = new ArrayList<>();

    // trigger factor信息
    private List<Map<Integer, String>> triggerFactorList = new ArrayList<>(128);
    private List<Integer> triggerFactorRowNumList = new ArrayList<>(128);
    private List<List<Map<Integer, String>>> conditionList = new ArrayList<>(128);

    @BeforeEach
    public void init() {
        initGroupLine("Q", "R", "S", "T", "U"); // U2C8292-Q
        initGroupLine("V", "W", "X", "Y", "Z"); // U2C8176-V
        initGroupLine("AK", "AL", "AM", "AN", "AO"); // U2C4292-AK
        initGroupLine("AP", "AQ", "AR", "AS", "AT"); // U2C4176-AP
        initGroupLine("AU", "AV", "AW", "AX", "AY"); // U2C4156-AU
        initGroupLine("AZ", "BA", "BB", "BC", "BD"); // U2C4144-AZ
        initGroupLine("BE", "BF", "BG", "BH", "BI"); // U2C4144Bosch-BE
        initGroupLine("BJ", "BK", "BL", "BM", "BN"); // U2C4100-BJ
        initGroupLine("BO", "BP", "BQ", "BR", "BS"); // U2C2176-BO
        initGroupLine("BT", "BU", "BV", "BW", "BX"); // U2C2156-BT
        initGroupLine("BY", "BZ", "CA", "CB", "CC"); // U2C2144-BY
        initGroupLine("CD", "CE", "CF", "CG", "CH"); // U2C2144Bosch-CD
        initGroupLine("CI", "CJ", "CK", "CL", "CM"); // U2C2100-CI
        CellLocation cellLocation = ExcelUtil.toLocation(conditionCol + beginWriteRowNum);
        startConditionX = cellLocation.getX();
    }

    @AfterAll
    public static void afterAll() {

    }

    @Test
    public void dealData() {
        readData();
        BigExcelWriter excelWriter = ExcelUtil.getBigWriter(FileUtil.file(parentDirectoryPath + "\\" + resultFileName),
                sheetName);
        excelWriter.getStyleSet().setAlign(HorizontalAlignment.LEFT, VerticalAlignment.CENTER);
        int line = beginWriteRowNum;
        for (int i = 0; i < 128; i++) {
            // int rowNum = triggerFactorRowNumList.get(i);
            int rowNum = 5;
            int regnum = i / 8;
            int n = i % 8;
            int group = 0;
            for (int j = 0; j < rowNum; j++) {
                Map<Integer, String> map = triggerFactorList.get(i);
//					for (int k = 0; k < 4; k++) {
//						if (!map.containsKey(group)) {
//							group++;
//						} else {
//							break;
//						}
//					}
                int initx = group;
                String initValue = "";
                for (int k = 0; k < 5; k++) {
                    if ("Reserved".equals(map.get(initx))) {
                        initx++;
                    } else {
                        initValue = map.get(initx);
                        break;
                    }
                }

                if (j == 0) {
                    excelWriter.writeCellValue("B" + line, number + String.format("%03d", i));
                    excelWriter.writeCellValue("C" + line, control);
                    excelWriter.writeCellValue("D" + line, rowNum);
                    excelWriter.writeCellValue("E" + line, labelEn);
                    excelWriter.writeCellValue("F" + line, labelJa);
                    excelWriter.writeCellValue("G" + line, condition);
                    excelWriter.writeCellValue("S" + line, "Group " + initx + " : " + initValue);
                    excelWriter.writeCellValue("T" + line, condition);
                }
//				excelWriter.writeCellValue("C" + (line + j), "Group " + group + " : " + map.get(group));
                excelWriter.writeCellValue("Q" + (line + j), "Group " + group + " : " + map.get(group));
                excelWriter.writeCellValue("R" + (line + j), "Group " + group + " : " + map.get(group));
                excelWriter.writeCellValue("BJ" + (line + j), "DMATRGSEL.DTSSEL" + regnum + ".UINT32 &=");
                excelWriter.writeCellValue("BK" + (line + j), "Config.c");
                excelWriter.writeCellValue("BL" + (line + j), "R_Config_DTS%s_Create");
                excelWriter.writeCellValue("BM" + (line + j), "_DTSn" + n + "_TRANSFER_REQUEST_GROUP_CLEAR");
                excelWriter.writeCellValue("BN" + (line + j), "DMATRGSEL.DTSSEL" + regnum + ".UINT32 |=");
                excelWriter.writeCellValue("BO" + (line + j), "Config.c");
                excelWriter.writeCellValue("BP" + (line + j), "R_Config_DTS%s_Create");
                excelWriter.writeCellValue("BQ" + (line + j), "_DTSn" + n + "_TRANSFER_REQUEST_GROUP_" + group);

                int x = startConditionX;
                for (int k = 0; k < conditionList.size(); k++) {
                    List<Map<Integer, String>> list = conditionList.get(k);
                    for (int l = 0; l < list.size(); l++) {

                        if (l == i) {
                            excelWriter.writeCellValue(x, (line + j - 1), list.get(l).get(group));
                        } else {
                            excelWriter.writeCellValue(x, (line + j - 1), "-");
                        }
                        x++;
                    }
                }
                group++;
            }

            line += rowNum;
        }
        if (FileUtil.exist(parentDirectoryPath + "\\triggerSource\\" + resultFileName)) {
            FileUtil.del(parentDirectoryPath + "\\triggerSource\\" + resultFileName);
        }
        File file = FileUtil.newFile(parentDirectoryPath + "\\triggerSource\\" + resultFileName);
        excelWriter.flush(file);
        excelWriter.close();
    }

    // 读取数据
    private void readData() {
        ExcelReader reader = ExcelUtil.getReader(FileUtil.file(parentDirectoryPath, excelName), sheetName);
        for (int i = beginRowNum; i <= endRowNum; i++) {
            Map<Integer, String> map = new HashMap<>(8);
            int rowNum = 5;
            String group0Value = reader.getCell(group0ValueLine + i).getStringCellValue();
            String group1Value = reader.getCell(group1ValueLine + i).getStringCellValue();
            String group2Value = reader.getCell(group2ValueLine + i).getStringCellValue();
            String group3Value = reader.getCell(group3ValueLine + i).getStringCellValue();
            String group4Value = reader.getCell(group4ValueLine + i).getStringCellValue();
//				if ("Reserved".equals(group0Value)) {
//					rowNum--;
//				} else {
//					map.put(0, group0Value);
//				}
//				if ("Reserved".equals(group1Value)) {
//					rowNum--;
//				} else {
//					map.put(1, group1Value);
//				}
//				if ("Reserved".equals(group2Value)) {
//					rowNum--;
//				} else {
//					map.put(2, group2Value);
//				}
//				if ("Reserved".equals(group3Value)) {
//					rowNum--;
//				} else {
//					map.put(3, group3Value);
//				}
            map.put(0, group0Value);
            map.put(1, group1Value);
            map.put(2, group2Value);
            map.put(3, group3Value);
            map.put(4, group4Value);
            triggerFactorRowNumList.add(rowNum);
            triggerFactorList.add(map);
        }

        for (ArrayList<String> arrayList : groupLines) {
            List<Map<Integer, String>> list = new ArrayList<>();
            for (int i = beginRowNum; i <= endRowNum; i++) {
                Map<Integer, String> map = new HashMap<>(16);
                map.put(0, reader.getCell(arrayList.get(0) + i).getStringCellValue());
                map.put(1, reader.getCell(arrayList.get(1) + i).getStringCellValue());
                map.put(2, reader.getCell(arrayList.get(2) + i).getStringCellValue());
                map.put(3, reader.getCell(arrayList.get(3) + i).getStringCellValue());
                map.put(4, reader.getCell(arrayList.get(4) + i).getStringCellValue());
                list.add(map);
            }
            conditionList.add(list);
        }

        reader.close();
    }

    // 初始化变量
    private void initGroupLine(String group0Line, String group1Line, String group2Line, String group3Line,
            String group4Line) {
        List<String> list = new ArrayList<>();
        list.add(group0Line);
        list.add(group1Line);
        list.add(group2Line);
        list.add(group3Line);
        list.add(group4Line);
        groupLines.add((ArrayList<String>) list);
    }
}
