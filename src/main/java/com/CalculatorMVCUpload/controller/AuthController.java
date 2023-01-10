package com.CalculatorMVCUpload.controller;

import com.CalculatorMVCUpload.configuration.CustomUserDetailsService;
import com.CalculatorMVCUpload.configuration.jwt.JwtProvider;
import com.CalculatorMVCUpload.entity.UserEntity;
import com.CalculatorMVCUpload.exception.BadAuthException;
import com.CalculatorMVCUpload.exception.ExistingLoginEmailRegisterException;
import com.CalculatorMVCUpload.payload.request.AuthentificationRequest;
import com.CalculatorMVCUpload.payload.request.RefreshTokenRequest;
import com.CalculatorMVCUpload.payload.request.RegistrationRequest;
import com.CalculatorMVCUpload.payload.response.AuthentificationResponse;
import com.CalculatorMVCUpload.payload.response.MeResponse;
import com.CalculatorMVCUpload.service.AuthService;
import com.CalculatorMVCUpload.service.UserService;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    private AuthService authService;

    @Autowired
    CustomUserDetailsService customUserDetailsService;
    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/register")
    public String registerUser(@RequestBody @Valid RegistrationRequest registrationRequest) {
        UserEntity userEntity = new UserEntity();
        if (userService.findByLogin(registrationRequest.getLogin()) != null
                || userService.findByEmail(registrationRequest.getEmail()) != null) {
            throw new ExistingLoginEmailRegisterException("This login or email is already registered");
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
    public ResponseEntity<AuthentificationResponse> auth(@RequestBody AuthentificationRequest request) {
        AuthentificationResponse authResponse = authService.authenticate(request);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/token")
    public ResponseEntity<AuthentificationResponse> getNewAccessToken(@RequestBody RefreshTokenRequest request){
        AuthentificationResponse authResponse = authService.getAccessToken(request.getRefreshToken());
        return ResponseEntity.ok(authResponse);
    }

    @GetMapping("/me")
    public MeResponse checkUserAuth(@RequestHeader(name = "Authorization") String bearer) {
        String token = null;
        if (hasText(bearer) && bearer.startsWith("Bearer ")) {
            token = bearer.substring(7);
        }
        if (token != null && jwtProvider.validateAccessToken(token)) {
            String userLogin = jwtProvider.getLoginFromAccessToken(token);
            String roleFromToken = jwtProvider.getRoleFromAccessToken(token);
            return new MeResponse(userLogin, roleFromToken);
        } else throw new BadAuthException("No user is authorized");
    }
}