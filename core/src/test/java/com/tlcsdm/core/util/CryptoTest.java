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

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.DES;

import java.security.SecureRandom;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author unknowIfGuestInDream
 * @date 2023/1/11 19:57
 */
public class CryptoTest {

    @Test
    public void md5() {
        Assertions.assertEquals("5f1dd408cc5d34848df67dd134026acf", SecureUtil.md5("unknowIfGuestInDream"));
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
        Assertions.assertEquals(decryptStr, content);
    }

    /**
     * des
     */
    @Test
    public void des() {
        String content = "我是一个测试的test字符串123";
        final DES des = SecureUtil.des("unknowIfGuestInDream".getBytes());
        final String encryptHex = des.encryptHex(content);
        Assertions.assertEquals("43d910f3e0f3c064d7419fb7b7000cd1f9df4163d1890936d5ead6fa69d9819f0b3a83eef966fdf6", encryptHex);
        final String result = des.decryptStr(encryptHex);
        Assertions.assertEquals(content, result);
    }

    /**
     * aes
     */
    @Test
    public void aes() {
        AES aes = new AES(Mode.ECB, Padding.PKCS5Padding,
            "3f4eefd3525675154a5e3a0183d8087b".getBytes());
        String encryptStr = aes.encryptHex("16c5");
        Assertions.assertEquals("c8691d6635862d026d1b2b3d07b529e4", encryptStr);
        Assertions.assertEquals("16c5", aes.decryptStr(encryptStr));
    }

    @Test
    public void rsa() {
        String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAL2QFsN2nlIw6M4pnDau6Y3xFaYwwGqbBnntV1VePQd" +
            "LDLv4zqR2PwhvGVBnxiWb/x3KGthOnzRGHjYXTXyOt1ce1vLPBCILt7MG4uusPQ/vBjRLNetXTz5vXXBlVVoCaPspHV/klzWo1RDLNIZ6nF" +
            "c8UFlXAhJvmadWIa1gVnvxAgMBAAECgYAMUgagup9soSAochzkkva8EnzBOe7vntbLaukfs8nMpQyVVQT9PHA5WJsxFiWTQTHSGlYnU+jWC" +
            "W6iPl57Z30z8DW2VvBqx/ct9zJ1wHCkiAssdBh0Tn0ahFF+n6H5Ux1Uu627VxLXEr5N5hLHUqiyGwfUGKiKXIV1OGQxESi+HwJBAOTQnZ6j" +
            "IT/5B63/x29VSa35k7pl1IfCWiLnPg0KiOS3esQmx7Rg7e9pdf896wI5EAB+CrL6dLZ7nFvUm8NT1X8CQQDUFaAgMkLp+lqMhLndgF9Znkg" +
            "ze0rkQrJjuFdcAz07dsrEy9yQmmZH1j7KHpxYcWqHhcqCdISRMO4buPPXnsaPAkBFj/abR9TP8hGXoG8tNayUq49x14tpSnjEq7B9nnpHrl" +
            "AwWenUuLMdWd55noVXw6lNvQPbrbDV2XaLpHNGSrJZAkAMIQB7VuSExRCQoLaQeFr7oRW3/AEYdMgG8+/rDzRdRjnqb0455tqamSIWJRID7" +
            "NiOz5P5Sfia5RV4+GbZmfb5AkAsF0FdmNxREszWKURfYxuqPgNs4eGx/p02dUKsEVzM7GE11x+R7mnDzJLltOYxK6ESUgwIScmU/TPxnU1/QL4H";

        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC9kBbDdp5SMOjOKZw2rumN8RWmMMBqmwZ57VdVXj0HSwy7+M6kdj8I" +
            "bxlQZ8Ylm/8dyhrYTp80Rh42F018jrdXHtbyzwQiC7ezBuLrrD0P7wY0SzXrV08+b11wZVVaAmj7KR1f5Jc1qNUQyzSGepxXPFBZVwISb5m" +
            "nViGtYFZ78QIDAQAB";
        final RSA rsa = new RSA(privateKey, publicKey);
        Assertions.assertEquals(privateKey, rsa.getPrivateKeyBase64());
        Assertions.assertEquals(publicKey, rsa.getPublicKeyBase64());

        String text = "我是一段测试aaaa";
        // 公钥加密，私钥解密
        final byte[] encrypt = rsa.encrypt(StrUtil.bytes(text, CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);
        final byte[] decrypt = rsa.decrypt(encrypt, KeyType.PrivateKey);
        Assertions.assertEquals(text, StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8));
        // 私钥加密，公钥解密
        final byte[] encrypt2 = rsa.encrypt(StrUtil.bytes(text, CharsetUtil.CHARSET_UTF_8), KeyType.PrivateKey);
        final byte[] decrypt2 = rsa.decrypt(encrypt2, KeyType.PublicKey);
        Assertions.assertEquals(text, StrUtil.str(decrypt2, CharsetUtil.CHARSET_UTF_8));
    }

}
