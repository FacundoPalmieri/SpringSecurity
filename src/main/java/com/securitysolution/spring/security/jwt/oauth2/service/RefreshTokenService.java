package com.securitysolution.spring.security.jwt.oauth2.service;

import com.securitysolution.spring.security.jwt.oauth2.exception.DataBaseException;
import com.securitysolution.spring.security.jwt.oauth2.exception.TokenInvalidException;
import com.securitysolution.spring.security.jwt.oauth2.model.RefreshToken;
import com.securitysolution.spring.security.jwt.oauth2.repository.IRefreshTokenRepository;
import com.securitysolution.spring.security.jwt.oauth2.service.interfaces.IRefreshTokenService;
import com.securitysolution.spring.security.jwt.oauth2.service.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * @author [Facundo Palmieri]
 */
@Service
public class RefreshTokenService implements IRefreshTokenService {

    @Autowired
    IRefreshTokenRepository refreshTokenRepository;

    @Autowired
    IUserService userService;

    @Override
    public RefreshToken createRefreshToken(String username) {

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(UUID.randomUUID().toString());
        refreshToken.setUser(userService.findByUsername(username));
        refreshToken.setCreatedDate(LocalDateTime.now());
        refreshToken.setExpirationDate(LocalDateTime.now().plusDays(15));
        return refreshTokenRepository.save(refreshToken);
    }


    @Override
    public boolean validateRefreshToken(String refreshToken, String username) {
        try{
            RefreshToken refreshTokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(() -> new TokenInvalidException("[Método: validateRefreshToken]", username));
            return true;
        }catch (DataAccessException | CannotCreateTransactionException e) {
            throw new DataBaseException(e, "userService",0L, username, "blockAccount");
        }

    }

    @Override
    public void deleteRefreshTokenByUsername(String token, String username) {
        try{
            Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByRefreshToken(token);
            if(optionalRefreshToken.isPresent()){
                refreshTokenRepository.delete(optionalRefreshToken.get());
            }else{
                throw new TokenInvalidException("deleteRefreshTokenByUsername",username);
            }

        }catch (DataAccessException | CannotCreateTransactionException e) {
            throw new DataBaseException(e, "userService",0L, username, "blockAccount");
        }
    }


    @Override
    public RefreshToken getRefreshTokenByUsername(String token, String username) {
        try{
            return refreshTokenRepository.findByRefreshToken(token).orElseThrow(() -> new TokenInvalidException("[Método: getRefreshToken]",username));
        }catch (DataAccessException | CannotCreateTransactionException e) {
            throw new DataBaseException(e, "userService",0L, username, "blockAccount");
        }
    }
}
