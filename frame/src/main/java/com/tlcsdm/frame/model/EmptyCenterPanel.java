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

package com.tlcsdm.frame.model;

import com.tlcsdm.frame.FXSampler;
import com.tlcsdm.frame.Sample;
import com.tlcsdm.frame.SampleBase;
import com.tlcsdm.frame.cache.SampleCacheFactory;
import com.tlcsdm.frame.service.CenterPanelService;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

/**
 * @author unknowIfGuestInDream
 * @date 2022/10/22 21:27
 */
public class EmptyCenterPanel implements CenterPanelService {

    private Stage stage;
    private SampleBase selectedSample;
    private TabPane tabPane;
    private Tab sampleTab;
    private Tab welcomeTab;

    @Override
    public Node getCenterPanel() {
        this.stage = FXSampler.getStage();
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
        this.selectedSample = (SampleBase) selectedSample;
        updateTab();
    }

    @Override
    public void handleWelcomePage(WelcomePage wPage) {
        welcomeTab = new Tab(wPage.getTitle());
        welcomeTab.setContent(wPage.getContent());
        tabPane.getTabs().setAll(welcomeTab);
    }

    private void updateTab() {
        if (selectedSample == null) {
            return;
        }
        sampleTab.setContent(buildCommonContent(selectedSample));
    }

    private Node buildCommonContent(Sample sample) {
        String key = sample.getProjectName() + sample.getSampleId();
        if (SampleCacheFactory.containsKey(key)) {
            Object obj = SampleCacheFactory.get(key);
            if (obj instanceof Node n) {
                return n;
            }
        }
        Node node = SampleBase.buildSample(sample, stage);
        SampleCacheFactory.put(key, node);
        return node;
    }
}
