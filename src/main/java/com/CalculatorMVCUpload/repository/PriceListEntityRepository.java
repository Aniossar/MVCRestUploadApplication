package com.CalculatorMVCUpload.repository;

import com.CalculatorMVCUpload.entity.PriceListEntity;
import com.CalculatorMVCUpload.entity.UploadedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceListEntityRepository extends JpaRepository<PriceListEntity, Integer> {

    PriceListEntity findTopByOrderByIdDesc();

    PriceListEntity findTopByForClientsOrderByIdDesc(String forClients);

    PriceListEntity findTopByForClientsContainingOrderByIdDesc(String forClients);
}
