package com.example.Boilerplate_JWTBasedAuthentication.service;

import com.example.Boilerplate_JWTBasedAuthentication.dto.request.JobIntroDTO;
import com.example.Boilerplate_JWTBasedAuthentication.dto.request.RecruiterProfileRequest;
import com.example.Boilerplate_JWTBasedAuthentication.dto.respone.NewestJobResponse;
import com.example.Boilerplate_JWTBasedAuthentication.dto.respone.RecruiterInfoResponse;
import com.example.Boilerplate_JWTBasedAuthentication.entity.Employee;
import com.example.Boilerplate_JWTBasedAuthentication.entity.JobPost;
import com.example.Boilerplate_JWTBasedAuthentication.entity.Recruiter;
import com.example.Boilerplate_JWTBasedAuthentication.entity.User;
import com.example.Boilerplate_JWTBasedAuthentication.repository.RecruiterRepository;
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
public class RecruiterService {
    private final UserRepository userRepository;
    private final RecruiterRepository recruiterRepository;

    @Transactional
    public void updateProfileRecruiter(String email, RecruiterProfileRequest recruiterProfileRequest){
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("email not found")
        );
        Recruiter recruiter = user.getRecruiter();
        recruiter.setAbout(recruiterProfileRequest.getAbout());
        recruiter.setWebsite(recruiterProfileRequest.getWebsite());
        recruiter.setIndustry(recruiterProfileRequest.getIndustry());
        recruiter.setLocation(recruiterProfileRequest.getLocation());
        recruiter.setSince(recruiterProfileRequest.getSince());
        recruiter.setSpecialization(recruiterProfileRequest.getSpecialization());

        recruiterRepository.save(recruiter);
    }

    @Transactional
    public RecruiterProfileRequest getProfile(String email){
        User user = userRepository.findByEmail(email).orElseThrow(
                ()-> new UsernameNotFoundException("email not found")
        );
        Recruiter recruiter = user.getRecruiter();

        return new RecruiterProfileRequest(
                recruiter.getAbout(),
                recruiter.getWebsite(),
                recruiter.getIndustry(),
                recruiter.getLocation(),
                recruiter.getSince(),
                recruiter.getSpecialization()
        );
    }

    @Transactional
    public RecruiterInfoResponse getRecruiterInfor(String email, String emailEmployee){
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("email not found")
        );
        Recruiter recruiter = user.getRecruiter();

        List<JobIntroDTO> list = new ArrayList<>();

        Employee employee = userRepository.findByEmail(emailEmployee).orElseThrow(
                () -> new UsernameNotFoundException("email not found")
        ).getEmployee();
        // Lấy danh sách bài đăng đã lưu của employee
        List<Integer> savedJob = employee.getJobPosts().stream().map(JobPost::getId).toList();

        for (JobPost jobPost : recruiter.getJobPosts()) {
            list.add(
                    new JobIntroDTO(
                            jobPost.getId(),
                            jobPost.getTitle(),
                            jobPost.getSalary(),
                            jobPost.getPosition(),
                            jobPost.getType(),
                            jobPost.getCreatedAt(),
                            savedJob.contains(jobPost.getId())
                    )
            );
        }

        return new RecruiterInfoResponse(
                recruiter.getAvatarLink(),
                user.getName(),
                recruiter.getLocation(),
                recruiter.getAbout(),
                recruiter.getWebsite(),
                recruiter.getIndustry(),
                recruiter.getSince(),
                list
        );
    }
}
