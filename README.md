# Sistema de Gestión de Licencias

Aplicación de escritorio desarrollada en **JavaFX** para la gestión de trámites de licencias, con persistencia en **PostgreSQL (Supabase)** y construcción mediante **Maven**.

---

## Credenciales de acceso inicial

Al iniciar la aplicación por primera vez, se encuentra creado un usuario administrador por defecto:

- **Usuario:** `admin`
- **Contraseña:** `admin`

> Estas credenciales permiten acceder al sistema y realizar la configuración inicial.  
> **Se recomienda cambiar la contraseña** después del primer inicio de sesión.

---

## Requisitos previos

- Java **24** o superior
- Maven **3.6+**
- Base de datos configurada (**PostgreSQL – Supabase**)

---

## Variables de entorno

Configura las siguientes variables antes de ejecutar el proyecto:

```bash
# Windows (Command Prompt)
set JAVA_HOME=C:\Program Files\Java\jdk-24
set MAVEN_HOME=C:\Program Files\Apache\maven
set PATH=%JAVA_HOME%\bin;%MAVEN_HOME%\bin;%PATH%
