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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * 需要在module.info中将jackson引用中去除static关键字才可启用测试类
 *
 * @author: unknowIfGuestInDream
 * @date: 2023/4/2 10:38
 */
@Disabled
public class JacksonTest {

    @Test
    public void entityToJson() throws Exception {
        //用户信息实体类
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName("pan_junbiao的博客");
        userInfo.setPassword("123456");
        userInfo.setBlogUrl("https://blog.csdn.net/pan_junbiao");
        userInfo.setBlogRemark("您好，欢迎访问 pan_junbiao的博客");
        userInfo.setCreateDate(new Date());

        //将参数转换成JSON对象
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(userInfo);
        //打印结果
        System.out.println(json);
    }

    @Test
    public void jsonToEntity() throws Exception {
        //JSON内容
        String json = "{\n" +
                "\t\"userName\": \"pan_junbiao的博客\",\n" +
                "\t\"password\": \"123456\",\n" +
                "\t\"blogUrl\": \"https://blog.csdn.net/pan_junbiao\",\n" +
                "\t\"blog-remark\": \"您好，欢迎访问 pan_junbiao的博客\",\n" +
                "\t\"createDate\": \"2020-05-30 16:18:28\"\n" +
                "}";

        //将JSON转换为实体类
        ObjectMapper mapper = new ObjectMapper();
        UserInfo userInfo = mapper.readValue(json, UserInfo.class);

        //打印结果
        System.out.println("用户名称：" + userInfo.getUserName());
        System.out.println("登录密码：" + userInfo.getPassword());
        System.out.println("博客地址：" + userInfo.getBlogUrl());
        System.out.println("博客信息：" + userInfo.getBlogRemark());
        System.out.println("创建时间：" + userInfo.getCreateDate());
    }

    @Test
    public void bean2Json() {
        //用户信息实体类
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName("pan_junbiao的博客");
        userInfo.setPassword("123456");
        userInfo.setBlogUrl("https://blog.csdn.net/pan_junbiao");
        userInfo.setBlogRemark("您好，欢迎访问 pan_junbiao的博客");
        userInfo.setCreateDate(new Date());

        //将参数转换成JSON对象
        String json = JacksonUtil.bean2Json(userInfo);
        //打印结果
        System.out.println(json);
    }

    @Test
    public void json2Bean() {
        //JSON内容
        String json = "{\n" +
                "\t\"userName\": \"pan_junbiao的博客\",\n" +
                "\t\"password\": \"123456\",\n" +
                "\t\"blogUrl\": \"https://blog.csdn.net/pan_junbiao\",\n" +
                "\t\"blog-remark\": \"您好，欢迎访问 pan_junbiao的博客\",\n" +
                "\t\"createDate\": \"2020-05-30 16:18:28\"\n" +
                "}";

        //将JSON转换为实体类
        UserInfo userInfo = JacksonUtil.json2Bean(json, UserInfo.class);

        //打印结果
        System.out.println("用户名称：" + userInfo.getUserName());
        System.out.println("登录密码：" + userInfo.getPassword());
        System.out.println("博客地址：" + userInfo.getBlogUrl());
        System.out.println("博客信息：" + userInfo.getBlogRemark());
        System.out.println("创建时间：" + userInfo.getCreateDate());
    }

    @Test
    public void json2Set() {
        Set<String> userSet = new HashSet<>();
        userSet.add("pan_junbiao的博客");
        userSet.add("您好，欢迎访问 pan_junbiao的博客");
        userSet.add("https://blog.csdn.net/pan_junbiao");

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
    public void json2Map() {
        Map<String, String> userMap = new HashMap<>();
        userMap.put("userName", "pan_junbiao的博客");
        userMap.put("blogUrl", "https://blog.csdn.net/pan_junbiao");
        userMap.put("blogRemark", "您好，欢迎访问 pan_junbiao的博客");

        //将map转化为JSON数据
        String json = JacksonUtil.bean2Json(userMap);

        //将JSON数据转换成Map集合
        Map<String, String> jsonToMap = JacksonUtil.json2Map(json, String.class, String.class);
        System.out.println("用户名称：" + jsonToMap.get("userName"));
        System.out.println("博客地址：" + jsonToMap.get("blogUrl"));
        System.out.println("博客备注：" + jsonToMap.get("blogRemark"));
    }
}
