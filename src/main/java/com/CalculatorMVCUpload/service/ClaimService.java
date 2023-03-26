package com.CalculatorMVCUpload.service;

import com.CalculatorMVCUpload.entity.ClaimEntity;
import com.CalculatorMVCUpload.repository.ClaimEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClaimService {

    @Autowired
    private ClaimEntityRepository claimEntityRepository;

    @Transactional
    public List<ClaimEntity> getAllClaims() {
        return claimEntityRepository.findAll();
    }

    @Transactional
    public List<ClaimEntity> getAllNotSolvedClaims(){
        return claimEntityRepository.selectNotSolvedClaims();
    }

    @Transactional
    public ClaimEntity getClaimViaId(int id) {
        return claimEntityRepository.getById(id);
    }

    @Transactional
    public void addNewClaim(ClaimEntity claimEntity) {
        claimEntityRepository.saveAndFlush(claimEntity);
    }

    @Transactional
    public void deleteFile(int id) {
        claimEntityRepository.deleteById(id);
    }
}
