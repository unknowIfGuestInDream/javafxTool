package com.tlcsdm.smc;

import com.tlcsdm.frame.CenterPanelService;
import com.tlcsdm.frame.Sample;
import com.tlcsdm.frame.SampleBase;
import com.tlcsdm.frame.model.Project;
import com.tlcsdm.frame.model.WelcomePage;
import javafx.scene.Node;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
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

    private static final String TAB_LOAD_CACHE = "TAB_LOAD_CACHE";
    private SmcSample selectedSample;
    private Project selectedProject;
    private Stage stage;

    private TabPane tabPane;
    private Tab welcomeTab;
    private Tab sampleTab;

    private ProgressIndicator progressIndicator;
    private StackPane progressIndicatorPane;

    @Override
    public Node setCenterPanel(Node centerPanel, Stage stage) {
        centerPanel = new TabPane();
        tabPane = (TabPane) centerPanel;
        this.stage = stage;
        // ProgressIndicator
        progressIndicator = new ProgressIndicator();
        progressIndicatorPane = new StackPane(progressIndicator);

        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.getStyleClass().add(TabPane.STYLE_CLASS_FLOATING);
        tabPane.getSelectionModel().selectedItemProperty().addListener(o -> updateTab());
        GridPane.setHgrow(tabPane, Priority.ALWAYS);
        GridPane.setVgrow(tabPane, Priority.ALWAYS);

        sampleTab = new Tab("Tool");

        return tabPane;
    }

    @Override
    public void changeSample() {
        if (tabPane.getTabs().contains(welcomeTab)) {
            tabPane.getTabs().setAll(sampleTab);
        }
        tabPane.getTabs().forEach(tab -> tab.getProperties().put(TAB_LOAD_CACHE, false));
    }

    @Override
    public void updateSampleChild(Sample selectedSample, Project selectedProject) {
        this.selectedSample = (SmcSample) selectedSample;
        this.selectedProject = selectedProject;
        updateTab();
    }

    @Override
    public void handleWelcomePage(WelcomePage wPage) {
        welcomeTab = new Tab(wPage.getTitle());
        welcomeTab.setContent(wPage.getContent());
        tabPane.getTabs().setAll(welcomeTab);
    }

    private void updateTab() {
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();

        // If the tab was already loaded and its just a tab switch, no need to reload.
        final Object tabLoadCache = selectedTab.getProperties().get(TAB_LOAD_CACHE);
        if (tabLoadCache != null && (boolean) tabLoadCache) {
            return;
        }

        progressIndicator.progressProperty().unbind();
        // we only update the selected tab - leaving the other tabs in their
        // previous state until they are selected
        if (selectedTab == sampleTab) {
            if (selectedSample == null) {
                return;
            }
            sampleTab.setContent(buildSampleTabContent(selectedSample));
        }
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
