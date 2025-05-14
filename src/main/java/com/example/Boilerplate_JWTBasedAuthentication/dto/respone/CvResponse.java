package com.example.Boilerplate_JWTBasedAuthentication.dto.respone;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CvResponse {
    private int idCV;
    private String nameEmployee;
    private String location;
    private String imageLink;
    private String cvLink;
}
