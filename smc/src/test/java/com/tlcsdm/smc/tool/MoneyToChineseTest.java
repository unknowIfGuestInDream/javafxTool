package com.tlcsdm.smc.tool;

import com.tlcsdm.common.base.BaseUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

/**
 * @author: 唐 亮
 * @date: 2023/1/1 1:02
 */
public class MoneyToChineseTest {

    @Test
    @Disabled
    public void moneyToChinese() {
        System.out.println(BaseUtils.number2CNMonetaryUnit(new BigDecimal("203.5")));
    }
}
