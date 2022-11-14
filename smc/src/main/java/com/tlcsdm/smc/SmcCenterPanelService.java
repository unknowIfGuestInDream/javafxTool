package com.tlcsdm.smc;

import com.tlcsdm.frame.CenterPanelService;
import com.tlcsdm.frame.Sample;
import com.tlcsdm.frame.SampleBase;
import com.tlcsdm.frame.model.Project;
import com.tlcsdm.frame.model.WelcomePage;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author: 唐 亮
 * @date: 2022/10/22 21:00
 */
public class SmcCenterPanelService implements CenterPanelService {

	private SmcSample selectedSample;
	private Project selectedProject;
	private Stage stage;
	private ScrollPane scrollPane;

	@Override
	public Node getCenterPanel(Stage stage) {
		scrollPane = new ScrollPane();
		this.stage = stage;
		scrollPane.setMaxHeight(Double.MAX_VALUE);
		scrollPane.setFitToWidth(true);
		scrollPane.setFitToHeight(true);

		GridPane.setHgrow(scrollPane, Priority.ALWAYS);
		GridPane.setVgrow(scrollPane, Priority.ALWAYS);

		return scrollPane;
	}

	@Override
	public void changeSample() {
		// Do nothing
	}

	@Override
	public void updateSampleChild(Sample selectedSample, Project selectedProject) {
		this.selectedSample = (SmcSample) selectedSample;
		this.selectedProject = selectedProject;
		updateContent();
	}

	@Override
	public void handleWelcomePage(WelcomePage wPage) {
		scrollPane.setContent(wPage.getContent());
	}

	private void updateContent() {
		if (selectedSample == null) {
			return;
		}
		prepareContent(scrollPane);
		scrollPane.setContent(buildSampleTabContent(selectedSample));
	}

	/**
	 * 加载content前初始化
	 */
	private void prepareContent(ScrollPane scrollPane) {
		// Do nothing
	}

	private String getResource(String resourceName, Class<?> baseClass) {
		Class<?> clz = baseClass == null ? getClass() : baseClass;
		return getResource(clz.getResourceAsStream(resourceName));
	}

	private String getResource(InputStream is) {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	private Node buildSampleTabContent(Sample sample) {
		return SampleBase.buildSample(sample, stage);
	}
}
