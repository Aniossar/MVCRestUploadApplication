package com.CalculatorMVCUpload.service.activity;

import com.CalculatorMVCUpload.entity.CalculatorActivityEntity;
import com.CalculatorMVCUpload.payload.request.CalcActivityFilterRequest;
import com.CalculatorMVCUpload.payload.response.CalculatorActivityResponse;
import com.CalculatorMVCUpload.repository.CalculatorActivityEntityRepository;
import com.CalculatorMVCUpload.service.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class CalculatorActivityService {

    @Autowired
    private CalculatorActivityEntityRepository calculatorActivityEntityRepository;

    @Autowired
    private UserService userService;

    private final String datePattern = "yyyy-MM-dd";

    @Transactional
    public List<CalculatorActivityEntity> getAllActivities() {
        return calculatorActivityEntityRepository.findAll();
    }

    @Transactional
    public void saveNewCalculatorActivity(CalculatorActivityEntity calculatorActivityEntity) {
        calculatorActivityEntityRepository.saveAndFlush(calculatorActivityEntity);
    }

    public String addPluses(String originalString, CharSequence delimiter) {
        if (!originalString.contains(delimiter)) {
            return "+" + originalString + "+";
        }
        Pattern pattern = Pattern.compile(delimiter.toString());
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
        } //else request.setCompanyName(quotesEscaping(request.getCompanyName()));
        if (request.getCertainPlaceAddress() == null) {
            request.setCertainPlaceAddress("%%");
        } //else request.setCertainPlaceAddress(quotesEscaping(request.getCertainPlaceAddress()));
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
        } //else request.setMaterials(quotesEscaping(request.getMaterials()));

        if (request.getType() == null) {
            request.setType("%%");
        } //else request.setType("%" + request.getType() + "%");

        return request;
    }

    @Transactional
    public List<CalculatorActivityEntity> getActivitiesByFilter(CalcActivityFilterRequest request) {
        List<Object[]> objects = calculatorActivityEntityRepository.selectByFilterFields(request.getDateFrom(), request.getDateTo(),
                request.getCompanyName(), request.getCertainPlaceAddress(),
                request.getMaterialPriceFrom(), request.getMaterialPriceTo(),
                request.getAddPriceFrom(), request.getAddPriceTo(),
                request.getAllPriceFrom(), request.getAllPriceTo(),
                request.getMaterials(), request.getType());
        return transformObjectListToCAE(objects);
    }

    @Transactional
    public List<CalculatorActivityEntity> getActivitiesByPreliminaryFilter(CalcActivityFilterRequest request) {
        List<Object[]> objects = calculatorActivityEntityRepository.selectByPreliminaryFilter(request.getDateFrom(), request.getDateTo(),
                request.getMaterialPriceFrom(), request.getMaterialPriceTo(),
                request.getAddPriceFrom(), request.getAddPriceTo(),
                request.getAllPriceFrom(), request.getAllPriceTo());

        return transformObjectListToCAE(objects);
    }

    public List<CalculatorActivityEntity> transformObjectListToCAE(List<Object[]> objects) {
        ArrayList<CalculatorActivityEntity> result = new ArrayList<>();
        objects.forEach(object -> {
            CalculatorActivityEntity entity = new CalculatorActivityEntity();
            entity.setActivityTime(((Timestamp) object[0]).toInstant());
            entity.setUserId((Integer) object[1]);
            entity.setType((String) object[2]);
            entity.setMaterials((String) object[3]);
            entity.setMaterialPrice((Double) object[4]);
            entity.setAddPrice((Double) object[5]);
            entity.setAllPrice((Double) object[6]);
            entity.setMainCoeff((Double) object[7]);
            entity.setMaterialCoeff((Double) object[8]);
            entity.setSlabs((Double) object[9]);
            entity.setProductSquare((Double) object[10]);
            entity.setCompanyName((String) object[12]);
            entity.setCertainPlaceAddress((String) object[13]);
            result.add(entity);
        });
        return result;
    }

    @Transactional
    public List<CalculatorActivityEntity> getActivitiesByTypeAndTime(Instant startTime, Instant endTime, String type) {
        return calculatorActivityEntityRepository.selectByTypeAndTime(startTime, endTime, type);
    }

    @Transactional
    public List<CalculatorActivityEntity> getActivitiesByType(String type) {
        return calculatorActivityEntityRepository.findAllByType(type);
    }

    @Transactional
    public List<CalculatorActivityEntity> checkExistingActivitiesByMaterialsAllPriceAndType(String materials, Double allPrice, String type) {
        return calculatorActivityEntityRepository.selectByMaterialsAllPriceAndType(materials, allPrice, type);
    }

    public CalculatorActivityResponse transformEntityToResponse(CalculatorActivityEntity activityEntity) {
        CalculatorActivityResponse response = new CalculatorActivityResponse();
        response.setActivityTime(activityEntity.getActivityTime());
        response.setUserId(activityEntity.getUserId());
        response.setUserLogin(userService.findById(activityEntity.getUserId()).getLogin());
        response.setCompanyName(activityEntity.getCompanyName());
        response.setCertainPlaceAddress(activityEntity.getCertainPlaceAddress());
        response.setType(activityEntity.getType());
        response.setMaterials(activityEntity.getMaterials());
        response.setMaterialPrice(activityEntity.getMaterialPrice());
        response.setAddPrice(activityEntity.getAddPrice());
        response.setAllPrice(activityEntity.getAllPrice());
        response.setMainCoeff(activityEntity.getMainCoeff());
        response.setMaterialCoeff(activityEntity.getMaterialCoeff());
        response.setSlabs(activityEntity.getSlabs());
        response.setProductSquare(activityEntity.getProductSquare());
        return response;
    }

}
