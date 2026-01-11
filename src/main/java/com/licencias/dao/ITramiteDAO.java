package com.licencias.dao;

import com.licencias.model.Tramite;

import java.util.List;

public interface ITramiteDAO {
    boolean crear(Tramite t);
    List<Tramite> listarTodos();

    Tramite buscarPorCedula(String cedula);
    boolean actualizarRequisitos(Tramite t);


    boolean actualizarNotas(Tramite t);

    boolean finalizarTramite(int idTramite);
}
