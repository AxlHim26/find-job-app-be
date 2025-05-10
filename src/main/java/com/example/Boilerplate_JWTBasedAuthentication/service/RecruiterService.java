package com.example.Boilerplate_JWTBasedAuthentication.service;

import com.example.Boilerplate_JWTBasedAuthentication.dto.request.RecruiterProfileRequest;
import com.example.Boilerplate_JWTBasedAuthentication.entity.Recruiter;
import com.example.Boilerplate_JWTBasedAuthentication.entity.User;
import com.example.Boilerplate_JWTBasedAuthentication.repository.RecruiterRepository;
import com.example.Boilerplate_JWTBasedAuthentication.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
}
