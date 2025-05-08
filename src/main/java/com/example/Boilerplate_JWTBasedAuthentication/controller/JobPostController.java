package com.example.Boilerplate_JWTBasedAuthentication.controller;

import com.example.Boilerplate_JWTBasedAuthentication.dto.request.JobPostRequest;
import com.example.Boilerplate_JWTBasedAuthentication.service.JobPostService;
import lombok.AllArgsConstructor;
import org.hibernate.dialect.temptable.TemporaryTableSessionUidColumn;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/jobpost")
@AllArgsConstructor
public class JobPostController {
    private final JobPostService jobPostService;
    @PostMapping("/create")
    public ResponseEntity<String> createJobPost(@RequestBody JobPostRequest jobPostRequest) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        jobPostService.creatJopPost(jobPostRequest, email);
        return ResponseEntity.ok("Create job post success!");
    }
}
