package com.example.Boilerplate_JWTBasedAuthentication.service;

import com.example.Boilerplate_JWTBasedAuthentication.dto.respone.NotificationResponse;
import com.example.Boilerplate_JWTBasedAuthentication.entity.*;
import com.example.Boilerplate_JWTBasedAuthentication.repository.*;
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
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final EmailService emailService;
    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    @Transactional
    public void rejectCvNotification(int idCV, String emailRecruiter) throws Exception {
        Application application = applicationRepository.findById(idCV).orElseThrow(
                () -> new UsernameNotFoundException("id not found")
        );
        Employee employee = application.getEmployee();
        Recruiter recruiter = userRepository.findByEmail(emailRecruiter).orElseThrow(
                () -> new UsernameNotFoundException("email not found")
        ).getRecruiter();
        String username = employee.getUser().getUsername();
        applicationRepository.delete(application);
        Notification notification = new Notification();
        notification.setMessage("We appreciate your application, but we've decided to move forward with other candidates.");
        notification.setEmployee(employee);
        notification.setRecruiter(recruiter);
        notificationRepository.save(notification);
        emailService.sendMailRejectNotification(username, application.getJobPost().getTitle(), recruiter.getUser().getName());
    }

    @Transactional
    public void acceptCvNotification(int idCV, String emailRecruiter) throws Exception {
        Application application = applicationRepository.findById(idCV).orElseThrow(
                () -> new UsernameNotFoundException("id not found")
        );
        Employee employee = application.getEmployee();
        Recruiter recruiter = userRepository.findByEmail(emailRecruiter).orElseThrow(
                () -> new UsernameNotFoundException("email not found")
        ).getRecruiter();
        String username = employee.getUser().getUsername();
        Notification notification = new Notification();
        notification.setMessage("Your application was not successful. Please check your email for more details. Thank you for your interest!");
        notification.setEmployee(employee);
        notification.setRecruiter(recruiter);
        notificationRepository.save(notification);
        emailService.sendMailAcceptNotification(username,
                application.getJobPost().getTitle(),
                recruiter.getUser().getName(),
                application.getCvLink());
    }

    @Transactional
    public List<NotificationResponse> getNotifications(String email){
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("email not found")
        );
        List<Notification> listNotifications = user.getEmployee().getNotifications();
        List<NotificationResponse> list = new ArrayList<>();
        for (Notification notification : listNotifications) {
            Recruiter recruiter = notification.getRecruiter();
            list.add(
                    new NotificationResponse(
                            notification.getId(),
                            (recruiter.getAvatarLink() == null) ? "unknown": recruiter.getAvatarLink(),
                            recruiter.getUser().getName(),
                            notification.getMessage()
                    )
            );
        }

        return list;
    }

    @Transactional
    public void deleteNotification(int idNoti){
        Notification notification = notificationRepository.findById((long) idNoti).orElseThrow(
                () -> new UsernameNotFoundException("id not found")
        );
        notificationRepository.delete(notification);
    }
}
