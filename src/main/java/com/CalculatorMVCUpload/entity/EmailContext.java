package com.CalculatorMVCUpload.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailContext {

    private String from;
    private String to;
    private String subject;
    private String templateLocation;
    private Map<String, Object> context;

}
