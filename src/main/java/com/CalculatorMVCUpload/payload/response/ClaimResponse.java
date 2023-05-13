package com.CalculatorMVCUpload.payload.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class ClaimResponse {

    private int id;

    private Instant timeDate;

    private int userId;

    private String userName;

    private String type;

    private String name;

    private int responsibleUserId;

    private String responsibleUserName;

    private Boolean isSolved;

    private String claimText;
}
