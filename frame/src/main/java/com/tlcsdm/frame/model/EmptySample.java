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

import com.tlcsdm.core.javafx.util.Config;
import com.tlcsdm.core.javafx.util.Keys;
import com.tlcsdm.frame.Sample;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * @author unknowIfGuestInDream
 */
public final class EmptySample implements Sample {
    private final String name;

    public EmptySample(String name) {
        this.name = name;
    }

    @Override
    public String getSampleId() {
        return "empty";
    }

    @Override
    public String getSampleName() {
        return name;
    }

    @Override
    public String getSampleDescription() {
        return null;
    }

    @Override
    public String getProjectName() {
        return null;
    }

    @Override
    public String getProjectVersion() {
        return null;
    }

    @Override
    public String getSampleVersion() {
        return null;
    }

    @Override
    public ImageView getSampleImageIcon() {
        return null;
    }

    @Override
    public Node getPanel(Stage stage) {
        return null;
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public String getOrderKey() {
        return "";
    }

    @Override
    public String getSampleXmlPrefix() {
        return getSampleName();
    }

    @Override
    public Node getControlPanel() {
        return null;
    }

    @Override
    public void initialize() {

    }

    @Override
    public boolean hasControlPanel() {
        return false;
    }

    @Override
    public void dispose() {
        // Do nothing
    }

    @Override
    public double getControlPanelDividerPosition() {
        return Config.getDouble(Keys.ControlDividerPosition, 0.6);
    }

}
