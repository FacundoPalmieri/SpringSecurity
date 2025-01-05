-- Creamos Permisos--
Insert into permissions (permission_name) values ('Create');
Insert into permissions (permission_name) values ('Read');
Insert into permissions (permission_name) values ('Update');
Insert into permissions (permission_name) values ('Delete');

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
insert into user_roles (user_id, role_id) values(1,2);


