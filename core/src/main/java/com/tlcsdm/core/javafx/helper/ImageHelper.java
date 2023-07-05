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

package com.tlcsdm.core.javafx.helper;

import com.tlcsdm.core.exception.CoreException;
import javafx.scene.image.Image;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * @author: unknowIfGuestInDream
 * @date: 2023/1/14 18:34
 */
public class ImageHelper {
    public static final String CLASSPATH_PREFIX = "classpath:";
    public static final String HTTP_PREFIX = "http:";
    public static final String HTTPS_PREFIX = "https:";
    public static final String FILE_PREFIX = "file:";

    public ImageHelper() {
    }

    public static Image image(String path) {
        try {
            String lowerCasePath = path.toLowerCase();
            Image image;
            if (lowerCasePath.startsWith(CLASSPATH_PREFIX)) {
                image = new Image(Objects.requireNonNull(ImageHelper.class.getResourceAsStream(path.substring(CLASSPATH_PREFIX.length()))));
            } else if (!lowerCasePath.startsWith(HTTP_PREFIX) && !lowerCasePath.startsWith(HTTPS_PREFIX) && !lowerCasePath.startsWith(FILE_PREFIX)) {
                image = new Image(Files.newInputStream(Paths.get(path)));
            } else {
                image = new Image(path);
            }

            return image;
        } catch (IOException e) {
            throw new CoreException(e);
        }
    }
}
