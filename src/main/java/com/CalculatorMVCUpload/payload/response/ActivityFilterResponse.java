package com.CalculatorMVCUpload.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ActivityFilterResponse {

    private Date dateFrom;

    private Date dateTo;

    private String companyName;

    private String certainPlaceAddress;

    private double materialPriceFrom;

    private double materialPriceTo;

    private double addPriceFrom;

    private double addPriceTo;

    private double allPriceFrom;

    private double allPriceTo;

    private String materials;
}
