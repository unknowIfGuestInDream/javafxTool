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

package com.tlcsdm.smc;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.StaticLog;
import com.tlcsdm.frame.SampleBase;
import com.tlcsdm.smc.util.SmcConstant;

import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public abstract class SmcSample extends SampleBase {
    public static final ProjectInfo PROJECT_INFO = new ProjectInfo();

    @Override
    public String getProjectName() {
        return SmcConstant.PROJECT_NAME;
    }

    @Override
    public String getProjectVersion() {
        return PROJECT_INFO.getVersion();
    }

    public static class ProjectInfo {

        private String version;
        private String date;

        public ProjectInfo() {

            try {
                InputStream s = SmcSampler.class.getModule().getResourceAsStream("META-INF/MANIFEST.MF");
                Manifest manifest = new Manifest(s);
                Attributes attr = manifest.getMainAttributes();
                // SmcConstant变量是为了打包exe时使用
                version = StrUtil.blankToDefault(attr.getValue("Implementation-Version"), SmcConstant.PROJECT_VERSION);
                date = StrUtil.blankToDefault(attr.getValue("Build-Day"), SmcConstant.PROJECT_BUILD_DAY);
            } catch (Throwable e) {
                version = SmcConstant.PROJECT_VERSION;
                date = SmcConstant.PROJECT_BUILD_DAY;
                StaticLog.error("Fail to get MANIFEST.MF.");
            }
        }

        public String getVersion() {
            return version;
        }

        public String getDate() {
            return date;
        }
    }

}
