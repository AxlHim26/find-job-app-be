package com.example.Boilerplate_JWTBasedAuthentication.dto.respone;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class ListJobResponse {
    private int id;
    private String title;
    private String imageUrl;
    private String description;
    private String position;
    private String qualification;
    private String experience;
    private String type;
    private String salary;
    private Date expirateAt;
    private Date createdAt;
    private String nameCompany;
    private String location;
    private String avatar;
}
