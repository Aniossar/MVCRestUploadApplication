package com.CalculatorMVCUpload.controller.api;

import com.CalculatorMVCUpload.configuration.jwt.JwtProvider;
import com.CalculatorMVCUpload.entity.users.RoleEntity;
import com.CalculatorMVCUpload.entity.users.UserEntity;
import com.CalculatorMVCUpload.exception.ExistingLoginEmailRegisterException;
import com.CalculatorMVCUpload.exception.IncorrectPayloadException;
import com.CalculatorMVCUpload.payload.request.SingleMessageRequest;
import com.CalculatorMVCUpload.payload.request.users.UserEditRequest;
import com.CalculatorMVCUpload.payload.response.UserInfoResponse;
import com.CalculatorMVCUpload.payload.response.UserListResponse;
import com.CalculatorMVCUpload.repository.RoleEntityRepository;
import com.CalculatorMVCUpload.service.users.UserManagementService;
import com.CalculatorMVCUpload.service.users.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Optional;

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
    @RolesAllowed({"ROLE_ADMIN", "ROLE_MODERATOR"})
    public void deleteUser(@RequestHeader(name = "Authorization") String bearer,
                           @RequestBody SingleMessageRequest request) {

        String token = jwtProvider.getTokenFromBearer(bearer);
        String roleFromToken = jwtProvider.getRoleFromAccessToken(token);
        String userLoginToDelete = request.getMessage();
        UserEntity userToDelete = userService.findByLogin(userLoginToDelete);

        if (userToDelete != null && userManagementService.isFirstUserCoolerOrEqualThanSecond(roleFromToken,
                userToDelete.getRoleEntity().getName())) {
            userService.deleteUser(userLoginToDelete);
        } else throw new IncorrectPayloadException("Bad user change request");
    }

    @GetMapping("/getUser/{id}")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_MODERATOR"})
    public UserInfoResponse getUser(@PathVariable int id) {
        Optional<UserEntity> userEntity = userService.findById(id);
        UserInfoResponse userInfoResponse = null;
        if (userEntity.isPresent()) {
            userInfoResponse = userManagementService.transferUserEntityToUserInfoResponse(userEntity.get());
        }
        return userInfoResponse;
    }

    @GetMapping("/getAllUsers")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_MODERATOR"})
    public List<UserListResponse> getAllUsers() {
        List<UserEntity> allUsers = userManagementService.getAllUsers();
        List<UserListResponse> userListResponses = userManagementService.transferUserEntitiesToUserListResponse(allUsers);
        return userListResponses;
    }


    @PutMapping("/editUser")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_MODERATOR"})
    public void editUser(@RequestHeader(name = "Authorization") String bearer,
                         @RequestBody UserEditRequest request) {

        String token = jwtProvider.getTokenFromBearer(bearer);
        String roleFromToken = jwtProvider.getRoleFromAccessToken(token);
        String userLoginToEdit = request.getLogin();
        UserEntity userToEdit = userService.findByLogin(userLoginToEdit);

        if (userToEdit != null && userManagementService.isFirstUserCoolerOrEqualThanSecond(roleFromToken,
                userToEdit.getRoleEntity().getName())) {
            if (request.getEmail() != null) {
                if(userService.findByEmail(request.getEmail()) == null){
                    userToEdit.setEmail(request.getEmail());
                } else throw new ExistingLoginEmailRegisterException("This email is already registered");
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
            if (request.getNewRole() != null) {
                RoleEntity userRole = roleEntityRepository.findByName("ROLE_" + request.getNewRole());
                userToEdit.setRoleEntity(userRole);
            }
            if (request.getEnabled() != null) {
                userToEdit.setEnabled(request.getEnabled());
            }

            userService.updateUser(userToEdit);
        } else throw new IncorrectPayloadException("Bad user change request");
    }

}