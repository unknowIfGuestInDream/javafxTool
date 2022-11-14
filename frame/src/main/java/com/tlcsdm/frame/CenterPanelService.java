package com.tlcsdm.frame;

import com.tlcsdm.frame.model.Project;
import com.tlcsdm.frame.model.WelcomePage;
import javafx.scene.Node;
import javafx.stage.Stage;

/**
 * BorderPane的center组件(应用模块实现)
 */
public interface CenterPanelService {

    /**
     * 设置CenterPanel
     */
    Node getCenterPanel(Stage stage);

    /**
     * 点击菜单时触发的CenterPanel action
     */
    void changeSample();

    /**
     * CenterPanel 内部组件的切换
     */
    void updateSampleChild(Sample selectedSample, Project selectedProject);

    //WelcomePage 处理

    /**
     * WelcomePage 处理
     */
    void handleWelcomePage(WelcomePage wPage);

}
