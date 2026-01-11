-- ============================================
-- ESQUEMA: Sistema de Gestión de Licencias
-- Base de datos: PostgreSQL
-- ============================================

-- =========================
-- TABLA: usuarios
-- =========================
CREATE TABLE public.usuarios (
    id SERIAL PRIMARY KEY,
    cedula VARCHAR NOT NULL UNIQUE,
    nombre VARCHAR NOT NULL,
    username VARCHAR NOT NULL UNIQUE,
    password VARCHAR NOT NULL,
    rol VARCHAR NOT NULL
        CHECK (rol IN ('ADMIN', 'ANALISTA')),
    estado BOOLEAN DEFAULT TRUE,
    intentos_fallidos INTEGER DEFAULT 0
);

-- =========================
-- TABLA: solicitantes
-- =========================
CREATE TABLE public.solicitantes (
    id SERIAL PRIMARY KEY,
    cedula VARCHAR NOT NULL UNIQUE,
    nombre VARCHAR NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    telefono VARCHAR NOT NULL,
    correo VARCHAR NOT NULL
);

-- =========================
-- TABLA: tramites
-- =========================
CREATE TABLE public.tramites (
    id SERIAL PRIMARY KEY,
    solicitante_id INTEGER UNIQUE,
    usuario_id INTEGER,
    tipo_licencia VARCHAR NOT NULL,
    fecha_solicitud TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR DEFAULT 'PENDIENTE'
        CHECK (
            estado IN (
                'PENDIENTE',
                'REQUISITOS',
                'EN_EXAMENES',
                'APROBADO',
                'REPROBADO',
                'RECHAZADO',
                'LICENCIA_EMITIDA'
            )
        ),
    req_medico BOOLEAN DEFAULT FALSE,
    req_pago BOOLEAN DEFAULT FALSE,
    req_multas BOOLEAN DEFAULT FALSE,
    nota_teorica NUMERIC DEFAULT 0,
    nota_practica NUMERIC DEFAULT 0,
    observaciones TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_tramite_solicitante
        FOREIGN KEY (solicitante_id) REFERENCES public.solicitantes(id),
    CONSTRAINT fk_tramite_usuario
        FOREIGN KEY (usuario_id) REFERENCES public.usuarios(id)
);

-- =========================
-- TABLA: licencias
-- =========================
CREATE TABLE public.licencias (
    id SERIAL PRIMARY KEY,
    tramite_id INTEGER UNIQUE,
    numero_licencia VARCHAR NOT NULL UNIQUE,
    fecha_emision DATE DEFAULT CURRENT_DATE,
    fecha_vencimiento DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by INTEGER,
    CONSTRAINT fk_licencia_tramite
        FOREIGN KEY (tramite_id) REFERENCES public.tramites(id),
    CONSTRAINT fk_licencia_usuario
        FOREIGN KEY (created_by) REFERENCES public.usuarios(id)
);

-- =========================
-- USUARIO ADMIN POR DEFECTO
-- =========================
INSERT INTO public.usuarios (
    cedula,
    nombre,
    username,
    password,
    rol
) VALUES (
    '0000000000',
    'Administrador',
    'admin',
    'admin',
    'ADMIN'
);
