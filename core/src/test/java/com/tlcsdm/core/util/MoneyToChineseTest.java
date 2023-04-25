package com.tlcsdm.core.util;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

/**
 * @author: unknowIfGuestInDream
 * @date: 2023/1/1 1:02
 */
public class MoneyToChineseTest {

    @Test
    @Disabled
    public void moneyToChinese() {
        System.out.println(MoneyToChineseUtil.number2CNMonetaryUnit(new BigDecimal("203.5")));
    }
}
