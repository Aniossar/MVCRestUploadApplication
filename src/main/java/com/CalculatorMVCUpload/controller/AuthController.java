package com.CalculatorMVCUpload.controller;

import com.CalculatorMVCUpload.configuration.CustomUserDetails;
import com.CalculatorMVCUpload.configuration.CustomUserDetailsService;
import com.CalculatorMVCUpload.configuration.jwt.JwtProvider;
import com.CalculatorMVCUpload.entity.UserEntity;
import com.CalculatorMVCUpload.exception.BadAuthException;
import com.CalculatorMVCUpload.exception.ExistingLoginEmailRegister;
import com.CalculatorMVCUpload.payload.AuthentificationRequest;
import com.CalculatorMVCUpload.payload.AuthentificationResponse;
import com.CalculatorMVCUpload.payload.MeResponse;
import com.CalculatorMVCUpload.payload.RegistrationRequest;
import com.CalculatorMVCUpload.service.UserService;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.util.StringUtils.hasText;


@RestController
@NoArgsConstructor
@Log
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    CustomUserDetailsService customUserDetailsService;
    @Autowired
    private JwtProvider jwtProvider;


    @PostMapping("/register")
    public String registerUser(@RequestBody @Valid RegistrationRequest registrationRequest) {
        UserEntity userEntity = new UserEntity();
        if (userService.findByLogin(registrationRequest.getLogin()) != null
                || userService.findByEmail(registrationRequest.getEmail()) != null){
            throw new ExistingLoginEmailRegister("This login or email is already registered");
        }
        userEntity.setPassword(registrationRequest.getPassword());
        userEntity.setLogin(registrationRequest.getLogin());
        userEntity.setEmail(registrationRequest.getEmail());
        userEntity.setFullName(registrationRequest.getFullName());
        userEntity.setCompanyName(registrationRequest.getCompanyName());
        userEntity.setPhoneNumber(registrationRequest.getPhoneNumber());
        userEntity.setAddress(registrationRequest.getAddress());
        userEntity.setCertainPlaceAddress(registrationRequest.getCertainPlaceAddress());
        userService.saveUser(userEntity, registrationRequest.getDesiredRole());
        return "OK";
    }

    @PostMapping("/auth")
    public AuthentificationResponse auth(@RequestBody AuthentificationRequest request) {
        try {
            UserEntity userEntity = userService.findByLoginAndPassword(request.getLogin(), request.getPassword());
            String token = jwtProvider.generateToken(userEntity.getLogin());
            return new AuthentificationResponse(token);
        } catch (NullPointerException nullPointerException) {
            log.warning("Failed auth with login: " + request.getLogin());
            throw new BadAuthException("Login/password are incorrect");
        }
    }

    @GetMapping("/me")
    public MeResponse checkUserAuth(@RequestHeader(name = "Authorization") String bearer) {
        String token = null;
        if (hasText(bearer) && bearer.startsWith("Bearer ")) {
            token = bearer.substring(7);
        }
        if (token != null && jwtProvider.validateToken(token)) {
            String userLogin = jwtProvider.getLoginFromToken(token);
            CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(userLogin);

            //добавить нормальный getRole
            MeResponse meResponse = new MeResponse(userLogin, "USER");
            return meResponse;
        } else return null;
    }
}