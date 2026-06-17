package com.example.GasTuanDat.chatbot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatbotService {

    private final RestTemplate restTemplate;

    // Sử dụng biến tĩnh hoặc có thể cấu hình trong application.yml
    private final String chatbotUrl = "http://localhost:8090/chat";
    private final String apiKey = "gastuandat_secret_key_2026"; // Nên đưa vào properties/yml ở môi trường thật

    public ChatbotResponse chat(ChatbotRequest request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-api-key", apiKey);

            HttpEntity<ChatbotRequest> entity = new HttpEntity<>(request, headers);

            log.info("Sending request to Chatbot RAG for session: {}", request.getSessionId());
            ResponseEntity<ChatbotResponse> response = restTemplate.postForEntity(chatbotUrl, entity, ChatbotResponse.class);

            return response.getBody();
        } catch (Exception e) {
            log.error("Error communicating with Chatbot RAG service: {}", e.getMessage());
            return ChatbotResponse.builder()
                    .intent("ERROR")
                    .response("Hệ thống trợ lý ảo tạm thời không khả dụng. Vui lòng thử lại sau.")
                    .sql(null)
                    .build();
        }
    }
}
