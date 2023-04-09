package com.CalculatorMVCUpload.payload.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class DirectoryCheckResponse {
    private String name;
    private long size;
    private long dateModified;
}
