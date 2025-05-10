package com.example.Boilerplate_JWTBasedAuthentication.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NovitaAIRequest {
    private String model;
    private List<Message> messages;
    private Integer max_tokens;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Message {
        private String role;
        private String content;
    }
} 