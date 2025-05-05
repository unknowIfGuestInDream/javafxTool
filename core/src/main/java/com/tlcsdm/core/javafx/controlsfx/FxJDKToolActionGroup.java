/*
 * Copyright (c) 2023 unknowIfGuestInDream.
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

package com.tlcsdm.core.javafx.controlsfx;

import cn.hutool.log.StaticLog;
import com.tlcsdm.core.javafx.dialog.ExceptionDialog;
import com.tlcsdm.core.javafx.helper.LayoutHelper;
import com.tlcsdm.core.javafx.util.OSUtil;
import org.controlsfx.control.action.Action;
import org.controlsfx.control.action.ActionGroup;

import java.io.File;
import java.io.IOException;

/**
 * JDK Tool ActionGroup.
 *
 * @author unknowIfGuestInDream
 */
public class FxJDKToolActionGroup {
    Action jmc;
    Action jconsole;
    Action jvisualvm;
    Action jshell;
    private static final String javaHome = System.getProperty("java.home");
    private static final String javaHomeEnv = System.getenv("JAVA_HOME");
    private final String jmcRelativePath;
    private final String jconsoleRelativePath;
    private final String jvisualvmRelativePath;
    private final String jshellRelativePath;

    public FxJDKToolActionGroup() {
        boolean isWin = OSUtil.getOS().equals(OSUtil.OS.WINDOWS);
        jmcRelativePath = "bin" + File.separator + (isWin ? "jmc.exe" : "jmc");
        jconsoleRelativePath = "bin" + File.separator + (isWin ? "jconsole.exe" : "jconsole");
        jvisualvmRelativePath = "bin" + File.separator + (isWin ? "jvisualvm.exe" : "jvisualvm");
        jshellRelativePath = "bin" + File.separator + (isWin ? "jshell.exe" : "jshell");

        jmc = createToolAction("jmc", jmcRelativePath, "/com/tlcsdm/core/static/icon/jmc.png");
        jconsole = createToolAction("jConsole", jconsoleRelativePath, "/com/tlcsdm/core/static/icon/java.png");
        jvisualvm = createToolAction("jvisualvm", jvisualvmRelativePath, "/com/tlcsdm/core/static/icon/jvisualvm.png");
        jshell = createToolAction("jshell", jshellRelativePath, "/com/tlcsdm/core/static/icon/jshell.png");
    }

    private Action createToolAction(String name, String relativePath, String iconPath) {
        Action action = new Action(name, e -> {
            File file = findToolExecutable(name, relativePath);
            if (file != null) {
                runProgram(file.getAbsolutePath());
            }
        });
        action.setGraphic(LayoutHelper.iconView(FxJDKToolActionGroup.class.getResource(iconPath)));

        // 检查工具是否存在
        boolean toolExists = findToolExecutable(name, relativePath) != null;
        if (!toolExists) {
            action.setDisabled(true);
        }

        return action;
    }

    private File findToolExecutable(String toolName, String relativePath) {
        File file = new File(javaHome, relativePath);
        if (file.exists()) {
            return file;
        }

        file = new File(javaHomeEnv, relativePath);
        if (file.exists()) {
            return file;
        }

        StaticLog.warn("Not found " + toolName + ".");
        return null;
    }

    private void runProgram(String filePath) {
        ProcessBuilder builder;
        if (OSUtil.getOS().equals(OSUtil.OS.WINDOWS)) {
            builder = new ProcessBuilder("cmd", "/c", "start", filePath);
        } else {
            builder = new ProcessBuilder("sh", "-c", filePath);
        }
        try {
            builder.start();
        } catch (IOException ex) {
            new ExceptionDialog(ex).show();
        }
    }

    public ActionGroup create() {
        return FxActionGroup.jdkTool(jmc, jconsole, jvisualvm, jshell);
    }
}
