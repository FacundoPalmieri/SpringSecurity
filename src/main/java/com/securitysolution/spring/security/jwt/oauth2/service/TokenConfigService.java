package com.securitysolution.spring.security.jwt.oauth2.service;

import com.securitysolution.spring.security.jwt.oauth2.exception.DataBaseException;
import com.securitysolution.spring.security.jwt.oauth2.enums.LogLevel;
import com.securitysolution.spring.security.jwt.oauth2.exception.NotFoundException;
import com.securitysolution.spring.security.jwt.oauth2.model.TokenConfig;
import com.securitysolution.spring.security.jwt.oauth2.repository.ITokenRepository;
import com.securitysolution.spring.security.jwt.oauth2.service.interfaces.ITokenConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


/**
 * Servicio encargado de la gestión de tokens en la aplicación.
 * <p>
 * Proporciona métodos para obtener y actualizar la fecha de expiración del token
 * almacenado en la base de datos. Maneja errores de acceso a datos y transacciones,
 * lanzando excepciones específicas en caso de fallos.
 * </p>
 *
 * <p>Este servicio implementa la interfaz {@link ITokenConfigService}.</p>
 */
@Service
public class TokenConfigService implements ITokenConfigService {
    @Autowired
    private ITokenRepository tokenRepository;


    /**
     * Obtiene la fecha de expiración del token almacenado en la base de datos.
     * <p>
     * Este método consulta el repositorio de tokens para recuperar la fecha de expiración
     * del token disponible. En caso de error de acceso a la base de datos o de transacción,
     * se lanza una excepción {@link DataBaseException}.
     * </p>
     *
     * @return Un {@link Long} que representa la fecha de expiración del token.
     * @throws DataBaseException Si ocurre un error de acceso a la base de datos o de transacción.
     * @throws NotFoundException Si no encuentra valor.
     */
    @Override
    public Long getExpiration() {
        try{
            Optional<TokenConfig> optional = tokenRepository.findFirstByOrderByIdAsc();
            if(optional.isPresent()){
                return optional.get().getExpiration();
            }
            throw new NotFoundException("","exception.tokenConfigNotFoundException.user",null,"exception.tokenConfigNotFoundException.log",null,"","Token Config Service","getExpiration", LogLevel.ERROR);
        }catch (DataAccessException | CannotCreateTransactionException e) {
            throw new DataBaseException(e, "TokenConfigService", 1L, "", "getExpiration");
        }
    }


    /**
     * Actualiza la fecha de expiración del token en la base de datos.
     * <p>
     * Este método modifica la expiración del token almacenado utilizando el repositorio.
     * La operación se ejecuta dentro de una transacción.
     * </p>
     *
     * @param milliseconds El nuevo tiempo de expiración en milisegundos.
     * @throws DataBaseException Si ocurre un error de acceso a la base de datos o de transacción.
     */
    @Transactional
    @Override
    public int updateExpiration(Long milliseconds) {
        try{
           return tokenRepository.update(milliseconds);

        }catch (DataAccessException | CannotCreateTransactionException e) {
            throw new DataBaseException(e, "TokenConfigService", 1L, "", "updateExpiration");
        }
    }
}
