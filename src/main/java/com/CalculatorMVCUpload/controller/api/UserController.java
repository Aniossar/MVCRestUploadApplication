package com.CalculatorMVCUpload.controller.api;

import com.CalculatorMVCUpload.configuration.jwt.JwtProvider;
import com.CalculatorMVCUpload.entity.users.ManagerAndUsersEntity;
import com.CalculatorMVCUpload.entity.users.RoleEntity;
import com.CalculatorMVCUpload.entity.users.UserEntity;
import com.CalculatorMVCUpload.exception.ExistingLoginEmailRegisterException;
import com.CalculatorMVCUpload.exception.IncorrectPayloadException;
import com.CalculatorMVCUpload.payload.request.users.ConnectTwoUsersRequest;
import com.CalculatorMVCUpload.payload.request.users.UserEditRequest;
import com.CalculatorMVCUpload.payload.response.UserInfoResponse;
import com.CalculatorMVCUpload.payload.response.UserListResponse;
import com.CalculatorMVCUpload.repository.RoleEntityRepository;
import com.CalculatorMVCUpload.service.users.KeyManagerService;
import com.CalculatorMVCUpload.service.users.UserManagementService;
import com.CalculatorMVCUpload.service.users.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private KeyManagerService keyManagerService;

    /*@DeleteMapping("/deleteUser")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_MODERATOR"})
    public void deleteUser(@RequestHeader(name = "Authorization") String bearer,
                           @RequestBody SingleIdRequest request) {

        String token = jwtProvider.getTokenFromBearer(bearer);
        String roleFromToken = jwtProvider.getRoleFromAccessToken(token);
        int userIdToDelete = request.getSomeId();
        UserEntity userToDelete = userService.findById(userIdToDelete);

        if (userToDelete != null && userManagementService.isFirstUserCoolerOrEqualThanSecond(roleFromToken,
                userToDelete.getRoleEntity().getName())) {
            userService.deleteUser(userIdToDelete);
        } else throw new IncorrectPayloadException("Bad user change request");
    }*/

    @GetMapping("/getUser/{id}")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_MODERATOR"})
    public UserInfoResponse getUser(@PathVariable int id) {
        UserEntity userEntity = userService.findById(id);
        UserInfoResponse userInfoResponse = null;
        if (userEntity != null) {
            userInfoResponse = userManagementService.transferUserEntityToUserInfoResponse(userEntity);
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
        int userIdToEdit = request.getUserId();
        UserEntity userToEdit = userService.findById(userIdToEdit);

        if (userToEdit != null && userManagementService.isFirstUserCoolerOrEqualThanSecond(roleFromToken,
                userToEdit.getRoleEntity().getName())) {
            if (request.getEmail() != null) {
                if (userService.findByEmail(request.getEmail()) == null) {
                    userToEdit.setEmail(request.getEmail());
                } else throw new ExistingLoginEmailRegisterException("This email is already registered");
            }
            if (request.getLogin() != null) {
                if (userService.findByLogin(request.getLogin()) == null) {
                    userToEdit.setLogin(request.getLogin());
                } else {
                    log.severe("Trying to use existing login " + request.getLogin() + " for another user");
                    throw new ExistingLoginEmailRegisterException("This login is already registered");
                }
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

    @PostMapping("/connectUserAndManager")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_MODERATOR"})
    public void connectUserWithManager(@RequestBody ConnectTwoUsersRequest request) {
        UserEntity manager = userService.findById(request.getUserIdMain());
        UserEntity user = userService.findById(request.getUserIdNonMain());
        if (manager.getRoleEntity().getName().contains("KEYMANAGER")) {
            keyManagerService.connectUserAndManager(manager, user);
        }
    }

    @GetMapping("/getAllUsersWithoutKeyManagers")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_MODERATOR"})
    public List<UserListResponse> getAllUsersWithoutKeyManagers() {
        List<UserListResponse> resultList = new ArrayList<>();
        List<UserEntity> allUsers = userManagementService.getAllUsers();
        List<ManagerAndUsersEntity> allManagersAndUsers = keyManagerService.getAllManagersAndUsers();
        for (UserEntity userEntity : allUsers) {
            if (!userEntity.getRoleEntity().getName().contains("ADMIN")
                    && !userEntity.getRoleEntity().getName().contains("MODERATOR")
                    && !userEntity.getRoleEntity().getName().contains("KEYMANAGER")) {
                boolean flagKeyManagerAsserts = false;
                for (ManagerAndUsersEntity entity : allManagersAndUsers) {
                    if (entity.getUser().getId() == userEntity.getId()) {
                        flagKeyManagerAsserts = true;
                        break;
                    }
                }
                if (!flagKeyManagerAsserts) {
                    resultList.add(userManagementService.transferSingleUserEntityToUserResponse(userEntity));
                }
            }
        }
        return resultList;
    }

    @GetMapping("/getMyUsers")
    @RolesAllowed({"ROLE_KEYMANAGER"})
    public List<UserListResponse> getAllUsersThatHaveThisManager(@RequestHeader(name = "Authorization") String bearer) {
        String token = jwtProvider.getTokenFromBearer(bearer);
        int idFromAccessToken = jwtProvider.getIdFromAccessToken(token);
        List<UserListResponse> resultList = new ArrayList<>();
        List<ManagerAndUsersEntity> managerViaUserId = keyManagerService.getManagerViaUserId(idFromAccessToken);
        for (ManagerAndUsersEntity entity : managerViaUserId) {
            resultList.add(userManagementService.transferSingleUserEntityToUserResponse(entity.getUser()));
        }
        return resultList;
    }

}