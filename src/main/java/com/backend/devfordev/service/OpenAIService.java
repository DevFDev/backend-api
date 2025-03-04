package com.backend.devfordev.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OpenAIService {

    @Value("${spring.openai.api-key}")
    private String API_KEY;

    private final RestTemplate restTemplate;

    public OpenAIService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public String generateAIComment(String title, String content) {
        String openaiUrl = "https://api.openai.com/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(API_KEY);

        String prompt = String.format(
                "You are responsible for commenting on community posts for developers. " +
                        "Reply to the community post with the title: \"%s\"\n and the content: \"%s\"\n" +
                        "Answers must be in Korean and should not exceed 300 characters in total.",
                title, content
        );
        // 요청 파라미터 설정 (GPT-3.5 또는 GPT-4 모델 선택)
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");  // 최신 지원 모델
        requestBody.put("messages", List.of(
                Map.of("role", "user", "content", prompt)
        ));
        requestBody.put("max_tokens", 150);  // 응답할 텍스트의 최대 토큰 수
        requestBody.put("temperature", 0.7); // 응답의 창의성 정도 (0.0 ~ 1.0)

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        // API 호출 및 응답 처리
        ResponseEntity<Map> response = restTemplate.exchange(openaiUrl, HttpMethod.POST, entity, Map.class);
        Map<String, Object> responseBody = response.getBody();
        List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");

        if (choices != null && !choices.isEmpty()) {
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            return (String) message.get("content");  // 첫 번째 응답 텍스트 반환
        }

        return "AI 댓글을 생성할 수 없습니다.";  // 오류 처리
    }
}
