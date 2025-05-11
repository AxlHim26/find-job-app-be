package com.example.Boilerplate_JWTBasedAuthentication.service;

import com.example.Boilerplate_JWTBasedAuthentication.dto.request.NovitaAIRequest;
import com.example.Boilerplate_JWTBasedAuthentication.dto.response.NovitaAIResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NovitaAIService {
    private static final String NOVITA_API_URL = "https://api.novita.ai/v3/openai/chat/completions";
    private final RestTemplate restTemplate;

    @Value("${novita.api.key}")
    private String apiKey;

    public String analyzeCV(String cvText) {
        try {
            // Tạo headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            // Tạo messages
            List<NovitaAIRequest.Message> messages = new ArrayList<>();
            
            // System message
            messages.add(NovitaAIRequest.Message.builder()
                    .role("system")
                    .content("Hành động như thể bạn là một nhà tuyển dụng ngành IT chuyên nghiệp, và cho các lời khuyên để " +
                            "cải thiện lại CV của họ, hãy trả lời bằng tiếng việt")
                    .build());

            // User message
            messages.add(NovitaAIRequest.Message.builder()
                    .role("user")
                    .content("Hãy trả lời bằng tiếng việt, hãy đóng vai là một nhà tuyển dụng nhân sự IT có nhiều năm kinh nghiệm trong việc đọc CV, " +
                            "hãy giúp tôi phân tích và cải thiện cv này dưới dạng văn bản (được trích xuất từ file pdf) " +
                            "và bạn hãy cho lời khuyên để có thể chỉnh sửa lại CV tốt hơn, dưới đây là văn bản: " + cvText)
                    .build());

            // Tạo request body
            NovitaAIRequest requestBody = NovitaAIRequest.builder()
                    .model("deepseek/deepseek-r1-turbo")
                    .messages(messages)
                    .max_tokens(2000)
                    .build();

            // Tạo HttpEntity
            HttpEntity<NovitaAIRequest> request = new HttpEntity<>(requestBody, headers);

            // Gọi API
            NovitaAIResponse response = restTemplate.postForObject(
                    NOVITA_API_URL,
                    request,
                    NovitaAIResponse.class
            );

            if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
                NovitaAIResponse.Choice choice = response.getChoices().get(0);
                if (choice != null && choice.getMessage() != null) {
                    return choice.getMessage().getContent();
                }
            }

            return "Không thể phân tích CV";

        } catch (Exception e) {
            log.error("Error analyzing CV with Novita AI: ", e);
            return "Lỗi khi phân tích CV: " + e.getMessage();
        }
    }
} 