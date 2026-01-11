package com.licencias.model;

import java.time.LocalDateTime;

public class Tramite {
    private int id;
    private Solicitante solicitante; // Relación con Solicitante
    private Usuario usuario;         // Relación con Usuario (Analista)
    private String tipoLicencia;
    private LocalDateTime fechaSolicitud;
    private String estado;

    public boolean isReqMedico() {

        return reqMedico;
    }

    public void setReqMedico(boolean reqMedico) {

        this.reqMedico = reqMedico;
    }

    public boolean isReqPago() {

        return reqPago;
    }

    public void setReqPago(boolean reqPago) {

        this.reqPago = reqPago;
    }

    public boolean isReqMultas() {

        return reqMultas;
    }

    public void setReqMultas(boolean reqMultas) {

        this.reqMultas = reqMultas;
    }

    public double getNotaTeorica() {

        return notaTeorica;
    }

    public void setNotaTeorica(double notaTeorica) {

        this.notaTeorica = notaTeorica;
    }

    public double getNotaPractica() {

        return notaPractica;
    }

    public void setNotaPractica(double notaPractica) {

        this.notaPractica = notaPractica;
    }

    public String getObservaciones() {

        return observaciones;
    }

    public void setObservaciones(String observaciones) {

        this.observaciones = observaciones;
    }

    // Requisitos
    private boolean reqMedico;
    private boolean reqPago;
    private boolean reqMultas;

    // Notas
    private double notaTeorica;
    private double notaPractica;
    private String observaciones;

    public Tramite() {
        this.fechaSolicitud = LocalDateTime.now();
        this.estado = "PENDIENTE";
    }



    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Solicitante getSolicitante() {
        return solicitante;
    }
    public void setSolicitante(Solicitante solicitante) {
        this.solicitante = solicitante;
    }
    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    public String getTipoLicencia() {
        return tipoLicencia;
    }
    public void setTipoLicencia(String tipoLicencia) {
        this.tipoLicencia = tipoLicencia;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public LocalDateTime getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(LocalDateTime fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }




}