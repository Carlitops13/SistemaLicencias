# Sistema de Gestión de Licencias

## Requisitos previos
- Java 24 o superior
- Maven 3.6+
- Base de datos configurada (postgres SQL-SUPABASE)

## Variables de entorno
Configura estas variables antes de ejecutar:

\`\`\`bash
# Windows (Command Prompt)
set JAVA_HOME=C:\Program Files\Java\jdk-24
set MAVEN_HOME=C:\Program Files\Apache\maven
set PATH=%JAVA_HOME%\bin;%MAVEN_HOME%\bin;%PATH%
\`\`\`

## Compilación

### Limpiar y compilar
\`\`\`bash
mvn clean compile
\`\`\`

### Compilar con todas las dependencias
\`\`\`bash
mvn clean install
\`\`\`

## Ejecución

### Ejecutar la aplicación JavaFX
\`\`\`bash
mvn javafx:run
\`\`\`

### O ejecutar el JAR generado
\`\`\`bash
mvn clean package
java -jar target/licencias-1.0.jar
\`\`\`

## Estructura del proyecto
\`\`\`
src/
├── main/
│   ├── java/com/licencias/
│   │   ├── controller/    (Controladores JavaFX)
│   │   ├── dao/           (Acceso a datos)
│   │   ├── model/         (Modelos de datos)
│   │   └── util/          (Utilidades)
│   └── resources/
│       └── view/          (Archivos FXML)
└── test/                  (Pruebas unitarias)
\`\`\`

## Troubleshooting
- Si falla la carga de FXML: verifica que los archivos estén en `src/main/resources/view/`
- Errores de BD: revisa la configuración de conexión en `application.properties` o `database.xml`
