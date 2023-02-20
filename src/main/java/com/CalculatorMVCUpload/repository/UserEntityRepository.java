package com.CalculatorMVCUpload.repository;

import com.CalculatorMVCUpload.entity.users.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Integer> {

    UserEntity findByLogin(String login);

    UserEntity findByEmail(String email);

    @Query(value = "SELECT * FROM user_table a WHERE a.registration_time > :timeFrom AND a.registration_time < :timeTo"
            , nativeQuery = true)
    List<UserEntity> selectByRegistrationTime(@Param("timeFrom") Instant timeFrom,
                                              @Param("timeTo") Instant timeTo);
}
