package com.CalculatorMVCUpload.repository;

import com.CalculatorMVCUpload.entity.CalculatorActivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface CalculatorActivityEntityRepository extends JpaRepository<CalculatorActivityEntity, Integer> {

    @Query(value = "SELECT * FROM users_receipt_summary a " +
            "WHERE a.activity_time > :dateFrom AND a.activity_time < :dateTo " +
            "AND a.company_name LIKE :companyName AND a.certain_place_address LIKE :certainPlaceAddress " +
            "AND a.material_price >= :materialPriceFrom AND a.material_price <= :materialPriceTo " +
            "AND a.add_price >= :addPriceFrom AND a.add_price <= :addPriceTo " +
            "AND a.all_price >= :allPriceFrom AND a.all_price <= :allPriceTo " +
            "AND a.materials LIKE :materials", nativeQuery = true)
    List<CalculatorActivityEntity> selectByFilterFields(@Param("dateFrom") LocalDate dateFrom,
                                                        @Param("dateTo") LocalDate dateTo,
                                                        @Param("companyName") String companyName,
                                                        @Param("certainPlaceAddress") String certainPlaceAddress,
                                                        @Param("materialPriceFrom") Double materialPriceFrom,
                                                        @Param("materialPriceTo") Double materialPriceTo,
                                                        @Param("addPriceFrom") Double addPriceFrom,
                                                        @Param("addPriceTo") Double addPriceTo,
                                                        @Param("allPriceFrom") Double allPriceFrom,
                                                        @Param("allPriceTo") Double allPriceTo,
                                                        @Param("materials") String materials);

    @Query(value = "SELECT * FROM users_receipt_summary a " +
            "WHERE a.activity_time > :timeFrom AND a.activity_time < :timeTo " +
            "AND a.type LIKE :searchingType", nativeQuery = true)
    List<CalculatorActivityEntity> selectByTypeAndTime(@Param("timeFrom") Instant timeFrom,
                                                       @Param("timeTo") Instant timeTo,
                                                       @Param("searchingType") String searchingType);

    List<CalculatorActivityEntity> findAllByType(String type);
}