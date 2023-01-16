package com.tlcsdm.smc.tool;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 既有头文件转换格式，方便后续粘到excel中
 */
public class HconvertExcel {

    // 生成结果目录路径
    private final String resultPath = "C:\\workspace\\test\\Hconvert";
    // 待比对文件的父级路径
    private final String generateFilesParentPath = "C:\\Users\\os_tangliang\\runtime-smc.rh850.product\\src\\smc_gen\\general";
    // 忽略的文件
    private final List<String> ignoreFileNames = List.of();
    // 如果markFileNames不为空，那么只会读取当前对象内的文件, 忽略ignoreFileNames配置
    private final List<String> markFileNames = List.of();
    // 支持的文件类型，默认只支持h文件
    private final List<String> supportFileType = List.of("h");

    @Test
    public void hconvertExcel() {
        FileUtil.clean(resultPath);
        List<File> files = FileUtil.loopFiles(generateFilesParentPath, new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (markFileNames.size() != 0) {
                    for (String markFile : markFileNames) {
                        if (file.isFile() && markFile.equals(file.getName())) {
                            return true;
                        }
                    }
                }
                if (markFileNames.size() == 0 && file.isFile() && !ignoreFileNames.contains(file.getName())) {
                    for (String fileType : supportFileType) {
                        if (fileType.equals(FileUtil.getSuffix(file))) {
                            return true;
                        }
                    }
                }
                return false;
            }
        });

        for (File file : files) {
            List<String> content = FileUtil.readUtf8Lines(file);
            List<String> newContent = new ArrayList<>();
            boolean f = false;
            for (String s : content) {
                // 去除原文件的脏内容
                s.replaceAll("\\t", "    ");
                // 为换行的注释添加tab
                if (f) {
                    s = "\t\t\t" + StrUtil.trim(s);
                    f = false;
                }
                if (s.startsWith("#define")) {
                    s = dealLine(s);
                    if (s.contains("/*") && !s.contains("*/")) {
                        f = true;
                    }
                }
                if ("r_smc_interrupt.h".equals(file.getName())) {
                    s = dealSmcInterrupt(s);
                }
                newContent.add(s);
            }
            FileUtil.writeUtf8Lines(newContent,
                    FileUtil.file(resultPath, FileUtil.getPrefix(file) + "_refer." + FileUtil.getSuffix(file)));
        }

    }

    private String dealLine(String s) {
        if (s.length() < 8) {
            return s;
        }
        int i1 = s.indexOf("(");
        int i2 = s.indexOf(")");
        int i3 = s.indexOf("/*");
        if (i1 < 0 || i2 < 0) {
            return s;
        }
        String s1 = s.substring(0, 7);
        String s2 = s.substring(7, i1);
        s2 = StrUtil.trim(s2);
        String s3 = s.substring(i1, i2 + 1);
        if (i3 < 0) {
            return s1 + "\t" + s2 + "\t" + s3;
        }
        String s4 = s.substring(i3);
        return s1 + "\t" + s2 + "\t" + s3 + "\t" + s4;
    }

    /**
     * r_smc_interrupt.h 特殊处理
     */
    private String dealSmcInterrupt(String s) {
        if (s.length() < 8) {
            return s;
        }
        int i1 = s.indexOf("_INT_PRIORITY");
        if (i1 < 0) {
            return s;
        }
        String s1 = s.substring(0, 7);
        String s2 = s.substring(7, i1);
        s2 = StrUtil.trim(s2);
        String s3 = s.substring(i1);
        return s1 + "\t" + s2 + "\t" + s3;
    }

}
