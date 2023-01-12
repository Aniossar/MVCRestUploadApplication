package com.CalculatorMVCUpload.controller.api;

import com.CalculatorMVCUpload.payload.request.RefreshTokenRequest;
import com.CalculatorMVCUpload.payload.response.AuthentificationResponse;
import com.CalculatorMVCUpload.service.AuthService;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@NoArgsConstructor
@Log
public class AuthSecuredController {

    @Autowired
    private AuthService authService;

    @PostMapping("/refreshToken")
    public ResponseEntity<AuthentificationResponse> getNewRefreshToken(@RequestBody RefreshTokenRequest request){
        AuthentificationResponse authResponse = authService.getNewRefreshToken(request.getRefreshToken());
        return ResponseEntity.ok(authResponse);
    }

}
