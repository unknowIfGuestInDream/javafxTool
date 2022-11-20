/**
 * Copyright (c) 2022, ControlsFX
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
package com.tlcsdm.smc;

import com.tlcsdm.frame.FXSamplerProject;
import com.tlcsdm.frame.model.WelcomePage;
import com.tlcsdm.smc.util.SmcConstant;
import javafx.beans.binding.StringBinding;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.HyperlinkLabel;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicBoolean;

public class SmcSamplerProject implements FXSamplerProject {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProjectName() {
        return "SMC";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSampleBasePackage() {
        return "com.tlcsdm.smc";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getModuleName() {
        return "com.tlcsdm.smc";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WelcomePage getWelcomePage() {
        VBox vBox = new VBox();
        vBox.getStyleClass().add("welcomePage");
        ImageView imgView = new ImageView();
        imgView.setStyle("-fx-image: url('com/tlcsdm/smc/static/ControlsFX.png');");
        StackPane pane = new StackPane();
        pane.setPrefHeight(207);
        pane.setStyle(
                "-fx-background-image: url('com/tlcsdm/smc/static/bar.png');" + "-fx-background-repeat: repeat-x;");
        pane.getChildren().add(imgView);
        Label label = new Label();
        label.setWrapText(true);
        StringBuilder desc = new StringBuilder();
        desc.append("SMC Tool is an agile development tool that helps you ");
        desc.append("to provide really high quality UI controls and other tools to ");
        desc.append("complement the core JavaFX distribution.");
        desc.append("\n\n");
        desc.append("Explore the available UI controls by clicking on the options to the left.");
        label.setText(desc.toString());
        label.setStyle("-fx-font-size: 1.5em; -fx-padding: 20 0 0 5;");

        TextField textField = new TextField();
        textField.setEditable(false);
        textField.setStyle("-fx-font-size: 1.5em; -fx-padding: 20 0 0 5;-fx-background-color: transparent;");
        StringBuilder sb = new StringBuilder();
        sb.append("This project is built on javaFX 17 and controlsfx 11. ");
        sb.append("If you have some questions or ideas, Please contact me.");
        textField.setText(sb.toString());

        HyperlinkLabel link = new HyperlinkLabel();
        link.setStyle("-fx-font-size: 1.5em; -fx-padding: 20 0 0 5;");
        link.textProperty().bind(new StringBinding() {
            @Override
            protected String computeValue() {
                return "project url: [" + SmcConstant.GITHUB_PROJECT_URL + "]";
            }
        });

        //存在调用两次的问题，先这样解决
        AtomicBoolean b = new AtomicBoolean(true);
        link.setOnAction(event -> {
            if (!b.get()) {
                b.set(true);
                return;
            }
            Desktop d = Desktop.getDesktop();
            try {
                URI address = new URI(SmcConstant.GITHUB_PROJECT_URL);
                d.browse(address);
                b.set(false);
            } catch (URISyntaxException | IOException e) {
                e.printStackTrace();
            }
        });

        vBox.getChildren().addAll(pane, label, textField, link);
        return new WelcomePage("Welcome to SMC Tool!", vBox);
    }
}
