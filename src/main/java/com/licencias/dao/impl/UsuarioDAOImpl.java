package com.licencias.dao.impl;

import com.licencias.dao.IUsuarioDAO;
import com.licencias.model.Usuario;
import com.licencias.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAOImpl implements IUsuarioDAO {


    @Override
    public Usuario obtenerPorUsername(String username) {
        String sql = "SELECT * FROM usuarios WHERE username = ? AND estado = true";
        return ejecutarBusqueda(sql, username);
    }


    @Override
    public Usuario buscarPorUsername(String username) {
        String sql = "SELECT * FROM usuarios WHERE username = ?";
        return ejecutarBusqueda(sql, username);
    }


    private Usuario ejecutarBusqueda(String sql, String parametro) {
        Usuario usuario = null;
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, parametro);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setId(rs.getInt("id"));
                    usuario.setCedula(rs.getString("cedula"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setUsername(rs.getString("username"));
                    usuario.setPassword(rs.getString("password"));
                    usuario.setRol(rs.getString("rol"));
                    usuario.setEstado(rs.getBoolean("estado"));
                    // Columna nueva para el control de bloqueos
                    usuario.setIntentosFallidos(rs.getInt("intentos_fallidos"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en búsqueda de usuario: " + e.getMessage());
        }
        return usuario;
    }

    @Override
    public boolean crear(Usuario u) {

        String sql = "INSERT INTO usuarios (cedula, nombre, username, password, rol, estado, intentos_fallidos) VALUES (?, ?, ?, ?, ?, ?, 0)";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, u.getCedula());
            ps.setString(2, u.getNombre());
            ps.setString(3, u.getUsername());
            ps.setString(4, u.getPassword());
            ps.setString(5, u.getRol());
            ps.setBoolean(6, u.isEstado());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al crear el usuario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizar(Usuario u) {

        boolean cambiarPassword = u.getPassword() != null && !u.getPassword().isEmpty();
        String sql;

        if (cambiarPassword) {
            sql = "UPDATE usuarios SET nombre=?, username=?, rol=?, estado=?, intentos_fallidos=?, password=? WHERE id=?";
        } else {
            sql = "UPDATE usuarios SET nombre=?, username=?, rol=?, estado=?, intentos_fallidos=? WHERE id=?";
        }

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, u.getNombre());
            ps.setString(2, u.getUsername());
            ps.setString(3, u.getRol());
            ps.setBoolean(4, u.isEstado());
            ps.setInt(5, u.getIntentosFallidos()); // Importante para resetear a 0 al desbloquear

            if (cambiarPassword) {
                ps.setString(6, u.getPassword());
                ps.setInt(7, u.getId());
            } else {
                ps.setInt(6, u.getId());
            }

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }


    @Override
    public boolean actualizarIntentos(Usuario u) {
        String sql = "UPDATE usuarios SET intentos_fallidos = ? WHERE id = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, u.getIntentosFallidos());
            ps.setInt(2, u.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar intentos: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY id ASC";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setCedula(rs.getString("cedula"));
                u.setNombre(rs.getString("nombre"));
                u.setUsername(rs.getString("username"));
                u.setRol(rs.getString("rol"));
                u.setEstado(rs.getBoolean("estado"));
                u.setIntentosFallidos(rs.getInt("intentos_fallidos"));
                lista.add(u);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
        }
        return lista;
    }
}