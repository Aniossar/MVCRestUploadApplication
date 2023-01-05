package com.CalculatorMVCUpload.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/login_window")
    public String myLogin() {
        return "login_window";
    }

    @GetMapping("/dashboard")
    public String myDashboard() {
        return "dashboard";
    }
}