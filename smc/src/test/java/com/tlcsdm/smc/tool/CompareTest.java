package com.tlcsdm.smc.tool;

import org.junit.jupiter.api.Test;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Assert;

/**
 * @author: unknowIfGuestInDream
 * @date: 2022/12/7 18:56
 */
public class CompareTest {

    @Test
    public void CompareTest1() {
        String startDate = "2022-12-07 00:00:00";
        String submit = "2022-12-15 12:00:00";
        Assert.isTrue(startDate.compareTo(submit) == -1);
    }

    @Test
    public void resource2() {
        System.out.println(ResourceUtil.getResource("u2a_DTS_Transfer_request_Table.xlsx").getPath());
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
        System.out.println(ResourceUtil.getResource("/com/tlcsdm/smc/static/templates").getPath());
    }

}
