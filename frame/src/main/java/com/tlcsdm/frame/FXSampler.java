/*
 * Copyright (c) 2019, 2023 unknowIfGuestInDream
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;

import com.tlcsdm.core.factory.InitializingFactory;
import com.tlcsdm.core.javafx.FxApp;
import com.tlcsdm.core.javafx.dialog.FxAlerts;
import com.tlcsdm.core.javafx.helper.LayoutHelper;
import com.tlcsdm.core.javafx.util.Config;
import com.tlcsdm.core.javafx.util.JavaFxSystemUtil;
import com.tlcsdm.core.javafx.util.StageUtils;
import com.tlcsdm.core.util.InterfaceScanner;
import com.tlcsdm.frame.model.EmptyCenterPanel;
import com.tlcsdm.frame.model.EmptySample;
import com.tlcsdm.frame.model.Project;
import com.tlcsdm.frame.model.SampleTree;
import com.tlcsdm.frame.model.WelcomePage;
import com.tlcsdm.frame.util.I18nUtils;
import com.tlcsdm.frame.util.SampleScanner;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

public final class FXSampler extends Application {

    private Map<String, Project> projectsMap;

    private static Stage stage;

    private Sample selectedSample;
    private CenterPanelService centerPanelService;

    private TreeView<Sample> samplesTreeView;
    private TreeItem<Sample> root;

    private Project selectedProject;
    // 用于闪屏功能
    private Label infoLb;
    private final StopWatch stopWatch = new StopWatch();
    // 用于 初始化UI
    private ServiceLoader<FXSamplerConfiguration> samplerConfigurations;
    private MenubarConfigration menubarConfigration = null;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) {
        stopWatch.start();
        stage = primaryStage;
        loadSplash();
        new Thread(() -> {
            JavaFxSystemUtil.initSystemLocal();
            showInfo(I18nUtils.get("frame.splash.init.version"));
            initializeSystem();
            Platform.runLater(() -> {
                try {
                    initializeUI();
                } catch (Throwable e) {
                    FxAlerts.exception(e);
                }
            });
        }).start();
    }

    /**
     * 加载闪屏图片
     */
    public void loadSplash() {
        Image image = null;
        // 加载闪屏图片
        ServiceLoader<SplashScreen> splashScreens = ServiceLoader.load(SplashScreen.class);
        for (SplashScreen s : splashScreens) {
            image = s.getImage();
        }
        if (image == null) {
            image = LayoutHelper.icon(getClass().getResource("/com/tlcsdm/frame/static/splash.png"));
        }
        ImageView view = new ImageView(image);
        infoLb = new Label();
        infoLb.setTextFill(Color.WHITE);
        AnchorPane.setRightAnchor(infoLb, 10.0);
        AnchorPane.setBottomAnchor(infoLb, 10.0);

        AnchorPane page = new AnchorPane();
        page.getChildren().addAll(view, infoLb);
        Stage loadingStage = new Stage();
        loadingStage.setScene(new Scene(page));
        loadingStage.setWidth(image.getWidth());
        loadingStage.setHeight(image.getHeight());
        loadingStage.initStyle(StageStyle.UNDECORATED);
        loadingStage.show();
        stage.addEventHandler(WindowEvent.WINDOW_SHOWN, event -> loadingStage.hide());
    }

    /**
     * 闪屏图片信息展示
     */
    public void showInfo(String info) {
        FxApp.runLater(() -> infoLb.setText(info));
    }

    /**
     * 初始化系统配置
     */
    public void initializeSystem() {
        showInfo(I18nUtils.get("frame.splash.init.system"));
        FxApp.init(stage, getClass().getResource("/fxsampler/logo.png"));
        samplerConfigurations = ServiceLoader.load(FXSamplerConfiguration.class);
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
        projectsMap = new SampleScanner().discoverSamples();
        InterfaceScanner.invoke(InitializingFactory.class, "initialize");
    }

    /**
     * 初始化
     */
    public void initializeUI() {
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
            MenuBar menuBar = menubarConfigration.getMenuBar();
            bp.setTop(menuBar);
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
        samplesTreeView.setMinWidth(200);
        samplesTreeView.setMaxWidth(200);
        samplesTreeView.setCellFactory(new Callback<>() {
            @Override
            public TreeCell<Sample> call(TreeView<Sample> param) {
                return new TreeCell<>() {
                    @Override
                    protected void updateItem(Sample item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText("");
                        } else {
                            setText(item.getSampleName());
                        }
                    }
                };
            }
        });
        samplesTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newSample) -> {
            if (newSample == null) {
                return;
            } else if (newSample.getValue() instanceof EmptySample) {
                Sample selectedSample = newSample.getValue();
                selectedProject = projectsMap.get(selectedSample.getSampleName());
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
        List<TreeItem<Sample>> projects = samplesTreeView.getRoot().getChildren();
        if (!projects.isEmpty()) {
            TreeItem<Sample> firstProject = projects.get(0);
            samplesTreeView.getSelectionModel().select(firstProject);
        } else {
            changeToWelcomeTab(null);
        }

        // put it all together
        Scene scene = new Scene(bp);
        scene.getStylesheets()
                .add(Objects.requireNonNull(getClass().getResource("/fxsampler/fxsampler.css")).toExternalForm());
        for (FXSamplerConfiguration fxsamplerConfiguration : samplerConfigurations) {
            String stylesheet = fxsamplerConfiguration.getSceneStylesheet();
            if (stylesheet != null) {
                scene.getStylesheets().add(stylesheet);
            }
            String title = fxsamplerConfiguration.getStageTitle();
            FxApp.setTitle(title);
            FxApp.setAppIcon(fxsamplerConfiguration.getAppIcon());
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
        StageUtils.loadPrimaryStageBound(stage);
        stage.show();
        stopWatch.stop();
        Console.log(String.format("Started Application in %.3f seconds", stopWatch.getTotalTimeSeconds()));
        samplesTreeView.requestFocus();
    }

    void buildSampleTree(String searchText) {
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
    void changeSample() {
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
     * 确认退出系统
     */
    public static void confirmExit(Event event) {
        if (Config.getBoolean(Config.Keys.ConfirmExit, true)) {
            if (FxAlerts.confirmYesNo(I18nUtils.get("frame.main.confirmExit.title"),
                    I18nUtils.get("frame.main.confirmExit.message"))) {
                doExit();
            } else if (event != null) {
                event.consume();
            }
        } else {
            doExit();
        }
    }

    /**
     * 退出系统
     */
    public static void doExit() {
        StageUtils.savePrimaryStageBound(stage);
        Platform.exit();
        System.exit(0);
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
}
