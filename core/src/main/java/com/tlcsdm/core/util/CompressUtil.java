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

import cn.hutool.log.StaticLog;
import com.tlcsdm.core.exception.CoreException;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * common-compress封装.
 *
 * @author unknowIfGuestInDream
 */
public class CompressUtil {

    private CompressUtil() {
    }

    /**
     * 解压zip包至目标目录下，若目录不存在会自动新建；
     * utf-8编码的zip文件中存在gbk编码的文件和文件夹，解码会有乱码
     * 文件夹名称存在中文的，新建文件夹会失败，采取跳过的策略
     * 若存在中文命名的文件会抛出异常，采取跳过的策略
     *
     * @param inputStream 输入的文件流
     * @param destDir     解压的目标地址
     */
    public static void unzip(InputStream inputStream, String destDir) {
        ArchiveEntry zipEntry;
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(
            inputStream); ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(bufferedInputStream)) {
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                File file = new File(destDir, zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    boolean mkdirs = file.mkdirs();
                    if (!mkdirs) {
                        StaticLog.warn("make dir fails, dir exists Chinese");
                    }
                } else {
                    try (FileOutputStream outPut = FileUtils.openOutputStream(
                        file); BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outPut)) {
                        IOUtils.copy(zipInputStream, bufferedOutputStream, 8192);
                    } catch (IOException e) {
                        StaticLog.warn("file exists Chinese");
                    }
                }
            }
        } catch (IOException e) {
            StaticLog.error("have an IOException", e);
            throw new CoreException("Failed to decompress", e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                StaticLog.error("failed to close the input stream");
            }
        }
    }

    /**
     * 压缩文件(文件夹)为zip包
     *
     * @param sourceFile
     * @param targetZipFile
     */
    public static void zipFiles(File sourceFile, File targetZipFile) throws IOException {
        try (ZipOutputStream outputStream = new ZipOutputStream(new FileOutputStream(targetZipFile))) {
            for (File file : Objects.requireNonNull(sourceFile.listFiles())) {
                addEntry("", file, outputStream);
            }
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    private static void addEntry(String dir, File inFile, ZipOutputStream out) throws IOException {
        if (inFile.isDirectory()) {
            File[] files = inFile.listFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    String name = inFile.getName();
                    if (!"".equals(dir)) {
                        name = dir + "/" + name;
                    }
                    addEntry(name, file, out);
                }
            }
        } else {
            doZip(inFile, out, dir);
        }

    }

    private static void doZip(File inFile, ZipOutputStream out, String dir) throws IOException {
        String entryName;
        if (!"".equals(dir)) {
            entryName = dir + "/" + inFile.getName();
        } else {
            entryName = inFile.getName();
        }
        ZipEntry entry = new ZipEntry(entryName);
        out.putNextEntry(entry);

        int len;
        byte[] buffer = new byte[1024];
        FileInputStream fis = new FileInputStream(inFile);
        while ((len = fis.read(buffer)) > 0) {
            out.write(buffer, 0, len);
            out.flush();
        }
        out.closeEntry();
        fis.close();
    }

    /**
     * 支持单文件或多层文件夹的压缩
     *
     * @param srcPath
     * @param targetPath
     */
    public static void zipFile(String srcPath, String targetPath) {
        int length;
        File file = new File(srcPath);
        List<File> filesToArchive;
        if (file.isDirectory()) {
            filesToArchive = getAllFile(new File(srcPath));
            length = srcPath.length();
        } else {
            filesToArchive = Collections.singletonList(file);
            length = file.getParent().length() + 1;
        }
        try (ArchiveOutputStream<ZipArchiveEntry> o = new ZipArchiveOutputStream(new File(targetPath))) {
            for (File f : filesToArchive) {
                ZipArchiveEntry entry = o.createArchiveEntry(f, f.getPath().substring(length));
                o.putArchiveEntry(entry);
                if (f.isFile()) {
                    try (InputStream i = Files.newInputStream(f.toPath())) {
                        IOUtils.copy(i, o);
                    }
                }
                o.closeArchiveEntry();
            }
        } catch (IOException e) {
            StaticLog.error("zipFile fails", e);
        }
    }

    public static List<File> getAllFile(File dirFile) {
        File[] childrenFiles = dirFile.listFiles();
        if (Objects.isNull(childrenFiles) || childrenFiles.length == 0) {
            return Collections.emptyList();
        }
        List<File> files = new ArrayList<>();
        for (File childFile : childrenFiles) {
            if (childFile.isFile()) {
                files.add(childFile);
            } else {
                files.add(childFile);
                List<File> cFiles = getAllFile(childFile);
                if (cFiles.isEmpty()) {
                    continue;
                }
                files.addAll(cFiles);
            }
        }
        return files;
    }

}
