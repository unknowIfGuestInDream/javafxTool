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

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.List;

/**
 * 项目总体依赖信息.
 * 可能需要手动维护，用于应用项目获取相应数据使用(需要应用模块过滤)
 *
 * @author unknowIfGuestInDream
 */
public class DependencyInfo {

    public static List<Dependency> getDependencyList() {
        return SingletonInstance.INSTANCE;
    }

    private static class SingletonInstance {
        private static final List<Dependency> INSTANCE = List.of(
            new Dependency("org.openjfx", "javafx", "21.0.4", true, "https://github.com/openjdk/jfx", "GPLv2+CE",
                "https://openjdk.java.net/legal/gplv2+ce.html"),

            new Dependency("org.controlsfx", "controlsfx", "11.2.2", true,
                "https://github.com/controlsfx/controlsfx", "The 3-Clause BSD License",
                "http://www.opensource.org/licenses/bsd-license.php"),

            new Dependency("org.junit.jupiter", "junit", "5.12.2", true, "https://github.com/junit-team/junit5",
                "Eclipse Public License - v 2.0", "https://www.eclipse.org/legal/epl-v20.html"),

            new Dependency("org.apache.poi", "poi", "5.4.1", false, "https://poi.apache.org/",
                "Apache License, Version 2.0", "https://www.apache.org/licenses/LICENSE-2.0"),

            new Dependency("org.freemarker", "freemarker", "2.3.34", false, "https://freemarker.apache.org/",
                "Apache License, Version 2.0", "https://www.apache.org/licenses/LICENSE-2.0"),

            new Dependency("ch.qos.logback", "logback", "1.5.18", true, "http://logback.qos.ch",
                "GNU Lesser General Public License", "http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html"),

            new Dependency("cn.hutool", "hutool", "5.8.37", true, "https://github.com/dromara/hutool",
                "Mulan Permissive Software License，Version 2", "https://license.coscl.org.cn/MulanPSL2"),

            new Dependency("org.apache.commons", "commons-lang3", "3.17.0", true,
                "https://commons.apache.org/proper/commons-lang/", "Apache License, Version 2.0",
                "https://www.apache.org/licenses/LICENSE-2.0"),

            new Dependency("org.apache.commons", "commons-email", "1.6.0", true,
                "https://commons.apache.org/proper/commons-email/", "Apache License, Version 2.0",
                "https://www.apache.org/licenses/LICENSE-2.0"),

            new Dependency("org.apache.commons", "commons-csv", "1.13.0", false,
                "https://commons.apache.org/proper/commons-csv/", "Apache License, Version 2.0",
                "https://www.apache.org/licenses/LICENSE-2.0"),

            new Dependency("org.apache.commons", "commons-configuration2", "2.11.0", true,
                "https://commons.apache.org/proper/commons-configuration/", "Apache License, Version 2.0",
                "https://www.apache.org/licenses/LICENSE-2.0"),

            new Dependency("commons-io", "commons-io", "2.19.0", true,
                "https://commons.apache.org/proper/commons-io/", "Apache License, Version 2.0",
                "https://www.apache.org/licenses/LICENSE-2.0"),

            new Dependency("org.dom4j", "dom4j", "2.1.4", false, "http://dom4j.github.io/", "Plexus",
                "https://github.com/dom4j/dom4j/blob/master/LICENSE"),

            new Dependency("io.github.java-diff-utils", "java-diff-utils", "4.15", false,
                "https://github.com/java-diff-utils/java-diff-utils", "Apache License, Version 2.0",
                "https://www.apache.org/licenses/LICENSE-2.0"),

            new Dependency("org.reflections", "reflections", "0.10.2", true,
                "http://github.com/ronmamo/reflections", "WTFPL", "http://www.wtfpl.net/"),

            new Dependency("io.github.classgraph", "classgraph", "4.8.179", false,
                "https://github.com/classgraph/classgraph", "MIT License",
                "http://opensource.org/licenses/MIT"),

            new Dependency("com.google.guava", "guava", "33.4.7-jre", false, "https://github.com/google/guava",
                "Apache License, Version 2.0", "https://www.apache.org/licenses/LICENSE-2.0"),

            new Dependency("com.github.ben-manes.caffeine", "caffeine", "3.2.0", false,
                "https://github.com/ben-manes/caffeine", "Apache License, Version 2.0",
                "https://www.apache.org/licenses/LICENSE-2.0"),

            new Dependency("org.apache.pdfbox", "pdfbox", "3.0.4", false, "https://pdfbox.apache.org/",
                "Apache License, Version 2.0", "https://www.apache.org/licenses/LICENSE-2.0"),

            new Dependency("technology.tabula", "tabula", "1.0.5", false,
                "https://github.com/tabulapdf/tabula-java", "MIT License",
                "http://www.opensource.org/licenses/mit-license.php"),

            new Dependency("com.fasterxml.jackson.core", "jackson", "2.18.3", false,
                "https://github.com/FasterXML/jackson", "Apache License, Version 2.0",
                "https://www.apache.org/licenses/LICENSE-2.0"),

            new Dependency("org.testfx", "testfx", "4.0.18", false, "https://github.com/TestFX/TestFX", "EUPL",
                "http://ec.europa.eu/idabc/eupl.html"),

            new Dependency("org.kordamp.bootstrapfx", "bootstrapfx", "0.4.0", false,
                "https://github.com/kordamp/bootstrapfx", "MIT License",
                "https://spdx.org/licenses/MIT.html"),

            new Dependency("org.kordamp.ikonli", "ikonli", "12.3.1", false, "https://github.com/kordamp/ikonli",
                "Apache-2.0", "https://spdx.org/licenses/Apache-2.0.html"),

            new Dependency("com.dlsc.pdfviewfx", "pdfviewfx", "3.1.0", false,
                "https://github.com/dlsc-software-consulting-gmbh/PDFViewFX", "Apache License, Version 2.0",
                "https://www.apache.org/licenses/LICENSE-2.0"),

            new Dependency("org.fxmisc.richtext", "richtextfx", "0.11.5", false,
                "https://github.com/FXMisc/RichTextFX", "The BSD 2-Clause License",
                "http://opensource.org/licenses/BSD-2-Clause"),

            new Dependency("org.apache.groovy", "groovy", "4.0.26", false, "https://groovy-lang.org",
                "Apache License, Version 2.0", "https://www.apache.org/licenses/LICENSE-2.0"),

            new Dependency("com.sikulix", "sikulixapi", "2.0.5", false, "https://github.com/RaiMan/SikuliX1",
                "MIT License", "http://www.sikulix.com/disclaimer/#license"),

            new Dependency("com.googlecode.aviator", "aviator", "5.4.3", false,
                "https://github.com/killme2008/aviator", "GNU LESSER GENERAL PUBLIC LICENSE",
                "http://www.gnu.org/licenses/lgpl.html"),

            new Dependency("net.coobird", "thumbnailator", "0.4.20", false,
                "https://github.com/coobird/thumbnailator", "MIT License",
                "https://spdx.org/licenses/MIT.html"),

            new Dependency("com.dlsc.preferencesfx", "preferencesfx", "11.17.0", false,
                "https://github.com/dlsc-software-consulting-gmbh/PreferencesFX", "Apache License, Version 2.0",
                "https://www.apache.org/licenses/LICENSE-2.0"),

            new Dependency("com.dlsc.formsfx", "formsfx", "11.6.0", false,
                "https://github.com/dlsc-software-consulting-gmbh/FormsFX", "Apache License, Version 2.0",
                "https://www.apache.org/licenses/LICENSE-2.0"),

            new Dependency("com.yahoo.platform.yui", "yuicompressor", "2.4.8", false,
                "https://github.com/yui/yuicompressor", "BSD License",
                "https://github.com/yui/yuicompressor/blob/master/LICENSE.TXT"),

            new Dependency("com.sun.xml.bind", "jaxb", "4.0.5", false, "https://github.com/eclipse-ee4j/jaxb-ri",
                "BSD-3-Clause license", "http://www.eclipse.org/org/documents/edl-v10.php"),

            new Dependency("org.python", "jython", "2.7.4", false, "https://github.com/jython/jython",
                "PSF license", "https://raw.githubusercontent.com/jython/jython/v2.7.4/LICENSE.txt"),

            new Dependency("net.bytebuddy", "byte-buddy", "1.17.5", false, "https://bytebuddy.net/",
                "Apache License, Version 2.0", "https://www.apache.org/licenses/LICENSE-2.0"),

            new Dependency("net.sourceforge.tess4j", "tess4j", "5.15.0", false, "https://github.com/nguyenq/tess4j",
                "Apache License, Version 2.0", "https://www.apache.org/licenses/LICENSE-2.0"),

            new Dependency("com.gluonhq", "rich-text-area", "1.2.1", false,
                "https://github.com/gluonhq/rich-text-area",
                "GPL-3.0 license", "http://www.gnu.org/licenses/gpl-3.0.html"),

            new Dependency("io.github.typhon0", "AnimateFX", "1.3.0", false,
                "https://github.com/Typhon0/AnimateFX",
                "Apache-2.0 license", "https://www.apache.org/licenses/LICENSE-2.0"),

            new Dependency("com.github.oshi", "oshi-core-java11", "6.8.0", false,
                "https://github.com/oshi/oshi",
                "MIT license", "https://opensource.org/licenses/MIT"),

            new Dependency("org.checkerframework", "checker-qual", "3.49.2", true,
                "https://github.com/typetools/checker-framework",
                "MIT License", "http://opensource.org/licenses/MIT"),

            new Dependency("org.jfxtras", "jfxtras", "17-r1", false,
                "https://jfxtras.org/",
                "The 3-Clause BSD License", "http://www.opensource.org/licenses/bsd-license.php"),

            new Dependency("net.lingala.zip4j", "zip4j", "2.11.5", false,
                "https://github.com/srikanth-lingala/zip4j",
                "Apache License, Version 2.0", "https://www.apache.org/licenses/LICENSE-2.0.txt"),

            new Dependency("net.sourceforge.plantuml", "plantuml", "1.2025.2", false,
                "https://plantuml.com/",
                "MIT License", "https://opensource.org/license/mit/"),

            new Dependency("fr.brouillard.oss", "cssfx", "11.5.1", true,
                "https://github.com/mcfoggy/cssfx",
                "Apache License, Version 2.0", "https://www.apache.org/licenses/LICENSE-2.0.txt"),

            new Dependency("org.apache.commons", "commons-compress", "1.27.1", false,
                "https://commons.apache.org/proper/commons-compress/", "Apache License, Version 2.0",
                "https://www.apache.org/licenses/LICENSE-2.0"),

            new Dependency("org.apache.commons", "commons-imaging", "1.0.0-alpha5", false,
                "https://commons.apache.org/proper/commons-imaging/", "Apache License, Version 2.0",
                "https://www.apache.org/licenses/LICENSE-2.0"),

            new Dependency("net.ifok.image", "image4j", "0.7.2", false,
                "https://image4j.sourceforge.net/", "GNU LGPL",
                "https://opensource.org/license/lgpl-3-0"),

            new Dependency("net.jonathangiles.tools", "teenyhttpd", "1.0.5", false,
                "https://github.com/JonathanGiles/TeenyHttpd", "MIT License",
                "http://www.opensource.org/licenses/mit-license.php"),

            new Dependency("com.cedarsoftware", "java-util", "3.3.0", false,
                "https://github.com/jdereg/java-util", "Apache License, Version 2.0",
                "https://www.apache.org/licenses/LICENSE-2.0"),

            new Dependency("com.fathzer", "javaluator", "3.0.5", false,
                "https://javaluator.fathzer.com", "Apache License, Version 2.0",
                "https://www.apache.org/licenses/LICENSE-2.0"),

            new Dependency("com.github.gino0631", "icns-core", "1.2", false,
                "https://github.com/gino0631/icns", "GNU LGPL",
                "https://opensource.org/license/lgpl-3-0"));
    }

    public static class Dependency {

        private final SimpleStringProperty group;
        private final SimpleStringProperty artifact;
        private final SimpleStringProperty version;
        private final SimpleBooleanProperty inUsed;
        private final SimpleStringProperty url;
        private final SimpleStringProperty license;
        private final SimpleStringProperty licenseUrl;

        public Dependency(String group, String artifact, String version, boolean inUsed, String url, String license,
            String licenseUrl) {
            this.group = new SimpleStringProperty(group);
            this.artifact = new SimpleStringProperty(artifact);
            this.version = new SimpleStringProperty(version);
            this.inUsed = new SimpleBooleanProperty(inUsed);
            this.url = new SimpleStringProperty(url);
            this.license = new SimpleStringProperty(license);
            this.licenseUrl = new SimpleStringProperty(licenseUrl);
        }

        public SimpleStringProperty group() {
            return group;
        }

        public SimpleStringProperty artifact() {
            return artifact;
        }

        public SimpleStringProperty version() {
            return version;
        }

        public SimpleBooleanProperty inUsed() {
            return inUsed;
        }

        public SimpleStringProperty url() {
            return url;
        }

        public SimpleStringProperty license() {
            return license;
        }

        public SimpleStringProperty licenseUrl() {
            return licenseUrl;
        }

        public String getGroup() {
            return group.get();
        }

        public void setGroup(String group) {
            this.group.set(group);
        }

        public String getArtifact() {
            return artifact.get();
        }

        public void setArtifact(String artifact) {
            this.artifact.set(artifact);
        }

        public String getVersion() {
            return version.get();
        }

        public void setVersion(String version) {
            this.version.set(version);
        }

        public Boolean getInUsed() {
            return inUsed.get();
        }

        public void setInUsed(boolean inUsed) {
            this.inUsed.set(inUsed);
        }

        public String getUrl() {
            return url.get();
        }

        public void setUrl(String url) {
            this.url.set(url);
        }

        public String getLicense() {
            return license.get();
        }

        public void setLicense(String license) {
            this.license.set(license);
        }

        public String getLicenseUrl() {
            return licenseUrl.get();
        }

        public void setLicenseUrl(String licenseUrl) {
            this.licenseUrl.set(licenseUrl);
        }
    }
}
