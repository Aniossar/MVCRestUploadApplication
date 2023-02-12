package com.CalculatorMVCUpload.repository;

import com.CalculatorMVCUpload.entity.ActivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityEntityRepository extends JpaRepository<ActivityEntity, Integer> {

    List<ActivityEntity> findByUserId(int userId);

    void deleteActivityEntitiesByUserId(int userId);

    List<ActivityEntity> findByActivityType(String activityType);

}
