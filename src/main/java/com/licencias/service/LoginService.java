package com.licencias.service;

import com.licencias.dao.IUsuarioDAO;
import com.licencias.dao.impl.UsuarioDAOImpl;
import com.licencias.model.Usuario;
import org.mindrot.jbcrypt.BCrypt;

public class LoginService {

    private final IUsuarioDAO usuarioDAO;

    public LoginService() {

        this.usuarioDAO = new UsuarioDAOImpl();
    }


    public Usuario encontrarUsuario(String username) {
        if (username == null || username.trim().isEmpty()) return null;
        // Llama al método que agregamos en tu UsuarioDAOImpl
        return usuarioDAO.buscarPorUsername(username.trim());
    }

    // Valida si la contraseña coincide usando BCrypt.

    public boolean verificarPassword(String passwordPlana, String passwordHash) {
        if (passwordHash == null || !passwordHash.startsWith("$2a$")) {
            return false;
        }
        try {
            return BCrypt.checkpw(passwordPlana, passwordHash);
        } catch (Exception e) {
            return false;
        }
    }


    public Usuario autenticar(String username, String passwordPlana) {
        Usuario u = usuarioDAO.obtenerPorUsername(username);
        if (u != null && verificarPassword(passwordPlana, u.getPassword())) {
            return u;
        }
        return null;
    }
}