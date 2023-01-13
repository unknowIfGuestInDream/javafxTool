package com.tlcsdm.frame.model;

import com.tlcsdm.frame.CenterPanelService;
import com.tlcsdm.frame.Sample;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * @author: unknowIfGuestInDream
 * @date: 2022/10/22 21:27
 */
public class EmptyCenterPanel implements CenterPanelService {

	private TabPane tabPane;
	private Tab sampleTab;
	private Tab welcomeTab;

	@Override
	public Node getCenterPanel() {
		tabPane = new TabPane();
		tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
		tabPane.getStyleClass().add(TabPane.STYLE_CLASS_FLOATING);
		tabPane.getSelectionModel().selectedItemProperty().addListener(o -> updateTab());
		GridPane.setHgrow(tabPane, Priority.ALWAYS);
		GridPane.setVgrow(tabPane, Priority.ALWAYS);

		sampleTab = new Tab("Sample");

		return tabPane;
	}

	@Override
	public void changeSample() {
		if (tabPane.getTabs().contains(welcomeTab)) {
			tabPane.getTabs().setAll(sampleTab);
		}
	}

	@Override
	public void updateSampleChild(Sample selectedSample, Project selectedProject) {
		updateTab();
	}

	@Override
	public void handleWelcomePage(WelcomePage wPage) {
		welcomeTab = new Tab(wPage.getTitle());
		welcomeTab.setContent(wPage.getContent());
		tabPane.getTabs().setAll(welcomeTab);
	}

	private void updateTab() {
	}
}
