package com.CalculatorMVCUpload.service.users;

import com.CalculatorMVCUpload.entity.users.RoleEntity;
import com.CalculatorMVCUpload.repository.RoleEntityRepository;
import com.CalculatorMVCUpload.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
