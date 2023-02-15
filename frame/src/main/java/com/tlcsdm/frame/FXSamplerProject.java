/**
 * Copyright (c) 2013, 2020, ControlsFX
 * All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of ControlsFX, any associated website, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * <p>
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

import com.tlcsdm.frame.model.WelcomePage;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public interface FXSamplerProject {

    /**
     * Returns the pretty name of the project, e.g. 'JFXtras' or 'ControlsFX'
     */
    String getProjectName();

    /**
     * All samples should be beneath this base package. For example, in ControlsFX,
     * this may be 'org.controlsfx.samples'.
     */
    String getSampleBasePackage();

    /**
     * Node that will be displayed in welcome tab, when project's root is
     * selected in the tree. If this method returns null, default page will
     * be used
     */
    WelcomePage getWelcomePage();

    /**
     * Module name of the project for which the sampler is to be used.
     * For example, in case of ControlsFX this is "org.controlsfx.controls".
     * Can be left blank if the sampler application is running on classpath.
     */
    default String getModuleName() {
        return "";
    }

    /**
     * 获取默认背景图
     *
     * @return Pane
     */
    default Pane getWelcomeBackgroundImagePane() {
        ImageView imgView = new ImageView();
        imgView.setStyle("-fx-image: url('com/tlcsdm/frame/static/JavaFXTool.png');");
        StackPane pane = new StackPane();
        pane.setPrefHeight(207);
        pane.setStyle(
                "-fx-background-image: url('com/tlcsdm/frame/static/JavaFXToolBar.png');-fx-background-repeat: repeat-x;");
        pane.getChildren().add(imgView);
        return pane;
    }
}
