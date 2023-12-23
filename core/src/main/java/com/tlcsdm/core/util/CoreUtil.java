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

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.log.StaticLog;
import com.tlcsdm.core.annotation.Order;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

/**
 * 工具包
 */
public class CoreUtil {

    private CoreUtil() {
    }

    /**
     * Object为null时, toString()会返回 "null". 本方法会在Object为null时返回空串.
     */
    public static String valueOf(Object obj) {
        return (obj == null) ? "" : obj.toString();
    }

    /**
     * 首字母小写
     */
    public static String toLowerCase4Index(String string) {
        if (Character.isLowerCase(string.charAt(0))) {
            return string;
        }

        char[] chars = string.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    /**
     * 首字母大写
     */
    public static String toUpperCase4Index(String string) {
        char[] chars = string.toCharArray();
        chars[0] = toUpperCase(chars[0]);
        return String.valueOf(chars);
    }

    /**
     * 字符转成大写
     */
    public static char toUpperCase(char chars) {
        if (97 <= chars && chars <= 122) {
            chars ^= 32;
        }
        return chars;
    }

    /**
     * 根据容量获取map初始大小, 参考JDK8中putAll方法中的实现以及guava的newHashMapWithExpectedSize方法
     *
     * @param expectedSize 容量大小
     */
    public static int newHashMapWithExpectedSize(int expectedSize) {
        if (expectedSize < 3) {
            return 4;
        } else {
            return expectedSize < 1073741824 ? (int) ((float) expectedSize / 0.75F + 1.0F) : 2147483647;
        }
    }

    /**
     * 深度克隆
     */
    public static Object deepClone(Object object) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            if (object != null) {
                objectOutputStream.writeObject(object);
            }
            ObjectInputStream objectInputStream = new ObjectInputStream(
                new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
            return objectInputStream.readObject();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 使用默认浏览器打开url
     */
    public static void openWeb(String url) {
        Desktop d = Desktop.getDesktop();
        try {
            URI address = new URI(url);
            d.browse(address);
        } catch (URISyntaxException | IOException e) {
            StaticLog.error(e);
        }
    }

    /**
     * 根据Order注解排序
     * 不存在注解则排到最后
     */
    public static void sortByOrder(List<Class<?>> list) {
        list.sort((c1, c2) -> {
            Integer p1 = AnnotationUtil.getAnnotationValue(c1, Order.class);
            Integer p2 = AnnotationUtil.getAnnotationValue(c2, Order.class);
            if (p1 == null && p2 == null) {
                return 0;
            }
            if (p1 == null) {
                return 1;
            }
            if (p2 == null) {
                return -1;
            }
            return Integer.compare(p1, p2);
        });
    }

    /**
     * 获取项目根目录
     * eclipse下的根目录和idea不同
     */
    public static String getRootPath() {
        String path = System.getProperty("user.dir");
        if (isStartupFromJar(CoreUtil.class)) {
            return path;
        }
        File file = new File(path);
        List<File> list = FileUtil.loopFiles(file.toPath(), 1,
            pathname -> "core".equals(pathname.getName()));
        return !list.isEmpty() ? path : file.getParent();
    }

    /**
     * 获取运行位置下runtime文件夹.
     */
    public static String getRuntimePath() {
        return getRootPath() + File.separator + "runtime";
    }

    /**
     * 判断类是否在jar中
     */
    public static boolean isStartupFromJar(Class<?> clazz) {
        URL url = clazz.getResource("");
        if (url != null) {
            return "jar".equals(url.getProtocol());
        }
        return false;
    }

    /**
     * 类是否存在，存在返回true.
     */
    public static boolean hasClass(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * 从url中获取域名.
     */
    public static String getDomainName(String url) {
        String host = "";
        try {
            URL Url = new URL(url);
            host = Url.getHost();
        } catch (Exception e) {
            StaticLog.error(e);
        }
        return host;
    }
}
