package com.CalculatorMVCUpload.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/login")
    public String myLogin() {
        return "login";
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @GetMapping("/dashboard")
    public String myDashboard() {
        return "dashboard";
    }

    @GetMapping("/userInfo")
    public String userPage() {
        return "userPage";
    }

    @GetMapping("/statistics")
    public String statistics() {
        return "statistics";
    }

    @GetMapping("/appUpdates")
    public String appUpdates() {
        return "appUpdates";
    }

    @GetMapping("/pricesUpdates")
    public String pricesUpdates() {
        return "pricesUpdates";
    }

    @GetMapping("/fileUploader")
    public String fileUploader() {
        return "fileUploader";
    }
}
