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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.multi.ListValueMap;
import cn.hutool.core.map.multi.SetValueMap;

import java.util.HashSet;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CollValueMapTest {

    @Test
    public void testListValueMapRemove() {
        final ListValueMap<String, String> entries = new ListValueMap<>();
        entries.putValue("one", "11");
        entries.putValue("one", "22");
        entries.putValue("one", "33");
        entries.putValue("one", "22");

        entries.putValue("two", "44");
        entries.putValue("two", "55");

        entries.putValue("three", "11");

        entries.removeValue("one", "22");

        Assertions.assertEquals(ListUtil.of("11", "33", "22"), entries.get("one"));

        entries.removeValues("two", ListUtil.of("44", "55"));
        Assertions.assertEquals(ListUtil.empty(), entries.get("two"));
    }

    @Test
    public void testSetValueMapRemove() {
        final SetValueMap<String, String> entries = new SetValueMap<>();
        entries.putValue("one", "11");
        entries.putValue("one", "22");
        entries.putValue("one", "33");
        entries.putValue("one", "22");

        entries.putValue("two", "44");
        entries.putValue("two", "55");

        entries.putValue("three", "11");

        entries.removeValue("one", "22");
        Assertions.assertEquals(CollUtil.newHashSet("11", "33"), entries.get("one"));

        entries.removeValues("two", ListUtil.of("44", "55"));
        Assertions.assertEquals(CollUtil.empty(HashSet.class), entries.get("two"));
    }

}
