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

import cn.hutool.log.StaticLog;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

/**
 * 转换bean时，需要opens bean所在包
 *
 * <pre><code>
 * @JsonIgnore 使用@JsonIgnore注解JSON序列化时将忽略该字段
 * @JsonProperty 用于属性，把属性的名称序列化时转换为另外一个名称 @JsonProperty("blog-remark")
 * @JsonFormat 用于属性或者方法，把属性的格式序列化时转换成指定的格式。@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
 * @JsonPropertyOrder 用于类， 指定属性在序列化时 json 中的顺序 @JsonPropertyOrder({ "birth_Date", "name" })
 * @JsonCreator 用于构造方法，和 @JsonProperty 配合使用，适用有参数的构造方法
 * <code>
 * @JsonCreator public Person(@JsonProperty("name")String name) {…}
 * </code>
 * @JsonAnySetter 用于属性或者方法，设置未反序列化的属性名和值作为键值存储到 map 中
 * @JsonAnyGetter 用于方法 ，获取所有未序列化的属性
 * </code></pre>
 *
 * @author: unknowIfGuestInDream
 * @date: 2023/4/2 10:16
 */
public class JacksonUtil {
    private static final JsonMapper mapper = JsonMapper.builder()
        // 反序列化时忽略json中存在但Java对象不存在的属性
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        // 序列化时日期格式默认为yyyy-MM-dd'T'HH:mm:ss.SSSZ
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).build();

    static {
        // 序列化时自定义时间日期格式
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        // 序列化时忽略值为null的属性
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 序列化时忽略值为默认值的属性
        mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_DEFAULT);
        mapper.setTimeZone(TimeZone.getTimeZone("GMT+:08:00"));
    }

    private JacksonUtil() {
    }

    /**
     * 将对象转换成JSON数据
     *
     * @param data 对象
     * @return JSON数据
     */
    public static String bean2Json(Object data) {
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            StaticLog.error(e);
        }
        return null;
    }

    /**
     * 将JSON数据转换成对象
     *
     * @param jsonData JSON数据
     * @param beanType 对象类型
     * @return 对象
     */
    public static <T> T json2Bean(String jsonData, Class<T> beanType) {
        try {
            return mapper.readValue(jsonData, beanType);
        } catch (IOException e) {
            StaticLog.error(e);
        }
        return null;
    }

    /**
     * 将JSON数据转换成列表
     *
     * @param jsonData JSON数据
     * @param beanType 对象类型
     * @return 列表
     */
    public static <T> List<T> json2List(String jsonData, Class<T> beanType) {
        try {
            JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, beanType);
            List<T> resultList = mapper.readValue(jsonData, javaType);
            return resultList;
        } catch (Exception e) {
            StaticLog.error(e);
        }
        return null;
    }

    /**
     * 将JSON数据转换成Set集合
     *
     * @param jsonData    JSON数据
     * @param elementType 元素类型
     * @return Set集合
     */
    public static <E> Set<E> json2Set(String jsonData, Class<E> elementType) {
        try {
            JavaType javaType = mapper.getTypeFactory().constructCollectionType(Set.class, elementType);
            Set<E> resultSet = mapper.readValue(jsonData, javaType);
            return resultSet;
        } catch (Exception e) {
            StaticLog.error(e);
        }
        return null;
    }

    /**
     * 将JSON数据转换成Map集合
     *
     * @param jsonData  JSON数据
     * @param keyType   键类型
     * @param valueType 值类型
     * @return Map集合
     */
    public static <K, V> Map<K, V> json2Map(String jsonData, Class<K> keyType, Class<V> valueType) {
        try {
            JavaType javaType = mapper.getTypeFactory().constructMapType(Map.class, keyType, valueType);
            Map<K, V> resultMap = mapper.readValue(jsonData, javaType);
            return resultMap;
        } catch (Exception e) {
            StaticLog.error(e);
        }
        return null;
    }

    public static JsonMapper getMapper() {
        return mapper;
    }
}
