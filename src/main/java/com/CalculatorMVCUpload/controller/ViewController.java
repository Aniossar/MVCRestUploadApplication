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
}
