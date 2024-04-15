package com.tlcsdm.core.util;

public class ColorValue {
    private int tc;
    private int[] xy;
    private RGBWAF rgbwaf;

    public ColorValue(int tc) {
        this.tc = tc;
    }

    public ColorValue(int[] xy) {
        this.xy = xy;
    }

    public ColorValue(int control, int[] value) {
        this.rgbwaf = new RGBWAF(control, value);
    }

    public int getTc() {
        return tc;
    }

    public int[] getXy() {
        return xy;
    }

    public RGBWAF getRgbwaf() {
        return rgbwaf;
    }

    public static class RGBWAF {
        private int control;
        private int[] value;

        public RGBWAF(int control, int[] value) {
            this.control = control;
            this.value = value;
        }

        public int getControl() {
            return control;
        }

        public int[] getValue() {
            return value;
        }
    }
}
