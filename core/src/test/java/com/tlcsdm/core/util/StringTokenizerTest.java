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
     * 
     * StringTokenizer(String str, String delim)
     * delim: 分隔符
     * 
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
