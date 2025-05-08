package com.example.Boilerplate_JWTBasedAuthentication.service;

import com.example.Boilerplate_JWTBasedAuthentication.dto.request.JobPostRequest;
import com.example.Boilerplate_JWTBasedAuthentication.entity.JobPost;
import com.example.Boilerplate_JWTBasedAuthentication.entity.User;
import com.example.Boilerplate_JWTBasedAuthentication.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobPostService {
    private final UserRepository userRepository;
    @Transactional
    public void creatJopPost(JobPostRequest jobPostRequest, String mail) throws Exception{
        User user = userRepository.findByEmail(mail).orElseThrow(
         () -> new UsernameNotFoundException("mail not found")
        );
        List<JobPost> list =user.getRecruiter().getJobPosts();
        list.add(
                new JobPost(
                        jobPostRequest.getTitle(),
                        jobPostRequest.getDescription(),
                        jobPostRequest.getPosition(),
                        jobPostRequest.getQualification(),
                        jobPostRequest.getExperience(),
                        jobPostRequest.getType(),
                        jobPostRequest.getSalary()
                )
        );
        userRepository.save(user);
    }
}
