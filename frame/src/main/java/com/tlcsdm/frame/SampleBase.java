/**
 * Copyright (c) 2013, 2020, ControlsFX
 * All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of ControlsFX, any associated website, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL CONTROLSFX BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.tlcsdm.frame;

import java.io.File;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;

import com.tlcsdm.core.javafx.util.FxXmlUtil;
import com.tlcsdm.frame.util.I18nUtils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.DES;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * A base class for samples - it is recommended that they extend this class
 * rather than Application, as then the samples can be run either standalone or
 * within FXSampler.
 */
public abstract class SampleBase extends Application implements Sample {

	protected Map<String, Object> userData = new LinkedHashMap<>();
	protected String aesSeed = "3f4eefd3525675154a5e3a0183d8087b";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start(Stage primaryStage) {
		ServiceLoader<FXSamplerConfiguration> configurationServiceLoader = ServiceLoader
				.load(FXSamplerConfiguration.class);

		primaryStage.setTitle(getSampleName());

		Scene scene = new Scene((Parent) buildSample(this, primaryStage), 800, 800);
		scene.getStylesheets()
				.add(Objects.requireNonNull(SampleBase.class.getResource("/fxsampler/fxsampler.css")).toExternalForm());
		for (FXSamplerConfiguration fxsamplerConfiguration : configurationServiceLoader) {
			String stylesheet = fxsamplerConfiguration.getSceneStylesheet();
			if (stylesheet != null) {
				scene.getStylesheets().add(stylesheet);
			}
		}
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isVisible() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Node getControlPanel() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getControlPanelDividerPosition() {
		return 0.6;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getSampleDescription() {
		return "";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getProjectName() {
		return "ControlsFX";
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
				if (!StrUtil.isEmpty(val)) {
					v.setInitialDirectory(new File(val));
				}
			} else if (value instanceof DirectoryChooser v) {
				if (!StrUtil.isEmpty(val)) {
					v.setInitialDirectory(new File(val));
				}
			} else if (value instanceof PasswordField v) {
				if (!StrUtil.isEmpty(val)) {
					DES des = SecureUtil.des(aesSeed.getBytes());
					v.setText(des.decryptStr(val));
				}
			} else if (value instanceof TextInputControl v) {
				v.setText(val);
			} else if (value instanceof CheckBox v) {
				v.setSelected(Boolean.parseBoolean(val));
			} else if (value instanceof DatePicker v) {
				if (!StrUtil.isEmpty(val)) {
					v.setValue(LocalDate.parse(val));
				}
			} else if (value instanceof String v) {
				v = val;
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
			} else if (value instanceof PasswordField v) {
				DES des = SecureUtil.des(aesSeed.getBytes());
				FxXmlUtil.set(k, des.encryptHex(v.getText()));
			} else if (value instanceof TextInputControl v) {
				FxXmlUtil.set(k, v.getText());
			} else if (value instanceof CheckBox v) {
				FxXmlUtil.set(k, v.isSelected());
			} else if (value instanceof DatePicker v) {
				FxXmlUtil.set(k, v.getValue());
			} else if (value instanceof String v) {
				FxXmlUtil.set(k, v);
			} else {
				// do nothing
			}
		});
	}

	protected String getSampleXmlPrefix() {
		return getProjectName() + "." + getSampleId();
	}

	/**
	 * Utility method to create the default look for samples.
	 */
	public static Node buildSample(Sample sample, Stage stage) {
		SplitPane splitPane = new SplitPane();
		// we guarantee that the build order is panel then control panel.
		final Node samplePanel = sample.getPanel(stage);
		final Node controlPanel = sample.getControlPanel();
		sample.initialize();
		splitPane.setDividerPosition(0, sample.getControlPanelDividerPosition());

		if (samplePanel != null) {
			samplePanel.getStyleClass().add("center-panel");
			splitPane.getItems().add(samplePanel);
		}

		final VBox rightPanel = new VBox();
		rightPanel.getStyleClass().add("right-panel");
		rightPanel.setMaxHeight(Double.MAX_VALUE);

		boolean addRightPanel = false;

		Label sampleName = new Label(sample.getSampleName());
		sampleName.getStyleClass().add("sample-name");
		rightPanel.getChildren().add(sampleName);

		// --- project name & version
		String version = sample.getProjectVersion();
		version = version == null ? "" : "@version@".equals(version) ? "" : " " + version.trim();

		final String projectName = sample.getProjectName() + version;
		if (!projectName.isEmpty()) {
			Label projectNameTitleLabel = new Label(I18nUtils.get("frame.sample.rightPanel.project") + ": ");
			projectNameTitleLabel.getStyleClass().add("project-name-title");

			Label projectNameLabel = new Label(projectName);
			projectNameLabel.getStyleClass().add("project-name");
			projectNameLabel.setWrapText(true);

			TextFlow textFlow = new TextFlow(projectNameTitleLabel, projectNameLabel);
			rightPanel.getChildren().add(textFlow);
		}

		// --- sample version
		final String sampleVersion = sample.getSampleVersion();
		if (sampleVersion != null && !sampleVersion.isEmpty()) {
			Label sampleVersionLabel = new Label(
					I18nUtils.get("frame.sample.rightPanel.sampleVersion") + ": " + sampleVersion);
			sampleVersionLabel.getStyleClass().add("sampleVersion");
			sampleVersionLabel.setWrapText(true);
			rightPanel.getChildren().add(sampleVersionLabel);
			addRightPanel = true;
		}

		// --- description
		final String description = sample.getSampleDescription();
		if (description != null && !description.isEmpty()) {
			Label descriptionLabel = new Label(description);
			descriptionLabel.getStyleClass().add("description");
			descriptionLabel.setWrapText(true);
			rightPanel.getChildren().add(descriptionLabel);

			addRightPanel = true;
		}

		if (controlPanel != null) {
			rightPanel.getChildren().add(new Separator());

			controlPanel.getStyleClass().add("control-panel");
			rightPanel.getChildren().add(controlPanel);
			VBox.setVgrow(controlPanel, Priority.ALWAYS);
			addRightPanel = true;
		}

		if (addRightPanel) {
			ScrollPane scrollPane = new ScrollPane(rightPanel);
			scrollPane.setMaxHeight(Double.MAX_VALUE);
			scrollPane.setFitToWidth(true);
			scrollPane.setFitToHeight(true);
			SplitPane.setResizableWithParent(scrollPane, false);
			splitPane.getItems().add(scrollPane);
		}

		return splitPane;
	}

	@Override
	public void dispose() {
	}
}
