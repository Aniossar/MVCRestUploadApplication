package com.CalculatorMVCUpload.service.users;

import com.CalculatorMVCUpload.entity.users.OnlineUserEntity;
import com.CalculatorMVCUpload.repository.OnlineUserEntityRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Log
public class OnlineUserService {

    @Autowired
    private OnlineUserEntityRepository onlineUserEntityRepository;

    @Transactional
    public List<OnlineUserEntity> getAllUsers() {
        return onlineUserEntityRepository.findAll();
    }

    @Transactional
    public OnlineUserEntity getUserViaId(int id) {
        return onlineUserEntityRepository.findByUserId(id);
    }

    @Transactional
    public List<OnlineUserEntity> getAllPingsAfter(Instant pingTime) {
        return onlineUserEntityRepository.findByLastPingTimeAfter(pingTime);
    }

    @Transactional
    public void saveUserLine(OnlineUserEntity onlineUserEntity) {
        onlineUserEntityRepository.saveAndFlush(onlineUserEntity);
    }

    @Transactional
    public void deleteUserLine(OnlineUserEntity onlineUserEntity) {
        onlineUserEntityRepository.delete(onlineUserEntity);
    }
}
