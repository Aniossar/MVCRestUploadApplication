package com.CalculatorMVCUpload.service.activity;

import com.CalculatorMVCUpload.entity.CalculatorActivityEntity;
import com.CalculatorMVCUpload.payload.request.CalcActivityFilterRequest;
import com.CalculatorMVCUpload.repository.CalculatorActivityEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

@Service
public class CalculatorActivityService {

    @Autowired
    private CalculatorActivityEntityRepository calculatorActivityEntityRepository;

    private final String datePattern = "yyyy-MM-dd";

    @Transactional
    public List<CalculatorActivityEntity> getAllActivities() {
        return calculatorActivityEntityRepository.findAll();
    }

    @Transactional
    public void saveNewCalculatorActivity(CalculatorActivityEntity calculatorActivityEntity) {
        calculatorActivityEntityRepository.saveAndFlush(calculatorActivityEntity);
    }

    public CalcActivityFilterRequest manageRequestForFilter(CalcActivityFilterRequest request) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
        if (request.getDateFrom() == null) {
            LocalDate date = LocalDate.of(2000, 01, 01);
            request.setDateFrom(date);
        }
        if (request.getDateTo() == null) {
            LocalDate date = LocalDate.now().plusDays(1);
            request.setDateTo(date);
        }
        if (request.getCompanyName() == null) {
            request.setCompanyName("%%");
        } else request.setCompanyName("%" + request.getCompanyName() + "%");
        if (request.getCertainPlaceAddress() == null) {
            request.setCertainPlaceAddress("%%");
        } else request.setCertainPlaceAddress("%" + request.getCertainPlaceAddress() + "%");
        if (request.getMaterialPriceFrom() == 0) {
            request.setMaterialPriceFrom(0);
        }
        if (request.getMaterialPriceTo() == 0) {
            request.setMaterialPriceTo(Double.MAX_VALUE);
        }
        if (request.getAddPriceFrom() == 0) {
            request.setAddPriceFrom(0);
        }
        if (request.getAddPriceTo() == 0) {
            request.setAddPriceTo(Double.MAX_VALUE);
        }
        if (request.getAllPriceFrom() == 0) {
            request.setAllPriceFrom(0);
        }
        if (request.getAllPriceTo() == 0) {
            request.setAllPriceTo(Double.MAX_VALUE);
        }
        if (request.getMaterials() == null) {
            request.setMaterials("%%");
        } else request.setMaterials("%" + request.getMaterials() + "%");

        return request;
    }

    public List<CalculatorActivityEntity> getActivitiesByFilter(CalcActivityFilterRequest request) {
        return calculatorActivityEntityRepository.selectByFilterFields(request.getDateFrom(), request.getDateTo(),
                request.getCompanyName(), request.getCertainPlaceAddress(),
                request.getMaterialPriceFrom(), request.getMaterialPriceTo(),
                request.getAddPriceFrom(), request.getAddPriceTo(),
                request.getAllPriceFrom(), request.getAllPriceTo(),
                request.getMaterials());
    }
}
