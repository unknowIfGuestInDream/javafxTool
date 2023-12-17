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

import cn.hutool.log.StaticLog;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 常用的基本的关于编码和解码的函数的集合.
 *
 * @author unknowIfGuestInDream
 */
public class HashUtil {
    /**
     * 散列算法-MD5.
     */
    public static final String HASH_MD5 = "MD5";
    /**
     * 散列算法-SHA1.
     */
    public static final String HASH_SHA1 = "SHA-1";
    /**
     * 散列算法-SHA256.
     */
    public static final String HASH_SHA256 = "SHA-256";

    private static final char[] HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private HashUtil() {
    }

    /**
     * 字节数组散列.
     *
     * @param algorithm 散列算法
     * @param bytes     被散列的字节数组
     * @return 散列结果，全小写字母
     * @throws NoSuchAlgorithmException 当未找到指定的散列算法时抛出异常
     */
    @Nonnull
    public static String hash(@Nonnull String algorithm, @Nonnull byte[] bytes) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        messageDigest.update(bytes);
        return bytesToHex(messageDigest.digest());
    }

    /**
     * 文件散列.
     *
     * @param algorithm 散列算法
     * @param file      被散列的文件
     * @return 散列结果，全小写字母
     * @throws IOException              当文件未找到或者输入输出时错误时抛出异常
     * @throws NoSuchAlgorithmException 当未找到指定的散列算法时抛出异常
     */
    @Nonnull
    public static String hash(@Nonnull String algorithm, @Nonnull File file) throws IOException, NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        try (FileInputStream fileInputStream = new FileInputStream(file);
             DigestInputStream digestInputStream = new DigestInputStream(fileInputStream, messageDigest)) {
            byte[] buffer = new byte[131072];
            while (true) {
                if (digestInputStream.read(buffer) <= 0) {
                    break;
                }
            }
            return bytesToHex(digestInputStream.getMessageDigest().digest());
        }
    }

    /**
     * 将字节数组转换成16进制字符串.
     *
     * @param bytes 要转换的字节数组
     * @return 转换后的字符串，全小写字母
     */
    @Nonnull
    public static String bytesToHex(@Nonnull byte[] bytes) {
        char[] chars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            byte b = bytes[i];
            chars[i << 1] = HEX[b >>> 4 & 0xf];
            chars[(i << 1) + 1] = HEX[b & 0xf];
        }
        return new String(chars);
    }

    /**
     * 字符串MD5散列.
     *
     * @param str 被散列的字符串
     * @return 散列结果，全小写字母
     */
    @Nullable
    public static String md5(@Nonnull String str) {
        try {
            return hash(HASH_MD5, str.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            StaticLog.error(e);
            return null;
        }
    }

    /**
     * 文件MD5散列.
     *
     * @param file 被散列的文件
     * @return 散列结果，全小写字母
     */
    @Nullable
    public static String md5(@Nonnull File file) {
        try {
            return hash(HASH_MD5, file);
        } catch (IOException | NoSuchAlgorithmException e) {
            StaticLog.error(e);
            return null;
        }
    }

    /**
     * 字符串SHA1散列.
     *
     * @param str 被散列的字符串
     * @return 散列结果，全小写字母
     */
    @Nullable
    public static String sha1(@Nonnull String str) {
        try {
            return hash(HASH_SHA1, str.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            StaticLog.error(e);
            return null;
        }
    }

    /**
     * 文件SHA1散列.
     *
     * @param file 被散列的文件
     * @return 散列结果，全小写字母
     */
    @Nullable
    public static String sha1(@Nonnull File file) {
        try {
            return hash(HASH_SHA1, file);
        } catch (IOException | NoSuchAlgorithmException e) {
            StaticLog.error(e);
            return null;
        }
    }
}
