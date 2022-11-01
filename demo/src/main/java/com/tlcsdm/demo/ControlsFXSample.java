package com.tlcsdm.demo;

import com.tlcsdm.demo.samples.Utils;
import com.tlcsdm.frame.SampleBase;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public abstract class ControlsFXSample extends SampleBase {

    /**
     * 项目信息
     */
    private static final ProjectInfo projectInfo = new ProjectInfo();

    @Override
    public String getProjectName() {
        return "ControlsFX";
    }

    @Override
    public String getProjectVersion() {
        return projectInfo.getVersion();
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
     * Returns URL for control's stylesheet.
     * If the CSS resource is available on module-path,
     */
    public String getControlStylesheetURL() {
        return null;
    }

    /**
     * 项目信息
     */
    private static class ProjectInfo {

        private String version;

        public ProjectInfo() {

            InputStream s = getClass().getClassLoader().getResourceAsStream(
                    "META-INF/MANIFEST.MF");

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

