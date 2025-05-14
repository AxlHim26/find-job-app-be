package com.example.Boilerplate_JWTBasedAuthentication.service;

import com.example.Boilerplate_JWTBasedAuthentication.dto.request.ApplyJobRequest;
import com.example.Boilerplate_JWTBasedAuthentication.dto.request.JobPostRequest;
import com.example.Boilerplate_JWTBasedAuthentication.dto.request.SaveJobRequest;
import com.example.Boilerplate_JWTBasedAuthentication.dto.respone.*;
import com.example.Boilerplate_JWTBasedAuthentication.entity.*;
import com.example.Boilerplate_JWTBasedAuthentication.repository.ApplicationRepository;
import com.example.Boilerplate_JWTBasedAuthentication.repository.EmployeeRepository;
import com.example.Boilerplate_JWTBasedAuthentication.repository.JobPostRepository;
import com.example.Boilerplate_JWTBasedAuthentication.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
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
    private final ApplicationRepository applicationRepository;
    private final JavaMailSenderImpl mailSender;
    private final EmailService emailService;

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
                jobPostRequest.getRequirement(),
                jobPostRequest.getPosition(),
                jobPostRequest.getQualification(),
                jobPostRequest.getExperience(),
                jobPostRequest.getType(),
                jobPostRequest.getWorkplaceType(),
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
                            jobPost.getId(),
                            jobPost.getTitle(),
                            jobPost.getRecruiter().getAvatarLink(),
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
                            recruiter.getAvatarLink()
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
            String avatar = (recruiter.getAvatarLink() == null || recruiter.getAvatarLink().isEmpty())
                    ? "unknown"
                    : recruiter.getAvatarLink();

            listJobResponses.add(
                    new ListJobResponse(
                            jobPost.getId(),
                            jobPost.getTitle(),
                            jobPost.getRecruiter().getAvatarLink(),
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
                            avatar
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
                .imageUrl(jobPost.getRecruiter().getAvatarLink()) // Default avatar
                .jobTitle(jobPost.getTitle())
                .companyName(u.getName())
                    .createdAt(jobPost.getCreatedAt())
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
                        .createdAt(jobPost.getCreatedAt())
                        .jobPosition(jobPost.getPosition())
                        .salary(jobPost.getSalary())
                        .build()
        ).toList();

        return result;
    }

    @Transactional
    public JobDetailResponse getJobDetail(int id) {
        JobPost jobPost = jobPostRepository.findJobPostsById(id);
        Recruiter recruiter = jobPost.getRecruiter();
        User user = recruiter.getUser();

        String avatarLink = recruiter.getAvatarLink();
        String location = recruiter.getLocation();

        return new JobDetailResponse(
                (avatarLink == null) ? "unknown" : avatarLink,
                user.getName(),
                user.getEmail(),
                jobPost.getTitle(),
                (location == null) ? "unknown" : location,
                jobPost.getDescription(),
                jobPost.getRequirement(),
                jobPost.getPosition(),
                jobPost.getQualification(),
                jobPost.getExperience(),
                jobPost.getType(),
                jobPost.getSalary()
        );
    }


    @Transactional
    public void applyFor(ApplyJobRequest request, String username) {
        boolean isUpdateCV = false;

        User user = userRepository.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );

        Employee employee = user.getEmployee();
        JobPost jobPost = jobPostRepository.findById(request.getJobId()).orElseThrow(
                () -> new RuntimeException("Job Post with ID " + request.getJobId() + " not found")
        );

        // Nếu đã có application trước đó thì cập nhật CV link
        Application application = applicationRepository.findByEmployeeAndJobPost(employee, jobPost);

        if (application != null) {
            application.setCvLink(request.getCvLink());
            applicationRepository.save(application);
            isUpdateCV = true;
        } else {
            // Tạo application mới nếu chưa có
            Application newApplication = new Application();
            newApplication.setEmployee(employee);
            newApplication.setJobPost(jobPost);
            newApplication.setCvLink(request.getCvLink());
            applicationRepository.save(newApplication);
            isUpdateCV = false;
        }

        // gửi mail thông báo
        emailService.sendMailNotificationTo(username, jobPost.getTitle(), jobPost.getRecruiter().getUser().getName(), isUpdateCV, request.getCvLink());
    }



    public List<JobSearchResponse> searchWith(Filter filter) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );
        Employee employee = user.getEmployee();
        List<Integer> savedJobs = employee.getJobPosts().stream().map(JobPost::getId).toList();

        List<JobPost> filteredJobs = jobPostRepository.searchJobs(
                filter.getTitle(),
                filter.getLocation(),
                filter.getPosition(),
                filter.getExperience(),
                filter.getSalary()
        );

        return filteredJobs.stream()
                .map(job -> JobSearchResponse.builder()
                        .id(job.getId())
                        .imageUrl(job.getRecruiter().getAvatarLink())
                        .jobTitle(job.getTitle())
                        .companyName(job.getRecruiter().getUser().getName())
                        .location(job.getRecruiter().getLocation())
                        .jobPosition(job.getPosition())
                        .jobType(job.getType())
                        .salary(job.getSalary())
                        .createdAt(job.getCreatedAt())
                        .isSaved(savedJobs.contains(job.getId()))
                        .build())
                .toList();
    }
}
