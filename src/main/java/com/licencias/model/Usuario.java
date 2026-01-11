package com.licencias.model;

public class Usuario {

    private int id;
    private String cedula;
    private String nombre;
    private String username;
    private String password;
    private String rol;
    private boolean estado;

    private int intentosFallidos;


    public int getIntentosFallidos() {
        return intentosFallidos;
    }
    public void setIntentosFallidos(int intentosFallidos) {
        this.intentosFallidos = intentosFallidos;
    }
    public Usuario() {
    }


    public Usuario(int id, String cedula, String nombre, String username, String password, String rol, boolean estado) {
        this.id = id;
        this.cedula = cedula;
        this.nombre = nombre;
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.estado = estado;
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

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }
    public void setRol(String rol) {
        this.rol = rol;
    }

    public boolean isEstado() {
        return estado;
    }
    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}