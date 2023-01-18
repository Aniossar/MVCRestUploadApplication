package com.CalculatorMVCUpload.service;

import com.CalculatorMVCUpload.entity.CalculatorActivityEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

@Service
@Log
public class JSONParsingService {

    public CalculatorActivityEntity readObjectFromString(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        CalculatorActivityEntity calculatorActivity = null;
        try {
            calculatorActivity = objectMapper.readValue(jsonString, CalculatorActivityEntity.class);
        } catch (JsonProcessingException e) {
            log.warning(e.getMessage());
        }
        return calculatorActivity;
    }
}
