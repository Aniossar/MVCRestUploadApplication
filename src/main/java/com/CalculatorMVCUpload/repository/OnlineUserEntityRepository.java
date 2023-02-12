package com.CalculatorMVCUpload.repository;

import com.CalculatorMVCUpload.entity.users.OnlineUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnlineUserEntityRepository extends JpaRepository<OnlineUserEntity, Integer> {

    OnlineUserEntity findByUserId(int id);
}
