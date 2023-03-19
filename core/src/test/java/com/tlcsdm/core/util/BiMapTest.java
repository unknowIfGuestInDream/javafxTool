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

import cn.hutool.core.map.BiMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class BiMapTest {

    @Test
    public void getTest() {
        final BiMap<String, Integer> biMap = new BiMap<>(new HashMap<>());
        biMap.put("aaa", 111);
        biMap.put("bbb", 222);

        Assertions.assertEquals(111, biMap.get("aaa"));
        Assertions.assertEquals(222, biMap.get("bbb"));

        Assertions.assertEquals("aaa", biMap.getKey(111));
        Assertions.assertEquals("bbb", biMap.getKey(222));
    }

    @Test
    public void computeIfAbsentTest() {
        final BiMap<String, Integer> biMap = new BiMap<>(new HashMap<>());
        biMap.put("aaa", 111);
        biMap.put("bbb", 222);

        biMap.computeIfAbsent("ccc", s -> 333);
        Assertions.assertEquals(333, biMap.get("ccc"));
        Assertions.assertEquals("ccc", biMap.getKey(333));
    }

    @Test
    public void putIfAbsentTest() {
        final BiMap<String, Integer> biMap = new BiMap<>(new HashMap<>());
        biMap.put("aaa", 111);
        biMap.put("bbb", 222);

        biMap.putIfAbsent("ccc", 333);
        Assertions.assertEquals(333, biMap.get("ccc"));
        Assertions.assertEquals("ccc", biMap.getKey(333));
    }
}
