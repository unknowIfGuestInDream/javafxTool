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

package com.tlcsdm.core.javafx.util;

import cn.hutool.log.StaticLog;
import com.tlcsdm.core.javafx.FxApp;
import com.tlcsdm.core.util.CoreUtil;
import com.tlcsdm.core.util.Win32Util;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.io.File;
import java.io.IOException;

/**
 * 系统工具.
 *
 * @author unknowIfGuestInDream
 */
public class OSUtil {

    private OSUtil() {
    }

    public enum OS {
        //操作系统
        WINDOWS, LINUX, MAC, UNKNOWN
    }

    private static OS os;

    public static OS getOS() {
        if (os == null) {
            String systemStr = System.getProperty("os.name").toLowerCase();
            if (systemStr.contains("win")) {
                os = OS.WINDOWS;
            } else if (systemStr.contains("nix") || systemStr.contains("nux") || systemStr.contains("aix")) {
                os = OS.LINUX;
            } else if (systemStr.contains("mac")) {
                os = OS.MAC;
            } else {
                os = OS.UNKNOWN;
            }
        }
        return os;
    }

    /**
     * 图片写入剪切板
     */
    public static void writeToClipboard(WritableImage writableImage) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putImage(writableImage);
        clipboard.setContent(content);
    }

    /**
     * 文本写入到剪切板
     */
    public static void writeToClipboard(String contentStr) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(contentStr);
        clipboard.setContent(content);
    }

    /**
     * 获得剪切板的文字
     */
    public static String getClipboardString() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        return clipboard.getString();
    }

    /**
     * 系统默认软件显示文档
     */
    public static void showDoc(String fileUri) {
        FxApp.hostServices.showDocument(fileUri);
    }

    /**
     * win mac linux 系统直接打开文件夹并选中文件
     * 其余系统打开文件夹
     */
    public static void openAndSelectedFile(String filePath) {
        //未知系统, 打开字体文件所在文件夹
        OS currentOS = getOS();
        if (currentOS == OS.UNKNOWN) {
            File dir = new File(filePath).getParentFile();
            showDoc(dir.toURI().toString());
            return;
        }
        File file = new File(filePath);
        //已知系统,用命令行打开文件夹,并选中文件
        filePath = "\"" + filePath + "\"";
        String cmd = "";
        if (currentOS == OS.WINDOWS) {
            if (file.exists() && file.isDirectory()) {
                cmd = "explorer " + filePath;
            } else {
                cmd = "explorer /select," + filePath;
            }
            try {
                Runtime.getRuntime().exec(cmd);
            } catch (IOException e) {
                StaticLog.error(e, "OpenAndSelectedFile failed.");
            }
        } else if (currentOS == OS.MAC || currentOS == OS.LINUX) {
            //cmd = "open " + filePath;  mac
            //cmd = "open -R " + filePath; linux
            showDoc(file.toURI().toString());
        }

    }

    public static void openAndSelectedFile(File file) {
        openAndSelectedFile(file.getPath());
    }

    /**
     * 设置程序到图层后.
     *
     * @param title 程序标题
     */
    public static void setWinIconAfter(String title) {
        if (OS.WINDOWS.equals(getOS())) {
            if (!CoreUtil.hasJna()) {
                return;
            }
            Win32Util.setWinIconAfter(title);
        }
    }

    /**
     * 设置程序到图层前.
     *
     * @param title 程序标题
     */
    public static void setWinIconTop(String title) {
        if (OS.WINDOWS.equals(getOS())) {
            if (!CoreUtil.hasJna()) {
                return;
            }
            Win32Util.setWinIconTop(title);
        }
    }

}


