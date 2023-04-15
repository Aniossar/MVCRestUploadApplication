package com.CalculatorMVCUpload.service.users;

import com.CalculatorMVCUpload.entity.users.RefreshTokenEntity;
import com.CalculatorMVCUpload.repository.RefreshTokensRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokensRepository refreshTokensRepository;

    @Autowired
    private EntityManager entityManager;

    @Transactional
    public List<RefreshTokenEntity> getAllUserRefreshTokens(int userId) {
        return refreshTokensRepository.findAllByUserId(userId);
    }

    @Transactional
    public void saveRefreshToken(RefreshTokenEntity refreshToken) {
        refreshTokensRepository.saveAndFlush(refreshToken);
    }

    @Transactional
    public void deleteRefreshToken(RefreshTokenEntity refreshToken) {
        refreshTokensRepository.deleteById(refreshToken.getId());
    }

    @Transactional
    public RefreshTokenEntity getTokenEntityViaToken(String refreshToken) {
        return refreshTokensRepository.findByRefreshToken(refreshToken);
    }

    @Transactional
    public int userIdViaToken(String refreshToken) {
        RefreshTokenEntity byRefreshToken = refreshTokensRepository.findByRefreshToken(refreshToken);
        if (byRefreshToken != null) {
            return byRefreshToken.getUserId();
        }
        return -1;
    }

    public RefreshTokenEntity getOldestToken(List<RefreshTokenEntity> list) {
        RefreshTokenEntity tokenWithOldestId = list.get(0);
        for (RefreshTokenEntity token : list) {
            if (tokenWithOldestId.getId() > token.getId()) tokenWithOldestId = token;
        }
        return tokenWithOldestId;
    }

    public boolean containsToken(List<RefreshTokenEntity> list, String refreshToken) {
        for (RefreshTokenEntity token : list) {
            if (refreshToken.equals(token.getRefreshToken())) return true;
        }
        return false;
    }
}
