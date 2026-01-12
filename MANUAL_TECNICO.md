# 🏗️ Manual Técnico - Sistema de Gestión de Licencias de Conducción

## Tabla de Contenidos
1. [Descripción General](#descripción-general)
2. [Stack Tecnológico](#stack-tecnológico)
3. [Arquitectura General](#arquitectura-general)
4. [Estructura de Directorios](#estructura-de-directorios)
5. [Modelos de Datos](#modelos-de-datos)
6. [Capa DAO](#capa-dao)
7. [Capa de Controladores](#capa-de-controladores)
8. [Servicios](#servicios)
9. [Utilidades](#utilidades)
10. [Flujos de Datos](#flujos-de-datos)
11. [Configuración y Ejecución](#configuración-y-ejecución)
12. [Base de Datos](#base-de-datos)
13. [Mejoras y Extensiones](#mejoras-y-extensiones)

---

## Descripción General

**Sistema de Gestión de Licencias de Conducción** es una aplicación JavaFX completa para administrar el proceso integral de solicitudes de licencias. Implementa la arquitectura **MVC (Model-View-Controller)** con separación clara entre capas.

### Características Principales

- ✅ Autenticación con cifrado BCrypt
- ✅ Gestión de usuarios y roles (ADMIN, ANALISTA)
- ✅ Registro y seguimiento de trámites
- ✅ Evaluación de exámenes teóricos y prácticos
- ✅ Validación de requisitos
- ✅ Generación e impresión de licencias
- ✅ Sistema de reportes
- ✅ Persistencia en BD PostgreSQL (Supabase)

---

## Stack Tecnológico

### Dependencias Principales

| Componente | Versión | Propósito |
|-----------|---------|----------|
| **Java** | 11+ | Lenguaje base |
| **JavaFX** | 21.0.6 | Framework UI |
| **Maven** | 3.6+ | Gestor de dependencias |
| **PostgreSQL** | 42.7.2 | Driver JDBC |
| **BCrypt** | 0.4 | Encriptación de contraseñas |
| **ControlsFX** | 11.2.1 | Componentes UI avanzados |
| **ikonli** | 12.3.1 | Iconos para UI |
| **BootstrapFX** | 0.4.0 | Estilos CSS Bootstrap |

### pom.xml Relevante

```xml
<dependencies>
    <!-- JavaFX UI Framework -->
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-controls</artifactId>
        <version>21.0.6</version>
    </dependency>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-fxml</artifactId>
        <version>21.0.6</version>
    </dependency>

    <!-- Base de Datos -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <version>42.7.2</version>
    </dependency>

    <!-- Seguridad - Encriptación BCrypt -->
    <dependency>
        <groupId>org.mindrot</groupId>
        <artifactId>jbcrypt</artifactId>
        <version>0.4</version>
    </dependency>

    <!-- Componentes UI Avanzados -->
    <dependency>
        <groupId>org.controlsfx</groupId>
        <artifactId>controlsfx</artifactId>
        <version>11.2.1</version>
    </dependency>
</dependencies>
```

---

## Arquitectura General

### Patrón MVC

```
┌─────────────────────────────────────────────────────────┐
│                  VIEW LAYER (FXML + CSS)                │
│  - login.fxml                                           │
│  - menu_admin.fxml / menu_analista.fxml                │
│  - formulario_solicitante.fxml                         │
│  - detalle_tramite.fxml                                │
│  - registro_examenes.fxml                              │
│  - verificar_requisitos.fxml                           │
│  - generar_licencia.fxml                               │
│  - reportes.fxml                                       │
└──────────────────────────┬──────────────────────────────┘
                           │ (Event Binding)
┌──────────────────────────▼──────────────────────────────┐
│           CONTROLLER LAYER (JavaFX Controllers)         │
│  - LoginController                                      │
│  - MenuAdminController                                 │
│  - MenuAnalistaController                              │
│  - FormularioSolicitanteController                     │
│  - DetalleTramiteController                            │
│  - ExamenesController                                  │
│  - RequisitosController                                │
│  - LicenciaController                                  │
│  - ReporteController                                   │
└──────────────────────────┬──────────────────────────────┘
                           │ (Llamadas a métodos)
┌──────────────────────────▼──────────────────────────────┐
│            SERVICE LAYER (Lógica de Negocio)            │
│  - LoginService (Autenticación)                        │
│  - TramiteService (Gestión de trámites)                │
│  - LicenciaService (Generación de licencias)           │
│  - ReporteService (Generación de reportes)             │
└──────────────────────────┬──────────────────────────────┘
                           │ (CRUD Operations)
┌──────────────────────────▼──────────────────────────────┐
│        DAO LAYER (Data Access Objects)                  │
│  - ISolicitanteDAO / SolicitanteDAOImpl                 │
│  - ITramiteDAO / TramiteDAOImpl                         │
│  - IUsuarioDAO / UsuarioDAOImpl                         │
│  (Interfaz + Implementación)                           │
└──────────────────────────┬──────────────────────────────┘
                           │ (SQL Queries)
┌──────────────────────────▼──────────────────────────────┐
│      DATABASE LAYER (PostgreSQL - Supabase)             │
│  - Tablas de datos                                      │
│  - Índices y constraints                                │
│  - Views (si aplica)                                    │
└─────────────────────────────────────────────────────────┘
```

### Flujo de Datos

```
┌──────────────────┐
│   Usuario (UI)   │ Ingresa datos / Hacer clic
└────────┬─────────┘
         │
         ▼
┌─────────────────────────────────────┐
│ @FXML Handler                       │ Evento FXML disparado
│ (event.getSource(), @FXML action)   │
└────────┬────────────────────────────┘
         │
         ▼
┌──────────────────────────────────────────────┐
│ Controller.metodo()                          │ Obtiene datos de UI
│ - Valida entrada                             │
│ - Llama al DAO/Service                       │
└────────┬─────────────────────────────────────┘
         │
         ▼
┌──────────────────────────────────────────────┐
│ DAO.metodo(param)                            │ Prepara SQL
│ - Crea PreparedStatement                     │
│ - Mapea parámetros                           │
│ - Ejecuta query                              │
└────────┬─────────────────────────────────────┘
         │
         ▼
┌──────────────────────────────────────────────┐
│ Base de Datos                                │ Procesa SQL
│ (PostgreSQL - Supabase)                      │
└────────┬─────────────────────────────────────┘
         │
         ▼ (ResultSet)
┌──────────────────────────────────────────────┐
│ DAO.mapearResultado()                        │ Mapea fila a objeto
│ - Lee columnas                               │
│ - Crea instancia de model                    │
└────────┬─────────────────────────────────────┘
         │
         ▼
┌──────────────────────────────────────────────┐
│ Controller.actualizarUI(objeto)              │ Actualiza pantalla
│ - Setea labels, tablas, etc.                 │
└──────────────────────────────────────────────┘
```

---

## Estructura de Directorios

```
SistemaLicencias/
│
├── src/
│   ├── main/
│   │   ├── java/com/licencias/
│   │   │   ├── AppLauncher.java                  ⭐ Punto de entrada
│   │   │   ├── Main.java                         ⭐ Clase main
│   │   │   │
│   │   │   ├── controller/                       📱 CAPA UI
│   │   │   │   ├── LoginController.java          🔐 Autenticación
│   │   │   │   ├── MenuAdminController.java      👨‍💼 Panel Admin
│   │   │   │   ├── MenuAnalistaController.java   👨‍💻 Panel Analista
│   │   │   │   ├── FormularioSolicitanteController.java
│   │   │   │   ├── DetalleTramiteController.java
│   │   │   │   ├── ExamenesController.java
│   │   │   │   ├── RequisitosController.java
│   │   │   │   ├── LicenciaController.java
│   │   │   │   └── ReporteController.java
│   │   │   │
│   │   │   ├── dao/                              💾 ACCESO A DATOS
│   │   │   │   ├── ISolicitanteDAO.java          📋 Interfaz
│   │   │   │   ├── ITramiteDAO.java
│   │   │   │   ├── IUsuarioDAO.java
│   │   │   │   │
│   │   │   │   └── impl/                         ⚙️ Implementación
│   │   │   │       ├── SolicitanteDAOImpl.java
│   │   │   │       ├── TramiteDAOImpl.java
│   │   │   │       └── UsuarioDAOImpl.java
│   │   │   │
│   │   │   ├── model/                            📦 MODELOS DE DATOS
│   │   │   │   ├── Usuario.java
│   │   │   │   ├── Solicitante.java
│   │   │   │   ├── Tramite.java
│   │   │   │   └── Licencia.java
│   │   │   │
│   │   │   ├── service/                          🔧 LÓGICA DE NEGOCIO
│   │   │   │   └── LoginService.java
│   │   │   │
│   │   │   └── util/                             🛠️ UTILIDADES
│   │   │       ├── ConexionBD.java
│   │   │       ├── GeneradorClaves.java
│   │   │       ├── Validador.java
│   │   │       └── VentanaUtil.java
│   │   │
│   │   └── resources/
│   │       ├── module-info.java                  📝 Módulos Java
│   │       ├── view/                             🎨 ARCHIVOS FXML
│   │       │   ├── login.fxml
│   │       │   ├── menu_admin.fxml
│   │       │   ├── menu_analista.fxml
│   │       │   ├── formulario_solicitante.fxml
│   │       │   ├── detalle_tramite.fxml
│   │       │   ├── registro_examenes.fxml
│   │       │   ├── verificar_requisitos.fxml
│   │       │   ├── generar_licencia.fxml
│   │       │   └── reportes.fxml
│   │       ├── styles/                           🎨 CSS
│   │       │   └── estilos.css
│   │       └── images/                           🖼️ IMÁGENES
│   │           ├── admin.png
│   │           ├── analista.png
│   │           ├── licencia.png
│   │           ├── login.png
│   │           ├── icono.ico
│   │           └── maclovin.jpg
│   │
│   └── test/                                     🧪 PRUEBAS UNITARIAS
│       └── java/com/licencias/
│           ├── dao/
│           ├── service/
│           └── util/
│
├── target/                                       📦 COMPILADOS
│   ├── classes/
│   └── SistemaLicencias-1.0-SNAPSHOT.jar
│
├── pom.xml                                       ⚙️ CONFIGURACIÓN MAVEN
├── mvnw / mvnw.cmd                               🚀 MAVEN WRAPPER
└── README.md                                     📖 DOCUMENTACIÓN
```

---

## Modelos de Datos

### Usuario.java

```java
public class Usuario {
    private int id;                    // PK
    private String cedula;             // Identificación
    private String nombre;             // Nombre completo
    private String username;           // Username login
    private String password;           // Password encriptado (BCrypt)
    private String rol;                // ADMIN o ANALISTA
    private boolean estado;            // Activo/Inactivo
    private int intentosFallidos;      // Contador de fallos login

    // Getters y Setters
    public boolean isEstado() { return estado; }
    public String getRol() { return rol; }
}
```

**Tabla en BD:**
```sql
CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    cedula VARCHAR(10) UNIQUE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    password TEXT NOT NULL,        -- BCrypt hash
    rol VARCHAR(20) NOT NULL,      -- ADMIN, ANALISTA
    estado BOOLEAN DEFAULT true,
    intentos_fallidos INTEGER DEFAULT 0,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

### Solicitante.java

```java
public class Solicitante {
    private int id;                    // PK
    private String cedula;             // Identificación única
    private String nombre;             // Nombre completo
    private LocalDate fechaNacimiento; // Fecha de nacimiento
    private String telefono;           // Teléfono (10 dígitos)
    private String correo;             // Email del solicitante

    // Constructor, getters y setters
}
```

**Tabla en BD:**
```sql
CREATE TABLE solicitantes (
    id SERIAL PRIMARY KEY,
    cedula VARCHAR(10) UNIQUE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    telefono VARCHAR(10),
    correo VARCHAR(100) UNIQUE,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

### Tramite.java

```java
public class Tramite {
    private int id;                    // PK
    private Solicitante solicitante;   // FK (relación)
    private Usuario usuario;           // FK Analista
    private String tipoLicencia;       // A, B, C, D, E
    private LocalDateTime fechaSolicitud;
    private String estado;             // PENDIENTE, EN_EXAMENES, APROBADO, etc.
    
    // Campos de requisitos
    private boolean reqMedico;         // Certificado médico
    private boolean reqPago;           // Pago de tasa
    private boolean reqMultas;         // Sin multas
    
    // Campos de evaluación
    private double notaTeorica;        // 0-20
    private double notaPractica;       // 0-20

    // Getters y setters
}
```

**Estados Posibles del Trámite:**
```
PENDIENTE
    ↓
REQUISITOS (Verificación de requisitos)
    ↓
EN_EXAMENES (Registro de exámenes)
    ├─→ REPROBADO (Si nota < 14)
    │   ↓ (reintentar)
    │   EN_EXAMENES
    │
    └─→ APROBADO (Si ambas notas ≥ 14)
        ↓
        LICENCIA_EMITIDA
        
RECHAZADO (Si falla requisitos)
```

**Tabla en BD:**
```sql
CREATE TABLE tramites (
    id SERIAL PRIMARY KEY,
    id_solicitante INTEGER NOT NULL REFERENCES solicitantes(id),
    id_usuario INTEGER REFERENCES usuarios(id),
    tipo_licencia VARCHAR(5),
    fecha_solicitud TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(50) DEFAULT 'PENDIENTE',
    req_medico BOOLEAN DEFAULT false,
    req_pago BOOLEAN DEFAULT false,
    req_multas BOOLEAN DEFAULT false,
    nota_teorica DECIMAL(5,2),
    nota_practica DECIMAL(5,2)
);
```

---

### Licencia.java

```java
public class Licencia {
    private int id;                    // PK
    private int tramiteId;             // FK
    private String numeroLicencia;     // Número único
    private LocalDate fechaEmision;    // Fecha emisión
    private LocalDate fechaVencimiento;// Fecha vencimiento (5 años)

    // Getters y setters
}
```

**Tabla en BD:**
```sql
CREATE TABLE licencias (
    id SERIAL PRIMARY KEY,
    id_tramite INTEGER NOT NULL UNIQUE REFERENCES tramites(id),
    numero_licencia VARCHAR(50) UNIQUE NOT NULL,
    fecha_emision DATE DEFAULT CURRENT_DATE,
    fecha_vencimiento DATE,            -- +5 años
    estado VARCHAR(20) DEFAULT 'ACTIVA'
);
```

---

## Capa DAO

### Patrón DAO (Data Access Object)

Cada modelo tiene:
1. **Interfaz**: Define métodos disponibles
2. **Implementación**: Implementa SQL

```java
// ========== INTERFAZ ==========
public interface ISolicitanteDAO {
    Solicitante buscarPorCedula(String cedula);
    Solicitante buscarPorId(int id);
    List<Solicitante> listarTodos();
    boolean insertar(Solicitante s);
    boolean actualizar(Solicitante s);
    boolean eliminar(int id);
}

// ========== IMPLEMENTACIÓN ==========
public class SolicitanteDAOImpl implements ISolicitanteDAO {
    
    @Override
    public Solicitante buscarPorCedula(String cedula) {
        String sql = "SELECT * FROM solicitantes WHERE cedula = ?";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, cedula);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                return mapearResultado(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error en buscarPorCedula: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public boolean insertar(Solicitante s) {
        String sql = "INSERT INTO solicitantes (cedula, nombre, fecha_nacimiento, " +
                    "telefono, correo) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            
            pst.setString(1, s.getCedula());
            pst.setString(2, s.getNombre());
            pst.setDate(3, java.sql.Date.valueOf(s.getFechaNacimiento()));
            pst.setString(4, s.getTelefono());
            pst.setString(5, s.getCorreo());
            
            int filas = pst.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("Error en insertar: " + e.getMessage());
        }
        return false;
    }
    
    private Solicitante mapearResultado(ResultSet rs) throws SQLException {
        Solicitante s = new Solicitante();
        s.setId(rs.getInt("id"));
        s.setCedula(rs.getString("cedula"));
        s.setNombre(rs.getString("nombre"));
        s.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
        s.setTelefono(rs.getString("telefono"));
        s.setCorreo(rs.getString("correo"));
        return s;
    }
}
```

### DAOs del Sistema

| DAO | Responsabilidad |
|-----|-----------------|
| **IUsuarioDAO** | CRUD de usuarios, autenticación |
| **ISolicitanteDAO** | CRUD de solicitantes |
| **ITramiteDAO** | CRUD de trámites, búsqueda por estado |

---

## Capa de Controladores

### LoginController.java

**Responsabilidades:**
1. Validar entrada del usuario
2. Autenticar contra BD
3. Bloquear después de 3 intentos
4. Navegar al menú según rol

```java
@FXML private TextField txtUsuario;
@FXML private PasswordField txtPassword;
@FXML private ComboBox<String> cmbRol;

private LoginService loginService;
private IUsuarioDAO usuarioDAO;

@FXML
public void handleLoginAction(ActionEvent event) {
    String username = txtUsuario.getText();
    String password = txtPassword.getText();
    String rolSeleccionado = cmbRol.getValue();

    // Validar campos
    if (username.isEmpty() || password.isEmpty() || rolSeleccionado == null) {
        mostrarAlerta("Campos Incompletos", "Complete todos los datos.");
        return;
    }

    // Buscar usuario
    Usuario usuario = loginService.encontrarUsuario(username);
    if (usuario == null) {
        mostrarAlerta("Error", "El usuario no existe.");
        return;
    }

    // Verificar cuenta activa
    if (!usuario.isEstado()) {
        mostrarAlerta("Cuenta Bloqueada", "Contacte al administrador.");
        return;
    }

    // Verificar contraseña (BCrypt)
    if (loginService.verificarPassword(password, usuario.getPassword())) {
        // Reset de intentos
        usuario.setIntentosFallidos(0);
        usuarioDAO.actualizarIntentos(usuario);
        
        // Navegar
        abrirMenuPrincipal(usuario.getRol());
    } else {
        // Incrementar intentos
        int intentos = usuario.getIntentosFallidos() + 1;
        usuario.setIntentosFallidos(intentos);
        
        // Bloquear si llega a 3
        if (intentos >= 3) {
            usuario.setEstado(false);
            usuarioDAO.actualizar(usuario);
            mostrarAlerta("BLOQUEO DE SEGURIDAD", 
                "Superó 3 intentos. Cuenta bloqueada.");
        } else {
            mostrarAlerta("Clave Incorrecta", 
                "Intento " + intentos + " de 3.");
        }
    }
}

private void abrirMenuPrincipal(String rol) {
    try {
        String rutaFXML = rol.equals("ADMIN") 
            ? "/view/menu_admin.fxml" 
            : "/view/menu_analista.fxml";
        
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource(rutaFXML)
        );
        Parent root = loader.load();
        
        Stage stage = (Stage) btnLogin.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Sistema de Licencias - " + rol);
    } catch (IOException e) {
        mostrarAlerta("Error", "No se pudo cargar el menú.");
    }
}
```

### FormularioSolicitanteController.java

**Responsabilidades:**
1. Validar datos de entrada
2. Crear Solicitante en BD
3. Crear Trámite asociado

```java
@FXML private TextField txtCedula;
@FXML private TextField txtNombre;
@FXML private TextField txtCorreo;
@FXML private TextField txtTelefono;
@FXML private DatePicker dpFechaNacimiento;
@FXML private ComboBox<String> cmbTipoLicencia;

private ISolicitanteDAO solicitanteDAO;
private ITramiteDAO tramiteDAO;

@FXML
public void guardarSolicitante() {
    String cedula = txtCedula.getText().trim();
    String nombre = txtNombre.getText().trim();
    String correo = txtCorreo.getText().trim();
    String telefono = txtTelefono.getText().trim();
    LocalDate fechaNac = dpFechaNacimiento.getValue();
    String tipoLicencia = cmbTipoLicencia.getValue();

    // Validaciones
    if (cedula.isEmpty() || nombre.isEmpty() || 
        correo.isEmpty() || tipoLicencia == null) {
        mostrarAlerta(Alert.AlertType.WARNING, 
            "Campos Incompletos", "Complete todos los campos.");
        return;
    }

    // Validar correo
    if (!Validador.esCorreoValido(correo)) {
        mostrarAlerta(Alert.AlertType.WARNING, 
            "Correo Inválido", "Ej: usuario@dominio.com");
        return;
    }

    // Verificar cédula única
    Solicitante existente = solicitanteDAO.buscarPorCedula(cedula);
    if (existente != null) {
        mostrarAlerta(Alert.AlertType.ERROR, 
            "Cédula Duplicada", "Este solicitante ya existe.");
        return;
    }

    // Crear solicitante
    Solicitante nuevoSolicitante = new Solicitante();
    nuevoSolicitante.setCedula(cedula);
    nuevoSolicitante.setNombre(nombre);
    nuevoSolicitante.setCorreo(correo);
    nuevoSolicitante.setTelefono(telefono);
    nuevoSolicitante.setFechaNacimiento(fechaNac);

    // Guardar en BD
    if (solicitanteDAO.insertar(nuevoSolicitante)) {
        // Obtener ID del solicitante
        Solicitante solicitanteGuardado = 
            solicitanteDAO.buscarPorCedula(cedula);
        
        // Crear trámite automático
        Tramite tramite = new Tramite();
        tramite.setSolicitante(solicitanteGuardado);
        tramite.setTipoLicencia(tipoLicencia);
        tramite.setEstado("PENDIENTE");
        
        tramiteDAO.insertar(tramite);
        
        mostrarAlerta(Alert.AlertType.INFORMATION, 
            "Éxito", "Solicitante registrado correctamente.");
        limpiarFormulario();
    } else {
        mostrarAlerta(Alert.AlertType.ERROR, 
            "Error", "No se pudo guardar el solicitante.");
    }
}
```

### DetalleTramiteController.java

**Responsabilidades:**
1. Listar trámites en tabla
2. Filtrar por cédula/nombre
3. Mostrar detalles del trámite seleccionado
4. Habilitar botón emitir si está aprobado

```java
@FXML private TableView<Tramite> tblTramites;
@FXML private TableColumn<Tramite, String> colCedula, colNombre, colEstado;
@FXML private TextField txtFiltroRapido;
@FXML private Label lblNombre, lblEstado;
@FXML private CheckBox chkReqMedico, chkReqPago, chkReqMultas;
@FXML private Label lblNotaTeorica, lblNotaPractica;
@FXML private Button btnGenerarLicencia;

private ITramiteDAO tramiteDAO;
private ObservableList<Tramite> masterData;

@FXML
public void initialize() {
    tramiteDAO = new TramiteDAOImpl();
    masterData = FXCollections.observableArrayList();

    // Configurar columnas
    colCedula.setCellValueFactory(cellData -> 
        new SimpleStringProperty(
            cellData.getValue().getSolicitante().getCedula()
        )
    );
    colNombre.setCellValueFactory(cellData -> 
        new SimpleStringProperty(
            cellData.getValue().getSolicitante().getNombre()
        )
    );
    colEstado.setCellValueFactory(
        new PropertyValueFactory<>("estado")
    );

    // Listener para selección
    tblTramites.getSelectionModel()
        .selectedItemProperty()
        .addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                actualizarInterfaz(newVal);
            }
        });

    // Listener para filtro
    txtFiltroRapido.textProperty()
        .addListener((obs, oldVal, newVal) -> filtrarDatos(newVal));

    // Cargar datos
    cargarDatosTabla();
}

private void cargarDatosTabla() {
    List<Tramite> lista = tramiteDAO.listarTodos();
    masterData.setAll(lista);
    tblTramites.setItems(masterData);
}

private void filtrarDatos(String filtro) {
    if (filtro == null || filtro.isEmpty()) {
        tblTramites.setItems(masterData);
    } else {
        ObservableList<Tramite> filtrada = 
            FXCollections.observableArrayList();
        
        for (Tramite t : masterData) {
            if (t.getSolicitante().getCedula().contains(filtro) ||
                t.getSolicitante().getNombre()
                    .toLowerCase()
                    .contains(filtro.toLowerCase())) {
                filtrada.add(t);
            }
        }
        tblTramites.setItems(filtrada);
    }
}

private void actualizarInterfaz(Tramite tramite) {
    // Datos solicitante
    lblNombre.setText(tramite.getSolicitante().getNombre());
    lblEstado.setText(tramite.getEstado());

    // Requisitos
    chkReqMedico.setSelected(tramite.isReqMedico());
    chkReqPago.setSelected(tramite.isReqPago());
    chkReqMultas.setSelected(tramite.isReqMultas());

    // Notas
    lblNotaTeorica.setText(
        String.format("%.2f", tramite.getNotaTeorica())
    );
    lblNotaPractica.setText(
        String.format("%.2f", tramite.getNotaPractica())
    );

    // Habilitar botón si está aprobado
    boolean estaAprobado = "APROBADO".equals(tramite.getEstado());
    boolean todosRequisitos = tramite.isReqMedico() && 
                             tramite.isReqPago() && 
                             tramite.isReqMultas();
    
    btnGenerarLicencia.setDisable(!(estaAprobado && todosRequisitos));
}
```

### ExamenesController.java

**Responsabilidades:**
1. Listar trámites en estado EN_EXAMENES o REPROBADO
2. Registrar notas teórica y práctica
3. Determinar APROBADO o REPROBADO según notas

```java
@FXML private TextField txtNotaTeorica, txtNotaPractica;
@FXML private Label lblPromedio;
@FXML private Button btnGuardar;

private ITramiteDAO tramiteDAO;
private Tramite tramiteSeleccionado;

@FXML
public void guardarNotas() {
    if (tramiteSeleccionado == null) {
        mostrarAlerta("Error", "Seleccione un solicitante.");
        return;
    }

    try {
        double notaTeo = Double.parseDouble(
            txtNotaTeorica.getText()
        );
        double notaPrac = Double.parseDouble(
            txtNotaPractica.getText()
        );

        // Validar rango
        if (notaTeo < 0 || notaTeo > 100 || 
            notaPrac < 0 || notaPrac > 100) {
            mostrarAlerta("Error", "Las notas deben estar entre 0-100.");
            return;
        }

        // Actualizar tramite
        tramiteSeleccionado.setNotaTeorica(notaTeo);
        tramiteSeleccionado.setNotaPractica(notaPrac);

        // Determinar estado
        if (notaTeo >= 60 && notaPrac >= 60) {
            tramiteSeleccionado.setEstado("APROBADO");
        } else {
            tramiteSeleccionado.setEstado("REPROBADO");
        }

        // Guardar
        if (tramiteDAO.actualizar(tramiteSeleccionado)) {
            mostrarAlerta(Alert.AlertType.INFORMATION, 
                "Éxito", "Notas guardadas correctamente.");
            actualizarLista();
            limpiarFormulario();
        }
    } catch (NumberFormatException e) {
        mostrarAlerta("Error", "Ingrese números válidos.");
    }
}
```

### RequisitosController.java

**Responsabilidades:**
1. Listar trámites en estado PENDIENTE o REQUISITOS
2. Validar requisitos (médico, pago, multas)
3. Aprobar o rechazar según requisitos

```java
@FXML private CheckBox chkCertificadoMedico, chkPago, chkSinMultas;
@FXML private Button btnAprobar, btnRechazar;

@FXML
public void aprobar() {
    if (tramiteActual == null) return;

    boolean todos = chkCertificadoMedico.isSelected() &&
                    chkPago.isSelected() &&
                    chkSinMultas.isSelected();

    if (!todos) {
        mostrarAlerta("Error", "Todos los requisitos deben estar marcados.");
        return;
    }

    tramiteActual.setReqMedico(true);
    tramiteActual.setReqPago(true);
    tramiteActual.setReqMultas(true);
    tramiteActual.setEstado("EN_EXAMENES");

    if (tramiteDAO.actualizar(tramiteActual)) {
        mostrarAlerta("Éxito", "Trámite aprobado. Pase a exámenes.");
        actualizarLista();
    }
}

@FXML
public void rechazar() {
    if (tramiteActual == null) return;

    tramiteActual.setEstado("RECHAZADO");
    
    if (tramiteDAO.actualizar(tramiteActual)) {
        mostrarAlerta("Éxito", "Trámite rechazado.");
        actualizarLista();
    }
}
```

---

## Servicios

### LoginService.java

```java
public class LoginService {
    private IUsuarioDAO usuarioDAO;

    public LoginService() {
        this.usuarioDAO = new UsuarioDAOImpl();
    }

    // Buscar usuario por username
    public Usuario encontrarUsuario(String username) {
        return usuarioDAO.buscarPorUsername(username);
    }

    // Verificar contraseña con BCrypt
    public boolean verificarPassword(String passwordPlano, String passwordHash) {
        return BCrypt.checkpw(passwordPlano, passwordHash);
    }

    // Encriptar contraseña
    public String encriptarPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
```

---

## Utilidades

### ConexionBD.java

```java
public class ConexionBD {
    private static final String URL = 
        "jdbc:postgresql://aws-0-us-west-2.pooler.supabase.com:6543/postgres";
    private static final String USER = "postgres.qjxnofdnirxzjlkglqbe";
    private static final String PASSWORD = "Pfr2qAlKDCsRuWiB";

    public static Connection obtenerConexion() {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            DriverManager.setLoginTimeout(10);
            
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            if (conn != null) {
                System.out.println("✓ Conexión exitosa a Supabase");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("✗ Driver de PostgreSQL no encontrado");
        } catch (SQLException e) {
            System.err.println("✗ Error de Conexión: " + e.getMessage());
        }
        return conn;
    }
}
```

### Validador.java

```java
public class Validador {
    
    public static boolean esCorreoValido(String correo) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return correo != null && correo.matches(regex);
    }

    public static void limitarCedula(TextField txt) {
        txt.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                txt.setText(newVal.replaceAll("[^\\d]", ""));
            }
            if (newVal.length() > 10) {
                txt.setText(newVal.substring(0, 10));
            }
        });
    }

    public static void limitarTelefono(TextField txt) {
        txt.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                txt.setText(newVal.replaceAll("[^\\d]", ""));
            }
            if (newVal.length() > 10) {
                txt.setText(newVal.substring(0, 10));
            }
        });
    }
}
```

### GeneradorClaves.java

```java
public class GeneradorClaves {
    
    public static String generarNumeroLicencia() {
        LocalDateTime ahora = LocalDateTime.now();
        String timestamp = ahora.format(
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
        );
        String random = String.format("%04d", 
            new Random().nextInt(10000));
        return "LIC-" + timestamp + "-" + random;
    }
}
```

---

## Flujos de Datos

### Flujo 1: Login y Autenticación

```
LoginController.handleLoginAction()
    ↓
Validar campos no vacíos
    ↓
LoginService.encontrarUsuario(username)
    ↓
UsuarioDAOImpl.buscarPorUsername()
    ├─ SQL: SELECT * FROM usuarios WHERE username = ?
    ├─ Ejecuta query
    └─ Mapea resultado
    ↓
¿Usuario existe?
    ├─ NO → Mostrar "usuario no existe"
    │
    └─ SÍ → ¿Cuenta activa?
        ├─ NO → Mostrar "cuenta bloqueada"
        │
        └─ SÍ → LoginService.verificarPassword(pass)
            ├─ Usa BCrypt.checkpw()
            └─ ¿Contraseña correcta?
                ├─ NO → Incrementar intentosFallidos
                │       ├─ ¿Intentos >= 3?
                │       │  └─ SÍ → Bloquear cuenta (estado = false)
                │       └─ Mostrar error
                │
                └─ SÍ → Reset intentosFallidos
                        ↓
                        abrirMenuPrincipal(rol)
                        └─ Cargar FXML según rol (ADMIN/ANALISTA)
```

### Flujo 2: Registro de Solicitante

```
FormularioSolicitanteController.guardarSolicitante()
    ↓
Validar campos obligatorios
    ├─ Cédula, nombre, correo, teléfono, tipo licencia
    ├─ Formato de correo
    └─ Rango de edad (≥18 años)
    ↓
SolicitanteDAOImpl.buscarPorCedula()
    ├─ ¿Cédula existe?
    │  └─ SÍ → Mostrar error "cédula duplicada"
    │
    └─ NO → SolicitanteDAOImpl.insertar(solicitante)
        ├─ SQL: INSERT INTO solicitantes VALUES (...)
        └─ ✓ Solicitante creado
            ↓
            TramiteDAOImpl.insertar(tramite)
            └─ SQL: INSERT INTO tramites VALUES (...)
                ├─ Estado: PENDIENTE
                ├─ tipoLicencia del solicitante
                └─ ✓ Trámite creado
```

### Flujo 3: Evaluación Completa

```
1️⃣ VERIFICAR REQUISITOS
   RequisitosController
   ├─ Seleccionar trámite
   ├─ Marcar checkboxes (médico, pago, multas)
   └─ Click APROBAR
      └─ Tramite.estado = "EN_EXAMENES"
         └─ TramiteDAOImpl.actualizar()

2️⃣ REGISTRAR EXÁMENES
   ExamenesController
   ├─ Seleccionar trámite (EN_EXAMENES)
   ├─ Ingresar nota teórica
   ├─ Ingresar nota práctica
   └─ Click GUARDAR
      ├─ ¿Ambas ≥ 14?
      │  └─ SÍ → Estado = "APROBADO"
      │  └─ NO → Estado = "REPROBADO"
      └─ TramiteDAOImpl.actualizar()

3️⃣ GENERAR LICENCIA
   LicenciaController
   ├─ Buscar por cédula
   ├─ Validar estado = "APROBADO"
   ├─ Validar todos requisitos marcados
   ├─ GeneradorClaves.generarNumeroLicencia()
   └─ Click IMPRIMIR
      ├─ LicenciaDAOImpl.insertar()
      ├─ TramiteDAOImpl.actualizar(estado = "LICENCIA_EMITIDA")
      └─ Mostrar diálogo de impresión
```

---

## Configuración y Ejecución

### Requisitos del Sistema

- Java 11 o superior
- Maven 3.6+
- Conexión a Internet (BD en Supabase)
- Acceso a puerto 6543 (PostgreSQL Supabase)

### Variables de Entorno

```bash
# Windows (PowerShell)
$env:JAVA_HOME = "C:\Program Files\Java\jdk-11"
$env:MAVEN_HOME = "C:\Program Files\Apache\maven"
$env:PATH = "$env:MAVEN_HOME\bin;$env:PATH"

# Verificar
java -version
mvn -version
```

### Compilación

```bash
# Limpiar y compilar
mvn clean compile

# Compilar y crear JAR
mvn clean package

# Compilar con skip de tests
mvn clean compile -DskipTests
```

### Ejecución

```bash
# Opción 1: Ejecutar con Maven
mvn javafx:run

# Opción 2: Ejecutar JAR
java -jar target/SistemaLicencias-1.0-SNAPSHOT.jar

# Opción 3: Desde IDE
# Click derecho en Main.java → Run As → Java Application
```

---

## Base de Datos

### Script de Creación

```sql
-- Base de datos
CREATE DATABASE licencias;
\c licencias;

-- Tabla usuarios
CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    cedula VARCHAR(10) UNIQUE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    password TEXT NOT NULL,
    rol VARCHAR(20) NOT NULL,
    estado BOOLEAN DEFAULT true,
    intentos_fallidos INTEGER DEFAULT 0,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla solicitantes
CREATE TABLE solicitantes (
    id SERIAL PRIMARY KEY,
    cedula VARCHAR(10) UNIQUE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    telefono VARCHAR(10),
    correo VARCHAR(100) UNIQUE,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla trámites
CREATE TABLE tramites (
    id SERIAL PRIMARY KEY,
    id_solicitante INTEGER NOT NULL REFERENCES solicitantes(id),
    id_usuario INTEGER REFERENCES usuarios(id),
    tipo_licencia VARCHAR(5),
    fecha_solicitud TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(50) DEFAULT 'PENDIENTE',
    req_medico BOOLEAN DEFAULT false,
    req_pago BOOLEAN DEFAULT false,
    req_multas BOOLEAN DEFAULT false,
    nota_teorica DECIMAL(5,2),
    nota_practica DECIMAL(5,2)
);

-- Tabla licencias
CREATE TABLE licencias (
    id SERIAL PRIMARY KEY,
    id_tramite INTEGER NOT NULL UNIQUE REFERENCES tramites(id),
    numero_licencia VARCHAR(50) UNIQUE NOT NULL,
    fecha_emision DATE DEFAULT CURRENT_DATE,
    fecha_vencimiento DATE,
    estado VARCHAR(20) DEFAULT 'ACTIVA'
);

-- Índices
CREATE INDEX idx_usuarios_cedula ON usuarios(cedula);
CREATE INDEX idx_usuarios_username ON usuarios(username);
CREATE INDEX idx_solicitantes_cedula ON solicitantes(cedula);
CREATE INDEX idx_tramites_estado ON tramites(estado);
CREATE INDEX idx_tramites_solicitante ON tramites(id_solicitante);
CREATE INDEX idx_licencias_numero ON licencias(numero_licencia);

-- Datos iniciales
INSERT INTO usuarios (cedula, nombre, username, password, rol)
VALUES 
('1234567890', 'Admin Sistema', 'admin', 
 '$2a$10$SlVZVnVsdmFjNVprM0djLuq8qVYqFv0FgVRCZqMWn2g1FUWm9YLWi', 'ADMIN'),
('2345678901', 'Analista Licencias', 'analista', 
 '$2a$10$SlVZVnVsdmFjNVprM0djLuq8qVYqFv0FgVRCZqMWn2g1FUWm9YLWi', 'ANALISTA');
```

**Contraseñas de prueba (en texto plano):**
- usuario: `admin` / contraseña: `admin123`
- usuario: `analista` / contraseña: `analista123`

---

## Mejoras y Extensiones

### Posibles Mejoras Futuras

1. **Seguridad**
   - [ ] Implementar JWT para tokens de sesión
   - [ ] Validación de acceso por endpoint
   - [ ] Logging de auditoría

2. **Funcionalidad**
   - [ ] Sistema de notificaciones por email
   - [ ] Reportes en PDF/Excel
   - [ ] Dashboard analítico
   - [ ] API REST

3. **Performance**
   - [ ] Paginación de tablas
   - [ ] Caché en memoria
   - [ ] Índices optimizados en BD

4. **UI/UX**
   - [ ] Temas oscuro/claro
   - [ ] Responsividad mejorada
   - [ ] Más animaciones

### Estructura de Carpetas para Extensiones

```
util/
├── ConexionBD.java         (Existente)
├── Validador.java          (Existente)
├── GeneradorClaves.java    (Existente)
├── VentanaUtil.java        (Existente)


service/
├── LoginService.java       (Existente)

```

---

## Diagrama de Clases

```
┌─────────────────┐
│    Usuario      │
├─────────────────┤
│ - id            │
│ - cedula        │
│ - nombre        │
│ - username      │
│ - password      │
│ - rol           │
│ - estado        │
└────────┬────────┘
         │ 1 a N
         │ (crea/modifica)
         │
┌────────▼────────────────┐
│      Trámite            │
├─────────────────────────┤
│ - id                    │
│ - solicitante (FK)      │
│ - usuario (FK)          │
│ - tipoLicencia          │
│ - estado                │
│ - reqMedico             │
│ - reqPago               │
│ - reqMultas             │
│ - notaTeorica           │
│ - notaPractica          │
└────────┬────────────────┘
         │
    ┌────┴─────────────────┐
    │                      │
┌───▼────────┐      ┌─────▼────────┐
│ Solicitante│      │   Licencia   │
├────────────┤      ├──────────────┤
│ - id       │      │ - id         │
│ - cedula   │      │ - tramiteId  │
│ - nombre   │      │ - numero     │
│ - fecha Nac│      │ - fechaEmis  │
│ - teléfono │      │ - fechaVenc  │
│ - correo   │      │ - estado     │
└────────────┘      └──────────────┘
```

---

## Referencias y Recursos

### Documentación Oficial

- [JavaFX Documentation](https://openjfx.io/)
- [PostgreSQL JDBC](https://jdbc.postgresql.org/)
- [Maven Documentation](https://maven.apache.org/)
- [BCrypt](https://www.mindrot.org/projects/jBCrypt/)

### Patrones de Diseño Utilizados

- **MVC**: Separación de responsabilidades
- **DAO**: Acceso a datos
- **Singleton**: Conexión a BD
- **Factory**: Creación de objetos

---

**Versión**: 1.0  
**Última actualización**: Enero 2026  
**Autor**: Equipo de Desarrollo  
**Licencia**: Propietario - Sistema de Licencias

