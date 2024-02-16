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

package com.tlcsdm.core.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Test for common-compress.
 *
 * @author unknowIfGuestInDream
 */
@DisabledIfSystemProperty(named = "workEnv", matches = "ci")
public class CompressTest {

    @Test
    public void testZipFile_use_file() {
        String srcPath = "E:\\testPlace\\compress\\aa\\2.png";
        String targetPath = "E:\\testPlace\\compress\\ecm.zip";
        CompressUtil.zipFile(srcPath, targetPath);
    }

    @Test
    public void testZipFile_use_dir() {
        String srcPath = "E:\\testPlace\\compress\\aa\\";
        String targetPath = "E:\\testPlace\\compress\\ecm.zip";
        CompressUtil.zipFile(srcPath, targetPath);
    }

    @Test
    public void unzip() throws FileNotFoundException {
        String targetPath = "E:\\testPlace\\compress\\ecm";
        CompressUtil.unzip(new FileInputStream("E:\\testPlace\\compress\\ecm.zip"), targetPath);
    }

    @Test
    public void zipFiles() throws IOException {
        String targetPath = "E:\\testPlace\\compress\\aa.zip";
        CompressUtil.zipFiles(new File("E:\\testPlace\\compress\\aa"), new File(targetPath));
    }
}
