package com.CalculatorMVCUpload.controller.api;

import com.CalculatorMVCUpload.configuration.jwt.JwtProvider;
import com.CalculatorMVCUpload.entity.users.UserEntity;
import com.CalculatorMVCUpload.exception.BadAuthException;
import com.CalculatorMVCUpload.exception.WrongPasswordUserMovesException;
import com.CalculatorMVCUpload.payload.request.users.PasswordChangeRequest;
import com.CalculatorMVCUpload.payload.request.users.RefreshTokenRequest;
import com.CalculatorMVCUpload.payload.request.users.UserEditRequest;
import com.CalculatorMVCUpload.payload.response.AuthentificationResponse;
import com.CalculatorMVCUpload.payload.response.UserInfoResponse;
import com.CalculatorMVCUpload.service.users.AuthService;
import com.CalculatorMVCUpload.service.users.UserManagementService;
import com.CalculatorMVCUpload.service.users.UserService;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@NoArgsConstructor
@Log
public class AuthSecuredController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserManagementService userManagementService;

    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/refreshToken")
    public ResponseEntity<AuthentificationResponse> getNewRefreshToken(@RequestBody RefreshTokenRequest request) {
        AuthentificationResponse authResponse = authService.getNewRefreshToken(request.getRefreshToken());
        return ResponseEntity.ok(authResponse);
    }

    @PutMapping("/changeOwnPassword")
    public void changePasswordByUser(@RequestHeader(name = "Authorization") String bearer,
                                     @RequestBody PasswordChangeRequest request) {
        String token = jwtProvider.getTokenFromBearer(bearer);
        int userId = jwtProvider.getIdFromAccessToken(token);
        if (userId != 0) {
            UserEntity userEntity = userService.findByIdAndPassword(userId, request.getOldPassword());
            if (userEntity != null) {
                userEntity.setPassword(request.getNewPassword());
                userService.updateUserPassword(userEntity);
            } else throw new WrongPasswordUserMovesException("Wrong old password");
        } else throw new BadAuthException("No user is authorized");
    }

    @GetMapping("/getUserInfo")
    public UserInfoResponse changePasswordByUser(@RequestHeader(name = "Authorization") String bearer) {
        String token = jwtProvider.getTokenFromBearer(bearer);
        int userId = jwtProvider.getIdFromAccessToken(token);
        if (userId != 0) {
            UserEntity userEntity = userService.findById(userId);
            UserInfoResponse userInfoResponse = userManagementService.transferUserEntityToUserInfoResponse(userEntity);
            return userInfoResponse;
        } else throw new BadAuthException("No user is authorized");
    }

    @PutMapping("/editOwnInfo")
    public void editMyself(@RequestHeader(name = "Authorization") String bearer,
                           @RequestBody UserEditRequest request) {

        String token = jwtProvider.getTokenFromBearer(bearer);
        int userId = jwtProvider.getIdFromAccessToken(token);
        UserEntity userEntity = userService.findById(userId);

        userManagementService.editUserFields(userEntity, request);
        userService.updateUser(userEntity);
    }

}
