package com.example.Boilerplate_JWTBasedAuthentication.service;

import com.example.Boilerplate_JWTBasedAuthentication.dto.request.JobPostRequest;
import com.example.Boilerplate_JWTBasedAuthentication.dto.request.SaveJobRequest;
import com.example.Boilerplate_JWTBasedAuthentication.dto.respone.ListJobResponse;
import com.example.Boilerplate_JWTBasedAuthentication.dto.respone.NewestJobResponse;
import com.example.Boilerplate_JWTBasedAuthentication.dto.respone.SaveJobStatus;
import com.example.Boilerplate_JWTBasedAuthentication.dto.respone.SavedJob;
import com.example.Boilerplate_JWTBasedAuthentication.entity.Employee;
import com.example.Boilerplate_JWTBasedAuthentication.entity.JobPost;
import com.example.Boilerplate_JWTBasedAuthentication.entity.Recruiter;
import com.example.Boilerplate_JWTBasedAuthentication.entity.User;
import com.example.Boilerplate_JWTBasedAuthentication.repository.EmployeeRepository;
import com.example.Boilerplate_JWTBasedAuthentication.repository.JobPostRepository;
import com.example.Boilerplate_JWTBasedAuthentication.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final JobPostRepository jobPostRepository;
    private final EmployeeRepository employeeRepository;

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

    public List<NewestJobResponse> getNewestJob() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );

        Employee employee = user.getEmployee();
        // Lấy danh sách bài đăng đã lưu của employee
        List<Integer> savedJob = employee.getJobPosts().stream().map(JobPost::getId)
                .toList();

        List<JobPost> newestJobs = jopPostRepository.findTop5ByOrderByCreatedAtDesc();
        List<NewestJobResponse> jobItems = new ArrayList<>();
        
        for (JobPost jobPost : newestJobs) {
            Recruiter recruiter = jobPost.getRecruiter();
            User u = recruiter.getUser();
            
            jobItems.add(NewestJobResponse.builder()
                .id(jobPost.getId())
                .imageUrl("avatar") // Default avatar
                .jobTitle(jobPost.getTitle())
                .companyName(u.getName())
                .location(recruiter.getLocation())
                .jobPosition(jobPost.getPosition())
                .jobType(jobPost.getType())
                .salary(jobPost.getSalary())
                .isSaved(savedJob.contains(jobPost.getId())) // Default value
                .build());
        }
        
        return jobItems;
    }

    public SaveJobStatus saveJob(String email, SaveJobRequest request) {
        boolean isJobSaved = true;

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );

        Employee employee = user.getEmployee();
        JobPost jobPost = jobPostRepository.findById(request.getJobId()).orElseThrow(
                () -> new UsernameNotFoundException("Job Post Not Found")
        );

        if (employee.getJobPosts().contains(jobPost)) {
            employee.getJobPosts().remove(jobPost);
            isJobSaved = false;
        } else {
            employee.getJobPosts().add(jobPost);
        }

        employeeRepository.save(employee);

        return SaveJobStatus.builder().isJobSaved(isJobSaved).build();
    }

    public List<SavedJob> getSavedJob(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );

        List<JobPost> listJobs = user.getEmployee().getJobPosts();

        List<SavedJob> result = listJobs.stream().map(
                jobPost -> SavedJob.builder()
                        .id(jobPost.getId())
                        .imageUrl(jobPost.getRecruiter().getAvatarLink())
                        .jobTitle(jobPost.getTitle())
                        .companyName(jobPost.getRecruiter().getUser().getName())
                        .location(jobPost.getPosition())
                        .jobType(jobPost.getType())
                        .salary(jobPost.getSalary())
                        .build()
        ).toList();

        return result;
    }
}
