package com.securitysolution.spring.security.jwt.oauth2.service;

import com.securitysolution.spring.security.jwt.oauth2.exception.DataBaseException;
import com.securitysolution.spring.security.jwt.oauth2.repository.IFailedLoginAttemptsRepository;
import com.securitysolution.spring.security.jwt.oauth2.service.interfaces.IFailedLoginAttemptsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FailedLoginAttemptsService implements IFailedLoginAttemptsService {

    @Autowired
    private IFailedLoginAttemptsRepository failedLoginAttemptsRepository;

    @Override
    public Integer get() {
        try{
           return failedLoginAttemptsRepository.findFirst();
        }catch (DataAccessException | CannotCreateTransactionException e) {
            throw new DataBaseException(e, "FailedLoginAttemptsService",1L, "", "getAttempt");
        }
    }

    @Override
    @Transactional
    public void update(Integer failedLoginAttempts) {
        try {
            failedLoginAttemptsRepository.update(failedLoginAttempts);
        }catch (DataAccessException | CannotCreateTransactionException e) {
            throw new DataBaseException(e, "FailedLoginAttemptsService",1L, "", "getAttempt");
        }
    }
}
