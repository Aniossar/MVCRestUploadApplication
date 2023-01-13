package com.CalculatorMVCUpload.payload.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FileInfoChangeRequest {

    private String info;

    private String forClients;
}
