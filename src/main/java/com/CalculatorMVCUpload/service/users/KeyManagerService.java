package com.CalculatorMVCUpload.service.users;

import com.CalculatorMVCUpload.entity.users.ManagerAndUsersEntity;
import com.CalculatorMVCUpload.entity.users.UserEntity;
import com.CalculatorMVCUpload.repository.ManagerAndUsersEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class KeyManagerService {

    @Autowired
    private ManagerAndUsersEntityRepository managerAndUsersEntityRepository;

    @Transactional
    public void saveManagerAndUser(ManagerAndUsersEntity managerAndUsersEntity) {
        managerAndUsersEntityRepository.save(managerAndUsersEntity);
    }

    @Transactional
    public List<ManagerAndUsersEntity> getAllManagersAndUsers() {
        return managerAndUsersEntityRepository.findAll();
    }

    @Transactional
    public void deleteManageAndUserLine(ManagerAndUsersEntity managerAndUsersEntity) {
        managerAndUsersEntityRepository.delete(managerAndUsersEntity);
    }

    @Transactional
    public List<ManagerAndUsersEntity> getManagerLinesViaManagerId(int managerId) {
        return managerAndUsersEntityRepository.findAllByKeyManager_Id(managerId);
    }

    @Transactional
    public ManagerAndUsersEntity getManagerViaUserId(int userId) {
        return managerAndUsersEntityRepository.findByUser_Id(userId);
    }

    public void connectUserAndManager(UserEntity manager, UserEntity user) {
        ManagerAndUsersEntity managerAndUsersEntity = managerAndUsersEntityRepository
                .findByKeyManager_IdAndUserId(manager.getId(), user.getId());
        if (managerAndUsersEntity != null) {
            return;
        }
        ManagerAndUsersEntity byUserId = managerAndUsersEntityRepository.findByUser_Id(user.getId());
        if (byUserId != null) {
            deleteManageAndUserLine(byUserId);
        }
        managerAndUsersEntity = new ManagerAndUsersEntity();
        managerAndUsersEntity.setKeyManager(manager);
        managerAndUsersEntity.setUser(user);

        saveManagerAndUser(managerAndUsersEntity);
    }
}
