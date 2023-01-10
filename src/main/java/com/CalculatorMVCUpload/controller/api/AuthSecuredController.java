package com.CalculatorMVCUpload.controller.api;

import com.CalculatorMVCUpload.configuration.jwt.JwtProvider;
import com.CalculatorMVCUpload.entity.UserEntity;
import com.CalculatorMVCUpload.exception.BadAuthException;
import com.CalculatorMVCUpload.exception.WrongPasswordUserMovesException;
import com.CalculatorMVCUpload.payload.request.PasswordChangeRequest;
import com.CalculatorMVCUpload.payload.request.RefreshTokenRequest;
import com.CalculatorMVCUpload.payload.response.AuthentificationResponse;
import com.CalculatorMVCUpload.service.AuthService;
import com.CalculatorMVCUpload.service.UserService;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.util.StringUtils.hasText;

@RestController
@RequestMapping("/api")
@NoArgsConstructor
@Log
public class AuthSecuredController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserService userService;

    @PostMapping("/refreshToken")
    public ResponseEntity<AuthentificationResponse> getNewRefreshToken(@RequestBody RefreshTokenRequest request){
        AuthentificationResponse authResponse = authService.getNewRefreshToken(request.getRefreshToken());
        return ResponseEntity.ok(authResponse);
    }

    @PutMapping("/changeOwnPassword")
    public void changePasswordByUser(@RequestHeader(name = "Authorization") String bearer,
                                     @RequestBody PasswordChangeRequest request) {
        String token = null;
        if (hasText(bearer) && bearer.startsWith("Bearer ")) {
            token = bearer.substring(7);
        }
        if (token != null && jwtProvider.validateAccessToken(token)) {
            String userLogin = jwtProvider.getLoginFromAccessToken(token);
            UserEntity userEntity = userService.findByLoginAndPassword(userLogin, request.getOldPassword());
            if(userEntity != null){
                userEntity.setPassword(request.getNewPassword());
                userService.updateUser(userEntity);
            } else throw new WrongPasswordUserMovesException("Wrong old password");
        } else throw new BadAuthException("No user is authorized");
    }
}
