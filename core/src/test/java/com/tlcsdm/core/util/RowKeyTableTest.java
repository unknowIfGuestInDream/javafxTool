/*
 * Copyright (c) 2019, 2023 unknowIfGuestInDream
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

import cn.hutool.core.map.multi.RowKeyTable;
import cn.hutool.core.map.multi.Table;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class RowKeyTableTest {

    @Test
    public void putGetTest() {
        final Table<Integer, Integer, Integer> table = new RowKeyTable<>();
        table.put(1, 2, 3);
        table.put(1, 6, 4);

        Assertions.assertEquals(3, table.get(1, 2));
        Assertions.assertNull(table.get(1, 3));

        //判断row和column确定的二维点是否存在
        Assertions.assertTrue(table.contains(1, 2));
        Assertions.assertFalse(table.contains(1, 3));

        //判断列
        Assertions.assertTrue(table.containsColumn(2));
        Assertions.assertFalse(table.containsColumn(3));

        // 判断行
        Assertions.assertTrue(table.containsRow(1));
        Assertions.assertFalse(table.containsRow(2));

        // 获取列
        Map<Integer, Integer> column = table.getColumn(6);
        Assertions.assertEquals(1, column.size());
        Assertions.assertEquals(4, column.get(1));
    }
}
