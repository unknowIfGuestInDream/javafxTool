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

import cn.hutool.core.io.LineHandler;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Console;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.style.StyleUtil;
import com.tlcsdm.core.wrap.hutool.FileUtil;
import com.tlcsdm.core.wrap.hutool.StrUtil;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 生成代码长度检测
 */
@DisabledIfSystemProperty(named = "workEnv", matches = "ci")
public class CodeStyleLength120Test {

    // 待比对文件的父级路径
    private final String generateFilesParentPath = "C:\\Users\\os_tangliang\\runtime-smc.rh850.product\\src\\smc_gen";
    // 检测的文件类型(填文件后缀即可)
    private final List<String> fileTypeList = List.of("h", "c");
    // 忽略的文件
    private final List<String> ignoreFilesList = List.of();
    // 结果信息输出路径
    private final String resultPath = "C:\\workspace\\test";
    // 结果文件名称(目前支持结果输出到excel)
    private final String resultFileName = "CodeStyleLength120.xlsx";
    // 初始化删除结果文件
    private final boolean initClean = true;
    // 结果信息
    private final List<Map<String, Object>> result = new ArrayList<>();
    // 待初始化文件对象
    private List<File> files;

    @BeforeEach
    public void init() {
        Assert.isTrue(FileUtil.isDirectory(generateFilesParentPath),
            "The variable generateFilesParentPath must be a folder");
        Assert.isTrue(FileUtil.isDirectory(resultPath), "The variable resultPath must be a folder");
        Assert.isTrue(StrUtil.endWith(resultFileName, ".xlsx"), "The variable resultFileName must end with \".xlsx\"");
        files = FileUtil.loopFiles(generateFilesParentPath, new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isFile() && !ignoreFilesList.contains(file.getName())) {
                    for (String fileType : fileTypeList) {
                        if (StrUtil.endWith(file.getName(), "." + fileType)) {
                            return true;
                        }
                    }
                }
                return false;
            }
        });
        Assert.notEmpty(files, "There are no eligible files in the current path");
        cleanFile(FileUtil.file(resultPath, resultFileName));
    }

    @Test
    public void CodeStyleLength120Test1() {
        for (File file : files) {
            AtomicInteger atomicInteger = new AtomicInteger(0);
            FileUtil.readUtf8Lines(file, new LineHandler() {
                @Override
                public void handle(String line) {
                    atomicInteger.incrementAndGet();
                    if (line.length() > 120) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("lineNumber", atomicInteger.get());
                        map.put("fileName", file.getName());
                        map.put("lineLength", line.length());
                        map.put("filePath", file.getPath());
                        map.put("line", line);
                        result.add(map);
                    }
                }
            });
        }
        handleResult();
    }

    // 结果信息处理
    private void handleResult() {
        if (result.size() == 0) {
            Console.log("No lines longer than 120");
            return;
        }
        ExcelWriter writer = ExcelUtil.getWriter(FileUtil.file(resultPath, resultFileName));
        setExcelStyle(writer);
        handleData(writer);
        writer.addHeaderAlias("lineNumber", "行号");
        writer.addHeaderAlias("fileName", "文件名");
        writer.addHeaderAlias("lineLength", "行文本长度");
        writer.addHeaderAlias("filePath", "文件路径");
        writer.addHeaderAlias("line", "行文本数据");
        writer.write(result, true);
        writer.close();
        Console.log("================== END ==================");
    }

    /**
     * 删除文件
     */
    private void cleanFile(File file) {
        if (initClean) {
            FileUtil.del(file);
        }
    }

    // 设置生成的excel样式
    private void setExcelStyle(ExcelWriter writer) {
        writer.getStyleSet().setAlign(HorizontalAlignment.LEFT, VerticalAlignment.CENTER);
        writer.getHeadCellStyle().setAlignment(HorizontalAlignment.CENTER);
        CellStyle style = writer.getStyleSet().getHeadCellStyle();
        StyleUtil.setColor(style, IndexedColors.LIGHT_YELLOW, FillPatternType.SOLID_FOREGROUND);
        writer.setColumnWidth(0, 10);
        writer.setColumnWidth(1, 20);
        writer.setColumnWidth(2, 10);
        writer.setColumnWidth(3, 70);
        writer.setColumnWidth(4, 110);
    }

    /**
     * 数据处理
     */
    private void handleData(ExcelWriter writer) {

    }

}
