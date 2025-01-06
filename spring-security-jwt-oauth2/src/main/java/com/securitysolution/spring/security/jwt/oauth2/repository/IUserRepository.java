package com.securitysolution.spring.security.jwt.oauth2.repository;

import com.securitysolution.spring.security.jwt.oauth2.model.UserSec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<UserSec, Long> {

    //Crea la sentencia en base al nombre en inglés del método
    //Tmb se puede hacer mediante Query pero en este caso no es necesario
    Optional<UserSec> findUserEntityByUsername(String username);
    UserSec findByResetPasswordToken(String token);

    @Query("SELECT failedLoginAttempts  FROM UserSec WHERE username = :username")
    Integer findFailedLoginAttemptsByUsername(@Param("username") String username);


}
