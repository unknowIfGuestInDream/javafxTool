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

package com.tlcsdm.core.freemarker;

import com.tlcsdm.core.exception.CoreException;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author unknowIfGuestInDream
 */
class CodeGenTest {

    @Test
    void test() throws IOException {
        String installDirName = "jfxtool";
        System.out.println(System.getProperty("java.io.tmpdir"));
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        File installDir = new File(tempDir, installDirName);
        if (installDir.exists()) {
            FileUtils.deleteDirectory(installDir);
        }
        installDir.mkdirs();
        System.out.println(Files.createTempDirectory(installDirName));
    }

    private void unzip(String zipPath, String destDirPath) throws IOException, CoreException {
        byte[] buf = new byte[8192];
        File destDir = new File(destDirPath);
        try (InputStream zipIn = new FileInputStream(zipPath); ZipInputStream zis = new ZipInputStream(
            new BufferedInputStream(zipIn))) {
            ZipEntry zEntry;
            while ((zEntry = zis.getNextEntry()) != null) {
                // if it is empty directory, create it
                if (zEntry.isDirectory()) {
                    new File(destDir, zEntry.getName()).mkdirs();
                    continue;
                }
                // if it is a file, extract it
                String filePath = zEntry.getName();
                int lastSeparator = filePath.lastIndexOf("/");
                String fileDir = "";
                if (lastSeparator >= 0) {
                    fileDir = filePath.substring(0, lastSeparator);
                }
                // create directory for a file
                new File(destDir, fileDir).mkdirs();
                // write file
                String destDirCanonicalPath = destDir.getCanonicalPath();
                File outFile = new File(destDir, filePath);
                String outFileCanonicalPath = outFile.getCanonicalPath();
                if (!outFileCanonicalPath.startsWith(destDirCanonicalPath + File.separator)) {
                    throw new CoreException("Entry is outside of the target dir: " + filePath);
                }
                try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outFile))) {
                    int n;
                    while ((n = zis.read(buf)) >= 0) {
                        outputStream.write(buf, 0, n);
                    }
                }
            }
        }
    }

}

