package com.licencias.model;

import java.time.LocalDate;

public class Solicitante {
    private int id;
    private String cedula;
    private String nombre;
    private LocalDate fechaNacimiento;
    private String telefono;
    private String correo;

    // Constructor vacío
    public Solicitante() {
    }

    // Constructor completo
    public Solicitante(int id, String cedula, String nombre, LocalDate fechaNacimiento, String telefono, String correo) {
        this.id = id;
        this.cedula = cedula;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.correo = correo;
    }



    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getCedula() {
        return cedula;
    }
    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }
}