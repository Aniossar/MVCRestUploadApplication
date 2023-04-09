package com.CalculatorMVCUpload.controller.api;

import com.CalculatorMVCUpload.entity.ActivityEntity;
import com.CalculatorMVCUpload.payload.response.ActivityResponse;
import com.CalculatorMVCUpload.service.activity.ActivityService;
import com.CalculatorMVCUpload.service.users.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@Log
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private UserService userService;

    @CrossOrigin
    @GetMapping("/allActivities")
    public List<ActivityResponse> getAllActivities() {
        List<ActivityEntity> allActivities = activityService.getAllActivities();
        List<ActivityResponse> allActivitiesResponse = new ArrayList<>();
        allActivities.forEach(activityEntity -> {
            ActivityResponse response = new ActivityResponse();
            response.setActivityType(activityEntity.getActivityType());
            response.setActivityTime(activityEntity.getActivityTime());
            response.setActivityMessage(activityEntity.getActivityMessage());
            response.setUserId(activityEntity.getUserId());
            response.setUserLogin(userService.findById(response.getUserId()).getLogin());
            allActivitiesResponse.add(response);
        });
        return allActivitiesResponse;
    }


}
