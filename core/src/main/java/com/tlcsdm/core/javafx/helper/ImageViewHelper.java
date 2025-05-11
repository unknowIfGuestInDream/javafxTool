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

package com.tlcsdm.core.javafx.helper;

import javafx.scene.image.ImageView;

import java.net.URL;

/**
 * @author unknowIfGuestInDream
 */
public class ImageViewHelper {

    private ImageViewHelper() {
    }

    public static ImageView get(String name) {
        URL url = getUrlFromName(name);
        return LayoutHelper.iconView(url);
    }

    private static URL getUrlFromName(String name) {
        return switch (name) {
            case "folder" -> ImageViewHelper.class.getResource("/com/tlcsdm/core/static/icon/folder.png");
            case "tools" -> ImageViewHelper.class.getResource("/com/tlcsdm/core/static/icon/tools.png");
            case "image" -> ImageViewHelper.class.getResource("/com/tlcsdm/core/static/icon/image.png");
            case "xlsx" -> ImageViewHelper.class.getResource("/com/tlcsdm/core/static/icon/xlsx.png");
            case "xml" -> ImageViewHelper.class.getResource("/com/tlcsdm/core/static/icon/xml.png");
            case "test" -> ImageViewHelper.class.getResource("/com/tlcsdm/core/static/icon/test.png");
            case "html" -> ImageViewHelper.class.getResource("/com/tlcsdm/core/static/icon/html.png");
            case "c" -> ImageViewHelper.class.getResource("/com/tlcsdm/core/static/icon/c.png");
            case "h" -> ImageViewHelper.class.getResource("/com/tlcsdm/core/static/icon/h.png");
            case "code" -> ImageViewHelper.class.getResource("/com/tlcsdm/core/static/icon/code.png");
            case "date" -> ImageViewHelper.class.getResource("/com/tlcsdm/core/static/icon/date.png");
            case "develop" -> ImageViewHelper.class.getResource("/com/tlcsdm/core/static/icon/develop.png");
            case "tune" -> ImageViewHelper.class.getResource("/com/tlcsdm/core/static/icon/tune.png");
            case "game" -> ImageViewHelper.class.getResource("/com/tlcsdm/core/static/icon/game.png");
            case "auxiliary" -> ImageViewHelper.class.getResource("/com/tlcsdm/core/static/icon/auxiliary.png");
            case "copilot" -> ImageViewHelper.class.getResource("/com/tlcsdm/core/static/icon/copilot.png");
            case "escape" -> ImageViewHelper.class.getResource("/com/tlcsdm/core/static/icon/escape.png");
            case "search" -> ImageViewHelper.class.getResource("/com/tlcsdm/core/static/icon/search.png");
            case "json" -> ImageViewHelper.class.getResource("/com/tlcsdm/core/static/icon/json.png");
            case "java" -> ImageViewHelper.class.getResource("/com/tlcsdm/core/static/icon/java.png");
            case "groovy" -> ImageViewHelper.class.getResource("/com/tlcsdm/core/static/icon/groovy.png");
            case "sql" -> ImageViewHelper.class.getResource("/com/tlcsdm/core/static/icon/sql.png");
            case "javascript" -> ImageViewHelper.class.getResource("/com/tlcsdm/core/static/icon/javascript.png");
            case "csv" -> ImageViewHelper.class.getResource("/com/tlcsdm/core/static/icon/csv.png");
            case "markdown" -> ImageViewHelper.class.getResource("/com/tlcsdm/core/static/icon/markdown.png");
            case "rgb" -> ImageViewHelper.class.getResource("/com/tlcsdm/core/static/icon/rgb.png");
            case "icon" -> ImageViewHelper.class.getResource("/com/tlcsdm/core/static/icon/icon.png");
            case "qrcode" -> ImageViewHelper.class.getResource("/com/tlcsdm/core/static/icon/qrcode.png");
            case "internet" -> ImageViewHelper.class.getResource("/com/tlcsdm/core/static/icon/internet.png");
            case "encrypt" -> ImageViewHelper.class.getResource("/com/tlcsdm/core/static/icon/encrypt.png");
            case "decrypt" -> ImageViewHelper.class.getResource("/com/tlcsdm/core/static/icon/decrypt.png");
            case "convert" -> ImageViewHelper.class.getResource("/com/tlcsdm/core/static/icon/convert.png");
            case "url" -> ImageViewHelper.class.getResource("/com/tlcsdm/core/static/icon/url.png");
            case "debug" -> ImageViewHelper.class.getResource("/com/tlcsdm/core/static/icon/debug.png");
            case "http" -> ImageViewHelper.class.getResource("/com/tlcsdm/core/static/icon/http.png");
            default -> throw new IllegalStateException("Unexpected value: " + name);
        };

    }

}
