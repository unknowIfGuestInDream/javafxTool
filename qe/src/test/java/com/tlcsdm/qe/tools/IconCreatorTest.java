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
import com.github.gino0631.icns.IcnsBuilder;
import com.github.gino0631.icns.IcnsIcons;
import com.github.gino0631.icns.IcnsType;
import net.ifok.image.image4j.codec.ico.ICOEncoder;
import net.ifok.image.image4j.util.ConvertUtil;
import net.ifok.image.image4j.util.ImageUtil;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.Imaging;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.Test;

public class IconCreatorTest {

    @Test
    void testICO() {
        File pngFile = new File(ResourceUtil.getResource("iconCreator/logo.png").getPath());

        try {
            // 加载PNG文件
            BufferedImage pngImage = ImageIO.read(pngFile);

            BufferedImage bmpImage = new BufferedImage(
                pngImage.getWidth(),
                pngImage.getHeight(),
                BufferedImage.TYPE_INT_RGB
            );

            // 3. 绘制到新图像，用指定颜色填充透明区域
            Graphics2D graphics = bmpImage.createGraphics();
            graphics.setColor(Color.WHITE);  // 设置背景色（此处为白色）
            graphics.fillRect(0, 0, bmpImage.getWidth(), bmpImage.getHeight());
            graphics.drawImage(pngImage, 0, 0, null);
            graphics.dispose();

            // 2. 创建输出文件
            byte[] bs = Imaging.writeImageToBytes(bmpImage, ImageFormats.BMP);
            BufferedImage bsImage = convert(bs);
            // 目标尺寸列表 (包括 16x16, 32x32, 48x48, 256x256)
            int[][] sizes = {
                {16, 16}, {32, 32}, {48, 48}, {256, 256}
            };

            // 存储生成的图像
            List<BufferedImage> icoImages = new ArrayList<>();

            // 为每个尺寸创建8bit和32bit版本
            for (int[] size : sizes) {
                BufferedImage pi = ImageUtil.scaleImage(bsImage, size[0], size[1]);
                if (size[0] != 256) {
                    // 8bit 深度
                    BufferedImage image8bit = ConvertUtil.convert8(pi);
                    icoImages.add(image8bit);
                }

                // 32bit 深度
                if (pi.getType() != BufferedImage.TYPE_INT_ARGB) {
                    BufferedImage image32bit = ConvertUtil.convert32(pi);
                    icoImages.add(image32bit);
                } else {
                    icoImages.add(pi);
                }
            }

            // 将图像保存为 ICO 文件
            File outputIcoFile = new File(pngFile.getParentFile(), "output.ico");
            boolean[] compress = new boolean[]{true, true, true, true, true, true, true};
            ICOEncoder.write(icoImages, null, compress, outputIcoFile);
            System.out.println("ICO文件已成功保存到 " + outputIcoFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage convert(byte[] imageBytes) throws IOException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes)) {
            return Imaging.getBufferedImage(bis);
        }
    }

    @Test
    void testICNS() throws IOException {
        File pngFile = new File(ResourceUtil.getResource("iconCreator/logo512.png").getPath());
        File outputIcoFile = new File(pngFile.getParentFile(), "output.icns");
        BufferedImage pngImage = ImageIO.read(pngFile);
        try (IcnsBuilder builder = IcnsBuilder.getInstance()) {

            //            builder.add(IcnsType.ICNS_16x16_24BIT_IMAGE, Files.newInputStream(getResource("/is32")));
            //            builder.add(IcnsType.ICNS_16x16_8BIT_MASK, Files.newInputStream(getResource("/s8mk")));
            //            builder.add(IcnsType.ICNS_32x32_24BIT_IMAGE, Files.newInputStream(getResource("/il32")));
            //            builder.add(IcnsType.ICNS_32x32_8BIT_MASK, Files.newInputStream(getResource("/l8mk")));
            //            builder.add(IcnsType.ICNS_128x128_JPEG_PNG_IMAGE, Files.newInputStream(getResource("/ic07_128x128.png")));
            //            builder.add(IcnsType.ICNS_256x256_JPEG_PNG_IMAGE, Files.newInputStream(getResource("/ic08_256x256.png")));
            //            builder.add(IcnsType.ICNS_512x512_JPEG_PNG_IMAGE, Files.newInputStream(getResource("/ic09_512x512.png")));
            //            builder.add(IcnsType.ICNS_16x16_2X_JPEG_PNG_IMAGE, Files.newInputStream(getResource("/ic11_16x16@2x.png")));
            //            builder.add(IcnsType.ICNS_32x32_2X_JPEG_PNG_IMAGE, Files.newInputStream(getResource("/ic12_32x32@2x.png")));
            //            builder.add(IcnsType.ICNS_128x128_2X_JPEG_PNG_IMAGE, Files.newInputStream(getResource("/ic13_128x128@2x.png")));
            //            builder.add(IcnsType.ICNS_256x256_2X_JPEG_PNG_IMAGE, Files.newInputStream(getResource("/ic14_256x256@2x.png")));
            //            builder.add(IcnsType.ICNS_1024x1024_2X_JPEG_PNG_IMAGE, Files.newInputStream(getResource("/ic10_1024x1024.png")));
            builder.add(IcnsType.ICNS_16x16_JPEG_PNG_IMAGE, getImageAsStream(pngImage, 16, 16));
            builder.add(IcnsType.ICNS_32x32_JPEG_PNG_IMAGE, getImageAsStream(pngImage, 32, 32));
            // builder.add(IcnsType.ICNS_64x64_JPEG_PNG_IMAGE, getImageAsStream(pngImage, 64, 64));
            builder.add(IcnsType.ICNS_128x128_JPEG_PNG_IMAGE, getImageAsStream(pngImage, 128, 128));
            builder.add(IcnsType.ICNS_256x256_JPEG_PNG_IMAGE, getImageAsStream(pngImage, 256, 256));
            builder.add(IcnsType.ICNS_512x512_JPEG_PNG_IMAGE, getImageAsStream(pngImage, 512, 512));

            try (IcnsIcons builtIcons = builder.build()) {
                FileOutputStream os = new FileOutputStream(outputIcoFile);
                builtIcons.writeTo(os);
            }
        }
    }

    private ByteArrayInputStream getImageAsStream(BufferedImage source, int targetWidth, int targetHeigth) throws
        IOException {
        BufferedImage bufImage = ImageUtil.scaleImage(source, targetWidth, targetHeigth);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(bufImage, "png", output);
        return new ByteArrayInputStream(output.toByteArray(), 0, output.size());
    }

    @Test
    void testXPM() throws IOException {
        File pngFile = new File(ResourceUtil.getResource("iconCreator/logo512.png").getPath());
        File outputIcoFile = new File(pngFile.getParentFile(), "output.xpm");
        BufferedImage pngImage = ImageIO.read(pngFile);

        Imaging.writeImage(pngImage, outputIcoFile, ImageFormats.XPM);
    }

}
