package com.CalculatorMVCUpload.controller.api;

import com.CalculatorMVCUpload.entity.ActivityEntity;
import com.CalculatorMVCUpload.payload.request.SingleMessageRequest;
import com.CalculatorMVCUpload.service.ActivityService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api")
@Log
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @GetMapping("/allActivities")
    public List<ActivityEntity> getAllActivities() {
        return activityService.getAllActivities();
    }

    @PostMapping("/saveCalculatorActivity")
    private String saveExternalActivity(@RequestBody SingleMessageRequest payload){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        try{
            String decodedActivity = new String(Base64.getDecoder().decode(payload.getMessage()));
            ActivityEntity activityEntity = new ActivityEntity(Instant.now(), "Calculator", userName,
                    decodedActivity);
            activityService.saveActivity(activityEntity);
            return "OK";
        } catch (Exception e){
            log.warning("Failed to deliver external activity from " + userName);
            return "FAIL";
        }
    }
}
