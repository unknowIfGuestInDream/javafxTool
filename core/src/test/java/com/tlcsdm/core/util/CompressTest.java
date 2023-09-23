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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test for CompressUtil.
 *
 * @author unknowIfGuestInDream
 */
class CompressTest {

    private final String jsCode = """
        /*示例代码*/
        function echo(stringA,stringB){
        	var hello="你好";
        	alert("hello world");
        }
        /*示例代码*/
        """;

    private final String cssCode = """
        @font-face {
            font-family: 'JetBrains Mono';
            src: url('JetBrainsMono-Regular.ttf');
        }
        .text-groovy-area,
        .text-java-area {
            -fx-font-family: 'JetBrains Mono';
            -fx-font-smoothing-type: gray;
            -fx-font-size: 13;
        }
        .keyword {
            -fx-fill: purple;
            -fx-font-weight: bold;
        }
        .paragraph-box:has-caret {
            -fx-background-color: #f2f9fc;
        }
        """;

    @Test
    void compressJsWithCode() {
        Assertions.assertEquals("function echo(c,b){var a=\"你好\";alert(\"hello world\")};", CompressUtil.compressJS(jsCode));
        Assertions.assertEquals("function echo(stringA,stringB){var hello=\"你好\";alert(\"hello world\")};", CompressUtil.compressJS(jsCode, -1, false, false, false, false));
    }

    @Test
    void compressCssWithCode() {
        Assertions.assertEquals("@font-face{font-family:'JetBrains Mono';src:url('JetBrainsMono-Regular.ttf')}.text-groovy-area,.text-java-area{-fx-font-family:'JetBrains Mono';-fx-font-smoothing-type:gray;-fx-font-size:13}.keyword{-fx-fill:purple;-fx-font-weight:bold}.paragraph-box:has-caret{-fx-background-color:#f2f9fc}"
            , CompressUtil.compressCSS(cssCode));
    }
}
