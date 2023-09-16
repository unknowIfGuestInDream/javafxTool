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

package com.tlcsdm.qe.tools;

import com.tlcsdm.core.javafx.util.Config;
import com.tlcsdm.core.javafx.util.FxmlUtil;
import com.tlcsdm.qe.QeSample;
import com.tlcsdm.qe.util.I18nUtils;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * 测试用，DALi Config
 *
 * @author unknowIfGuestInDream
 */
public class DaliDemo extends QeSample implements Initializable {

    @Override
    public boolean isVisible() {
        return super.isVisible();
    }

    @Override
    public Node getPanel(Stage stage) {
        FXMLLoader fxmlLoader = FxmlUtil.loadFxmlFromResource(DaliDemo.class.getResource("/com/tlcsdm/qe/fxml/daliConfig.fxml"),
            ResourceBundle.getBundle(I18nUtils.BASENAME, Config.defaultLocale));
        return fxmlLoader.getRoot();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public String getSampleId() {
        return "daliConfig";
    }

    @Override
    public String getSampleName() {
        return "DALI Config";
    }

    @Override
    public String getOrderKey() {
        return "daliConfig";
    }

    @Override
    public String getSampleDescription() {
        return "此组件测试用";
    }

    @Override
    public String getSampleVersion() {
        return "1.0.0-Beta";
    }

    @Override
    public boolean hasRightPanel() {
        return false;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
