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

package com.tlcsdm.demo;

import com.tlcsdm.demo.samples.Utils;
import com.tlcsdm.frame.SampleBase;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public abstract class ControlsFXSample extends SampleBase {

    // 项目信息
    private static final ProjectInfo PROJECTINFO = new ProjectInfo();

    @Override
    public String getProjectName() {
        return "ControlsFX";
    }

    @Override
    public String getProjectVersion() {
        return PROJECTINFO.getVersion();
    }

    @Override
    public String getOrderKey() {
        return getSampleName();
    }

    @Override
    public String getSampleVersion() {
        return "1.0.0";
    }

    @Override
    public String getSampleId() {
        return "controlsFX";
    }

    @Override
    public ImageView getSampleImageIcon() {
        return null;
    }

    /**
     * A full URL to the javadoc for the API being demonstrated in this sample.
     */
    public abstract String getJavaDocURL();

    /**
     * A full URL to a sample source code, which is assumed to be in java.
     */
    public String getSampleSourceURL() {
        return Utils.SAMPLES_BASE + getClass().getName().replace('.', '/') + ".java";
    }

    /**
     * Returns URL for control's stylesheet. If the CSS resource is available on
     * module-path,
     */
    public String getControlStylesheetURL() {
        return null;
    }

    @Override
    protected void initializeUserData() {
        // Do nothing
    }

    /**
     * 项目信息
     */
    private static class ProjectInfo {

        private String version;

        public ProjectInfo() {

            InputStream s = getClass().getClassLoader().getResourceAsStream("META-INF/MANIFEST.MF");

            try {
                Manifest manifest = new Manifest(s);
                Attributes attr = manifest.getMainAttributes();
                version = attr.getValue("Implementation-Version");
            } catch (IOException e) {
                System.out.println("Unable to load project version for ControlsFX "
                    + "samples project as the manifest file can't be read "
                    + "or the Implementation-Version attribute is unavailable.");
                version = "";
            }
        }

        public String getVersion() {
            return version;
        }
    }
}
