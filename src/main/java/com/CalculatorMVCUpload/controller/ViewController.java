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


    @GetMapping("/changePassword?token={restoreToken}")
    public String restorePasswordPage(String restoreToken) {
        return "restorePass";
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

    @GetMapping("/usersList")
    public String usersList(){
        return "usersList";
    }

    @GetMapping("/profile")
    public String profile(){
        return "profile";
    }

    @GetMapping("/createUser")
    public String createUser(){
        return "createUser";
    }
}
