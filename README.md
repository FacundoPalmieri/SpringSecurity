# Spring-security-jwt-oauth2

## Descripción

Este es un proyecto base de seguridad que implementa autenticación y autorización basada en roles utilizando credenciales, JWT y OAuth2. 
El proyecto permite la creación y actualización de usuarios, roles y permisos. Además, incluye funcionalidades como el restablecimiento de contraseñas con envío de correos electrónicos.
Cuenta con endpoints de parametrización disponibles solo para usuarios con rol de "desarrollador", lo que permite actualizar de manera ágil:
-   Mensajes personalizados para usuarios.
-   Logs configurables mediante pares clave-valor.
-   Límites de intentos de inicio de sesión fallidos.
-   Tiempo de expiración del token JWT.


### Características principales:
- **Autenticación y autorización** basadas en roles.
- **Creación y actualización** de usuarios, roles y permisos.
- **Restablecimiento de contraseñas** con envío de correo electrónico.
- **Mensajes personalizables** a usuarios y registros (logs) configurables.
- **Control de intentos de inicio de sesión fallidos** (solo para usuarios con rol de "desarrollador").
- **Tiempo de expiración del Token JWT** (solo para usuarios con rol de "desarrollador").
- **Configuración flexible** mediante variables de entorno en el `application.properties`.
- **Documentación**: Todo el proyecto está documentado usando **Javadoc** y los endpoints están descritos en **Swagger**.

## Tecnologías utilizadas

- **Java 17**
- **Spring Boot 3.2.5**
- **JWT (JSON Web Tokens)**
- **OAuth2**

## Instalación

Para instalar y ejecutar el proyecto, sigue estos pasos:

1. Clona el repositorio:
   ```bash
   git clone <URL del repositorio>
   cd spring-security-jwt-oauth2

2. Verificar tener Java 17 instalado en tu sistema.

3. Configura las variables de entorno necesarias, que pueden definirse en el archivo application.properties.

4. Para compilar y ejecutar el proyecto, usa Maven:
   mvn clean install
   mvn spring-boot:run
