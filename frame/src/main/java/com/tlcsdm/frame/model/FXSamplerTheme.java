/*
 * Copyright (c) 2023 unknowIfGuestInDream
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

import java.util.Arrays;
import java.util.List;

/**
 * 主题.
 *
 * @author unknowIfGuestInDream
 */
public enum FXSamplerTheme {

    PRIMER_LIGHT("Primer-Light", FXSamplerTheme.class.getResource("/com/tlcsdm/frame/static/css/primer-light.css").toExternalForm()),
    PRIMER_DARK("Primer-Dark", FXSamplerTheme.class.getResource("/com/tlcsdm/frame/static/css/primer-dark.css").toExternalForm()),
    NORD_LIGHT("Nord-Light", FXSamplerTheme.class.getResource("/com/tlcsdm/frame/static/css/nord-light.css").toExternalForm()),
    NORD_DARK("Nord-Dark", FXSamplerTheme.class.getResource("/com/tlcsdm/frame/static/css/nord-dark.css").toExternalForm()),
    DRACULA("Dracula", FXSamplerTheme.class.getResource("/com/tlcsdm/frame/static/css/dracula.css").toExternalForm()),
    CUPERTINO_LIGHT("Cupertino-Light", FXSamplerTheme.class.getResource("/com/tlcsdm/frame/static/css/cupertino-light.css").toExternalForm()),
    CUPERTINO_DARK("Cupertino-Dark", FXSamplerTheme.class.getResource("/com/tlcsdm/frame/static/css/cupertino-dark.css").toExternalForm()),
    BOOTSTRAP2("Bootstrap2", FXSamplerTheme.class.getResource("/com/tlcsdm/frame/static/css/bootstrap2.css").toExternalForm()),
    BOOTSTRAP3("Bootstrap3", FXSamplerTheme.class.getResource("/com/tlcsdm/frame/static/css/bootstrap3.css").toExternalForm()),
    UNUSED("Modena", null);

    private final String name;
    private final String url;

    FXSamplerTheme(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    /**
     * 根据name获取FXSamplerTheme.
     */
    public static FXSamplerTheme fromThemeName(String name, FXSamplerTheme defaultTheme) {
        for (FXSamplerTheme theme : FXSamplerTheme.values()) {
            if (theme.getName().equals(name)) {
                return theme;
            }
        }
        return defaultTheme;
    }

    /**
     * 获取主题.
     */
    public static List<String> getThemes(FXSamplerTheme... excludeThemes) {
        List<FXSamplerTheme> themes = List.of(excludeThemes);
        return Arrays.stream(FXSamplerTheme.values()).filter(t -> !themes.contains(t)).map(FXSamplerTheme::getName).toList();
    }
}
