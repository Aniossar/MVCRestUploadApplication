package com.CalculatorMVCUpload.payload.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class ActivityResponse {

    private Instant activityTime;

    private String activityType;

    private int userId;

    private String userLogin;

    private String activityMessage;
}
