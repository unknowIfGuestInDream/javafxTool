/*
 * Copyright (c) 2019, 2023 unknowIfGuestInDream
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

import cn.hutool.core.lang.Console;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.cell.CellLocation;
import cn.hutool.poi.excel.cell.CellUtil;
import com.tlcsdm.core.util.DiffHandleUtil;
import com.tlcsdm.core.wrap.hutool.FileUtil;
import com.tlcsdm.core.wrap.hutool.StrUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * API_HeaderComment 测试，作业停止，暂不考虑代码实现。
 *
 * @date 2022/8/24 17:25
 * @since: 1.0
 */
@Disabled
public class APIHeaderCommentTest {
    /*
     * 需要定义的配置
     */
    // 忽略的sheet
    private final List<String> ignoreSheetNames = List.of("Overview", "Summary");
    // 如果markSheetNames不为空，那么只会读取当前对象内的sheet, 忽略ignoreSheetNames配置
    private final List<String> markSheetNames = List.of("DMA");
    // excel的父级目录路径
    private final String parentDirectoryPath = "C:\\workspace\\test";
    // excel文件名称
    private final String excelName = "TestSpec_API_HeaderComment_RH850F1KH-D8.xlsx";
    // excel中待读取内容的左上角Cell
    private final String startCell = "C19";
    // excel中待读取内容的右下角Cell列名, endCell = endCellColumn + 计算获取的最后一行行号 - 2
    private final String endCellColumn = "F";
    // excel中Generate Files值对应的Cell
    private final String generateFileCell = "C15";
    // 待比对文件的父级路径, 如果为空，则默认为parentDirectoryPath值
    private final String generateFilesParentPath = "C:\\runtime-smc.rh850.product\\src\\smc_gen\\general";
    // 初始化清理resultPath路径
    private final boolean initClean = true;

    /*
     * 待初始化对象
     */
    // 需要读取的sheetName集合
    private List<String> sheetNames;
    // 生成结果目录路径
    private String resultPath;
    // 生成结果文件目录路径
    private String filesPath;

    @BeforeEach
    public void init() {
        ExcelReader reader = ExcelUtil.getReader(FileUtil.file(parentDirectoryPath, excelName));
        sheetNames = reader.getSheetNames().stream()
            .filter(s -> (markSheetNames.size() == 0 && !ignoreSheetNames.contains(s))
                || (markSheetNames.size() != 0 && markSheetNames.contains(s)))
            .collect(Collectors.toList());
//		resultPath = parentDirectoryPath + "\\" + excelName.substring(0, excelName.lastIndexOf("."));
//		filesPath = resultPath + "\\files";
//		cleanDir(resultPath);
    }

    @Test
    public void APIHeaderCommentTest1() {
        File testFile = FileUtil.file(parentDirectoryPath, excelName);
        for (String sheetName : sheetNames) {
            ExcelReader reader = ExcelUtil.getReader(testFile, sheetName);
            Cell cell = reader.getCell("B15");
            CellStyle cellStyle = cell.getCellStyle();
            Color color = cellStyle.getFillBackgroundColorColor();
            if (color != null) {

            }
        }
    }

    /**
     * 需要读取的excel部分单独放到一个sheet,然后转换文本，运用其他工具进行比对 考虑手动生成测试文件： 遍历将区域内容读取生成到文件中
     */
    @Test
    @Disabled
    public void generalTest1() {
        File testFile = FileUtil.file(parentDirectoryPath, excelName);
        Map<String, String> generateFileMap = new HashMap<>();
        for (String sheetName : sheetNames) {
            BigExcelWriter excelWriter = ExcelUtil.getBigWriter();
            logHandler("========================= Begin Reading " + sheetName + " =========================", 1);
            logHandler("Start reading sheet: " + sheetName, 1);
            ExcelReader reader = ExcelUtil.getReader(testFile, sheetName);
            String endCell = getEndCell(reader);
            logHandler("endCell: " + endCell, 1);
            CellLocation start = ExcelUtil.toLocation(startCell);
            CellLocation end = ExcelUtil.toLocation(endCell);
            int startX = start.getX();
            int startY = start.getY();
            int endX = end.getX();
            int endY = end.getY();
            String generateFileName = reader.getCell(generateFileCell).getStringCellValue();
            generateFileMap.put(sheetName, generateFileName);
            excelWriter.renameSheet(0, sheetName);
            List<List<String>> list = new ArrayList<>(endY - startY + 1);
            for (int j = startY; j <= endY; j++) {
                List<String> l = new ArrayList<>(endX - startX + 1);
                boolean isDefine = false;
                String firstValue = valueOf(CellUtil.getCellValue(reader.getCell(startX, j)));
                if ("#define".equals(StrUtil.trim(firstValue)) || "#ifndef".equals(StrUtil.trim(firstValue))) {
                    isDefine = true;
                }
                for (int j2 = startX; j2 <= endX; j2++) {
                    String cellValue = valueOf(CellUtil.getCellValue(reader.getCell(j2, j)));
                    if (isDefine && j2 < endX) {
                        cellValue = StrUtil.trimEnd(cellValue) + " ";
                    }
                    l.add(cellValue);
                }
                list.add(l);
            }
            excelWriter.write(list, false);
            File file = FileUtil.file(filesPath, sheetName + ".xlsx");
            excelWriter.flush(file);
            excelWriter.close();
            logHandler("========================= End Reading " + sheetName + " =========================", 1);
        }

        for (String sheetName : sheetNames) {
            ExcelReader reader = ExcelUtil.getReader(FileUtil.file(filesPath, sheetName + ".xlsx"), sheetName);
            String generateFileName = generateFileMap.get(sheetName);
            FileUtil.writeUtf8String(reader.readAsText(false).replaceAll("\\t", ""),
                FileUtil.file(filesPath, generateFileName));
            reader.close();
            logHandler("========================= Begin Comparing " + generateFileName + " =========================",
                1);
            String generateFileParent = getGenerateFileParent();
            File generateFile = FileUtil.file(generateFileParent, generateFileName);
            if (FileUtil.exist(generateFile)) {
                List<String> diffString = DiffHandleUtil.diffString(filesPath + "\\" + generateFileName,
                    generateFileParent + "\\" + generateFileName);
                DiffHandleUtil.generateDiffHtml(diffString, resultPath + "\\" + sheetName + ".html");
            }
            ThreadUtil.safeSleep(200);
            logHandler("========================= End Comparing " + generateFileName + " =========================", 1);
        }
        logHandler("============================= End =============================", 1);
    }

    /**
     * 清空文件夹
     */
    private void cleanDir(String dirPath) {
        if (initClean) {
            FileUtil.clean(dirPath);
        }
    }

    /**
     * 日志处理 level: 0 debug 1 log 2 warning 3 error
     */
    private void logHandler(String message, int level) {
        switch (level) {
            case 1:
                Console.log(message);
                break;
            case 2:
                Console.log("Warning: {}", message);
                break;
            case 3:
                Console.error(message);
                break;
            case 0:
            default:
                break;
        }

    }

    /**
     * 获取生成文件路径
     */
    private String getGenerateFileParent() {
        return generateFilesParentPath.length() == 0 ? parentDirectoryPath : generateFilesParentPath;
    }

    /**
     * 获取EndCell值
     * <p>
     * 为End Sheet 所在行数 -2
     */
    private String getEndCell(ExcelReader reader) {
        return endCellColumn + (reader.getRowCount() - 2);
    }

    private static String valueOf(Object obj) {
        return (obj == null) ? "" : obj.toString();
    }

}
