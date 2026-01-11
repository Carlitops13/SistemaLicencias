package com.licencias.dao;

import com.licencias.model.Usuario;

import java.util.List;

public interface IUsuarioDAO {
    Usuario obtenerPorUsername(String username);


    Usuario buscarPorUsername(String username);

    boolean crear(Usuario u);

    boolean actualizar(Usuario u);

    boolean actualizarIntentos(Usuario u);

    List<Usuario> listarTodos();
}
