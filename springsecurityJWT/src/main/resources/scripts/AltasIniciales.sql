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
insert into users (username, password, enabled, account_not_expired, account_not_locked, credential_not_expired)
Values('facundo','$2y$10$UQFtK9DZzFlhhsn40NCjd.bB/.PgQaXLGew0mMyGq6tv/RAxJxVW2',true, true, true, true);


-- Asociamos Usuarios y roles
insert into user_roles (user_id, role_id) values(1,2);


