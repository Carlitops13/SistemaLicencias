package com.licencias.dao.impl;

import com.licencias.dao.ISolicitanteDAO;
import com.licencias.model.Solicitante;
import com.licencias.util.ConexionBD;
import java.sql.*;

public class SolicitanteDAOImpl implements ISolicitanteDAO {

    @Override
    public Solicitante buscarPorCedula(String cedula) {
        String sql = "SELECT * FROM solicitantes WHERE cedula = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cedula);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Solicitante(
                        rs.getInt("id"),
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getDate("fecha_nacimiento").toLocalDate(),
                        rs.getString("telefono"),
                        rs.getString("correo")
                );
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null; // No existe
    }

    @Override
    public void crear(Solicitante s) {
        String sql = "INSERT INTO solicitantes (cedula, nombre, fecha_nacimiento, telefono, correo) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, s.getCedula());
            ps.setString(2, s.getNombre());
            ps.setDate(3, java.sql.Date.valueOf(s.getFechaNacimiento()));
            ps.setString(4, s.getTelefono());
            ps.setString(5, s.getCorreo());

            int affected = ps.executeUpdate();

            // Obtener el ID generado automáticamente
            if (affected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    s.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al crear solicitante: " + e.getMessage());
        }
    }
}