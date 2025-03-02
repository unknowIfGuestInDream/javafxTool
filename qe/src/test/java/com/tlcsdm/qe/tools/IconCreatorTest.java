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
import net.ifok.image.image4j.util.ConvertUtil;
import net.ifok.image.image4j.util.ImageUtil;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
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
                BufferedImage pi = ImageUtil.scaleImage(pngImage, size[0], size[1]);
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
            ICOEncoder.write(icoImages, outputIcoFile);
            System.out.println("ICO文件已成功保存到 " + outputIcoFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
