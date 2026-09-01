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

package com.tlcsdm.autoupdate;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 控制 {@link AutoUpdate} 行为的可选参数.
 *
 * <p>默认情况下只会把安装包下载到临时目录并在完成后定位文件, 不会自动覆盖安装,
 * 这是最安全的行为. 如果希望下载完成后自动解压覆盖并重启, 需要通过
 * {@link Builder#autoApply(boolean)} 打开开关, 并设置 {@link Builder#installDir(Path)}
 * 与 {@link Builder#relaunchCommand(String)}.</p>
 *
 * @author unknowIfGuestInDream
 */
public class AutoUpdateOptions {

    private final Path targetDir;
    private final boolean autoApply;
    private final Path installDir;
    private final String relaunchCommand;

    private AutoUpdateOptions(final Builder builder) {
        this.targetDir = builder.targetDir;
        this.autoApply = builder.autoApply;
        this.installDir = builder.installDir;
        this.relaunchCommand = builder.relaunchCommand;
    }

    /**
     * 返回一份使用默认值的配置.
     *
     * @return 默认配置
     */
    public static AutoUpdateOptions defaults() {
        return new Builder().build();
    }

    /**
     * 安装包下载到的目录.
     *
     * @return 下载目录
     */
    public Path getTargetDir() {
        return targetDir;
    }

    /**
     * 是否在下载完成后自动解压覆盖并重启.
     *
     * @return 是否自动应用更新
     */
    public boolean isAutoApply() {
        return autoApply;
    }

    /**
     * 需要被覆盖的安装目录, 仅当 {@link #isAutoApply()} 为 true 时使用.
     *
     * @return 安装目录, 可能为 {@code null}
     */
    public Path getInstallDir() {
        return installDir;
    }

    /**
     * 更新完成后用于重启应用的命令, 仅当 {@link #isAutoApply()} 为 true 时使用.
     *
     * @return 重启命令, 可能为 {@code null}
     */
    public String getRelaunchCommand() {
        return relaunchCommand;
    }

    /**
     * {@link AutoUpdateOptions} 的构建器.
     *
     * @author unknowIfGuestInDream
     */
    public static class Builder {

        private Path targetDir = Paths.get(System.getProperty("java.io.tmpdir"), "javafxTool-update");
        private boolean autoApply;
        private Path installDir;
        private String relaunchCommand;

        /**
         * 设置安装包下载目录.
         *
         * @param dir 下载目录
         * @return 当前构建器
         */
        public Builder targetDir(final Path dir) {
            if (dir != null) {
                this.targetDir = dir;
            }
            return this;
        }

        /**
         * 设置是否在下载完成后自动解压覆盖并重启.
         *
         * @param apply 是否自动应用更新
         * @return 当前构建器
         */
        public Builder autoApply(final boolean apply) {
            this.autoApply = apply;
            return this;
        }

        /**
         * 设置需要被覆盖的安装目录.
         *
         * @param dir 安装目录
         * @return 当前构建器
         */
        public Builder installDir(final Path dir) {
            this.installDir = dir;
            return this;
        }

        /**
         * 设置更新完成后用于重启应用的命令.
         *
         * @param command 重启命令
         * @return 当前构建器
         */
        public Builder relaunchCommand(final String command) {
            this.relaunchCommand = command;
            return this;
        }

        /**
         * 构建 {@link AutoUpdateOptions} 实例.
         *
         * @return 配置实例
         */
        public AutoUpdateOptions build() {
            return new AutoUpdateOptions(this);
        }
    }
}
