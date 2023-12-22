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

package com.tlcsdm.smc.codeDev.ecm;

import com.tlcsdm.core.factory.InitializingFactory;
import com.tlcsdm.core.util.InterfaceScanner;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * U2A的ECM脚本.
 *
 * @author unknowIfGuestInDream
 * @since 1.0.1
 */
public final class U2AEcmScript extends AbstractU2XFamilyScript {

    @Override
    protected void initDefaultValue() {
        super.initDefaultValue();
        sheetNameField.setText("U2A");
        categoryConfigField.setText("""
            categoryId;F
            categoryEnName;G
            categoryJpName;H
            """);
        functionConfigField.setText("""
            optMaskint;G
            optIntg;G
            optDCLS;G
            optIntrg;I
            optErroroutput;J
            optErrort;K
            optDelayt;L
            """);
        errorSourceIdColField.setText("A");
        categoryIdColField.setText("B");
        errorSourceNumberColField.setText("C");
        errorSourceEnNameColField.setText("D");
        errorSourceDescColField.setText("E");
        errorSourceJpNameColField.setText("W");
        productConfigField.setText("""
            RH850U2A16;516;N
            RH850U2A16;373;O
            RH850U2A16;292;P
            RH850U2A8;373;R
            RH850U2A8;292;S
            RH850U2A6;292;T
            RH850U2A6;176;T
            RH850U2A6;156;U
            RH850U2A6;144;V
            """);
        tagConfigField.setDisable(true);
    }

    @Override
    public TitledPane createErrorSourceControl() {
        TitledPane titledPane = super.createErrorSourceControl();
        GridPane grid = (GridPane) titledPane.getContent();
        grid.getChildren().remove(tagConfigLabel);
        grid.getChildren().remove(tagConfigField);
        return titledPane;
    }

    @Override
    public void initializeUserDataBindings() {
        super.initializeUserDataBindings();
        userData.remove("tagConfig");
    }

    public static void main(String[] args) {
        InterfaceScanner.invoke(InitializingFactory.class, "initialize");
        launch(args);
    }

    @Override
    public String getSampleId() {
        return "u2AEcmScript";
    }

    @Override
    public String getSampleName() {
        return "U2AECMScript";
    }

    @Override
    public Node getPanel(Stage stage) {
        return super.getPanel(stage);
    }

    @Override
    public String getOrderKey() {
        return "U2AEcmScript";
    }

    @Override
    protected String getFtlPath() {
        return "smc/ecm/u2a.ftl";
    }

    @Override
    protected String getGroovyPath() {
        return "codeDev/ecm/u2a.groovy";
    }
}
