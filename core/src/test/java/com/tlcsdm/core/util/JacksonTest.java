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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 需要在module.info中将jackson引用中去除static关键字才可启用测试类.
 *
 * @author unknowIfGuestInDream
 * @date 2023/4/2 10:38
 */
@Disabled
class JacksonTest {

    @Test
    void entityToJson() throws Exception {
        //用户信息实体类
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName("Guest");
        userInfo.setPassword("123456");
        userInfo.setBlogUrl("https://www.baidu.com");
        userInfo.setBlogRemark("您好，欢迎访问 Guest");
        userInfo.setCreateDate(new Date());
        //将参数转换成JSON对象
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(userInfo);
        //打印结果
        System.out.println(json);
    }

    @Test
    void jsonToEntity() throws Exception {
        //JSON内容
        String json = "{\n" +
            "\t\"userName\": \"Guest\",\n" +
            "\t\"password\": \"123456\",\n" +
            "\t\"blogUrl\": \"https://www.baidu.com\",\n" +
            "\t\"blog-remark\": \"您好，欢迎访问 Guest\",\n" +
            "\t\"createDate\": \"2020-05-30 16:18:28\"\n" +
            "}";
        //将JSON转换为实体类
        ObjectMapper mapper = new ObjectMapper();
        UserInfo userInfo = mapper.readValue(json, UserInfo.class);
        //打印结果
        Assertions.assertEquals("Guest", userInfo.getUserName());
        Assertions.assertNull(userInfo.getPassword());
        Assertions.assertEquals("https://www.baidu.com", userInfo.getBlogUrl());
        Assertions.assertEquals("您好，欢迎访问 Guest", userInfo.getBlogRemark());
        Assertions.assertEquals(1590826708000L, userInfo.getCreateDate().getTime());
    }

    @Test
    void bean2Json() {
        //用户信息实体类
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName("Guest");
        userInfo.setPassword("123456");
        userInfo.setBlogUrl("https://www.baidu.com");
        userInfo.setBlogRemark("您好，欢迎访问 Guest");
        userInfo.setCreateDate(new Date());
        //将参数转换成JSON对象
        String json = JacksonUtil.bean2Json(userInfo);
        System.out.println(json);
    }

    @Test
    void json2Bean() {
        //JSON内容
        String json = "{\n" +
            "\t\"userName\": \"Guest\",\n" +
            "\t\"password\": \"123456\",\n" +
            "\t\"blogUrl\": \"https://www.baidu.com\",\n" +
            "\t\"blog-remark\": \"您好，欢迎访问 Guest\",\n" +
            "\t\"createDate\": \"2020-05-30 16:18:28\"\n" +
            "}";
        //将JSON转换为实体类
        UserInfo userInfo = JacksonUtil.json2Bean(json, UserInfo.class);
        Assertions.assertEquals("Guest", userInfo.getUserName());
        Assertions.assertNull(userInfo.getPassword());
        Assertions.assertEquals("https://www.baidu.com", userInfo.getBlogUrl());
        Assertions.assertEquals("您好，欢迎访问 Guest", userInfo.getBlogRemark());
        Assertions.assertEquals(1590826708000L, userInfo.getCreateDate().getTime());
    }

    @Test
    void json2Set() {
        Set<String> userSet = new HashSet<>();
        userSet.add("Guest");
        userSet.add("您好，欢迎访问 Guest");
        userSet.add("https://www.baidu.com");

        //将Set转化为JSON数据
        String json = JacksonUtil.bean2Json(userSet);
        System.out.println(json + "\n");

        //将JSON数据转换成Set集合
        Set<String> jsonToSet = JacksonUtil.json2Set(json, String.class);
        for (String item : jsonToSet) {
            System.out.println(item);
        }
    }

    @Test
    void json2Map() {
        Map<String, String> userMap = new HashMap<>();
        userMap.put("userName", "Guest");
        userMap.put("blogUrl", "https://www.baidu.com");
        userMap.put("blogRemark", "您好，欢迎访问 Guest");

        //将map转化为JSON数据
        String json = JacksonUtil.bean2Json(userMap);

        //将JSON数据转换成Map集合
        Map<String, String> jsonToMap = JacksonUtil.json2Map(json, String.class, String.class);
        Assertions.assertEquals("Guest", jsonToMap.get("userName"));
        Assertions.assertEquals("https://www.baidu.com", jsonToMap.get("blogUrl"));
        Assertions.assertEquals("您好，欢迎访问 Guest", jsonToMap.get("blogRemark"));
    }

    /**
     * 忽略未知字段配置测试.
     */
    @Test
    void testUnknownProperties() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String json = "{\"age\":10,\"name\":\"曹操\",\"class\":\"语文\"}";
        Assertions.assertDoesNotThrow(() -> objectMapper.readValue(json, User.class));
    }

    /**
     * 属性为NULL不被序列化测试.
     */
    @Test
    void testNonNull() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        User user = new User();
        user.setAge(10);
        Assertions.assertDoesNotThrow(() -> objectMapper.writeValueAsString(user));
    }

    /**
     * json字符串值带反斜杠("\")，默认反序列化会失败测试.
     */
    @Test
    void testFailOnEmptyBeans() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER.mappedFeature(), true);
        String json = "{\"age\":10,\"name\":\"曹\\操\"}";
        Assertions.assertDoesNotThrow(() -> objectMapper.readValue(json, User.class));
    }

    /**
     * json字符串带注释符，默认反序列化会失败测试.
     */
    @Test
    void testAllowComments() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        String json = "{"
            + "\"age\"" + ":" + 10 +
            "/*" + "," +
            "\"name\"" + ":" + "\"曹操\"*/" +
            "}";
        //Feature.ALLOW_COMMENTS打开时，JSON里的注释符会被过滤掉,解析器能解析
        Assertions.assertDoesNotThrow(() -> objectMapper.readValue(json, User.class));
    }

    /**
     * 反序列Json字符串中包含制控制字符测试.
     */
    @Test
    void testAllowSingleQuotes() {
        ObjectMapper objectMapper = new ObjectMapper();
        //开启单引号解析
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        //开启JSON字符串包含非引号控制字符的解析（\n换行符）
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        String json = "{'age':12, 'name':'曹操\n'}";
        Assertions.assertDoesNotThrow(() -> objectMapper.readValue(json, User.class));
    }

    /**
     * 反序列Json字符串中属性名没有双引号测试.
     */
    @Test
    void testAllowUnquotedFieldNames() {
        ObjectMapper objectMapper = new ObjectMapper();
        //开启单引号解析
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        //开启属性名没有双引号的非标准json字符串
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        String json = "{age:12, name:'曹操'}";
        Assertions.assertDoesNotThrow(() -> objectMapper.readValue(json, User.class));
    }

}
