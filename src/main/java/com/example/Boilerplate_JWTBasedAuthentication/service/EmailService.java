package com.example.Boilerplate_JWTBasedAuthentication.service;

import com.example.Boilerplate_JWTBasedAuthentication.entity.JobPost;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class EmailService {

    private final JavaMailSenderImpl mailSender;

    public EmailService(JavaMailSenderImpl mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendMailNotificationTo(String username, String jobTitle, String recruiterName, boolean isUpdateCV, String cvLink) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(username);
            if (isUpdateCV) {
                mail.setSubject("Updated your CV in - " + jobTitle + " of company " + recruiterName);
                mail.setText("Your cv of this post is updated successfully, you can see your final CV here : " + cvLink);
            } else {
                mail.setSubject("Applied your CV in - " + jobTitle + " of company " + recruiterName);
                mail.setText("Your cv of this post is apply successfully, you can see your final CV here : " + cvLink);
            }

            mailSender.send(mail);
        } catch (Exception e) {
            log.error("Exception occurred while sending email", e);
            throw e;
        }
    }

    @Async
    @Transactional
    public void sendMailRejectNotification(String username, String jobTitle, String companyName) throws Exception {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(username);
            mail.setSubject("Your Application for " + jobTitle + " at " + companyName);

            String mailContent = "Dear " + username + ",\n\n"
                    + "Thank you for your interest in the " + jobTitle + " position at " + companyName + ", "
                    + "and for the time you took to apply.\n\n"
                    + "After careful consideration, we regret to inform you that we have decided to move forward with another candidate for this position. "
                    + "This decision was not easy due to the high quality of applicants like yourself.\n\n"
                    + "We truly appreciate your effort and the opportunity to learn more about your background and experience. "
                    + "We will keep your information on file should another opportunity arise that matches your profile.\n\n"
                    + "We wish you all the best in your job search and future career.\n\n"
                    + "Kind regards,\n"
                    + companyName;

            mail.setText(mailContent);
            mailSender.send(mail);

        } catch (Exception e) {
            throw new Exception("Exception occurred while sending email", e);
        }
    }

    @Async
    @Transactional
    public void sendMailAcceptNotification(String username, String jobTitle, String companyName, String linkCV) throws Exception {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(username);
            mail.setSubject("Your Application for " + jobTitle + " at " + companyName);

            String mailContent = "Dear " + username + ",\n\n"
                    + "Congratulations! We are pleased to inform you that you have been selected for the " + jobTitle + " position at " + companyName + ".\n\n"
                    + "We were very impressed with your qualifications and background, and we believe you will be a valuable addition to our team.\n\n"
                    + "Please take a moment to review your application and next steps here: " + linkCV + "\n\n"
                    + "Our team will reach out to you shortly with further details regarding the onboarding process.\n\n"
                    + "We look forward to working with you.\n\n"
                    + "Best regards,\n"
                    + companyName;

            mail.setText(mailContent);
            mailSender.send(mail);

        } catch (Exception e) {
            throw new Exception("Exception occurred while sending acceptance email", e);
        }
    }

}
