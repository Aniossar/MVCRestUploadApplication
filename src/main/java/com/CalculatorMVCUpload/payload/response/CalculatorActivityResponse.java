package com.CalculatorMVCUpload.payload.response;

import com.CalculatorMVCUpload.entity.CalculatorActivityEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class CalculatorActivityResponse {

    private Instant activityTime;

    private int userId;

    private String userLogin;

    private String companyName;

    private String certainPlaceAddress;

    private String type;

    private String materials;

    private double materialPrice;

    private double addPrice;

    private double allPrice;

    private double mainCoeff;

    private double materialCoeff;

    private double slabs;

    private double productSquare;

    public List<String> getClassFieldsName() {
        Field[] fields = CalculatorActivityEntity.class.getDeclaredFields();
        List<Field> fieldList = Arrays.asList(fields);

        List<String> fieldNameList = fieldList.stream().map(item -> item.getName()).collect(Collectors.toList());
        return fieldNameList;
    }
}
