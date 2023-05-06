package com.CalculatorMVCUpload.service.users;

import com.CalculatorMVCUpload.entity.users.ManagerAndUsersEntity;
import com.CalculatorMVCUpload.entity.users.RoleEntity;
import com.CalculatorMVCUpload.entity.users.UserEntity;
import com.CalculatorMVCUpload.exception.ExistingLoginEmailRegisterException;
import com.CalculatorMVCUpload.payload.request.users.UserEditRequest;
import com.CalculatorMVCUpload.payload.response.UserInfoResponse;
import com.CalculatorMVCUpload.payload.response.UserListResponse;
import com.CalculatorMVCUpload.repository.RoleEntityRepository;
import com.CalculatorMVCUpload.repository.UserEntityRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Log
public class UserManagementService {

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private RoleEntityRepository roleEntityRepository;

    @Autowired
    private KeyManagerService keyManagerService;

    @Autowired
    private UserService userService;

    private final Map<String, Integer> userRolesMap;

    @Autowired
    public UserManagementService(RoleEntityRepository roleEntityRepository) {
        List<RoleEntity> roleEntityList = roleEntityRepository.findAll();
        this.userRolesMap = roleEntityList.stream().collect(Collectors.toMap(RoleEntity::getName, RoleEntity::getId));
    }

    public boolean isFirstUserCoolerOrEqualThanSecond(String roleName1, String roleName2) {
        return userRolesMap.get(roleName1) <= userRolesMap.get(roleName2);
    }

    @Transactional
    public List<UserEntity> getAllUsers() {
        return userEntityRepository.findAll();
    }

    public UserEntity editUserFields(UserEntity userEntity, UserEditRequest request) {
        if (request.getEmail() != null) {
            if (userService.findByEmail(request.getEmail()) == null) {
                userEntity.setEmail(request.getEmail());
            } else log.severe("Trying to use existing email");
        }
        if (request.getLogin() != null) {
            if (userService.findByLogin(request.getLogin()) == null) {
                userEntity.setLogin(request.getLogin());
            } else log.severe("Trying to use existing login " + request.getLogin() + " for another user");
        }
        if (request.getFullName() != null) {
            userEntity.setFullName(request.getFullName());
        }
        if (request.getCompanyName() != null) {
            userEntity.setCompanyName(request.getCompanyName());
        }
        if (request.getPhoneNumber() != null) {
            userEntity.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getAddress() != null) {
            userEntity.setAddress(request.getAddress());
        }
        if (request.getCertainPlaceAddress() != null) {
            userEntity.setCertainPlaceAddress(request.getCertainPlaceAddress());
        }
        if (request.getAppAccess() != null) {
            userEntity.setAppAccess(request.getAppAccess());
        }
        if (request.getNewRole() != null) {
            RoleEntity userRole = roleEntityRepository.findByName("ROLE_" + request.getNewRole());
            userEntity.setRoleEntity(userRole);
        }
        if (request.getEnabled() != null) {
            userEntity.setEnabled(request.getEnabled());
        }
        return userEntity;
    }

    public List<UserListResponse> transferUserEntitiesToUserListResponse(List<UserEntity> entityList) {
        List<UserListResponse> userListResponse = new ArrayList<>();
        for (UserEntity user : entityList) {
            UserListResponse userResponse = transferSingleUserEntityToUserResponse(user);
            userListResponse.add(userResponse);
        }
        return userListResponse;
    }

    public UserListResponse transferSingleUserEntityToUserResponse(UserEntity userEntity) {
        UserListResponse userResponse = new UserListResponse();
        userResponse.setId(userEntity.getId());
        userResponse.setLogin(userEntity.getLogin());
        userResponse.setFullName(userEntity.getFullName());
        userResponse.setCompanyName(userEntity.getCompanyName());
        userResponse.setCertainPlaceAddress(userEntity.getCertainPlaceAddress());
        userResponse.setRoleEntity(userEntity.getRoleEntity());
        ManagerAndUsersEntity managerViaUserId = keyManagerService.getManagerViaUserId(userEntity.getId());
        if (managerViaUserId != null) {
            userResponse.setKeyManagerId(managerViaUserId.getKeyManager().getId());
        }
        return userResponse;
    }

    public UserInfoResponse transferUserEntityToUserInfoResponse(UserEntity userEntity) {
        UserInfoResponse userInfoResponse = new UserInfoResponse();
        userInfoResponse.setId(userEntity.getId());
        userInfoResponse.setLogin(userEntity.getLogin());
        userInfoResponse.setEmail(userEntity.getEmail());
        userInfoResponse.setFullName(userEntity.getFullName());
        userInfoResponse.setCompanyName(userEntity.getCompanyName());
        userInfoResponse.setPhoneNumber(userEntity.getPhoneNumber());
        userInfoResponse.setAddress(userEntity.getAddress());
        userInfoResponse.setCertainPlaceAddress(userEntity.getCertainPlaceAddress());
        userInfoResponse.setAppAccess(userEntity.getAppAccess());
        userInfoResponse.setRoleEntity(userEntity.getRoleEntity());
        userInfoResponse.setEnabled(userEntity.isEnabled());
        ManagerAndUsersEntity managerViaUserId = keyManagerService.getManagerViaUserId(userEntity.getId());
        if (managerViaUserId != null) {
            userInfoResponse.setKeyManagerId(managerViaUserId.getKeyManager().getId());
        }
        return userInfoResponse;
    }
}
