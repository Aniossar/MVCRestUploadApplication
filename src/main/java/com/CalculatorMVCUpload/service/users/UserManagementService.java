package com.CalculatorMVCUpload.service.users;

import com.CalculatorMVCUpload.entity.users.RoleEntity;
import com.CalculatorMVCUpload.entity.users.UserEntity;
import com.CalculatorMVCUpload.payload.response.UserInfoResponse;
import com.CalculatorMVCUpload.payload.response.UserListResponse;
import com.CalculatorMVCUpload.repository.RoleEntityRepository;
import com.CalculatorMVCUpload.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserManagementService {

    @Autowired
    private UserEntityRepository userEntityRepository;

    private RoleEntityRepository roleEntityRepository;
    private final Map<String, Integer> userRolesMap;

    @Autowired
    public UserManagementService(RoleEntityRepository roleEntityRepository) {
        List<RoleEntity> roleEntityList = roleEntityRepository.findAll();
        this.userRolesMap = roleEntityList.stream().collect(Collectors.toMap(RoleEntity::getName, RoleEntity::getId));
    }

    public boolean isFirstUserCoolerThanSecond(String roleName1, String roleName2) {
        return userRolesMap.get(roleName1) < userRolesMap.get(roleName2);
    }

    @Transactional
    public List<UserEntity> getAllUsers() {
        return userEntityRepository.findAll();
    }

    public List<UserListResponse> transferUserEntitiesToUserListResponse(List<UserEntity> entityList) {
        List<UserListResponse> userListResponse = new ArrayList<>();
        for (UserEntity user : entityList) {
            UserListResponse userResponse = new UserListResponse();
            userResponse.setId(user.getId());
            userResponse.setLogin(user.getLogin());
            userResponse.setFullName(user.getFullName());
            userResponse.setCompanyName(user.getCompanyName());
            userResponse.setCertainPlaceAddress(user.getCertainPlaceAddress());
            userListResponse.add(userResponse);
        }
        return userListResponse;
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

        return userInfoResponse;
    }
}
