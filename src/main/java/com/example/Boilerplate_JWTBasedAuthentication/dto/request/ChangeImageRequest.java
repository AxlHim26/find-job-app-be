package com.example.Boilerplate_JWTBasedAuthentication.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeImageRequest {
    private String imageUrl;
}
