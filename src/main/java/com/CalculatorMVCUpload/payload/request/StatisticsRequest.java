package com.CalculatorMVCUpload.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StatisticsRequest {

    private String type;

    private String startDateTime;

    private String endDateTime;

    private int precession;
}
