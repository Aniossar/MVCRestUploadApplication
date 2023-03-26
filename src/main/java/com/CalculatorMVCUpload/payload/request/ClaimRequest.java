package com.CalculatorMVCUpload.payload.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClaimRequest {

    public enum ClaimType {
        ADVICE, ERROR
    }

    private ClaimType claimType;

    private String claimText;
}
