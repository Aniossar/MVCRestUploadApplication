package com.CalculatorMVCUpload.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CalculatorActivityEntity {

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
