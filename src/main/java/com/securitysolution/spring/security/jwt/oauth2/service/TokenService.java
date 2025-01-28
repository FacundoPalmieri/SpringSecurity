package com.securitysolution.spring.security.jwt.oauth2.service;

import com.securitysolution.spring.security.jwt.oauth2.dto.TokenConfigDTO;
import com.securitysolution.spring.security.jwt.oauth2.exception.DataBaseException;
import com.securitysolution.spring.security.jwt.oauth2.repository.ITokenRepository;
import com.securitysolution.spring.security.jwt.oauth2.service.interfaces.ITokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TokenService implements ITokenService {
    @Autowired
    private ITokenRepository tokenRepository;

    @Override
    public Long getExpiration() {
        try{
           return tokenRepository.findFirst();
        }catch (DataAccessException | CannotCreateTransactionException e) {
            throw new DataBaseException(e, "TokenService", 1L, "", "getExpiration");
        }
    }

    @Transactional
    @Override
    public void updateExpiration(Long milliseconds) {
        try{
            tokenRepository.update(milliseconds);
        }catch (DataAccessException | CannotCreateTransactionException e) {
            throw new DataBaseException(e, "TokenService", 1L, "", "updateExpiration");
        }
    }
}
