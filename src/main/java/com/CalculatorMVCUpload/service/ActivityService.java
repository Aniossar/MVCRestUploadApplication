/*
package com.CalculatorMVCUpload.service;

import com.CalculatorMVCUpload.entity.ActivityEntity;
import com.CalculatorMVCUpload.entity.CalculatorActivityEntity;
import com.CalculatorMVCUpload.payload.request.ActivityFilterRequest;
import com.CalculatorMVCUpload.payload.response.ActivityFilterResponse;
import com.CalculatorMVCUpload.repository.ActivityEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivityService {

    @Autowired
    private ActivityEntityRepository activityEntityRepository;

    @Transactional
    public List<ActivityFilterResponse> performActivityFilter(ActivityFilterRequest request) {
        List<ActivityEntity> calculatorActivities = activityEntityRepository.findByActivityType("Calculator");
        Map<ActivityEntity, CalculatorActivityEntity> activityCalcMessageMap = new HashMap<>();
        if(calculatorActivities != null){
            calculatorActivities.stream().map(item -> item.getActivityMessage())
        }
        if(request.getAddPriceFrom() != null){
            calculatorActivities.stream().filter()
        }

    }

    @Transactional
    public List<ActivityEntity> getAllActivities() {
        return activityEntityRepository.findAll();
    }

    @Transactional
    public List<ActivityEntity> getCertainUserActivities(String login) {
        return activityEntityRepository.findByLogin(login);
    }

    @Transactional
    public void saveActivity(ActivityEntity activityEntity) {
        activityEntityRepository.save(activityEntity);
    }

    @Transactional
    public void deleteActivitiesForUser(String login) {
        activityEntityRepository.deleteActivityEntitiesByLogin(login);
    }

}
*/
