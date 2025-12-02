/*
 * Copyright (c) 2025 unknowIfGuestInDream.
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

package com.tlcsdm.core.watermark;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;

import javax.imageio.ImageIO;

/**
 * 基于LSB（最低有效位）的暗水印实现.
 * LSB水印的局限性：
 * 1. 对图像处理操作（如压缩、缩放、格式转换）敏感
 * 2. 容易被发现和移除
 * 3. 适合对图像质量要求不高且不需要强鲁棒性的场景
 * <p>
 * 注意事项
 * 目前看只支持png
 *
 * @author unknowIfGuestInDream
 */
public class LSBWatermark {
    /**
     * 添加暗水印到图片
     *
     * @param inputImage    原始图片文件
     * @param outputImage   输出图片文件
     * @param watermarkText 水印文本
     */
    public static void addTextWatermark(File inputImage, File outputImage, String watermarkText) throws IOException {
        BufferedImage image = ImageIO.read(inputImage);

        // 将水印文本转换为字节数组
        byte[] watermarkBytes = watermarkText.getBytes(StandardCharsets.UTF_8);

        // 将字节数组转换为位集合
        BitSet watermarkBits = BitSet.valueOf(watermarkBytes);

        // 计算需要多少像素来存储水印(每个像素可以存储3位 - RGB各1位)
        int requiredPixels = (watermarkBytes.length * 8 + 2) / 3;

        // 检查图片是否有足够像素存储水印
        if (requiredPixels > image.getWidth() * image.getHeight()) {
            throw new IllegalArgumentException("图片太小，无法容纳水印数据");
        }

        // 首先写入水印长度(使用前16位)
        int watermarkLength = watermarkBytes.length;
        int pixelIndex = 0;

        // 写入水印长度(16位)
        for (int i = 0; i < 16; i++) {
            boolean bit = ((watermarkLength >> (15 - i)) & 1) == 1;
            setNextWatermarkBit(image, pixelIndex++, bit);
        }

        // 写入水印数据
        for (int i = 0; i < watermarkBits.size(); i++) {
            setNextWatermarkBit(image, pixelIndex++, watermarkBits.get(i));
        }

        // 保存带水印的图片
        String formatName = outputImage.getName().substring(outputImage.getName().lastIndexOf(".") + 1);
        ImageIO.write(image, formatName, outputImage);
    }

    private static void setNextWatermarkBit(BufferedImage image, int bitIndex, boolean bit) {
        int x = bitIndex % image.getWidth();
        int y = bitIndex / image.getWidth();

        int rgb = image.getRGB(x, y);

        // 分解RGB分量
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;

        // 根据位索引决定修改哪个通道的最低有效位
        int channel = bitIndex % 3;
        switch (channel) {
            case 0 -> r = (r & 0xFE) | (bit ? 1 : 0);
            case 1 -> g = (g & 0xFE) | (bit ? 1 : 0);
            case 2 -> b = (b & 0xFE) | (bit ? 1 : 0);
        }

        // 重新组合RGB值
        int newRgb = (r << 16) | (g << 8) | b;
        image.setRGB(x, y, newRgb);
    }

    /**
     * 从图片中提取暗水印
     *
     * @param watermarkedImage 带水印的图片文件
     * @return 提取出的水印文本
     * @throws IOException 如果读取图片失败
     */
    public static String extractTextWatermark(File watermarkedImage) throws IOException {
        BufferedImage image = ImageIO.read(watermarkedImage);

        // 首先读取水印长度(16位)
        int watermarkLength = 0;
        int pixelIndex = 0;

        for (int i = 0; i < 16; i++) {
            boolean bit = getNextWatermarkBit(image, pixelIndex++);
            watermarkLength = (watermarkLength << 1) | (bit ? 1 : 0);
        }

        // 计算需要读取的位数
        int totalBits = watermarkLength * 8;
        BitSet watermarkBits = new BitSet(totalBits);

        // 读取水印数据
        for (int i = 0; i < totalBits; i++) {
            watermarkBits.set(i, getNextWatermarkBit(image, pixelIndex++));
        }

        // 将BitSet转换为字节数组
        byte[] watermarkBytes = watermarkBits.toByteArray();

        // 确保字节数组长度正确
        if (watermarkBytes.length < watermarkLength) {
            byte[] correctedBytes = new byte[watermarkLength];
            System.arraycopy(watermarkBytes, 0, correctedBytes, 0, watermarkBytes.length);
            watermarkBytes = correctedBytes;
        } else if (watermarkBytes.length > watermarkLength) {
            byte[] correctedBytes = new byte[watermarkLength];
            System.arraycopy(watermarkBytes, 0, correctedBytes, 0, watermarkLength);
            watermarkBytes = correctedBytes;
        }

        return new String(watermarkBytes, StandardCharsets.UTF_8);
    }

    private static boolean getNextWatermarkBit(BufferedImage image, int bitIndex) {
        int x = bitIndex % image.getWidth();
        int y = bitIndex / image.getWidth();

        int rgb = image.getRGB(x, y);

        // 根据位索引决定读取哪个通道的最低有效位
        int channel = bitIndex % 3;
        return switch (channel) {
            case 0 -> ((rgb >> 16) & 0x01) == 1;
            case 1 -> ((rgb >> 8) & 0x01) == 1;
            case 2 -> (rgb & 0x01) == 1;
            default -> false;
        };
    }
}
