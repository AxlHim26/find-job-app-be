package com.example.Boilerplate_JWTBasedAuthentication.controller;

import com.example.Boilerplate_JWTBasedAuthentication.dto.common.RestResponse;
import com.example.Boilerplate_JWTBasedAuthentication.dto.request.JobPostRequest;
import com.example.Boilerplate_JWTBasedAuthentication.dto.respone.ListJobResponse;
import com.example.Boilerplate_JWTBasedAuthentication.service.JobPostService;
import lombok.AllArgsConstructor;
import org.hibernate.dialect.temptable.TemporaryTableSessionUidColumn;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/jobpost")
@AllArgsConstructor
public class JobPostController {
    private final JobPostService jobPostService;
    @PostMapping("/create")
    public ResponseEntity<RestResponse<Void>> createJobPost(@RequestBody JobPostRequest jobPostRequest) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        jobPostService.creatJopPost(jobPostRequest, email);
        return ResponseEntity.ok().body(
                RestResponse.success("Create job post success!")
        );
    }
    @GetMapping("/list")
    public ResponseEntity<RestResponse<List<ListJobResponse>>> getListJobPost() throws Exception{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        List<ListJobResponse> list = jobPostService.getJobPosts(email);

        return ResponseEntity.status(HttpStatus.OK).body(
                RestResponse.success(
                        list,
                        "get success"
                        )
        );
    }
    @GetMapping("/list-recent")
    public ResponseEntity<RestResponse<List<ListJobResponse>>> getListJobPostRecent() throws Exception{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        List<ListJobResponse> list = jobPostService.getJobPostsRecent(email);

        return ResponseEntity.status(HttpStatus.OK).body(
                RestResponse.success(
                    list,
                        "get list success!"
                )
        );
    }
}
