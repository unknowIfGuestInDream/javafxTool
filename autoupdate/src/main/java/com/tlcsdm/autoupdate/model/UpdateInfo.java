/*
 * Copyright (c) 2026 unknowIfGuestInDream.
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

package com.tlcsdm.autoupdate.model;

import java.util.Objects;

/**
 * 描述一次可用更新的元数据.
 *
 * <p>由应用模块的版本检查逻辑填充, 传递给自动更新流程使用. 通过 {@link Builder} 构建,
 * 只有 {@code downloadUrl} 是必填项, 其余字段用于增强下载与提示体验.</p>
 *
 * @author unknowIfGuestInDream
 */
public final class UpdateInfo {

    private final String version;
    private final String currentVersion;
    private final String downloadUrl;
    private final String fileName;
    private final String releaseNotes;
    private final String releaseUrl;
    private final long size;
    private final String sha256;

    private UpdateInfo(Builder builder) {
        this.version = builder.version;
        this.currentVersion = builder.currentVersion;
        this.downloadUrl = builder.downloadUrl;
        this.fileName = builder.fileName;
        this.releaseNotes = builder.releaseNotes;
        this.releaseUrl = builder.releaseUrl;
        this.size = builder.size;
        this.sha256 = builder.sha256;
    }

    /**
     * 创建一个新的 {@link Builder}.
     *
     * @return 构建器实例
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 最新版本号, 例如 {@code 1.2.0}.
     *
     * @return 最新版本号
     */
    public String getVersion() {
        return version;
    }

    /**
     * 当前运行的版本号.
     *
     * @return 当前版本号
     */
    public String getCurrentVersion() {
        return currentVersion;
    }

    /**
     * 更新包的下载地址 (必填).
     *
     * @return 下载地址
     */
    public String getDownloadUrl() {
        return downloadUrl;
    }

    /**
     * 下载后保存的文件名, 未指定时从下载地址推断.
     *
     * @return 文件名
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * 版本更新说明 (release body).
     *
     * @return 更新说明
     */
    public String getReleaseNotes() {
        return releaseNotes;
    }

    /**
     * 发布页地址, 用于在无法自动更新时提供手动下载入口.
     *
     * @return 发布页地址
     */
    public String getReleaseUrl() {
        return releaseUrl;
    }

    /**
     * 更新包大小 (字节), 未知时为 {@code 0}.
     *
     * @return 文件大小
     */
    public long getSize() {
        return size;
    }

    /**
     * 更新包的 SHA-256 校验值 (小写十六进制), 未提供时为 {@code null}.
     *
     * @return SHA-256 校验值
     */
    public String getSha256() {
        return sha256;
    }

    /**
     * 是否配置了校验值.
     *
     * @return 存在 SHA-256 校验值时返回 {@code true}
     */
    public boolean hasChecksum() {
        return sha256 != null && !sha256.isBlank();
    }

    @Override
    public String toString() {
        return "UpdateInfo{version=" + version + ", currentVersion=" + currentVersion + ", downloadUrl=" + downloadUrl
            + ", fileName=" + fileName + ", size=" + size + '}';
    }

    /**
     * {@link UpdateInfo} 的构建器.
     *
     * @author unknowIfGuestInDream
     */
    public static final class Builder {

        private String version;
        private String currentVersion;
        private String downloadUrl;
        private String fileName;
        private String releaseNotes;
        private String releaseUrl;
        private long size;
        private String sha256;

        private Builder() {
        }

        /**
         * 设置最新版本号.
         *
         * @param version 最新版本号
         * @return 当前构建器
         */
        public Builder version(String version) {
            this.version = version;
            return this;
        }

        /**
         * 设置当前运行的版本号.
         *
         * @param currentVersion 当前版本号
         * @return 当前构建器
         */
        public Builder currentVersion(String currentVersion) {
            this.currentVersion = currentVersion;
            return this;
        }

        /**
         * 设置更新包下载地址.
         *
         * @param downloadUrl 下载地址
         * @return 当前构建器
         */
        public Builder downloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
            return this;
        }

        /**
         * 设置下载后保存的文件名.
         *
         * @param fileName 文件名
         * @return 当前构建器
         */
        public Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        /**
         * 设置版本更新说明.
         *
         * @param releaseNotes 更新说明
         * @return 当前构建器
         */
        public Builder releaseNotes(String releaseNotes) {
            this.releaseNotes = releaseNotes;
            return this;
        }

        /**
         * 设置发布页地址.
         *
         * @param releaseUrl 发布页地址
         * @return 当前构建器
         */
        public Builder releaseUrl(String releaseUrl) {
            this.releaseUrl = releaseUrl;
            return this;
        }

        /**
         * 设置更新包大小 (字节).
         *
         * @param size 文件大小
         * @return 当前构建器
         */
        public Builder size(long size) {
            this.size = size;
            return this;
        }

        /**
         * 设置更新包的 SHA-256 校验值.
         *
         * @param sha256 SHA-256 校验值
         * @return 当前构建器
         */
        public Builder sha256(String sha256) {
            this.sha256 = sha256;
            return this;
        }

        /**
         * 构建 {@link UpdateInfo} 实例.
         *
         * @return 不可变的更新信息
         * @throws IllegalArgumentException 当下载地址为空时抛出
         */
        public UpdateInfo build() {
            if (Objects.toString(downloadUrl, "").isBlank()) {
                throw new IllegalArgumentException("downloadUrl must not be blank");
            }
            if (Objects.toString(fileName, "").isBlank()) {
                this.fileName = guessFileName(downloadUrl);
            }
            return new UpdateInfo(this);
        }

        private static String guessFileName(String url) {
            String path = url;
            int query = path.indexOf('?');
            if (query >= 0) {
                path = path.substring(0, query);
            }
            int slash = path.lastIndexOf('/');
            String name = slash >= 0 ? path.substring(slash + 1) : path;
            return name.isBlank() ? "update.bin" : name;
        }
    }
}
