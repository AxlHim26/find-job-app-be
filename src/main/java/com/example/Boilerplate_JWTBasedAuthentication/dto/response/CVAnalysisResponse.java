package com.example.Boilerplate_JWTBasedAuthentication.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CVAnalysisResponse {
    private String analysis;

    public String getMarkdownContent() {
        // Tìm vị trí bắt đầu của phần markdown (sau thẻ </think>)
        int thinkEndIndex = analysis.indexOf("</think>");
        if (thinkEndIndex == -1) {
            return analysis;
        }

        // Lấy phần markdown sau thẻ </think>
        String markdownContent = analysis.substring(thinkEndIndex + 8).trim();
        
        // Loại bỏ các ký tự xuống dòng thừa ở đầu
        return markdownContent.replaceFirst("^\\s*\n+", "");
    }
} 