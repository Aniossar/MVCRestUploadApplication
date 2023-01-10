package com.CalculatorMVCUpload.configuration;

import lombok.Getter;
import lombok.extern.java.Log;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;

@Configuration
@Log
@Getter
public class SystemParametersConfig implements CommandLineRunner {

    private Instant applicationStarted;

    public SystemParametersConfig() {
    }

    @Override
    public void run(String... args) throws Exception {
        this.applicationStarted = Instant.now();
        log.info("Application started: " + applicationStarted);
    }
}
