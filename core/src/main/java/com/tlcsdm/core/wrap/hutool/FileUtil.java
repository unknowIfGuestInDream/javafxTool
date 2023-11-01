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

package com.tlcsdm.core.wrap.hutool;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

/**
 * {@link cn.hutool.core.io.FileUtil}
 * 
 * @author unknowIfGuestInDream
 */
public class FileUtil {

    /**
     * {@inheritDoc}
     */
    public static File file(String path) {
        return cn.hutool.core.io.FileUtil.file(path);
    }

    /**
     * {@inheritDoc}
     */
    public static boolean del(File file) {
        return cn.hutool.core.io.FileUtil.del(file);
    }

    /**
     * {@inheritDoc}
     */
    public static String readUtf8String(File file) {
        return cn.hutool.core.io.FileUtil.readUtf8String(file);
    }

    /**
     * {@inheritDoc}
     */
    public static boolean isEmpty(File file) {
        return cn.hutool.core.io.FileUtil.isEmpty(file);
    }

    /**
     * {@inheritDoc}
     */
    public static <T> File appendUtf8Lines(Collection<T> list, File file) {
        return cn.hutool.core.io.FileUtil.appendUtf8Lines(list, file);
    }

    /**
     * {@inheritDoc}
     */
    public static List<File> loopFiles(Path path, int maxDepth, FileFilter fileFilter) {
        return cn.hutool.core.io.FileUtil.loopFiles(path, maxDepth, fileFilter);
    }

}
