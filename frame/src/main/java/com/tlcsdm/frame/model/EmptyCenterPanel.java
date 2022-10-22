package com.tlcsdm.frame.model;

import com.tlcsdm.frame.CenterPanelService;
import com.tlcsdm.frame.Sample;
import javafx.scene.Node;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

/**
 * @author: 唐 亮
 * @date: 2022/10/22 21:27
 */
public class EmptyCenterPanel implements CenterPanelService {

    @Override
    public Node setCenterPanel(Node centerPanel, Stage stage) {
        return new TabPane();
    }

    @Override
    public void changeSample() {

    }

    @Override
    public void updateSampleChild(Sample selectedSample, Project selectedProject) {

    }

    @Override
    public void handleWelcomePage(WelcomePage wPage) {

    }
}
