package com.licencias.dao;

import com.licencias.model.Solicitante;

public interface ISolicitanteDAO {
    Solicitante buscarPorCedula(String cedula);

    void crear(Solicitante s);
}
