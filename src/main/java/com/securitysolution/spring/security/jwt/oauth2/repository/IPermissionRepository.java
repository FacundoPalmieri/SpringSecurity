package com.securitysolution.spring.security.jwt.oauth2.repository;

import com.securitysolution.spring.security.jwt.oauth2.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPermissionRepository extends JpaRepository<Permission, Long> {

}
