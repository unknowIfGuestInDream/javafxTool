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

package com.tlcsdm.core.util;

import cn.hutool.core.util.EscapeUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;

/**
 * @author: unknowIfGuestInDream
 * @date: 2023/4/16 10:01
 */
public class HtmlUtil {

    public static final String NBSP = "&nbsp;";
    public static final String AMP = "&amp;";
    public static final String QUOTE = "&quot;";
    public static final String APOS = "&apos;";
    public static final String LT = "&lt;";
    public static final String GT = "&gt;";

    public static final String RE_HTML_MARK = "(<[^<]*?>)|(<[\\s]*?/[^<]*?>)|(<[^<]*?/[\\s]*?>)";
    public static final String RE_SCRIPT = "<[\\s]*?script[^>]*?>.*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";

    private static final char[][] TEXT = new char[256][];

    static {
        // ascii码值最大的是【0x7f=127】，扩展ascii码值最大的是【0xFF=255】，因为ASCII码使用指定的7位或8位二进制数组合来表示128或256种可能的字符，标准ASCII码也叫基础ASCII码。
        for (int i = 0; i < 256; i++) {
            TEXT[i] = new char[]{(char) i};
        }

        // special HTML characters
        TEXT['\''] = "&#039;".toCharArray(); // 单引号 ('&apos;' doesn't work - it is not by the w3 specs)
        TEXT['"'] = QUOTE.toCharArray(); // 双引号
        TEXT['&'] = AMP.toCharArray(); // &符
        TEXT['<'] = LT.toCharArray(); // 小于号
        TEXT['>'] = GT.toCharArray(); // 大于号
        TEXT[' '] = NBSP.toCharArray(); // 不断开空格（non-breaking space，缩写nbsp。ASCII值是32：是用键盘输入的空格；ASCII值是160：不间断空格，即 &nbsp，所产生的空格，作用是在页面换行时不被打断）
    }

    /**
     * 转义文本中的HTML字符为安全的字符，以下字符被转义：
     * <ul>
     * <li>' 替换为 &amp;#039; (&amp;apos; doesn't work in HTML4)</li>
     * <li>" 替换为 &amp;quot;</li>
     * <li>&amp; 替换为 &amp;amp;</li>
     * <li>&lt; 替换为 &amp;lt;</li>
     * <li>&gt; 替换为 &amp;gt;</li>
     * </ul>
     *
     * @param text 被转义的文本
     * @return 转义后的文本
     */
    public static String escape(String text) {
        return encode(text);
    }

    /**
     * 还原被转义的HTML特殊字符
     *
     * @param htmlStr 包含转义符的HTML内容
     * @return 转换后的字符串
     */
    public static String unescape(String htmlStr) {
        if (StrUtil.isBlank(htmlStr)) {
            return htmlStr;
        }

        return EscapeUtil.unescapeHtml4(htmlStr);
    }

    // ---------------------------------------------------------------- encode text

    /**
     * 清除所有HTML标签，但是不删除标签内的内容
     *
     * @param content 文本
     * @return 清除标签后的文本
     */
    public static String cleanHtmlTag(String content) {
        return content.replaceAll(RE_HTML_MARK, "");
    }

    /**
     * 清除指定HTML标签和被标签包围的内容<br>
     * 不区分大小写
     *
     * @param content  文本
     * @param tagNames 要清除的标签
     * @return 去除标签后的文本
     */
    public static String removeHtmlTag(String content, String... tagNames) {
        return removeHtmlTag(content, true, tagNames);
    }

    /**
     * 清除指定HTML标签，不包括内容<br>
     * 不区分大小写
     *
     * @param content  文本
     * @param tagNames 要清除的标签
     * @return 去除标签后的文本
     */
    public static String unwrapHtmlTag(String content, String... tagNames) {
        return removeHtmlTag(content, false, tagNames);
    }

    /**
     * 清除指定HTML标签<br>
     * 不区分大小写
     *
     * @param content        文本
     * @param withTagContent 是否去掉被包含在标签中的内容
     * @param tagNames       要清除的标签
     * @return 去除标签后的文本
     */
    public static String removeHtmlTag(String content, boolean withTagContent, String... tagNames) {
        String regex;
        for (String tagName : tagNames) {
            if (StrUtil.isBlank(tagName)) {
                continue;
            }
            tagName = tagName.trim();
            // (?i)表示其后面的表达式忽略大小写
            if (withTagContent) {
                // 标签及其包含内容
                regex = StrUtil.format("(?i)<{}(\\s+[^>]*?)?/?>(.*?</{}>)?", tagName, tagName);
            } else {
                // 标签不包含内容
                regex = StrUtil.format("(?i)<{}(\\s+[^>]*?)?/?>|</?{}>", tagName, tagName);
            }

            content = ReUtil.delAll(regex, content); // 非自闭标签小写
        }
        return content;
    }

    /**
     * 去除HTML标签中的属性，如果多个标签有相同属性，都去除
     *
     * @param content 文本
     * @param attrs   属性名（不区分大小写）
     * @return 处理后的文本
     */
    public static String removeHtmlAttr(String content, String... attrs) {
        String regex;
        for (String attr : attrs) {
            // (?i)     表示忽略大小写
            // \s*      属性名前后的空白符去除
            // [^>]+?   属性值，至少有一个非>的字符，>表示标签结束
            // \s+(?=>) 表示属性值后跟空格加>，即末尾的属性，此时去掉空格
            // (?=\s|>) 表示属性值后跟空格（属性后还有别的属性）或者跟>（最后一个属性）
            regex = StrUtil.format("(?i)(\\s*{}\\s*=[^>]+?\\s+(?=>))|(\\s*{}\\s*=[^>]+?(?=\\s|>))", attr, attr);
            content = content.replaceAll(regex, StrUtil.EMPTY);
        }
        return content;
    }

    /**
     * 去除指定标签的所有属性
     *
     * @param content  内容
     * @param tagNames 指定标签
     * @return 处理后的文本
     */
    public static String removeAllHtmlAttr(String content, String... tagNames) {
        String regex;
        for (String tagName : tagNames) {
            regex = StrUtil.format("(?i)<{}[^>]*?>", tagName);
            content = content.replaceAll(regex, StrUtil.format("<{}>", tagName));
        }
        return content;
    }

    /**
     * Encoder
     *
     * @param text 被编码的文本
     * @return 编码后的字符
     */
    private static String encode(String text) {
        int len;
        if ((text == null) || ((len = text.length()) == 0)) {
            return StrUtil.EMPTY;
        }
        StringBuilder buffer = new StringBuilder(len + (len >> 2));
        char c;
        for (int i = 0; i < len; i++) {
            c = text.charAt(i);
            if (c < 256) {
                buffer.append(TEXT[c]);
            } else {
                buffer.append(c);
            }
        }
        return buffer.toString();
    }
}
