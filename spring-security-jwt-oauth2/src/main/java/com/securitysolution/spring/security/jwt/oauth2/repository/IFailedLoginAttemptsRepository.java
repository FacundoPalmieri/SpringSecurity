package com.securitysolution.spring.security.jwt.oauth2.repository;

import com.securitysolution.spring.security.jwt.oauth2.model.FailedLoginAttempts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IFailedLoginAttemptsRepository extends JpaRepository<FailedLoginAttempts, Long> {

    @Query("SELECT f.value FROM FailedLoginAttempts f")
    Integer findFirst();

    @Modifying
    @Query("UPDATE FailedLoginAttempts f SET f.value = :value")
    void update(@Param("value") Integer value);
}


