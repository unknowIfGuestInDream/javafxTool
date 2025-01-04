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

import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * @author unknowIfGuestInDream
 * @date 2023/3/19 18:46
 */
@Disabled("MultiMap.class is deprecated.")
public class MultiMapTest {

    private static MultiMap<String, String> multimap;

    @BeforeEach
    public void init() {
        multimap = new MultiMap<>();
        multimap.put("Zachary", "Taylor");
        multimap.put("John", "Adams");
        multimap.put("John", "Tyler");
        multimap.put("John", "Kennedy");
        multimap.put("George", "Washington");
        multimap.put("George", "Bush");
        multimap.put("Grover", "Cleveland");
    }

    @Test
    public void get() {
        Assertions.assertEquals(1, multimap.get("Zachary").size());
        Assertions.assertEquals(3, multimap.get("John").size());
        ArrayList<String> John = (ArrayList<String>) multimap.get("John");
        Assertions.assertEquals("Tyler", John.get(1));
        Assertions.assertEquals(2, multimap.get("George").size());
        Assertions.assertEquals(1, multimap.get("Grover").size());
        Assertions.assertFalse(multimap.containsKey("May"));
        Assertions.assertTrue(multimap.containsKey("John"));
        Assertions.assertEquals(7, multimap.size());
    }

    @Test
    public void put() {
        Assertions.assertEquals(3, multimap.get("John").size());
        multimap.put("John", "Tom");
        Assertions.assertEquals(4, multimap.get("John").size());
        multimap.putIfAbsent("John", "Tyler");
        Assertions.assertEquals(4, multimap.get("John").size());
    }

    @Test
    public void remove() {
        Assertions.assertEquals(3, multimap.get("John").size());
        Assertions.assertEquals(1, multimap.remove("Zachary").size());
        Assertions.assertEquals(3, multimap.get("John").size());
        Assertions.assertTrue(multimap.remove("John", "Tyler"));
        ArrayList<String> John = (ArrayList<String>) multimap.get("John");
        Assertions.assertNotEquals("Tyler", John.get(1));
        Assertions.assertEquals(2, multimap.get("John").size());
    }

    @Test
    public void clear() {
        Assertions.assertFalse(multimap.isEmpty());
        multimap.clear();
        Assertions.assertTrue(multimap.isEmpty());
    }
}
