package com.CalculatorMVCUpload.repository;

import com.CalculatorMVCUpload.entity.PriceListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceListEntityRepository extends JpaRepository<PriceListEntity, Integer> {

    PriceListEntity findTopByOrderByIdDesc();
}
