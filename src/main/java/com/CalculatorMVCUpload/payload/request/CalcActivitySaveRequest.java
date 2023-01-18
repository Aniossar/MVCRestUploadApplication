package com.CalculatorMVCUpload.payload.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CalcActivitySaveRequest {

    private String type;

    private String materials;

    private double materialPrice;

    private double addPrice;

    private double allPrice;

    private double mainCoeff;

    private double materialCoeff;

    private double slabs;

    private double productSquare;
}

