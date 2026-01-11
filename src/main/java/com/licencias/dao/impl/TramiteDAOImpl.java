package com.licencias.dao.impl;

import com.licencias.dao.ITramiteDAO;
import com.licencias.model.Solicitante;
import com.licencias.model.Tramite;
import com.licencias.util.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TramiteDAOImpl implements ITramiteDAO {

    @Override
    public boolean crear(Tramite t) {
        String sql = "INSERT INTO tramites (solicitante_id, usuario_id, tipo_licencia, estado) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, t.getSolicitante().getId());
            ps.setInt(2, t.getUsuario() != null ? t.getUsuario().getId() : 1);
            ps.setString(3, t.getTipoLicencia());
            ps.setString(4, "PENDIENTE");

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al crear trámite: " + e.getMessage());
            return false;
        }
    }


    @Override
    public List<Tramite> listarTodos() {
        List<Tramite> lista = new ArrayList<>();

        String sql = "SELECT t.id, t.tipo_licencia, t.fecha_solicitud, t.estado, " +
                "t.req_medico, t.req_pago, t.req_multas, t.observaciones, " +
                "s.id as sol_id, s.cedula, s.nombre " +
                "FROM tramites t " +
                "INNER JOIN solicitantes s ON t.solicitante_id = s.id " +
                "ORDER BY t.fecha_solicitud DESC";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Tramite t = new Tramite();
                t.setId(rs.getInt("id"));
                t.setTipoLicencia(rs.getString("tipo_licencia"));
                t.setEstado(rs.getString("estado"));


                t.setReqMedico(rs.getBoolean("req_medico"));
                t.setReqPago(rs.getBoolean("req_pago"));
                t.setReqMultas(rs.getBoolean("req_multas"));
                t.setObservaciones(rs.getString("observaciones"));

                java.sql.Timestamp ts = rs.getTimestamp("fecha_solicitud");
                if (ts != null) t.setFechaSolicitud(ts.toLocalDateTime());

                Solicitante s = new Solicitante();
                s.setId(rs.getInt("sol_id"));
                s.setCedula(rs.getString("cedula"));
                s.setNombre(rs.getString("nombre"));

                t.setSolicitante(s);
                lista.add(t);
            }
        } catch (SQLException e) {
            System.err.println(" ERROR SQL en listarTodos: " + e.getMessage());
        }
        return lista;
    }


    @Override
    public Tramite buscarPorCedula(String cedula) {

        String sql = "SELECT t.*, s.nombre as nombre_s, s.correo, s.telefono " +
                "FROM tramites t " +
                "JOIN solicitantes s ON t.solicitante_id = s.id " +
                "WHERE s.cedula = ? " +
                "ORDER BY t.fecha_solicitud DESC LIMIT 1";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cedula);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Tramite t = new Tramite();
                t.setId(rs.getInt("id"));
                t.setEstado(rs.getString("estado"));
                t.setReqMedico(rs.getBoolean("req_medico"));
                t.setReqPago(rs.getBoolean("req_pago"));
                t.setReqMultas(rs.getBoolean("req_multas"));
                t.setNotaTeorica(rs.getDouble("nota_teorica"));
                t.setNotaPractica(rs.getDouble("nota_practica"));
                t.setObservaciones(rs.getString("observaciones"));
                t.setTipoLicencia(rs.getString("tipo_licencia"));

                Solicitante s = new Solicitante();
                s.setCedula(cedula);
                s.setNombre(rs.getString("nombre_s"));
                s.setCorreo(rs.getString("correo"));
                t.setSolicitante(s);

                return t;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean actualizarRequisitos(Tramite t) {

        String sql = "UPDATE tramites SET req_medico=?, req_pago=?, req_multas=?, observaciones=?, estado=? WHERE id=?";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, t.isReqMedico());
            ps.setBoolean(2, t.isReqPago());
            ps.setBoolean(3, t.isReqMultas());
            ps.setString(4, t.getObservaciones()); // <--- NUEVO CAMPO
            ps.setString(5, t.getEstado());
            ps.setInt(6, t.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizarNotas(Tramite t) {
        String sql = "UPDATE tramites SET nota_teorica = ?, nota_practica = ?, estado = ? WHERE id = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, t.getNotaTeorica());
            ps.setDouble(2, t.getNotaPractica());
            ps.setString(3, t.getEstado());
            ps.setInt(4, t.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean finalizarTramite(int idTramite) {

        String sqlLicencia = "INSERT INTO licencias (tramite_id, numero_licencia, fecha_emision, fecha_vencimiento) " +
                "SELECT t.id, s.cedula, CURRENT_DATE, (CURRENT_DATE + INTERVAL '5 years') " +
                "FROM tramites t JOIN solicitantes s ON t.solicitante_id = s.id " +
                "WHERE t.id = ? AND NOT EXISTS (SELECT 1 FROM licencias WHERE tramite_id = ?)";


        String sqlEstado = "UPDATE tramites SET estado = 'LICENCIA_EMITIDA' WHERE id = ?";

        try (Connection conn = ConexionBD.obtenerConexion()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps1 = conn.prepareStatement(sqlLicencia);
                 PreparedStatement ps2 = conn.prepareStatement(sqlEstado)) {

                ps1.setInt(1, idTramite);
                ps1.setInt(2, idTramite);
                ps1.executeUpdate();

                ps2.setInt(1, idTramite);
                ps2.executeUpdate();

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



}