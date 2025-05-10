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

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;

import java.time.LocalDate;

/**
 * @author unknowIfGuestInDream
 * @date 2023/8/6 15:45
 */
public class CoreConstant {

    /**
     * JavaFX API
     */
    public static final String JAVAFX_API_URL = "https://openjfx.io/javadoc/21";

    /**
     * JavaFX CSS API
     */
    public static final String JAVAFX_API_CSS_URL = "https://openjfx.io/javadoc/21/javafx.graphics/javafx/scene/doc-files/cssref.html";

    /**
     * JavaFX FXML API
     */
    public static final String JAVAFX_API_FXML_URL = "https://openjfx.io/javadoc/21/javafx.fxml/javafx/fxml/doc-files/introduction_to_fxml.html";
    /**
     * JVM配置key.
     */
    public static final String JVM_WORKENV = "workEnv";
    /**
     * 开发环境.
     */
    public static final String JVM_WORKENV_DEV = "dev";
    /**
     * CI/CD环境.
     */
    public static final String JVM_WORKENV_CI = "ci";
    /**
     * 测试环境.
     */
    public static final String JVM_WORKENV_TEST = "test";
    /**
     * 生产环境.
     */
    public static final String JVM_WORKENV_PROD = "prod";

    /**
     * 编码UTF-8.
     */
    public static final String ENCODING_UTF_8 = "UTF-8";

    /**
     * LICENSE内容
     */
    public static final String PROJECT_LICENSE_CONTENT_STRING = StrUtil.format("""
        MIT License

        Copyright (c) {year} unknowIfGuestInDream

        Permission is hereby granted, free of charge, to any person obtaining a copy
        of this software and associated documentation files (the "Software"), to deal
        in the Software without restriction, including without limitation the rights
        to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
        copies of the Software, and to permit persons to whom the Software is
        furnished to do so, subject to the following conditions:

        The above copyright notice and this permission notice shall be included in all
        copies or substantial portions of the Software.

        THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
        IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
        FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
        AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
        LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
        OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
        SOFTWARE.
                """, MapUtil.builder("year", LocalDate.now().getYear()).build());

}
