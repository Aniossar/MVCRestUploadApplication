package com.CalculatorMVCUpload.service.users;

import com.CalculatorMVCUpload.entity.users.ShopAndUsersEntity;
import com.CalculatorMVCUpload.entity.users.UserEntity;
import com.CalculatorMVCUpload.repository.ShopAndUsersEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ShopUsersService {

    @Autowired
    private ShopAndUsersEntityRepository shopAndUsersEntityRepository;

    @Transactional
    public void saveShopAndUser(ShopAndUsersEntity shopAndUsersEntity) {
        shopAndUsersEntityRepository.save(shopAndUsersEntity);
    }

    @Transactional
    public List<ShopAndUsersEntity> getAllShopsAndUsers() {
        return shopAndUsersEntityRepository.findAll();
    }

    @Transactional
    public void deleteShopAndUserLine(ShopAndUsersEntity shopAndUsersEntity) {
        shopAndUsersEntityRepository.delete(shopAndUsersEntity);
    }

    @Transactional
    public List<ShopAndUsersEntity> getShopLinesViaShopId(int shopId) {
        return shopAndUsersEntityRepository.findAllByShop_Id(shopId);
    }

    @Transactional
    public ShopAndUsersEntity getShopViaUserId(int userId) {
        return shopAndUsersEntityRepository.findByUser_Id(userId);
    }

    public void connectShopAndUser(UserEntity shop, UserEntity user) {
        ShopAndUsersEntity shopAndUsersEntity = shopAndUsersEntityRepository
                .findByShop_IdAndUser_Id(shop.getId(), user.getId());
        if (shopAndUsersEntity != null) {
            return;
        }
        ShopAndUsersEntity byUserId = shopAndUsersEntityRepository.findByUser_Id(user.getId());
        if (byUserId != null) {
            deleteShopAndUserLine(byUserId);
        }
        shopAndUsersEntity = new ShopAndUsersEntity();
        shopAndUsersEntity.setShop(shop);
        shopAndUsersEntity.setUser(user);

        saveShopAndUser(shopAndUsersEntity);
    }
}
