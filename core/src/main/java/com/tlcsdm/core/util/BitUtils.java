/*
 * Copyright (c) 2024 unknowIfGuestInDream.
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

import java.nio.ByteBuffer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 字节操作工具类.
 *
 * @author unknowIfGuestInDream
 */
public class BitUtils {

    private BitUtils() {
    }

    /**
     * 二进制转换字节数组
     *
     * @param binaryString 010010000110010101101100011011000110111100100000010101110110111101110010011011000110010000100001
     */
    public static byte[] binaryStringToByteArray(String binaryString) {
        if (binaryString == null || binaryString.length() % 8 != 0) {
            throw new IllegalArgumentException("Invalid binary string length, must be a multiple of 8.");
        }
        int numberOfBytes = binaryString.length() / 8;
        byte[] bytes = new byte[numberOfBytes];
        for (int i = 0; i < numberOfBytes; ++i) {
            // 获取当前字节的二进制字符串
            String byteString = binaryString.substring(i * 8, (i + 1) * 8);
            // 解析并存储
            bytes[i] = (byte) Integer.parseInt(byteString, 2);
        }
        return bytes;
    }

    /**
     * 数字转换 2进制字符串，不足指定长度，补充0
     */
    public static String binaryNum2Str(int binaryNum, int len) {
        //		return String.format("%32s", Integer.toBinaryString(binaryNum)).replace(' ', '0');
        return String.format(("%" + len + "s"), Integer.toBinaryString(binaryNum)).replace(' ', '0');
    }

    /**
     * 数字转换 2进制字符串，不足指定长度，补充0
     */
    public static String binaryNum2Str2(int binaryNum, int len) {
        String binaryStr = Integer.toBinaryString(binaryNum);
        int leftPadLen = len - (binaryStr.length());
        if (leftPadLen <= 0) {
            return binaryStr;
        }
        String collect = Stream.generate(() -> "0").limit(leftPadLen).collect(Collectors.joining());
        return collect + binaryStr;
    }

    /**
     * int转换字节数组
     */
    public static byte[] intToByteArray(int value) {
        return ByteBuffer.allocate(4).putInt(value).array();
    }
}
