# Sistema de Gestión de Licencias

Aplicación de escritorio desarrollada en **JavaFX** para la gestión de trámites de licencias, con persistencia en **PostgreSQL (Supabase)** y construcción mediante **Maven**.

---

## Credenciales de acceso inicial

Al iniciar la aplicación por primera vez, se encuentra creado un usuario administrador por defecto:

- **Usuario:** `admin --> administrador / carlitos --> analista`
- **Contraseña:** `admin --> administrador / 123456 --> analista`
- **Link del Video** https://youtu.be/JE13b5O7bMU
- **Versión 1.0.0 jar y .exe:** https://github.com/Carlitops13/SistemaLicencias/releases/tag/v1.0.0 
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
```


---

## Diseño de prototipo de las pantallas

En esta sección se presentan las capturas de pantalla del prototipo del sistema, con el objetivo de mostrar la interfaz gráfica y el flujo de navegación de la aplicación.

### Pantalla de inicio de sesión
<!--<img width="799" height="525" alt="image" src="https://github.com/user-attachments/assets/b45467dc-353c-4ef0-b267-978c4808befe" />
 -->


### Menú principal
<!--<img width="999" height="635" alt="image" src="https://github.com/user-attachments/assets/01077a69-737c-4179-b73f-fafdf283c8d5" />
 -->


### Menú Analista
 <img width="996" height="628" alt="image" src="https://github.com/user-attachments/assets/dc07082e-24d7-4af3-9e2b-87c17a41eca4" />



### Registro 
 <img width="829" height="657" alt="image" src="https://github.com/user-attachments/assets/858a01ac-f207-465c-8a46-1d8dd242ce7b" />
 


### Otras pantallas
 <img width="646" height="578" alt="image" src="https://github.com/user-attachments/assets/19f6718d-9eef-4825-85b2-5aae707e8d8b" />
 
 <img width="948" height="660" alt="image" src="https://github.com/user-attachments/assets/446e1716-bcd2-48a5-afda-6193b6855e6f" />
 
 <img width="629" height="661" alt="image" src="https://github.com/user-attachments/assets/5e540f4e-2a7b-46ea-877d-f30edceb4bef" />



