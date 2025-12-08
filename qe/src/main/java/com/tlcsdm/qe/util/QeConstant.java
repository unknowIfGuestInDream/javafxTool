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

package com.tlcsdm.qe.util;

import java.time.LocalDate;
import java.util.List;

/**
 * smc模块常量
 *
 * @author unknowIfGuestInDream
 * @date 2022/11/16 21:11
 */
public class QeConstant {
    /**
     * github项目地址
     */
    public static final String GITHUB_PROJECT_URL = "https://github.com/unknowIfGuestInDream/javafxTool";
    /**
     * 项目支持地址
     */
    public static final String GITHUB_PROJECT_SUPPORT_URL = "https://github.com/unknowIfGuestInDream/javafxTool/issues";
    /**
     * 项目讨论地址
     */
    public static final String GITHUB_PROJECT_FEEDBACK_URL = "https://github.com/unknowIfGuestInDream/javafxTool/discussions";
    /**
     * 项目许可证
     */
    public static final String PROJECT_LICENSE_URL = "https://javafxtool.tlcsdm.com/LICENSE.txt";
    /**
     * 项目许可证名称
     */
    public static final String PROJECT_LICENSE_NAME = "MIT";
    /**
     * 项目名
     */
    public static final String PROJECT_NAME = "qe";
    /**
     * 项目作者
     */
    public static final String PROJECT_AUTHOR = "unknowIfGuestInDream";
    /**
     * 项目版权声明
     */
    public static final String PROJECT_COPYRIGHT = "Copyright © " + LocalDate.now().getYear() + " unknowIfGuestInDream";
    /**
     * 发布版本
     * 可由VersionCheckerService实现类修改数据
     */
    public static String PROJECT_RELEASE_URL = "https://github.com/unknowIfGuestInDream/javafxTool/releases?q=qe&expanded=true";
    /**
     * 默认项目版本号
     */
    public static final String PROJECT_VERSION = "1.0.2";
    /**
     * 项目构建日期
     */
    public static final String PROJECT_BUILD_DAY = "2025-12-08";
    /**
     * 检查更新所用的api
     */
    public static final String PROJECT_VERSION_CHECK_URL = "https://api.github.com/repos/unknowIfGuestInDream/javafxTool/releases";
    /**
     * 用于校验的tag后缀
     */
    public static final String PROJECT_TAG_SUBFIX = "-qe";

    /**
     * 当前项目加的依赖
     */
    public static final List<String> DEPENDENCY_LIST = List.of("poi", "freemarker", "dom4j", "java-diff-utils",
        "richtextfx", "thumbnailator", "jackson", "yuicompressor", "teenyhttpd", "image4j", "commons-imaging",
        "icns-core", "preferencesfx", "jSerialComm");

    private QeConstant() {
    }
}
