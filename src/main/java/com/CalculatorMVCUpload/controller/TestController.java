package com.CalculatorMVCUpload.controller;

import com.CalculatorMVCUpload.payload.request.SingleMessageRequest;
import com.CalculatorMVCUpload.service.JSONParsingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private JSONParsingService jsonParsingService;

    @PostMapping("/testJson")
    public void testJson(@RequestBody SingleMessageRequest request) {
        System.out.println(request.getMessage());
        System.out.println(jsonParsingService.readObjectFromString(request.getMessage()));
    }
}
