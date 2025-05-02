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

package com.tlcsdm.core.ai.deepseek;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tlcsdm.core.util.JacksonUtil;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * @author unknowIfGuestInDream
 */
public class DeepSeekChatClient {
    private static final String API_BASE_URL = "https://api.deepseek.com/v1";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String apiKey;
    //对话上下文
    private final List<ChatMessage> conversationHistory;

    public DeepSeekChatClient(String apiKey) {
        this.apiKey = apiKey;
        this.conversationHistory = new ArrayList<>();
        this.httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(30))
            .build();

        this.objectMapper = JacksonUtil.getJsonMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    // 同步聊天方法
    public String chat(String userMessage, boolean webSearch, boolean deepThought) throws DeepSeekApiException {
        return chat(userMessage, "deepseek-chat", webSearch, deepThought, 0.7, null);
    }

    public String chat(String userMessage, String model, boolean webSearch, boolean deepThought,
        Double temperature, Integer maxTokens) throws DeepSeekApiException {
        // 添加用户消息到历史
        addUserMessage(userMessage);

        // 构建请求
        ChatCompletionRequest request = new ChatCompletionRequest(
            model,
            new ArrayList<>(conversationHistory),
            temperature,
            maxTokens,
            false, // 非流式
            webSearch,
            deepThought
        );

        try {
            ChatCompletionResponse response = createChatCompletion(request);

            if (response.getChoices() != null && !response.getChoices().isEmpty()) {
                String assistantReply = response.getChoices().get(0).getMessage().getContent();
                // 添加助手回复到历史
                addAssistantMessage(assistantReply);
                return assistantReply;
            }
            throw new DeepSeekApiException("No response choices available");
        } catch (DeepSeekApiException e) {
            // 移除最后一条用户消息，因为对话失败
            if (!conversationHistory.isEmpty() &&
                "user".equals(conversationHistory.get(conversationHistory.size() - 1).getRole())) {
                conversationHistory.remove(conversationHistory.size() - 1);
            }
            throw e;
        }
    }

    // 异步流式聊天方法
    public CompletableFuture<String> chatStream(String userMessage, boolean webSearch, boolean deepThought,
        Consumer<String> chunkConsumer) {
        return chatStream(userMessage, "deepseek-chat", webSearch, deepThought, 0.7, null, chunkConsumer);
    }

    public CompletableFuture<String> chatStream(String userMessage, String model, boolean webSearch, boolean deepThought,
        Double temperature, Integer maxTokens, Consumer<String> chunkConsumer) {
        // 添加用户消息到历史
        addUserMessage(userMessage);

        // 构建请求
        ChatCompletionRequest request = new ChatCompletionRequest(
            model,
            new ArrayList<>(conversationHistory),
            temperature,
            maxTokens,
            true, // 流式
            webSearch,
            deepThought
        );

        CompletableFuture<String> future = new CompletableFuture<>();
        StringBuilder fullResponse = new StringBuilder();

        try {
            String requestBody = objectMapper.writeValueAsString(request);

            HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + "/chat/completions"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

            // 使用 sendAsync 并手动处理流
            httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofLines())
                .thenAccept(response -> {
                    if (response.statusCode() >= 200 && response.statusCode() < 300) {
                        try {
                            // 直接处理 Stream<String>
                            response.body().forEach(line -> {
                                try {
                                    if (line.startsWith("data: ") && !line.equals("data: [DONE]")) {
                                        String data = line.substring(6);
                                        ChatCompletionStreamResponse chunk = objectMapper.readValue(
                                            data, ChatCompletionStreamResponse.class);

                                        if (chunk.getChoices() != null && !chunk.getChoices().isEmpty()) {
                                            String content = chunk.getChoices().get(0).getDelta().getContent();
                                            if (content != null) {
                                                chunkConsumer.accept(content);
                                                fullResponse.append(content);
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    future.completeExceptionally(e);
                                }
                            });

                            // 添加助手回复到历史
                            String responseText = fullResponse.toString();
                            addAssistantMessage(responseText);
                            future.complete(responseText);

                        } catch (Exception e) {
                            future.completeExceptionally(e);
                        } finally {
                            response.body().close();
                        }
                    } else {
                        future.completeExceptionally(new DeepSeekApiException(
                            "API request failed with status code: " + response.statusCode()));
                    }
                })
                .exceptionally(e -> {
                    future.completeExceptionally(e);
                    return null;
                });

        } catch (Exception e) {
            future.completeExceptionally(e);
        }

        return future;
    }

    // 历史记录管理方法
    public void addSystemMessage(String content) {
        conversationHistory.add(new ChatMessage("system", content));
    }

    public void addUserMessage(String content) {
        conversationHistory.add(new ChatMessage("user", content));
    }

    public void addAssistantMessage(String content) {
        conversationHistory.add(new ChatMessage("assistant", content));
    }

    public void clearConversationHistory() {
        conversationHistory.clear();
    }

    public List<ChatMessage> getConversationHistory() {
        return new ArrayList<>(conversationHistory);
    }

    // 核心API调用方法
    private ChatCompletionResponse createChatCompletion(ChatCompletionRequest request) throws DeepSeekApiException {
        try {
            String requestBody = objectMapper.writeValueAsString(request);

            HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + "/chat/completions"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return objectMapper.readValue(response.body(), ChatCompletionResponse.class);
            } else {
                throw new DeepSeekApiException("API request failed with status code: " + response.statusCode() +
                    ", response: " + response.body());
            }
        } catch (JsonProcessingException e) {
            throw new DeepSeekApiException("Failed to serialize request or deserialize response", e);
        } catch (Exception e) {
            throw new DeepSeekApiException("HTTP request failed", e);
        }
    }

    private void streamChatCompletion(ChatCompletionRequest request, Flow.Subscriber<String> subscriber)
        throws DeepSeekApiException {
        try {
            String requestBody = objectMapper.writeValueAsString(request);

            HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + "/chat/completions"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

            // 使用AtomicReference来安全地管理Subscription
            AtomicReference<Flow.Subscription> subscriptionRef = new AtomicReference<>();
            //CompletableFuture<HttpResponse<String>> resp =
            httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofLines())
                .thenAccept(response -> {
                    if (response.statusCode() >= 200 && response.statusCode() < 300) {
                        // 创建安全的Subscriber包装器
                        Flow.Subscriber<String> safeSubscriber = new Flow.Subscriber<>() {
                            @Override
                            public void onSubscribe(Flow.Subscription subscription) {
                                subscriptionRef.set(subscription);
                                subscriber.onSubscribe(subscription);
                                subscription.request(1); // 请求第一个数据项
                            }

                            @Override
                            public void onNext(String line) {
                                try {
                                    if (line.startsWith("data: ") && !line.equals("data: [DONE]")) {
                                        String data = line.substring(6);
                                        ChatCompletionStreamResponse chunk = objectMapper.readValue(data,
                                            ChatCompletionStreamResponse.class);
                                        if (chunk.getChoices() != null && !chunk.getChoices().isEmpty()) {
                                            String content = chunk.getChoices().get(0).getDelta().getContent();
                                            if (content != null) {
                                                subscriber.onNext(content);
                                            }
                                        }
                                    }
                                    // 请求下一个数据项
                                    Flow.Subscription sub = subscriptionRef.get();
                                    if (sub != null) {
                                        sub.request(1);
                                    }
                                } catch (JsonProcessingException e) {
                                    onError(e);
                                }
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                subscriber.onError(throwable);
                            }

                            @Override
                            public void onComplete() {
                                subscriber.onComplete();
                            }
                        };
                        // 处理响应流
                        response.body().forEach(safeSubscriber::onNext);
                        safeSubscriber.onComplete();
                    } else {
                        subscriber.onError(new DeepSeekApiException(
                            "API request failed with status code: " + response.statusCode()));
                    }
                })
                .exceptionally(e -> {
                    subscriber.onError(e);
                    return null;
                });
        } catch (JsonProcessingException e) {
            throw new DeepSeekApiException("Failed to serialize request", e);
        }
    }
}
