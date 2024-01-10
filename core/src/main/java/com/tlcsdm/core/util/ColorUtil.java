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

import javafx.scene.paint.Color;

/**
 * Conversion tool for RGB, color temperature, coordinates, Color and hex.
 *
 * @author unknowIfGuestInDream
 */
public class ColorUtil {

    private ColorUtil() {
    }

    /**
     * Convert RGB to hexadecimal.
     *
     * @param red   R
     * @param green G
     * @param blue  B
     * @return hex
     */
    public static String rgb2Hex(int red, int green, int blue) {
        return String.format("#%02X%02X%02X", red, green, blue);
    }

    /**
     * RGB to Integer.
     *
     * @param red   R
     * @param green G
     * @param blue  B
     * @return int
     */
    public static int rgbToInt(int red, int green, int blue) {
        int r = (red << 16) & 0x00ff0000;
        int g = (green << 8) & 0x0000FF00;
        int b = blue & 0x000000FF;
        return 0xFF000000 | r | g | b;
    }

    /**
     * Convert hexadecimal color string to RGB.
     *
     * @param hexStr hexadecimal
     * @return r, g, b
     */
    public static int[] hex2RGB(String hexStr) {
        if (hexStr != null && hexStr.length() >= 7) {
            int[] rgb = new int[3];
            rgb[0] = Integer.valueOf(hexStr.substring(1, 3), 16);
            rgb[1] = Integer.valueOf(hexStr.substring(3, 5), 16);
            rgb[2] = Integer.valueOf(hexStr.substring(5, 7), 16);
            return rgb;
        }
        return new int[]{};
    }

    /**
     * Convert Color to hex.
     *
     * @param color {@link javafx.scene.paint.Color}
     * @return hex (Length is 7 or 9) e.g. #FFFFFF | #FFFFFFFF
     */
    public static String color2Hex(Color color) {
        int red = (int) Math.round(color.getRed() * 255.0D);
        int green = (int) Math.round(color.getGreen() * 255.0D);
        int blue = (int) Math.round(color.getBlue() * 255.0D);
        int alpha = (int) Math.round(color.getOpacity() * 255.0D);
        if (alpha == 255) {
            return String.format("#%02x%02x%02x", red, green, blue);
        } else {
            return String.format("#%02x%02x%02x%02x", red, green, blue, alpha);
        }
    }

    private static final double[] MATRIX_SRGB2XYZ_D50 = {0.436052025, 0.385081593, 0.143087414, 0.222491598, 0.716886060, 0.060621486, 0.013929122, 0.097097002, 0.714185470};

    /**
     * Convert sRGB to tristimulus value.
     *
     * @param red   R
     * @param green G
     * @param blue  B
     * @return xyz (*100 %)
     */
    public static double[] rgb2Xyz(int red, int green, int blue) {
        double[] xyz = new double[3];
        double[] rgb = new double[]{red / 255.0, green / 255.0, blue / 255.0};
        // RGB转sRGB的伽马变换公式
        for (int i = 0; i < 3; i++) {
            if (rgb[i] > 0.04045) {
                rgb[i] = Math.pow((rgb[i] + 0.055) / 1.055, 2.4);
            } else {
                rgb[i] = rgb[i] / 12.92;
            }
        }
        xyz[0] = rgb[0] * 0.4124 + rgb[1] * 0.3576 + rgb[2] * 0.1805;
        xyz[1] = rgb[0] * 0.2126 + rgb[1] * 0.7152 + rgb[2] * 0.0722;
        xyz[2] = rgb[0] * 0.0193 + rgb[1] * 0.1192 + rgb[2] * 0.9505;
        return xyz;
    }

    /**
     * Convert RGB to coordinates.
     *
     * @param red   R
     * @param green G
     * @param blue  B
     * @return coordinates
     */
    public static double[] rgb2Coor(int red, int green, int blue) {
        double[] coor = new double[3];
        double[] rgb = new double[]{red / 255.0, green / 255.0, blue / 255.0};
        double t = rgb[0] * 0.667 + rgb[1] * 1.1323 + rgb[2] * 1.2007;
        coor[0] = (rgb[0] * 0.49 + rgb[1] * 0.31 + rgb[2] * 0.2) / t;
        coor[1] = (rgb[0] * 0.177 + rgb[1] * 0.8124 + rgb[2] * 0.0106) / t;
        coor[2] = (rgb[1] * 0.0099 + rgb[2] * 0.9901) / t;
        return coor;
    }

    /**
     * Tristimulus values converted to coordinates.
     *
     * @param trimX X
     * @param trimY Y
     * @param trimZ Z
     * @return coorX, coorY
     */
    public static double[] xyz2Coor(double trimX, double trimY, double trimZ) {
        double coorX = trimX / (trimX + trimY + trimZ);
        double coorY = trimY / (trimX + trimY + trimZ);
        return new double[]{coorX, coorY};
    }

    /**
     * Color coordinates to color temperature unit (K)
     *
     * @param coorX X
     * @param coorY Y
     * @return CCT (K)
     */
    public static double xyz2Temperature(double coorX, double coorY) {
        double n = (coorX - 0.3320) / (0.1858 - coorY);
        return 437 * n * n * n + 3601 * n * n + 6831 * n + 5517;
    }

    /**
     * RGB to color temperature.
     *
     * @param red   R
     * @param green G
     * @param blue  B
     * @return CCT (K)
     */
    public static double rgb2Temperature(int red, int green, int blue) {
        double[] trim = rgb2Xyz(red, green, blue);
        double[] coors = xyz2Coor(trim[0], trim[1], trim[2]);
        return xyz2Temperature(coors[0], coors[1]);
    }

    /**
     * Color temperature to RGB.
     *
     * @param temperature Color temperature unit (K) 1000 ~ 40000
     * @return r, g, b
     */
    public static int[] temperature2Rgb(double temperature) {
        if (temperature < 1000) {
            temperature = 1000;
        }
        if (temperature > 40000) {
            temperature = 40000;
        }
        int r, g, b;
        double temp = temperature / 100.0;
        if (temp <= 66) {
            r = 255;
            g = (int) Math.min(Math.max(99.4708025861 * Math.log(temp) - 161.1195681661, 0), 255);
        } else {
            r = (int) Math.min(Math.max(329.698727446 * Math.pow(temp - 60, -0.1332047592), 0), 255);
            g = (int) Math.min(Math.max(288.1221695283 * Math.pow(temp - 60, -0.0755148492), 0), 255);
        }
        if (temp >= 66) {
            b = 255;
        } else if (temp <= 19) {
            b = 0;
        } else {
            b = (int) Math.min(Math.max(138.5177312231 * Math.log(temp - 10) - 305.0447927307, 0), 255);
        }
        return new int[]{r, g, b};
    }
}