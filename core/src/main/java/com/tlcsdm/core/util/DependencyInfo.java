package com.tlcsdm.core.util;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.List;

/**
 * 项目总体依赖信息
 * 可能需要手动维护，用于应用项目获取相应数据使用(需要应用模块过滤)
 *
 * @author: unknowIfGuestInDream
 * @date: 2023/8/3 17:05
 */
public class DependencyInfo {

    public static List<Dependency> getDependencyList() {
        return SingletonInstance.INSTANCE;
    }

    private static class SingletonInstance {
        private static final List<Dependency> INSTANCE = List.of(
            new Dependency("org.openjfx", "javafx", "17.0.8", true, "https://github.com/openjdk/jfx", "GPLv2+CE",
                "https://openjdk.java.net/legal/gplv2+ce.html"),

            new Dependency("org.controlsfx", "controlsfx", "11.1.2", true, "https://github.com/controlsfx/controlsfx",
                "The 3-Clause BSD License", "http://www.opensource.org/licenses/bsd-license.php"),

            new Dependency("org.junit.jupiter", "junit", "5.10.0", true, "https://github.com/junit-team/junit5",
                "Eclipse Public License - v 2.0", "https://www.eclipse.org/legal/epl-v20.html"),

            new Dependency("org.apache.poi", "poi", "5.2.3", false, "https://poi.apache.org/",
                "Apache License, Version 2.0", "http://www.apache.org/licenses/LICENSE-2.0.txt"),

            new Dependency("org.freemarker", "freemarker", "2.3.32", false, "https://freemarker.apache.org/",
                "Apache License, Version 2.0", "http://www.apache.org/licenses/LICENSE-2.0.txt"),

            new Dependency("ch.qos.logback", "logback", "1.4.8", true, "http://logback.qos.ch",
                "GNU Lesser General Public License", "http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html"),

            new Dependency("cn.hutool", "hutool", "5.8.21", true, "https://github.com/dromara/hutool",
                "Mulan Permissive Software License，Version 2", "https://license.coscl.org.cn/MulanPSL2"),

            new Dependency("org.apache.commons", "commons-lang3", "3.13.0", true,
                "https://commons.apache.org/proper/commons-lang/", "Apache License, Version 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0.txt"),

            new Dependency("org.apache.commons", "commons-configuration2", "2.9.0", true,
                "https://commons.apache.org/proper/commons-configuration/", "Apache License, Version 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0.txt"),

            new Dependency("commons-io", "commons-io", "2.13.0", true, "https://commons.apache.org/proper/commons-io/",
                "Apache License, Version 2.0", "http://www.apache.org/licenses/LICENSE-2.0"),

            new Dependency("org.dom4j", "dom4j", "2.1.4", false, "http://dom4j.github.io/", "Plexus",
                "https://github.com/dom4j/dom4j/blob/master/LICENSE"),

            new Dependency("io.github.java-diff-utils", "java-diff-utils", "4.12", false,
                "https://github.com/java-diff-utils/java-diff-utils", "Apache License, Version 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0"),

            new Dependency("org.reflections", "reflections", "0.10.2", true, "http://github.com/ronmamo/reflections",
                "WTFPL", "http://www.wtfpl.net/"),

            new Dependency("com.google.guava", "guava", "32.1.2-jre", false, "https://github.com/google/guava",
                "Apache License, Version 2.0", "http://www.apache.org/licenses/LICENSE-2.0"),

            new Dependency("com.github.ben-manes.caffeine", "caffeine", "3.1.7", false,
                "https://github.com/ben-manes/caffeine", "Apache License, Version 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0.txt"),

            new Dependency("org.apache.pdfbox", "pdfbox", "2.0.29", false, "https://pdfbox.apache.org/",
                "Apache License, Version 2.0", "http://www.apache.org/licenses/LICENSE-2.0"),

            new Dependency("com.fasterxml.jackson.core", "jackson", "2.15.2", false,
                "https://github.com/FasterXML/jackson", "Apache License, Version 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0"),

            new Dependency("org.testfx", "testfx", "4.0.16-alpha", false, "https://github.com/TestFX/TestFX", "EUPL",
                "http://ec.europa.eu/idabc/eupl.html"),

            new Dependency("org.kordamp.bootstrapfx", "bootstrapfx", "0.4.0", false,
                "https://github.com/kordamp/bootstrapfx", "MIT License",
                "https://github.com/kordamp/bootstrapfx/blob/master/LICENSE"),

            new Dependency("org.kordamp.ikonli", "ikonli", "12.3.1", false, "https://github.com/kordamp/ikonli",
                "Apache-2.0", "https://spdx.org/licenses/Apache-2.0.html"),

            new Dependency("org.fxmisc.richtext", "richtextfx", "0.11.0", false, "https://github.com/FXMisc/RichTextFX",
                "The BSD 2-Clause License", "http://opensource.org/licenses/BSD-2-Clause"),

            new Dependency("org.apache.groovy", "groovy", "4.0.13", false, "https://groovy-lang.org",
                "Apache License, Version 2.0", "http://www.apache.org/licenses/LICENSE-2.0"),

            new Dependency("com.sikulix", "sikulixapi", "2.0.5", false, "https://github.com/RaiMan/SikuliX1",
                "MIT License", "http://www.sikulix.com/disclaimer/#license"),

            new Dependency("com.googlecode.aviator", "aviator", "5.3.3", false, "https://github.com/killme2008/aviator",
                "GNU LESSER GENERAL PUBLIC LICENSE", "http://www.gnu.org/licenses/lgpl.html"));
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
