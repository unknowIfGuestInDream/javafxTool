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

package com.tlcsdm.qe.tools;

import cn.hutool.core.io.resource.ResourceUtil;
import net.ifok.image.image4j.codec.ico.ICOEncoder;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IconCreatorTest {

    @Test
    void test1() {
        File pngFile = new File(ResourceUtil.getResource("iconCreator/logo.png").getPath());

        try {
            // 加载PNG文件
            BufferedImage pngImage = ImageIO.read(pngFile);

            // 目标尺寸列表 (包括 16x16, 32x32, 48x48, 256x256)
            int[][] sizes = {
                {16, 16}, {32, 32}, {48, 48}, {256, 256}
            };

            // 存储生成的图像
            List<BufferedImage> icoImages = new ArrayList<>();

            // 为每个尺寸创建8bit和32bit版本
            for (int[] size : sizes) {
                if (size[0] != 256) {
                    // 16bit 深度
                    BufferedImage image8bit = convertImage(pngImage, size[0], size[1], 8);
                    icoImages.add(image8bit);
                }

                // 32bit 深度
                BufferedImage image32bit = convertImage(pngImage, size[0], size[1], 32);
                icoImages.add(image32bit);
            }

            // 将图像保存为 ICO 文件
            File outputIcoFile = new File(pngFile.getParentFile(), "output.ico");
            ICOEncoder.write(icoImages, outputIcoFile);
            System.out.println("ICO文件已成功保存到 " + outputIcoFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 核心转换方法
    private static BufferedImage convertImage(BufferedImage original, int width, int height, int bitDepth) {
        // 1. 缩放图像到目标尺寸
        BufferedImage scaledImage = scaleImage(original, width, height);

        // 2. 转换颜色深度
        if (bitDepth == 8) {
            return convertTo8Bit(scaledImage);
        } else if (bitDepth == 32) {
            return convertTo32Bit(scaledImage);
        } else {
            throw new IllegalArgumentException("不支持的位深度: " + bitDepth);
        }
    }

    // 高质量缩放
    private static BufferedImage scaleImage(BufferedImage original, int width, int height) {
        BufferedImage scaled = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = scaled.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.drawImage(original.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
        g.dispose();
        return scaled;
    }

    // 转换为8位索引颜色（简单方法，颜色可能失真）
    private static BufferedImage convertTo8Bit(BufferedImage image) {
        BufferedImage indexed = new BufferedImage(
            image.getWidth(),
            image.getHeight(),
            BufferedImage.TYPE_BYTE_INDEXED
        );
        Graphics2D g = indexed.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return indexed;
    }

    // 转换为32位ARGB
    private static BufferedImage convertTo32Bit(BufferedImage image) {
        BufferedImage argb = new BufferedImage(
            image.getWidth(),
            image.getHeight(),
            BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D g = argb.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return argb;
    }

}
