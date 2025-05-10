/*
 * Copyright (c) 2025 unknowIfGuestInDream.
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

package com.tlcsdm.core.javafx.richtext;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SON Code Area
 *
 * @author unknowIfGuestInDream
 */
public class JsonCodeArea extends CodeArea {

    //匹配 JSON 的 key
    private static final String KEY_PATTERN = "\"(\\w+)\"(?=\\s*:)";
    //匹配 JSON 的 value（支持字符串、数字、布尔值和 null）
    private static final String VALUE_PATTERN = ":\\s*(\".*?\"|\\d+|true|false|null)";
    //匹配 JSON 的结构符号（如大括号、中括号、逗号等）
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String COMMA_PATTERN = ",";
    private static final String COLON_PATTERN = ":";

    private static final Pattern PATTERN = Pattern.compile(
        "(?<KEY>" + KEY_PATTERN + ")" +
            "|(?<VALUE>" + VALUE_PATTERN + ")" +
            "|(?<BRACE>" + BRACE_PATTERN + ")" +
            "|(?<BRACKET>" + BRACKET_PATTERN + ")" +
            "|(?<COMMA>" + COMMA_PATTERN + ")" +
            "|(?<COLON>" + COLON_PATTERN + ")");

    public JsonCodeArea() {
        super();
        getStyleClass().add("text-json-area");
        getStylesheets().add(
            getClass().getResource("/com/tlcsdm/core/static/javafx/richtext/json-keywords.css").toExternalForm());
        this.setParagraphGraphicFactory(LineNumberFactory.get(this));
        this.textProperty().addListener((obs, oldText, newText) -> {
            this.setStyleSpans(0, computeHighlighting(newText));
        });
    }

    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        while (matcher.find()) {
            String styleClass = matcher.group("KEY") != null ? "key"
                : matcher.group("VALUE") != null ? "value"
                : matcher.group("BRACE") != null ? "brace"
                : matcher.group("BRACKET") != null ? "bracket"
                : matcher.group("COMMA") != null ? "comma"
                : matcher.group("COLON") != null ? "colon" : null;
            assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }
}
