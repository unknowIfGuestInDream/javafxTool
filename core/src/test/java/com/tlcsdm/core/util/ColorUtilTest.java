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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ColorUtilTest {

    @Test
    void rgb2Hex() {
        Assertions.assertEquals("#FFFFFF", ColorUtil.rgb2Hex(255, 255, 255));
        Assertions.assertEquals("#2E8B57", ColorUtil.rgb2Hex(46, 139, 87));
        Assertions.assertEquals("#2E8B57", ColorUtil.rgb2Hex(46, 139, 87));
        Assertions.assertEquals("#BA55D3", ColorUtil.rgb2Hex(186, 85, 211));
    }

    @Test
    void rgbToInt() {
        Assertions.assertEquals(-8355712, ColorUtil.rgbToInt(128, 128, 128));
        Assertions.assertEquals(new java.awt.Color(138, 43, 255).getRGB(), ColorUtil.rgbToInt(138, 43, 255));
        Assertions.assertEquals(new java.awt.Color(21, 80, 70).getRGB(), ColorUtil.rgbToInt(21, 80, 70));
    }

    @Test
    void hex2RGB() {
        Assertions.assertArrayEquals(new int[]{255, 255, 255}, ColorUtil.hex2RGB("#FFFFFF"));
        Assertions.assertArrayEquals(new int[]{186, 85, 211}, ColorUtil.hex2RGB("#BA55D3"));
    }

    @Test
    void color2Hex() {
        Assertions.assertEquals("#0000ff", ColorUtil.color2Hex(Color.BLUE));
        Assertions.assertEquals("#ffffff", ColorUtil.color2Hex(Color.WHITE));
        Assertions.assertEquals("#000000", ColorUtil.color2Hex(Color.BLACK));
        Assertions.assertEquals("#00ffff", ColorUtil.color2Hex(Color.CYAN));
    }

    @Test
    void rgb2Xyz() {
        Assertions.assertArrayEquals(new double[]{0.1805, 0.0722, 0.9505}, ColorUtil.rgb2Xyz(0, 0, 255));
        Assertions.assertArrayEquals(new double[]{0.9505, 1.0, 1.089}, ColorUtil.rgb2Xyz(255, 255, 255));
        Assertions.assertArrayEquals(new double[]{0.20517540535826123, 0.21586050011389923, 0.2350720846240363},
            ColorUtil.rgb2Xyz(128, 128, 128));
        Assertions.assertArrayEquals(new double[]{0.11280467230290668, 0.06731708050603462, 0.1489216023688173},
            ColorUtil.rgb2Xyz(120, 42, 108));
        Assertions.assertArrayEquals(new double[]{0.29395109306561823, 0.14351027398190436, 0.9582847252084468},
            ColorUtil.rgb2Xyz(138, 43, 255));
        Assertions.assertArrayEquals(new double[]{0.04283412134788732, 0.06338947481682042, 0.06792130844707142},
            ColorUtil.rgb2Xyz(21, 80, 70));
    }

    @Test
    void
    rgb2Coor() {
        Assertions.assertArrayEquals(new double[]{0.3333333333333333, 0.3333333333333333, 0.3333333333333333},
            ColorUtil.rgb2Coor(255, 255, 255));
    }

    @Test
    void
    coor2Rgb() {
        Assertions.assertArrayEquals(new double[]{0.21661409043112514, 0.7602523659305994, 0.023133543638275467},
            ColorUtil.coor2Rgb(0.34, 0.55));
    }

    @Test
    void xyz2Coor() {
        Assertions.assertArrayEquals(new double[]{0.1500166223404255, 0.060006648936170214},
            ColorUtil.xyz2Coor(0.1805, 0.0722, 0.9505));
    }

    @Test
    void xyz2Temperature() {
        Assertions.assertEquals(6051.981743443466, ColorUtil.xyz2Temperature(0.7360781208762206, 0.26392187912377935));
    }

    @Test
    void rgb2Temperature() {
        Assertions.assertEquals(5919.9125198469455, ColorUtil.rgb2Temperature(255, 247, 238));
    }

    @Test
    void temperature2Rgb() {
        Assertions.assertArrayEquals(new int[]{255, 247, 238}, ColorUtil.temperature2Rgb(6051.981743443466));
    }
}
