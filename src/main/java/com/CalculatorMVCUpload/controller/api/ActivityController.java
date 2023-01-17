package com.CalculatorMVCUpload.controller.api;

import com.CalculatorMVCUpload.entity.ActivityEntity;
import com.CalculatorMVCUpload.entity.CalculatorActivityEntity;
import com.CalculatorMVCUpload.entity.users.UserEntity;
import com.CalculatorMVCUpload.payload.request.CalcActivityFilterRequest;
import com.CalculatorMVCUpload.payload.request.CalcActivitySaveRequest;
import com.CalculatorMVCUpload.service.activity.ActivityService;
import com.CalculatorMVCUpload.service.activity.CalculatorActivityService;
import com.CalculatorMVCUpload.service.users.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
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


}
