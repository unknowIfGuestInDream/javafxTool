/**
 * Copyright (c) 2013, 2020, ControlsFX
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of ControlsFX, any associated website, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL CONTROLSFX BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.tlcsdm.frame;

import javafx.scene.Node;
import javafx.stage.Stage;

/**
 *
 */
public interface Sample {

    /**
     * 组件id
     */
    String getSampleId();

    /**
     * A short, most likely single-word, name to show to the user - e.g. "CheckBox"
     */
    String getSampleName();

    /**
     * A short, multiple sentence description of the sample.
     */
    String getSampleDescription();

    /**
     * Returns the name of the project that this sample belongs to (e.g. 'JFXtras'
     * or 'ControlsFX').
     */
    String getProjectName();

    /**
     * Returns the version of the project that this sample belongs to (e.g. '1.0.0')
     */
    String getProjectVersion();

    /**
     * sample version
     */
    String getSampleVersion();

    /**
     * Returns the main sample panel.
     */
    Node getPanel(final Stage stage);

    /**
     * Returns the panel to display to the user that allows for manipulating
     * the sample.
     */
    Node getControlPanel();

    /**
     * Note that initialize() is called after getPanel()
     */
    void initialize();

    /**
     * Provides a place to dispose of any resources when sample is deselected
     */
    void dispose();

    /**
     * Returns divider position to use for split between main panel and control panel
     */
    double getControlPanelDividerPosition();

    /**
     * If true this sample is shown to users, if false it is not.
     */
    boolean isVisible();

    /**
     * 排序字段
     */
    String getOrderKey();

}