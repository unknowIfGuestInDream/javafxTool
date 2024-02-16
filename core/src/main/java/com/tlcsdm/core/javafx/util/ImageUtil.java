/*
 * Copyright (c) 2023 unknowIfGuestInDream.
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

package com.tlcsdm.core.javafx.util;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.log.StaticLog;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.Imaging;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 图片工具.
 *
 * @author unknowIfGuestInDream
 */
public class ImageUtil {

    private ImageUtil() {
    }

    public static BufferedImage getBufferedImage(String path) {
        return getBufferedImage(new File(path));
    }

    public static BufferedImage getBufferedImage(File file) {
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = Imaging.getBufferedImage(file);
        } catch (Exception var5) {
            try {
                bufferedImage = ImageIO.read(file);
            } catch (IOException e) {
                StaticLog.error(e);
            }
        }
        return bufferedImage;
    }

    public static Image getFXImage(String path) {
        return getFXImage(new File(path));
    }

    public static Image getFXImage(File file) {
        Image image;
        try {
            image = SwingFXUtils.toFXImage(Imaging.getBufferedImage(file), null);
        } catch (Exception var3) {
            image = new Image("file:" + file.getAbsolutePath());
        }
        return image;
    }

    public static Image getFXImage(byte[] bytes) {
        Image image;
        try {
            image = SwingFXUtils.toFXImage(Imaging.getBufferedImage(bytes), null);
        } catch (Exception var3) {
            image = new Image(new ByteArrayInputStream(bytes));
        }
        return image;
    }

    /**
     * javafx中大图片透明度设置(多线程).
     *
     * @param image   需要 改变透明度的图片对象
     * @param opacity 透明度，介于0-1之间
     * @return 新的可写的image对象
     */
    public static WritableImage imgBigOpacity(Image image, double opacity) {
        if (opacity < 0 || opacity > 1) {
            throw new IllegalArgumentException("Opacity needs to be between 0-1, please reset the opacity!");
        }
        // 获取PixelReader
        PixelReader pixelReader = image.getPixelReader();
        // 创建WritableImage
        WritableImage wImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
        PixelWriter pixelWriter = wImage.getPixelWriter();
        double imgHeight = image.getHeight();
        double tempHeight = imgHeight % 3;
        // 将原来的单个线程改变透明度(下面注释的代码)改为了三个线程，解决了大图片更改透明度缓慢的问题
        FutureTask<Boolean> futureTask1 = new FutureTask<>(() -> {
            for (int readY = 0; readY < tempHeight; readY++) {
                for (int readX = 0; readX < image.getWidth(); readX++) {
                    Color color = pixelReader.getColor(readX, readY);
                    //如果是透明的，就不改变
                    if (color.getOpacity() == 0) {
                        continue;
                    }
                    // 最后一个参数是透明设置。需要设置透明不能改变原来的，只能重新创建对象赋值，
                    Color c1 = new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity);
                    // c1=c1.darker();
                    pixelWriter.setColor(readX, readY, c1);
                }
            }
            return true;
        });
        new Thread(futureTask1, "The first opacity rendering thread").start();

        FutureTask<Boolean> futureTask2 = new FutureTask<>(() -> {
            for (int readY = (int) tempHeight; readY < 2 * tempHeight; readY++) {
                for (int readX = 0; readX < image.getWidth(); readX++) {
                    Color color = pixelReader.getColor(readX, readY);
                    //如果是透明的，就不改变
                    if (color.getOpacity() == 0) {
                        continue;
                    }
                    Color c1 = new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity);
                    // c1=c1.darker();
                    pixelWriter.setColor(readX, readY, c1);
                }
            }

            return true;
        });
        new Thread(futureTask2, "The second opacity rendering thread").start();
        FutureTask<Boolean> futureTask3 = new FutureTask<>(() -> {
            for (int readY = (int) (2 * tempHeight); readY < imgHeight; readY++) {
                for (int readX = 0; readX < image.getWidth(); readX++) {
                    Color color = pixelReader.getColor(readX, readY);
                    //如果是rgb为0且透明的，就不改变
                    if (color.getOpacity() == 0) {
                        continue;
                    }
                    Color c1 = new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity);
                    // c1=c1.darker();
                    pixelWriter.setColor(readX, readY, c1);
                }
            }

            return true;
        });
        new Thread(futureTask3, "The third opacity rendering thread").start();

        // 这部分代码可以自主选用。用了可以保证全部图片全部刷新完再展示，不然图片是先渲染上部分，再是中下部分
        try {
            // 等待三个线程全部执行完毕
            if (futureTask1.get() && futureTask2.get() && futureTask3.get()) {
                return wImage;
            }
        } catch (ExecutionException e) {
            StaticLog.error(e);
        } catch (InterruptedException e) {
            StaticLog.error(e);
            Thread.currentThread().interrupt();
        }
        return wImage;
    }

    /**
     * javafx中图片透明度设置(单线程).
     *
     * @param image   需要 改变透明度的图片对象
     * @param opacity 透明度，介于0-1之间
     * @return 新的可写的image对象
     * @author LIu Mingyao
     */
    public static WritableImage imgOpacity(Image image, double opacity) {
        if (opacity < 0 || opacity > 1) {
            throw new IllegalArgumentException("Opacity needs to be between 0-1, please reset the opacity!");
        }
        // 获取PixelReader
        PixelReader pixelReader = image.getPixelReader();
        // 创建WritableImage
        WritableImage wImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
        PixelWriter pixelWriter = wImage.getPixelWriter();
        double imgHeight = image.getHeight();
        // 单线程更改透明度，得到每个坐标像素点的color，并重新设值，赋予透明度，最后将新color设给新的image对象(wImage的pixelWriter)
        for (int readY = 0; readY < image.getHeight(); readY++) {
            for (int readX = 0; readX < image.getWidth(); readX++) {
                Color color = pixelReader.getColor(readX, readY);
                // 现在写入一个更为明亮的颜色到PixelWriter中
                // color = color.brighter();
                // 更暗
                color = color.darker();
                // 最后一个参数是透明设置。需要设置透明不能改变原来的，只能重新创建对象赋值，
                Color c1 = new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity);
                pixelWriter.setColor(readX, readY, c1.brighter());
            }
        }
        return wImage;
    }

    public static void writeImage(Image image, File file) {
        writeImage(SwingFXUtils.fromFXImage(image, null), file);
    }

    public static void writeImage(BufferedImage bufferedImage, File file) {
        try {
            String suf = FileNameUtil.getSuffix(file).toUpperCase();
            if ("JPG".equals(suf)) {
                suf = "JPEG";
            }
            Imaging.writeImage(bufferedImage, file, ImageFormats.valueOf(suf));
        } catch (Exception e) {
            StaticLog.error(e);
        }

    }

}
