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

import java.util.*;

/**
 * 可能与 ListValueMap 功能类似
 * 可以优先使用 ListValueMap
 *
 * @author: unknowIfGuestInDream
 * @date: 2023/3/19 18:41
 */
public class MultiMap<K, V> {
    private Map<K, Collection<V>> map = new HashMap<>();

    /**
     * 在这个multimap中用指定的键添加指定的值。
     */
    public void put(K key, V value) {
        map.computeIfAbsent(key, k -> new ArrayList<>());
        map.get(key).add(value);
    }

    /**
     * 如果没有，则将指定的键与给定的值相关联
     * 已经与一个值相关联
     */
    public void putIfAbsent(K key, V value) {
        map.computeIfAbsent(key, k -> new ArrayList<>());
        // 如果该值不存在，则插入它
        if (!map.get(key).contains(value)) {
            map.get(key).add(value);
        }
    }

    /**
     * 返回指定键映射到的值的集合，
     * 如果此multimap不包含键的映射，则为 null。
     */
    public Collection<V> get(Object key) {
        return map.get(key);
    }

    /**
     * 返回此multimap中包含的键的集合视图。
     */
    public Set<K> keySet() {
        return map.keySet();
    }

    /**
     * 返回此multimap中包含的映射的集合视图。
     */
    public Set<Map.Entry<K, Collection<V>>> entrySet() {
        return map.entrySet();
    }

    /**
     * 返回集合中存在的值的集合视图
     * 这个multimap。
     */
    public Collection<Collection<V>> values() {
        return map.values();
    }

    /**
     * 如果此multimap包含指定键的映射，则返回 true。
     */
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    /**
     * 如果存在，则从此multimap中删除指定键的映射
     * 并返回与键关联的先前值的集合，或
     * 如果没有键映射，则为 null。
     */
    public Collection<V> remove(Object key) {
        return map.remove(key);
    }

    /**
     * 返回此 multimap 中键值映射的总数。
     */
    public int size() {
        int size = 0;
        for (Collection<V> value : map.values()) {
            size += value.size();
        }
        return size;
    }

    /**
     * 如果此multimap不包含键值映射，则返回 true。
     */
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * 从此multimap中删除所有映射。
     */
    public void clear() {
        map.clear();
    }

    /**
     * 仅在当前为指定键时删除该条目
     * 映射到指定的值，如果移除则返回true
     */
    public boolean remove(K key, V value) {
        if (map.get(key) != null) {
            return map.get(key).remove(value);
        }

        return false;
    }

    /**
     * 仅当当前替换指定键的条目
     * 映射到指定的值，如果被替换则返回true
     */
    public boolean replace(K key, V oldValue, V newValue) {
        if (map.get(key) != null) {
            if (map.get(key).remove(oldValue)) {
                return map.get(key).add(newValue);
            }
        }
        return false;
    }
}
