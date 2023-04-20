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

package com.tlcsdm.jfxcommon.provider;

import cn.hutool.core.util.StrUtil;
import com.tlcsdm.frame.model.WelcomePage;
import com.tlcsdm.frame.service.FXSamplerProject;
import com.tlcsdm.jfxcommon.util.I18nUtils;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

/**
 * @author unknowIfGuestInDream
 */
public class CommonSamplerProjectProvider implements FXSamplerProject {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProjectName() {
        return "Common";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSampleBasePackage() {
        return "com.tlcsdm.jfxcommon";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getModuleName() {
        return "com.tlcsdm.jfxcommon";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WelcomePage getWelcomePage() {
        VBox vBox = new VBox();
        vBox.getStyleClass().add("welcomePage");
        Label label = new Label();
        label.setWrapText(true);
        String desc = """
                {introduce}
                """;
        Map<String, String> map = new HashMap<>();
        map.put("introduce", I18nUtils.get("common.welcomePage.desc.introduce"));
        label.setText(StrUtil.format(desc, map));
        label.setStyle("-fx-font-size: 1.5em; -fx-padding: 20 0 0 5;");

        vBox.getChildren().addAll(getWelcomeBackgroundImagePane(), label);
        return new WelcomePage("Common", vBox);
    }

}
