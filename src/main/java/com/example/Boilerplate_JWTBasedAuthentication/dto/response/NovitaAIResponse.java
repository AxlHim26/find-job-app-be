package com.example.Boilerplate_JWTBasedAuthentication.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NovitaAIResponse {
    private String id;
    private String object;
    private Long created;
    private String model;
    private List<Choice> choices;
    private Usage usage;
    private String system_fingerprint;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Choice {
        private Integer index;
        private Message message;
        private String finish_reason;
        private ContentFilterResults content_filter_results;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Message {
        private String role;
        private String content;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContentFilterResults {
        private FilterResult hate;
        private FilterResult self_harm;
        private FilterResult sexual;
        private FilterResult violence;
        private FilterResult jailbreak;
        private FilterResult profanity;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FilterResult {
        private Boolean filtered;
        private Boolean detected;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Usage {
        private Integer prompt_tokens;
        private Integer completion_tokens;
        private Integer total_tokens;
        private Object prompt_tokens_details;
        private Object completion_tokens_details;
    }
} 