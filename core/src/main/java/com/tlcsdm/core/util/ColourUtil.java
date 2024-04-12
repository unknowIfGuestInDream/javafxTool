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

import java.awt.color.ColorSpace;
import java.util.ArrayList;
import java.util.List;

/**
 * Conversion tool for RGB, color temperature and coordinates.
 *
 * @author unknowIfGuestInDream
 */
public class ColourUtil {

    private ColourUtil() {
    }

    /**
     * Translate liner-RGB to XYZ.
     *
     * @param red   R [0, 255]
     * @param green G [0, 255]
     * @param blue  B [0, 255]
     * @return XYZ [0, 1]
     */
    public static double[] linerRgb2Xyz(int red, int green, int blue) {
        double[] xyz = new double[3];
        double[] rgb = new double[]{red / 255.0, green / 255.0, blue / 255.0};
        xyz[0] = rgb[0] * 0.41239 + rgb[1] * 0.35758 + rgb[2] * 0.18048;
        xyz[1] = rgb[0] * 0.21263 + rgb[1] * 0.71516 + rgb[2] * 0.07219;
        xyz[2] = rgb[0] * 0.01933 + rgb[1] * 0.11919 + rgb[2] * 0.95053;
        return xyz;
    }

    /**
     * Translate liner-RGB to XY-coordinate.
     *
     * @param red   R [0, 255]
     * @param green G [0, 255]
     * @param blue  B [0, 255]
     * @return XY-coordinate [0, 1]
     */
    public static double[] linerRgb2Coor(int red, int green, int blue) {
        double[] xyz = linerRgb2Xyz(red, green, blue);
        double[] xy = new double[2];
        double sum = 0;
        for (int i = 0; i < 3; i++) {
            sum += xyz[i];
        }
        xy[0] = xyz[0] / sum;
        xy[1] = xyz[1] / sum;
        return xy;
    }

    /**
     * Translate liner-RGB to Tc.
     *
     * @param red   R [0, 255]
     * @param green G [0, 255]
     * @param blue  B [0, 255]
     * @return Tc (K) [1000, 40000]
     */
    public static double linerRgb2Tc(int red, int green, int blue) {
        double[] xy = linerRgb2Coor(red, green, blue);
        return coor2Tc(xy[0], xy[1]);
    }

    /**
     * Calculate liner-sRGB from xy-coordinate.
     *
     * @param coorX X [0, 1]
     * @param coorY Y [0, 1]
     * @return liner-RGB [0, 1]
     */
    public static double[] calculateForLinerRgbFromCoor(double coorX, double coorY) {
        int[] xyz = new int[3];
        double[] liner = new double[3];
        double sum = 0;
        xyz[0] = (int) (coorX * 65534);
        xyz[1] = (int) (coorY * 65534);
        xyz[2] = 65536 - xyz[0] - xyz[1];
        liner[0] = ((xyz[0] * 3318) + (xyz[1] * (-1574)) + (xyz[2] * (-510))) >> 16;
        liner[1] = ((xyz[0] * (-992)) + (xyz[1] * 1921) + (xyz[2] * 42)) >> 16;
        liner[2] = ((xyz[0] * 56) + (xyz[1] * (-208)) + (xyz[2] * 1075)) >> 16;
        for (int i = 0; i < liner.length; i++) {
            if (liner[i] <= 0.0) {
                liner[i] = 0.0;
            }
            sum += liner[i];
        }
        liner[0] /= sum;
        liner[1] /= sum;
        liner[2] = (1.0 - (liner[0] + liner[1]));
        return liner;
    }

    /**
     * Translate liner-sRGB to sRGB.
     *
     * @param liner liner-sRGB [0, 1]
     * @return sRGB [0, 1]
     */
    public static float[] linerRgb2sRgb(double[] liner) {
        float[] lRgb = new float[3];
        lRgb[0] = (float) liner[0];
        lRgb[1] = (float) liner[1];
        lRgb[2] = (float) liner[2];
        return ColorSpace.getInstance(ColorSpace.CS_LINEAR_RGB).toRGB(lRgb);
    }

    /**
     * Translate XY to liner-RGB.
     *
     * @param coorX X [0, 1]
     * @param coorY Y [0, 1]
     * @return liner-RGB [0, 255]
     */
    public static int[] coor2LinerRgb(double coorX, double coorY) {
        int[] rgb = new int[3];
        double[] liner = calculateForLinerRgbFromCoor(coorX, coorY);
        for (int i = 0; i < 3; i++) {
            rgb[i] = (int) (liner[i] * 255);
        }
        return rgb;
    }

    /**
     * Translate XY to sRGB.
     *
     * @param coorX X [0, 1]
     * @param coorY Y [0, 1]
     * @return sRGB [0, 1]
     */
    public static float[] coor2sRgb(double coorX, double coorY) {
        double[] liner = calculateForLinerRgbFromCoor(coorX, coorY);
        return linerRgb2sRgb(liner);
    }

    /**
     * Translate XY to Tc.
     *
     * @param coorX X [0, 1]
     * @param coorY Y [0, 1]
     * @return Tc (K) [1000, 40000]
     */
    public static double coor2Tc(double coorX, double coorY) {
        double x = (int) (coorX * 65534) >> 16;
        double y = (int) (coorY * 65534) >> 16;
        double n = (x - 0.3320) / (0.1858 - y);
        return ((535 * (n * n * n)) + (3601 * (n * n)) + (6861 * n) + 5517);
    }

    /**
     * Translate Tc to liner-RGB.
     *
     * @param tc (K) [1000, 40000]
     * @return liner-RGB [0, 255]
     */
    public static int[] tc2LinerRgb(double tc) {
        double[] xy = tc2Coor(tc);
        return coor2LinerRgb(xy[0], xy[1]);
    }

    /**
     * Translate Tc to sRGB.
     *
     * @param tc (K) [1000, 40000]
     * @return sRGB [0, 1]
     */
    public static float[] tc2sRgb(double tc) {
        double[] xy = tc2Coor(tc);
        double[] liner = calculateForLinerRgbFromCoor(xy[0], xy[1]);
        return linerRgb2sRgb(liner);
    }

    /**
     * Translate tc K to Mirek.
     *
     * @param tc (K)
     * @return (Mirek)
     */
    public static double tcK2Mirek(double tc) {
        return 1000000 / tc;
    }

    /**
     * Translate Tc to XY.
     *
     * @param tc (K) [1000, 40000]
     * @return XY-coordinate [0, 1]
     */
    public static double[] tc2Coor(double tc) {
        List<TcXyData> list = getTcXyList();
        double[] xy = new double[2];
        double mirek = tcK2Mirek(tc);
        boolean ret = false;
        for (int i = 0; i < list.size(); i++) {
            TcXyData data = list.get(i);
            if (data.mirek >= mirek) {
                xy[0] = data.x / 65536.0;
                xy[1] = data.y / 65536.0;
                ret = true;
                break;
            }
        }
        if (!ret) {
            xy[0] = list.get(list.size() - 1).x / 65536.0;
            xy[1] = list.get(list.size() - 1).y / 65536.0;
        }
        return xy;
    }

    private static List<TcXyData> tcXyList;

    private static List<TcXyData> getTcXyList() {
        if (tcXyList == null) {
            initializeTcXyData();
        }
        return tcXyList;
    }

    private static void initializeTcXyData() {
        tcXyList = new ArrayList<>(63);
        tcXyList.add(new TcXyData(10, 15855, 15613));
        tcXyList.add(new TcXyData(20, 16046, 15901));
        tcXyList.add(new TcXyData(30, 16256, 16213));
        tcXyList.add(new TcXyData(40, 16550, 16535));
        tcXyList.add(new TcXyData(50, 16808, 16889));
        tcXyList.add(new TcXyData(60, 17087, 17262));
        tcXyList.add(new TcXyData(70, 17385, 17653));
        tcXyList.add(new TcXyData(80, 17703, 18057));
        tcXyList.add(new TcXyData(90, 18039, 18473));
        tcXyList.add(new TcXyData(100, 18393, 18897));
        tcXyList.add(new TcXyData(110, 18763, 19327));
        tcXyList.add(new TcXyData(120, 19148, 19759));
        tcXyList.add(new TcXyData(130, 19548, 20192));
        tcXyList.add(new TcXyData(140, 19960, 20623));
        tcXyList.add(new TcXyData(150, 20383, 21049));
        tcXyList.add(new TcXyData(160, 20815, 21469));
        tcXyList.add(new TcXyData(170, 21257, 21880));
        tcXyList.add(new TcXyData(180, 21705, 22280));
        tcXyList.add(new TcXyData(190, 22159, 22669));
        tcXyList.add(new TcXyData(200, 22617, 23044));
        tcXyList.add(new TcXyData(210, 23079, 23406));
        tcXyList.add(new TcXyData(220, 23542, 23752));
        tcXyList.add(new TcXyData(230, 24006, 24082));
        tcXyList.add(new TcXyData(240, 24470, 24395));
        tcXyList.add(new TcXyData(250, 24933, 24691));
        tcXyList.add(new TcXyData(260, 25393, 24970));
        tcXyList.add(new TcXyData(270, 25851, 25231));
        tcXyList.add(new TcXyData(280, 26305, 25475));
        tcXyList.add(new TcXyData(290, 26754, 25701));
        tcXyList.add(new TcXyData(300, 27198, 25909));
        tcXyList.add(new TcXyData(310, 27637, 26101));
        tcXyList.add(new TcXyData(320, 28069, 26275));
        tcXyList.add(new TcXyData(330, 28495, 26433));
        tcXyList.add(new TcXyData(340, 28913, 26574));
        tcXyList.add(new TcXyData(350, 29324, 26700));
        tcXyList.add(new TcXyData(360, 29727, 26810));
        tcXyList.add(new TcXyData(370, 30123, 26906));
        tcXyList.add(new TcXyData(380, 30510, 26987));
        tcXyList.add(new TcXyData(390, 30889, 27055));
        tcXyList.add(new TcXyData(400, 31260, 27110));
        tcXyList.add(new TcXyData(410, 31623, 27153));
        tcXyList.add(new TcXyData(420, 31977, 27184));
        tcXyList.add(new TcXyData(430, 32323, 27204));
        tcXyList.add(new TcXyData(450, 32660, 27214));
        tcXyList.add(new TcXyData(460, 32990, 27214));
        tcXyList.add(new TcXyData(470, 33311, 27204));
        tcXyList.add(new TcXyData(480, 33624, 27186));
        tcXyList.add(new TcXyData(490, 33929, 27160));
        tcXyList.add(new TcXyData(500, 34227, 27126));
        tcXyList.add(new TcXyData(510, 34516, 27085));
        tcXyList.add(new TcXyData(520, 34799, 27038));
        tcXyList.add(new TcXyData(530, 35074, 26985));
        tcXyList.add(new TcXyData(540, 35342, 26926));
        tcXyList.add(new TcXyData(550, 35603, 26862));
        tcXyList.add(new TcXyData(560, 35857, 26793));
        tcXyList.add(new TcXyData(570, 36106, 26722));
        tcXyList.add(new TcXyData(580, 36346, 26643));
        tcXyList.add(new TcXyData(590, 36581, 26562));
        tcXyList.add(new TcXyData(600, 36811, 26479));
        tcXyList.add(new TcXyData(610, 37034, 26392));
        tcXyList.add(new TcXyData(620, 37251, 26303));
        tcXyList.add(new TcXyData(630, 37464, 26212));
        tcXyList.add(new TcXyData(640, 37671, 26118));
    }

    private static final double[][] TC_TO_XY_TABLE = new double[][] {
        {1000, 0.649926, 0.347373}, {2000, 0.382051, 0.383735}, {3000, 0.294976, 0.293084}, {4000, 0.251537, 0.252632}, {5000, 0.226676, 0.238825}, {6000, 0.210096, 0.230874}, {7000, 0.197830, 0.225496}, {8000, 0.188631, 0.221716}, {9000, 0.181464, 0.218934}, {10000, 0.175683, 0.216615}
    };

    public static int[] translateColourValueTCtoRGB(double tc) {
        // Placeholder for actual implementation
        // This is a simplified version of the algorithm and may not cover all cases
        double temperature = tc / 100.0;
        double red, green, blue;

        if (temperature <= 66) {
            red = 255;
            green = temperature - 2;
            green = 99.4708025861 * Math.log(green) - 161.1195681661;
            if (temperature <= 19) {
                blue = 0;
            } else {
                blue = temperature - 10;
                blue = 138.5177312231 * Math.log(blue) - 305.0447927307;
            }
        } else {
            red = temperature - 60;
            red = 329.698727446 * Math.pow(red, -0.1332047592);
            green = temperature - 60;
            green = 288.1221695283 * Math.pow(green, -0.0755148492);
            blue = 255;
        }

        return new int[]{
            Math.min(Math.max((int)red, 0), 255),
            Math.min(Math.max((int)green, 0), 255),
            Math.min(Math.max((int)blue, 0), 255)
        };
    }

    public static double translateColourValueRGBtoTC(double[] rgb) {
        // Implementation based on the provided formulas
        return 0.0; // Placeholder for actual implementation
    }

    public static double[] translateColourValueTCtoXY(double tc) {
        // Directly uses the TC_TO_XY_TABLE to find XY values
        return new double[2]; // Placeholder for actual implementation
    }

    public static double translateColourValueXYtoTC(double[] xy) {
        // Calculates Tc based on XY values using provided formulas
        return 0.0; // Placeholder for actual implementation
    }

    private record TcXyData(int mirek, int x, int y) {
    }
}
