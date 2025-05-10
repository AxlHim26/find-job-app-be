package com.example.Boilerplate_JWTBasedAuthentication.controller;

import com.example.Boilerplate_JWTBasedAuthentication.dto.common.RestResponse;
import com.example.Boilerplate_JWTBasedAuthentication.dto.request.RecruiterProfileRequest;
import com.example.Boilerplate_JWTBasedAuthentication.service.JobPostService;
import com.example.Boilerplate_JWTBasedAuthentication.service.RecruiterService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/recruiter")
@AllArgsConstructor
public class RecruiterController {
    private final RecruiterService recruiterService;
    @PostMapping("update/profile")
    public ResponseEntity<RestResponse<Void>> updateRecruiterProfile(@RequestBody RecruiterProfileRequest recruiterProfileRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        recruiterService.updateProfileRecruiter(email, recruiterProfileRequest);
        return ResponseEntity.ok().body(RestResponse.success("update success!"));
    }
    @GetMapping("get/profile")
    public ResponseEntity<RestResponse<RecruiterProfileRequest>> getRecuiterProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return ResponseEntity.ok().body(
                RestResponse.success(
                        recruiterService.getProfile(email),
                        "get profile success!"
                )
        );
    }
}
