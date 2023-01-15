package com.CalculatorMVCUpload.controller.api;

import com.CalculatorMVCUpload.configuration.jwt.JwtProvider;
import com.CalculatorMVCUpload.entity.RoleEntity;
import com.CalculatorMVCUpload.entity.UserEntity;
import com.CalculatorMVCUpload.exception.IncorrectPayloadException;
import com.CalculatorMVCUpload.payload.request.BlockUserRequest;
import com.CalculatorMVCUpload.payload.request.RegistrationRequest;
import com.CalculatorMVCUpload.payload.request.RoleChangeRequest;
import com.CalculatorMVCUpload.payload.request.SingleMessageRequest;
import com.CalculatorMVCUpload.repository.RoleEntityRepository;
import com.CalculatorMVCUpload.service.users.UserManagementService;
import com.CalculatorMVCUpload.service.users.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/api/users")
@Log
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserManagementService userManagementService;

    @Autowired
    private RoleEntityRepository roleEntityRepository;

    @DeleteMapping("/deleteUser")
    public void deleteUser(@RequestHeader(name = "Authorization") String bearer,
                           @RequestBody SingleMessageRequest request) {

        String token = jwtProvider.getTokenFromBearer(bearer);
        String roleFromToken = jwtProvider.getRoleFromAccessToken(token);
        String userLoginToDelete = request.getMessage();
        UserEntity userToDelete = userService.findByLogin(userLoginToDelete);

        if (userToDelete != null && userManagementService.isFirstUserCoolerThanSecond(roleFromToken,
                userToDelete.getRoleEntity().getName())) {
            userService.deleteUser(userLoginToDelete);
        } else throw new IncorrectPayloadException("Bad user change request");
    }

    @PutMapping("/editUser")
    public void editUser(@RequestHeader(name = "Authorization") String bearer,
                         @RequestBody RegistrationRequest request) {

        String token = jwtProvider.getTokenFromBearer(bearer);
        String roleFromToken = jwtProvider.getRoleFromAccessToken(token);
        String userLoginToEdit = request.getLogin();
        UserEntity userToEdit = userService.findByLogin(userLoginToEdit);

        if (userToEdit != null && userManagementService.isFirstUserCoolerThanSecond(roleFromToken,
                userToEdit.getRoleEntity().getName())) {
            if (request.getEmail() != null) {
                userToEdit.setEmail(request.getEmail());
            }
            if (request.getFullName() != null) {
                userToEdit.setFullName(request.getFullName());
            }
            if (request.getCompanyName() != null) {
                userToEdit.setCompanyName(request.getCompanyName());
            }
            if (request.getPhoneNumber() != null) {
                userToEdit.setPhoneNumber(request.getPhoneNumber());
            }
            if (request.getAddress() != null) {
                userToEdit.setAddress(request.getAddress());
            }
            if (request.getCertainPlaceAddress() != null) {
                userToEdit.setCertainPlaceAddress(request.getCertainPlaceAddress());
            }
            if (request.getAppAccess() != null) {
                userToEdit.setAppAccess(request.getAppAccess());
            }

            userService.updateUser(userToEdit);
        } else throw new IncorrectPayloadException("Bad user change request");
    }

    @PutMapping("/editUserRole")
    @RolesAllowed("ROLE_ADMIN")
    public void editUserRole(@RequestHeader(name = "Authorization") String bearer,
                             @RequestBody RoleChangeRequest request) {

        String userLoginToEdit = request.getLogin();
        UserEntity userToEdit = userService.findByLogin(userLoginToEdit);

        if (userToEdit != null) {
            RoleEntity userRole = roleEntityRepository.findByName("ROLE_" + request.getNewRole());
            if(userRole != null){
                userToEdit.setRoleEntity(userRole);
                userService.updateUser(userToEdit);
            } else throw new IncorrectPayloadException("Bad user change request");
        } else throw new IncorrectPayloadException("Bad user change request");
    }

    @PutMapping("/editUserEnabled")
    @RolesAllowed("ROLE_ADMIN")
    public void editUserRole(@RequestHeader(name = "Authorization") String bearer,
                             @RequestBody BlockUserRequest request) {

        String userLoginToEdit = request.getLogin();
        UserEntity userToEdit = userService.findByLogin(userLoginToEdit);

        if (userToEdit != null) {
            userToEdit.setEnabled(request.isStatusEnabled());
            userService.updateUser(userToEdit);
        } else throw new IncorrectPayloadException("Bad user change request");
    }
}