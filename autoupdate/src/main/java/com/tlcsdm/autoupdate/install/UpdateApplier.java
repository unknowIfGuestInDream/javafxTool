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

package com.tlcsdm.autoupdate.install;

import com.tlcsdm.core.javafx.util.ConfigureUtil;
import com.tlcsdm.core.javafx.util.OSUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;

/**
 * 更新包安装器.
 *
 * <p>提供两种应用更新的方式:</p>
 * <ol>
 *     <li>{@link #reveal(Path)} 在文件管理器中定位已下载的更新包 (安全默认, 由用户手动安装);</li>
 *     <li>{@link #applyAndRestart(Path, Path, String)} 生成并运行平台脚本, 在应用退出后
 *     解压覆盖安装目录并重新启动 (静默更新).</li>
 * </ol>
 *
 * <p>脚本内容由 {@link #buildUpdateScript(OSUtil.OS, Path, Path, String)} 生成, 该方法为纯函数,
 * 便于单元测试.</p>
 *
 * @author unknowIfGuestInDream
 */
public class UpdateApplier {

    /**
     * 应用退出前脚本等待的秒数, 用于释放被占用的文件.
     */
    private static final int WAIT_SECONDS = 3;

    /**
     * 在系统文件管理器中定位并选中已下载的更新包.
     *
     * @param packageFile 更新包路径
     */
    public void reveal(Path packageFile) {
        OSUtil.openAndSelectedFile(packageFile.toFile());
    }

    /**
     * 生成平台更新脚本, 写入临时文件并以独立进程启动.
     *
     * <p>调用方应在本方法返回后退出应用 (例如 {@code Platform.exit()}), 以便脚本能够替换
     * 正在使用的文件.</p>
     *
     * @param packageFile      已下载的更新包 ({@code .zip} 或安装程序)
     * @param installDir       需要覆盖更新的安装目录
     * @param relaunchCommand  更新完成后重新启动应用的命令, 为 {@code null} 时不重启
     * @throws IOException 写入或启动脚本失败时抛出
     */
    public void applyAndRestart(Path packageFile, Path installDir, String relaunchCommand) throws IOException {
        OSUtil.OS os = OSUtil.getOS();
        String script = buildUpdateScript(os, packageFile, installDir, relaunchCommand);
        boolean windows = os == OSUtil.OS.WINDOWS;
        String suffix = windows ? ".bat" : ".sh";
        Path scriptDir = Path.of(ConfigureUtil.getConfigurePath(), "update");
        Files.createDirectories(scriptDir);
        Path scriptFile = Files.createTempFile(scriptDir, "javafxtool-update-", suffix);
        Files.writeString(scriptFile, script);
        if (!windows) {
            makeExecutable(scriptFile);
        }
        ProcessBuilder builder = windows
            ? new ProcessBuilder(windowsShell(), "/c", "start", "", scriptFile.toString())
            : new ProcessBuilder("/bin/sh", scriptFile.toString());
        builder.directory(installDir.toFile());
        builder.redirectOutput(ProcessBuilder.Redirect.DISCARD);
        builder.redirectError(ProcessBuilder.Redirect.DISCARD);
        builder.start();
    }

    /**
     * 解析 Windows 命令解释器的绝对路径, 避免依赖 {@code PATH} 查找 {@code cmd}.
     *
     * <p>优先使用系统环境变量 {@code ComSpec}, 其次根据 {@code SystemRoot} 拼接,
     * 最终回退到默认安装位置.</p>
     *
     * @return {@code cmd.exe} 的绝对路径
     */
    private static String windowsShell() {
        String comSpec = System.getenv("ComSpec");
        if (comSpec != null && !comSpec.isBlank()) {
            return comSpec;
        }
        String systemRoot = System.getenv("SystemRoot");
        if (systemRoot == null || systemRoot.isBlank()) {
            systemRoot = "C:\\Windows";
        }
        return systemRoot + "\\System32\\cmd.exe";
    }

    /**
     * 构建平台更新脚本内容.
     *
     * <p>当更新包以 {@code .zip} 结尾时, 脚本会将其解压覆盖到安装目录; 否则视为安装程序直接运行.
     * 若提供了 {@code relaunchCommand}, 更新完成后会重新启动应用.</p>
     *
     * @param os              目标操作系统
     * @param packageFile     已下载的更新包
     * @param installDir      需要覆盖更新的安装目录
     * @param relaunchCommand 重新启动应用的命令, 可为 {@code null}
     * @return 脚本文本
     */
    public static String buildUpdateScript(OSUtil.OS os, Path packageFile, Path installDir, String relaunchCommand) {
        String pkg = packageFile.toString();
        String dir = installDir.toString();
        boolean zip = pkg.toLowerCase().endsWith(".zip");
        boolean relaunch = relaunchCommand != null && !relaunchCommand.isBlank();
        if (os == OSUtil.OS.WINDOWS) {
            StringBuilder sb = new StringBuilder();
            sb.append("@echo off\n");
            sb.append("timeout /t ").append(WAIT_SECONDS).append(" /nobreak >nul\n");
            if (zip) {
                sb.append("powershell -NoProfile -Command \"Expand-Archive -LiteralPath '").append(pkg)
                    .append("' -DestinationPath '").append(dir).append("' -Force\"\n");
            } else {
                sb.append("start \"\" \"").append(pkg).append("\"\n");
            }
            if (relaunch) {
                sb.append("start \"\" \"").append(relaunchCommand).append("\"\n");
            }
            sb.append("del \"%~f0\"\n");
            return sb.toString();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("#!/bin/sh\n");
        sb.append("sleep ").append(WAIT_SECONDS).append('\n');
        if (zip) {
            sb.append("unzip -o \"").append(pkg).append("\" -d \"").append(dir).append("\"\n");
        } else {
            sb.append("\"").append(pkg).append("\"\n");
        }
        if (relaunch) {
            sb.append("\"").append(relaunchCommand).append("\" &\n");
        }
        sb.append("rm -- \"$0\"\n");
        return sb.toString();
    }

    private static void makeExecutable(Path scriptFile) throws IOException {
        try {
            Set<PosixFilePermission> permissions = Files.getPosixFilePermissions(scriptFile);
            permissions.add(PosixFilePermission.OWNER_EXECUTE);
            Files.setPosixFilePermissions(scriptFile, permissions);
        } catch (UnsupportedOperationException e) {
            // Non-POSIX file system, fall back to File#setExecutable
            File file = scriptFile.toFile();
            if (!file.setExecutable(true)) {
                throw new IOException("Unable to mark update script as executable: " + scriptFile);
            }
        }
    }
}
