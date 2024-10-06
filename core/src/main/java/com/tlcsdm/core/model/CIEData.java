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

package com.tlcsdm.core.model;

/**
 * @author unknowIfGuestInDream
 */
public class CIEData {
    private int waveLength;
    private double X, Y, Z;
    private double normalizedX, normalizedY, normalizedZ; // x + y + z = 1
    private double relativeX, relativeY, relativeZ; // y = 1;

    public CIEData(double x, double y) {
        setxy(x, y, 1.0);
    }

    public CIEData(int waveLength, double X, double Y, double Z) {
        this.waveLength = waveLength;
        this.X = X;
        this.Y = Y;
        this.Z = Z;
        setTristimulusValues(X, Y, Z);
    }

    public final void setxy(double x, double y, double Y) {
        this.normalizedX = x;
        this.normalizedY = y;
        this.normalizedZ = 1 - x - y;

        this.X = x * Y / y;
        this.Y = Y;
        this.Z = (1 - x - y) * Y / y;

        this.relativeX = x / y;
        this.relativeY = 1.0;
        this.relativeZ = (1 - x - y) / y;
    }

    public final void setTristimulusValues(double X, double Y, double Z) {
        this.X = X;
        this.Y = Y;
        this.Z = Z;

        double[] xyz = normalize(X, Y, Z);
        this.normalizedX = xyz[0];
        this.normalizedY = xyz[1];
        this.normalizedZ = xyz[2];

        xyz = relative(X, Y, Z);
        this.relativeX = xyz[0];
        this.relativeY = xyz[1];
        this.relativeZ = xyz[2];
    }

    public static double[] normalize(double X, double Y, double Z) {
        double[] xyz = new double[3];
        double sum = X + Y + Z;
        if (sum == 0) {
            return array(X, Y, Z);
        }
        xyz[0] = X / sum;
        xyz[1] = Y / sum;
        xyz[2] = Z / sum;
        return xyz;
    }

    public static double[] relative(double x, double y, double z) {
        if (y == 0) {
            return array(x, y, z);
        }
        double[] xyz = new double[3];
        xyz[0] = x / y;
        xyz[1] = 1.0;
        xyz[2] = z / y;
        return xyz;
    }

    private static double[] array(double r, double g, double b) {
        return new double[]{r, g, b};
    }

    public static double[] sRGB65(CIEData d) {
        double[] xyz = array(d.getRelativeX(), d.getRelativeY(), d.getRelativeZ());
        return XYZd50toSRGBd65(xyz);
    }

    public static double[] XYZd50toSRGBd65(double[] xyzD50) {
        double[] xyzD65 = D50toD65(xyzD50);
        return XYZd65toSRGBd65(xyzD65);
    }

    public static double[] XYZd50toSRGBd65(double X, double Y, double Z) {
        return XYZd50toSRGBd65(array(X, Y, Z));
    }

    public static double[] XYZd65toSRGBd65(double[] xyzd65) {
        double[] linearRGB = XYZd65toSRGBd65Linear(xyzd65);
        double[] srgb = gammaSRGB(linearRGB);
        return srgb;
    }

    public static double[] gammaSRGB(double[] linearRGB) {
        double[] srgb = new double[3];
        srgb[0] = gammaSRGB(linearRGB[0]);
        srgb[1] = gammaSRGB(linearRGB[1]);
        srgb[2] = gammaSRGB(linearRGB[2]);
        return srgb;
    }

    public static double gammaSRGB(double v) {
        if (v <= 0.0031308) {
            return v * 12.92;
        } else {
            return 1.055 * Math.pow(v, 1d / 2.4) - 0.055;
        }
    }

    public static double[] XYZd65toSRGBd65Linear(double[] xyzd65) {
        double[][] matrix = {
            {3.2409699419045235, -1.5373831775700944, -0.49861076029300355},
            {-0.9692436362808797, 1.8759675015077204, 0.0415550574071756},
            {0.05563007969699365, -0.20397695888897652, 1.0569715142428786}
        };
        double[] linearRGB = multiply(matrix, xyzd65);
        linearRGB = clipRGB(linearRGB);
        return linearRGB;
    }

    public static double[] D50toD65(double[] d50) {
        double[][] matrix = {
            {0.9555766, -0.0230393, 0.0631636},
            {-0.0282895, 1.0099416, 0.0210077},
            {0.0122982, -0.0204830, 1.3299098}
        };
        double[] d65 = multiply(matrix, d50);
        return d65;
    }

    public static double[] clipRGB(double[] rgb) {
        double[] outputs = new double[rgb.length];
        System.arraycopy(rgb, 0, outputs, 0, rgb.length);
        for (int i = 0; i < outputs.length; ++i) {
            if (outputs[i] < 0) {
                outputs[i] = 0;
            }
            if (outputs[i] > 1) {
                outputs[i] = 1;
            }
        }
        return outputs;
    }

    public static double[] multiply(double[][] matrix, double[] columnVector) {
        if (matrix == null || columnVector == null) {
            return null;
        }
        double[] outputs = new double[matrix.length];
        for (int i = 0; i < matrix.length; ++i) {
            double[] row = matrix[i];
            outputs[i] = 0d;
            for (int j = 0; j < Math.min(row.length, columnVector.length); ++j) {
                outputs[i] += row[j] * columnVector[j];
            }
        }
        return outputs;
    }

    public double getX() {
        return X;
    }

    public void setX(double X) {
        this.X = X;
    }

    public double getY() {
        return Y;
    }

    public void setY(double Y) {
        this.Y = Y;
    }

    public double getZ() {
        return Z;
    }

    public void setZ(double Z) {
        this.Z = Z;
    }

    public int getWaveLength() {
        return waveLength;
    }

    public void setWaveLength(int waveLength) {
        this.waveLength = waveLength;
    }

    public double getNormalizedX() {
        return normalizedX;
    }

    public double getNormalizedY() {
        return normalizedY;
    }

    public double getNormalizedZ() {
        return normalizedZ;
    }

    public double getRelativeX() {
        return relativeX;
    }

    public double getRelativeZ() {
        return relativeZ;
    }

    public double getRelativeY() {
        return relativeY;
    }
}
