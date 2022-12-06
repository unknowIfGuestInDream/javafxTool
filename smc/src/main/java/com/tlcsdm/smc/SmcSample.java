package com.tlcsdm.smc;

import com.tlcsdm.core.javafx.util.Config;
import com.tlcsdm.frame.SampleBase;

public abstract class SmcSample extends SampleBase {

	@Override
	public String getProjectName() {
		return "smc";
	}

	@Override
	public String getProjectVersion() {
		return Config.JAVAFX_TOOL_VERSION;
	}

}
