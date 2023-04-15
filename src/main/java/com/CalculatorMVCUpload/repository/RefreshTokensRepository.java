package com.CalculatorMVCUpload.repository;

import com.CalculatorMVCUpload.entity.users.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefreshTokensRepository extends JpaRepository<RefreshTokenEntity, Long> {

    @Query(value = "SELECT * FROM user_refresh_table a WHERE a.user_id = :userId ", nativeQuery = true)
    List<RefreshTokenEntity> findAllByUserId(@Param("userId") int userId);

    RefreshTokenEntity findByRefreshToken(String refreshToken);

    //Long deleteById(long id);
}
