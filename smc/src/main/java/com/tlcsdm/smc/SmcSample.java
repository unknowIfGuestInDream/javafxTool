package com.tlcsdm.smc;

import cn.hutool.core.util.StrUtil;
import com.tlcsdm.core.javafx.util.Config;
import com.tlcsdm.core.javafx.util.FxXmlUtil;
import com.tlcsdm.frame.SampleBase;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.TextInputControl;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.util.HashMap;
import java.util.Map;

public abstract class SmcSample extends SampleBase {

    protected Map<String, Object> userData = new HashMap<>();

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
     * Because initialize() is called after getPanel()
     * so userData needs to be initialized in getPanel()
     */
    protected void initializeUserData() {
        if (!FxXmlUtil.hasKey(getSampleXmlPrefix(), "id")) {
            return;
        }
        //todo
    }

    /**
     * Manually call the current method after the function completes
     * For example manually calling the current method after clicking the generate button
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
