package com.CalculatorMVCUpload.controller;

import com.CalculatorMVCUpload.configuration.CustomUserDetails;
import com.CalculatorMVCUpload.configuration.CustomUserDetailsService;
import com.CalculatorMVCUpload.configuration.jwt.JwtFilter;
import com.CalculatorMVCUpload.configuration.jwt.JwtProvider;
import com.CalculatorMVCUpload.payload.AuthentificationRequest;
import com.CalculatorMVCUpload.payload.AuthentificationResponse;
import com.CalculatorMVCUpload.payload.MeResponse;
import com.CalculatorMVCUpload.payload.RegistrationRequest;
import com.CalculatorMVCUpload.entity.UserEntity;
import com.CalculatorMVCUpload.service.UserService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.Collection;

import static org.springframework.util.StringUtils.hasText;


@RestController
@NoArgsConstructor
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
        userEntity.setPassword(registrationRequest.getPassword());
        userEntity.setLogin(registrationRequest.getLogin());
        userEntity.setEmail(registrationRequest.getEmail());
        userService.saveUser(userEntity, registrationRequest.getDesiredRole());
        return "OK";
    }

    @PostMapping("/auth")
    public AuthentificationResponse auth(@RequestBody AuthentificationRequest request) {
        UserEntity userEntity = userService.findByLoginAndPassword(request.getLogin(), request.getPassword());
        String token = jwtProvider.generateToken(userEntity.getLogin());
        return new AuthentificationResponse(token);
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