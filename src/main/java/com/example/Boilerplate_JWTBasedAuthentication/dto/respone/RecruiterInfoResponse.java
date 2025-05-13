package com.example.Boilerplate_JWTBasedAuthentication.dto.respone;

import com.example.Boilerplate_JWTBasedAuthentication.dto.request.JobIntroDTO;
import com.example.Boilerplate_JWTBasedAuthentication.entity.JobPost;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class RecruiterInfoResponse {
    private String imageLink;
    private String recruiterName;
    private String location;
    private String about;
    private String website;
    private String industry;
    private String since;
    private List<JobIntroDTO> jobIntroDTOs;
}
