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
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

/**
 * 转换bean时，需要opens bean所在包
 *
 * <pre>{@code
 * @JsonIgnore 使用@JsonIgnore注解JSON序列化时将忽略该字段
 * @JsonProperty 用于属性，把属性的名称序列化时转换为另外一个名称 @JsonProperty("blog-remark")
 * @JsonFormat 用于属性或者方法，把属性的格式序列化时转换成指定的格式。@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
 * @JsonPropertyOrder 用于类， 指定属性在序列化时 json 中的顺序 @JsonPropertyOrder({ "birth_Date", "name" })
 * @JsonCreator 用于构造方法，和 @JsonProperty 配合使用，适用有参数的构造方法
 * @JsonCreator public Person(@JsonProperty("name")String name) {…}
 * @JsonAnySetter 用于属性或者方法，设置未反序列化的属性名和值作为键值存储到 map 中
 * @JsonAnyGetter 用于方法 ，获取所有未序列化的属性
 * }</pre>
 *
 * @author unknowIfGuestInDream
 * @date 2023/4/2 10:16
 */
public class JacksonUtil {
    private static final JsonMapper mapper = JsonMapper.builder()
        // 反序列化时忽略json中存在但Java对象不存在的属性
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        // 将null反序列化为基本数据类型是否报错
        .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false)
        // 无明确类型时，是否将json数组反序列化为java数组（若是true，就对应Object[] ,反之就是List<?>）
        .configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, false)
        // 出现重复的json字段是否报错
        .configure(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY, false)
        // 如果json中出现了java实体字段中已显式标记应当忽略的字段，是否报错
        .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
        // 是否将空字符("")串当作null对象
        .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, false)
        // 是否接受将空数组("[]")作为null
        .configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, false)
        // 是否接受将浮点数作为整数
        .configure(DeserializationFeature.ACCEPT_FLOAT_AS_INT, true)
        // 读取到未知的枚举当作null
        .configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, false)
        // 反序列化是否会适应DeserializationContext#getTimeZone()提供的时区 （此特性仅对java8的时间/日期有效）
        .configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, true)
        // 返回的java.util.date转换成时间戳
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true)
        // 将DURATIONS转换成时间戳
        .configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, true)
        // 是否将单个元素的集合展开，（即：去除数组符号"[]"）
        .configure(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED, false)
        // map序列化后，是否用key对其排序
        .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, false)
        // 是否使用缩进，格式化输出
        .configure(SerializationFeature.INDENT_OUTPUT, false)
        // 使用getter取代setter探测属性，这是针对集合类型，可以直接修改集合的属性
        .configure(MapperFeature.USE_GETTERS_AS_SETTERS, true)
        // 如何处理transient字段，如果true(不能访问此属性) ，若是false则不能通过字段访问（还是可以使用getter和setter访问）
        .configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, false)
        // 是否自动检测字段 （若true,则将所有public实例字段视为为属性）
        .configure(MapperFeature.AUTO_DETECT_FIELDS, true)
        // getter方法必需要有对应的setter或字段或构造方法参数，才能视为一个属性
        .configure(MapperFeature.REQUIRE_SETTERS_FOR_GETTERS, false)
        // 是否可以修改final成员字段
        .configure(MapperFeature.ALLOW_FINAL_FIELDS_AS_MUTATORS, true)
        // 是否能推断属性，(即使用字段和setter是不可见的，但getter可见即可推断属性)
        .configure(MapperFeature.INFER_PROPERTY_MUTATORS, true)
        // 调用AccessibleObject#setAccessible设为true .将原来不可见的属性，变为可见
        .configure(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS, true)
        // 按字母表顺序序列化字段（若false，按字段声明的顺序）
        .configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, false)
        // 反序列化属性时不区分大小写 （true时，会影响性能）
        .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, false)
        // 枚举反序列化不区别大小写
        .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, false)
        // 允许解析一些枚举的基于文本的值类型但忽略反序列化值的大小写 如日期/时间类型反序列化器
        .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_VALUES, false)
        // 是否允许json注解（Json规范是不能加注释的，但这里可以配置）
        .enable(JsonReadFeature.ALLOW_JAVA_COMMENTS)
        // 是否允许对所有字符都可加反斜杠转义
        .enable(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER)
        // 是否允许出现字段名不带引号
        .enable(JsonReadFeature.ALLOW_UNQUOTED_FIELD_NAMES)
        // 是否允许json数组中出现缺失值 （如["value1",,"value3",]将被反序列化为["value1", null, "value3", null]）
        .disable(JsonReadFeature.ALLOW_MISSING_VALUES)
        // 是否允许出现yaml注释
        .disable(JsonReadFeature.ALLOW_YAML_COMMENTS)
        // 是否允许json尾部有逗号 （如{"a": true,}）
        .disable(JsonReadFeature.ALLOW_TRAILING_COMMA)
        // 确定分析程序是否允许 JSON 字符串包含不带引号的控制字符（值小于 32 的 ASCII 字符，包括制表符和换行符）的功能。
        // 如果功能设置为 false，则在遇到此类字符时将引发异常
        .enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS)
        // 是否允许出现单引号,默认false
        .enable(JsonReadFeature.ALLOW_SINGLE_QUOTES)
        // 是否自动关闭不属于生成器的底层输出流
        .enable(JsonGenerator.Feature.AUTO_CLOSE_TARGET)
        // 是否自动补全json(当有不匹配的JsonToken#START_ARRAY JsonToken#START_OBJECT时)
        .enable(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT)
        // 是否刷新generator
        .enable(JsonGenerator.Feature.FLUSH_PASSED_TO_STREAM)
        // 非ASCII码是否需要转义
        .disable(JsonWriteFeature.ESCAPE_NON_ASCII)
        // 属性序列化的可见范围
        .visibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NON_PRIVATE)
        // 属性反序列化的可见范围
        .visibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.PROTECTED_AND_PUBLIC)
        // 静态工厂方法的反序列化
        .visibility(PropertyAccessor.CREATOR, JsonAutoDetect.Visibility.PUBLIC_ONLY)
        // 字段
        .visibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.PUBLIC_ONLY)
        // 布尔的序列化
        .visibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.PUBLIC_ONLY)
        // 时区
        .defaultTimeZone(TimeZone.getTimeZone("GMT+:08:00"))
        // 当地时区
        .defaultLocale(Locale.getDefault())
        // 序列化时忽略值为默认值的属性
        .defaultPropertyInclusion(JsonInclude.Value.construct(JsonInclude.Include.NON_DEFAULT, JsonInclude.Include.NON_DEFAULT))
        // 序列化时忽略值为null的属性
        .serializationInclusion(JsonInclude.Include.NON_NULL)
        // 序列化时自定义时间日期格式
        .defaultDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
        .build();

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
