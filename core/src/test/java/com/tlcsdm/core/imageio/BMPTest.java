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

package com.tlcsdm.core.imageio;

import cn.hutool.core.io.resource.ResourceUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author unknowIfGuestInDream
 */
public class BMPTest {

    @Test
    void png() throws IOException {
        InputStream is = ResourceUtil.getResource("imageio/dbeaver1.png").openStream();
        BufferedImage bufferedImage = ImageIO.read(is);
        // 要輸出的寬度
        int exportWidth = bufferedImage.getWidth() / 2;
        // 要輸出的高度
        int exportHeight = bufferedImage.getHeight() / 2;
        // 讀入一個空白的 BufferedImage 物件，只定義 寬度、高度、輸出類型
        BufferedImage emptyImage = new BufferedImage(exportWidth, exportHeight, bufferedImage.getType());
        // 用剛才建立的空白 BufferedImage 物件來建立畫布
        Graphics2D g2d = emptyImage.createGraphics();
        g2d.drawImage(
            bufferedImage, // 把我們讀入的圖片畫上去
            0, // x軸起始點
            0, // y軸起始點
            exportWidth, // 要畫上去的寬度
            exportHeight, // 要畫上去的長度
            null
                     );
        g2d.dispose();
        String parent = ResourceUtil.getResource("imageio").getFile();
        File outFile = new File(parent, "start.png");
        if (!outFile.exists()) {
            outFile.createNewFile();
        }
        FileOutputStream os = new FileOutputStream(outFile);
        ImageIO.write(emptyImage, "png", os);
    }

    @ParameterizedTest
    @ValueSource(ints = {16, 32, 48, 256})
    void bmp1(int size) throws IOException {
        InputStream is = ResourceUtil.getResource("imageio/dbeaver1.png").openStream();
        BufferedImage bufferedImage = ImageIO.read(is);
        BufferedImage emptyImage = new BufferedImage(size, size, bufferedImage.getType());
        Graphics2D g2d = emptyImage.createGraphics();
        g2d.drawImage(
            bufferedImage,
            0,
            0,
            size,
            size,
            null
                     );
        g2d.dispose();
        String parent = ResourceUtil.getResource("imageio").getFile();
        File outFile = new File(parent, "bmp-" + size + "_32.bmp");
        if (!outFile.exists()) {
            outFile.createNewFile();
        }
        FileOutputStream os = new FileOutputStream(outFile);
        ImageIO.write(emptyImage, "bmp", os);
    }

    @Test
    void png2bmp() throws IOException {
        InputStream is = ResourceUtil.getResource("imageio/dbeaver1.png").openStream();
        BufferedImage bufferedImage = ImageIO.read(is);
        String parent = ResourceUtil.getResource("imageio").getFile();
        File outFile = new File(parent, "png.bmp");
        if (!outFile.exists()) {
            outFile.createNewFile();
        }
        FileOutputStream os = new FileOutputStream(outFile);
        ImageIO.write(bufferedImage, "bmp", os);
    }

    @ParameterizedTest
    @ValueSource(ints = {16, 32, 48})
    void bmp2_16depth(int size) throws IOException {
        InputStream is = ResourceUtil.getResource("imageio/dbeaver1.png").openStream();
        BufferedImage bufferedImage = ImageIO.read(is);
        BufferedImage emptyImage = new BufferedImage(size, size, BufferedImage.TYPE_USHORT_565_RGB);
        Graphics2D g2d = emptyImage.createGraphics();
        g2d.drawImage(
            bufferedImage,
            0,
            0,
            size,
            size,
            null
                     );
        g2d.dispose();
        String parent = ResourceUtil.getResource("imageio").getFile();
        File outFile = new File(parent, "bmp-" + size + "_16.bmp");
        if (!outFile.exists()) {
            outFile.createNewFile();
        }
        FileOutputStream os = new FileOutputStream(outFile);
        ImageIO.write(emptyImage, "bmp", os);
    }

    @Test
    void png2ico() throws IOException {
        InputStream is = ResourceUtil.getResource("imageio/dbeaver1.png").openStream();
        BufferedImage bufferedImage = ImageIO.read(is);
        String parent = ResourceUtil.getResource("imageio").getFile();
        File outFile = new File(parent, "img.ico");
        if (!outFile.exists()) {
            outFile.createNewFile();
        }
        FileOutputStream os = new FileOutputStream(outFile);
        ImageIO.write(bufferedImage, "ico", os);
    }
}
