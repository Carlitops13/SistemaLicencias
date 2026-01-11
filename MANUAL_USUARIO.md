# 📋 Manual de Usuario - Sistema de Gestión de Licencias de Conducción

## Tabla de Contenidos
1. [Introducción](#introducción)
2. [Acceso al Sistema](#acceso-al-sistema)
3. [Ventana de Login](#-ventana-de-login)
4. [Menú Administrador](#-menú-administrador)
5. [Menú Analista](#-menú-analista)
6. [Ventanas de Funcionalidad](#-ventanas-de-funcionalidad)
7. [Guía de Flujos](#guía-de-flujos)
8. [Consejos y Troubleshooting](#consejos-y-troubleshooting)

---

## Introducción

El **Sistema de Gestión de Licencias de Conducción** es una aplicación integral para administrar el proceso completo de solicitudes de licencias. Permite:

- ✅ Registrar nuevos solicitantes
- ✅ Gestionar trámites completos
- ✅ Registrar calificaciones de exámenes
- ✅ Verificar cumplimiento de requisitos
- ✅ Emitir e imprimir licencias
- ✅ Generar reportes
- ✅ Administrar usuarios del sistema

**Roles del Sistema:**
- **Administrador (ADMIN)**: Acceso completo a todas las funciones, gestión de usuarios
- **Analista (ANALISTA)**: Gestión de trámites, evaluaciones y emisión de licencias

---

## Acceso al Sistema

### Requisitos Previos
- Conexión a Internet activa (BD en Supabase)
- Credenciales válidas de usuario
- Navegador o aplicación actualizada

### Inicio de la Aplicación

**Opción 1: Con Maven instalado**
```bash
mvn javafx:run
```

**Opción 2: Con JAR ejecutable**
```bash
java -jar SistemaLicencias-1.0-SNAPSHOT.jar
```

**Opción 3: Desde IDE (Eclipse, IntelliJ, NetBeans)**
- Clic derecho en `Main.java`
- Seleccionar "Run As" → "Java Application"

---

## 🔐 Ventana de Login

### Descripción General
Primera pantalla del sistema. Aquí se autentifican los usuarios y se asigna el rol correspondiente.

### Elementos de la Interfaz

```
┌─────────────────────────────────────┐
│   SISTEMA DE LICENCIAS              │
│   🔐 Acceso al Sistema              │
├─────────────────────────────────────┤
│                                     │
│  Usuario:      [_________________]  │
│                                     │
│  Contraseña:   [_________________]  │
│                                     │
│  Rol:          [ADMIN ▼]           │
│                                     │
│              [INGRESAR]             │
│                                     │
└─────────────────────────────────────┘
```

### Campos a Completar

| Campo | Descripción | Ejemplo |
|-------|-------------|---------|
| **Usuario** | Nombre de usuario del sistema | `admin` |
| **Contraseña** | Contraseña encriptada (oculta) | `•••••••` |
| **Rol** | Seleccione su rol | ADMIN o ANALISTA |

### Procedimiento de Acceso

1. **Escriba su usuario** en el primer campo
2. **Escriba su contraseña** en el segundo campo (se mostrará con puntos)
3. **Seleccione su rol** en el combo box
4. **Haga clic en "INGRESAR"** para acceder

### Mensajes de Validación

| Mensaje | Significado | Solución |
|---------|-------------|----------|
| "Campos Incompletos" | Falta ingresar datos | Complete todos los campos |
| "El usuario no existe" | Usuario no registrado en BD | Verifique el nombre de usuario |
| "Clave Incorrecta (Intento 1 de 3)" | Contraseña incorrecta | Intente nuevamente |
| "Cuenta Bloqueada" | Superó 3 intentos fallidos | Contacte al administrador |
| "Error de Rol" | Rol seleccionado no coincide | Seleccione el rol correcto |

### Seguridad de la Cuenta

- ⚠️ **Bloqueo tras 3 intentos fallidos**: Si falla 3 veces, la cuenta se bloqueará automáticamente
- 🔒 **Contraseña Encriptada**: Las contraseñas se guardan encriptadas en la BD (BCrypt)
- 🔑 **Cambio de Contraseña**: Solo el administrador puede resetearla

---

## 👨‍💼 Menú Administrador

### Acceso
Solo usuarios con rol **ADMIN** pueden acceder a este menú.

### Vista General del Menú

```
┌─────────────────────────────────────────────┐
│   PANEL DE ADMINISTRADOR                    │
│   Bienvenido: admin                         │
├─────────────────────────────────────────────┤
│                                             │
│  [ 👥 GESTIONAR USUARIOS ]                 │
│  [ 📋 REGISTRAR SOLICITANTES ]             │
│  [ 📝 VERIFICAR REQUISITOS ]               │
│  [ 📊 REGISTRO DE EXÁMENES ]               │
│  [ 📄 GENERAR LICENCIA ]                   │
│  [ 📈 VER TRÁMITES ]                       │
│  [ 📊 REPORTES ]                           │
│                                             │
│  [ 🔄 CAMBIAR A MODO ANALISTA ]            │
│  [ 🚪 CERRAR SESIÓN ]                      │
│                                             │
└─────────────────────────────────────────────┘
```

### Opciones del Menú Administrador

#### 1️⃣ **GESTIONAR USUARIOS**

**Propósito**: Crear, editar, buscar y desactivar usuarios del sistema.

**Interfaz Principal:**

```
┌────────────────────────────────────────┐
│  GESTIÓN DE USUARIOS                   │
├────────────────────────────────────────┤
│ Buscar: [______________]               │
│                                        │
│ Tabla de Usuarios:                     │
│ ┌──────────────────────────────────┐  │
│ │ Cédula│ Nombre│ Usuario│ Rol│Est│  │
│ ├──────────────────────────────────┤  │
│ │ 123456│ Juan  │ juan01 │ADMIN│✓ │  │
│ │ 234567│ María │maria01 │ANAL │✓ │  │
│ └──────────────────────────────────┘  │
│                                        │
│ Formulario de Edición:                 │
│ Cédula:      [______________]          │
│ Nombre:      [______________]          │
│ Usuario:     [______________]          │
│ Contraseña:  [______________]          │
│ Rol:         [ADMIN ▼]                │
│ Activo:      [✓]                       │
│                                        │
│ [NUEVO]  [GUARDAR]  [ELIMINAR]         │
│ [CERRAR VENTANA]                       │
└────────────────────────────────────────┘
```

**Procedimiento:**

**Para crear un nuevo usuario:**
1. Haga clic en botón **"NUEVO"**
2. Complete los campos:
   - Cédula (10 dígitos, única)
   - Nombre completo
   - Nombre de usuario (sin espacios)
   - Contraseña (mínimo 6 caracteres)
   - Seleccione rol (ADMIN o ANALISTA)
3. Active el checkbox "Activo"
4. Haga clic en **"GUARDAR"**

**Para editar un usuario existente:**
1. Busque en el campo superior (por cédula, nombre, usuario o rol)
2. Haga clic en la fila en la tabla
3. Modifique los campos deseados
4. Haga clic en **"GUARDAR"**

**Para desactivar un usuario:**
1. Seleccione el usuario de la tabla
2. Desactive el checkbox "Activo"
3. Haga clic en **"GUARDAR"**
⚠️ *Un usuario inactivo no puede iniciar sesión*

---

#### 2️⃣ **REGISTRAR SOLICITANTES**

**Propósito**: Crear nuevos solicitantes en el sistema.

**Interfaz:**

```
┌────────────────────────────────────────┐
│  REGISTRO DE SOLICITANTES              │
├────────────────────────────────────────┤
│                                        │
│  Cédula:             [______________]  │
│  Nombre Completo:    [______________]  │
│  Teléfono:           [______________]  │
│  Correo Electrónico: [______________]  │
│                                        │
│  Fecha de Nacimiento: [___/___/_____]  │
│  Tipo de Licencia:   [A    ▼]         │
│  Fecha de Solicitud: [___/___/_____]  │
│                                        │
│  [GUARDAR] [LIMPIAR] [REGRESAR]        │
│                                        │
└────────────────────────────────────────┘
```

**Campos Obligatorios:**
- ✓ Cédula (10 dígitos, única en el sistema)
- ✓ Nombre completo
- ✓ Correo válido (ej: usuario@dominio.com)
- ✓ Teléfono (10 dígitos)
- ✓ Fecha de nacimiento (mínimo 18 años)
- ✓ Tipo de licencia

**Tipos de Licencia Disponibles:**
- **A**: Motocicletas
- **B**: Vehículos livianos (autos)
- **C**: Vehículos medianos
- **D**: Buses de pasajeros
- **E**: Camiones pesados

**Procedimiento:**

1. Complete todos los campos obligatorios
2. Verifique que la cédula no esté registrada
3. Valide el formato del correo
4. Seleccione tipo de licencia
5. Haga clic en **"GUARDAR"**

**Validaciones Automáticas:**
- ✅ Cédula única (no puede repetirse)
- ✅ Formato de correo válido
- ✅ Teléfono con 10 dígitos
- ✅ Mínimo 18 años al momento de la solicitud

---

#### 3️⃣ **VERIFICAR REQUISITOS**

*(Misma funcionalidad que en Menú Analista - ver sección correspondiente)*

#### 4️⃣ **REGISTRO DE EXÁMENES**

*(Misma funcionalidad que en Menú Analista - ver sección correspondiente)*

#### 5️⃣ **GENERAR LICENCIA**

*(Misma funcionalidad que en Menú Analista - ver sección correspondiente)*

#### 6️⃣ **VER TRÁMITES**

**Propósito**: Visualizar y filtrar todos los trámites del sistema con sus detalles completos.

*(Misma interfaz que en Menú Analista)*

#### 7️⃣ **REPORTES**

**Propósito**: Generar reportes estadísticos y analíticos del sistema.

**Funcionalidades Disponibles:**
- Reporte de trámites por estado
- Reporte de solicitantes registrados
- Reporte de licencias emitidas
- Estadísticas de aprobación/rechazo

---

## 👨‍💻 Menú Analista

### Acceso
Solo usuarios con rol **ANALISTA** pueden acceder.

### Vista General

```
┌─────────────────────────────────────────────┐
│   PANEL DE ANALISTA                         │
│   Bienvenido: analista01                    │
├─────────────────────────────────────────────┤
│                                             │
│  [ 📊 REGISTRO DE EXÁMENES ]               │
│  [ ✓ VERIFICAR REQUISITOS ]                │
│  [ 📄 GENERAR LICENCIA ]                   │
│  [ 📋 VER TRÁMITES ]                       │
│                                             │
│  [ 🚪 CERRAR SESIÓN ]                      │
│                                             │
└─────────────────────────────────────────────┘
```

### Opciones del Menú Analista

---

## 🪟 Ventanas de Funcionalidad

### 📊 Ventana: Registro de Exámenes

**Propósito**: Ingresa las calificaciones teóricas y prácticas de los exámenes de los solicitantes.

**Interfaz:**

```
┌────────────────────────────────────────┐
│  REGISTRO DE EXÁMENES                  │
├────────────────────────────────────────┤
│ Filtro: [______________]               │
│                                        │
│ Tabla de Espera:                       │
│ ┌──────────────────────────────────┐  │
│ │ Cédula   │ Nombre               │  │
│ ├──────────────────────────────────┤  │
│ │ 1234567  │ Juan García          │  │
│ │ 2345678  │ María López          │  │
│ │ 3456789  │ Carlos Rodríguez     │  │
│ └──────────────────────────────────┘  │
│                                        │
│ ─── SOLICITANTE SELECCIONADO ───       │
│ Nombre: ________________                │
│ Tipo:   Licencia B                     │
│ Promedio: 0.00                         │
│ Estado: EN_EXAMENES                    │
│                                        │
│ Nota Teórica:   [___] (0-100)          │
│ Nota Práctica:  [___] (0-100)          │
│ Observaciones:  [_____________]        │
│                                        │
│ [GUARDAR] [LIMPIAR] [REGRESAR]         │
│ [ACTUALIZAR LISTA]                     │
└────────────────────────────────────────┘
```

**Procedimiento:**

1. **Busque el solicitante** usando el filtro (cédula o nombre)
2. **Haga clic en la fila** para seleccionarlo
3. **Ingrese nota teórica** (0-100)
4. **Ingrese nota práctica** (0-100)
5. Opcionalmente agregue **observaciones**
6. **Haga clic en "GUARDAR"**

**Validaciones:**
- ✅ Ambas notas entre 0-100
- ✅ El solicitante debe existir
- ✅ No puede tener notas duplicadas

**Resultado:**
- Si ambas notas ≥ 60 → **APROBADO**
- Si alguna nota < 60 → **REPROBADO**

**Notas Reprobadas:**
- Se mostrarán con fondo **ROJO** en la tabla
- El solicitante puede reintentar examen
- Se actualiza su estado a "REPROBADO"

---

### ✓ Ventana: Verificar Requisitos

**Propósito**: Validar que los solicitantes cumplan con todos los requisitos previos.

**Interfaz:**

```
┌────────────────────────────────────────────┐
│  VERIFICAR REQUISITOS                      │
├────────────────────────────────────────────┤
│ Buscar: [______________]                   │
│                                            │
│ Lista de Pendientes:                       │
│ ┌────────────────────────────────────────┐│
│ │ 1234567 - Juan García (ROJO si REQS)  ││
│ │ 2345678 - María López                  ││
│ └────────────────────────────────────────┘│
│                                            │
│ ─── SOLICITANTE ACTUAL ───                 │
│ Solicitante: Juan García                   │
│                                            │
│ ─── REQUISITOS ───                         │
│ [✓] Certificado Médico Válido             │
│ [ ] Pago de Tasa Confirmado               │
│ [✓] Sin Multas de Tránsito                │
│                                            │
│ Observaciones:                             │
│ [_________________________________]        │
│ [_________________________________]        │
│                                            │
│ [APROBAR] [RECHAZAR] [REGRESAR]            │
│                                            │
└────────────────────────────────────────────┘
```

**Requisitos a Validar:**

| Requisito | Significado | Validación |
|-----------|-------------|-----------|
| **Certificado Médico** | Debe presentar examen médico válido | ✓ Completado / ✗ Pendiente |
| **Pago de Tasa** | Debe confirmar pago de solicitud | ✓ Completado / ✗ Pendiente |
| **Sin Multas** | Debe NO tener multas activas | ✓ Sin multas / ✗ Con multas |

**Procedimiento:**

1. **Busque el solicitante** usando el filtro
2. **Haga clic en su nombre** en la lista
3. **Marque los checkboxes** de requisitos completados
4. **Agregue observaciones** si es necesario
5. **Haga clic en "APROBAR"** o **"RECHAZAR"**

**Acciones Disponibles:**

- **APROBAR**: Todos los requisitos se cumplen ✓
  - Tramite pasa a estado "REQUISITOS_OK"
  
- **RECHAZAR**: No cumple requisitos ✗
  - Tramite vuelve a estado "RECHAZADO"
  - Se registra motivo en observaciones

---

### 📄 Ventana: Generar Licencia

**Propósito**: Emitir el documento oficial de licencia de conducción.

**Interfaz:**

```
┌────────────────────────────────────────────┐
│  GENERAR LICENCIA                          │
├────────────────────────────────────────────┤
│                                            │
│ Buscar Cédula: [______________]            │
│                                            │
│ ┌──────────────────────────────────────┐  │
│ │      LICENCIA DE CONDUCCIÓN          │  │
│ │                                      │  │
│ │  Número: LIC-001234567              │  │
│ │  Nombre: JUAN GARCÍA                │  │
│ │  Tipo:   B                          │  │
│ │  Válida hasta: 2029-01-11           │  │
│ │                                      │  │
│ │  [Espacio para foto]                │  │
│ │                                      │  │
│ └──────────────────────────────────────┘  │
│                                            │
│ [IMPRIMIR LICENCIA]                        │
│                                            │
└────────────────────────────────────────────┘
```

**Requisitos Previos para Emitir:**

Todos los siguientes deben cumplirse:

- ✅ Estado del trámite: **APROBADO**
- ✅ Certificado médico completado
- ✅ Pago de tasa confirmado
- ✅ Sin multas activas
- ✅ Nota teórica ≥ 60
- ✅ Nota práctica ≥ 60

**Procedimiento:**

1. **Ingrese la cédula** del solicitante
2. **Presione Enter** o haga clic fuera del campo
3. El sistema **valida todos los requisitos**
4. Si todo es correcto, se **visualiza la licencia**
5. Haga clic en **"IMPRIMIR LICENCIA"**

**Opciones de Impresión:**

- Imprimir directamente a impresora
- Guardar como PDF
- Ver previa antes de imprimir

**Datos Incluidos en la Licencia:**
- Número único de licencia
- Datos completos del solicitante
- Tipo de licencia
- Fecha de emisión
- Fecha de vencimiento (5 años)
- Foto del solicitante (si disponible)

---

### 📋 Ventana: Ver Trámites (Bandeja de Gestión)

**Propósito**: Ver y filtrar todos los trámites del sistema con sus detalles completos.

**Interfaz:**

```
┌────────────────────────────────────────────────────┐
│  BANDEJA DE GESTIÓN DE TRÁMITES                    │
├──────────────────────┬───────────────────────────┤
│ LISTA (Izquierda)    │ DETALLES (Derecha)        │
├──────────────────────┼───────────────────────────┤
│ Filtro:              │                           │
│ [Cédula o Nombre_]   │ ─── SOLICITANTE ───       │
│                      │                           │
│ ┌────────────────┐   │ Nombre: Juan García       │
│ │ Cédula│Nombre  │   │ Cédula: 1234567-0        │
│ ├────────────────┤   │ Correo: juan@mail.com    │
│ │1234│Juan García│   │ Tipo:   Licencia B       │
│ │2345│María López│   │ Estado: APROBADO         │
│ │3456│Carlos Rdz │   │                           │
│ └────────────────┘   │ ─── REQUISITOS ───       │
│                      │                           │
│ [ACTUALIZAR]         │ ✓ Certificado Médico    │
│ [CERRAR]             │ ✓ Pago Confirmado       │
│                      │ ✓ Sin Multas             │
│                      │ Obs: ...                 │
│                      │                           │
│                      │ ─── EVALUACIÓN ───       │
│                      │ Teórica: 75.50           │
│                      │ Práctica: 82.00          │
│                      │                           │
│                      │ ─── LICENCIA ───         │
│                      │ Número: LIC-001234       │
│                      │ Válida: 2024-2029        │
│                      │                           │
│                      │ [EMITIR LICENCIA]        │
│                      │ [CERRAR VENTANA]         │
└──────────────────────┴───────────────────────────┘
```

**Panel Izquierdo: Lista de Trámites**

**Filtro Rápido:**
- Escriba cédula o nombre
- Filtra en **tiempo real**
- Muestra solo coincidencias

**Tabla:**
- Cédula: ID del solicitante
- Nombre: Nombre completo
- Estado: Estado actual del trámite

**Panel Derecho: Detalles del Trámite**

Se actualiza al seleccionar un trámite.

**Secciones:**

1. **Encabezado de Solicitante**
   - Nombre (grande)
   - Cédula y correo
   - Tipo de licencia
   - Estado actual

2. **Requisitos**
   - Certificado médico
   - Pago tasa
   - Validación de multas
   - Observaciones

3. **Notas de Evaluación**
   - Nota teórica
   - Nota práctica
   - Promedio

4. **Información de Licencia**
   - Número de licencia
   - Fechas de validez

**Botones de Acción:**

- **EMITIR LICENCIA**: Genera y prepara para imprimir (solo si APROBADO)
- **CERRAR VENTANA**: Cierra sin cambiar datos

---

### 📊 Ventana: Reportes

**Propósito**: Generar reportes analíticos del sistema.

**Funcionalidades:**

1. **Reportes de Trámites**
   - Por estado (PENDIENTE, APROBADO, RECHAZADO, etc.)
   - Por tipo de licencia
   - Por rango de fechas

2. **Reportes de Solicitudes**
   - Total de solicitantes
   - Nuevos registros por período
   - Tasa de aprobación

3. **Reportes de Licencias**
   - Licencias emitidas
   - Por vencer
   - Vigentes

---

## Guía de Flujos

### Flujo 1: Registro de un Nuevo Solicitante

```
1. Admin accede → Menú Administrador
    ↓
2. Click en "REGISTRAR SOLICITANTES"
    ↓
3. Completa formulario:
   - Cédula (única)
   - Nombre completo
   - Correo válido
   - Teléfono (10 dígitos)
   - Fecha nacimiento (mayor 18 años)
   - Tipo de licencia (A, B, C, D, E)
    ↓
4. Click "GUARDAR"
    ↓
5. ✅ Solicitante registrado
   - Se crea trámite automático (PENDIENTE)
   - Entra en cola de procesamiento
```

### Flujo 2: Evaluación Completa de un Trámite

```
1. Analista inicia sesión → Menú Analista
    ↓
2. PASO 1: Verificar Requisitos
   - Accede a "VERIFICAR REQUISITOS"
   - Busca solicitante
   - Marca requisitos completados
   - Aprueba requisitos
    ↓
3. PASO 2: Registrar Exámenes
   - Accede a "REGISTRO DE EXÁMENES"
   - Busca solicitante
   - Ingresa nota teórica
   - Ingresa nota práctica
   - Si ambas ≥ 60 → APROBADO
    ↓
4. PASO 3: Emitir Licencia
   - Accede a "GENERAR LICENCIA"
   - Busca cédula del solicitante
   - Sistema valida todos requisitos
   - Click "IMPRIMIR LICENCIA"
    ↓
5. ✅ Licencia generada y lista para entregar
```

### Flujo 3: Rechazo por Requisitos Incompletos

```
1. Analista verifica requisitos
    ↓
2. Algún requisito NO está cumplido
    ↓
3. Click "RECHAZAR"
    ↓
4. Tramite vuelve a estado RECHAZADO
    ↓
5. Solicitante debe completar requisito faltante
    ↓
6. Se revisa nuevamente
```

### Flujo 4: Reprobación de Examen

```
1. Analista registra examen
    ↓
2. Nota teórica < 60 ó Nota práctica < 60
    ↓
3. Tramite pasa a estado REPROBADO
    ↓
4. Sistema marca en rojo en tabla
    ↓
5. Solicitante puede reintentar examen
    ↓
6. Si aprueba segunda vez → APROBADO
```

---

## Consejos y Troubleshooting

### ✅ Consejos de Uso Efectivo

1. **Búsqueda Rápida**
   - Use filtros para encontrar rápidamente
   - Funciona con cédula completa o parcial
   - Nombre: presione Ctrl+F para buscar

2. **Validación de Datos**
   - Revise dos veces antes de guardar
   - Los datos no se pueden editar después
   - Cédula y correo deben ser únicos

3. **Eficiencia en Proceso**
   - Procese requisitos primero
   - Luego registre exámenes
   - Finalmente emita licencias
   - Esto optimiza el flujo

4. **Seguridad**
   - No comparta credenciales
   - Cierre sesión siempre
   - Bloquee la PC cuando se ausente

### ⚠️ Problemas Comunes y Soluciones

| Problema | Causa | Solución |
|----------|-------|----------|
| **"Campos Incompletos"** en Login | Falta ingresar usuario, contraseña o rol | Complete todos los campos |
| **"El usuario no existe"** | Usuario no registrado | Verifique el nombre de usuario |
| **Cuenta bloqueada** | 3 intentos fallidos | Contacte al administrador |
| **"Cédula ya existe"** al registrar | Solicitante ya registrado | Verifique cédula ingresada |
| **Correo rechazado** | Formato inválido | Use formato: usuario@dominio.com |
| **"Botón IMPRIMIR deshabilitado"** | Requisitos no completados | Verifique: médico ✓, pago ✓, sin multas ✓, notas ≥ 60 |
| **No carga tabla de trámites** | Problema de conexión BD | Verifique conexión a internet |
| **Fecha bloqueada en calendario** | Menor de 18 años | Seleccione fecha de alguien mayor de 18 |

### 🔧 Acciones Recomendadas

**Si la aplicación falla:**
1. Cierre la aplicación
2. Verifique conexión a internet
3. Ejecute nuevamente

**Si no puede acceder:**
1. Verifique datos de usuario
2. Cuente los intentos fallidos
3. Contacte al administrador si está bloqueado

**Si no ve cambios guardados:**
1. Recargue la lista (botón ACTUALIZAR)
2. Cierre y abra ventana nuevamente
3. Reinicie la aplicación

---

## 📞 Soporte Técnico

- **Administrador del Sistema**: Contactar para bloqueos de cuenta
- **Soporte Técnico**: Para problemas de conexión o errores
- **Manual Técnico**: Consulte MANUAL_TECNICO.md para arquitectura del sistema

---

## 🎓 Glosario de Términos

| Término | Definición |
|---------|-----------|
| **Trámite** | Solicitud de licencia de un solicitante |
| **Solicitante** | Persona que solicita licencia |
| **Requisito** | Documento o cumplimiento previo (médico, pago, multas) |
| **Nota Teórica** | Calificación del examen escrito |
| **Nota Práctica** | Calificación del examen en carretera |
| **Estado** | Situación actual del trámite (PENDIENTE, APROBADO, etc.) |
| **Licencia** | Documento oficial de conducción |
| **Vigencia** | Período de validez de la licencia (5 años) |
| **BCrypt** | Encriptación segura de contraseñas |
| **Rol** | Tipo de usuario (ADMIN, ANALISTA) |

---

**Versión**: 1.0  
**Última actualización**: Enero 2026  
**Autor**: Equipo de Desarrollo

