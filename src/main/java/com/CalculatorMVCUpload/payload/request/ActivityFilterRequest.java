package com.CalculatorMVCUpload.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class ActivityFilterRequest {

    private Date dateFrom;

    private Date dateTo;

    private double materialPriceFrom;

    private double materialPriceTo;

    private double addPriceFrom;

    private double addPriceTo;

    private double allPriceFrom;

    private double allPriceTo;

    private String materials;

}
