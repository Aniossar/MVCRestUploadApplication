package com.CalculatorMVCUpload.payload.request;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CalcActivityFilterRequest {

    private LocalDate dateFrom;

    private LocalDate dateTo;

    private String companyName;

    private String certainPlaceAddress;

    private double materialPriceFrom;

    private double materialPriceTo;

    private double addPriceFrom;

    private double addPriceTo;

    private double allPriceFrom;

    private double allPriceTo;

    private String materials;

    private String type;
}
