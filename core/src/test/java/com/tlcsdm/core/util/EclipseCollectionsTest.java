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

import com.tlcsdm.core.util.groovy.Person;
import org.eclipse.collections.api.bag.ImmutableBag;
import org.eclipse.collections.api.bag.MutableBag;
import org.eclipse.collections.api.bimap.ImmutableBiMap;
import org.eclipse.collections.api.bimap.MutableBiMap;
import org.eclipse.collections.api.factory.primitive.LongLists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.list.primitive.MutableBooleanList;
import org.eclipse.collections.api.list.primitive.MutableByteList;
import org.eclipse.collections.api.list.primitive.MutableCharList;
import org.eclipse.collections.api.list.primitive.MutableDoubleList;
import org.eclipse.collections.api.list.primitive.MutableFloatList;
import org.eclipse.collections.api.list.primitive.MutableIntList;
import org.eclipse.collections.api.list.primitive.MutableLongList;
import org.eclipse.collections.api.list.primitive.MutableShortList;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.multimap.ImmutableMultimap;
import org.eclipse.collections.api.multimap.MutableMultimap;
import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.api.stack.ImmutableStack;
import org.eclipse.collections.api.stack.MutableStack;
import org.eclipse.collections.impl.Counter;
import org.eclipse.collections.impl.factory.Bags;
import org.eclipse.collections.impl.factory.BiMaps;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Maps;
import org.eclipse.collections.impl.factory.Multimaps;
import org.eclipse.collections.impl.factory.Sets;
import org.eclipse.collections.impl.factory.Stacks;
import org.eclipse.collections.impl.factory.primitive.BooleanLists;
import org.eclipse.collections.impl.factory.primitive.ByteLists;
import org.eclipse.collections.impl.factory.primitive.CharLists;
import org.eclipse.collections.impl.factory.primitive.DoubleLists;
import org.eclipse.collections.impl.factory.primitive.FloatLists;
import org.eclipse.collections.impl.factory.primitive.IntLists;
import org.eclipse.collections.impl.factory.primitive.ShortLists;
import org.eclipse.collections.impl.list.primitive.IntInterval;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author unknowIfGuestInDream
 */
class EclipseCollectionsTest {

    private static List<String> words;

    @BeforeAll
    static void loadData() {
        words = Lists.mutable.of((
                "Bah, Bah, black sheep,\n" +
                    "Have you any wool?\n").split("[ ,\n?]+")
                                );
    }

    @Test
    void jdk() {
        var people = List.of(new Person("Alice", "", 19),
            new Person("Bob", "", 52), new Person("Carol", "", 35));

        var namesOver21 = people.stream()               // Bun
            .filter(person -> person.getAge() > 21)  // Meat
            .map(Person::getName)                    // Meat
            .toList();           // Bun

        namesOver21.forEach(System.out::println);
    }

    @Test
    void collections() {
        var people = Lists.immutable.of(new Person("Alice", "", 19),
            new Person("Bob", "", 52), new Person("Carol", "", 35));

        var namesOver21 = people
            .select(person -> person.getAge() > 21) // Meat, no buns
            .collect(Person::getName);              // Meat

        namesOver21.forEach(System.out::println);
    }

    /**
     * 原生jdk实现
     */
    @Test
    public void countJdkNaive() {
        Map<String, Integer> wordCount = new HashMap<>();

        words.forEach(w -> {
            int count = wordCount.getOrDefault(w, 0);
            count++;
            wordCount.put(w, count);
        });

        System.out.println(wordCount);
        Assertions.assertEquals(2, wordCount.get("Bah").intValue());
        Assertions.assertEquals(1, wordCount.get("sheep").intValue());
    }

    /**
     * Stream实现
     */
    @Test
    void countJdkStream() {
        Map<String, Long> wordCounts = words.stream()
            .collect(Collectors.groupingBy(w -> w, Collectors.counting()));
        Assertions.assertEquals(2, wordCounts.get("Bah").intValue());
        Assertions.assertEquals(1, wordCounts.get("sheep").intValue());
    }

    /**
     * 引入计数器
     */
    @Test
    public void countJdkEfficient() {
        Map<String, Counter> wordCounts = new HashMap<>();

        words.forEach(
            w -> {
                Counter counter = wordCounts.computeIfAbsent(w, x -> new Counter());
                counter.increment();
            }
                     );

        Assertions.assertEquals(2, wordCounts.get("Bah").getCount());
        Assertions.assertEquals(1, wordCounts.get("sheep").getCount());
    }

    @Test
    void mutable() {
        //Initializing mutable list with empty(), of(), with() method
        MutableList<String> mutableListEmpty =
            Lists.mutable.empty();
        MutableList<String> mutableListOf =
            Lists.mutable.of("One", "One", "Two", "Three");
        MutableList<String> mutableListWith =
            Lists.mutable.with("One", "One", "Two", "Three");

        //Various container types available
        MutableSet<String> mutableSet =
            Sets.mutable.with("One", "One", "Two", "Three");
        MutableBag<String> mutableBag =
            Bags.mutable.with("One", "One", "Two", "Three");
        MutableStack<String> mutableStack =
            Stacks.mutable.with("One", "One", "Two", "Three");
        MutableMap<String, String> mutableMap =
            Maps.mutable.with("key1", "value1", "key2", "value2", "key3", "value3");
        MutableMultimap<String, String> multimapWithList =
            Multimaps.mutable.list.with("key1", "value1-1", "key1", "value1-2", "key2", "value2-1");
        MutableBiMap<String, String> mutableBiMap =
            BiMaps.mutable.with("key1", "value1", "key2", "value2", "key3", "value3");
    }

    @Test
    void immutable() {
        //Initializing immutable list with empty(), of(), with() method
        ImmutableList<String> immutableListEmpty =
            Lists.immutable.empty();
        ImmutableList<String> immutableListOf =
            Lists.immutable.of("One", "One", "Two", "Three");
        ImmutableList<String> immutableListWith =
            Lists.immutable.with("One", "One", "Two", "Three");

        //Various container types available
        ImmutableSet<String> immutableSet =
            Sets.immutable.with("One", "One", "Two", "Three");
        ImmutableBag<String> immutableBag =
            Bags.immutable.with("One", "One", "Two", "Three");
        ImmutableStack<String> immutableStack =
            Stacks.immutable.with("One", "One", "Two", "Three");
        ImmutableMap<String, String> immutableMap =
            Maps.immutable.with("key1", "value1", "key2", "value2", "key3", "value3");
        ImmutableMultimap<String, String> immutableMultimapWithList =
            Multimaps.immutable.list.with("key1", "value1-1", "key1", "value1-2", "key2", "value2-1");
        ImmutableBiMap<String, String> immutableBiMap =
            BiMaps.immutable.with("key1", "value1", "key2", "value2", "key3", "value3");
    }

    @Test
    void primitive() {
        //Mutable and immutable Lists, Sets, Bags, Stacks and Maps are available for all 8 primitive types
        MutableIntList intList =
            IntLists.mutable.of(1, 2, 3);
        MutableLongList longList =
            LongLists.mutable.of(1L, 2L, 3L);
        MutableCharList charList =
            CharLists.mutable.of('a', 'b', 'c');
        MutableShortList shortList =
            ShortLists.mutable.of((short) 1, (short) 2, (short) 3);
        MutableByteList byteList =
            ByteLists.mutable.of((byte) 1, (byte) 2, (byte) 3);
        MutableBooleanList booleanList =
            BooleanLists.mutable.of(true, false);
        MutableFloatList floatList =
            FloatLists.mutable.of(1.0f, 2.0f, 3.0f);
        MutableDoubleList doubleList =
            DoubleLists.mutable.of(1.0, 2.0, 3.0);

        //You can create a ranged ints with IntInterval
        IntInterval oneTo10 =
            IntInterval.fromTo(1, 10); // ints from 1 to 10
        // [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
        IntInterval oneTo10By3 =
            IntInterval.fromToBy(1, 10, 3); // ints from 1 to 10 step by 3
        // [1, 4, 7, 10]
        IntInterval oddsFrom1To10 =
            IntInterval.oddsFromTo(1, 10); // odd ints from 1 to 10
        // [1, 3, 5, 7, 9]
        IntInterval evensFrom1To10 =
            IntInterval.evensFromTo(1, 10); // even ints from 1 to 10
        // [2, 4, 6, 8, 10]
    }

}
