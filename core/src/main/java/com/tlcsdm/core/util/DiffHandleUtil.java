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

package com.tlcsdm.core.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.StaticLog;
import com.github.difflib.DiffUtils;
import com.github.difflib.UnifiedDiffUtils;
import com.github.difflib.patch.Patch;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * 文本差异工具.
 *
 * @author unknowIfGuestInDream
 * @since 1.0
 */
public class DiffHandleUtil {

    private DiffHandleUtil() {
    }

    /**
     * 对比两文件的差异，返回原始文件+diff格式
     *
     * @param original 原文件内容
     * @param revised  对比文件内容
     */
    public static List<String> diffString(List<String> original, List<String> revised) {
        return diffString(original, revised, null, null);
    }

    /**
     * 对比两文件的差异，返回原始文件+diff格式
     *
     * @param original         原文件内容
     * @param revised          对比文件内容
     * @param originalFileName 原始文件名
     * @param revisedFileName  对比文件名
     */
    public static List<String> diffString(List<String> original, List<String> revised, String originalFileName, String revisedFileName) {
        originalFileName = originalFileName == null ? "Original" : originalFileName;
        revisedFileName = revisedFileName == null ? "Revised" : revisedFileName;
        // 两文件的不同点
        Patch<String> patch = DiffUtils.diff(original, revised);
        // 生成统一的差异格式
        List<String> unifiedDiff = UnifiedDiffUtils.generateUnifiedDiff(originalFileName, revisedFileName, original,
            patch, 0);
        int diffCount = unifiedDiff.size();
        if (diffCount == 0) {
            // 如果两文件没差异则插入如下
            unifiedDiff.add("--- " + originalFileName);
            unifiedDiff.add("+++ " + revisedFileName);
            unifiedDiff.add("@@ -0,0 +0,0 @@");
        } else if (diffCount >= 3 && !unifiedDiff.get(2).contains("@@ -1,")) {
            unifiedDiff.set(1, unifiedDiff.get(1));
            // 如果第一行没变化则插入@@ -0,0 +0,0 @@
            unifiedDiff.add(2, "@@ -0,0 +0,0 @@");
        }
        // 原始文件中每行前加空格
        List<String> original1 = original.stream().map(v -> " " + v).collect(Collectors.toList());
        // 差异格式插入到原始文件中
        return insertOrig(original1, unifiedDiff);
    }

    /**
     * 对比两文件的差异，返回原始文件+diff格式
     *
     * @param filePathOriginal 原文件路径
     * @param filePathRevised  对比文件路径
     */
    public static List<String> diffString(String filePathOriginal, String filePathRevised) {
        // 原始文件
        List<String> original = null;
        // 对比文件
        List<String> revised = null;
        File originalFile = new File(filePathOriginal);
        File revisedFile = new File(filePathRevised);
        try {
            original = Files.readAllLines(originalFile.toPath());
            revised = Files.readAllLines(revisedFile.toPath());
        } catch (IOException e) {
            StaticLog.error(e);
        }
        return diffString(original, revised, originalFile.getName(), revisedFile.getName());
    }

    public static void generateDiffHtml(List<String> diffString, String htmlPath) {
        generateDiffHtml(htmlPath, List.of(diffString));
    }

    /**
     * 通过两文件的差异diff生成 html文件，打开此 html文件便可看到文件对比的明细内容
     */
    public static void generateDiffHtml(String htmlPath, List<List<String>> diffStringList) {
        String template = getDiffHtml(diffStringList);
        // 文件读取为字符流
        FileWriter f;
        try {
            f = new FileWriter(htmlPath);
            // 文件加入缓冲区
            BufferedWriter buf = new BufferedWriter(f);
            // 向缓冲区写入
            buf.write(template);
            // 关闭缓冲区并将信息写入文件
            buf.close();
            f.close();
        } catch (IOException e) {
            StaticLog.error(e);
        }
    }

    /**
     * 通过两文件的差异diff生成 html文件，打开此 html文件便可看到文件对比的明细内容
     */
    public static String getDiffHtml(List<List<String>> diffStringList) {
        // 如果打开html为空白界面，可能cdn加载githubCss失败 ,githubCss 链接可替换为
        //        String githubCss = "https://cdnjs.cloudflare.com/ajax/libs/highlight.js/10.7.1/styles/github.min.css";
        //https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.8.0/styles/github-dark.min.css
        //        String diff2htmlCss = "https://cdn.jsdelivr.net/npm/diff2html/bundles/css/diff2html.min.css";
        //        String diff2htmlJs = "https://cdn.jsdelivr.net/npm/diff2html/bundles/js/diff2html-ui.min.js";
        Map<String, Object> map = new HashMap<>(8);
        try {
            map.put("highlightCss", CoreUtil.readStream(
                DiffHandleUtil.class.getResource("/com/tlcsdm/core/static/diff2html/github.min.css").openStream()));
            map.put("diff2htmlCss", CoreUtil.readStream(
                DiffHandleUtil.class.getResource("/com/tlcsdm/core/static/diff2html/diff2html.min.css").openStream()));
            map.put("diff2htmlJs", CoreUtil.readStream(
                DiffHandleUtil.class.getResource("/com/tlcsdm/core/static/diff2html/diff2html-ui.min.js")
                    .openStream()));
        } catch (IOException e) {
            StaticLog.error(e);
        }
        String template = """
            <!DOCTYPE html>
            <html lang="en-us">
            <head>
              <meta charset="utf-8" />
              <meta name="google" content="notranslate" />
              <meta name="author" content="liang.tang.cx@gmail.com">
            </head>
            <style type="text/css">
            {highlightCss}
            </style>
            <style type="text/css">
            {diff2htmlCss}
            </style>
            <script type="text/javascript">
            {diff2htmlJs}
            </script>
            <script>
              const diffString = `
            {diffString}
              `;

              document.addEventListener('DOMContentLoaded', function () {
                var targetElement = document.getElementById('myDiffElement');
                var configuration = {
                  drawFileList: true,
                  fileListToggle: true,
                  fileListStartVisible: true,
                  fileContentToggle: true,
                  matching: 'lines',
                  outputFormat: 'side-by-side',
                  synchronisedScroll: true,
                  highlight: true,
                  renderNothingWhenEmpty: true,
                };
                var diff2htmlUi = new Diff2HtmlUI(targetElement, diffString, configuration);
                diff2htmlUi.draw();
                diff2htmlUi.highlightCode();
              });
            </script>
            <body>
              <div id="myDiffElement"></div>
            </body>
            </html>
            """;
        StringJoiner diffStringJoiner = new StringJoiner("\n");
        for (int i = 0; i < diffStringList.size(); i++) {
            List<String> diffString = diffStringList.get(i);
            StringBuilder builder = new StringBuilder();
            for (String line : diffString) {
                // 特殊处理 $
                builder.append(StrUtil.replace(line, "$", "\\$"));
                builder.append("\n");
            }
            diffStringJoiner.add(builder.toString());
        }
        map.put("diffString", diffStringJoiner.toString());
        return StrUtil.format(template, map);
    }

    // 统一差异格式插入到原始文件
    public static List<String> insertOrig(List<String> original, List<String> unifiedDiff) {
        List<String> result = new ArrayList<>();
        // unifiedDiff中根据@@分割成不同行，然后加入到diffList中
        List<List<String>> diffList = new ArrayList<>();
        List<String> d = new ArrayList<>();
        for (int i = 0; i < unifiedDiff.size(); i++) {
            String u = unifiedDiff.get(i);
            if (u.startsWith("@@") && !"@@ -0,0 +0,0 @@".equals(u) && !u.contains("@@ -1,")) {
                List<String> twoList = new ArrayList<>(d);
                diffList.add(twoList);
                d.clear();
                d.add(u);
                continue;
            }
            if (i == unifiedDiff.size() - 1) {
                d.add(u);
                List<String> twoList = new ArrayList<>(d);
                diffList.add(twoList);
                d.clear();
                break;
            }
            d.add(u);
        }

        // 将diffList和原始文件original插入到result，返回result
        for (int i = 0; i < diffList.size(); i++) {
            List<String> diff = diffList.get(i);
            List<String> nexDiff = i == diffList.size() - 1 ? null : diffList.get(i + 1);
            // 含有@@的一行
            String simb = i == 0 ? diff.get(2) : diff.get(0);
            String nexSimb = nexDiff == null ? null : nexDiff.get(0);
            // 插入到result
            insert(result, diff);
            // 解析含有@@的行，得到原文件从第几行开始改变，改变了多少（即增加和减少的行）
            Map<String, Integer> map = getRowMap(simb);
            if (null != nexSimb) {
                Map<String, Integer> nexMap = getRowMap(nexSimb);
                int start = 0;
                if (map.get("orgRow") != 0) {
                    start = map.get("orgRow") + map.get("orgDel") - 1;
                }
                int end = nexMap.get("revRow") - 2;
                // 插入不变的
                insert(result, getOrigList(original, start, end));
            }

            int start = (map.get("orgRow") + map.get("orgDel") - 1);
            start = start == -1 ? 0 : start;
            if (simb.contains("@@ -1,") && null == nexSimb && map.get("orgDel") != original.size()) {
                insert(result, getOrigList(original, start, original.size() - 1));
            } else if (null == nexSimb && (map.get("orgRow") + map.get("orgDel") - 1) < original.size()) {
                insert(result, getOrigList(original, start, original.size() - 1));
            }
        }
        int diffCount = diffList.size() - 1;
        if (!"@@ -0,0 +0,0 @@".equals(unifiedDiff.get(2))) {
            diffCount = Math.max(diffList.size(), 1);
        }
        result.set(1, result.get(1) + " ( " + diffCount + " different )");
        return result;
    }

    // 将源文件中没变的内容插入result
    public static void insert(List<String> result, List<String> noChangeContent) {
        result.addAll(noChangeContent);
    }

    // 解析含有@@的行得到修改的行号删除或新增了几行
    public static Map<String, Integer> getRowMap(String str) {
        Map<String, Integer> map = new HashMap<>();
        if (str.startsWith("@@")) {
            String[] sp = str.split(" ");
            String org = sp[1];
            String[] orgSp = org.split(",");
            // 源文件要删除行的行号
            map.put("orgRow", Integer.valueOf(orgSp[0].substring(1)));
            // 源文件删除的行数
            map.put("orgDel", Integer.valueOf(orgSp[1]));

            String[] revSp = org.split(",");
            // 对比文件要增加行的行号
            map.put("revRow", Integer.valueOf(revSp[0].substring(1)));
            map.put("revAdd", Integer.valueOf(revSp[1]));
        }
        return map;
    }

    // 从原文件中获取指定的部分行
    public static List<String> getOrigList(List<String> original1, int start, int end) {
        List<String> list = new ArrayList<>();
        if (!original1.isEmpty() && start <= end && end < original1.size()) {
            for (; start <= end; start++) {
                list.add(original1.get(start));
            }
        }
        return list;
    }
}
