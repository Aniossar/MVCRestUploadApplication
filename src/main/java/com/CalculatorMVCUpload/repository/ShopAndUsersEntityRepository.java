package com.CalculatorMVCUpload.repository;

import com.CalculatorMVCUpload.entity.users.ShopAndUsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShopAndUsersEntityRepository extends JpaRepository<ShopAndUsersEntity, Integer> {

    List<ShopAndUsersEntity> findAllByShop_Id(int shopId);

    ShopAndUsersEntity findByShop_IdAndUser_Id(int shopId, int userId);

    ShopAndUsersEntity findByUser_Id(int userId);
}
