package com.CalculatorMVCUpload.controller;

import com.CalculatorMVCUpload.entity.ActivityEntity;
import com.CalculatorMVCUpload.entity.UploadedFile;
import com.CalculatorMVCUpload.payload.AuthentificationRequest;
import com.CalculatorMVCUpload.payload.ExternalActivityPayload;
import com.CalculatorMVCUpload.service.ActivityService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
@Log
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @GetMapping("/api/allActivities")
    public List<ActivityEntity> getAllActivities() {
        return activityService.getAllActivities();
    }

    @PostMapping("/api/saveCalculatorActivity")
    private String saveExternalActivity(@RequestBody ExternalActivityPayload payload){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        try{
            ActivityEntity activityEntity = new ActivityEntity(Instant.now(), "Calculator", userName,
                    payload.getActivityMessage());
            activityService.saveActivity(activityEntity);
            return "OK";
        } catch (Exception e){
            log.warning("Failed to deliver external activity from " + userName);
            return "FAIL";
        }
    }
}
