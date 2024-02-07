/*
 * Copyright (c) 2024 unknowIfGuestInDream.
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

package com.tlcsdm.core.javafx.control;

import com.tlcsdm.core.javafx.control.skin.DecorationTitlePaneSkin;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.control.TitledPane;

import java.util.Objects;

/**
 * @author unknowIfGuestInDream
 */
public class DecorationTitlePane extends TitledPane {

    public DecorationTitlePane() {
        super();
        getStyleClass().add("decoration-title-pane");
    }

    public DecorationTitlePane(String var1, Node var2) {
        this();
        this.setText(var1);
        this.setContent(var2);
    }

    private ObjectProperty<Severity> severity;

    /**
     * Severity of messages.
     */
    public final ObjectProperty<Severity> severityProperty() {
        if (severity == null) {
            severity = new SimpleObjectProperty<>(this, "severity", Severity.OK);
        }
        return severity;
    }

    public final Severity getSeverity() {
        return severity == null ? Severity.OK : severityProperty().get();
    }

    public final void setSeverity(Severity severity) {
        severityProperty().set(severity);
    }

    private StringProperty tooltipMsg;

    /**
     * Tooltip text.
     */
    public final StringProperty tooltipMsgProperty() {
        if (tooltipMsg == null) {
            tooltipMsg = new SimpleStringProperty(this, "tooltipMsg", "");
        }
        return tooltipMsg;
    }

    public final String getTooltipMsg() {
        return tooltipMsg == null ? "" : tooltipMsgProperty().get();
    }

    public final void setTooltipMsg(String message) {
        tooltipMsgProperty().set(message);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new DecorationTitlePaneSkin(this);
    }

    @Override
    public String getUserAgentStylesheet() {
        return Objects.requireNonNull(
                DecorationTitlePane.class.getResource("/com/tlcsdm/core/static/javafx/control/decorationtitlepane.css"))
            .toExternalForm();
    }

    public void setDecoration(Severity severity, String message) {
        setTooltipMsg(message);
        setSeverity(severity);
    }

    /**
     * Set Severity without tip.
     */
    public void setDecoration(Severity severity) {
        setDecoration(severity, null);
    }
}
