package com.licencias.model;

import java.time.LocalDate;

public class Licencia {
    private int id;
    private int tramiteId;
    private String numeroLicencia;
    private LocalDate fechaEmision;
    private LocalDate fechaVencimiento;

    public Licencia() {
    }

    public Licencia(int id, int tramiteId, String numeroLicencia, LocalDate fechaEmision, LocalDate fechaVencimiento) {
        this.id = id;
        this.tramiteId = tramiteId;
        this.numeroLicencia = numeroLicencia;
        this.fechaEmision = fechaEmision;
        this.fechaVencimiento = fechaVencimiento;
    }

    // --- Getters y Setters ---

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getTramiteId() {
        return tramiteId;
    }

    public String getNumeroLicencia() {
        return numeroLicencia;
    }
    public void setNumeroLicencia(String numeroLicencia) {
        this.numeroLicencia = numeroLicencia;
    }

    public LocalDate getFechaEmision() {
        return fechaEmision;
    }
    public void setFechaEmision(LocalDate fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }
    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
}