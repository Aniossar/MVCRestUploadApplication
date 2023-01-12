package com.CalculatorMVCUpload.controller.api;

import com.CalculatorMVCUpload.configuration.jwt.JwtProvider;
import com.CalculatorMVCUpload.entity.UserEntity;
import com.CalculatorMVCUpload.exception.BadAuthException;
import com.CalculatorMVCUpload.exception.WrongPasswordUserMovesException;
import com.CalculatorMVCUpload.payload.request.PasswordChangeRequest;
import com.CalculatorMVCUpload.service.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/api")
@Log
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProvider jwtProvider;

    @DeleteMapping("/deleteUser/{login}")
    @RolesAllowed("ROLE_ADMIN")
    public void deleteUser(@PathVariable String login) {
        userService.deleteUser(login);
    }


    @PutMapping("/changeOwnPassword")
    public void changePasswordByUser(@RequestHeader(name = "Authorization") String bearer,
                                     @RequestBody PasswordChangeRequest request) {
        String userLogin = jwtProvider.getLoginFromBearer(bearer);
        if (userLogin != null) {
            UserEntity userEntity = userService.findByLoginAndPassword(userLogin, request.getOldPassword());
            if (userEntity != null) {
                userEntity.setPassword(request.getNewPassword());
                userService.updateUser(userEntity);
            } else throw new WrongPasswordUserMovesException("Wrong old password");
        } else throw new BadAuthException("No user is authorized");
    }
}
