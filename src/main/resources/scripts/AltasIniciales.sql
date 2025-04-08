-- Creamos Permisos--
Insert into permissions (permission,name) values ('Create', 'Create');
Insert into permissions (permission,name) values ('Read', 'Read');
Insert into permissions (permission,name) values ('Update','Update');
Insert into permissions (permission,name) values ('Delete', 'Delete');

-- Creamos Roles --
Insert into roles (role) values('USER');
Insert into roles (role) values('ADMIN');
Insert into roles (role) values ('DEV');


-- Asociamos Roles y permisos-
insert into roles_permissions (role_id, permission_id) values(1,2);
insert into roles_permissions (role_id, permission_id) values(2,1);
insert into roles_permissions (role_id, permission_id) values(2,2);
insert into roles_permissions (role_id, permission_id) values(2,3);
insert into roles_permissions (role_id, permission_id) values(2,4);
insert into roles_permissions (role_id, permission_id) values(3,2);

-- Creamos Usuarios --
INSERT INTO users (
    username,
    password,
    failed_login_attempts,
    locktime,
    creation_Date_Time,
    last_Update_Date_Time,
    enabled,
    account_not_expired,
    account_not_Locked,
    credential_not_expired,
    reset_password_token
)
VALUES(
    'palmierifacundo@gmail.com',
    '$2a$12$UkfZXXN8LK6UaDKMc1rvBunpg7tmu4fkscm9O1EclaQCdCF8JyddK',  -- '$Facundo12345678',
    0, -- failedLoginAttempts
    NULL, -- locktime
    '2025-01-03 00:00:00', -- creationDateTime (puedes ajustar la fecha y hora según sea necesario)
    '2025-01-03 00:00:00', -- lastUpdateDateTime (puedes ajustar la fecha y hora según sea necesario)
    true, -- enabled
    true, -- accountNotExpired (no_expirada)
    true, -- accountNotLocked (no_bloqueada)
    true, -- credentialNotExpired (credenciales_noexpiradas)
    NULL  -- resetPasswordToken (token_rest)
);

-- Asociamos Usuarios y roles
insert into user_roles (user_id, role_id) values(1,3);
insert into user_roles (user_id, role_id) values(1,2);



-- Messages
INSERT INTO message_config (message_key, value, locale) VALUES ('userService.save.passwordNotEquals.user', 'Las password deben coincidir.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('userService.save.passwordNotEquals.log', 'No coinciden las password - [UsuarioCreador: {0}] - [UsuarioNuevo: {0}].', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('userService.save.ok', 'Usuario creado correctamente.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('userService.update.ok', 'Usuario actualizado correctamente.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('userService.findAll.ok', 'Usuarios recuperados correctamente.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('userService.findById.ok.user', 'Usuario encontrado correctamente.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('userService.findById.error.log', '[Clase: {0}] - [Método: {1}] - [Mensaje: Usuario con id {2} No encontrado.]', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('userService.findById.error.user', 'Usuario no encontrado.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('userService.requestResetPassword.success', 'Solicitud de restablecimiento de contraseña procesada. Por favor, revisa tu correo electrónico.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('userService.resetPassword.success', 'Se ha restablecido la contraseña exitosamente.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('userService.resetPassword.asunto', 'Restablecimiento de Contraseña', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('userService.resetPassword.error', 'Token de restablecimiento de contraseña no válido o expirado.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('userService.dominio', 'https://tu-dominio.com/reset-password', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('userService.requestResetPassword.mensaje', 'Para restablecer tu contraseña, haz clic en el siguiente enlace: {0} \n\n IMPORTANTE! \n\n - SI USTED NO SOLICITÓ RESTABLECERLA, COMUNIQUESE CON EL ÁREA DE SOPORTE DE MANERA INMEDIATA - .', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('userService.requestResetPassword.asunto', 'Solicitud de Restablecimiento de Contraseña.', 'es_AR');

INSERT INTO message_config (message_key, value, locale) VALUES ('userDetailServiceImpl.refreshToken.invalidCode', 'Código inválido.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('userDetailServiceImpl.refreshToken.refreshTokenExpired', 'Refresh Token Expirado.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('userDetailServiceImpl.refreshToken.ok', 'Refresh Token actualizado y jwt creado.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('userDetailServiceImpl.logout.ok', 'Sesión cerrada correctamente.', 'es_AR');


INSERT INTO message_config (message_key, value, locale) VALUES ('roleService.save.ok', 'Rol creado correctamente.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('roleService.update.ok', 'Rol actualizado correctamente.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('roleService.getAll.user.ok', 'Roles encontrado correctamente.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('roleService.getById.user.ok', 'Rol encontrado correctamente.', 'es_AR');


INSERT INTO message_config (message_key, value, locale) VALUES ('permissionService.getAll.ok', 'Permisos recuperados correctamente.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('permissionService.getById.ok', 'Permiso encontrado correctamente.', 'es_AR');

INSERT INTO message_config (message_key, value, locale) VALUES ('exception.database.log', 'Error al acceder a la base de datos: [ENTIDAD: {0}] - [ID OBJETO {1}] -  [NOMBRE OBJETO:{2}] - [OPERACIÓN:{3}] -  [CAUSA RAÍZ: {4}] - [MENSAJE USUARIO: {5}]', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('exception.database.user', 'Ocurrió un error al procesar la solicitud en la base de datos. Por favor, intente nuevamente más tarde.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('exception.validation.log', 'Validación error [Campo: {0}] - [Mensaje: {1}] - [Usuario: - {2}].', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('exception.generic', 'Ha ocurrido un error inesperado. Por favor, inténtalo de nuevo más tarde.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('exception.notFound', 'La URL que has solicitado no existe. Por favor verifica y volvé a intentarlo.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('exception.authenticationRequired.log', 'Usuario no autenticado [Usuario : {0}] - [URL: {1}] - [Mensaje: {2}].', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('exception.authenticationRequired.user', 'Debes autenticarte primero antes de ingresar a esta página.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('exception.accessDenied.log', 'No tiene permiso para acceder a este recurso [Usuario : {0}] - [URL: {1}] - [Mensaje: {2}].', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('exception.accessDenied.user', 'No tienes permiso para acceder a este recurso.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('exception.validateToken.log', 'Token inválido para el usuario [{0}] - [IP:{1}].', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('exception.validateToken.user', 'Token inválido.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('exception.expiredToken.log', 'Token expirado [USUARIO: {0}] - [IP:{1}].', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('exception.expiredToken.user', 'Token expirado.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('exception.badCredentials.log', 'Intento de inicio de sesión fallido - [Usuario: {0}] [IP:{1}].', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('exception.badCredentials.user', 'Usuario o contraseña incorrecta.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('exception.usernameExisting.log ', 'Usuario [{0}] ya se encuentra registrado.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('exception.usernameExisting.user ', 'El Usuario {0} ya se encuentra registrado.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('exception.usernameNotFound.log', 'UserNameNotFoundException: [ Usuario {0} inexistente o dado de baja.]', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('exception.usernameNotFound.user', 'Usuario inexistente.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('exception.resourceNotFoundException.user', 'No se encontró el ID: {0}', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('exception.blockAccount.log', 'Cuenta Bloqueada: [ID USER:{0} - [USERNAME:{1}].', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('exception.blockAccount.user', 'Su cuenta se encuentra bloqueada. Por favor, restablezca la password para ingresar.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('exception.roleNotFound.user', 'Rol no encontrado.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('exception.roleNotFound.log ', '[Clase: {0}] - [Método: {1}] - [Mensaje: Rol con id {2} No encontrado].', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('exception.roleNotFoundUserCreationException.log', '[Clase: {0}] - [Método: {1}] - [Mensaje: Usuario NO CREADO -  Rol con id {2} No encontrado.]', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('exception.roleNotFoundUserCreationException.user', 'No se pudo crear el usuario. No se encontró el Rol.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('exception.roleExisting.log', '[Clase: {0}] - [Método: {1}] - [Mensaje: Rol {2} ya se encuentra registrado.]', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('exception.roleExisting.user', 'El Rol {0} ya se encuentra registrado.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('exception.permissionNotFoundRoleCreationException.log', '[Clase: {0}] - [Método: {1}] - [Mensaje: ROL NO CREADO - Permiso con id {2} No encontrado.]', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('exception.permissionNotFoundRoleCreationException.user', 'Rol no creado, no se pudo asociar el permiso.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('exception.permissionNotFound.user', 'Permiso no encontrado.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('exception.permissionNotFound.log', '[Clase: {0}] - [Método: {1}] - [Mensaje: Permiso id {2} No encontrado.]', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('exception.messageNotFound.user', 'Mensaje no encontrado.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('exception.messageNotFound.log', '[Clase: {0}] - [Método: {1}] - [Detalle: Mensaje id {2} No encontrado.]', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('exception.validateSelfUpdate.log', '[Clase: {0}] - [Método: {1}] - [Detalle: El usuario con ID {2} a actualizar es el mismo que el autenticado.]', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('exception.validateSelfUpdate.user', 'La actualización del propio usuario no está permitida.', 'es_AR');

INSERT INTO message_config (message_key, value, locale) VALUES ('exception.save.validateNotDevRole.log', '[Clase: {0}] - [Método: {1}] - [Detalle: No puede actualizarse al usuario con ID {2} al Rol de tipo DEV.]', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('exception.save.validateNotDevRole.user', 'La actualización a un Rol de tipo -Desarrollador- no está permitida.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('exception.update.validateNotDevRole.log', '[Clase: {0}] - [Método: {1}] - [Detalle: No puede actualizarse al usuario con ID {2} al Rol de tipo DEV.]', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('exception.update.validateNotDevRole.user', 'La actualización a un Rol de tipo -Desarrollador- no está permitida.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('exception.validateUpdateUser.log', '[Clase: {0}] - [Método: {1}] - [Detalle: Usuario ID{2} - El value a actualizar es igual al que ya cuenta.]', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('exception.validateUpdateUser.user ', 'El value proporcionado es igual al actual, no se realizaron cambios.', 'es_AR');

INSERT INTO message_config (message_key, value, locale) VALUES ('exception.tokenConfigNotFoundException.log', 'No se encuentra registro en la base de datos para actualizar el value [Clase: {0}] - [Método: {1}]', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('exception.tokenConfigNotFoundException.user', 'No se encuentra registro en la base de datos para actualizar el value.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('exception.refreshTokenConfigNotFoundException.log', 'No se encuentra registro en la base de datos para actualizar o recuperar el value [Clase: {0}] - [Método: {1}]', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('exception.refreshTokenConfigNotFoundException.user', 'No se encuentra registro en la base de datos para actualizar o recuperar el value.', 'es_AR');

INSERT INTO message_config (message_key, value, locale) VALUES ('exception.refreshToken.log', 'Refresh Token no encontrado, inválido o expirado. [Clase: {0}] - [Método:{1}] - [IdUser: {2}]- [Mensaje:{3}]', 'es_AR');


INSERT INTO message_config (message_key, value, locale) VALUES ('userSecCreateDTO.username.empty', 'El username no puede estar vacío.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('userSecCreateDTO.username.email', 'El username debe ser un correo válido.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('userSecCreateDTO.password.empty', 'La password no puede estar vacía.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('userSecCreateDTO.password.min', 'La password debe contener al menos 10 caracteres.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('userSecCreateDTO.password.pattern', 'La password debe contener al menos un carácter especial, una mayúscula y un número.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('userSecCreateDTO.role.empty', 'El usuario debe asignarse al menos 1 Rol.', 'es_AR');

INSERT INTO message_config (message_key, value, locale) VALUES ('userSecUpdateDTO.id.empty', 'El ID no puede estar vacío.', 'es_AR');

INSERT INTO message_config (message_key, value, locale) VALUES ('roleDTO.role.empty', 'El nombre del rol no puede estar vacío.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('roleDTO.permission.empty', 'El rol debe contar con al menos un permiso.', 'es_AR');


INSERT INTO message_config (message_key, value, locale) VALUES ('permissionDTO.name.empty', 'El nombre del permiso no puede estar vacío.', 'es_AR');

INSERT INTO message_config (message_key, value, locale) VALUES ('tokenDTO.expiration.empty', 'El tiempo de expiración no puede estar vacío.', 'es_AR');

INSERT INTO message_config (message_key, value, locale) VALUES ('FailedLoginAttemptsDTO.value.empty', 'La cantidad de inicios de sesión no puede estar vacía.', 'es_AR');

INSERT INTO message_config (message_key, value, locale) VALUES ('refreshTokenRequestDTO.refreshEmpty', 'El campo RefreshToken está vacío.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('refreshTokenRequestDTO.userIdEmpty', 'El ID del usuario no puede ser nulo.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('refreshTokenRequestDTO.usernameEmpty', 'El username no puede ser nulo.', 'es_AR');

INSERT INTO message_config (message_key, value, locale) VALUES ('refreshTokenConfigDTO.invalidExpiration', 'El tiempo de expiración debe ser mayor o igual a 1.', 'es_AR');

INSERT INTO message_config (message_key, value, locale) VALUES ('refreshTokenService.deleteRefreshToken', 'Error al eliminar el Refresh Token', 'es_AR');


INSERT INTO message_config (message_key, value, locale) VALUES ('config.getMessage.ok', 'Listado de configuraciones correcto.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('config.updateMessage.ok', 'Actualización correcta.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('config.getAttempts.ok', 'Registro recuperado.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('config.updateAttempts.ok', 'Se actualizaron los intentos de sessión a: {0}.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('config.getExpirationToken.ok', 'Registro recuperado.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('config.updateExpirationToken.ok', 'Se actualizó el tiempo de expiración del token a {0} minutos.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('config.getExpirationRefreshToken.ok', 'Registro recuperado.', 'es_AR');
INSERT INTO message_config (message_key, value, locale) VALUES ('config.updateExpirationRefreshToken.ok', 'Se actualizó el tiempo de expiración del Refresh token a {0} días.', 'es_AR');



-- Intentos fallidos
insert into failed_login_config (id,value) values(1,3);


-- Expiración Token
insert into token_config (id,expiration) values(1,3600000);

-- Expiración Refresh Token
insert into Refresh_Token_Config(id,expiration) values (1, 14);