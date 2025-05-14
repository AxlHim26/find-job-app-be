package com.example.Boilerplate_JWTBasedAuthentication.controller;

import com.example.Boilerplate_JWTBasedAuthentication.dto.common.RestResponse;
import com.example.Boilerplate_JWTBasedAuthentication.dto.respone.NotificationResponse;
import com.example.Boilerplate_JWTBasedAuthentication.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/notification")
@AllArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/accept")
    public ResponseEntity<RestResponse<Void>> acceptCV(@RequestParam int id) throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        notificationService.acceptCvNotification(id, username);
        return ResponseEntity.ok().body(
                RestResponse.success("Accept success")
        );
    }

    @PostMapping("/reject")
    public ResponseEntity<RestResponse<Void>> rejectCV(@RequestParam int id) throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        notificationService.rejectCvNotification(id, username);
        return ResponseEntity.ok().body(
                RestResponse.success("Reject success")
        );
    }

    @GetMapping("/get")
    public ResponseEntity<RestResponse<List<NotificationResponse>>> getNotifications(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok().body(
                RestResponse.success(
                        notificationService.getNotifications(username),
                        "get success"
                )
        );
    }

    @PostMapping("/delete")
    private ResponseEntity<RestResponse<Void>> deleteNotification(@RequestParam int id){
        notificationService.deleteNotification(id);
        return ResponseEntity.ok().body(
                RestResponse.success(
                        "delete success"
                )
        );
    }
}
