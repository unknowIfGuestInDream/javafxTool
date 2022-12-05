package com.tlcsdm.smc;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import com.tlcsdm.core.javafx.util.Config;
import com.tlcsdm.frame.SampleBase;

public abstract class SmcSample extends SampleBase {

	// 项目信息
	private static final ProjectInfo PROJECTINFO = new ProjectInfo();

	@Override
	public String getProjectName() {
		return "smc";
	}

	@Override
	public String getProjectVersion() {
		return Config.JAVAFX_TOOL_VERSION;
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
				// Manifest-Version
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
