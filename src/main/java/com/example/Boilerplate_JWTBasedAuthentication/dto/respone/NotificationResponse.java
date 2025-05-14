package com.example.Boilerplate_JWTBasedAuthentication.dto.respone;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationResponse {
    private int idNotification;
    private String imageLink;
    private String nameCompany;
    private String message;
}
