/*
 * Copyright (c) 2023 unknowIfGuestInDream
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

package com.tlcsdm.smc.tool;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * @author unknowIfGuestInDream
 * @date 2022/12/7 18:56
 */
public class CompareTest {

    @Test
    public void CompareTest1() {
        String startDate = "2022-12-07 00:00:00";
        String submit = "2022-12-15 12:00:00";
        Assert.isTrue(startDate.compareTo(submit) == -1);
    }

    @Test
    @Disabled
    public void resource2() {
        System.out.println(ResourceUtil.getResource("demo.xlsx").getPath());
    }

    @Test
    public void getSub() {
        System.out.println(FileUtil.getSuffix("r_cg_dma.h"));
    }

    @Test
    public void str() {
        String s = "#define _TAUB_CHANNEL0_NEGATIVE_PHASE_PERIOD                (0x0001U) /* Negative phase period */";
        int i1 = s.indexOf("(");
        int i2 = s.indexOf(")");
        int i3 = s.indexOf("/*");
        String s1 = s.substring(0, 8);
        System.out.println(s1);
        String s2 = s.substring(8, i1);
        System.out.println(s2);
        String s3 = s.substring(i1, i2 + 1);
        System.out.println(s3);
        String s4 = s.substring(i3);
        System.out.println(s4);
    }

    @Test
    public void path() {
        System.out.println(ResourceUtil.getResource("templates").getPath());
    }

}
