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

package com.tlcsdm.core.javafx.dialog;

import com.tlcsdm.core.javafx.FxApp;
import com.tlcsdm.core.util.I18nUtils;
import javafx.concurrent.Worker;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * 浏览器弹窗.
 *
 * @author unknowIfGuestInDream
 */
public class WebBrowserDialog {

    private static final String defaultUrl = "https://docs.oracle.com/en/java/javase/17";
    private static final double NaviBarMimDimension = 32.0;
    private static final double PaddingValue = 2.0;
    private static final String buttonStyle = "-fx-font-weight: bold; -fx-font-size: 16px;";

    // Unicode characters for buttons
    private static final String goButtonUnicodeSymbol = "\u21B5";
    private static final String stopButtonUnicodeSymbol = "\u2715";
    private static final String backButtonUnicodeSymbol = "\u003C";
    private static final String forwardButtonUnicodeSymbol = "\u003E";
    private static final String reloadButtonUnicodeSymbol = "\u27F3";

    private WebBrowserDialog() {
    }

    public static void openWebBrowser(String url) {
        final String initialURL = url != null ? url : defaultUrl;

        final WebView webView = new WebView();
        final WebEngine webEngine = webView.getEngine();

        final TextField urlBox = new TextField();
        urlBox.setMinHeight(NaviBarMimDimension);

        urlBox.setText(initialURL);
        HBox.setHgrow(urlBox, Priority.ALWAYS);
        urlBox.setOnAction(e -> webEngine.load(urlBox.getText()));

        final Label bottomTitle = new Label();
        bottomTitle.textProperty().bind(urlBox.textProperty());

        final Button goStopButton = new Button(goButtonUnicodeSymbol);
        goStopButton.setStyle(buttonStyle);
        goStopButton.setOnAction(e -> webEngine.load(urlBox.getText()));

        final Button backButton = new Button(backButtonUnicodeSymbol);
        backButton.setStyle(buttonStyle);
        backButton.setDisable(true);
        backButton.setOnAction(e -> webEngine.getHistory().go(-1));

        final Button forwardButton = new Button(forwardButtonUnicodeSymbol);
        forwardButton.setStyle(buttonStyle);
        forwardButton.setDisable(true);
        forwardButton.setOnAction(e -> webEngine.getHistory().go(+1));

        final Button reloadButton = new Button(reloadButtonUnicodeSymbol);
        reloadButton.setStyle(buttonStyle);
        reloadButton.setOnAction(e -> webEngine.reload());

        final HBox naviBar = new HBox();
        naviBar.getChildren().addAll(backButton, forwardButton, urlBox, reloadButton, goStopButton);
        naviBar.setPadding(new Insets(PaddingValue)); // Small padding in the navigation Bar
        final VBox root = new VBox();
        root.getChildren().addAll(naviBar, webView, bottomTitle);
        VBox.setVgrow(webView, Priority.ALWAYS);

        webEngine.locationProperty().addListener((observable, oldValue, newValue) -> urlBox.setText(newValue));

        // If the Worker.State is in lower State than SUCCEEDED (i.e. in READY, SCHEDULED or RUNNING State),
        // then the goStopButton should be in 'Stop' configuration
        // else the goStopButton should be in 'Go' configuration
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.compareTo(Worker.State.SUCCEEDED) < 0) {
                bottomTitle.setVisible(true);
                goStopButton.setText(stopButtonUnicodeSymbol);
                goStopButton.setOnAction(e -> webEngine.getLoadWorker().cancel());
            } else {
                bottomTitle.setVisible(false);
                goStopButton.setText(goButtonUnicodeSymbol);
                goStopButton.setOnAction(e -> webEngine.load(urlBox.getText()));
            }
        });

        webEngine.getHistory().currentIndexProperty().addListener((observable, oldValue, newValue) -> {
            int length = webEngine.getHistory().getEntries().size();
            backButton.setDisable((int) newValue == 0);
            forwardButton.setDisable((int) newValue >= length - 1);
        });

        webEngine.load(initialURL);

        FxDialog<VBox> dialog = new FxDialog<VBox>().setTitle(I18nUtils.get("core.dialog.webBrowser.title")).setOwner(
            FxApp.primaryStage).setResizable(true).setBody(root);
        dialog.show();
    }
}
