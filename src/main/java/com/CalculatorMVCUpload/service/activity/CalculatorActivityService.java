package com.CalculatorMVCUpload.service.activity;

import com.CalculatorMVCUpload.entity.CalculatorActivityEntity;
import com.CalculatorMVCUpload.payload.request.CalcActivityFilterRequest;
import com.CalculatorMVCUpload.repository.CalculatorActivityEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

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

    public String addPluses(String originalString) {
        if (!originalString.contains(",")) {
            return "+" + originalString + "+";
        }
        Pattern pattern = Pattern.compile(",");
        String[] str = pattern.split(originalString);
        String resultString = "";
        for (String strElement : str) {
            resultString = resultString + "+" + strElement + "+";
        }
        return resultString;
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
        if (request.getMaterialPriceFrom() == -1) {
            request.setMaterialPriceFrom(0);
        }
        if (request.getMaterialPriceTo() == -1) {
            request.setMaterialPriceTo(Double.MAX_VALUE);
        }
        if (request.getAddPriceFrom() == -1) {
            request.setAddPriceFrom(0);
        }
        if (request.getAddPriceTo() == -1) {
            request.setAddPriceTo(Double.MAX_VALUE);
        }
        if (request.getAllPriceFrom() == -1) {
            request.setAllPriceFrom(0);
        }
        if (request.getAllPriceTo() == -1) {
            request.setAllPriceTo(Double.MAX_VALUE);
        }
        if (request.getMaterials() == null) {
            request.setMaterials("%%");
        } else request.setMaterials("%" + request.getMaterials() + "%");

        return request;
    }

    @Transactional
    public List<CalculatorActivityEntity> getActivitiesByFilter(CalcActivityFilterRequest request) {
        return calculatorActivityEntityRepository.selectByFilterFields(request.getDateFrom(), request.getDateTo(),
                request.getCompanyName(), request.getCertainPlaceAddress(),
                request.getMaterialPriceFrom(), request.getMaterialPriceTo(),
                request.getAddPriceFrom(), request.getAddPriceTo(),
                request.getAllPriceFrom(), request.getAllPriceTo(),
                request.getMaterials(), request.getType());
    }

    @Transactional
    public List<CalculatorActivityEntity> getActivitiesByPreliminaryFilter(CalcActivityFilterRequest request) {
        return calculatorActivityEntityRepository.selectByPreliminaryFilter(request.getDateFrom(), request.getDateTo(),
                request.getMaterialPriceFrom(), request.getMaterialPriceTo(),
                request.getAddPriceFrom(), request.getAddPriceTo(),
                request.getAllPriceFrom(), request.getAllPriceTo());
    }

    @Transactional
    public List<CalculatorActivityEntity> getActivitiesByTypeAndTime(Instant startTime, Instant endTime, String type) {
        return calculatorActivityEntityRepository.selectByTypeAndTime(startTime, endTime, type);
    }

    @Transactional
    public List<CalculatorActivityEntity> getActivitiesByType(String type) {
        return calculatorActivityEntityRepository.findAllByType(type);
    }
}
