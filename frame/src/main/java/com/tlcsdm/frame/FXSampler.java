/*
 * Copyright (c) 2023 unknowIfGuestInDream.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of unknowIfGuestInDream, any associated website, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL UNKNOWIFGUESTINDREAM BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.tlcsdm.frame;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.StaticLog;
import com.tlcsdm.core.event.ApplicationExitEvent;
import com.tlcsdm.core.event.ApplicationFailedEvent;
import com.tlcsdm.core.event.ApplicationPreparedEvent;
import com.tlcsdm.core.event.ApplicationReadyEvent;
import com.tlcsdm.core.event.ApplicationRestartEvent;
import com.tlcsdm.core.event.ApplicationStartingEvent;
import com.tlcsdm.core.eventbus.EventBus;
import com.tlcsdm.core.eventbus.Subscribe;
import com.tlcsdm.core.exception.SampleDefinitionException;
import com.tlcsdm.core.factory.InitializingFactory;
import com.tlcsdm.core.factory.config.ThreadPoolTaskExecutor;
import com.tlcsdm.core.javafx.FxApp;
import com.tlcsdm.core.javafx.dialog.FxAlerts;
import com.tlcsdm.core.javafx.helper.LayoutHelper;
import com.tlcsdm.core.javafx.util.Config;
import com.tlcsdm.core.javafx.util.JavaFxSystemUtil;
import com.tlcsdm.core.javafx.util.Keys;
import com.tlcsdm.core.javafx.util.StageUtil;
import com.tlcsdm.core.util.CoreConstant;
import com.tlcsdm.core.util.DependencyUtil;
import com.tlcsdm.core.util.InterfaceScanner;
import com.tlcsdm.frame.cache.SampleCacheFactory;
import com.tlcsdm.frame.event.SplashAnimFinishedEvent;
import com.tlcsdm.frame.model.DefaultTreeViewCellFactory;
import com.tlcsdm.frame.model.EmptyCenterPanel;
import com.tlcsdm.frame.model.EmptySample;
import com.tlcsdm.frame.model.Project;
import com.tlcsdm.frame.model.SampleTree;
import com.tlcsdm.frame.model.SampleTreeViewModel;
import com.tlcsdm.frame.model.WelcomePage;
import com.tlcsdm.frame.service.BannerPrinterService;
import com.tlcsdm.frame.service.CenterPanelService;
import com.tlcsdm.frame.service.FXSamplerConfiguration;
import com.tlcsdm.frame.service.MenubarConfigration;
import com.tlcsdm.frame.service.SamplePostProcessorService;
import com.tlcsdm.frame.service.SamplesTreeViewConfiguration;
import com.tlcsdm.frame.service.SplashScreen;
import com.tlcsdm.frame.service.VersionCheckerService;
import com.tlcsdm.frame.util.I18nUtils;
import com.tlcsdm.frame.util.SampleScanner;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;

/**
 * 启动类.
 */
public final class FXSampler extends Application {

    private Map<String, Project> projectsMap;
    private static Stage stage;

    private Sample selectedSample;
    private CenterPanelService centerPanelService;

    private TreeView<Sample> samplesTreeView;
    private TreeItem<Sample> root;
    private List<TreeItem<Sample>> projects;

    private Project selectedProject;
    private final StopWatch stopWatch = new StopWatch();
    // 用于 初始化UI
    private FXSamplerConfiguration fxsamplerConfiguration;
    private MenubarConfigration menubarConfigration;
    // 闪屏部分
    private Stage loadingStage;
    private boolean animationFinished;
    private boolean supportAnim;
    private boolean hasPrepared;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) {
        stopWatch.start();
        this.initializeProperties();
        this.printBanner();
        stage = primaryStage;
        StaticLog.debug("Load splash screen.");
        EventBus.getDefault().register(this);
        loadSplash();
        StaticLog.debug("Initialize the system environment.");
        JavaFxSystemUtil.initSystemLocal();
        StaticLog.debug("Initialize system resources.");
        initializeSystem();
        EventBus.getDefault().post(new ApplicationStartingEvent());
        Platform.runLater(() -> {
            try {
                StaticLog.debug("Initialize UI resources.");
                initializeUI();
                EventBus.getDefault().post(new ApplicationPreparedEvent());
                ThreadPoolTaskExecutor.get().execute(() -> {
                    StaticLog.debug("Initialize resources.");
                    initializeSource();
                });
            } catch (Throwable e) {
                EventBus.getDefault().post(new ApplicationFailedEvent(e));
                FxAlerts.exception(e);
            }
        });
        EventBus.getDefault().post(new ApplicationReadyEvent());
    }

    /**
     * 加载闪屏功能.
     */
    private void loadSplash() {
        Parent parent = null;
        // 加载闪屏图片
        ServiceLoader<SplashScreen> splashScreens = ServiceLoader.load(SplashScreen.class);
        for (SplashScreen s : splashScreens) {
            parent = s.getParent();
            supportAnim = s.supportAnimation();
        }
        if (parent == null) {
            return;
        }
        Stage mainStage = new Stage();
        mainStage.initStyle(StageStyle.UTILITY);
        mainStage.setOpacity(0);
        mainStage.show();
        loadingStage = new Stage(supportAnim ? StageStyle.TRANSPARENT : StageStyle.UNDECORATED);
        loadingStage.initOwner(mainStage);
        Scene scene = new Scene(parent);
        scene.setFill(Color.TRANSPARENT);
        scene.setCamera(new PerspectiveCamera());
        loadingStage.setScene(scene);
        loadingStage.show();
        stage.addEventHandler(WindowEvent.WINDOW_SHOWN, event -> {
            loadingStage.close();
            mainStage.close();
        });
        stage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event -> EventBus.getDefault().post(new ApplicationExitEvent()));
    }

    /**
     * 动画闪屏时使用，判断资源类是否加载完成.
     */
    @Subscribe
    public void appPreparedHandler(ApplicationPreparedEvent event) {
        hasPrepared = true;
        if (loadingStage != null && loadingStage.isShowing() && supportAnim && animationFinished) {
            stage.show();
            stage.requestFocus();
        }
    }

    /**
     * 动画闪屏时使用，判断动画是否完成.
     */
    @Subscribe
    public void splashAnimFinishedHandler(SplashAnimFinishedEvent event) {
        animationFinished = true;
        if (loadingStage != null && loadingStage.isShowing() && hasPrepared) {
            stage.show();
            stage.requestFocus();
        }
    }

    /**
     * 初始化系统配置.
     */
    private void initializeSystem() {
        loadConfiguration();
        Image appIcon;
        if (fxsamplerConfiguration == null) {
            appIcon = LayoutHelper.icon(getClass().getResource("/fxsampler/logo.png"));
        } else {
            appIcon = fxsamplerConfiguration.getAppIcon();
        }
        FxApp.init(stage, appIcon, getHostServices());
        ServiceLoader<MenubarConfigration> menubarConfigrations = ServiceLoader.load(MenubarConfigration.class);
        for (MenubarConfigration m : menubarConfigrations) {
            menubarConfigration = m;
        }
        ServiceLoader<CenterPanelService> centerPanelServices = ServiceLoader.load(CenterPanelService.class);
        for (CenterPanelService c : centerPanelServices) {
            centerPanelService = c;
        }
        if (centerPanelService == null) {
            centerPanelService = new EmptyCenterPanel();
        }
        // 先初始化资源，再扫描可用组件，使组件isVisible()可以调用初始化的资源
        InterfaceScanner.invoke(InitializingFactory.class, "initialize");
        projectsMap = new SampleScanner().discoverSamples();
    }

    /**
     * 加载程序配置，主要是程序图标加载.
     */
    private void loadConfiguration() {
        ServiceLoader<FXSamplerConfiguration> samplerConfigurations = ServiceLoader.load(FXSamplerConfiguration.class);
        for (FXSamplerConfiguration configuration : samplerConfigurations) {
            fxsamplerConfiguration = configuration;
        }
    }

    /**
     * 初始化UI.
     */
    private void initializeUI() {
        buildSampleTree(null);
        // simple layout: TreeView on left, sample area on right
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(5, 10, 10, 10));
        grid.setHgap(10);
        grid.setVgap(10);

        BorderPane bp = new BorderPane();
        bp.setPadding(new Insets(10, 20, 10, 20));

        // menubar
        if (menubarConfigration != null) {
            HBox menuBox = menubarConfigration.getMenuBox();
            bp.setTop(menuBox);
        }

        // --- left hand side
        // search box
        final TextField searchBox = new TextField();
        searchBox.setPromptText(I18nUtils.get("frame.searchBox.promptText"));
        searchBox.getStyleClass().add("search-box");
        searchBox.textProperty().addListener(o -> buildSampleTree(searchBox.getText()));
        GridPane.setMargin(searchBox, new Insets(5, 0, 0, 0));
        grid.add(searchBox, 0, 1);

        // treeview
        samplesTreeView = new TreeView<>(root);
        samplesTreeView.setShowRoot(false);
        samplesTreeView.getStyleClass().add("samples-tree");
        samplesTreeView.setMinWidth(210);
        samplesTreeView.setCellFactory(new DefaultTreeViewCellFactory());
        samplesTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newSample) -> {
            if (newSample == null) {
                return;
            } else if (newSample.getValue() instanceof EmptySample) {
                selectedProject = projectsMap.get(newSample.getValue().getSampleName());
                if (selectedProject != null) {
                    changeToWelcomeTab(selectedProject.getWelcomePage());
                }
                return;
            }
            if (selectedSample != null) {
                selectedSample.dispose();
            }
            selectedSample = newSample.getValue();
            changeSample();
        });
        GridPane.setVgrow(samplesTreeView, Priority.ALWAYS);
        grid.add(samplesTreeView, 0, 2);
        bp.setLeft(grid);

        Node centerPanel = centerPanelService.getCenterPanel();
        bp.setCenter(centerPanel);

        // by default we'll show the welcome message of first project in the tree
        // if no projects are available, we'll show the default page
        projects = samplesTreeView.getRoot().getChildren();
        if (!projects.isEmpty()) {
            TreeItem<Sample> firstProject = projects.get(0);
            samplesTreeView.getSelectionModel().select(firstProject);
        } else {
            changeToWelcomeTab(null);
        }
        // 配置samplesTreeView
        ServiceLoader<SamplesTreeViewConfiguration> samplesTreeViewConfigurations = ServiceLoader.load(SamplesTreeViewConfiguration.class);
        for (SamplesTreeViewConfiguration samplesTreeViewConfiguration : samplesTreeViewConfigurations) {
            Callback<TreeView<Sample>, TreeCell<Sample>> cellFactory = samplesTreeViewConfiguration.cellFactory();
            if (cellFactory != null) {
                samplesTreeView.setCellFactory(cellFactory);
            }
            SampleTreeViewModel model = new SampleTreeViewModel(samplesTreeView);
            samplesTreeViewConfiguration.configSampleTreeView(model);
        }
        setUserAgentStylesheet(STYLESHEET_MODENA);
        // put it all together
        Scene scene = new Scene(bp);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/fxsampler/fxsampler.css")).toExternalForm());
        if (fxsamplerConfiguration != null) {
            String stylesheet = fxsamplerConfiguration.getSceneStylesheet();
            if (stylesheet != null) {
                scene.getStylesheets().add(stylesheet);
            }
            String theme = fxsamplerConfiguration.getTheme().getUrl();
            if (theme != null) {
                scene.getStylesheets().add(theme);
            }
            String title = fxsamplerConfiguration.getStageTitle();
            FxApp.setTitle(title);
        }
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setOnCloseRequest(FXSampler::confirmExit);
        // set width / height values to be 75% of users screen resolution
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setWidth(screenBounds.getWidth() * 0.75);
        stage.setHeight(screenBounds.getHeight() * .75);
        // stage.setResizable(false);
        if (StrUtil.isEmpty(FxApp.title)) {
            stage.setTitle(I18nUtils.get("frame.stage.title"));
        } else {
            stage.setTitle(FxApp.title);
        }
        // 加载上次位置
        StageUtil.loadPrimaryStageBound(stage);
        stage.setOnShown(windowEvent -> {
            if (Config.getBoolean(Keys.CheckForUpdatesAtStartup, true)) {
                // 检查更新
                StaticLog.debug("Version Checker...");
                ThreadPoolTaskExecutor.get().execute(() -> {
                    ServiceLoader<VersionCheckerService> versionCheckerServices = ServiceLoader.load(VersionCheckerService.class);
                    for (VersionCheckerService versionCheckerService : versionCheckerServices) {
                        versionCheckerService.checkNewVersion();
                    }
                });
            }
        });
        if (!supportAnim) {
            stage.show();
            stage.requestFocus();
        }

        stopWatch.stop();
        Console.log(String.format("Started Application in %.3f seconds", stopWatch.getTotalTimeSeconds()));
        samplesTreeView.requestFocus();
    }

    /**
     * 启动后初始化资源.
     */
    private void initializeSource() {
        // 在调用buildSampleTree(null) 后projects包含了所有Sample数据
        SamplePostProcessorService.Samples.addAll(projects);
        ServiceLoader<SamplePostProcessorService> samplePostProcessorServices = ServiceLoader.load(SamplePostProcessorService.class);
        try {
            for (SamplePostProcessorService samplePostProcessor : samplePostProcessorServices) {
                samplePostProcessor.postProcessBeanFactory();
            }
        } catch (SampleDefinitionException e) {
            StaticLog.error(e);
        }
    }

    private void buildSampleTree(String searchText) {
        // rebuild the whole tree (it isn't memory intensive - we only scan
        // classes once at startup)
        root = new TreeItem<>(new EmptySample(I18nUtils.get("frame.sample.emptySample")));
        root.setExpanded(true);

        for (String projectName : projectsMap.keySet()) {
            final Project project = projectsMap.get(projectName);
            if (project == null) {
                continue;
            }

            // now work through the project sample tree building the rest
            SampleTree.TreeNode n = project.getSampleTree().getRoot();
            root.getChildren().add(n.createTreeItem());
        }

        // with this newly built and full tree, we filter based on the search text
        if (searchText != null) {
            pruneSampleTree(root, searchText);

            // FIXME weird bug in TreeView I think
            samplesTreeView.setRoot(null);
            samplesTreeView.setRoot(root);
        }

        // and finally we sort the display a little
        sort(root, Comparator.comparing(o -> o.getValue().getOrderKey()));
    }

    // 切换左侧菜单时触发
    private void changeSample() {
        if (selectedSample == null) {
            return;
        }
        centerPanelService.changeSample();
        centerPanelService.updateSampleChild(selectedSample, selectedProject);
    }

    public static Stage getStage() {
        return stage;
    }

    /**
     * 确认退出系统.
     */
    public static void confirmExit(Event event) {
        if (Config.getBoolean(Keys.ConfirmExit, true)) {
            if (FxAlerts.confirmYesNo(I18nUtils.get("frame.main.confirmExit.title"), I18nUtils.get("frame.main.confirmExit.message"))) {
                doExit();
            } else if (event != null) {
                event.consume();
            }
        } else {
            doExit();
        }
    }

    /**
     * 退出系统.
     */
    public static void doExit() {
        StageUtil.savePrimaryStageBound(stage);
        EventBus.getDefault().post(new ApplicationExitEvent());
        Platform.exit();
        System.exit(0);
    }

    /**
     * 重启程序.
     */
    public static void restart() {
        Platform.runLater(() -> {
            EventBus.getDefault().post(new ApplicationRestartEvent());
            stage.close();
            SampleCacheFactory.clear();
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                StaticLog.error(e);
                Thread.currentThread().interrupt();
            }
            new FXSampler().start(new Stage());
        });
    }

    private void sort(TreeItem<Sample> node, Comparator<TreeItem<Sample>> comparator) {
        node.getChildren().sort(comparator);
        for (TreeItem<Sample> child : node.getChildren()) {
            sort(child, comparator);
        }
    }

    // true == keep, false == delete
    private boolean pruneSampleTree(TreeItem<Sample> treeItem, String searchText) {
        // we go all the way down to the leaf nodes, and check if they match
        // the search text. If they do, they stay. If they don't, we remove them.
        // As we pop back up we check if the branch nodes still have children,
        // and if not we remove them too
        if (searchText == null) {
            return true;
        }

        if (treeItem.isLeaf()) {
            // check for match. Return true if we match (to keep), and false
            // to delete
            return treeItem.getValue().getSampleName().toUpperCase().contains(searchText.toUpperCase());
        } else {
            // go down the tree...
            List<TreeItem<Sample>> toRemove = new ArrayList<>();

            for (TreeItem<Sample> child : treeItem.getChildren()) {
                boolean keep = pruneSampleTree(child, searchText);
                if (!keep) {
                    toRemove.add(child);
                }
            }

            // remove the unrelated tree items
            treeItem.getChildren().removeAll(toRemove);

            // return true if there are children to this branch, false otherwise
            // (by returning false we say that we should delete this now-empty branch)
            return !treeItem.getChildren().isEmpty();
        }
    }

    private void changeToWelcomeTab(WelcomePage wPage) {
        if (null == wPage) {
            wPage = getDefaultWelcomePage();
        }
        centerPanelService.handleWelcomePage(wPage);
    }

    private WelcomePage getDefaultWelcomePage() {
        // line 1
        Label welcomeLabel1 = new Label(I18nUtils.get("frame.sample.defaultWelcomeLabel1"));
        welcomeLabel1.setStyle("-fx-font-size: 2em; -fx-padding: 0 0 0 5;");

        // line 2
        Label welcomeLabel2 = new Label(I18nUtils.get("frame.sample.defaultWelcomeLabel2"));
        welcomeLabel2.setStyle("-fx-font-size: 1.25em; -fx-padding: 0 0 0 5;");

        return new WelcomePage(I18nUtils.get("frame.sample.welcome"), new VBox(5, welcomeLabel1, welcomeLabel2));
    }

    /**
     * 输出banner.
     */
    private void printBanner() {
        ServiceLoader<BannerPrinterService> banners = ServiceLoader.load(BannerPrinterService.class);
        for (BannerPrinterService banner : banners) {
            banner.printBanner();
        }
    }

    /**
     * 初始化虚拟机参数.
     */
    private void initializeProperties() {
        // 如果在启动时设置了workEnv, 那使用设置值,否则根据Keys.UseDevMode来设置参数
        if (System.getProperty(CoreConstant.JVM_WORKENV) == null) {
            if (Config.getBoolean(Keys.UseDevMode, false)) {
                System.setProperty(CoreConstant.JVM_WORKENV, CoreConstant.JVM_WORKENV_DEV);
            } else {
                System.setProperty(CoreConstant.JVM_WORKENV, CoreConstant.JVM_WORKENV_PROD);
            }
        }
        // 修复vosk在jna 5.10.0中修改了encoding初始化方式之后产生的乱码问题
        if (DependencyUtil.hasJna() && System.getProperty("jna.encoding") == null) {
            System.setProperty("jna.encoding", CoreConstant.ENCODING_UTF_8);
        }
    }
}
