package com.CalculatorMVCUpload.repository;

import com.CalculatorMVCUpload.entity.users.ManagerAndUsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManagerAndUsersEntityRepository extends JpaRepository<ManagerAndUsersEntity, Integer> {

    List<ManagerAndUsersEntity> findAllByKeyManager_Id(Integer managerId);

    ManagerAndUsersEntity findByKeyManager_IdAndUserId(int managerId, int userId);

    ManagerAndUsersEntity findByUser_Id(int userId);
}
