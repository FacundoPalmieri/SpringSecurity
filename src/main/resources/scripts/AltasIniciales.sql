-- Creamos Permisos--
Insert into permisos (permission_name) values ('Create');
Insert into permisos (permission_name) values ('Read');
Insert into permisos (permission_name) values ('Update');
Insert into permisos (permission_name) values ('Delete');

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
    intentos_fallidos,
    fecha_bloqueo,
    fecha_creacion,
    ultima_actualizacion,
    activa,
    no_expirada,
    no_bloqueada,
    credenciales_no_expiradas,
    token_rest
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



-- Messages
INSERT INTO Mensajes (clave, valor, locale) VALUES ('userService.save.passwordNotEquals.user', 'Las password deben coincidir.', 'es_AR');
INSERT INTO Mensajes (clave, valor, locale) VALUES ('userService.save.passwordNotEquals.log', 'No coinciden las password - [UsuarioCreador: {0}] - [UsuarioNuevo: {0}].', 'es_AR');
INSERT INTO Mensajes (clave, valor, locale) VALUES ('userService.save.ok', 'Usuario creado correctamente.', 'es_AR');
INSERT INTO Mensajes (clave, valor, locale) VALUES ('userService.requestResetPassword.success', 'Solicitud de restablecimiento de contraseña procesada. Por favor, revisa tu correo electrónico.', 'es_AR');
INSERT INTO Mensajes (clave, valor, locale) VALUES ('userService.resetPassword.success', 'Se ha restablecido la contraseña exitosamente.', 'es_AR');
INSERT INTO Mensajes (clave, valor, locale) VALUES ('userService.resetPassword.asunto', 'Restablecimiento de Contraseña', 'es_AR');
INSERT INTO Mensajes (clave, valor, locale) VALUES ('userService.resetPassword.error', 'Token de restablecimiento de contraseña no válido o expirado.', 'es_AR');
INSERT INTO Mensajes (clave, valor, locale) VALUES ('userService.dominio', 'https://tu-dominio.com/reset-password', 'es_AR');
INSERT INTO Mensajes (clave, valor, locale) VALUES ('userService.requestResetPassword.mensaje', 'Para restablecer tu contraseña, haz clic en el siguiente enlace: {0} \n\n IMPORTANTE! \n\n - SI USTED NO SOLICITÓ RESTABLECERLA, COMUNIQUESE CON EL ÁREA DE SOPORTE DE MANERA INMEDIATA - .', 'es_AR');
INSERT INTO Mensajes (clave, valor, locale) VALUES ('userService.requestResetPassword.asunto', 'Solicitud de Restablecimiento de Contraseña.', 'es_AR');

INSERT INTO Mensajes (clave, valor, locale) VALUES ('exception.database.log', 'Error al acceder a la base de datos: [ENTIDAD: {0}] - [ID OBJETO {1}] -  [NOMBRE OBJETO:{2}] - [OPERACIÓN:{3}] -  [CAUSA RAÍZ: {4}] - [MENSAJE USUARIO: {5}]', 'es_AR');
INSERT INTO Mensajes (clave, valor, locale) VALUES ('exception.database.user', 'Error al obtener todos los permisos.', 'es_AR');
INSERT INTO Mensajes (clave, valor, locale) VALUES ('exception.validation.log', 'Validación error [Campo: {0}] - [Mensaje: {1}] - [Usuario: - {2}].', 'es_AR');
INSERT INTO Mensajes (clave, valor, locale) VALUES ('exception.generic', 'Ha ocurrido un error inesperado. Por favor, inténtalo de nuevo más tarde.', 'es_AR');
INSERT INTO Mensajes (clave, valor, locale) VALUES ('exception.notFound', 'La URL que has solicitado no existe. Por favor verifica y volvé a intentarlo.', 'es_AR');
INSERT INTO Mensajes (clave, valor, locale) VALUES ('exception.accessDenied.log', 'No tiene permiso para acceder a este recurso [Usuario : {0}] - [URL: {1}] - [Mensaje: {2}].', 'es_AR');
INSERT INTO Mensajes (clave, valor, locale) VALUES ('exception.accessDenied.user', 'No tienes permiso para acceder a este recurso.', 'es_AR');
INSERT INTO Mensajes (clave, valor, locale) VALUES ('exception.validateToken.log', 'Token inválido para el usuario [{0}] - [IP:{1}].', 'es_AR');
INSERT INTO Mensajes (clave, valor, locale) VALUES ('exception.validateToken.user', 'Token inválido.', 'es_AR');
INSERT INTO Mensajes (clave, valor, locale) VALUES ('exception.expiredToken.log', 'Token expirado [USUARIO: {0}] - [IP:{1}].', 'es_AR');
INSERT INTO Mensajes (clave, valor, locale) VALUES ('exception.expiredToken.user', 'Token expirado.', 'es_AR');
INSERT INTO Mensajes (clave, valor, locale) VALUES ('exception.badCredentials.log', 'Intento de inicio de sesión fallido - [Usuario: {0}] [IP:{1}].', 'es_AR');
INSERT INTO Mensajes (clave, valor, locale) VALUES ('exception.badCredentials.user', 'Usuario o contraseña incorrecta.', 'es_AR');
INSERT INTO Mensajes (clave, valor, locale) VALUES ('exception.usernameNotFound.log', 'UserNameNotFoundException: [ Usuario {0} inexistente o dado de baja.]', 'es_AR');
INSERT INTO Mensajes (clave, valor, locale) VALUES ('exception.usernameNotFound.user', 'Usuario inexistente.', 'es_AR');
INSERT INTO Mensajes (clave, valor, locale) VALUES ('exception.resourceNotFoundException.user', 'No se encontró el ID: {0}', 'es_AR');
INSERT INTO Mensajes (clave, valor, locale) VALUES ('exception.blockAccount.log', 'Cuenta Bloqueada: [ID USER:{0} - [USERNAME:{1}].', 'es_AR');
INSERT INTO Mensajes (clave, valor, locale) VALUES ('exception.blockAccount.user', 'Su cuenta se encuentra bloqueada. Por favor, restablezca la password para ingresar.', 'es_AR');

INSERT INTO Mensajes (clave, valor, locale) VALUES ('userSecDTO.username.empty', 'El username no puede estar vacío.', 'es_AR');
INSERT INTO Mensajes (clave, valor, locale) VALUES ('userSecDTO.username.email', 'El username debe ser un correo válido.', 'es_AR');
INSERT INTO Mensajes (clave, valor, locale) VALUES ('userSecDTO.password.empty', 'La password no puede estar vacía.', 'es_AR');
INSERT INTO Mensajes (clave, valor, locale) VALUES ('userSecDTO.password.min', 'La password debe contener al menos 10 caracteres.', 'es_AR');
INSERT INTO Mensajes (clave, valor, locale) VALUES ('userSecDTO.password.pattern', 'La password debe contener al menos un carácter especial, una mayúscula y un número.', 'es_AR');
INSERT INTO Mensajes (clave, valor, locale) VALUES ('userSecDTO.password.role', 'El usuario debe asignarse al menos 1 Rol.', 'es_AR');

INSERT INTO Mensajes (clave, valor, locale) VALUES ('config.getMessage.ok', 'Listado de configuraciones correcto.', 'es_AR');
INSERT INTO Mensajes (clave, valor, locale) VALUES ('config.updateMessage.ok', 'Actualización correcta.', 'es_AR');
INSERT INTO Mensajes (clave, valor, locale) VALUES ('config.getAttempts.ok', 'Registro recuperado.', 'es_AR');
INSERT INTO Mensajes (clave, valor, locale) VALUES ('config.updateAttempts.ok', 'Se actualizaron los intentos de sessión a: {0}.', 'es_AR');
INSERT INTO Mensajes (clave, valor, locale) VALUES ('config.getExpirationToken.ok', 'Registro recuperado.', 'es_AR');
INSERT INTO Mensajes (clave, valor, locale) VALUES ('config.updateExpirationToken.ok', 'Se actualizó el tiempo de expiración del token a {0} minutos.', 'es_AR');



-- Intentos fallidos
insert into intentos_fallidos (id,valor) values(1,3);


-- Expiración Token
insert into token_config (id,expiracion) values(1,60000);