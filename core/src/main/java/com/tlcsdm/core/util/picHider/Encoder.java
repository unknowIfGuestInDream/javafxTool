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

package com.tlcsdm.core.util.picHider;

import cn.hutool.log.StaticLog;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * 嵌入（将一个文件或信息隐藏到一张图片）.
 *
 * @author unknowIfGuestInDream
 */
public class Encoder {
    /**
     * 掩码（用来指定图像中哪些位可以被修改来隐藏信息，由ARGB四个分量组成的，每个分量是8位的二进制数）
     */
    private static final String MASK_BINARY_STRING = "00001111" // Alpha Channel Mask
        + "00001111" // Red Channel Mask
        + "00001111" // Green Channel Mask
        + "00001111" // Blue Channel Mask
        ;

    long mask = 0;
    int dataByteCursor = 0;
    int dataBitCursor = 1 << 7;
    // 要存放的数据
    byte[] dataBytes = null;

    /**
     * @param dataFile   数据文件（需要隐藏的文件）
     * @param coverImage 载体图片（原始图片）
     * @param stegoImage 输出图片（包含隐藏文件的图片）
     */
    public void encode(String dataFile, String coverImage, String stegoImage) throws IOException {
        File imgFile = new File(coverImage);
        if (!imgFile.exists()) {
            StaticLog.warn("文件不存在 {}", coverImage);
            return;
        }
        final String maskBinaryStr = MASK_BINARY_STRING;
        mask = Long.parseLong(maskBinaryStr, 2);
        BufferedImage image = ImageIO.read(imgFile);
        int width = image.getWidth();
        int height = image.getHeight();
        // 需要隐写的文件
        File file = new File(dataFile);
        if (!file.exists()) {
            StaticLog.warn("文件不存在 {}", dataFile);
            return;
        }
        // 获取文件名
        String fileName = file.getName();
        long fileSize = file.length();

        // 描述信息文件长度,文件名
        String profile = String.format("%s,%s", fileSize, fileName);
        byte[] encryptBytes = SecureEncoder.encrypt(profile);
        StaticLog.debug("描述信息长度={}", encryptBytes.length);
        StaticLog.debug("描述信息={}", profile);

        // 组成：1个字节魔数“l”, 4个字节描述信息长度，n个字节描述信息
        int headerByteSize = (1 + 4 + encryptBytes.length);

        // 计算图片可以隐藏的文件大小
        int canHiddenLen = image.getHeight() * image.getWidth();
        //// 掩码中可以分配出多少个bit用于存储数据
        int bitCount = (int) maskBinaryStr.chars().filter(ch -> ch == '1').count();
        canHiddenLen = canHiddenLen * bitCount;
        canHiddenLen = (canHiddenLen + 7) / 8 - headerByteSize;
        // 转换成兆字节
        double megaBytes = canHiddenLen / (1024.0 * 1024.0);
        StaticLog.debug("头长度={}, 掩码数据位数量={}, 数据文件大小={}, 可以隐藏文件大小={}字节  {}",
            headerByteSize, bitCount, fileSize, canHiddenLen, megaBytes);
        if (canHiddenLen < fileSize) {
            StaticLog.error("无法进行隐藏");
            return;
        }

        // 计算新数组的大小=头长度+n个字节文件长度
        dataBytes = new byte[headerByteSize + (int) fileSize];

        // 【魔数】。字符“l”
        dataBytes[0] = (byte) 'l';
        // 【描述信息-长度】
        int encryptBytesLength = encryptBytes.length;
        dataBytes[1] = (byte) (encryptBytesLength >>> 24);
        dataBytes[2] = (byte) (encryptBytesLength >>> 16);
        dataBytes[3] = (byte) (encryptBytesLength >>> 8);
        dataBytes[4] = (byte) encryptBytesLength;
        // 描述信息
        System.arraycopy(encryptBytes, 0, dataBytes, 5, encryptBytes.length);
        // 【文件内容】
        int offset = headerByteSize;
        try (FileInputStream fis = new FileInputStream(file)) {
            int bytesRead;
            byte[] buffer = new byte[8192];
            while ((bytesRead = fis.read(buffer)) != -1) {
                if (offset + bytesRead > dataBytes.length) {
                    throw new IOException("Buffer overflow: dataBytes is not large enough to hold the file content.");
                }
                System.arraycopy(buffer, 0, dataBytes, offset, bytesRead);
                offset += bytesRead;
            }
        }

        dataByteCursor = 0;
        dataBitCursor = 1 << 7;

        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                int rgb = image.getRGB(w, h);
                rgb = amendBits(rgb);
                image.setRGB(w, h, rgb);
            }
        }

        File output = new File(stegoImage);
        ImageIO.write(image, "png", output);
        StaticLog.debug("写入完成！已成功写入到文件: " + output.getAbsolutePath());
    }

    private int amendBits(int rgb) {
        int bitMask = 1 << 31;
        for (int i = 0; i < 32; i++) {
            if ((mask & bitMask) != 0) {
                Boolean bit = get1Bit();
                if (bit != null) {
                    rgb = setBit(rgb, bitMask, bit);
                }
            }
            bitMask >>>= 1;
        }
        return rgb;
    }

    public Boolean get1Bit() {
        if (dataByteCursor >= dataBytes.length) {
            return null;
        }
        int num = dataBytes[dataByteCursor];
        int dataBitMask = dataBitCursor;
        boolean v = (num & dataBitMask) != 0;
        dataBitCursor >>>= 1;
        if (dataBitCursor == 0) {
            dataByteCursor++;
            dataBitCursor = 1 << 7;
        }
        return v;
    }

    public int setBit(int num, int mask, boolean newBit) {
        // 清除原来的第pos位
        num &= ~mask;
        if (newBit) {
            num |= mask;
        }
        return num;
    }
}
