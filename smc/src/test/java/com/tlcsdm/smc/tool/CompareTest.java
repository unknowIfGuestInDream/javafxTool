package com.tlcsdm.smc.tool;

import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

/**
 * @author: 唐 亮
 * @date: 2022/12/7 18:56
 */
public class CompareTest {

    @Test
    public void GirretTest1() {
        String startDate = "2022-12-07 00:00:00";
        String submit = "2022-12-15 12:00:00";
        Assert.isTrue(startDate.compareTo(submit) == -1);
    }
}
