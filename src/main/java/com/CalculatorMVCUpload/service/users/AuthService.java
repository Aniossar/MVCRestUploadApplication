package com.CalculatorMVCUpload.service.users;

import com.CalculatorMVCUpload.configuration.jwt.JwtProvider;
import com.CalculatorMVCUpload.entity.users.PasswordResetToken;
import com.CalculatorMVCUpload.entity.users.RefreshTokenEntity;
import com.CalculatorMVCUpload.entity.users.UserEntity;
import com.CalculatorMVCUpload.exception.BadAuthException;
import com.CalculatorMVCUpload.payload.request.users.AuthentificationRequest;
import com.CalculatorMVCUpload.payload.response.AuthentificationResponse;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Log
public class AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private RefreshTokenService refreshTokenService;

    private final Map<String, ArrayList<String>> refreshStorage = new HashMap<>();
    private Map<String, PasswordResetToken> restoringPasswordTokensStorage = new HashMap<>();
    private final int numberOfConcurrentSessions = 5;

    public AuthentificationResponse authenticate(AuthentificationRequest authRequest) {
        try {
            UserEntity userEntity = userService.findByLoginAndPassword(authRequest.getLogin(), authRequest.getPassword());
            if (userEntity == null) {
                userEntity = userService.findByEmailAndPassword(authRequest.getLogin(), authRequest.getPassword());
            }
            if (userEntity.isEnabled()) {
                String accessToken = jwtProvider.generateAccessToken
                        (userEntity.getLogin(), userEntity.getId(), userEntity.getRoleEntity());
                String refreshToken = jwtProvider.generateRefreshToken(userEntity.getLogin(), userEntity.getId());
                List<RefreshTokenEntity> allUserRefreshTokens = refreshTokenService.getAllUserRefreshTokens(userEntity.getId());
                if (allUserRefreshTokens != null && allUserRefreshTokens.size() >= numberOfConcurrentSessions) {
                    RefreshTokenEntity oldestToken = refreshTokenService.getOldestToken(allUserRefreshTokens);
                    refreshTokenService.deleteRefreshToken(oldestToken);
                }
                RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
                refreshTokenEntity.setRefreshToken(refreshToken);
                refreshTokenEntity.setUserId(userEntity.getId());
                refreshTokenService.saveRefreshToken(refreshTokenEntity);
                return new AuthentificationResponse(accessToken, refreshToken);
            }
        } catch (NullPointerException nullPointerException) {
            log.warning("Failed auth with login: " + authRequest.getLogin());
            throw new BadAuthException("Login/password are incorrect");
        }
        return new AuthentificationResponse(null, null);
    }

    public AuthentificationResponse getAccessToken(String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            int idFromRefreshToken = jwtProvider.getIdFromRefreshToken(refreshToken);
            List<RefreshTokenEntity> savedRefreshTokens = refreshTokenService.getAllUserRefreshTokens(idFromRefreshToken);
            if (savedRefreshTokens != null && refreshTokenService.containsToken(savedRefreshTokens, refreshToken)) {
                UserEntity userEntity = userService.findById(idFromRefreshToken);
                if (userEntity.isEnabled()) {
                    String accessToken = jwtProvider.generateAccessToken
                            (userEntity.getLogin(), userEntity.getId(), userEntity.getRoleEntity());
                    return new AuthentificationResponse(accessToken, null);
                }
            }
        }
        return new AuthentificationResponse(null, null);
    }

    public AuthentificationResponse getNewRefreshToken(String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            int idFromRefreshToken = jwtProvider.getIdFromRefreshToken(refreshToken);
            List<RefreshTokenEntity> savedRefreshTokens = refreshTokenService.getAllUserRefreshTokens(idFromRefreshToken);
            if (savedRefreshTokens != null && refreshTokenService.containsToken(savedRefreshTokens, refreshToken)) {
                UserEntity userEntity = userService.findById(idFromRefreshToken);
                String accessToken = jwtProvider.generateAccessToken
                        (userEntity.getLogin(), userEntity.getId(), userEntity.getRoleEntity());
                String newRefreshToken = jwtProvider.generateRefreshToken(userEntity.getLogin(), userEntity.getId());
                RefreshTokenEntity tokenEntityViaToken = refreshTokenService.getTokenEntityViaToken(refreshToken);
                refreshTokenService.deleteRefreshToken(tokenEntityViaToken);
                RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
                refreshTokenEntity.setRefreshToken(newRefreshToken);
                refreshTokenEntity.setUserId(userEntity.getId());
                refreshTokenService.saveRefreshToken(refreshTokenEntity);
                return new AuthentificationResponse(accessToken, newRefreshToken);
            }
        }
        throw new BadAuthException("Token is not valid");
    }

    public String generateRestoringPasswordToken(@NonNull int userId) {
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        Instant expiryTime = Instant.now().plus(30, ChronoUnit.MINUTES);
        resetToken.setExpiryTime(expiryTime);
        resetToken.setUserId(userId);
        restoringPasswordTokensStorage.put(token, resetToken);
        return token;
    }

    public String getLoginFromRestoreToken(@NonNull String token) {
        PasswordResetToken resetToken = restoringPasswordTokensStorage.get(token);
        if (resetToken != null) {
            Instant timeNow = Instant.now();
            if (timeNow.isBefore(resetToken.getExpiryTime())) {
                String userLogin = resetToken.getLogin();
                restoringPasswordTokensStorage.remove(token);
                return userLogin;
            }
        }
        return null;
    }

    public int getIdFromRestoreToken(@NonNull String token) {
        PasswordResetToken resetToken = restoringPasswordTokensStorage.get(token);
        if (resetToken != null) {
            Instant timeNow = Instant.now();
            if (timeNow.isBefore(resetToken.getExpiryTime())) {
                int userId = resetToken.getUserId();
                restoringPasswordTokensStorage.remove(token);
                return userId;
            }
        }
        return -1;
    }

}
