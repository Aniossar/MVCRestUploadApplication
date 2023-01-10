package com.CalculatorMVCUpload.service;

import com.CalculatorMVCUpload.configuration.jwt.JwtProvider;
import com.CalculatorMVCUpload.entity.UserEntity;
import com.CalculatorMVCUpload.exception.BadAuthException;
import com.CalculatorMVCUpload.payload.request.AuthentificationRequest;
import com.CalculatorMVCUpload.payload.response.AuthentificationResponse;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Log
public class AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProvider jwtProvider;

    private final Map<String, String> refreshStorage = new HashMap<>();

    public AuthentificationResponse authenticate(AuthentificationRequest authRequest) {
        try {
            UserEntity userEntity = userService.findByLoginAndPassword(authRequest.getLogin(), authRequest.getPassword());
            String accessToken = jwtProvider.generateAccessToken(userEntity.getLogin(), userEntity.getRoleEntity());
            String refreshToken = jwtProvider.generateRefreshToken(userEntity.getLogin());
            refreshStorage.put(userEntity.getLogin(), refreshToken);
            return new AuthentificationResponse(accessToken, refreshToken);
        } catch (NullPointerException nullPointerException) {
            log.warning("Failed auth with login: " + authRequest.getLogin());
            throw new BadAuthException("Login/password are incorrect");
        }
    }

    public AuthentificationResponse getAccessToken(String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            String login = jwtProvider.getLoginFromRefreshToken(refreshToken);
            String savedRefreshToken = refreshStorage.get(login);
            if (savedRefreshToken != null && savedRefreshToken.equals(refreshToken)) {
                UserEntity userEntity = userService.findByLogin(login);
                String accessToken = jwtProvider.generateAccessToken(userEntity.getLogin(), userEntity.getRoleEntity());
                return new AuthentificationResponse(accessToken, null);
            }
        }
        return new AuthentificationResponse(null, null);
    }

    public AuthentificationResponse getNewRefreshToken(String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            String login = jwtProvider.getLoginFromRefreshToken(refreshToken);
            String savedRefreshToken = refreshStorage.get(login);
            if (savedRefreshToken != null && savedRefreshToken.equals(refreshToken)) {
                UserEntity userEntity = userService.findByLogin(login);
                String accessToken = jwtProvider.generateAccessToken(userEntity.getLogin(), userEntity.getRoleEntity());
                String newRefreshToken = jwtProvider.generateRefreshToken(userEntity.getLogin());
                refreshStorage.put(userEntity.getLogin(), newRefreshToken);
                return new AuthentificationResponse(accessToken, newRefreshToken);
            }
        }
        throw new BadAuthException("Token is not valid");
    }
}
