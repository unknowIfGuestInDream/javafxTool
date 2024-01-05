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
        Assertions.assertEquals("0xFFFFFFFF", ColorUtil.rgb2Hex(255, 255, 255));
    }

    @Test
    void rgbToInt() {
        Assertions.assertEquals(-8355712, ColorUtil.rgbToInt(128, 128, 128));
    }

    @Test
    void hex2RGB() {
        int[] rgb = ColorUtil.hex2RGB("#FFFFFF");
        Assertions.assertEquals(255, rgb[0]);
        Assertions.assertEquals(255, rgb[1]);
        Assertions.assertEquals(255, rgb[2]);
    }

    @Test
    void color2Hex() {
        Assertions.assertEquals("#0000ff", ColorUtil.color2Hex(Color.BLUE));
    }

    @Test
    void rgb2Xyz() {
        double[] xyz = ColorUtil.rgb2Xyz(128, 0, 0);
        Assertions.assertEquals(356.992, xyz[0]);
        Assertions.assertEquals(128.0, xyz[1]);
        Assertions.assertEquals(0.0, xyz[2]);
    }

    @Test
    void xyz2Coor() {
        double[] coor = ColorUtil.xyz2Coor(356.992, 128.0, 0.0);
        Assertions.assertEquals(0.7360781208762206, coor[0]);
        Assertions.assertEquals(0.26392187912377935, coor[1]);
    }

    @Test
    void coor2Cct() {
        Assertions.assertEquals(6051.981743443466, ColorUtil.coor2Temperature(0.7360781208762206, 0.26392187912377935));
    }

    @Test
    void rgb2Temperature() {
        Assertions.assertEquals(5186.987858143659, ColorUtil.rgb2Temperature(255, 247, 238));
    }

    @Test
    void temperature2Rgb() {
        int[] rgb = ColorUtil.temperature2Rgb(6051.981743443466);
        Assertions.assertEquals(255, rgb[0]);
        Assertions.assertEquals(247, rgb[1]);
        Assertions.assertEquals(238, rgb[2]);
    }
}
