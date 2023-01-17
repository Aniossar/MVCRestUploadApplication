package com.CalculatorMVCUpload.controller.api;

import com.CalculatorMVCUpload.entity.CalculatorActivityEntity;
import com.CalculatorMVCUpload.entity.users.UserEntity;
import com.CalculatorMVCUpload.payload.request.CalcActivityFilterRequest;
import com.CalculatorMVCUpload.payload.request.CalcActivitySaveRequest;
import com.CalculatorMVCUpload.service.activity.CalculatorActivityService;
import com.CalculatorMVCUpload.service.users.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/app")
@Log
public class AppActivityController {

    @Autowired
    private UserService userService;

    @Autowired
    private CalculatorActivityService calculatorActivityService;

    @GetMapping("/allCalcActivities")
    public List<CalculatorActivityEntity> getAllCalculatorActivities(){
        return calculatorActivityService.getAllActivities();
    }

    @PostMapping("/saveCalcActivity")
    public String saveCalculatorActivity(@RequestBody CalcActivitySaveRequest request) {

        String loginName = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            CalculatorActivityEntity activityEntity = new CalculatorActivityEntity();
            activityEntity.setActivityTime(Instant.now());

            activityEntity.setLogin(loginName);

            UserEntity userEntity = userService.findByLogin(loginName);
            activityEntity.setUserId(userEntity.getId());
            activityEntity.setCompanyName(userEntity.getCompanyName());
            activityEntity.setCertainPlaceAddress(userEntity.getCertainPlaceAddress());

            activityEntity.setType(request.getType());
            activityEntity.setMaterials(request.getMaterials());
            activityEntity.setMaterialPrice(request.getMaterialPrice());
            activityEntity.setAddPrice(request.getAddPrice());
            activityEntity.setAllPrice(request.getAllPrice());
            activityEntity.setMainCoeff(request.getMainCoeff());
            activityEntity.setMaterialCoeff(request.getMaterialCoeff());
            activityEntity.setSlabs(request.getSlabs());
            activityEntity.setProductSquare(request.getProductSquare());

            calculatorActivityService.saveNewCalculatorActivity(activityEntity);
            return "OK";
        } catch (Exception e) {
            log.warning("Failed to deliver calculator activity from " + loginName);
            return "FAIL";
        }
    }

    @PostMapping("/calcActivityFilter")
    public List<CalculatorActivityEntity> getActivitiesByFilter(@RequestBody CalcActivityFilterRequest request){
        CalcActivityFilterRequest filterRequest = calculatorActivityService.manageRequestForFilter(request);
        return calculatorActivityService.getActivitiesByFilter(filterRequest);
    }
}
