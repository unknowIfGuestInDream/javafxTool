package com.tlcsdm.demo;

import com.tlcsdm.frame.CenterPanelService;
import com.tlcsdm.frame.Sample;
import com.tlcsdm.frame.SampleBase;
import com.tlcsdm.frame.model.Project;
import com.tlcsdm.frame.model.WelcomePage;
import javafx.concurrent.Worker;
import javafx.scene.Node;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author: 唐 亮
 * @date: 2022/10/22 21:00
 */
public class FXCenterPanelService implements CenterPanelService {

    private static final String TAB_LOAD_CACHE = "TAB_LOAD_CACHE";
    private ControlsFXSample selectedSample;
    private Project selectedProject;
    private Stage stage;

    private TabPane tabPane;
    private Tab welcomeTab;
    private Tab sampleTab;
    private Tab javaDocTab;
    private Tab sourceTab;
    private Tab cssTab;

    private ProgressIndicator progressIndicator;
    private StackPane progressIndicatorPane;

    private WebView javaDocWebView;
    private WebView sourceWebView;
    private WebView cssWebView;

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

        sampleTab = new Tab("Sample");
        javaDocTab = new Tab("JavaDoc");
        javaDocWebView = new WebView();
        javaDocTab.setContent(javaDocWebView);

        sourceTab = new Tab("Source");
        sourceWebView = new WebView();
        sourceTab.setContent(sourceWebView);

        cssTab = new Tab("Css");
        cssWebView = new WebView();
        cssTab.setContent(cssWebView);

        return tabPane;
    }

    @Override
    public void changeSample() {
        if (tabPane.getTabs().contains(welcomeTab)) {
            tabPane.getTabs().setAll(sampleTab, javaDocTab, sourceTab, cssTab);
        }
        tabPane.getTabs().forEach(tab -> tab.getProperties().put(TAB_LOAD_CACHE, false));
    }

    @Override
    public void updateSampleChild(Sample selectedSample, Project selectedProject) {
        this.selectedSample = (ControlsFXSample) selectedSample;
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
        } else if (selectedTab == javaDocTab) {
            prepareTabContent(javaDocTab, javaDocWebView);
            loadWebViewContent(javaDocWebView, selectedSample, ControlsFXSample::getJavaDocURL, sample -> "No Javadoc available");
        } else if (selectedTab == sourceTab) {
            prepareTabContent(sourceTab, sourceWebView);
            loadWebViewContent(sourceWebView, selectedSample, ControlsFXSample::getSampleSourceURL, this::formatSourceCode);
        } else if (selectedTab == cssTab) {
            prepareTabContent(cssTab, cssWebView);
            loadWebViewContent(cssWebView, selectedSample, ControlsFXSample::getControlStylesheetURL, this::formatCss);
        }
    }

    private void prepareTabContent(Tab tab, WebView webView) {
        tab.setContent(progressIndicatorPane);
        final WebEngine engine = webView.getEngine();
        progressIndicator.progressProperty().bind(engine.getLoadWorker().progressProperty());
        engine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                tab.setContent(webView);
                tab.getProperties().put(TAB_LOAD_CACHE, true);
            }
        });
    }

    private void loadWebViewContent(WebView webView, ControlsFXSample sample, Function<ControlsFXSample, String> urlFunction,
                                    Function<ControlsFXSample, String> contentFunction) {
        final String url = urlFunction.apply(sample);
        if (url != null && url.startsWith("http")) {
            webView.getEngine().load(url);
        } else {
            webView.getEngine().loadContent(contentFunction.apply(sample));
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

    private String formatSourceCode(ControlsFXSample sample) {
        String sourceURL = sample.getSampleSourceURL();
        String src;
        if (sourceURL == null) {
            src = "No sample source available";
        } else {
            src = "Sample Source not found";
            try {
                src = getSourceCode(sample);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // Escape '<' by "&lt;" to ensure correct rendering by SyntaxHighlighter
        src = src.replace("<", "&lt;");

        String template = getResource("/com/tlcsdm/demo/util/SourceCodeTemplate.html", null);
        return template.replace("<source/>", src);
    }

    private String getSourceCode(ControlsFXSample sample) {
        String sourceURL = sample.getSampleSourceURL();
        try {
            // try loading via the web or local file system
            URL url = new URL(sourceURL);
            InputStream is = url.openStream();
            return getResource(is);
        } catch (IOException e) {
            // no-op - the URL may not be valid, no biggy
        }
        return getResource(sourceURL, sample.getClass());
    }

    private String formatCss(ControlsFXSample sample) {
        String cssUrl = sample.getControlStylesheetURL();
        String src;
        if (cssUrl == null) {
            src = "No CSS source available";
        } else {
            src = "Css not found";
            try {
                if (selectedProject != null && !selectedProject.getModuleName().isEmpty()) {
                    // module-path
                    final Optional<Module> projectModuleOptional = ModuleLayer.boot()
                            .findModule(selectedProject.getModuleName());
                    if (projectModuleOptional.isPresent()) {
                        final Module projectModule = projectModuleOptional.get();
                        src = getResource(projectModule.getResourceAsStream(cssUrl));
                    } else {
                        System.err.println("Module name defined doesn't exist");
                    }
                } else {
                    // classpath
                    src = getResource(getClass().getResourceAsStream(cssUrl));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // Escape '<' by "&lt;" to ensure correct rendering by SyntaxHighlighter
        src = src.replace("<", "&lt;");

        String template = getResource("/com/tlcsdm/demo/util/CssTemplate.html", null);
        return template.replace("<source/>", src);
    }

    private Node buildSampleTabContent(Sample sample) {
        return SampleBase.buildSample(sample, stage);
    }
}
