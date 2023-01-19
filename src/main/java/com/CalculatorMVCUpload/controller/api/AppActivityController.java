package com.CalculatorMVCUpload.controller.api;

import com.CalculatorMVCUpload.entity.CalculatorActivityEntity;
import com.CalculatorMVCUpload.entity.users.UserEntity;
import com.CalculatorMVCUpload.payload.request.CalcActivityFilterRequest;
import com.CalculatorMVCUpload.payload.request.CalcActivitySaveRequest;
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

    @GetMapping("/allCalcActivities")
    public List<CalculatorActivityEntity> getAllCalculatorActivities() {
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
    public List<CalculatorActivityEntity> getActivitiesByFilter(@RequestBody CalcActivityFilterRequest request) {
        CalcActivityFilterRequest filterRequest = calculatorActivityService.manageRequestForFilter(request);
        return calculatorActivityService.getActivitiesByFilter(filterRequest);
    }

    @PostMapping("/calcActivityFilterFile")
    public ResponseEntity<UploadFileResponse> getActivitiesByFilterToFile(@RequestBody CalcActivityFilterRequest request) {
        CalcActivityFilterRequest filterRequest = calculatorActivityService.manageRequestForFilter(request);
        List<CalculatorActivityEntity> activitiesByFilter = calculatorActivityService.getActivitiesByFilter(filterRequest);

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

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = excelWriterService.loadFileAsResource(fileName);
        return fileController.downloadFile(resource, request);
    }


}
