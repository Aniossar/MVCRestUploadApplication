package com.CalculatorMVCUpload.controller.api;

import com.CalculatorMVCUpload.configuration.SystemParametersConfig;
import com.CalculatorMVCUpload.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api")
public class SystemInfoController {

    @Autowired
    private SystemParametersConfig systemParametersConfig;

    @Autowired
    private UserService userService;

    private final String patternFormat = "dd.MM.yyyy hh:mm:ss";

    @GetMapping("/getApplicationStart")
    public String getApplicationStart() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(patternFormat).withZone(ZoneId.systemDefault());
        String formattedApplicationStart = formatter.format(systemParametersConfig.getApplicationStarted());
        return formattedApplicationStart;
    }

    @GetMapping("/getApplicationWorkingTime")
    public String getApplicationWorkingTime() {
        Instant timeNow = Instant.now();
        Instant applicationStartTime = systemParametersConfig.getApplicationStarted();
        long secondPassed = timeNow.getEpochSecond() - applicationStartTime.getEpochSecond();
        long days = secondPassed / 86400;
        long hours = secondPassed / 3600 % 24;
        long minutes = secondPassed / 60 % 60;
        long seconds = secondPassed - days * 86400 - hours * 3600 - minutes * 60;
        String dayStr = (days != 0) ? days + " дней " : "";
        String hourStr = (hours != 0) ? hours + " часов " : "";
        return dayStr + hourStr + minutes + " минут " + seconds + " секунд";
    }
}
