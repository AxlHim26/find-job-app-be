package com.example.Boilerplate_JWTBasedAuthentication.service;

import com.example.Boilerplate_JWTBasedAuthentication.dto.request.JobPostRequest;
import com.example.Boilerplate_JWTBasedAuthentication.dto.respone.ListJobResponse;
import com.example.Boilerplate_JWTBasedAuthentication.entity.JobPost;
import com.example.Boilerplate_JWTBasedAuthentication.entity.Recruiter;
import com.example.Boilerplate_JWTBasedAuthentication.entity.User;
import com.example.Boilerplate_JWTBasedAuthentication.repository.JobPostRepository;
import com.example.Boilerplate_JWTBasedAuthentication.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobPostService {
    private final UserRepository userRepository;
    private final JobPostRepository jopPostRepository;
    @Transactional
    public void creatJopPost(JobPostRequest jobPostRequest, String mail) throws Exception{
        User user = userRepository.findByEmail(mail).orElseThrow(
         () -> new UsernameNotFoundException("mail not found")
        );
        Recruiter recruiter = user.getRecruiter();
        JobPost jobPost = new JobPost(
                recruiter,
                jobPostRequest.getTitle(),
                jobPostRequest.getDescription(),
                jobPostRequest.getPosition(),
                jobPostRequest.getQualification(),
                jobPostRequest.getExperience(),
                jobPostRequest.getType(),
                jobPostRequest.getSalary(),
                jobPostRequest.getExpirateAt()
        );
        jopPostRepository.save(jobPost);
    }

    @Transactional
    public List<ListJobResponse> getJobPosts(String email){
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("email not found")
        );
        List<ListJobResponse> listJobResponses = new ArrayList<>();
        List<JobPost> listJobPost = user.getRecruiter().getJobPosts();
        Recruiter recruiter = user.getRecruiter();
        for (JobPost jobPost : listJobPost) {
            listJobResponses.add(
                    new ListJobResponse(
                            jobPost.getTitle(),
                            jobPost.getDescription(),
                            jobPost.getPosition(),
                            jobPost.getQualification(),
                            jobPost.getExperience(),
                            jobPost.getType(),
                            jobPost.getSalary(),
                            jobPost.getExpirateAt(),
                            jobPost.getCreatedAt(),
                            user.getName(),
                            recruiter.getLocation(),
                            "avatar"
                    )
            );
        }
        return listJobResponses;
    }

    @Transactional
    public List<ListJobResponse> getJobPostsRecent(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("email not found")
        );

        List<ListJobResponse> listJobResponses = new ArrayList<>();
        List<JobPost> listJobPost = user.getRecruiter().getJobPosts();
        Recruiter recruiter = user.getRecruiter();

        listJobPost.sort((job1, job2) -> job2.getCreatedAt().compareTo(job1.getCreatedAt()));

        int count = Math.min(5, listJobPost.size());
        for (int i = 0; i < count; i++) {
            JobPost jobPost = listJobPost.get(i);
            listJobResponses.add(
                    new ListJobResponse(
                            jobPost.getTitle(),
                            jobPost.getDescription(),
                            jobPost.getPosition(),
                            jobPost.getQualification(),
                            jobPost.getExperience(),
                            jobPost.getType(),
                            jobPost.getSalary(),
                            jobPost.getExpirateAt(),
                            jobPost.getCreatedAt(),
                            user.getName(),
                            recruiter.getLocation(),
                            "avatar"
                    )
            );
        }

        return listJobResponses;
    }
}
