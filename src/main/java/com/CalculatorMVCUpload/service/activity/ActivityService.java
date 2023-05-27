package com.CalculatorMVCUpload.service.activity;

import com.CalculatorMVCUpload.entity.ActivityEntity;
import com.CalculatorMVCUpload.repository.ActivityEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ActivityService {

    @Autowired
    private ActivityEntityRepository activityEntityRepository;

    @Transactional
    public List<ActivityEntity> getAllActivities() {
        return activityEntityRepository.findAll();
    }

    @Transactional
    public List<ActivityEntity> getCertainUserActivities(int userId) {
        return activityEntityRepository.findByUserId(userId);
    }

    @Transactional
    public void saveActivity(ActivityEntity activityEntity) {
        activityEntityRepository.save(activityEntity);
    }

    @Transactional
    public void deleteActivitiesForUser(int userId) {
        activityEntityRepository.deleteActivityEntitiesByUserId(userId);
    }

}
