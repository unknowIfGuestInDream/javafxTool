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

package com.tlcsdm.core.util;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.DES;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.security.SecureRandom;

/**
 * @author unknowIfGuestInDream
 * @date 2023/1/11 19:57
 */
public class CryptoTest {

    @Test
    public void md5() {
        System.out.println(SecureUtil.md5("unknowIfGuestInDream"));
    }

    @Test
    public void aes1() {
        final SecureRandom random = RandomUtil.getSecureRandom("123456".getBytes());
        final SecretKey secretKey = KeyUtil.generateKey("AES", 128, random);

        String content = "12sdfsdfs你好啊！";
        AES aes = new AES(secretKey);
        final String result1 = aes.encryptBase64(content);
        System.out.println(result1);
        final String decryptStr = aes.decryptStr(result1);
        System.out.println(decryptStr);
    }

    /**
     * des
     */
    @Test
    public void des() {
        String content = "我是一个测试的test字符串123";
        final DES des = SecureUtil.des("unknowIfGuestInDream".getBytes());

        final String encryptHex = des.encryptHex(content);
        System.out.println(encryptHex);
        final String result = des.decryptStr(encryptHex);
        System.out.println(result);
    }

    /**
     * aes
     */
    @Test
    public void aes() {
//		AES aes = new AES(Mode.valueOf(Mode.ECB.name()), Padding.PKCS5Padding,
//				HexUtil.decodeHex("0102030405060708090a0b0c0d0e0f10"));
        AES aes = new AES(Mode.ECB, Padding.PKCS5Padding,
            "3f4eefd3525675154a5e3a0183d8087b".getBytes());
        String encryptStr = aes.encryptHex("16c5");
        System.out.println(encryptStr);
        System.out.println(aes.decryptStr(encryptStr));
    }

}
