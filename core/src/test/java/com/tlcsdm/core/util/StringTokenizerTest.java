/*
 * Copyright (c) 2023 unknowIfGuestInDream.
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

import java.util.StringTokenizer;

/**
 * Test for StringTokenizer.
 *
 * @author unknowIfGuestInDream
 */
class StringTokenizerTest {

    /**
     * StringTokenizer(String)采用采用默认分隔符.
     * 空格字符，制表符、换行符、回车符、和换页符
     * <p>
     * StringTokenizer(String str, String delim)
     * delim: 分隔符
     * <p>
     * StringTokenizer(String str, String delim, boolean returnDelims)
     * returnDelims: 是否返回分隔符的标志作为令牌
     */
    @Test
    void stringTokenizer() {
        StringTokenizer st1 = new StringTokenizer("www abc\n\tcom\n");
        String s = "";
        Assertions.assertEquals(3, st1.countTokens());
        while (st1.hasMoreElements()) {
            s += st1.nextToken();
        }
        Assertions.assertEquals("wwwabccom", s);
        Assertions.assertEquals(0, st1.countTokens());

        StringTokenizer st2 = new StringTokenizer("www.abc.com", ".", true);
        s = "";
        Assertions.assertEquals(5, st2.countTokens());
        while (st2.hasMoreElements()) {
            s += st2.nextToken();
        }
        Assertions.assertEquals("www.abc.com", s);

        StringTokenizer st3 = new StringTokenizer("www.abc.com", ".", false);
        s = "";
        Assertions.assertEquals(3, st3.countTokens());
        while (st3.hasMoreElements()) {
            s += st3.nextToken();
        }
        Assertions.assertEquals("wwwabccom", s);
    }

}
