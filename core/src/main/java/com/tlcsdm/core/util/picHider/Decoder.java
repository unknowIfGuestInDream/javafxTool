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
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import javax.imageio.ImageIO;

/**
 * 提取（从隐写图像中恢复出原来被隐藏的文件或信息）.
 *
 * @author unknowIfGuestInDream
 */
public class Decoder {
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
     * 解析出的描述信息长度
     */
    int profileLength = 0;
    /**
     * 数据长度
     */
    int dataByteLength = 0;
    /**
     * 文件名
     */
    String fileName = null;

    /**
     * @param stegoImage 输入图片（包含隐藏文件的图片）
     * @param output     输出目录
     */
    public void decode(String stegoImage, String output) throws IOException {
        File dataImg = new File(stegoImage);
        if (!dataImg.exists()) {
            StaticLog.warn("文件不存在 {}", stegoImage);
            return;
        }
        mask = Long.parseLong(MASK_BINARY_STRING, 2);
        BufferedImage image = ImageIO.read(dataImg);
        int width = image.getWidth();
        int height = image.getHeight();
        StaticLog.debug("图片尺寸: width={}, height={}", width, height);

        // 1个魔数，4位描述信息长度
        dataBytes = new byte[5];

        dataByteCursor = 0;
        dataBitCursor = 1 << 7;

        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                int rgb = image.getRGB(w, h);
                extractBits(rgb);
            }
        }

        int dataStartByte = 1 + 4 + profileLength;

        if (fileName != null && !fileName.isEmpty()) {
            output = (output == null || output.trim().isEmpty()) ? null : output.trim();
            File file = new File(output, "output_" + fileName);
            try (FileOutputStream fos = new FileOutputStream(file, false)) {
                // 将字节数组写入文件，追加模式
                fos.write(dataBytes, dataStartByte, dataByteLength);
                StaticLog.debug("解析出数据已成功写入到文件: " + file.getAbsolutePath());
            } catch (IOException e) {
                StaticLog.error(e);
            }
        }
    }

    private void extractBits(int rgb) {
        int bitMask = 1 << 31;
        for (int i = 0; i < 32; i++) {
            if ((mask & bitMask) != 0) {
                addBit((rgb & bitMask) == bitMask);
            }
            bitMask >>>= 1;
        }
    }

    private void addBit(boolean num) {
        if (dataByteCursor >= dataBytes.length) {
            return;
        }
        if (num) {
            dataBytes[dataByteCursor] += (byte) dataBitCursor;
        }
        dataBitCursor >>= 1;
        // (dataBitCursor == 0) 表示下标 dataByteCursor 的字节已经读取完
        if (dataBitCursor == 0) {
            dataBitCursor = 1 << 7;
            dataByteCursor++;
            // 解析出描述信息长度
            if (dataByteCursor == 5) {
                profileLength = ByteBuffer.wrap(dataBytes, 1, 4).getInt();
                StaticLog.debug("解析出描述信息长度={}", profileLength);
                this.growArray(profileLength);
            }
            // 解析出描述信息
            if (profileLength > 0) {
                if (dataByteCursor == profileLength + 5) {
                    byte[] bytes = Arrays.copyOfRange(dataBytes, 5, profileLength + 5);
                    String decrypt = SecureEncoder.decrypt(bytes);
                    StaticLog.debug("解析出描述信息={}", decrypt);
                    dataByteLength = Integer.parseInt(decrypt.split(",")[0]);
                    fileName = decrypt.split(",")[1];
                    this.growArray(dataByteLength);
                }
            }
        }
    }

    /**
     * 扩充 dataBytes 数组长度
     *
     * @param growLength 扩充长度
     */
    private void growArray(int growLength) {
        byte[] newArray = new byte[dataBytes.length + growLength];
        System.arraycopy(dataBytes, 0, newArray, 0, dataBytes.length);
        dataBytes = newArray;
    }
}
