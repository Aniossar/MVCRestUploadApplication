package com.CalculatorMVCUpload.controller.api;

import com.CalculatorMVCUpload.entity.CalculatorActivityEntity;
import com.CalculatorMVCUpload.entity.users.UserEntity;
import com.CalculatorMVCUpload.payload.request.CalcActivityFilterRequest;
import com.CalculatorMVCUpload.payload.request.CalcActivitySaveRequest;
import com.CalculatorMVCUpload.payload.response.CalculatorActivityResponse;
import com.CalculatorMVCUpload.payload.response.UploadFileResponse;
import com.CalculatorMVCUpload.service.ExcelWriterService;
import com.CalculatorMVCUpload.service.activity.CalculatorActivityService;
import com.CalculatorMVCUpload.service.users.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/app")
@Log
public class AppActivityController {

    @Autowired
    private UserService userService;

    @Autowired
    private CalculatorActivityService calculatorActivityService;

    @Autowired
    private ExcelWriterService excelWriterService;

    @Autowired
    private FileController fileController;

    @CrossOrigin
    @GetMapping("/allCalcActivities")
    public List<CalculatorActivityResponse> getAllCalculatorActivities() {
        List<CalculatorActivityResponse> activityResponses = new ArrayList<>();
        List<CalculatorActivityEntity> allActivities = calculatorActivityService.getAllActivities();
        allActivities.forEach(activityEntity -> {
            activityResponses.add(calculatorActivityService.transformEntityToResponse(activityEntity));
        });
        return activityResponses;
    }

    @CrossOrigin
    @GetMapping("/allCalcActivitiesByType/{type}")
    public List<CalculatorActivityEntity> getAllCalculatorActivitiesByType(@PathVariable String type) {
        return calculatorActivityService.getActivitiesByType(type);
    }

    @CrossOrigin
    @PostMapping("/saveCalcActivity")
    public String saveCalculatorActivity(@RequestBody CalcActivitySaveRequest request) {

        String loginName = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            CalculatorActivityEntity activityEntity = new CalculatorActivityEntity();
            activityEntity.setActivityTime(Instant.now());

            UserEntity userEntity = userService.findByLogin(loginName);
            activityEntity.setUserId(userEntity.getId());
            /*activityEntity.setCompanyName(userEntity.getCompanyName());
            activityEntity.setCertainPlaceAddress(userEntity.getCertainPlaceAddress());*/

            activityEntity.setType(request.getType());
            activityEntity.setMaterials(request.getMaterials());
            activityEntity.setMaterialPrice(request.getMaterialPrice());
            activityEntity.setAddPrice(request.getAddPrice());
            activityEntity.setAllPrice(request.getAllPrice());
            activityEntity.setMainCoeff(request.getMainCoeff());
            activityEntity.setMaterialCoeff(request.getMaterialCoeff());
            activityEntity.setSlabs(request.getSlabs());
            activityEntity.setProductSquare(request.getProductSquare());

            List<CalculatorActivityEntity> existingList = calculatorActivityService
                    .checkExistingActivitiesByMaterialsAllPriceAndType(
                            activityEntity.getMaterials(), activityEntity.getAllPrice(), activityEntity.getType());
            int counterNotLastDay = 0;
            if (existingList.size() != 0) {
                for (CalculatorActivityEntity existingEntity : existingList) {
                    long difference = activityEntity.getActivityTime().getEpochSecond()
                            - existingEntity.getActivityTime().getEpochSecond();
                    if (difference > 86400) counterNotLastDay++;
                }
            }
            if (existingList.size() == counterNotLastDay) {
                calculatorActivityService.saveNewCalculatorActivity(activityEntity);
                return "OK";
            }
            return "This line already exists";

        } catch (Exception e) {
            log.warning("Failed to deliver calculator activity from " + loginName);
            return "FAIL";
        }
    }

    @CrossOrigin
    @PostMapping("/calcActivityFilter")
    public List<CalculatorActivityResponse> getActivitiesByFilter(@RequestBody CalcActivityFilterRequest request) {
        CalcActivityFilterRequest filterRequest = calculatorActivityService.manageRequestForFilter(request);
        if (filterRequest.getCompanyName().contains(",") || filterRequest.getCertainPlaceAddress().contains("#")
                || filterRequest.getMaterials().contains(",") || filterRequest.getType().contains(",")) {
            List<CalculatorActivityEntity> activitiesByPreliminaryFilter = calculatorActivityService
                    .getActivitiesByPreliminaryFilter(filterRequest);
            List<CalculatorActivityResponse> resultActivitiesList = new ArrayList<>();
            for (CalculatorActivityEntity entity : activitiesByPreliminaryFilter) {
                if (checkIsEntityFieldEqualsRequest(calculatorActivityService.addPluses(filterRequest.getCompanyName(), ","),
                        calculatorActivityService.addPluses(entity.getCompanyName(), ","))
                        &&
                        checkIsEntityFieldEqualsRequest(calculatorActivityService.addPluses(filterRequest.getCertainPlaceAddress(), "#"),
                                calculatorActivityService.addPluses(entity.getCertainPlaceAddress(), "#"))
                        &&
                        checkIsEntityFieldEqualsRequest(calculatorActivityService.addPluses(filterRequest.getMaterials(), ","),
                                calculatorActivityService.addPluses(entity.getMaterials(), ","))
                        &&
                        checkIsEntityFieldEqualsRequest(calculatorActivityService.addPluses(filterRequest.getType(), ","),
                                calculatorActivityService.addPluses(entity.getType(), ","))) {
                    resultActivitiesList.add(calculatorActivityService.transformEntityToResponse(entity));
                }
            }
            return resultActivitiesList;
        }
        List<CalculatorActivityResponse> activityResponses = new ArrayList<>();
        List<CalculatorActivityEntity> activitiesByFilter = calculatorActivityService.getActivitiesByFilter(filterRequest);
        if (activitiesByFilter.size() > 0) {
            activitiesByFilter.forEach(activityEntity -> {
                activityResponses.add(calculatorActivityService.transformEntityToResponse(activityEntity));
            });
        }
        return activityResponses;
    }

    public boolean checkIsEntityFieldEqualsRequest(String requestField, String entityField) {
        if ("+%%+".equals(requestField)) return true;
        return requestField.contains(entityField);
    }

    @CrossOrigin
    @PostMapping("/calcActivityFilterFile")
    public ResponseEntity<UploadFileResponse> getActivitiesByFilterToFile(@RequestBody CalcActivityFilterRequest request) {
        List<CalculatorActivityResponse> activitiesByFilter = getActivitiesByFilter(request);
        String fileName = excelWriterService.writeExcelFile(activitiesByFilter);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/app/downloadFile/")
                .path(fileName + ".xlsx")
                .toUriString();

        if (fileName != null) {
            UploadFileResponse response = new UploadFileResponse
                    (fileName + ".xlsx", fileDownloadUri, "xlsx", 0);
            return ResponseEntity.ok(response);
        }
        return null;
    }

    @CrossOrigin
    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = excelWriterService.loadFileAsResource(fileName);
        return fileController.downloadFile(resource, request);
    }
}
