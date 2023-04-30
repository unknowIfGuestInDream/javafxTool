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

package com.tlcsdm.smc.tool;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.cell.CellLocation;
import cn.hutool.poi.excel.cell.CellUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * pin数据整理
 */
@DisabledIfEnvironmentVariable(named = "ENV", matches = "workflow", disabledReason = "Not support in github action")
public class PinAssignment {

    // excel的父级目录路径
    private final String parentDirectoryPath = "C:\\workspace\\test";
    // excel文件名称
    private final String excelName = "E02_01_List_of_Pin_Assignment.xlsx";

    private static int startX;
    private static int startY;
    private static int endX;
    private static int endY;

    private static int pinStartX;
    private static int pinStartY;
    private static int pinEndX;
    private static int pinEndY;

    private static final int deviceNum = 11;

    private static final List<String> INPUT_SOURCE_LIST = List.of("TAUD0I0", "TAUD0I1", "TAUD0I2", "TAUD0I3", "TAUD0I4",
            "TAUD0I5", "TAUD0I6", "TAUD0I7", "TAUD0I8", "TAUD0I9", "TAUD0I10", "TAUD0I11", "TAUD0I12", "TAUD0I13",
            "TAUD0I14", "TAUD0I15", "TAUD1I0", "TAUD1I1", "TAUD1I2", "TAUD1I3", "TAUD1I4", "TAUD1I5", "TAUD1I6",
            "TAUD1I7", "TAUD1I8", "TAUD1I9", "TAUD1I10", "TAUD1I11", "TAUD1I12", "TAUD1I13", "TAUD1I14", "TAUD1I15",
            "TAUD2I0", "TAUD2I1", "TAUD2I2", "TAUD2I3", "TAUD2I4", "TAUD2I5", "TAUD2I6", "TAUD2I7", "TAUD2I8",
            "TAUD2I9", "TAUD2I10", "TAUD2I11", "TAUD2I12", "TAUD2I13", "TAUD2I14", "TAUD2I15", "TAUJ0I0", "TAUJ0I1",
            "TAUJ0I2", "TAUJ0I3", "TAUJ1I0", "TAUJ1I1", "TAUJ1I2", "TAUJ1I3", "TAUJ2I0", "TAUJ2I1", "TAUJ2I2",
            "TAUJ2I3", "TAUJ3I0", "TAUJ3I1", "TAUJ3I2", "TAUJ3I3");

    String inStartLine = "B";

    private static final List<String> OUTPUT_SOURCE_LIST = List.of("TAUD0O0", "TAUD0O1", "TAUD0O2", "TAUD0O3",
            "TAUD0O4", "TAUD0O5", "TAUD0O6", "TAUD0O7", "TAUD0O8", "TAUD0O9", "TAUD0O10", "TAUD0O11", "TAUD0O12",
            "TAUD0O13", "TAUD0O14", "TAUD0O15", "TAUD1O0", "TAUD1O1", "TAUD1O2", "TAUD1O3", "TAUD1O4", "TAUD1O5",
            "TAUD1O6", "TAUD1O7", "TAUD1O8", "TAUD1O9", "TAUD1O10", "TAUD1O11", "TAUD1O12", "TAUD1O13", "TAUD1O14",
            "TAUD1O15", "TAUD2O0", "TAUD2O1", "TAUD2O2", "TAUD2O3", "TAUD2O4", "TAUD2O5", "TAUD2O6", "TAUD2O7",
            "TAUD2O8", "TAUD2O9", "TAUD2O10", "TAUD2O11", "TAUD2O12", "TAUD2O13", "TAUD2O14", "TAUD2O15", "TAUJ0O0",
            "TAUJ0O1", "TAUJ0O2", "TAUJ0O3", "TAUJ1O0", "TAUJ1O1", "TAUJ1O2", "TAUJ1O3", "TAUJ2O0", "TAUJ2O1",
            "TAUJ2O2", "TAUJ2O3", "TAUJ3O0", "TAUJ3O1", "TAUJ3O2", "TAUJ3O3");
    String outStartLine = "N";

    @BeforeAll
    public static void init() {
        CellLocation cellLocation = ExcelUtil.toLocation("AR6");
        startX = cellLocation.getX();
        startY = cellLocation.getY();
        CellLocation cellLocation1 = ExcelUtil.toLocation("BW773");
        endX = cellLocation1.getX();
        endY = cellLocation1.getY();

        CellLocation cellLocation2 = ExcelUtil.toLocation("T6");
        pinStartX = cellLocation2.getX();
        pinStartY = cellLocation2.getY();
        CellLocation cellLocation3 = ExcelUtil.toLocation("AD773");
        pinEndX = cellLocation3.getX();
        pinEndY = cellLocation3.getY();
    }

    @Test
    public void deal() {
        File file = FileUtil.file(parentDirectoryPath, excelName);
        ExcelReader reader = ExcelUtil.getReader(file, "data");

        List<List<String>> putList = new ArrayList<>();
        for (int i = startY; i <= endY; i++) {
            List<String> l = new ArrayList<>();
            for (int j = startX; j <= endX; j++) {
                l.add(valueOf(CellUtil.getCellValue(reader.getCell(j, i))));
            }
            putList.add(l);
        }

        List<List<String>> pinList = new ArrayList<>();
        for (int i = pinStartY; i <= pinEndY; i++) {
            List<String> l = new ArrayList<>();
            for (int j = pinStartX; j <= pinEndX; j++) {
                l.add(valueOf(CellUtil.getCellValue(reader.getCell(j, i))));
            }
            pinList.add(l);
        }
        reader.close();

        ExcelWriter excelWriter = ExcelUtil.getWriter(file, "result");
        for (int i = 0; i < putList.size(); i++) {
            List<String> l = putList.get(i);
            for (int j = 0; j < l.size(); j++) {
                String val = l.get(j);
                if (StrUtil.isEmpty(val)) {
                    continue;
                }
                for (int k = 0; k < INPUT_SOURCE_LIST.size(); k++) {
                    if (val.equals(INPUT_SOURCE_LIST.get(k))) {
                        List<String> p = pinList.get(i);
                        for (int m = 0; m < p.size(); m++) {
                            if (StrUtil.isNotEmpty(p.get(m))) {
                                excelWriter.getCell(1 + m, 2 + k).setCellValue(p.get(m));
                                continue;
                            }
                        }
                    }
                }

                for (int k = 0; k < OUTPUT_SOURCE_LIST.size(); k++) {
                    if (val.equals(OUTPUT_SOURCE_LIST.get(k))) {
                        List<String> p = pinList.get(i);
                        for (int m = 0; m < p.size(); m++) {
                            if (StrUtil.isNotEmpty(p.get(m))) {
                                excelWriter.getCell(13 + m, 2 + k).setCellValue(p.get(m));
                                continue;
                            }
                        }
                    }
                }

            }
        }
        excelWriter.flush();
        excelWriter.close();
    }

    private static String valueOf(Object obj) {
        return (obj == null) ? "" : obj.toString();
    }

}
