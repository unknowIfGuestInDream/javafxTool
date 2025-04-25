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

package com.tlcsdm.core.util;

import cn.hutool.core.io.resource.ResourceUtil;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Platform;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.win32.StdCallLibrary;

import java.io.File;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

/**
 * JNA 测试.
 */
public class JnaTest {

    /**
     * @see <a href="https://github.com/java-native-access/jna/blob/master/www/GettingStarted.md">...</a>
     */
    @Test
    void hello() {
        CLibrary.INSTANCE.printf("Hello, World\n");
        for (int i = 0; i < 5; i++) {
            CLibrary.INSTANCE.printf("Argument %d: %s\n", i);
        }
    }

    /**
     * Windows kernel32 library.
     */
    @Test
    @EnabledOnOs({OS.WINDOWS})
    void kernel32() {
        Kernel32 INSTANCE = Native.load("kernel32", Kernel32.class);
        // Optional: wraps every call to the native library in a
        // synchronized block, limiting native calls to one at a time
        Kernel32 SYNC_INSTANCE = (Kernel32) Native.synchronizedLibrary(INSTANCE);
        SYSTEMTIME time = new SYSTEMTIME();
        SYNC_INSTANCE.GetSystemTime(time);

        System.out.println("Today's integer value is " + time.wDay);
    }

    @Test
    @EnabledOnOs({OS.WINDOWS})
    void window() {
        WinDef.HWND hwnd = User32.INSTANCE.FindWindow(null, "vol36-1-09.pdf - Adobe Acrobat Pro DC (64-bit)");
        if (hwnd == null) {
            System.out.println("pdf is not running");
        } else {
            WinDef.RECT win_rect = new WinDef.RECT();
            User32.INSTANCE.GetWindowRect(hwnd, win_rect);
            int win_width = win_rect.right - win_rect.left;
            int win_height = win_rect.bottom - win_rect.top;
            User32.INSTANCE.MoveWindow(hwnd, 300, 100, win_width, win_height, true);
        }
    }

    // This is the standard, stable way of mapping, which supports extensive
    // customization and mapping of Java to native types.
    public interface CLibrary extends Library {
        CLibrary INSTANCE = Native.load((Platform.isWindows() ? "msvcrt" : "c"), CLibrary.class);

        void printf(String format, Object... args);
    }

    // kernel32.dll uses the __stdcall calling convention (check the function
    // declaration for "WINAPI" or "PASCAL"), so extend StdCallLibrary
    // Most C libraries will just extend com.sun.jna.Library,
    public interface Kernel32 extends StdCallLibrary {
        // Method declarations, constant and structure definitions go here
        void GetSystemTime(SYSTEMTIME result);
    }

    @Structure.FieldOrder({"wYear", "wMonth", "wDayOfWeek", "wDay", "wHour", "wMinute", "wSecond", "wMilliseconds"})
    public static class SYSTEMTIME extends Structure {
        public short wYear;
        public short wMonth;
        public short wDayOfWeek;
        public short wDay;
        public short wHour;
        public short wMinute;
        public short wSecond;
        public short wMilliseconds;
    }

    public static class Direct {

        public static native double cos(double x);

        public static native double sin(double x);

        static {
            Native.register(Platform.C_LIBRARY_NAME);
        }

        public static void main(String[] args) {
            System.out.println("cos(0)=" + cos(0));
            System.out.println("sin(0)=" + sin(0));
        }
    }

    public interface DLibrary extends Library {

        //此处我的jdk版本为64位,故加载64位的Dll
        DLibrary INSTANCE = Native.load(new File(ResourceUtil.getResource("jna/Dll1.dll").getFile()).getAbsolutePath(),
            DLibrary.class);

        //Dll2x64中定义的函数
        int add(int a, int b);

        int substract(int a, int b);
    }

    @Test
    @EnabledOnOs({OS.WINDOWS})
    void c_dll_test() {
        //System.setProperty("jna.encoding", "GBK");
        //动态库初始化
        NativeLibrary INSTANCE = NativeLibrary.getInstance(
            new File(ResourceUtil.getResource("jna/Dll1.dll").getFile()).getAbsolutePath());
        int adres = INSTANCE.getFunction("add").invokeInt(new Object[]{3, 9});
        int subres = INSTANCE.getFunction("substract").invokeInt(new Object[]{30, 10});
        System.out.println("add: " + adres);
        System.out.println("substract: " + subres);
        //释放动态库连接，也可以不释放，没有太大关系
        INSTANCE.close();
    }

    @Test
    @EnabledOnOs({OS.WINDOWS})
    void c_dll_test_without_close() {
        System.out.println(DLibrary.INSTANCE.add(1, 2));
        System.out.println(DLibrary.INSTANCE.substract(10, 2));
    }
}
