package com.CalculatorMVCUpload.repository;

import com.CalculatorMVCUpload.entity.StatisticsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface StatisticsEntityRepository extends JpaRepository<StatisticsEntity, Integer> {

    @Query(value = "SELECT * FROM statistics_table a WHERE a.time_slice >= :timeFrom AND a.time_slice < :timeTo"
            , nativeQuery = true)
    List<StatisticsEntity> selectByPeriodOfTime(@Param("timeFrom") Instant timeFrom,
                                                @Param("timeTo") Instant timeTo);
}
