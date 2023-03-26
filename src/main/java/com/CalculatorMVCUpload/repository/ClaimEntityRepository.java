package com.CalculatorMVCUpload.repository;

import com.CalculatorMVCUpload.entity.ClaimEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClaimEntityRepository extends JpaRepository<ClaimEntity, Integer> {

    @Query(value = "SELECT * FROM claims_table c WHERE c.is_Solved = false", nativeQuery = true)
    List<ClaimEntity> selectNotSolvedClaims();

}
