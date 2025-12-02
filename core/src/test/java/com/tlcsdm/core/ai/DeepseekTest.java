/*
 * Copyright (c) 2025 unknowIfGuestInDream.
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

package com.tlcsdm.core.ai;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tlcsdm.core.ai.deepseek.DeepSeekApiException;
import com.tlcsdm.core.ai.deepseek.DeepSeekChatClient;
import com.tlcsdm.core.util.JacksonUtil;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author unknowIfGuestInDream
 */
class DeepseekTest {

    private static final String aesKey = "3f4alpd3525678154a5e3a0183d8087b";
    private static final String encryptStr = "aac70872f21545ff2c56a590188bbe4c20035e0e130568c8dabeb699947e6cdf6df2dd72f573c55a8829ea53a2e22cd4";
    private static String token;

    @BeforeAll
    static void init() {
        AES aes = new AES(Mode.ECB, Padding.PKCS5Padding, aesKey.getBytes());
        token = aes.decryptStr(encryptStr);
    }

    @Test
    void ds() throws DeepSeekApiException {
        String apiKey = token;
        DeepSeekChatClient client = new DeepSeekChatClient(apiKey);
        // 简单对话
        System.out.println("用户: 你好，你是谁？");
        String reply = client.chat("你好，你是谁？", false, true);
        System.out.println("助手: " + reply);

        // 带上下文的后续对话
        System.out.println("用户: 我刚才问了你什么？");
        reply = client.chat("我刚才问了你什么？", false, false);
        System.out.println("助手: " + reply);

        // 流式对话
        System.out.println("用户: 请用流式方式告诉我关于Java 17的新特性");
        client.chatStream("请用流式方式告诉我关于Java 17的新特性", true, false, chunk -> {
            System.out.print(chunk);
            System.out.flush();
        }).join();

        // 查看对话历史
        System.out.println("\n对话历史:");
        client.getConversationHistory().forEach(msg -> {
            System.out.println(msg.getRole() + ": " + msg.getContent());
        });
    }

    @Test
    void use() throws DeepSeekApiException {
        String apiKey = token;
        DeepSeekChatClient client = new DeepSeekChatClient(apiKey);

        // 1. 简单对话
        String response = client.chat("Java 17有什么新特性？", true, false);
        System.out.println(response);

        // 2. 流式对话
        client.chatStream("详细解释Java 17的密封类", true, true, chunk -> {
            System.out.print(chunk);
        }).join();

        // 3. 带上下文的对话
        client.addUserMessage("记住我的名字是张三");
        client.addUserMessage("我的名字是什么？");
        String reply = client.chat("", false, false); // 空消息会使用历史上下文
        System.out.println(reply); // 应该回答"张三"
    }

    /**
     * 高级功能配置.
     */
    @Test
    void useBuilder() throws DeepSeekApiException {
        String apiKey = token;
        DeepSeekChatClient client = new DeepSeekChatClient(apiKey);
        // 使用特定模型，设置温度和最大token数
        String reply = client.chat("你的知识截止到什么时候？",
            "deepseek-chat",  // 模型
            false,                // 不使用联网搜索
            true,                 // 启用深度思考
            0.5,                  // 温度参数
            500);                 // 最大token数
        System.out.println(reply);

        // 在对话开始前设置系统角色消息
        client.addSystemMessage("你是一个专业的Java开发助手，回答要简洁专业");

        // 自定义流式响应处理器
        CompletableFuture<String> future = client.chatStream(
            "解释JVM的工作原理",
            "deepseek-chat",
            false,
            true,
            0.7,
            null,
            chunk -> {
                // 实时打印流式响应
                System.out.print(chunk);
                System.out.flush();
            });

        //为流式响应添加超时机制
        //        future.orTimeout(30, TimeUnit.SECONDS)
        //            .exceptionally(e -> {
        //                if (e instanceof TimeoutException) {
        //                    System.err.println("响应超时");
        //                }
        //                return null;
        //            });

        future.thenAccept(fullResponse -> {
            System.out.println("\n\n完整响应已接收，长度: " + fullResponse.length());
        }).exceptionally(e -> {
            System.err.println("对话失败: " + e.getMessage());
            return null;
        }).join();
    }

    /**
     * 查询账户余额.
     */
    @Test
    void queryUsage() throws Exception {
        String endpoint = "/user/balance";
        JsonNode response = makeApiRequest(endpoint, "GET", null);

        System.out.println("\n账户余额信息:");
        System.out.println("当前账户是否有余额可供 API 调用: " + response.path("is_available").asBoolean());
        response.path("balance_infos").valueStream().forEach(e -> {
            System.out.println("货币: " + e.path("currency").asText());
            System.out.println("总的可用余额: " + e.path("total_balance").asText());
            System.out.println("未过期的赠金余额: " + e.path("granted_balance").asText());
            System.out.println("充值余额: " + e.path("topped_up_balance").asText());
        });
    }

    /**
     * 列出可用模型.
     */
    @Test
    void listModels() throws Exception {
        String endpoint = "/models";
        JsonNode response = makeApiRequest(endpoint, "GET", null);

        System.out.println("\n可用模型列表:");
        JsonNode models = response.path("data");
        for (JsonNode model : models) {
            System.out.println("\n模型ID: " + model.path("id").asText());
            System.out.println("组织: " + model.path("owned_by").asText());
        }
    }

    /**
     * 通用的API请求方法.
     */
    private static JsonNode makeApiRequest(String endpoint, String method, String requestBody) throws Exception {
        HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
            .uri(URI.create("https://api.deepseek.com/v1" + endpoint))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + token);

        if ("GET".equalsIgnoreCase(method)) {
            requestBuilder.GET();
        } else if ("POST".equalsIgnoreCase(method)) {
            requestBuilder.POST(HttpRequest.BodyPublishers.ofString(requestBody));
        } else {
            throw new IllegalArgumentException("不支持的HTTP方法: " + method);
        }

        HttpRequest request = requestBuilder.build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("API请求失败: " + response.statusCode() + " - " + response.body());
        }
        ObjectMapper objectMapper = JacksonUtil.getJsonMapper();
        return objectMapper.readTree(response.body());
    }
}
