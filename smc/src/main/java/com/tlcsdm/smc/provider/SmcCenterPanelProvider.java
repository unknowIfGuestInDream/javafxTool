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

package com.tlcsdm.smc.provider;

import com.tlcsdm.frame.FXSampler;
import com.tlcsdm.frame.Sample;
import com.tlcsdm.frame.SampleBase;
import com.tlcsdm.frame.model.Project;
import com.tlcsdm.frame.model.WelcomePage;
import com.tlcsdm.frame.service.CenterPanelService;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import org.controlsfx.control.MaskerPane;

/**
 * @author unknowIfGuestInDream
 * @date 2022/10/22 21:00
 */
public class SmcCenterPanelProvider implements CenterPanelService {

    private SampleBase selectedSample;
    private Project selectedProject;
    private Stage stage;
    private ScrollPane scrollPane;
    private final MaskerPane masker = new MaskerPane();

    @Override
    public Node getCenterPanel() {
        scrollPane = new ScrollPane();
        this.stage = FXSampler.getStage();
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
        this.selectedSample = (SampleBase) selectedSample;
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
        scrollPane.setContent(buildSmcContent(selectedSample));
    }

    /**
     * 加载content前初始化
     */
    private void prepareContent(ScrollPane scrollPane) {
        scrollPane.setContent(masker);
    }

    private Node buildSmcContent(Sample sample) {
        return SampleBase.buildSample(sample, stage);
    }
}
