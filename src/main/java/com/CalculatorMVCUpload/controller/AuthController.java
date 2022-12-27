package com.CalculatorMVCUpload.controller;

import com.CalculatorMVCUpload.configuration.jwt.JwtProvider;
import com.CalculatorMVCUpload.payload.AuthentificationRequest;
import com.CalculatorMVCUpload.payload.AuthentificationResponce;
import com.CalculatorMVCUpload.payload.RegistrationRequest;
import com.CalculatorMVCUpload.entity.UserEntity;
import com.CalculatorMVCUpload.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtProvider jwtProvider;

    public AuthController() {
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody @Valid RegistrationRequest registrationRequest) {
        UserEntity userEntity = new UserEntity();
        userEntity.setPassword(registrationRequest.getPassword());
        userEntity.setLogin(registrationRequest.getLogin());
        userEntity.setEmail(registrationRequest.getEmail());
        userService.saveUser(userEntity, registrationRequest.getDesiredRole());
        return "OK";
    }

    @PostMapping("/auth")
    public AuthentificationResponce auth(@RequestBody AuthentificationRequest request) {
        UserEntity userEntity = userService.findByLoginAndPassword(request.getLogin(), request.getPassword());
        String token = jwtProvider.generateToken(userEntity.getLogin());
        return new AuthentificationResponce(token);
    }
}
