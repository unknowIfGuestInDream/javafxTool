package com.tlcsdm.core.javafx.util;

import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

/**
 * @author unknowIfGuestInDream
 */
public class OSUtil {

    private OSUtil() {
    }

    public enum OS {
        //操作系统
        WINDOWS,
        LINUX,
        MAC,
        UNKNOWN
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

}


