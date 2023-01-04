package com.tlcsdm.smc;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import com.tlcsdm.core.javafx.util.Config;
import com.tlcsdm.core.javafx.util.FxXmlUtil;
import com.tlcsdm.frame.SampleBase;

import cn.hutool.core.util.StrUtil;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.TextInputControl;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

public abstract class SmcSample extends SampleBase {

	protected Map<String, Object> userData = new LinkedHashMap<>();

	@Override
	public String getProjectName() {
		return "smc";
	}

	@Override
	public String getProjectVersion() {
		return Config.JAVAFX_TOOL_VERSION;
	}

	@Override
	public void initialize() {
		initializeUserData();
	}

	/**
	 * Because initialize() is called after getPanel() so userData needs to be
	 * initialized in getPanel()
	 */
	protected void initializeUserData() {
		if (!FxXmlUtil.hasKey(getSampleXmlPrefix(), "id")) {
			return;
		}
		userData.forEach((key, value) -> {
			String k = getSampleXmlPrefix() + "." + key;
			String val = FxXmlUtil.get(k, "");
			if (value instanceof FileChooser v) {
				v.setInitialDirectory(new File(val));
			} else if (value instanceof DirectoryChooser v) {
				v.setInitialDirectory(new File(val));
			} else if (value instanceof TextInputControl v) {
				v.setText(val);
			} else if (value instanceof CheckBox v) {
				v.setSelected(Boolean.parseBoolean(val));
			} else if (value instanceof ComboBoxBase v) {
				v.setValue(val);
			} else {
				// do nothing
			}
		});
	}

	/**
	 * Manually call the current method after the function completes For example
	 * manually calling the current method after clicking the generate button
	 */
	protected void bindUserData() {
		if (userData.size() == 0 || StrUtil.isEmpty(getSampleId())) {
			return;
		}
		FxXmlUtil.set(getSampleXmlPrefix(), "id", getSampleId());
		FxXmlUtil.set(getSampleXmlPrefix(), "version", getSampleVersion());
		userData.forEach((key, value) -> {
			String k = getSampleXmlPrefix() + "." + key;
			if (value instanceof FileChooser v) {
				FxXmlUtil.set(k, v.getInitialDirectory());
			} else if (value instanceof DirectoryChooser v) {
				FxXmlUtil.set(k, v.getInitialDirectory());
			} else if (value instanceof TextInputControl v) {
				FxXmlUtil.set(k, v.getText());
			} else if (value instanceof CheckBox v) {
				FxXmlUtil.set(k, v.isSelected());
			} else if (value instanceof ComboBoxBase v) {
				FxXmlUtil.set(k, v.getValue());
			} else {
				// do nothing
			}
		});
	}

	protected String getSampleXmlPrefix() {
		return getProjectName() + "." + getSampleId();
	}

}
