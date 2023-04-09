package com.CalculatorMVCUpload.payload.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

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
}
